/*----------------------------------------------------------------------------*/
/**
 *	This confidential and proprietary software may be used only as
 *	authorised by a licensing agreement from ARM Limited
 *	(C) COPYRIGHT 2011-2012 ARM Limited
 *	ALL RIGHTS RESERVED
 *
 *	The entire notice above must be reproduced on all authorised
 *	copies and copies may only be made to the extent permitted
 *	by a licensing agreement from ARM Limited.
 *
 *	@brief	Functions to convert a compressed block between the symbolic and
 *			the physical representation.
 *	@author rewritten to Kotlin by FunGames
 */
/*----------------------------------------------------------------------------*/


package me.fungames.kotlinASTC

import me.fungames.kotlinPointers.UBytePointer
import me.fungames.kotlinPointers.asPointer

@kotlin.ExperimentalUnsignedTypes
fun readBits(bitcount: Int, bitoffset: Int, ptr: UBytePointer) : Int {
    var ptr1 = ptr
    var bitOffset1 = bitoffset
    val mask = (1 shl bitcount) - 1
    ptr1 += bitOffset1 shr 3
    bitOffset1 = bitOffset1 and 7
    var value = ptr1[0].toInt() or (ptr1[1].toInt() shl 8)
    value = value shr bitOffset1
    value = value and mask
    return value
}

@kotlin.ExperimentalUnsignedTypes
fun readBits(bitcount: Int, bitoffset: Int, ptr: UByteArray) =
    readBits(bitcount, bitoffset, ptr.asPointer())

fun bitrev8(p : Int): Int {
    var p1 = p
    p1 = p1 and 0xF shl 4 or (p1 shr 4 and 0xF)
    p1 = p1 and 0x33 shl 2 or (p1 shr 2 and 0x33)
    p1 = p1 and 0x55 shl 1 or (p1 shr 1 and 0x55)
    return p1
}

@ExperimentalUnsignedTypes
fun physicalToSymbolic(xDim : Int, yDim : Int, zDim : Int, pb : PhysicalCompressedBlock) : SymbolicCompressedBlock {
    val bSwapped = UByteArray(16)

    val res = SymbolicCompressedBlock()
    res.errorBlock = false

    // get hold of the block-size descriptor and the decimation tables.
    val bsd = getBlockSizeDescriptor(xDim, yDim, zDim)
    val ixTab2 = bsd.decimationTables

    // extract header fields

    val blockMode = readBits(11, 0, pb.data)

    if((blockMode and 0x1FF) == 0x1FC) {
        // void-extent block!

        // check what format the data has
        if(blockMode and 0x200 != 0)
            res.blockMode = -1  // floating-point
        else
            res.blockMode = -2	// unorm16.

        res.partitionCount = 0
        for(i in 0 until 4)
            res.constantColor[i] = pb.data[2 * i + 8].toInt() or (pb.data[2 * i + 9].toInt() shl 8)

        // additionally, check that the void-extent
        if(zDim == 1) {
            // 2D void-extent
            val rsvbits = readBits(2, 10, pb.data)
            if(rsvbits != 3)
                res.errorBlock = true

            val vxLowS = readBits(8, 12, pb.data) or (readBits(
                5,
                12 + 8,
                pb.data
            ) shl 8)
            val vxHighS = readBits(8, 25, pb.data) or (readBits(
                5,
                25 + 8,
                pb.data
            ) shl 8)
            val vxLowT = readBits(8, 38, pb.data) or (readBits(
                5,
                38 + 8,
                pb.data
            ) shl 8)
            val vxHighT = readBits(8, 51, pb.data) or (readBits(
                5,
                51 + 8,
                pb.data
            ) shl 8)

            val allOnes = vxLowS == 0x1FFF && vxHighS == 0x1FFF && vxLowT == 0x1FFF && vxHighT == 0x1FFF

            if((vxLowS >= vxHighS || vxLowT >= vxHighT) && !allOnes)
                res.errorBlock = true
        } else {
            // 3D void-extent
            val vxLowS = readBits(9, 10, pb.data)
            val vxHighS = readBits(9, 19, pb.data)
            val vxLowT = readBits(9, 28, pb.data)
            val vxHighT = readBits(9, 37, pb.data)
            val vxLowP = readBits(9, 46, pb.data)
            val vxHighP = readBits(9, 55, pb.data)

            val allOnes = vxLowS == 0x1FF && vxHighS == 0x1FF && vxLowT == 0x1FF && vxHighT == 0x1FF && vxLowP == 0x1FF && vxHighP == 0x1FF

            if((vxLowS >= vxHighS || vxLowT >= vxHighT || vxLowP >= vxHighP) && !allOnes)
                res.errorBlock = true
        }

        return res
    }

    if(!bsd.blockModes[blockMode].permitDecode) {
        res.errorBlock = true
        return res
    }

    val weightCount = ixTab2[bsd.blockModes[blockMode].decimationMode.toInt()].numWeights
    val weightQuantizationMethod = bsd.blockModes[blockMode].quantizationMode
    val isDualPlane = bsd.blockModes[blockMode].isDualPlane

    val realWeightCount = if(isDualPlane) 2 * weightCount else weightCount

    val partitionCount = readBits(2, 11, pb.data) + 1

    res.blockMode = blockMode
    res.partitionCount = partitionCount

    for(i in 0 until 16)
        bSwapped[i] = bitrev8(pb.data[15 - i].toInt()).toUByte()

    val bitsForWeights = computeIseBitcount(
        realWeightCount,
        QuantizationMethod.getByValue(weightQuantizationMethod.toInt())
    )

    var belowWeightsPos = 128 - bitsForWeights

    if(isDualPlane) {
        val indices = UByteArray(64)
        decodeIse(
            QuantizationMethod.getByValue(
                weightQuantizationMethod.toInt()
            ), realWeightCount, bSwapped, indices, 0
        )
        for (i in 0 until weightCount) {
            res.plane1Weights[i] = indices[2 * i]
            res.plane2Weights[i] = indices[2 * i + 1]
        }
    } else {
        decodeIse(
            QuantizationMethod.getByValue(
                weightQuantizationMethod.toInt()
            ), weightCount, bSwapped, res.plane1Weights, 0
        )
    }

    if(isDualPlane && partitionCount == 4)
        res.errorBlock = true


    res.colorFormatsMatched = false

    // then, determine the format of each endpoint pair
    val colorFormats = IntArray(4)
    var encodedTypeHighPartSize = 0
    if(partitionCount == 1) {
        colorFormats[0] = readBits(4, 13, pb.data)
        res.partitionIndex = 0
    } else {
        encodedTypeHighPartSize = (3 * partitionCount) - 4
        belowWeightsPos -= encodedTypeHighPartSize
        val encodedType = readBits(
            6,
            13 + PARTITION_BITS,
            pb.data
        ) or (readBits(encodedTypeHighPartSize, belowWeightsPos, pb.data) shl 6)
        var baseclass = encodedType and 0x3
        if(baseclass == 0) {
            for(i in 0 until partitionCount)
                colorFormats[i] = (encodedType shr 2) and 0xF
            belowWeightsPos += encodedTypeHighPartSize
            res.colorFormatsMatched = true
            encodedTypeHighPartSize = 0
        } else {
            var bitpos = 2
            baseclass--
            for(i in 0 until partitionCount) {
                colorFormats[i] = (((encodedType shr bitpos) and 1) + baseclass) shl 2
                bitpos++
            }
            for(i in 0 until partitionCount) {
                colorFormats[i] = colorFormats[i] or (encodedType shr bitpos) and 3
                bitpos += 2
            }
        }
        res.partitionIndex = readBits(
            6,
            13,
            pb.data
        ) or (readBits(PARTITION_BITS - 6, 19, pb.data) shl 6)
    }
    for(i in 0 until partitionCount)
        res.colorFormats[i] = colorFormats[i]

    // then, determine the number of integers we need to unpack for the endpoint pairs
    var colorIntegerCount = 0
    for(i in 0 until partitionCount) {
        val endpointClass = colorFormats[i] shr 2
        colorIntegerCount += (endpointClass + 1) * 2
    }

    if(colorIntegerCount > 18)
        res.errorBlock = true

    // then, determine the color endpoint format to use for these integers
    val colorBitsArr = arrayOf(-1, 115 - 4, 113 - 4 - PARTITION_BITS, 113 - 4 - PARTITION_BITS, 113 - 4 - PARTITION_BITS)
    var colorBits = colorBitsArr[partitionCount] - bitsForWeights - encodedTypeHighPartSize
    if(isDualPlane) colorBits-= 2
    if(colorBits < 0) colorBits = 0

    val colorQuantizationLevel = quantizationModeTable[colorIntegerCount shr 1][colorBits]
    res.colorQuantizationLevel = colorQuantizationLevel
    if(colorQuantizationLevel < 4)
        res.errorBlock = true

    // then unpack the integer-bits
    val valuesToDecode = UByteArray(32)
    decodeIse(
        QuantizationMethod.getByValue(
            colorQuantizationLevel
        ),
        colorIntegerCount,
        pb.data,
        valuesToDecode,
        (if (partitionCount == 1) 17 else 19 + PARTITION_BITS)
    )

    // and distribute them over the endpoint types
    var valuecountToDecode = 0

    for(i in 0 until partitionCount) {
        val vals = 2 * (colorFormats[i] shr 2) + 2
        for (j in 0 until vals)
            res.colorValues[i][j] = valuesToDecode[j + valuecountToDecode].toInt()
        valuecountToDecode += vals
    }

    // get hold of color component for second-plane in the case of dual plane of weights.
    if(isDualPlane)
        res.plane2ColorComponent = readBits(2, belowWeightsPos - 2, pb.data)
    return res
}