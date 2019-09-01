package com.fungames.kotlinASTC

class Pointer<T> (val data : Array<T?>) {

    private var pos = 0
    val size : Int = data.size

    operator fun plusAssign(i : Int) {
        rangeCheck(pos + i)
        pos += i
    }

    operator fun minusAssign(i : Int) {
        rangeCheck(pos - i)
        pos -= i
    }

    operator fun inc() : Pointer<T> {
        plusAssign(1)
        return this
    }

    operator fun dec() : Pointer<T> {
        minusAssign(1)
        return this
    }

    operator fun get(i : Int) : T {
        rangeCheck(pos + i)
        return data[pos + i] ?: throw IllegalStateException("Value at ${pos + 1} is not initialized yet")
    }

    operator fun set(i : Int, b : T) {
        rangeCheck(pos + i)
        data[pos + i] = b
    }

    operator fun minus(decrement : Int) : Pointer<T> {
        val p = Pointer(this.data)
        p.pos = pos - decrement
        return p
    }

    operator fun plus(increment : Int) : Pointer<T> {
        val p = Pointer(this.data)
        p.pos = pos + increment
        return p
    }

    private fun rangeCheck(newValue : Int) {
            if(newValue !in data.indices) throw ArrayIndexOutOfBoundsException("$newValue is out of range, array size ${data.size}")
    }

}