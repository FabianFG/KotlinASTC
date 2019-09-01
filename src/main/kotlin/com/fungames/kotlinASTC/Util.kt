package com.fungames.kotlinASTC

inline fun <reified T> Array<T>.toPointer() = Pointer(this.toNullableArray())

inline fun <reified T> Array<T>.toNullableArray() = this as Array<T?>

inline fun <reified T> Array<T?>.toNonNullableArray() = this as Array<T>

data class Int4(
    var x: Int,
    var y: Int,
    var z: Int,
    var w: Int
) {
    fun setValues(x: Int, y: Int, z: Int, w: Int) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    operator fun plus(i: Int): Int4 {
        return Int4(x + i, y + i, z + i, w + i)
    }

    operator fun plus(i: Int4): Int4 {
        return Int4(x + i.x, y + i.y, z + i.z, w + i.w)
    }

    operator fun minus(i: Int): Int4 {
        return Int4(x - i, y - i, z - i, w - i)
    }

    operator fun minus(i: Int4): Int4 {
        return Int4(x - i.x, y - i.y, z - i.z, w - i.w)
    }

    operator fun times(i: Int4): Int4 {
        return Int4(x * i.x, y * i.y, z * i.z, w * i.w)
    }

    operator fun times(i: Int): Int4 {
        return Int4(x * i, y * i, z * i, w * i)
    }

    operator fun div(i: Int4): Int4 {
        return Int4(x / i.x, y / i.y, z / i.z, w / i.w)
    }

    operator fun div(i: Int): Int4 {
        return Int4(x / i, y / i, z / i, w / i)
    }

    infix fun shl(bitCount: Int): Int4 {
        return Int4(x shl bitCount, y shl bitCount, z shl bitCount, w shl bitCount)
    }

    infix fun shr(bitCount: Int): Int4 {
        return Int4(x shr bitCount, y shr bitCount, z shr bitCount, w shr bitCount)
    }

    infix fun or(other: Int4): Int4 {
        return Int4(x or other.x, y or other.y, z or other.z, w or other.w)
    }

    infix fun and(other: Vector4): Int4 {
        return Int4(x and other.x, y and other.y, z and other.z, w and other.w)
    }
}

data class Vector4(
    var x: Int,
    var y: Int,
    var z: Int,
    var w: Int
) {
    fun setValues(x: Int, y: Int, z: Int, w: Int) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }
}

fun Float.toSf16(): Int {
    val fbits = this.toBits()
    val sign = fbits.ushr(16) and 0x8000          // sign only
    var `val` = (fbits and 0x7fffffff) + 0x1000 // rounded value

    if (`val` >= 0x47800000)
    // might be or become NaN/Inf
    {                                     // avoid Inf due to rounding
        return if (fbits and 0x7fffffff >= 0x47800000) {                                 // is or must become NaN/Inf
            if (`val` < 0x7f800000) sign or 0x7c00 else sign or 0x7c00 or        // remains +/-Inf or NaN

                    (fbits and 0x007fffff).ushr(13)     // make it +/-Inf
            // keep NaN (and Inf) bits
        } else sign or 0x7bff
// unrounded not quite Inf
    }
    if (`val` >= 0x38800000)
    // remains normalized value
        return sign or (`val` - 0x38000000).ushr(13) // exp - 127 + 15
    if (`val` < 0x33000000)
    // too small for subnormal
        return sign                      // becomes +/-0
    `val` = (fbits and 0x7fffffff).ushr(23)  // tmp exp for subnormal calc
    return sign or ((fbits and 0x7fffff or 0x800000) // add subnormal bit
            + 0x800000.ushr(`val` - 102))     // round depending on cut off
        .ushr(126 - `val`)   // div by 2^(1-(exp-127+15)) and >> 13 | exp=0
}

fun Int.toFloat16(): Float = this.toUShort().toFloat16()

fun UShort.toFloat16(): Float {
    val hbits = this.toInt()
    var mant = hbits and 0x03ff // 10 bits mantissa
    var exp = hbits and 0x7c00 // 5 bits exponent
    if (exp == 0x7c00)
    // NaN/Inf
        exp = 0x3fc00 // -> NaN/Inf
    else if (exp != 0)
    // normalized value
    {
        exp += 0x1c000 // exp - 15 + 127
        if (mant == 0 && exp > 0x1c400)
        // smooth transition
            return Float.fromBits(hbits and 0x8000 shl 16 or (exp shl 13) or 0x3ff)
    } else if (mant != 0)
    // && exp==0 -> subnormal
    {
        exp = 0x1c400 // make it normal
        do {
            mant = mant shl 1 // mantissa * 2
            exp -= 0x400 // decrease exp by 1
        } while (mant and 0x400 == 0) // while not normal
        mant = mant and 0x3ff // discard subnormal bit
    } // else +/-0 -> +/-0
    val f = 0.0f
    return Float.fromBits( // combine all parts
        hbits and 0x8000 shl 16 // sign << ( 31 - 15 )
                or (exp or mant shl 13)
    ) // value << ( 23 - 10 )
}

fun Float.frexp() = this.toDouble().frexp()

fun Double.frexp(): FrexPHolder {
    val value = this

    if (value == 0.0 || value == -0.0)
        return FrexPHolder(0, 0.0)

    if (value.isNaN())
        return FrexPHolder(-1, Double.NaN)

    if (value.isInfinite())
        return FrexPHolder(-1, value)

    var mantissa = value
    var exponent = 0
    var sign = 1

    if (mantissa < 0f) {
        sign--
        mantissa = -mantissa
    }
    while (mantissa < 0.5f) {
        mantissa *= 2.0f
        exponent -= 1
    }
    while (mantissa >= 1.0f) {
        mantissa *= 0.5f
        exponent++
    }
    mantissa *= sign
    return FrexPHolder(exponent, mantissa)
}

data class FrexPHolder(
    val exponent: Int,
    val mantissa: Double
)