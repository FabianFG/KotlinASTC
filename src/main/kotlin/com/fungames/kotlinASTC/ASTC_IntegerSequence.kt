package com.fungames.kotlinASTC


// unpacked quint triplets <low,middle,high> for each packed-quint value
val quintsOfInteger = arrayOf(
    arrayOf(0, 0, 0),	arrayOf(1, 0, 0),	arrayOf(2, 0, 0),	arrayOf(3, 0, 0),
    arrayOf(4, 0, 0),	arrayOf(0, 4, 0),	arrayOf(4, 4, 0),	arrayOf(4, 4, 4),
    arrayOf(0, 1, 0),	arrayOf(1, 1, 0),	arrayOf(2, 1, 0),	arrayOf(3, 1, 0),
    arrayOf(4, 1, 0),	arrayOf(1, 4, 0),	arrayOf(4, 4, 1),	arrayOf(4, 4, 4),
    arrayOf(0, 2, 0),	arrayOf(1, 2, 0),	arrayOf(2, 2, 0),	arrayOf(3, 2, 0),
    arrayOf(4, 2, 0),	arrayOf(2, 4, 0),	arrayOf(4, 4, 2),	arrayOf(4, 4, 4),
    arrayOf(0, 3, 0),	arrayOf(1, 3, 0),	arrayOf(2, 3, 0),	arrayOf(3, 3, 0),
    arrayOf(4, 3, 0),	arrayOf(3, 4, 0),	arrayOf(4, 4, 3),	arrayOf(4, 4, 4),
    arrayOf(0, 0, 1),	arrayOf(1, 0, 1),	arrayOf(2, 0, 1),	arrayOf(3, 0, 1),
    arrayOf(4, 0, 1),	arrayOf(0, 4, 1),	arrayOf(4, 0, 4),	arrayOf(0, 4, 4),
    arrayOf(0, 1, 1),	arrayOf(1, 1, 1),	arrayOf(2, 1, 1),	arrayOf(3, 1, 1),
    arrayOf(4, 1, 1),	arrayOf(1, 4, 1),	arrayOf(4, 1, 4),	arrayOf(1, 4, 4),
    arrayOf(0, 2, 1),	arrayOf(1, 2, 1),	arrayOf(2, 2, 1),	arrayOf(3, 2, 1),
    arrayOf(4, 2, 1),	arrayOf(2, 4, 1),	arrayOf(4, 2, 4),	arrayOf(2, 4, 4),
    arrayOf(0, 3, 1),	arrayOf(1, 3, 1),	arrayOf(2, 3, 1),	arrayOf(3, 3, 1),
    arrayOf(4, 3, 1),	arrayOf(3, 4, 1),	arrayOf(4, 3, 4),	arrayOf(3, 4, 4),
    arrayOf(0, 0, 2),	arrayOf(1, 0, 2),	arrayOf(2, 0, 2),	arrayOf(3, 0, 2),
    arrayOf(4, 0, 2),	arrayOf(0, 4, 2),	arrayOf(2, 0, 4),	arrayOf(3, 0, 4),
    arrayOf(0, 1, 2),	arrayOf(1, 1, 2),	arrayOf(2, 1, 2),	arrayOf(3, 1, 2),
    arrayOf(4, 1, 2),	arrayOf(1, 4, 2),	arrayOf(2, 1, 4),	arrayOf(3, 1, 4),
    arrayOf(0, 2, 2),	arrayOf(1, 2, 2),	arrayOf(2, 2, 2),	arrayOf(3, 2, 2),
    arrayOf(4, 2, 2),	arrayOf(2, 4, 2),	arrayOf(2, 2, 4),	arrayOf(3, 2, 4),
    arrayOf(0, 3, 2),	arrayOf(1, 3, 2),	arrayOf(2, 3, 2),	arrayOf(3, 3, 2),
    arrayOf(4, 3, 2),	arrayOf(3, 4, 2),	arrayOf(2, 3, 4),	arrayOf(3, 3, 4),
    arrayOf(0, 0, 3),	arrayOf(1, 0, 3),	arrayOf(2, 0, 3),	arrayOf(3, 0, 3),
    arrayOf(4, 0, 3),	arrayOf(0, 4, 3),	arrayOf(0, 0, 4),	arrayOf(1, 0, 4),
    arrayOf(0, 1, 3),	arrayOf(1, 1, 3),	arrayOf(2, 1, 3),	arrayOf(3, 1, 3),
    arrayOf(4, 1, 3),	arrayOf(1, 4, 3),	arrayOf(0, 1, 4),	arrayOf(1, 1, 4),
    arrayOf(0, 2, 3),	arrayOf(1, 2, 3),	arrayOf(2, 2, 3),	arrayOf(3, 2, 3),
    arrayOf(4, 2, 3),	arrayOf(2, 4, 3),	arrayOf(0, 2, 4),	arrayOf(1, 2, 4),
    arrayOf(0, 3, 3),	arrayOf(1, 3, 3),	arrayOf(2, 3, 3),	arrayOf(3, 3, 3),
    arrayOf(4, 3, 3),	arrayOf(3, 4, 3),	arrayOf(0, 3, 4),	arrayOf(1, 3, 4)
)

// unpacked trit quintuplets <low,_,_,_,high> for each packed-quint value
val tritsOfInteger = arrayOf(
    arrayOf(0, 0, 0, 0, 0),	arrayOf(1, 0, 0, 0, 0),	arrayOf(2, 0, 0, 0, 0),	arrayOf(0, 0, 2, 0, 0),
    arrayOf(0, 1, 0, 0, 0),	arrayOf(1, 1, 0, 0, 0),	arrayOf(2, 1, 0, 0, 0),	arrayOf(1, 0, 2, 0, 0),
    arrayOf(0, 2, 0, 0, 0),	arrayOf(1, 2, 0, 0, 0),	arrayOf(2, 2, 0, 0, 0),	arrayOf(2, 0, 2, 0, 0),
    arrayOf(0, 2, 2, 0, 0),	arrayOf(1, 2, 2, 0, 0),	arrayOf(2, 2, 2, 0, 0),	arrayOf(2, 0, 2, 0, 0),
    arrayOf(0, 0, 1, 0, 0),	arrayOf(1, 0, 1, 0, 0),	arrayOf(2, 0, 1, 0, 0),	arrayOf(0, 1, 2, 0, 0),
    arrayOf(0, 1, 1, 0, 0),	arrayOf(1, 1, 1, 0, 0),	arrayOf(2, 1, 1, 0, 0),	arrayOf(1, 1, 2, 0, 0),
    arrayOf(0, 2, 1, 0, 0),	arrayOf(1, 2, 1, 0, 0),	arrayOf(2, 2, 1, 0, 0),	arrayOf(2, 1, 2, 0, 0),
    arrayOf(0, 0, 0, 2, 2),	arrayOf(1, 0, 0, 2, 2),	arrayOf(2, 0, 0, 2, 2),	arrayOf(0, 0, 2, 2, 2),
    arrayOf(0, 0, 0, 1, 0),	arrayOf(1, 0, 0, 1, 0),	arrayOf(2, 0, 0, 1, 0),	arrayOf(0, 0, 2, 1, 0),
    arrayOf(0, 1, 0, 1, 0),	arrayOf(1, 1, 0, 1, 0),	arrayOf(2, 1, 0, 1, 0),	arrayOf(1, 0, 2, 1, 0),
    arrayOf(0, 2, 0, 1, 0),	arrayOf(1, 2, 0, 1, 0),	arrayOf(2, 2, 0, 1, 0),	arrayOf(2, 0, 2, 1, 0),
    arrayOf(0, 2, 2, 1, 0),	arrayOf(1, 2, 2, 1, 0),	arrayOf(2, 2, 2, 1, 0),	arrayOf(2, 0, 2, 1, 0),
    arrayOf(0, 0, 1, 1, 0),	arrayOf(1, 0, 1, 1, 0),	arrayOf(2, 0, 1, 1, 0),	arrayOf(0, 1, 2, 1, 0),
    arrayOf(0, 1, 1, 1, 0),	arrayOf(1, 1, 1, 1, 0),	arrayOf(2, 1, 1, 1, 0),	arrayOf(1, 1, 2, 1, 0),
    arrayOf(0, 2, 1, 1, 0),	arrayOf(1, 2, 1, 1, 0),	arrayOf(2, 2, 1, 1, 0),	arrayOf(2, 1, 2, 1, 0),
    arrayOf(0, 1, 0, 2, 2),	arrayOf(1, 1, 0, 2, 2),	arrayOf(2, 1, 0, 2, 2),	arrayOf(1, 0, 2, 2, 2),
    arrayOf(0, 0, 0, 2, 0),	arrayOf(1, 0, 0, 2, 0),	arrayOf(2, 0, 0, 2, 0),	arrayOf(0, 0, 2, 2, 0),
    arrayOf(0, 1, 0, 2, 0),	arrayOf(1, 1, 0, 2, 0),	arrayOf(2, 1, 0, 2, 0),	arrayOf(1, 0, 2, 2, 0),
    arrayOf(0, 2, 0, 2, 0),	arrayOf(1, 2, 0, 2, 0),	arrayOf(2, 2, 0, 2, 0),	arrayOf(2, 0, 2, 2, 0),
    arrayOf(0, 2, 2, 2, 0),	arrayOf(1, 2, 2, 2, 0),	arrayOf(2, 2, 2, 2, 0),	arrayOf(2, 0, 2, 2, 0),
    arrayOf(0, 0, 1, 2, 0),	arrayOf(1, 0, 1, 2, 0),	arrayOf(2, 0, 1, 2, 0),	arrayOf(0, 1, 2, 2, 0),
    arrayOf(0, 1, 1, 2, 0),	arrayOf(1, 1, 1, 2, 0),	arrayOf(2, 1, 1, 2, 0),	arrayOf(1, 1, 2, 2, 0),
    arrayOf(0, 2, 1, 2, 0),	arrayOf(1, 2, 1, 2, 0),	arrayOf(2, 2, 1, 2, 0),	arrayOf(2, 1, 2, 2, 0),
    arrayOf(0, 2, 0, 2, 2),	arrayOf(1, 2, 0, 2, 2),	arrayOf(2, 2, 0, 2, 2),	arrayOf(2, 0, 2, 2, 2),
    arrayOf(0, 0, 0, 0, 2),	arrayOf(1, 0, 0, 0, 2),	arrayOf(2, 0, 0, 0, 2),	arrayOf(0, 0, 2, 0, 2),
    arrayOf(0, 1, 0, 0, 2),	arrayOf(1, 1, 0, 0, 2),	arrayOf(2, 1, 0, 0, 2),	arrayOf(1, 0, 2, 0, 2),
    arrayOf(0, 2, 0, 0, 2),	arrayOf(1, 2, 0, 0, 2),	arrayOf(2, 2, 0, 0, 2),	arrayOf(2, 0, 2, 0, 2),
    arrayOf(0, 2, 2, 0, 2),	arrayOf(1, 2, 2, 0, 2),	arrayOf(2, 2, 2, 0, 2),	arrayOf(2, 0, 2, 0, 2),
    arrayOf(0, 0, 1, 0, 2),	arrayOf(1, 0, 1, 0, 2),	arrayOf(2, 0, 1, 0, 2),	arrayOf(0, 1, 2, 0, 2),
    arrayOf(0, 1, 1, 0, 2),	arrayOf(1, 1, 1, 0, 2),	arrayOf(2, 1, 1, 0, 2),	arrayOf(1, 1, 2, 0, 2),
    arrayOf(0, 2, 1, 0, 2),	arrayOf(1, 2, 1, 0, 2),	arrayOf(2, 2, 1, 0, 2),	arrayOf(2, 1, 2, 0, 2),
    arrayOf(0, 2, 2, 2, 2),	arrayOf(1, 2, 2, 2, 2),	arrayOf(2, 2, 2, 2, 2),	arrayOf(2, 0, 2, 2, 2),
    arrayOf(0, 0, 0, 0, 1),	arrayOf(1, 0, 0, 0, 1),	arrayOf(2, 0, 0, 0, 1),	arrayOf(0, 0, 2, 0, 1),
    arrayOf(0, 1, 0, 0, 1),	arrayOf(1, 1, 0, 0, 1),	arrayOf(2, 1, 0, 0, 1),	arrayOf(1, 0, 2, 0, 1),
    arrayOf(0, 2, 0, 0, 1),	arrayOf(1, 2, 0, 0, 1),	arrayOf(2, 2, 0, 0, 1),	arrayOf(2, 0, 2, 0, 1),
    arrayOf(0, 2, 2, 0, 1),	arrayOf(1, 2, 2, 0, 1),	arrayOf(2, 2, 2, 0, 1),	arrayOf(2, 0, 2, 0, 1),
    arrayOf(0, 0, 1, 0, 1),	arrayOf(1, 0, 1, 0, 1),	arrayOf(2, 0, 1, 0, 1),	arrayOf(0, 1, 2, 0, 1),
    arrayOf(0, 1, 1, 0, 1),	arrayOf(1, 1, 1, 0, 1),	arrayOf(2, 1, 1, 0, 1),	arrayOf(1, 1, 2, 0, 1),
    arrayOf(0, 2, 1, 0, 1),	arrayOf(1, 2, 1, 0, 1),	arrayOf(2, 2, 1, 0, 1),	arrayOf(2, 1, 2, 0, 1),
    arrayOf(0, 0, 1, 2, 2),	arrayOf(1, 0, 1, 2, 2),	arrayOf(2, 0, 1, 2, 2),	arrayOf(0, 1, 2, 2, 2),
    arrayOf(0, 0, 0, 1, 1),	arrayOf(1, 0, 0, 1, 1),	arrayOf(2, 0, 0, 1, 1),	arrayOf(0, 0, 2, 1, 1),
    arrayOf(0, 1, 0, 1, 1),	arrayOf(1, 1, 0, 1, 1),	arrayOf(2, 1, 0, 1, 1),	arrayOf(1, 0, 2, 1, 1),
    arrayOf(0, 2, 0, 1, 1),	arrayOf(1, 2, 0, 1, 1),	arrayOf(2, 2, 0, 1, 1),	arrayOf(2, 0, 2, 1, 1),
    arrayOf(0, 2, 2, 1, 1),	arrayOf(1, 2, 2, 1, 1),	arrayOf(2, 2, 2, 1, 1),	arrayOf(2, 0, 2, 1, 1),
    arrayOf(0, 0, 1, 1, 1),	arrayOf(1, 0, 1, 1, 1),	arrayOf(2, 0, 1, 1, 1),	arrayOf(0, 1, 2, 1, 1),
    arrayOf(0, 1, 1, 1, 1),	arrayOf(1, 1, 1, 1, 1),	arrayOf(2, 1, 1, 1, 1),	arrayOf(1, 1, 2, 1, 1),
    arrayOf(0, 2, 1, 1, 1),	arrayOf(1, 2, 1, 1, 1),	arrayOf(2, 2, 1, 1, 1),	arrayOf(2, 1, 2, 1, 1),
    arrayOf(0, 1, 1, 2, 2),	arrayOf(1, 1, 1, 2, 2),	arrayOf(2, 1, 1, 2, 2),	arrayOf(1, 1, 2, 2, 2),
    arrayOf(0, 0, 0, 2, 1),	arrayOf(1, 0, 0, 2, 1),	arrayOf(2, 0, 0, 2, 1),	arrayOf(0, 0, 2, 2, 1),
    arrayOf(0, 1, 0, 2, 1),	arrayOf(1, 1, 0, 2, 1),	arrayOf(2, 1, 0, 2, 1),	arrayOf(1, 0, 2, 2, 1),
    arrayOf(0, 2, 0, 2, 1),	arrayOf(1, 2, 0, 2, 1),	arrayOf(2, 2, 0, 2, 1),	arrayOf(2, 0, 2, 2, 1),
    arrayOf(0, 2, 2, 2, 1),	arrayOf(1, 2, 2, 2, 1),	arrayOf(2, 2, 2, 2, 1),	arrayOf(2, 0, 2, 2, 1),
    arrayOf(0, 0, 1, 2, 1),	arrayOf(1, 0, 1, 2, 1),	arrayOf(2, 0, 1, 2, 1),	arrayOf(0, 1, 2, 2, 1),
    arrayOf(0, 1, 1, 2, 1),	arrayOf(1, 1, 1, 2, 1),	arrayOf(2, 1, 1, 2, 1),	arrayOf(1, 1, 2, 2, 1),
    arrayOf(0, 2, 1, 2, 1),	arrayOf(1, 2, 1, 2, 1),	arrayOf(2, 2, 1, 2, 1),	arrayOf(2, 1, 2, 2, 1),
    arrayOf(0, 2, 1, 2, 2),	arrayOf(1, 2, 1, 2, 2),	arrayOf(2, 2, 1, 2, 2),	arrayOf(2, 1, 2, 2, 2),
    arrayOf(0, 0, 0, 1, 2),	arrayOf(1, 0, 0, 1, 2),	arrayOf(2, 0, 0, 1, 2),	arrayOf(0, 0, 2, 1, 2),
    arrayOf(0, 1, 0, 1, 2),	arrayOf(1, 1, 0, 1, 2),	arrayOf(2, 1, 0, 1, 2),	arrayOf(1, 0, 2, 1, 2),
    arrayOf(0, 2, 0, 1, 2),	arrayOf(1, 2, 0, 1, 2),	arrayOf(2, 2, 0, 1, 2),	arrayOf(2, 0, 2, 1, 2),
    arrayOf(0, 2, 2, 1, 2),	arrayOf(1, 2, 2, 1, 2),	arrayOf(2, 2, 2, 1, 2),	arrayOf(2, 0, 2, 1, 2),
    arrayOf(0, 0, 1, 1, 2),	arrayOf(1, 0, 1, 1, 2),	arrayOf(2, 0, 1, 1, 2),	arrayOf(0, 1, 2, 1, 2),
    arrayOf(0, 1, 1, 1, 2),	arrayOf(1, 1, 1, 1, 2),	arrayOf(2, 1, 1, 1, 2),	arrayOf(1, 1, 2, 1, 2),
    arrayOf(0, 2, 1, 1, 2),	arrayOf(1, 2, 1, 1, 2),	arrayOf(2, 2, 1, 1, 2),	arrayOf(2, 1, 2, 1, 2),
    arrayOf(0, 2, 2, 2, 2),	arrayOf(1, 2, 2, 2, 2),	arrayOf(2, 2, 2, 2, 2),	arrayOf(2, 1, 2, 2, 2)
)

fun computeIseBitcount(items : Int, quant : QuantizationMethod) : Int {
    when(quant) {
        QuantizationMethod.QUANT_2 -> return items
        QuantizationMethod.QUANT_3 -> return (8 * items + 4) / 5
        QuantizationMethod.QUANT_4 -> return 2 * items
        QuantizationMethod.QUANT_5 -> return (7 * items + 2) / 3
        QuantizationMethod.QUANT_6 -> return (13 * items + 4) / 5
        QuantizationMethod.QUANT_8 -> return 3 * items
        QuantizationMethod.QUANT_10 -> return (10 * items + 2) / 3
        QuantizationMethod.QUANT_12 -> return (18 * items + 4) / 5
        QuantizationMethod.QUANT_16 -> return items * 4
        QuantizationMethod.QUANT_20 -> return (13 * items + 2) / 3
        QuantizationMethod.QUANT_24 -> return (23 * items + 4) / 5
        QuantizationMethod.QUANT_32 -> return 5 * items
        QuantizationMethod.QUANT_40 -> return (16 * items + 2) / 3
        QuantizationMethod.QUANT_48 -> return (28 * items + 4) / 5
        QuantizationMethod.QUANT_64 -> return 6 * items
        QuantizationMethod.QUANT_80 -> return (19 * items + 2) / 3
        QuantizationMethod.QUANT_96 -> return (33 * items + 4) / 5
        QuantizationMethod.QUANT_128 -> return 7 * items
        QuantizationMethod.QUANT_160 -> return (22 * items + 2) / 3
        QuantizationMethod.QUANT_192 -> return (38 * items + 4) / 5
        QuantizationMethod.QUANT_256 -> return 8 * items
        else -> return 100000
    }
}

fun decodeIse(quantizationLevel : QuantizationMethod, elements : Int, inputData : Array<UByte>, outputData : Array<UByte>, bitOffset : Int) {
    var bitOffset = bitOffset
    // note: due to how the trit/quint-block unpacking is done in this function,
    // we may write more temporary results than the number of outputs
    // The maximum actual number of results is 64 bit, but we keep 4 additional elements
    // of padding.

    val results = Array(68) {0.toUByte()}
    val tqBlocks = Array(22) {0.toUByte()}		// trit-blocks or quint-blocks

    val (bits, trits, quints) = findNumberOfBitsTritsQuints(quantizationLevel)

    var lCounter = 0
    var hCounter = 0

    //Useless
    // trit-blocks or quint-blocks must be zeroed out before we collect them in the loop below.
//    for (i = 0; i < 22; i++)
//    tq_blocks[i] = 0;

    // collect bits for each element, as well as bits for any trit-blocks and quint-blocks.
    for(i in 0 until elements) {
        results[i] = readBits(bits, bitOffset, inputData).toUByte()
        bitOffset += bits
        if(trits != 0) {
            val bitsToRead = arrayOf(2, 2, 1, 2, 1)
            val blockShift = arrayOf(0, 2, 4, 5, 7)
            val next1Counter = arrayOf(1, 2, 3, 4, 0)
            val hcounterIncr = arrayOf(0, 0, 0, 0, 1)
            val tdata = readBits(bitsToRead[lCounter], bitOffset, inputData)
            bitOffset += bitsToRead[lCounter]
            tqBlocks[hCounter] = (tqBlocks[hCounter] or (tdata shl blockShift[lCounter]).toUByte())
            hCounter += hcounterIncr[lCounter]
            lCounter = next1Counter[lCounter]
        }
        if(quints != 0) {
            val bitsToRead = arrayOf(3, 2, 2)
            val blockShift = arrayOf(0, 3, 5)
            val next1Counter = arrayOf(1, 2, 0)
            val hcounterIncr = arrayOf(0, 0, 1)
            val tdata = readBits(bitsToRead[lCounter], bitOffset, inputData)
            bitOffset += bitsToRead[lCounter]
            tqBlocks[hCounter] = (tqBlocks[hCounter] or ((tdata shl blockShift[lCounter]).toUByte())).toUByte()
            hCounter += hcounterIncr[lCounter]
            lCounter = next1Counter[lCounter]
        }

    }

    // unpack trit-blocks or quint-blocks as needed
    if(trits != 0) {
        val tritBlocks = (elements + 4) / 5
        for(i in 0 until tritBlocks) {
            val tritPtr = tritsOfInteger[tqBlocks[i].toInt()]
            results[5 * i] = (results[5 * i] or ((tritPtr[0] shl bits).toUByte()))
            results[5 * i + 1] = (results[5 * i + 1] or ((tritPtr[1] shl bits).toUByte()))
            results[5 * i + 2] = (results[5 * i + 2] or ((tritPtr[2] shl bits).toUByte()))
            results[5 * i + 3] = (results[5 * i + 3] or ((tritPtr[3] shl bits).toUByte()))
            results[5 * i + 4] = (results[5 * i + 4] or ((tritPtr[4] shl bits).toUByte()))
        }
    }

    if(quints != 0) {
        val quintsBlocks = (elements + 2) / 3
        for(i in 0 until quintsBlocks) {
            val quintPtr = quintsOfInteger[tqBlocks[i].toInt()]
//            results[3 * i] = (results[3 * i].toInt() or (quintPtr[0] shl bits)).toUByte()
//            results[3 * i + 1] = (results[3 * i + 1].toInt() or (quintPtr[1] shl bits)).toUByte()
//            results[3 * i + 2] = (results[3 * i + 2].toInt() or (quintPtr[2] shl bits)).toUByte()
            results[3 * i] = results[3 * i] or (quintPtr[0] shl bits).toUByte()
            results[3 * i + 1] = results[3 * i + 1] or (quintPtr[1] shl bits).toUByte()
            results[3 * i + 2] = results[3 * i + 2] or (quintPtr[2] shl bits).toUByte()
        }
    }

    for (i in 0 until elements)
        outputData[i] = results[i]

}

fun findNumberOfBitsTritsQuints(quantizationLevel: QuantizationMethod) : Triple<Int, Int, Int> {
    var bits = 0
    var trits = 0
    var quints = 0
    when(quantizationLevel) {
        QuantizationMethod.QUANT_2 -> bits = 1
        QuantizationMethod.QUANT_3 -> {bits = 0; trits = 1}
        QuantizationMethod.QUANT_4 -> bits = 2
        QuantizationMethod.QUANT_5 -> {bits = 0; quints = 1}
        QuantizationMethod.QUANT_6 -> {bits = 1; trits = 1}
        QuantizationMethod.QUANT_8 -> bits = 3
        QuantizationMethod.QUANT_10 -> {bits = 1; quints = 1}
        QuantizationMethod.QUANT_12 -> {bits = 2; trits = 1}
        QuantizationMethod.QUANT_16 -> bits = 4
        QuantizationMethod.QUANT_20 -> {bits = 2; quints = 1}
        QuantizationMethod.QUANT_24 -> {bits = 3; trits = 1}
        QuantizationMethod.QUANT_32 -> bits = 5
        QuantizationMethod.QUANT_40 -> {bits = 3; quints = 1}
        QuantizationMethod.QUANT_48 -> {bits = 4; trits = 1}
        QuantizationMethod.QUANT_64 -> bits = 6
        QuantizationMethod.QUANT_80 -> {bits = 4; quints = 1}
        QuantizationMethod.QUANT_96 -> {bits = 5; trits = 1}
        QuantizationMethod.QUANT_128 -> bits = 7
        QuantizationMethod.QUANT_160 -> {bits = 5; quints = 1}
        QuantizationMethod.QUANT_192 -> {bits = 6; trits = 1}
        QuantizationMethod.QUANT_256 -> bits = 8
    }
    return Triple(bits, trits, quints)
}