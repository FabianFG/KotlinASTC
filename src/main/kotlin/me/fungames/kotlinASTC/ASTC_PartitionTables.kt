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
 *	@brief	Functions to generate partition tables for ASTC.
 *
 *			We generate tables only for the block sizes that have actually been
 *			specified to the codec.
 *	@author rewritten to Kotlin by FunGames
 */
/*----------------------------------------------------------------------------*/
@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")
package me.fungames.kotlinASTC

import me.fungames.kotlinPointers.ULongPointer
import me.fungames.kotlinPointers.asPointer

val partitionTables : Array<Array<Array<PartitionInfo>?>?> = arrayOfNulls(4096)


fun genCanonicalizedPartitionTable(texelCount: Int, partitionTable: UByteArray, canonicalized: ULongPointer) {

    val mappedIndex = Array(4) {-1}
    var mapWeightCount = 0

    for (i in 0 until texelCount) {
        val index = partitionTable[i].toInt()
        if (mappedIndex[index] == -1)
            mappedIndex[index] = mapWeightCount++
        val xlatIndex = mappedIndex[index].toULong()
        canonicalized[i shr 5] = canonicalized[i shr 5] or (xlatIndex shl (2 * (i and 0x1F)))
    }
}

fun compareCanonicalizedPartitionTables(part1 : ULongPointer, part2 : ULongPointer) : Boolean {
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

    val canonicalizeds = ULongArray(PARTITION_COUNT * 7)

    for (i in 0 until PARTITION_COUNT)
        genCanonicalizedPartitionTable(
            texelCount,
            pi[i].partitionOfTexel,
            canonicalizeds.asPointer() + i * 7
        )

    for (i in 0 until PARTITION_COUNT)
        for (j in 0 until i)
            if (compareCanonicalizedPartitionTables(
                    canonicalizeds.asPointer() + 7 * i,
                    canonicalizeds.asPointer() + 7 * j
                )
            ) {
                pi[i].partitionCount = 0
                partitionTablesZapped++
            }
}

fun hash52(inp: Int): Int {
    var inp1 = inp
    inp1 = inp1 xor inp1.ushr(15)

    inp1 *= -0x1121f76f // (2^4+1)*(2^7+1)*(2^17-1)
    inp1 = inp1 xor inp1.ushr(5)
    inp1 += inp1 shl 16
    inp1 = inp1 xor inp1.ushr(7)
    inp1 = inp1 xor inp1.ushr(3)
    inp1 = inp1 xor (inp1 shl 6)
    inp1 = inp1 xor inp1.ushr(17)
    return inp1
}


fun selectPartition(seed: Int, x: Int, y: Int, z: Int, partitionCount: Int, smallBlock: Boolean): Int {
    var pSeed = seed
    var x1 = x
    var y1 = y
    var z1 = z
    if (smallBlock) {
        x1 = x1 shl 1
        y1 = y1 shl 1
        z1 = z1 shl 1
    }

    pSeed += (partitionCount - 1) * 1024

    val rNum = hash52(pSeed)

    var seed1 = (rNum and 0xF)
    var seed2 = (rNum.ushr(4) and 0xF)
    var seed3 = (rNum.ushr(8) and 0xF)
    var seed4 = (rNum.ushr(12) and 0xF)
    var seed5 = (rNum.ushr(16) and 0xF)
    var seed6 = (rNum.ushr(20) and 0xF)
    var seed7 = (rNum.ushr(24) and 0xF)
    var seed8 = (rNum.ushr(28) and 0xF)
    var seed9 = (rNum.ushr(18) and 0xF)
    var seed10 = (rNum.ushr(22) and 0xF)
    var seed11 = (rNum.ushr(26) and 0xF)
    var seed12 = (rNum.ushr(30) or (rNum shl 2) and 0xF)

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
    if ((pSeed and 1) != 0) {
        sh1 = if ((pSeed and 2) != 0) 4 else 5
        sh2 = if (partitionCount == 3) 6 else 5
    } else {
        sh1 = if (partitionCount == 3) 6 else 5
        sh2 = if ((pSeed and 2) != 0) 4 else 5
    }
    sh3 = if ((pSeed and 0x10) != 0) sh1 else sh2

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


    var a = seed1 * x1 + seed2 * y1 + seed11 * z1 + (rNum shr 14)
    var b = seed3 * x1 + seed4 * y1 + seed12 * z1 + (rNum shr 10)
    var c = seed5 * x1 + seed6 * y1 + seed9 * z1 + (rNum shr 6)
    var d = seed7 * x1 + seed8 * y1 + seed10 * z1 + (rNum shr 2)


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
                partitionOfTexel[tempIdx] = part.toUByte()
                tempIdx++
            }

    val texelsPerBlock = xDim * yDim * zDim

    val counts = Array(4) {0}

    for (i in 0 until texelsPerBlock) {
        val partition = pt.partitionOfTexel[i].toInt()
        pt.texelsOfPartition[partition][counts[partition]++] = i.toUByte()
    }

    for(i in 0 until 4)
        pt.texelsPerPartition[i] = counts[i].toUByte()

    when {
        counts[0] == 0 -> pt.partitionCount = 0
        counts[1] == 0 -> pt.partitionCount = 1
        counts[2] == 0 -> pt.partitionCount = 2
        counts[2] == 0 -> pt.partitionCount = 3
        else -> pt.partitionCount = 4
    }

    val bsd = getBlockSizeDescriptor(xDim, yDim, zDim)
    val texelsToProcess = bsd.texelcountForBitmapPartitioning
    for (i in 0 until texelsToProcess) {
        val idx = bsd.texelsForBitmapPartitioning[i]
        pt.coverageBitmaps[pt.partitionOfTexel[idx].toInt()] = pt.coverageBitmaps[pt.partitionOfTexel[idx].toInt()] or (1 shl i).toULong()
    }

}

fun generatePartitionTables(xDim : Int, yDim : Int, zDim : Int) {
    val onePartition = arrayOf(PartitionInfo())
    val twoPartitions = Array(1024) { PartitionInfo() }
    val threePartitions = Array(1024) { PartitionInfo() }
    val fourPartitions = Array(1024) { PartitionInfo() }

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