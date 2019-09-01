package com.fungames.kotlinASTC

val partitionTables : Array<Array<Array<PartitionInfo>?>?> = arrayOfNulls(4096)


fun genCanonicalizedPartitionTable(texelCount: Int, partitionTable: Array<Int>, canonicalized: Pointer<ULong>) {

    val mappedIndex = Array(4) {-1}
    var mapWeightCount = 0

    for (i in 0 until texelCount) {
        val index = partitionTable[i]
        if (mappedIndex[index] == -1)
            mappedIndex[index] = mapWeightCount++
        val xlatIndex = mappedIndex[index].toULong()
        canonicalized[i shr 5] = canonicalized[i shr 5] or (xlatIndex shl (2 * (i and 0x1F)))
    }
}

fun compareCanonicalizedPartitionTables(part1 : Pointer<ULong>, part2 : Pointer<ULong>) : Boolean {
    if (part1[0] != part2[0])
        return false
    if (part1[1] != part2[1])
        return false
    if (part1[2] != part2[2])
        return false
    if (part1[3] != part2[3])
        return false
    if (part1[4] != part2[4])
        return false
    if (part1[5] != part2[5])
        return false
    if (part1[6] != part2[6])
        return false
    return true
}

/*
   For a partition table, detect partitionings that are equivalent, then mark them as invalid. This reduces the number of partitions that the codec has to consider and thus improves encode
   performance. */
fun partitionTableZapEqualElement(xDim: Int, yDim: Int, zDim: Int, pi: Array<PartitionInfo>) {
    var partitionTablesZapped = 0

    val texelCount = xDim * yDim * zDim

    val canonicalizeds = Array(PARTITION_COUNT * 7) {0.toULong()}

    for (i in 0 until PARTITION_COUNT)
        genCanonicalizedPartitionTable(texelCount, pi[i].partitionOfTexel, canonicalizeds.toPointer() + i * 7)

    for (i in 0 until PARTITION_COUNT)
        for (j in 0 until i)
            if (compareCanonicalizedPartitionTables(canonicalizeds.toPointer() + 7* i, canonicalizeds.toPointer() + 7* j)) {
                pi[i].partitionCount = 0
                partitionTablesZapped++
            }
}

fun hash52(inp: Int): Int {
    var inp = inp
    inp = inp xor inp.ushr(15)

    inp *= -0x1121f76f // (2^4+1)*(2^7+1)*(2^17-1)
    inp = inp xor inp.ushr(5)
    inp += inp shl 16
    inp = inp xor inp.ushr(7)
    inp = inp xor inp.ushr(3)
    inp = inp xor (inp shl 6)
    inp = inp xor inp.ushr(17)
    return inp
}


fun selectPartition(seed: Int, x: Int, y: Int, z: Int, partitionCount: Int, smallBlock: Boolean): Int {
    var seed = seed
    var x = x
    var y = x
    var z = x
    if (smallBlock) {
        x = x shl 1
        y = y shl 1
        z = z shl 1
    }

    seed += (partitionCount - 1) * 1024

    val rnum = hash52(seed)

    var seed1 = (rnum and 0xF)
    var seed2 = (rnum.ushr(4) and 0xF)
    var seed3 = (rnum.ushr(8) and 0xF)
    var seed4 = (rnum.ushr(12) and 0xF)
    var seed5 = (rnum.ushr(16) and 0xF)
    var seed6 = (rnum.ushr(20) and 0xF)
    var seed7 = (rnum.ushr(24) and 0xF)
    var seed8 = (rnum.ushr(28) and 0xF)
    var seed9 = (rnum.ushr(18) and 0xF)
    var seed10 = (rnum.ushr(22) and 0xF)
    var seed11 = (rnum.ushr(26) and 0xF)
    var seed12 = (rnum.ushr(30) or (rnum shl 2) and 0xF)

    // squaring all the seeds in order to bias their distribution
    // towards lower values.
    seed1 *= seed1
    seed2 *= seed2
    seed3 *= seed3
    seed4 *= seed4
    seed5 *= seed5
    seed6 *= seed6
    seed7 *= seed7
    seed8 *= seed8
    seed9 *= seed9
    seed10 *= seed10
    seed11 *= seed11
    seed12 *= seed12


    val sh1: Int
    val sh2: Int
    val sh3: Int
    if ((seed and 1) != 0) {
        sh1 = if ((seed and 2) != 0) 4 else 5
        sh2 = if (partitionCount == 3) 6 else 5
    } else {
        sh1 = if (partitionCount == 3) 6 else 5
        sh2 = if ((seed and 2) != 0) 4 else 5
    }
    sh3 = if ((seed and 0x10) != 0) sh1 else sh2

    seed1 = seed1 shr sh1
    seed2 = seed2 shr sh2
    seed3 = seed3 shr sh1
    seed4 = seed4 shr sh2
    seed5 = seed5 shr sh1
    seed6 = seed6 shr sh2
    seed7 = seed7 shr sh1
    seed8 = seed8 shr sh2

    seed9 = seed9 shr sh3
    seed10 = seed10 shr sh3
    seed11 = seed11 shr sh3
    seed12 = seed12 shr sh3


    var a = seed1 * x + seed2 * y + seed11 * z + (rnum shr 14)
    var b = seed3 * x + seed4 * y + seed12 * z + (rnum shr 10)
    var c = seed5 * x + seed6 * y + seed9 * z + (rnum shr 6)
    var d = seed7 * x + seed8 * y + seed10 * z + (rnum shr 2)


    // apply the saw
    a = a and 0x3F
    b = b and 0x3F
    c = c and 0x3F
    d = d and 0x3F

    // remove some of the components if we are to output < 4 partitions.
    if (partitionCount <= 3)
        d = 0
    if (partitionCount <= 2)
        c = 0
    if (partitionCount <= 1)
        b = 0

    val partition: Int
    partition = if (a >= b && a >= c && a >= d)
        0
    else if (b >= c && b >= d)
        1
    else if (c >= d)
        2
    else
        3
    return partition
}

fun generateOnePartitionTable(xDim: Int, yDim: Int, zDim: Int, partitionCount: Int, partitionIndex: Int, pt: PartitionInfo) {
    val smallBlock = (xDim * yDim * zDim) < 32

    val partitionOfTexel = pt.partitionOfTexel

    var tempIdx = 0
    for(z in 0 until zDim)
        for(y in 0 until yDim)
            for(x in 0 until xDim) {
                val part = selectPartition(partitionIndex, x, y, z, partitionCount, smallBlock)
                partitionOfTexel[tempIdx] = part
                tempIdx++
            }

    val texelsPerBlock = xDim * yDim * zDim

    val counts = Array(4) {0}

    for (i in 0 until texelsPerBlock) {
        val partition = pt.partitionOfTexel[i]
        pt.texelsOfPartition[partition][counts[partition]++] = i
    }

    for(i in 0 until 4)
        pt.texelsPerPartition[i] = counts[i]

    when {
        counts[0] == 0 -> pt.partitionCount = 0
        counts[1] == 0 -> pt.partitionCount = 1
        counts[2] == 0 -> pt.partitionCount = 2
        counts[2] == 0 -> pt.partitionCount = 3
        else -> pt.partitionCount = 4
    }

    val bsd = getBlockSizeDescriptor(xDim, yDim, zDim);
    val texelsToProcess = bsd.texelcountForBitmapPartitioning
    for (i in 0 until texelsToProcess) {
        val idx = bsd.texelsForBitmapPartitioning[i]
        pt.coverageBitmaps[pt.partitionOfTexel[idx]] = pt.coverageBitmaps[pt.partitionOfTexel[idx]] or (1 shl i).toULong()
    }

}

fun generatePartitionTables(xDim : Int, yDim : Int, zDim : Int) {
    val onePartition = arrayOf(PartitionInfo())
    val twoPartitions = Array(1024) {PartitionInfo()}
    val threePartitions = Array(1024) {PartitionInfo()}
    val fourPartitions = Array(1024) {PartitionInfo()}

    val partitionTable : Array<Array<PartitionInfo>?> = arrayOf(null, onePartition, twoPartitions, threePartitions, fourPartitions)

    generateOnePartitionTable(xDim, yDim, zDim, 1, 0, onePartition[0])
    for (i in 0 until 1024) {
        generateOnePartitionTable(xDim, yDim, zDim, 2, i, twoPartitions[i])
        generateOnePartitionTable(xDim, yDim, zDim, 3, i, threePartitions[i])
        generateOnePartitionTable(xDim, yDim, zDim, 4, i, fourPartitions[i])
    }

    partitionTableZapEqualElement(xDim, yDim, zDim, twoPartitions)
    partitionTableZapEqualElement(xDim, yDim, zDim, threePartitions)
    partitionTableZapEqualElement(xDim, yDim, zDim, fourPartitions)

    partitionTables[xDim + 16 * yDim + 256 * zDim] = partitionTable
}


fun getPartitionTable(xDim : Int, yDim : Int, zDim : Int, partitionCount : Int) : Array<PartitionInfo> {
    val ptindex = xDim + 16 * yDim + 256 * zDim
    if(partitionTables[ptindex] == null)
        generatePartitionTables(xDim, yDim, zDim)
    val resTable = partitionTables[ptindex]?: throw IllegalStateException("Partition table must be not null at this point")
    return resTable[partitionCount]?: throw IllegalStateException("Partition table must be not null at this point")

}