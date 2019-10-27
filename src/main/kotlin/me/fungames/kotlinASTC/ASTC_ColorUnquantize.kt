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
 *	@brief	Color unquantization functions for ASTC.
 *	@author rewritten to Kotlin by FunGames
 */
/*----------------------------------------------------------------------------*/

@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")
package me.fungames.kotlinASTC


data class UnpackColorEndpointsResult(
    val colorEndpoint0 : UShort4,
    val colorEndpoint1 : UShort4,
    val rgbHdrEndpoint : Boolean,
    val alphaHdrEndpoint : Boolean,
    val nanEndpoint : Boolean
)

fun unpackColorEndpoints(decodeMode: AstcDecodeMode, format : Int, quantizationLevel: Int, input : IntArray) : UnpackColorEndpointsResult {
    val output0 = UShort4(0u, 0u, 0u, 0u)
    val output1 = UShort4(0u, 0u, 0u, 0u)
    var rgbHdr = 0
    var alphaHdr = 0
    var nanEndpoint = 0

    when(EndpointFormats.getByValue(format)) {
        EndpointFormats.FMT_LUMINANCE -> {
            rgbHdr = 0
            alphaHdr = 0
            luminanceUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_LUMINANCE_DELTA -> {
            rgbHdr = 0
            alphaHdr = 0
            luminanceDeltaUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_HDR_LUMINANCE_SMALL_RANGE -> {
            rgbHdr = 1
            alphaHdr = -1
            hdrLuminanceSmallRangeUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_HDR_LUMINANCE_LARGE_RANGE -> {
            rgbHdr = 1
            alphaHdr = -1
            hdrLuminanceLargeRangeUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_LUMINANCE_ALPHA -> {
            rgbHdr = 0
            alphaHdr = 0
            luminanceAlphaUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_LUMINANCE_ALPHA_DELTA -> {
            rgbHdr = 0
            alphaHdr = 0
            luminanceAlphaDeltaUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_RGB_SCALE -> {
            rgbHdr = 0
            alphaHdr = 0
            rgbScaleUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_RGB_SCALE_ALPHA -> {
            rgbHdr = 0
            alphaHdr = 0
            rgbScaleAlphaUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_HDR_RGB_SCALE -> {
            rgbHdr = 1
            alphaHdr = -1
            hdrRgboUnpack3(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_RGB -> {
            rgbHdr = 0
            alphaHdr = 0
            rgbUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_RGB_DELTA -> {
            rgbHdr = 0
            alphaHdr = 0
            rgbDeltaUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_HDR_RGB -> {
            rgbHdr = 1
            alphaHdr = -1
            hdrRgbUnpack3(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_RGBA -> {
            rgbHdr = 0
            alphaHdr = 0
            rgbaUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_RGBA_DELTA -> {
            rgbHdr = 0
            alphaHdr = 0
            rgbaDeltaUnpack(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_HDR_RGB_LDR_ALPHA -> {
            rgbHdr = 1
            alphaHdr = 0
            hdrRgbLdrAlphaUnpack3(input, quantizationLevel, output0, output1)
        }
        EndpointFormats.FMT_HDR_RGBA -> {
            rgbHdr = 1
            alphaHdr = -1
            hdrRgbLdrAlphaUnpack3(input, quantizationLevel, output0, output1)
        }
    }

    if (alphaHdr == -1) {
        if(alphaForceUseOfHdr) {
            output0.w = 0x7800u
            output1.w = 0x7800u
            alphaHdr = 1
        } else {
            output0.w = 0x00FFu
            output1.w = 0x00FFu
            alphaHdr = 0
        }
    }

    when(decodeMode) {
        AstcDecodeMode.DECODE_LDR_SRGB -> {
            if (rgbHdr == 1) {
                output0.x = 0xFF00u
                output0.y = 0x0000u
                output0.z = 0xFF00u
                output0.w = 0xFF00u
                output1.x = 0xFF00u
                output1.y = 0x0000u
                output1.z = 0xFF00u
                output1.w = 0xFF00u
            } else {
                output0.x = (output0.x * 257u).toUShort()
                output0.y = (output0.y * 257u).toUShort()
                output0.z = (output0.z * 257u).toUShort()
                output0.w = (output0.w * 257u).toUShort()
                output1.x = (output1.x * 257u).toUShort()
                output1.y = (output1.y * 257u).toUShort()
                output1.z = (output1.z * 257u).toUShort()
                output1.w = (output1.w * 257u).toUShort()
            }
            rgbHdr = 0
            alphaHdr = 0
        }
        AstcDecodeMode.DECODE_LDR -> {
            if (rgbHdr == 1) {
                output0.x = 0xFFFFu
                output0.y = 0xFFFFu
                output0.z = 0xFFFFu
                output0.w = 0xFFFFu
                output1.x = 0xFFFFu
                output1.y = 0xFFFFu
                output1.z = 0xFFFFu
                output1.w = 0xFFFFu
                nanEndpoint = 1
            } else {
                output0.x = (output0.x * 257u).toUShort()
                output0.y = (output0.y * 257u).toUShort()
                output0.z = (output0.z * 257u).toUShort()
                output0.w = (output0.w * 257u).toUShort()
                output1.x = (output1.x * 257u).toUShort()
                output1.y = (output1.y * 257u).toUShort()
                output1.z = (output1.z * 257u).toUShort()
                output1.w = (output1.w * 257u).toUShort()
            }
            rgbHdr = 0
            alphaHdr = 0
        }
        AstcDecodeMode.DECODE_HDR -> {
            if (rgbHdr == 0)
            {
                output0.x = (output0.x * 257u).toUShort()
                output0.y = (output0.y * 257u).toUShort()
                output0.z = (output0.z * 257u).toUShort()
                output1.x = (output1.x * 257u).toUShort()
                output1.y = (output1.y * 257u).toUShort()
                output1.z = (output1.z * 257u).toUShort()
            }
            if (alphaHdr == 0)
            {
                output0.w = (output0.w * 257u).toUShort()
                output1.w = (output1.w * 257u).toUShort()
            }
        }
    }

    return UnpackColorEndpointsResult(
        output0,
        output1,
        rgbHdr != 0,
        alphaHdr != 0,
        nanEndpoint != 0
    )
}

fun rgbDeltaUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) : Int {
    // unquantize the color endpoints
    var r0 = colorUnquantizationTables[quantizationLevel][input[0]].toInt()
    var g0 = colorUnquantizationTables[quantizationLevel][input[2]].toInt()
    var b0 = colorUnquantizationTables[quantizationLevel][input[4]].toInt()

    var r1 = colorUnquantizationTables[quantizationLevel][input[1]].toInt()
    var g1 = colorUnquantizationTables[quantizationLevel][input[3]].toInt()
    var b1 = colorUnquantizationTables[quantizationLevel][input[5]].toInt()

    // perform the bit-transfer procedure
    r0 = r0 or (r1 and 0x80 shl 1)
    g0 = g0 or (g1 and 0x80 shl 1)
    b0 = b0 or (b1 and 0x80 shl 1)
    r1 = r1 and 0x7F
    g1 = g1 and 0x7F
    b1 = b1 and 0x7F
    if ((r1 and 0x40) != 0)
        r1 -= 0x80
    if ((g1 and 0x40) != 0)
        g1 -= 0x80
    if ((b1 and 0x40) != 0)
        b1 -= 0x80

    r0 = r0 shr 1
    g0 = g0 shr 1
    b0 = b0 shr 1
    r1 = r1 shr 1
    g1 = g1 shr 1
    b1 = b1 shr 1

    val rgbsum = r1 + g1 + b1

    r1 += r0
    g1 += g0
    b1 += b0


    val retval: Int

    var r0e: Int
    var g0e: Int
    var b0e: Int
    var r1e: Int
    var g1e: Int
    var b1e: Int

    if (rgbsum >= 0) {
        r0e = r0
        g0e = g0
        b0e = b0

        r1e = r1
        g1e = g1
        b1e = b1

        retval = 0
    } else {
        r0e = r1 + b1 shr 1
        g0e = g1 + b1 shr 1
        b0e = b1

        r1e = r0 + b0 shr 1
        g1e = g0 + b0 shr 1
        b1e = b0

        retval = 1
    }

    if (r0e < 0)
        r0e = 0
    else if (r0e > 255)
        r0e = 255

    if (g0e < 0)
        g0e = 0
    else if (g0e > 255)
        g0e = 255

    if (b0e < 0)
        b0e = 0
    else if (b0e > 255)
        b0e = 255

    if (r1e < 0)
        r1e = 0
    else if (r1e > 255)
        r1e = 255

    if (g1e < 0)
        g1e = 0
    else if (g1e > 255)
        g1e = 255

    if (b1e < 0)
        b1e = 0
    else if (b1e > 255)
        b1e = 255

    output0.setValues(r0e.toUShort(), g0e.toUShort(), b0e.toUShort(), 255u)
    output1.setValues(r1e.toUShort(), g1e.toUShort(), b1e.toUShort(), 255u)

    return retval
}

fun rgbUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) : Int {
    var ri0b = colorUnquantizationTables[quantizationLevel][input[0]].toInt()
    var ri1b = colorUnquantizationTables[quantizationLevel][input[1]].toInt()
    var gi0b = colorUnquantizationTables[quantizationLevel][input[2]].toInt()
    var gi1b = colorUnquantizationTables[quantizationLevel][input[3]].toInt()
    val bi0b = colorUnquantizationTables[quantizationLevel][input[4]].toInt()
    val bi1b = colorUnquantizationTables[quantizationLevel][input[5]].toInt()

    return if (ri0b + gi0b + bi0b > ri1b + gi1b + bi1b) {
        // blue-contraction
        ri0b = ri0b + bi0b shr 1
        gi0b = gi0b + bi0b shr 1
        ri1b = ri1b + bi1b shr 1
        gi1b = gi1b + bi1b shr 1

        output0.setValues(ri1b.toUShort(), gi1b.toUShort(), bi1b.toUShort(), 255u)
        output1.setValues(ri0b.toUShort(), gi0b.toUShort(), bi0b.toUShort(), 255u)
        1
    } else {
        output0.setValues(ri0b.toUShort(), gi0b.toUShort(), bi0b.toUShort(), 255u)
        output1.setValues(ri1b.toUShort(), gi1b.toUShort(), bi1b.toUShort(), 255u)
        0
    }

}

fun rgbaUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    val order = rgbUnpack(input, quantizationLevel, output0, output1)
    if (order == 0) {
        output0.w = colorUnquantizationTables[quantizationLevel][input[6]].toUShort()
        output1.w = colorUnquantizationTables[quantizationLevel][input[7]].toUShort()
    } else {
        output0.w = colorUnquantizationTables[quantizationLevel][input[7]].toUShort()
        output1.w = colorUnquantizationTables[quantizationLevel][input[6]].toUShort()
    }
}

fun rgbaDeltaUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    var a0 = colorUnquantizationTables[quantizationLevel][input[6]].toInt()
    var a1 = colorUnquantizationTables[quantizationLevel][input[7]].toInt()
    a0 = a0 or (a1 and 0x80 shl 1)
    a1 = a1 and 0x7F
    if ((a1 and 0x40) != 0)
        a1 -= 0x80
    a0 = a0 shr 1
    a1 = a1 shr 1
    a1 += a0

    if (a1 < 0)
        a1 = 0
    else if (a1 > 255)
        a1 = 255
    val order = rgbDeltaUnpack(input, quantizationLevel, output0, output1)
    if (order == 0) {
        output0.w = a0.toUShort()
        output1.w = a1.toUShort()
    } else {
        output0.w = a1.toUShort()
        output1.w = a0.toUShort()
    }
}

fun rgbScaleUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    val ir = colorUnquantizationTables[quantizationLevel][input[0]].toInt()
    val ig = colorUnquantizationTables[quantizationLevel][input[1]].toInt()
    val ib = colorUnquantizationTables[quantizationLevel][input[2]].toInt()

    val iscale = colorUnquantizationTables[quantizationLevel][input[3]].toInt()
    output1.setValues(ir.toUShort(), ig.toUShort(), ib.toUShort(), 255u)
    output0.setValues(((ir * iscale) shr 8).toUShort(), ((ig * iscale) shr 8).toUShort(),
        ((ib * iscale) shr 8).toUShort(), 255u)
}

fun rgbScaleAlphaUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    rgbScaleUnpack(input, quantizationLevel, output0, output1)
    output0.w = colorUnquantizationTables[quantizationLevel][input[4]].toUShort()
    output1.w = colorUnquantizationTables[quantizationLevel][input[5]].toUShort()
}

fun luminanceUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    val lum0 = colorUnquantizationTables[quantizationLevel][input[0]].toUShort()
    val lum1 = colorUnquantizationTables[quantizationLevel][input[1]].toUShort()
    output0.setValues(lum0, lum0, lum0, 255u)
    output1.setValues(lum1, lum1, lum1, 255u)
}

fun luminanceDeltaUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    val v0 = colorUnquantizationTables[quantizationLevel][input[0]].toInt()
    val v1 = colorUnquantizationTables[quantizationLevel][input[1]].toInt()
    val l0 = (v0 shr 2) or (v1 and 0xC0)
    var l1 = l0 + (v1 and 0x3F)

    if (l1 > 255)
        l1 = 255

    output0.setValues(v0.toUShort(), v0.toUShort(), v0.toUShort(), 255u)
    output1.setValues(l1.toUShort(), l1.toUShort(), l1.toUShort(), 255u)
}

fun luminanceAlphaUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    val lum0 = colorUnquantizationTables[quantizationLevel][input[0]].toUShort()
    val lum1 = colorUnquantizationTables[quantizationLevel][input[1]].toUShort()
    val alpha0 = colorUnquantizationTables[quantizationLevel][input[2]].toUShort()
    val alpha1 = colorUnquantizationTables[quantizationLevel][input[3]].toUShort()
    output0.setValues(lum0, lum0, lum0, alpha0)
    output1.setValues(lum1, lum1, lum1, alpha1)
}

fun luminanceAlphaDeltaUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    var lum0 = colorUnquantizationTables[quantizationLevel][input[0]].toInt()
    var lum1 = colorUnquantizationTables[quantizationLevel][input[1]].toInt()
    var alpha0 = colorUnquantizationTables[quantizationLevel][input[2]].toInt()
    var alpha1 = colorUnquantizationTables[quantizationLevel][input[3]].toInt()

    lum0 = lum0 or (lum1 and 0x80 shl 1)
    alpha0 = alpha0 or (alpha1 and 0x80 shl 1)
    lum1 = lum1 and 0x7F
    alpha1 = alpha1 and 0x7F
    if ((lum1 and 0x40) != 0)
        lum1 -= 0x80
    if ((alpha1 and 0x40) != 0)
        alpha1 -= 0x80

    lum0 = lum0 shr 1
    lum1 = lum1 shr 1
    alpha0 = alpha0 shr 1
    alpha1 = alpha1 shr 1
    lum1 += lum0
    alpha1 += alpha0

    if (lum1 < 0)
        lum1 = 0
    else if (lum1 > 255)
        lum1 = 255

    if (alpha1 < 0)
        alpha1 = 0
    else if (alpha1 > 255)
        alpha1 = 255

    output0.setValues(lum0.toUShort(), lum0.toUShort(), lum0.toUShort(), alpha0.toUShort())
    output1.setValues(lum1.toUShort(), lum1.toUShort(), lum1.toUShort(), alpha1.toUShort())
}

// RGB-offset format
fun hdrRgboUnpack3(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    val v0 = colorUnquantizationTables[quantizationLevel][input[0]].toInt()
    val v1 = colorUnquantizationTables[quantizationLevel][input[1]].toInt()
    val v2 = colorUnquantizationTables[quantizationLevel][input[2]].toInt()
    val v3 = colorUnquantizationTables[quantizationLevel][input[3]].toInt()

    val modeval = v0 and 0xC0 shr 6 or (v1 and 0x80 shr 7 shl 2) or (v2 and 0x80 shr 7 shl 3)

    val majcomp: Int
    val mode: Int
    when {
        modeval and 0xC != 0xC -> {
            majcomp = modeval shr 2
            mode = modeval and 3
        }
        modeval != 0xF -> {
            majcomp = modeval and 3
            mode = 4
        }
        else -> {
            majcomp = 0
            mode = 5
        }
    }

    var red = v0 and 0x3F
    var green = v1 and 0x1F
    var blue = v2 and 0x1F
    var scale = v3 and 0x1F

    val bit0 = v1 shr 6 and 1
    val bit1 = v1 shr 5 and 1
    val bit2 = v2 shr 6 and 1
    val bit3 = v2 shr 5 and 1
    val bit4 = v3 shr 7 and 1
    val bit5 = v3 shr 6 and 1
    val bit6 = v3 shr 5 and 1

    val ohcomp = 1 shl mode

    if (ohcomp and 0x30 != 0) {
        green = green or (bit0 shl 6)
    }
    if (ohcomp and 0x3A != 0) {
        green = green or (bit1 shl 5)
    }
    if (ohcomp and 0x30 != 0) {
        blue = blue or (bit2 shl 6)
    }
    if (ohcomp and 0x3A != 0) {
        blue = blue or (bit3 shl 5)
    }

    if (ohcomp and 0x3D != 0) {
        scale = scale or (bit6 shl 5)
    }
    if (ohcomp and 0x2D != 0) {
        scale = scale or (bit5 shl 6)
    }
    if (ohcomp and 0x04 != 0) {
        scale = scale or (bit4 shl 7)
    }

    if (ohcomp and 0x3B != 0) {
        red = red or (bit4 shl 6)
    }
    if (ohcomp and 0x04 != 0) {
        red = red or (bit3 shl 6)
    }

    if (ohcomp and 0x10 != 0) {
        red = red or (bit5 shl 7)
    }
    if (ohcomp and 0x0F != 0) {
        red = red or (bit2 shl 7)
    }

    if (ohcomp and 0x05 != 0) {
        red = red or (bit1 shl 8)
    }
    if (ohcomp and 0x0A != 0) {
        red = red or (bit0 shl 8)
    }

    if (ohcomp and 0x05 != 0) {
        red = red or (bit0 shl 9)
    }
    if (ohcomp and 0x02 != 0) {
        red = red or (bit6 shl 9)
    }

    if (ohcomp and 0x01 != 0) {
        red = red or (bit3 shl 10)
    }
    if (ohcomp and 0x02 != 0) {
        red = red or (bit5 shl 10)
    }


    // expand to 12 bits.
    val shamts = intArrayOf(1, 1, 2, 3, 4, 5)
    val shamt = shamts[mode]
    red = red shl shamt
    green = green shl shamt
    blue = blue shl shamt
    scale = scale shl shamt

    // on modes 0 to 4, the values stored for "green" and "blue" are differentials,
    // not absolute values.
    if (mode != 5) {
        green = red - green
        blue = red - blue
    }

    // switch around components.
    val temp: Int
    when (majcomp) {
        1 -> {
            temp = red
            red = green
            green = temp
        }
        2 -> {
            temp = red
            red = blue
            blue = temp
        }
    }


    var red0 = red - scale
    var green0 = green - scale
    var blue0 = blue - scale

    // clamp to [0,0xFFF].
    if (red < 0) {
        red = 0
    }
    if (green < 0) {
        green = 0
    }
    if (blue < 0) {
        blue = 0
    }

    if (red0 < 0) {
        red0 = 0
    }
    if (green0 < 0) {
        green0 = 0
    }
    if (blue0 < 0) {
        blue0 = 0
    }
    output0.setValues((red0 shl 4).toUShort(), (green0 shl 4).toUShort(), (blue0 shl 4).toUShort(), 0x7800u)
    output1.setValues((red shl 4).toUShort(), (green shl 4).toUShort(), (blue shl 4).toUShort(), 0x7800u)
}

fun hdrRgbUnpack3(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    val v0 = colorUnquantizationTables[quantizationLevel][input[0]].toInt()
    val v1 = colorUnquantizationTables[quantizationLevel][input[1]].toInt()
    val v2 = colorUnquantizationTables[quantizationLevel][input[2]].toInt()
    val v3 = colorUnquantizationTables[quantizationLevel][input[3]].toInt()
    val v4 = colorUnquantizationTables[quantizationLevel][input[4]].toInt()
    val v5 = colorUnquantizationTables[quantizationLevel][input[5]].toInt()

    // extract all the fixed-placement bitfields
    val modeval = v1 and 0x80 shr 7 or (v2 and 0x80 shr 7 shl 1) or (v3 and 0x80 shr 7 shl 2)

    val majcomp = v4 and 0x80 shr 7 or (v5 and 0x80 shr 7 shl 1)

    if(majcomp == 3) {
        output0.setValues((v0 shl 8).toUShort(), (v2 shl 8).toUShort(), ((v4 and 0x7F) shl 9).toUShort(), 0x7800u)
        output1.setValues((v1 shl 8).toUShort(), (v3 shl 8).toUShort(), ((v5 and 0x7F) shl 9).toUShort(), 0x7800u)
    }

    var a = v0 or (v1 and 0x40 shl 2)
    var b0 = v2 and 0x3f
    var b1 = v3 and 0x3f
    var c = v1 and 0x3f
    var d0 = v4 and 0x7f
    var d1 = v5 and 0x7f

    // get hold of the number of bits in 'd0' and 'd1'
    val dbitsTab = intArrayOf(7, 6, 7, 6, 5, 6, 5, 6)
    val dbits = dbitsTab[modeval]

    // extract six variable-placement bits
    val bit0 = v2 shr 6 and 1
    val bit1 = v3 shr 6 and 1

    val bit2 = v4 shr 6 and 1
    val bit3 = v5 shr 6 and 1
    val bit4 = v4 shr 5 and 1
    val bit5 = v5 shr 5 and 1

    // and prepend the variable-placement bits depending on mode.
    val ohmod = 1 shl modeval    // one-hot-mode
    if ((ohmod and 0xA4) != 0)
        a = a or (bit0 shl 9)
    if ((ohmod and 0x8) != 0)
        a = a or (bit2 shl 9)
    if ((ohmod and 0x50) != 0)
        a = a or (bit4 shl 9)

    if ((ohmod and 0x50) != 0)
        a = a or (bit5 shl 10)
    if ((ohmod and 0xA0) != 0)
        a = a or (bit1 shl 10)

    if ((ohmod and 0xC0) != 0)
        a = a or (bit2 shl 11)

    if ((ohmod and 0x4) != 0)
        c = c or (bit1 shl 6)
    if ((ohmod and 0xE8) != 0)
        c = c or (bit3 shl 6)

    if ((ohmod and 0x20) != 0)
        c = c or (bit2 shl 7)


    if ((ohmod and 0x5B) != 0)
        b0 = b0 or (bit0 shl 6)
    if ((ohmod and 0x5B) != 0)
        b1 = b1 or (bit1 shl 6)

    if ((ohmod and 0x12) != 0)
        b0 = b0 or (bit2 shl 7)
    if ((ohmod and 0x12) != 0)
        b1 = b1 or (bit3 shl 7)

    if ((ohmod and 0xAF) != 0)
        d0 = d0 or (bit4 shl 5)
    if ((ohmod and 0xAF) != 0)
        d1 = d1 or (bit5 shl 5)
    if ((ohmod and 0x5) != 0)
        d0 = d0 or (bit2 shl 6)
    if ((ohmod and 0x5) != 0)
        d1 = d1 or (bit3 shl 6)

    // sign-extend 'd0' and 'd1'
    // note: this code assumes that signed right-shift actually sign-fills, not zero-fills.
    var d0x = d0
    var d1x = d1
    val sxShamt = 32 - dbits
    d0x = d0x shl sxShamt
    d0x = d0x shr sxShamt
    d1x = d1x shl sxShamt
    d1x = d1x shr sxShamt
    d0 = d0x
    d1 = d1x

    // expand all values to 12 bits, with left-shift as needed.
    val valShamt = modeval shr 1 xor 3
    a = a shl valShamt
    b0 = b0 shl valShamt
    b1 = b1 shl valShamt
    c = c shl valShamt
    d0 = d0 shl valShamt
    d1 = d1 shl valShamt

    // then compute the actual color values.
    var red1 = a
    var green1 = a - b0
    var blue1 = a - b1
    var red0 = a - c
    var green0 = a - b0 - c - d0
    var blue0 = a - b1 - c - d1

    // clamp the color components to [0,2^12 - 1]
    if (red0 < 0)
        red0 = 0
    else if (red0 > 0xFFF)
        red0 = 0xFFF

    if (green0 < 0)
        green0 = 0
    else if (green0 > 0xFFF)
        green0 = 0xFFF

    if (blue0 < 0)
        blue0 = 0
    else if (blue0 > 0xFFF)
        blue0 = 0xFFF

    if (red1 < 0)
        red1 = 0
    else if (red1 > 0xFFF)
        red1 = 0xFFF

    if (green1 < 0)
        green1 = 0
    else if (green1 > 0xFFF)
        green1 = 0xFFF

    if (blue1 < 0)
        blue1 = 0
    else if (blue1 > 0xFFF)
        blue1 = 0xFFF


    // switch around the color components
    val temp0: Int
    val temp1: Int
    when (majcomp) {
        1                    // switch around red and green
        -> {
            temp0 = red0
            temp1 = red1
            red0 = green0
            red1 = green1
            green0 = temp0
            green1 = temp1
        }
        2                    // switch around red and blue
        -> {
            temp0 = red0
            temp1 = red1
            red0 = blue0
            red1 = blue1
            blue0 = temp0
            blue1 = temp1
        }
        0                    // no switch
        -> {
        }
    }

    output0.setValues((red0 shl 4).toUShort(), (green0 shl 4).toUShort(), (blue0 shl 4).toUShort(), 0x7800u)
    output1.setValues((red1 shl 4).toUShort(), (green1 shl 4).toUShort(), (blue1 shl 4).toUShort(), 0x7800u)
}

fun hdrRgbLdrAlphaUnpack3(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    hdrRgbUnpack3(input, quantizationLevel, output0, output1)

    val v6 = colorUnquantizationTables[quantizationLevel][input[6]].toUShort()
    val v7 = colorUnquantizationTables[quantizationLevel][input[7]].toUShort()
    output0.w = v6
    output1.w = v7
}

fun hdrLuminanceSmallRangeUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    val v0 = colorUnquantizationTables[quantizationLevel][input[0]].toInt()
    val v1 = colorUnquantizationTables[quantizationLevel][input[1]].toInt()
    val y0: Int
    var y1: Int
    if ((v0 and 0x80) != 0) {
        y0 = v1 and 0xE0 shl 4 or (v0 and 0x7F shl 2)
        y1 = v1 and 0x1F shl 2
    } else {
        y0 = v1 and 0xF0 shl 4 or (v0 and 0x7F shl 1)
        y1 = v1 and 0xF shl 1
    }

    y1 += y0
    if (y1 > 0xFFF)
        y1 = 0xFFF

    output0.setValues((y0 shl 4).toUShort(), (y0 shl 4).toUShort(), (y0 shl 4).toUShort(), 0x7800u)
    output1.setValues((y1 shl 4).toUShort(), (y1 shl 4).toUShort(), (y1 shl 4).toUShort(), 0x7800u)
}

fun hdrLuminanceLargeRangeUnpack(input: IntArray, quantizationLevel: Int, output0: UShort4, output1: UShort4) {
    val v0 = colorUnquantizationTables[quantizationLevel][input[0]].toInt()
    val v1 = colorUnquantizationTables[quantizationLevel][input[1]].toInt()

    val y0: Int
    val y1: Int
    if (v1 >= v0) {
        y0 = v0 shl 4
        y1 = v1 shl 4
    } else {
        y0 = (v1 shl 4) + 8
        y1 = (v0 shl 4) - 8
    }
    output0.setValues((y0 shl 4).toUShort(), (y0 shl 4).toUShort(), (y0 shl 4).toUShort(), 0x7800u)
    output1.setValues((y1 shl 4).toUShort(), (y1 shl 4).toUShort(), (y1 shl 4).toUShort(), 0x7800u)
}
