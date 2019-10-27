/*----------------------------------------------------------------------------*/
/**
 *	This confidential and proprietary software may be used only as
 *	authorised by a licensing agreement from ARM Limited
 *	(C) COPYRIGHT 2011-2012, 2018 ARM Limited
 *	ALL RIGHTS RESERVED
 *
 *	The entire notice above must be reproduced on all authorised
 *	copies and copies may only be made to the extent permitted
 *	by a licensing agreement from ARM Limited.
 *
 *	@brief	Internal function and data declarations for ASTC codec.
 *	@author rewritten to Kotlin by FunGames
 */
/*----------------------------------------------------------------------------*/

@file:Suppress("EXPERIMENTAL_API_USAGE")

package me.fungames.kotlinASTC

import me.fungames.kotlinPointers.BytePointer

const val MAX_WEIGHTS_PER_BLOCK = 64
const val MAX_DECIMATION_MODES = 87
const val MAX_TEXELS_PER_BLOCK = 216
const val MAX_WEIGHT_MODES = 2048
const val TEXEL_WEIGHT_SUM = 16
const val MIN_WEIGHT_BITS_PER_BLOCK = 24
const val MAX_WEIGHT_BITS_PER_BLOCK = 96
const val PARTITION_BITS = 10
const val PARTITION_COUNT = (1 shl PARTITION_BITS)
fun astcIsnan(p : Float) = ((p)!=(p))

enum class AstcDecodeMode {
    DECODE_LDR_SRGB,
    DECODE_LDR,
    DECODE_HDR
}

enum class Bitness {
    BITNESS_8,
    BITNESS_16
}

/*
	In ASTC, we support relatively many combinations of weight precisions and weight transfer functions.
	As such, for each combination we support, we have a hardwired data structure.

	This structure provides the following information: A table, used to estimate the closest quantized
	weight for a given floating-point weight. For each quantized weight, the corresponding unquantized
	and floating-point values. For each quantized weight, a previous-value and a next-value.
*/
class QuantizationAndTransferTable(
    var method : QuantizationMethod,
    val unquantizedValue : UByteArray = UByteArray(32),
    val unquantizedValueFlt : FloatArray = FloatArray(32),
    val prevQuantizedValue : UByteArray = UByteArray(32),
    val nextQuantizedValue : UByteArray = UByteArray(32),
    val closestQuantizedWeight : UByteArray = UByteArray(1025)
)

enum class EndpointFormats(var i : Int)
{
    FMT_LUMINANCE(0),
    FMT_LUMINANCE_DELTA(1),
    FMT_HDR_LUMINANCE_LARGE_RANGE(2),
    FMT_HDR_LUMINANCE_SMALL_RANGE(3),
    FMT_LUMINANCE_ALPHA(4),
    FMT_LUMINANCE_ALPHA_DELTA(5),
    FMT_RGB_SCALE(6),
    FMT_HDR_RGB_SCALE(7),
    FMT_RGB(8),
    FMT_RGB_DELTA(9),
    FMT_RGB_SCALE_ALPHA(10),
    FMT_HDR_RGB(11),
    FMT_RGBA(12),
    FMT_RGBA_DELTA(13),
    FMT_HDR_RGB_LDR_ALPHA(14),
    FMT_HDR_RGBA(15);

    companion object {
        fun getByValue(i : Int) = values().first { it.i == i }
    }
}

class SwizzlePattern(
    var r : Int,
    var g : Int,
    var b : Int,
    var a : Int
)

/*
	Partition table representation:
	For each block size, we have 3 tables, each with 1024 partitionings;
	these three tables correspond to 2, 3 and 4 partitions respectively.
	For each partitioning, we have:
	* a 4-entry table indicating how many texels there are in each of the 4 partitions.
	  This may be from 0 to a very large value.
	* a table indicating the partition index of each of the texels in the block.
	  Each index may be 0, 1, 2 or 3.
	* Each element in the table is an uint8_t indicating partition index (0, 1, 2 or 3)
*/
class PartitionInfo(
    var partitionCount: Int = 0,
    val texelsPerPartition : UByteArray = UByteArray(4),
    val partitionOfTexel : UByteArray = UByteArray(MAX_TEXELS_PER_BLOCK),
    val texelsOfPartition : Array<UByteArray> = Array(4) {UByteArray(MAX_TEXELS_PER_BLOCK)},

    val coverageBitmaps : ULongArray = ULongArray(4)
)

/*
   In ASTC, we don't necessarily provide a weight for every texel.
   As such, for each block size, there are a number of patterns where some texels
   have their weights computed as a weighted average of more than 1 weight.
   As such, the codec uses a data structure that tells us: for each texel, which
   weights it is a combination of for each weight, which texels it contributes to.
   The decimation_table is this data structure.
*/
class DecimationTable(
    var numTexels : Int = 0,
    var numWeights : Int = 0,
    var texelNumWeights : UByteArray = UByteArray(MAX_TEXELS_PER_BLOCK),
    var texelWeightsInt : Array<UByteArray> = Array(MAX_TEXELS_PER_BLOCK) { UByteArray(4) },
    var texelWeightsFloat: Array<FloatArray> = Array(MAX_TEXELS_PER_BLOCK) { FloatArray(4) },
    var texelWeights : Array<UByteArray> = Array(MAX_TEXELS_PER_BLOCK) { UByteArray(4) },
    var weightNumTexels : UByteArray = UByteArray(MAX_WEIGHTS_PER_BLOCK),
    var weightTexel : Array<UByteArray> = Array(MAX_WEIGHTS_PER_BLOCK) { UByteArray(MAX_TEXELS_PER_BLOCK) },
    var weightsInt : Array<UByteArray> = Array(MAX_WEIGHTS_PER_BLOCK) { UByteArray(MAX_TEXELS_PER_BLOCK) },
    var weightsFlt : Array<FloatArray> = Array(MAX_WEIGHTS_PER_BLOCK) { FloatArray(MAX_TEXELS_PER_BLOCK) }
)

/*
   data structure describing information that pertains to a block size and its associated block modes.
*/
class BlockMode(
    var decimationMode : Byte = 0,
    var quantizationMode : Byte = 0,
    var isDualPlane : Boolean = false,
    var permitEncode : Boolean = false,
    var permitDecode : Boolean = false,
    var percentile : Float = 0.0f
)

class BlockSizeDescriptor(
    var decimationModeCount : Int = 0,
    var decimationModeSamples : IntArray = IntArray(MAX_DECIMATION_MODES),
    var decimationModeMaxPrec1Plane: IntArray = IntArray(MAX_DECIMATION_MODES),
    var decimationModeMaxPrec2Planes: IntArray = IntArray(MAX_DECIMATION_MODES),
    var decimationModePercentile: FloatArray = FloatArray(MAX_DECIMATION_MODES),
    var permitEncode : BooleanArray = BooleanArray(MAX_DECIMATION_MODES) {false},
    val decimationTables : Array<DecimationTable> = Array(MAX_DECIMATION_MODES + 1) { DecimationTable() },
    var blockModes: Array<BlockMode> = Array(MAX_WEIGHT_MODES) { BlockMode() },

    // for the k-means bed bitmap partitioning algorithm, we don't
    // want to consider more than 64 texels; this array specifies
    // which 64 texels (if that many) to consider.
    var texelcountForBitmapPartitioning : Int = 0,
    var texelsForBitmapPartitioning : IntArray = IntArray(64)
)

class ImageBlock (
    var origData : FloatArray = FloatArray(MAX_TEXELS_PER_BLOCK * 4),  // original input data
    var workData : FloatArray = FloatArray(MAX_TEXELS_PER_BLOCK * 4),  // the data that we will compress, either linear or LNS (0..65535 in both cases)
    var derivData : FloatArray = FloatArray(MAX_TEXELS_PER_BLOCK * 4), // derivative of the conversion function used, used to modify error weighting

    var rgbLns : BooleanArray = BooleanArray(MAX_TEXELS_PER_BLOCK),      // 1 if RGB data are being treated as LNS
    var alphaLns : BooleanArray = BooleanArray(MAX_TEXELS_PER_BLOCK),    // 1 if Alpha data are being treated as LNS
    var nanTexel : BooleanArray = BooleanArray(MAX_TEXELS_PER_BLOCK),    // 1 if the texel is a NaN-texel.

    var redMin : Float = 0.0f, var redMax : Float = 0.0f,
    var greenMin : Float = 0.0f, var greenMax : Float = 0.0f,
    var blueMin : Float = 0.0f, var blueMax : Float = 0.0f,
    var alphaMin : Float = 0.0f, var alphaMax : Float = 0.0f,
    var grayScale : Int = 0,				// 1 if R=G=B for every pixel, 0 otherwise

    var xPos : Int = 0, var yPos : Int = 0, var zPos : Int = 0

)

class PhysicalCompressedBlock(val data : UByteArray) {
    constructor(ptr : BytePointer) : this(ptr.asArray().copyOfRange(ptr.pos, ptr.pos + 16).toUByteArray())
    init {
        require(data.size == 16) { "PhysicalCompressedBlocks must be 16 bytes big" }
    }
}

class SymbolicCompressedBlock(
    var errorBlock : Boolean = false,
    var blockMode : Int = 0,
    var partitionCount : Int = 0,
    var partitionIndex : Int = 0,
    var colorFormats : IntArray = IntArray(4),
    var colorFormatsMatched : Boolean = false,
    var colorValues : Array<IntArray> = Array(4) {IntArray(12)},
    var colorQuantizationLevel : Int = 0,
    var plane1Weights : UByteArray = UByteArray(MAX_WEIGHTS_PER_BLOCK),
    var plane2Weights: UByteArray = UByteArray(MAX_WEIGHTS_PER_BLOCK),
    var plane2ColorComponent : Int = 0,
    var constantColor : IntArray = IntArray(4)
)

enum class QuantizationMethod(var i : Int) {


    QUANT_2(0),
    QUANT_3(1),
    QUANT_4(2),
    QUANT_5(3),
    QUANT_6(4),
    QUANT_8(5),
    QUANT_10(6),
    QUANT_12(7),
    QUANT_16(8),
    QUANT_20(9),
    QUANT_24(10),
    QUANT_32(11),
    QUANT_40(12),
    QUANT_48(13),
    QUANT_64(14),
    QUANT_80(15),
    QUANT_96(16),
    QUANT_128(17),
    QUANT_160(18),
    QUANT_192(19),
    QUANT_256(20);

    companion object {
        fun getByValue(i : Int) = values().first { it.i == i }
    }
}