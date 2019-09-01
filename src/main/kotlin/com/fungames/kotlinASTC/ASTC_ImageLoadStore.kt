@file:Suppress("NAME_SHADOWING")

package com.fungames.kotlinASTC

import kotlin.math.*

@kotlin.ExperimentalUnsignedTypes
data class ASTCCodecImage(val bitness : Bitness, val xsize : Int, val ysize : Int, val zsize : Int, val padding : Int, val xDim : Int, val yDim : Int, val decodeMode: ASTC_DecodeMode, val swizzlePattern: SwizzlePattern) {
    val imageData8 : Pointer<Pointer<Pointer<Byte>>>?
    val imageData16 : Pointer<Pointer<Pointer<Int>>>?

    constructor(bitness : Bitness, xsize : Int, ysize : Int, zsize : Int, padding : Int, blockDimX : Int, blockDimY : Int) : this(bitness, xsize, ysize, zsize, padding, blockDimX, blockDimY, ASTC_DecodeMode.DECODE_LDR, SwizzlePattern(0, 1, 2, 3))

    private val xBlocks = (xsize + xDim - 1) / xDim
    private val yBlocks = (ysize + yDim - 1) / yDim
    private val zBlocks = zsize

    //TODO ?
    private val zDim = zsize

    init {
        val exsize = xsize + 2 * padding
        val eysize = ysize + 2 * padding
        val ezsize = if(zsize == 1) 1 else zsize + 2 * padding
        when(bitness) {
            Bitness.BITNESS_8 -> {
                imageData8 = Pointer(arrayOfNulls(ezsize))
                imageData8[0] = Pointer(arrayOfNulls(ezsize * eysize))
                imageData8[0][0] = Pointer(arrayOfNulls(4 * ezsize * eysize * exsize))
                for(i in 1 until ezsize) {
                    imageData8[i] = imageData8[0] + i * eysize
                    imageData8[i][0] = imageData8[0][0] + 4 * i * exsize * eysize
                }
                for(i in 0 until ezsize)
                    for(j in 1 until eysize)
                        imageData8[i][j] = imageData8[i][0] + 4 * j * exsize
                imageData16 = null
            }
            Bitness.BITNESS_16 -> {
                imageData16 = Pointer(arrayOfNulls(ezsize))
                imageData16[0] = Pointer(arrayOfNulls(ezsize * eysize))
                imageData16[0][0] = Pointer(arrayOfNulls(4 * ezsize * eysize * exsize))
                for(i in 1 until ezsize) {
                    imageData16[i] = imageData16[0] + i * eysize
                    imageData16[i][0] = imageData16[0][0] + 4 * i * exsize * eysize
                }
                for(i in 0 until ezsize)
                    for(j in 1 until eysize)
                        imageData16[i][j] = imageData16[i][0] + 4 * j * exsize
                imageData8 = null
            }
        }
    }

    fun decode(data : ByteArray) = decode(data.toTypedArray())
    fun decode(data : Array<Byte>) {
        val data = data.toPointer()
        for (z in 0 until zBlocks)
            for (y in 0 until yBlocks)
                for (x in 0 until xBlocks) {
                    val offset = ((y * xBlocks) + x) * 16
                    val bp = data + offset
                    val pcb = PhysicalCompressedBlock(bp)
                    val scb = physicalToSymbolic(xDim, yDim, zDim, pcb)
                    val pb = decompressSymbolicBlock(decodeMode, xDim, yDim, zDim, x * xDim, y * yDim, z * zDim, scb)
                    writeImageblock(pb, xDim, yDim, zDim, x * xDim, y * yDim, z * zDim, swizzlePattern)
                }

    }

    fun toBuffer() : ByteArray {
        if( imageData8 != null) {
            return imageData8[0][0].data.toNonNullableArray().toByteArray()
        }
        return ByteArray(0)
    }

    fun initializeImage() {
        val exsize = xsize + 2 * padding
        val eysize = ysize + 2 * padding
        val ezsize = if(zsize == 1) 1 else zsize + 2 * padding

        when(bitness) {
            Bitness.BITNESS_8 -> {
                if(imageData8 == null)
                    throw IllegalStateException()
                for(z in 0 until ezsize)
                    for(y in 0 until eysize)
                        for(x in 0 until exsize) {
                            imageData8[z][y][4 * x] = 0
                            imageData8[z][y][4 * x + 1] = 0
                            imageData8[z][y][4 * x + 2] = 0
                            imageData8[z][y][4 * x + 3] = 0xFF.toByte()
                        }
            }
            Bitness.BITNESS_16 -> {
                if(imageData16 == null)
                    throw IllegalStateException()
                for(z in  0 until ezsize)
                    for(y in  0 until eysize)
                        for(x in 0 until exsize) {
                            imageData16[z][y][4 * x] = 0
                            imageData16[z][y][4 * x + 1] = 0
                            imageData16[z][y][4 * x + 2] = 0
                            imageData16[z][y][4 * x + 3] = 0x3C00
                        }
            }
        }
    }

    fun writeImageblock(pb: ImageBlock,	// picture-block to initialize with image data. We assume that orig_data is valid.
                        // block dimensions
                        xDim: Int, yDim: Int, zDim: Int,
                        // position to write block to
                        xPos : Int, yPos : Int, zPos : Int, swz : SwizzlePattern) {
        val fptr = pb.origData.toPointer()
        val nptr = pb.nanTexel.toPointer()

        val data = Array(7) {0f}
        data[4] = 0.0f
        data[5] = 1.0f

        if(imageData8 != null)
                for (z in 0 until zDim)
                    for (y in 0 until yDim)
                        for (x in 0 until xDim) {
                            val xi = xPos + x
                            val yi = yPos + y
                            val zi = zPos + z

                            if (xi >= 0 && yi >= 0 && zi >= 0 && xi < xsize && yi < ysize && zi < zsize) {
                                if(nptr[0]) {
                                    // NaN-pixel, but we can't display it. Display purple instead.
                                    imageData8[zi][yi][4 * xi] = 0xFF.toByte()
                                    imageData8[zi][yi][4 * xi + 1] = 0x00.toByte()
                                    imageData8[zi][yi][4 * xi + 2] = 0xFF.toByte()
                                    imageData8[zi][yi][4 * xi + 3] = 0xFF.toByte()
                                } else {
                                    // apply swizzle
                                    if (performSrgbTransform) {
                                        var r = fptr[0]
                                        var g = fptr[1]
                                        var b = fptr[2]

                                        if (r <= 0.0031308f)
                                            r *= 12.92f
                                        else if (r <= 1)
                                            r = 1.055f * r.pow(1.0f / 2.4f) - 0.055f

                                        if (g <= 0.0031308f)
                                            g *= 12.92f
                                        else if (g <= 1)
                                            g = 1.055f * g.pow(1.0f / 2.4f) - 0.055f

                                        if (b <= 0.0031308f)
                                            b *= 12.92f
                                        else if (b <= 1)
                                            b = 1.055f * b.pow(1.0f / 2.4f) - 0.055f

                                        data[0] = r
                                        data[1] = g
                                        data[2] = b
                                    } else {
                                        data[0] = fptr[0]
                                        data[1] = fptr[1]
                                        data[2] = fptr[2]
                                    }
                                    data[3] = fptr[3]

                                    val xcoord = data[0] * 2.0f - 1.0f
                                    val ycoord = data[3] * 2.0f - 1.0f
                                    var zcoord = 1.0f - xcoord * xcoord - ycoord * ycoord
                                    if (zcoord < 0.0f)
                                        zcoord = 0.0f
                                    data[6] = (sqrt(zcoord) * 0.5f) + 0.5f

                                    // clamp to [0,1]
                                    if (data[0] > 1.0f)
                                        data[0] = 1.0f
                                    if (data[1] > 1.0f)
                                        data[1] = 1.0f
                                    if (data[2] > 1.0f)
                                        data[2] = 1.0f
                                    if (data[3] > 1.0f)
                                        data[3] = 1.0f

                                    val ri = floor(data[swz.r] * 255.0f + 0.5f).toInt()
                                    val gi = floor(data[swz.g] * 255.0f + 0.5f).toInt()
                                    val bi = floor(data[swz.b] * 255.0f + 0.5f).toInt()
                                    val ai = floor(data[swz.a] * 255.0f + 0.5f).toInt()

                                    imageData8[zi][yi][4 * xi] = ri.toByte()
                                    imageData8[zi][yi][4 * xi + 1] = gi.toByte()
                                    imageData8[zi][yi][4 * xi + 2] = bi.toByte()
                                    imageData8[zi][yi][4 * xi + 3] = ai.toByte()
                                }
                            }
                            fptr += 4
                            nptr += 1
                        }
        else if (imageData16 != null) {
            for (z in 0 until zDim)
                for (y in 0 until yDim)
                    for (x in 0 until xDim) {
                        val xi = xPos + x
                        val yi = xPos + x
                        val zi = xPos + x

                        if (xi >= 0 && yi >= 0 && zi >= 0 && xi < xsize && yi < ysize && zi < zsize) {
                            if(nptr[0]) {
                                imageData16[zi][yi][4 * xi] = 0xFFFF
                                imageData16[zi][yi][4 * xi + 1] = 0xFFFF
                                imageData16[zi][yi][4 * xi + 2] = 0xFFFF
                                imageData16[zi][yi][4 * xi + 3] = 0xFFFF
                            } else {
                                // apply swizzle
                                if (performSrgbTransform) {
                                    var r = fptr[0]
                                    var g = fptr[1]
                                    var b = fptr[2]

                                    if (r <= 0.0031308f)
                                        r *= 12.92f
                                    else if (r <= 1)
                                        r = 1.055f * r.pow(1.0f / 2.4f) - 0.055f

                                    if (g <= 0.0031308f)
                                        g *= 12.92f
                                    else if (g <= 1)
                                        g = 1.055f * g.pow(1.0f / 2.4f) - 0.055f

                                    if (b <= 0.0031308f)
                                        b *= 12.92f
                                    else if (b <= 1)
                                        b = 1.055f * b.pow(1.0f / 2.4f) - 0.055f

                                    data[0] = r
                                    data[1] = g
                                    data[2] = b
                                } else {
                                    data[0] = fptr[0]
                                    data[1] = fptr[1]
                                    data[2] = fptr[2]
                                }
                                data[3] = fptr[3]

                                val x = data[0] * 2.0f - 1.0f
                                val y = data[3] * 2.0f - 1.0f
                                var z = 1.0f - x * x - y * y
                                if (z < 0.0f)
                                    z = 0.0f
                                data[6] = (sqrt(z) * 0.5f) + 0.5f

                                // clamp to [0,1]
                                if (data[0] > 1.0f)
                                    data[0] = 1.0f
                                if (data[1] > 1.0f)
                                    data[1] = 1.0f
                                if (data[2] > 1.0f)
                                    data[2] = 1.0f
                                if (data[3] > 1.0f)
                                    data[3] = 1.0f

                                val r = data[swz.r].toSf16()
                                val g = data[swz.b].toSf16()
                                val b = data[swz.b].toSf16()
                                val a = data[swz.a].toSf16()

                                imageData16[zi][yi][4 * xi] = r
                                imageData16[zi][yi][4 * xi + 1] = g
                                imageData16[zi][yi][4 * xi + 2] = b
                                imageData16[zi][yi][4 * xi + 3] = a
                            }
                        }
                        fptr += 4
                        nptr += 1
                    }
        }
    }
}

@kotlin.ExperimentalUnsignedTypes
fun unorm16ToSf16(p: Int): Int {
    var p = p
    if (p == 0xFFFF) {
        return 0x3C00 // value of 1.0 .
    }
    if (p < 4) {
        return (p shl 8).toUShort().toInt()
    }

    val lz = Integer.numberOfLeadingZeros(p) - 16
    p = (p shl lz + 1).toUShort().toInt()
    p = (p ushr 6).toUShort().toInt()
    p = (p or (14 - lz shl 10)).toUShort().toInt()
    return p
}


fun lnsToSf16(p : Int) : Int {
    val mc = p and 0x7FF
    val ec = p shr 11
    val mt : Int
    mt = when {
        mc < 512 -> 3 * mc
        mc < 1536 -> 4 * mc - 512
        else -> 5 * mc - 2048
    }

    var res = (ec shl 10) or (mt shr 3)
    if (res >= 0x7BFF)
        res = 0x7BFF
    return res
}

fun floatToLns(p : Float) : Float {
    if(astcIsnan(p) || p <= 1.0f / 67108864.0f) {
        // underflow or NaN value, return 0.
        // We count underflow if the input value is smaller than 2^-26.
        return 0.0f
    }

    if(abs(p) >= 65536.0f) {
        // overflow, return a +INF value
        return 65535.0f
    }

    val frexPHolder = p.frexp()
    val normFrac = frexPHolder.mantissa.toFloat()
    var expo = frexPHolder.exponent
    var p1 : Float
    if (expo < -13) {
        // input number is smaller than 2^-14. In this case, multiply by 2^25.
        p1 = p * 33554432.0f
        expo = 0
    } else {
        expo += 14
        p1 = (normFrac - 0.5f) * 4096.0f
    }

    if(p1 < 384.0f)
        p1 *= 4.0f / 3.0f
    else if (p1 <= 1408.0f)
        p1 += 128.0f
    else
        p1 = (p1 + 512.0f) * (4.0f / 5.0f)

    p1 += expo * 2048.0f
    return p1 + 1.0f

}


// helper function to initialize the orig-data from the work-data
@kotlin.ExperimentalUnsignedTypes
fun imageblockInitializeOrigFromWork(pb : ImageBlock, pixelCount : Int) {
    val fptr = pb.origData.toPointer()
    val wptr = pb.workData.toPointer()

    for (i in 0 until pixelCount) {
        if (pb.rgbLns[i]) {
            fptr[0] = lnsToSf16(wptr[0].toInt()).toFloat16()
            fptr[1] = lnsToSf16(wptr[1].toInt()).toFloat16()
            fptr[2] = lnsToSf16(wptr[2].toInt()).toFloat16()
        } else {
            fptr[0] = unorm16ToSf16(wptr[0].toInt()).toFloat16()
            fptr[1] = unorm16ToSf16(wptr[1].toInt()).toFloat16()
            fptr[2] = unorm16ToSf16(wptr[2].toInt()).toFloat16()
        }
        if (pb.alphaLns[i])
            fptr[3] = lnsToSf16(wptr[3].toInt()).toFloat16()
        else
            fptr[3] = unorm16ToSf16(wptr[3].toInt()).toFloat16()

        fptr += 4
        wptr += 4
    }

    imageblockInitializeDerivFromWorkAndOrig(pb, pixelCount)
}

// helper function to initialize the work-data from the orig-data
fun imageblockInitializeWorkFromOrig(pb : ImageBlock, pixelCount : Int) {
    val fptr = pb.origData.toPointer()
    val wptr = pb.workData.toPointer()

    for (i in 0 until pixelCount) {
        if (pb.rgbLns[i]) {
            wptr[0] = floatToLns(fptr[0])
            wptr[1] = floatToLns(fptr[1])
            wptr[2] = floatToLns(fptr[2])
        } else {
            wptr[0] = fptr[0] * 65535.0f
            wptr[1] = fptr[1] * 65535.0f
            wptr[2] = fptr[2] * 65535.0f
        }

        if (pb.alphaLns[i])
            wptr[3] = floatToLns(fptr[3])
        else
            wptr[3] = fptr[3] * 65535.0f
        fptr += 4
        wptr += 4
    }

    pb.origData = fptr.data.toNonNullableArray()
    pb.workData = wptr.data.toNonNullableArray()

    imageblockInitializeDerivFromWorkAndOrig(pb, pixelCount)
}

// helper function to initialize the orig-data from the work-data
fun imageblockInitializeDerivFromWorkAndOrig(pb : ImageBlock, pixelCount: Int) {
    val fptr = pb.origData.toPointer()
    val wptr = pb.workData.toPointer()
    val dptr = pb.derivData.toPointer()

    for (i in 0 until pixelCount) {
        // compute derivatives for RGB first
        if (pb.rgbLns[i]) {
            val r = max(fptr[0], 6e-5f)
            val g = max(fptr[1], 6e-5f)
            val b = max(fptr[2], 6e-5f)

            var rderiv = (floatToLns(r * 1.05f) - floatToLns(r)) / (r * 0.05f)
            var gderiv = (floatToLns(g * 1.05f) - floatToLns(g)) / (g * 0.05f)
            var bderiv = (floatToLns(b * 1.05f) - floatToLns(b)) / (b * 0.05f)

            // the derivative may not actually take values smaller than 1/32 or larger than 2^25;
            // if it does, we clamp it.
            if (rderiv < (1.0f / 32.0f))
                rderiv = (1.0f / 32.0f)
            else if (rderiv > 33554432.0f)
                rderiv = 33554432.0f

            if (gderiv < (1.0f / 32.0f))
                gderiv = (1.0f / 32.0f)
            else if (gderiv > 33554432.0f)
                gderiv = 33554432.0f

            if (bderiv < (1.0f / 32.0f))
                bderiv = (1.0f / 32.0f)
            else if (bderiv > 33554432.0f)
                bderiv = 33554432.0f

            dptr[0] = rderiv
            dptr[1] = gderiv
            dptr[2] = bderiv
        } else {
            dptr[0] = 65535.0f
            dptr[1] = 65535.0f
            dptr[2] = 65535.0f
        }

        // then compute derivatives for Alpha
        if(pb.alphaLns[i]) {
            val a = max(fptr[3], 6e-5f)
            var aderiv = (floatToLns(a * 1.05f) - floatToLns(a)) / (a * 0.05f)
            // the derivative may not actually take values smaller than 1/32 or larger than 2^25;
            // if it does, we clamp it.
            if (aderiv < 1.0f / 32.0f)
                aderiv = 1.0f / 32.0f
            else if (aderiv > 33554432.0f)
                aderiv = 33554432.0f

            dptr[3] = aderiv
        } else
            dptr[3] = 65535.0f

        fptr += 4
        wptr += 4
        dptr += 4

    }

    pb.origData = fptr.data.toNonNullableArray()
    pb.workData = wptr.data.toNonNullableArray()
    pb.derivData = dptr.data.toNonNullableArray()
}

/**
   For an imageblock, update its flags.

   The updating is done based on work_data, not orig_data.
*/
fun updateImageblockFlags(pb : ImageBlock, xDim : Int, yDim : Int, zDim : Int) {
    var redMin = 1e38f; var redMax = -1e38f
    var greenMin = 1e38f; var greenMax = -1e38f
    var blueMin = 1e38f; var blueMax = -1e38f
    var alphaMin = 1e38f; var alphaMax = -1e38f

    val texelsPerBlock = xDim * yDim * zDim

    var grayScale = 1

    for (i in 0 until texelsPerBlock) {
        val red = pb.workData[4 * i]
        val green = pb.workData[4 * i + 1]
        val blue = pb.workData[4 * i + 2]
        val alpha = pb.workData[4 * i + 3]
        if (red < redMin)
            redMin = red
        if (red > redMax)
            redMax = red
        if (green < greenMin)
            greenMin = green
        if (green > greenMax)
            greenMax = green
        if (blue < blueMin)
            blueMin = blue
        if (blue > blueMax)
            blueMax = blue
        if (alpha < alphaMin)
            alphaMin = alpha
        if (alpha > alphaMax)
            alphaMax = alpha

        if (grayScale == 1 && (red != green || red != blue))
            grayScale = 0

    }

    pb.redMin = redMin
    pb.redMax = redMax
    pb.greenMin = greenMin
    pb.greenMax = greenMax
    pb.blueMin = blueMin
    pb.blueMax = blueMax
    pb.alphaMin = alphaMin
    pb.alphaMax = alphaMax
    pb.grayScale = grayScale

}