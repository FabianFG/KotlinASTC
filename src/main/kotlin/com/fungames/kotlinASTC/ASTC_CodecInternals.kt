package com.fungames.kotlinASTC

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

enum class ASTC_DecodeMode {
    DECODE_LDR_SRGB,
    DECODE_LDR,
    DECODE_HDR
};

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
    val unquantizedValue : Array<Int> = Array(32) {0},
    val unquantizedValueFlt : Array<Float> = Array(32) {0f},
    val prevQuantizedValue : Array<Int> = Array(32) {0},
    val nextQuantizedValue : Array<Int> = Array(32) {0},
    val closestQuantizedWeight : Array<Int> = Array(1025) {0}
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
        fun getByValue(i : Int) = EndpointFormats.values().filter { it.i == i }.first()
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
    val texelsPerPartition : Array<Int> = Array(4) {0},
    val partitionOfTexel : Array<Int> = Array(MAX_TEXELS_PER_BLOCK) {0},
    val texelsOfPartition : Array<Array<Int>> = Array(4) {Array(MAX_TEXELS_PER_BLOCK) {0} },

    val coverageBitmaps : Array<ULong> = Array(4) {0.toULong()}
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
    var texelNumWeights : Array<Int> = Array(MAX_TEXELS_PER_BLOCK) {0},
    var texelWeightsInt : Array<Array<Int>> = Array(MAX_TEXELS_PER_BLOCK) { Array(4) {0} },
    var texelWeightsFloat: Array<Array<Float>> = Array(MAX_TEXELS_PER_BLOCK) { Array(4) {0.0f} },
    var texelWeights : Array<Array<Int>> = Array(MAX_TEXELS_PER_BLOCK) { Array(4) {0} },
    var weightNumTexels : Array<Int> = Array(MAX_WEIGHTS_PER_BLOCK) {0},
    var weightTexel : Array<Array<Int>> = Array(MAX_WEIGHTS_PER_BLOCK) { Array(MAX_TEXELS_PER_BLOCK) {0} },
    var weightsInt : Array<Array<Int>> = Array(MAX_WEIGHTS_PER_BLOCK) { Array(MAX_TEXELS_PER_BLOCK) {0} },
    var weightsFlt : Array<Array<Float>> = Array(MAX_WEIGHTS_PER_BLOCK) { Array(MAX_TEXELS_PER_BLOCK) {0.0f} }
)

/*
   data structure describing information that pertains to a block size and its associated block modes.
*/
class BlockMode(
    var decimationMode : Int = 0,
    var quantizationMode : Int = 0,
    var isDualPlane : Boolean = false,
    var permitEncode : Boolean = false,
    var permitDecode : Boolean = false,
    var percentile : Float = 0.0f
)

class BlockSizeDescriptor(
    var decimationModeCount : Int = 0,
    var decimationModeSamples : Array<Int> = Array(MAX_DECIMATION_MODES) {0},
    var decimationModeMaxPrec1Plane: Array<Int> = Array(MAX_DECIMATION_MODES) {0},
    var decimationModeMaxPrec2Planes: Array<Int> = Array(MAX_DECIMATION_MODES) {0},
    var decimationModePercentile: Array<Float> = Array(MAX_DECIMATION_MODES) {0.0f},
    var permitEncode : Array<Boolean> = Array(MAX_DECIMATION_MODES) {false},
    val decimationTables : Array<DecimationTable> = Array(MAX_DECIMATION_MODES + 1) {DecimationTable()},
    var blockModes: Array<BlockMode> = Array(MAX_WEIGHT_MODES) {BlockMode()},

    // for the k-means bed bitmap partitioning algorithm, we don't
    // want to consider more than 64 texels; this array specifies
    // which 64 texels (if that many) to consider.
    var texelcountForBitmapPartitioning : Int = 0,
    var texelsForBitmapPartitioning : Array<Int> = Array(64) {0}
)

class ImageBlock (
    var origData : Array<Float> = Array(MAX_TEXELS_PER_BLOCK * 4) {0.0f},  // original input data
    var workData : Array<Float> = Array(MAX_TEXELS_PER_BLOCK * 4) {0.0f},  // the data that we will compress, either linear or LNS (0..65535 in both cases)
    var derivData : Array<Float> = Array(MAX_TEXELS_PER_BLOCK * 4) {0.0f}, // derivative of the conversion function used, used to modify error weighting

    var rgbLns : Array<Boolean> = Array(MAX_TEXELS_PER_BLOCK) {false},      // 1 if RGB data are being treated as LNS
    var alphaLns : Array<Boolean> = Array(MAX_TEXELS_PER_BLOCK) {false},    // 1 if Alpha data are being treated as LNS
    var nanTexel : Array<Boolean> = Array(MAX_TEXELS_PER_BLOCK) {false},    // 1 if the texel is a NaN-texel.

    var redMin : Float = 0.0f, var redMax : Float = 0.0f,
    var greenMin : Float = 0.0f, var greenMax : Float = 0.0f,
    var blueMin : Float = 0.0f, var blueMax : Float = 0.0f,
    var alphaMin : Float = 0.0f, var alphaMax : Float = 0.0f,
    var grayScale : Int = 0,				// 1 if R=G=B for every pixel, 0 otherwise

    var xPos : Int = 0, var yPos : Int = 0, var zPos : Int = 0

)

class PhysicalCompressedBlock(val data : Array<UByte>) {
    constructor(ptr : Pointer<Byte>) : this(arrayOf(ptr[0].toUByte(), ptr[1].toUByte(), ptr[2].toUByte(), ptr[3].toUByte(), ptr[4].toUByte(), ptr[5].toUByte(), ptr[6].toUByte(), ptr[7].toUByte(), ptr[8].toUByte(), ptr[9].toUByte(), ptr[10].toUByte(), ptr[11].toUByte(), ptr[12].toUByte(), ptr[13].toUByte(), ptr[14].toUByte(), ptr[15].toUByte()))
    init {
        require(data.size == 16) { "PhysicalCompressedBlocks must be 16 bytes big" }
    }
}

class SymbolicCompressedBlock(
    var errorBlock : Boolean = false,
    var blockMode : Int = 0,
    var partitionCount : Int = 0,
    var partitionIndex : Int = 0,
    var colorFormats : Array<Int> = Array(4) {0},
    var colorFormatsMatched : Boolean = false,
    var colorValues : Array<Array<Int>> = Array(4) {Array(12) {0}},
    var colorQuantizationLevel : Int = 0,
    var plane1Weights : Array<UByte> = Array(MAX_WEIGHTS_PER_BLOCK) {0.toUByte()},
    var plane2Weights: Array<UByte> = Array(MAX_WEIGHTS_PER_BLOCK) {0.toUByte()},
    var plane2ColorComponent : Int = 0,
    var constantColor : Array<Int> = Array(4) {0}
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
        fun getByValue(i : Int) = QuantizationMethod.values().filter { it.i == i }.first()
    }
}