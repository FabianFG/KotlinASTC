package com.fungames.kotlinASTC

fun computeValueOfTexelInt(texelToGet : Int, it : DecimationTable, weights : Array<Int>): Int {
    var summedValue = 8
    val weightsToEvaluate = it.texelNumWeights[texelToGet]
    for (i in 0 until weightsToEvaluate)
        summedValue += weights[it.texelWeights[texelToGet][i]] * it.texelWeightsInt[texelToGet][i]
    return summedValue shr 4
}

fun lerpColorInt(decodeMode: ASTC_DecodeMode, color0 : Vector4, color1 : Vector4, weight : Int, plane2Weight : Int, plane2ColorComponent : Int // -1 in 1-plane mode
    ): Vector4 {
    var ecolor0 = Int4(color0.x, color0.y, color0.z, color0.w)
    var ecolor1 = Int4(color1.x, color1.y, color1.z, color1.w)

    val eweight1 = Int4(weight, weight, weight, weight)
    when (plane2ColorComponent) {
        0 -> eweight1.x = plane2Weight
        1-> eweight1.y = plane2Weight
        2 -> eweight1.z = plane2Weight
        3 -> eweight1.w = plane2Weight
    }

    val eweight0 = Int4(64, 64, 64, 64) - eweight1

    if (decodeMode == ASTC_DecodeMode.DECODE_LDR_SRGB) {
        ecolor0 = ecolor0 shr 8
        ecolor1 = ecolor1 shr 8
    }
    var color = (ecolor0 * eweight0) + (ecolor1 * eweight1) + Int4(32, 32, 32, 32)
    color = color shr 6
    if (decodeMode == ASTC_DecodeMode.DECODE_LDR_SRGB)
        color = color or (color shl 8)

    return Vector4(color.x, color.y, color.z, color.w)
}


@kotlin.ExperimentalUnsignedTypes
fun decompressSymbolicBlock(decodeMode : ASTC_DecodeMode,
                            xDim : Int, yDim : Int, zDim : Int,   // dimensions of block
                            xPos : Int, yPos : Int, zPos : Int,   // position of block
                            scb : SymbolicCompressedBlock) : ImageBlock {
    val blk = ImageBlock()
    blk.xPos = xPos
    blk.yPos = yPos
    blk.zPos = zPos

    // if we detected an error-block, blow up immediately.
    if(scb.errorBlock) {
        if(decodeMode == ASTC_DecodeMode.DECODE_LDR_SRGB) {
            for (i in 0 until xDim * yDim * zDim) {
                blk.origData[4 * i] = 1.0f
                blk.origData[4 * i + 1] = 0.0f
                blk.origData[4 * i + 2] = 1.0f
                blk.origData[4 * i + 3] = 1.0f
                blk.rgbLns[i] = false
                blk.alphaLns[i] = false
                blk.nanTexel[i] = false
            }
        } else {
            for (i in 0 until xDim * yDim * zDim) {
                blk.origData[4 * i] = 0.0f
                blk.origData[4 * i + 1] = 0.0f
                blk.origData[4 * i + 2] = 0.0f
                blk.origData[4 * i + 3] = 0.0f
                blk.rgbLns[i] = false
                blk.alphaLns[i] = false
                blk.nanTexel[i] = false
            }
        }

        imageblockInitializeWorkFromOrig(blk, xDim * yDim * zDim)
        updateImageblockFlags(blk, xDim, yDim, zDim)
        return blk
    }

    if(scb.blockMode < 0) {
        var red = 0f; var green = 0f; var blue = 0f; var alpha = 0f
        var useLns = false
        var useNan = false

        if(scb.blockMode == -2) {
            // For sRGB decoding, we should return only the top 8 bits.
            val mask = if (decodeMode === ASTC_DecodeMode.DECODE_LDR_SRGB) 0xFF00 else 0xFFFF
            red = unorm16ToSf16(scb.constantColor[0] and mask).toFloat16()
            green = unorm16ToSf16(scb.constantColor[1] and mask).toFloat16()
            blue = unorm16ToSf16(scb.constantColor[2] and mask).toFloat16()
            alpha = unorm16ToSf16(scb.constantColor[3] and mask).toFloat16()
            useLns = false
            useNan = false
        } else {
            when (decodeMode) {
                ASTC_DecodeMode.DECODE_LDR_SRGB -> {
                    red = 1.0f
                    green = 0.0f
                    blue = 1.0f
                    alpha = 1.0f
                    useLns = false
                    useNan = false
                }
                ASTC_DecodeMode.DECODE_LDR -> {
                    red = 0.0f
                    green = 0.0f
                    blue = 0.0f
                    alpha = 0.0f
                    useLns = false
                    useNan = true
                }
                ASTC_DecodeMode.DECODE_HDR -> {

                    red = (scb.constantColor[0]).toFloat16()
                    green = (scb.constantColor[1]).toFloat16()
                    blue = (scb.constantColor[2]).toFloat16()
                    alpha = (scb.constantColor[3]).toFloat16()
                    useLns = true
                    useNan = false
                }
            }
        }

        for(i in 0 until xDim * yDim * zDim) {
            blk.origData[4 * i] = red
            blk.origData[4 * i + 1] = green
            blk.origData[4 * i + 2] = blue
            blk.origData[4 * i + 3] = alpha
            blk.rgbLns[i] = useLns
            blk.alphaLns[i] = useLns
            blk.nanTexel[i] = useNan
        }

        imageblockInitializeWorkFromOrig(blk, xDim * yDim * zDim)
        updateImageblockFlags(blk, xDim, yDim, zDim)
        return blk
    }

    // get the appropriate partition-table entry
    val partitionCount = scb.partitionCount
    val pt = getPartitionTable(xDim, yDim, zDim, partitionCount)[scb.partitionIndex]

    // get the appropriate block descriptor
    val bsd = getBlockSizeDescriptor(xDim, yDim, zDim)
    val ixtab2 = bsd.decimationTables

    val it = ixtab2[bsd.blockModes[scb.blockMode].decimationMode]

    val isDualPlane = bsd.blockModes[scb.blockMode].isDualPlane

    val weightQuantizationLevel = bsd.blockModes[scb.blockMode].quantizationMode

    // decode the color endpoints
    val colorEndpoint0 = Array(4) {Vector4(0, 0, 0, 0)}
    val colorEndpoint1 = Array(4) {Vector4(0, 0, 0, 0)}
    val rgbHdrEndpoint = Array(4) {false}
    val alphaHdrEndpoint = Array(4) {false}
    val nanEndpoint = Array(4) {false}

    for (i in 0 until partitionCount) {
        val (output0, output1, rgbHdr, alphaHdr, nan) = unpackColorEndpoints(decodeMode,
            scb.colorFormats[i],
            scb.colorQuantizationLevel, scb.colorValues[i])
        colorEndpoint0[i] = output0
        colorEndpoint1[i] = output1
        rgbHdrEndpoint[i] = rgbHdr
        alphaHdrEndpoint[i] = alphaHdr
        nanEndpoint[i] = nan
    }

    // first unquantize the weights
    val uqPlane1Weights = Array(MAX_WEIGHTS_PER_BLOCK) {0}
    val uqPlane2Weights = Array(MAX_WEIGHTS_PER_BLOCK) {0}
    val weightCount = it.numWeights

    val qat = quantAndXferTables[weightQuantizationLevel]

    for (i in 0 until weightCount)
        uqPlane1Weights[i] = qat.unquantizedValue[scb.plane1Weights[i].toInt()]
    if (isDualPlane)
        for (i in 0 until weightCount)
            uqPlane2Weights[i] = qat.unquantizedValue[scb.plane2Weights[i].toInt()]

    // then undecimate them.
    val weights = Array(MAX_TEXELS_PER_BLOCK) {0}
    val plane2Weights = Array(MAX_TEXELS_PER_BLOCK) {0}

    val texelsPerBlock = xDim * yDim * zDim
    for (i in 0 until texelsPerBlock)
        weights[i] = computeValueOfTexelInt(i, it, uqPlane1Weights)
    if (isDualPlane)
        for (i in 0 until texelsPerBlock)
            plane2Weights[i] = computeValueOfTexelInt(i, it, uqPlane2Weights)

    val plane2ColorComponent = scb.plane2ColorComponent

    // now that we have endpoint colors and weights, we can unpack actual colors for
    // each texel.
    for (i in 0 until texelsPerBlock) {
        val partition = pt.partitionOfTexel[i]

        val color = lerpColorInt(decodeMode,
                                colorEndpoint0[partition],
                                colorEndpoint1[partition],
                                weights[i],
                                plane2Weights[i],
                                if (isDualPlane) plane2ColorComponent else -1)
        blk.rgbLns[i] = rgbHdrEndpoint[partition]
        blk.alphaLns[i] = alphaHdrEndpoint[partition]
        blk.nanTexel[i] = nanEndpoint[partition]

        blk.workData[4 * i] = color.x.toFloat()
        blk.workData[4 * i + 1] = color.y.toFloat()
        blk.workData[4 * i + 2] = color.z.toFloat()
        blk.workData[4 * i + 3] = color.w.toFloat()
    }

    imageblockInitializeOrigFromWork(blk, xDim * yDim * zDim)

    updateImageblockFlags(blk, xDim, yDim, zDim)
    return blk
}