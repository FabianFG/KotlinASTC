package com.fungames.kotlinASTC

import kotlin.random.Random


fun get2dPercentileTable(blockDimX: Int, blockDimY: Int) : Array<Float> {
    when(blockDimX) {
        4 -> when(blockDimY) {
            4 -> return PercentileTable4x4.percentileTable4x4
            5 -> return PercentileTable4x5.percentileTable4x5
            6 -> return PercentileTable4x6.percentileTable4x6
            8 -> return PercentileTable4x8.percentileTable4x8
            10 -> return PercentileTable4x10.percentileTable4x10
            12 -> return PercentileTable4x12.percentileTable4x12
        }
        5 -> when(blockDimY) {
            4 -> return PercentileTable5x4.percentileTable5x4
            5 -> return PercentileTable5x5.percentileTable5x5
            6 -> return PercentileTable5x6.percentileTable5x6
            8 -> return PercentileTable5x8.percentileTable5x8
            10 -> return PercentileTable5x10.percentileTable5x10
            12 -> return PercentileTable5x12.percentileTable5x12
        }
        6 -> when(blockDimY) {
            4 -> return PercentileTable6x4.percentileTable6x4
            5 -> return PercentileTable6x5.percentileTable6x5
            6 -> return PercentileTable6x6.percentileTable6x6
            8 -> return PercentileTable6x8.percentileTable6x8
            10 -> return PercentileTable6x10.percentileTable6x10
            12 -> return PercentileTable6x12.percentileTable6x12
        }
        8 -> when(blockDimY) {
            4 -> return PercentileTable8x4.percentileTable8x4
            5 -> return PercentileTable8x5.percentileTable8x5
            6 -> return PercentileTable8x6.percentileTable8x6
            8 -> return PercentileTable8x8.percentileTable8x8
            10 -> return PercentileTable8x10.percentileTable8x10
            12 -> return PercentileTable8x12.percentileTable8x12
        }
        10 -> when(blockDimY) {
            4 -> return PercentileTable10x4.percentileTable10x4
            5 -> return PercentileTable10x5.percentileTable10x5
            6 -> return PercentileTable10x6.percentileTable10x6
            8 -> return PercentileTable10x8.percentileTable10x8
            10 -> return PercentileTable10x10.percentileTable10x10
            12 -> return PercentileTable10x12.percentileTable10x12
        }
        12 -> when(blockDimY) {
            4 -> return PercentileTable12x4.percentileTable12x4
            5 -> return PercentileTable12x5.percentileTable12x5
            6 -> return PercentileTable12x6.percentileTable12x6
            8 -> return PercentileTable12x8.percentileTable12x8
            10 -> return PercentileTable12x10.percentileTable12x10
            12 -> return PercentileTable12x12.percentileTable12x12
        }
    }
    throw IllegalStateException("This should never happen")
}

val dummyPercentileTable3d = Array(2048) {0.0f}
fun get3dPercentileTable(blockDimX : Int, blockDimY : Int, blockDimZ : Int) = dummyPercentileTable3d

fun decodeBlockMode2d(blockMode: Int) : Pair<Boolean, BlockModeDecodingResult> {
    val res = BlockModeDecodingResult()

    var baseQuantMode = blockMode shr 4 and 1
    var H = blockMode shr 9 and 1
    var D = blockMode shr 10 and 1

    val A = blockMode shr 5 and 0x3

    var N = 0
    var M = 0

    if (blockMode and 3 != 0) {
        baseQuantMode = baseQuantMode or (blockMode and 3 shl 1)
        var B = blockMode shr 7 and 3
        when (blockMode shr 2 and 3) {
            0 -> {
                N = B + 4
                M = A + 2
            }
            1 -> {
                N = B + 8
                M = A + 2
            }
            2 -> {
                N = A + 2
                M = B + 8
            }
            3 -> {
                B = B and 1
                if ((blockMode and 0x100) != 0) {
                    N = B + 2
                    M = A + 2
                } else {
                    N = A + 2
                    M = B + 6
                }
            }
        }
    } else {
        baseQuantMode = baseQuantMode or (blockMode shr 2 and 3 shl 1)
        if (((blockMode shr 2) and 3) == 0)
            return Pair(false, res)
        val B = blockMode shr 9 and 3
        when (blockMode shr 7 and 3) {
            0 -> {
                N = 12
                M = A + 2
            }
            1 -> {
                N = A + 2
                M = 12
            }
            2 -> {
                N = A + 6
                M = B + 6
                D = 0
                H = 0
            }
            3 -> when (blockMode shr 5 and 3) {
                0 -> {
                    N = 6
                    M = 10
                }
                1 -> {
                    N = 10
                    M = 6
                }
                2, 3 -> return Pair(false, res)
            }
        }
    }

    val weightCount = N * M * (D + 1)
    val qmode = baseQuantMode - 2 + 6 * H


    val weightbits = computeIseBitcount(weightCount, QuantizationMethod.getByValue(qmode))
    if(weightCount > MAX_WEIGHTS_PER_BLOCK || weightbits < MIN_WEIGHT_BITS_PER_BLOCK)
        return Pair(false, res)

    res.xWeights = N
    res.yWeights = M
    res.isDualPlane = D != 0
    res.quantizationMode = qmode
    return Pair(true, res)
}

fun decodeBlockMode3d(blockMode : Int) : Pair<Boolean, BlockModeDecodingResult> {
    val res = BlockModeDecodingResult()

    var baseQuantMode = (blockMode shr 4) and 1
    var H = (blockMode shr 9) and 1
    var D = (blockMode shr 10) and 1

    val A = (blockMode shr 5) and 0x3

    var N = 0; var M = 0; var Q = 0

    if((blockMode and 3) != 0) {
        baseQuantMode = baseQuantMode or ((blockMode and 3) shl 1)
        val B = (blockMode shr 7) and 3
        val C = (blockMode shr 2) and 0x3
        N = A + 2
        M = B + 2
        Q = C + 2
    } else {
        baseQuantMode = baseQuantMode or (((blockMode shr 2) and 3) shl 1)
        if(((blockMode shr 2) and 3) == 0)
            return Pair(false, res)
        var B = (blockMode shr 9) and 3
        if(((blockMode shr 7) and 3) != 3) {
            D = 0
            H = 0
        }
        when((blockMode shr 7) and 3) {
            0 -> {
                N = 6
                M = B + 2
                Q = A + 2
            }
            1 -> {
                N = A + 2
                M = 6
                Q = B + 2
            }
            2 -> {
                N = A + 2
                M = B + 2
                Q = 6
            }
            3 -> {
                N = 2
                M = 2
                Q = 2
                when((blockMode shr 5) and 3) {
                    0 -> N = 6
                    1 -> M = 6
                    2 -> Q = 6
                    3 -> return Pair(false, res)
                }
            }
        }

        val weightCount = N * M * Q * (D + 1)
        val qMode = (baseQuantMode - 2) + 6 * H

        val weightbits = computeIseBitcount(weightCount, QuantizationMethod.getByValue(qMode))
        if (weightCount > MAX_WEIGHTS_PER_BLOCK || weightbits < MIN_WEIGHT_BITS_PER_BLOCK || weightbits > MAX_WEIGHT_BITS_PER_BLOCK)
            return Pair(false, res)
        res.xWeights = N
        res.yWeights = M
        res.zWeights = Q
        res.isDualPlane = D != 0
        res.quantizationMode  = qMode
        return Pair(true, res)
    }
    return Pair(false, res)
}

data class BlockModeDecodingResult(
    var xWeights : Int = 0, var yWeights : Int = 0, var zWeights : Int = 0,
    var isDualPlane : Boolean = false,
    var quantizationMode: Int = 0
    )
fun initializeDecimationTable2d(
    // dimensions of the block
    xDim: Int, yDim: Int,
    // number of grid points in 2d weight grid
    xWeights : Int, yWeights : Int, dt : DecimationTable) {

    val texelsPerBlock = xDim * yDim
    val weightsPerBlock = xWeights * yWeights

    val weightcountOfTexel = Array(MAX_TEXELS_PER_BLOCK) {0}
    val gridWeightsOfTexel = Array(MAX_TEXELS_PER_BLOCK) {Array(4) {0} }
    val weightsOfTexel = Array(MAX_TEXELS_PER_BLOCK) {Array(4) {0} }

    val texelcountOfWeight = Array(MAX_WEIGHTS_PER_BLOCK) {0}
    val texelsOfWeight = Array(MAX_WEIGHTS_PER_BLOCK) {Array(MAX_TEXELS_PER_BLOCK) {0}}
    val texelWeightsOfWeight = Array(MAX_WEIGHTS_PER_BLOCK) {Array(MAX_TEXELS_PER_BLOCK) {0}}

    // Not needed because arrays where initialized with 0
    //    for (i = 0; i < weights_per_block; i++)
    //    texelcount_of_weight[i] = 0;
    //    for (i = 0; i < texels_per_block; i++)
    //    weightcount_of_texel[i] = 0;

    for(y in 0 until yDim)
        for(x in 0 until xDim) {
            val texel = y * xDim + x

            val xWeight = (((1024 + xDim / 2) / (xDim - 1)) * x * (xWeights - 1) + 32) shr 6
            val yWeight = (((1024 + yDim / 2) / (yDim - 1)) * y * (yWeights - 1) + 32) shr 6

            val xWeightFrac = xWeight and 0xF
            val yWeightFrac = yWeight and 0xF
            val xWeightInt = xWeight shr 4
            val yWeightInt = yWeight shr 4
            val qWeight = Array(4) {0}
            val weight = Array(4) {0}
            qWeight[0] = xWeightInt + yWeightInt * xWeights
            qWeight[1] = qWeight[0] + 1
            qWeight[2] = qWeight[0] + xWeights
            qWeight[3] = qWeight[2] + 1

            // truncated-precision bilinear interpolation.
            val prod = xWeightFrac * yWeightFrac

            weight[3] = (prod + 8) shr 4
            weight[1] = xWeightFrac - weight[3]
            weight[2] = yWeightFrac - weight[3]
            weight[0] = 16 - xWeightFrac - yWeightFrac + weight[3]

            for (i in 0 until 4)
                if(weight[i] != 0) {
                    gridWeightsOfTexel[texel][weightcountOfTexel[texel]] = qWeight[i]
                    weightsOfTexel[texel][weightcountOfTexel[texel]] = weight[i]
                    weightcountOfTexel[texel]++
                    texelsOfWeight[qWeight[i]][texelcountOfWeight[qWeight[i]]] = texel
                    texelWeightsOfWeight[qWeight[i]][texelcountOfWeight[qWeight[i]]] = weight[i]
                    texelcountOfWeight[qWeight[i]]++
                }
        }

    for (i in 0 until texelsPerBlock) {
        dt.texelNumWeights[i] = weightcountOfTexel[i]

        for (j in 0 until weightcountOfTexel[i]) {
            dt.texelWeightsInt[i][j] = weightsOfTexel[i][j]
            dt.texelWeightsFloat[i][j] = (weightsOfTexel[i][j]) * (1.0f / TEXEL_WEIGHT_SUM)
            dt.texelWeights[i][j] = gridWeightsOfTexel[i][j]
        }
    }

    for(i in 0 until weightsPerBlock) {
        dt.weightNumTexels[i] = texelcountOfWeight[i]

        for (j in 0 until texelcountOfWeight[i]) {
            dt.weightTexel[i][j] = texelsOfWeight[i][j]
            dt.weightsInt[i][j] = texelWeightsOfWeight[i][j]
            dt.weightsFlt[i][j] = texelWeightsOfWeight[i][j].toFloat()
        }
    }

    dt.numTexels = texelsPerBlock
    dt.numWeights = weightsPerBlock
}

fun initializeDecimationTable3d(
    // dimensions of the block
    xDim: Int, yDim: Int, zDim: Int,
    // number of grid points in 3d weight grid
    xWeights : Int, yWeights : Int, zWeights : Int, dt : DecimationTable) {

    val texelsPerBlock = xDim * yDim * zDim
    val weightsPerBlock = xWeights * yWeights * zWeights

    val weightcountOfTexel = Array(MAX_TEXELS_PER_BLOCK) {0}
    val gridWeightsOfTexel = Array(MAX_TEXELS_PER_BLOCK) {Array(4) {0}}
    val weightsOfTexel = Array(MAX_TEXELS_PER_BLOCK) {Array(4) {0}}

    val texelcountOfWeight = Array(MAX_WEIGHTS_PER_BLOCK) {0}
    val texelsOfWeight = Array(MAX_WEIGHTS_PER_BLOCK) {Array(MAX_TEXELS_PER_BLOCK) {0}}
    val texelWeightsOfWeight = Array(MAX_WEIGHTS_PER_BLOCK) {Array(MAX_TEXELS_PER_BLOCK) {0}}

    // Not needed because arrays where initialized with 0
    //    for (i = 0; i < weights_per_block; i++)
    //    texelcount_of_weight[i] = 0;
    //    for (i = 0; i < texels_per_block; i++)
    //    weightcount_of_texel[i] = 0;

    for(z in 0 until zDim)
        for(y in 0 until yDim)
            for(x in 0 until xDim) {
                val texel = (z * yDim + y) * xDim + x

                val xWeight = (((1024 + xDim / 2) / (xDim - 1)) * x * (xWeights - 1) + 32) shr 6
                val yWeight = (((1024 + yDim / 2) / (yDim - 1)) * x * (yWeights - 1) + 32) shr 6
                val zWeight = (((1024 + zDim / 2) / (zDim - 1)) * x * (zWeights - 1) + 32) shr 6

                val xWeightFrac = xWeight and 0xF
                val yWeightFrac = yWeight and 0xF
                val zWeightFrac = zWeight and 0xF
                val xWeightInt = xWeight shr 4
                val yWeightInt = yWeight shr 4
                val zWeightInt = zWeight shr 4
                val qWeight = Array(4) {0}
                val weight = Array(4) {0}
                qWeight[0] = (zWeightInt * yWeights + yWeightInt) * xWeights + xWeightInt
                qWeight[3] = ((zWeightInt + 1) * yWeights + (yWeightInt + 1)) * xWeights + (xWeightInt + 1)

                // simplex interpolation
                val fs = xWeightFrac
                val ft = yWeightFrac
                val fp = zWeightFrac

                //TODO int cas = ((fs > ft) << 2) + ((ft > fp) << 1) + ((fs > fp)); Original, Check
                val cas = (if(fs > ft) 1 else 0 shl 2) + (if(ft > fp) 1 else 0 shl 1) + (if(fs > fp) 1 else 0)
                val N = xWeights
                val NM = xWeights * yWeights

                val s1 : Int; val s2 : Int; val w0 : Int; val w1 : Int; val w2 : Int; val w3 : Int
                when(cas) {
                    7 -> {
                        s1 = 1
                        s2 = N
                        w0 = 16 - fs
                        w1 = fs - ft
                        w2 = ft - fp
                        w3 = fp
                    }
                    3 -> {
                        s1 = 1
                        s2 = N
                        w0 = 16 - fs
                        w1 = fs - ft
                        w2 = ft - fp
                        w3 = fp
                    }
                    5 -> {
                        s1 = 1
                        s2 = NM
                        w0 = 16 - fs
                        w1 = fs - fp
                        w2 = fp - ft
                        w3 = ft
                    }
                    4 -> {
                        s1 = NM
                        s2 = 1
                        w0 = 16 - fp
                        w1 = fp - fs
                        w2 = fs - ft
                        w3 = ft
                    }
                    2 -> {
                        s1 = N
                        s2 = NM
                        w0 = 16 - ft
                        w1 = ft - fp
                        w2 = fp - fs
                        w3 = fs
                    }
                    0 -> {
                        s1 = NM
                        s2 = N
                        w0 = 16 - fp
                        w1 = fp - ft
                        w2 = ft - fs
                        w3 = fs
                    }
                    else -> {
                        s1 = NM
                        s2 = N
                        w0 = 16 - fp
                        w1 = fp - ft
                        w2 = ft - fs
                        w3 = fs
                    }
                }

                qWeight[1] = qWeight[0] + s1
                qWeight[2] = qWeight[1] + s2
                weight[0] = w0
                weight[1] = w1
                weight[2] = w2
                weight[3] = w3

                for(i in 0 until 4)
                    if(weight[i] != 0) {
                        gridWeightsOfTexel[texel][weightcountOfTexel[texel]] = qWeight[i]
                        weightsOfTexel[texel][weightcountOfTexel[texel]] = weight[i]
                        weightcountOfTexel[texel]++
                        texelsOfWeight[qWeight[i]][texelcountOfWeight[qWeight[i]]] = texel
                        texelWeightsOfWeight[qWeight[i]][texelcountOfWeight[qWeight[i]]] = weight[i]
                        texelcountOfWeight[qWeight[i]]++
                    }
            }

    for(i in 0 until texelsPerBlock) {
        dt.texelNumWeights[i] = weightcountOfTexel[i]

        for(j in 0 until weightcountOfTexel[i]) {
            dt.texelWeightsInt[i][j] = weightsOfTexel[i][j]
            dt.texelWeightsFloat[i][j] = weightsOfTexel[i][j] * (1.0f / TEXEL_WEIGHT_SUM)
            dt.texelWeights[i][j] = gridWeightsOfTexel[i][j]
        }
    }

    for(i in 0 until weightsPerBlock) {
        dt.weightNumTexels[i] = texelcountOfWeight[i]
        for(j in 0 until texelcountOfWeight[i]) {
            dt.weightTexel[i][j] = texelsOfWeight[i][j]
            dt.weightsInt[i][j] = texelWeightsOfWeight[i][j]
            dt.weightsFlt[i][j] = texelWeightsOfWeight[i][j].toFloat()
        }
    }

    dt.numTexels = texelsPerBlock
    dt.numWeights = weightsPerBlock
}

internal val bsdPointers : Array<BlockSizeDescriptor?> = arrayOfNulls(4096)

fun getBlockSizeDescriptor(xDim: Int, yDim: Int, zDim: Int) : BlockSizeDescriptor {
    val bsdIndex = xDim + (yDim shl 4) + (zDim shl 8)
    if(bsdPointers[bsdIndex] == null) {
        val bsd = BlockSizeDescriptor()
        when {
            zDim > 1 -> constructBlockSizeDescriptor3d(xDim, yDim, zDim, bsd)
            else -> constructBlockSizeDescriptor2d(xDim, yDim, bsd)
        }
        bsdPointers[bsdIndex] = bsd
    }
    return bsdPointers[bsdIndex] ?: throw IllegalStateException("Bsd must be not null at this point")
}

fun constructBlockSizeDescriptor2d(xDim: Int, yDim: Int, bsd : BlockSizeDescriptor) {
    val decimationModeIndex = Array(256) { -1 }    // for each of the 256 entries in the decim_table_array, its index
    var decimationModeCount = 0

    // gather all the infill-modes that can be used with the current block size
    for (xWeights in 2..12)
        for (yWeights in 2..12) {
            if(xWeights * yWeights > MAX_WEIGHTS_PER_BLOCK)
                continue
            val dt = DecimationTable()
            decimationModeIndex[yWeights * 16 + xWeights] = decimationModeCount
            initializeDecimationTable2d(xDim, yDim, xWeights, yWeights, dt)

            val weightCount = xWeights * yWeights

            var maxprec1Plane = -1
            var maxprec2Planes = -1
            for(i in 0 until 12) {
                val bits1Plane = computeIseBitcount(weightCount, QuantizationMethod.getByValue(i))
                val bits2Plane = computeIseBitcount(2 * weightCount, QuantizationMethod.getByValue(i))
                if (bits1Plane in MIN_WEIGHT_BITS_PER_BLOCK..MAX_WEIGHT_BITS_PER_BLOCK)
                    maxprec1Plane = i
                if (bits2Plane in MIN_WEIGHT_BITS_PER_BLOCK..MAX_WEIGHT_BITS_PER_BLOCK)
                    maxprec2Planes = i
            }

            if(2 * xWeights * yWeights > MAX_WEIGHTS_PER_BLOCK)
                maxprec2Planes = -1

            bsd.permitEncode[decimationModeCount] = (xWeights <= xDim && yWeights <= yDim)

            bsd.decimationModeSamples[decimationModeCount] = weightCount
            bsd.decimationModeMaxPrec1Plane[decimationModeCount] = maxprec1Plane
            bsd.decimationModeMaxPrec2Planes[decimationModeCount] = maxprec2Planes
            bsd.decimationTables[decimationModeCount] = dt

            decimationModeCount++

        }

    for(i in 0 until MAX_DECIMATION_MODES)
        bsd.decimationModePercentile[i] = 1.0f

    for(i in decimationModeCount until MAX_DECIMATION_MODES) {
        bsd.permitEncode[i] = false
        bsd.decimationModeSamples[i] = 0
        bsd.decimationModeMaxPrec1Plane[i] = -1
        bsd.decimationModeMaxPrec2Planes[i] = -1
    }

    bsd.decimationModeCount = decimationModeCount

    val percentiles = get2dPercentileTable(xDim, yDim)

    // then construct the list of block formats
    for (i in 0 until 2048) {
        val (success, res) = decodeBlockMode2d(i)
        val xWeights = res.xWeights
        val yWeights = res.yWeights
        val isDualPlane = res.isDualPlane
        val quantizationMode = res.quantizationMode
        var fail = false
        var permitEncode = true

        if(success) {
            if(xWeights > xDim || yWeights > yDim)
                permitEncode = false
        } else {
            fail = true
            permitEncode = false
        }

        if(fail) {
            bsd.blockModes[i].decimationMode = -1
            bsd.blockModes[i].quantizationMode = -1
            bsd.blockModes[i].isDualPlane = false
            bsd.blockModes[i].permitEncode = false
            bsd.blockModes[i].permitDecode = false
            bsd.blockModes[i].percentile = 1.0f
        } else {
            val decimationMode = decimationModeIndex[yWeights * 16 + xWeights]
            bsd.blockModes[i].decimationMode = decimationMode
            bsd.blockModes[i].quantizationMode = quantizationMode
            bsd.blockModes[i].isDualPlane = isDualPlane
            bsd.blockModes[i].permitEncode = permitEncode
            bsd.blockModes[i].permitDecode = permitEncode 	// disallow decode of grid size larger than block size.
            bsd.blockModes[i].percentile = percentiles[i]

            if(bsd.decimationModePercentile[decimationMode] > percentiles[i])
                bsd.decimationModePercentile[decimationMode] = percentiles[i]
        }
    }

    if(xDim * yDim <= 64) {
        bsd.texelcountForBitmapPartitioning = xDim * yDim
        for(i in 0 until xDim * yDim)
            bsd.texelsForBitmapPartitioning[i] = i
    }

    else {
        // pick 64 random texels for use with bitmap partitioning.
        val arr = Array(MAX_TEXELS_PER_BLOCK) {0}
        //Useless when initializing with 0
        //for(i in 0 until xDim * yDim * zDim)
        //   arr[i] = 0
        var arrElementsSet = 0
        while (arrElementsSet < 64) {
            val idx = Random.nextInt() %(xDim * yDim)
            if(arr[idx] == 0) {
                arrElementsSet++
                arr[idx] = 1
            }
        }
        var texelWeightsWritten = 0
        var idx = 0
        while(texelWeightsWritten < 64) {
            if(arr[idx] != 0)
                bsd.texelsForBitmapPartitioning[texelWeightsWritten++] = idx
            idx++
        }
        bsd.texelcountForBitmapPartitioning = 64
    }

}

fun constructBlockSizeDescriptor3d(xDim: Int, yDim: Int, zDim: Int, bsd : BlockSizeDescriptor) {
    val decimationModeIndex = Array(512) { -1 }    // for each of the 512 entries in the decim_table_array, its index
    var decimationModeCount = 0

    // gather all the infill-modes that can be used with the current block size
    for (xWeights in 2..6)
        for (yWeights in 2..6)
            for (zWeights in 2..6) {
                if ((xWeights * yWeights * zWeights) > MAX_WEIGHTS_PER_BLOCK)
                    continue
                val dt = DecimationTable()
                decimationModeIndex[zWeights * 64 + yWeights * 8 + xWeights] = decimationModeCount
                initializeDecimationTable3d(xDim, yDim, zDim, xWeights, yWeights, zWeights, dt)

                val weightCount = xWeights * yWeights * zWeights

                var maxprec1Plane = -1
                var maxprec2Planes = -1
                for (i in 0 until 12) {
                    val bits1Plane = computeIseBitcount(weightCount, QuantizationMethod.getByValue(i))
                    val bits2Planes = computeIseBitcount(2 * weightCount, QuantizationMethod.getByValue(i))
                    if (bits1Plane in MIN_WEIGHT_BITS_PER_BLOCK..MAX_WEIGHT_BITS_PER_BLOCK)
                        maxprec1Plane = i
                    if (bits2Planes in MIN_WEIGHT_BITS_PER_BLOCK..MAX_WEIGHT_BITS_PER_BLOCK)
                        maxprec2Planes = i
                }

                if ((2 * xWeights * yWeights * zWeights) > MAX_WEIGHTS_PER_BLOCK)
                    maxprec2Planes = -1
                bsd.permitEncode[decimationModeCount] = xWeights <= xDim && yWeights <= yDim && zWeights <= zDim
                bsd.decimationModeSamples[decimationModeCount] = weightCount
                bsd.decimationModeMaxPrec1Plane[decimationModeCount] = maxprec1Plane
                bsd.decimationModeMaxPrec2Planes[decimationModeCount] = maxprec2Planes
                bsd.decimationTables[decimationModeCount] = dt

                decimationModeCount++
            }
    for (i in 0 until MAX_DECIMATION_MODES) {
        bsd.decimationModePercentile[i] = 1.0f
    }
    for (i in decimationModeCount until MAX_DECIMATION_MODES) {
        bsd.permitEncode[i] = false
        bsd.decimationModeSamples[i] = 0
        bsd.decimationModeMaxPrec1Plane[i] = -1
        bsd.decimationModeMaxPrec2Planes[i] = -1
    }

    bsd.decimationModeCount = decimationModeCount

    val percentiles = get3dPercentileTable(xDim, yDim, zDim)

    // then construct the list of block formats
    for (i in 0 until 2048) {

        val (success, res) = decodeBlockMode3d(i)
        val xWeights = res.xWeights
        val yWeights = res.yWeights
        val zWeights = res.zWeights
        val isDualPlane = res.isDualPlane
        val quantizationMode = res.quantizationMode
        var fail = false
        var permitEncode = true
        if (success) {
            if (xWeights > xDim || yWeights > yDim || zWeights > zDim)
                permitEncode = false
        } else {
            fail = true
            permitEncode = false
        }
        if (fail) {
            bsd.blockModes[i].decimationMode = -1
            bsd.blockModes[i].quantizationMode = -1
            bsd.blockModes[i].isDualPlane = false
            bsd.blockModes[i].permitEncode = false
            bsd.blockModes[i].permitDecode = false
            bsd.blockModes[i].percentile = 1.0f
        } else {
            val decimationMode = decimationModeIndex[zWeights * 64 + yWeights * 8 + xWeights]
            bsd.blockModes[i].decimationMode = decimationMode
            bsd.blockModes[i].quantizationMode = quantizationMode
            bsd.blockModes[i].isDualPlane = isDualPlane
            bsd.blockModes[i].permitEncode = permitEncode
            bsd.blockModes[i].permitDecode = permitEncode
            bsd.blockModes[i].percentile = percentiles[i]

            if (bsd.decimationModePercentile[decimationMode] > percentiles[i])
                bsd.decimationModePercentile[decimationMode] = percentiles[i]
        }

    }

    if (xDim * yDim * zDim <= 64) {
        bsd.texelcountForBitmapPartitioning = xDim * yDim * zDim
        for (i in 0 until xDim * yDim * zDim)
            bsd.texelsForBitmapPartitioning[i] = i
    } else {
        // pick 64 random texels for use with bitmap partitioning.
        val arr = Array(MAX_TEXELS_PER_BLOCK) {0}
        //Useless when initializing with 0
        //for(i in 0 until xDim * yDim * zDim)
        //   arr[i] = 0
        var arrElementsSet = 0
        while (arrElementsSet < 64) {
            val idx = Random.nextInt() %(xDim * yDim * zDim)
            if(arr[idx] == 0) {
                arrElementsSet++
                arr[idx] = 1
            }
        }
        var texelWeightsWritten = 0
        var idx = 0
        while(texelWeightsWritten < 64) {
            if(arr[idx] != 0)
                bsd.texelsForBitmapPartitioning[texelWeightsWritten++] = idx
            idx++
        }
        bsd.texelcountForBitmapPartitioning = 64
    }
}