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
 *	@brief	Functions and data table related to data quantization in ASTC.
 *  @author rewritten to Kotlin by FunGames
 */
/*----------------------------------------------------------------------------*/
@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

package me.fungames.kotlinASTC

val colorQuantizationTables = arrayOf(
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u,
        5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u,
        5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u,
        5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u,
        5u, 5u, 5u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u,
        0u, 0u, 0u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u,
        5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u,
        5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 6u, 6u, 6u, 6u, 6u, 6u, 6u,
        6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u,
        6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 7u, 7u, 7u,
        7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 4u, 4u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u,
        6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u,
        6u, 6u, 6u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u,
        8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u,
        9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u,
        9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 7u, 7u, 7u,
        7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u,
        7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 5u, 5u, 5u, 5u, 5u, 5u, 5u,
        5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u,
        5u, 5u, 5u, 5u, 5u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 4u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u,
        4u, 4u, 4u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u,
        8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u,
        6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 10u, 10u, 10u, 10u, 10u, 10u, 10u,
        10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u,
        11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u,
        11u, 11u, 11u, 11u, 11u, 11u, 11u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u,
        7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u,
        9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 5u, 5u, 5u,
        5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u,
        5u, 5u, 5u, 5u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 1u, 1u, 1u, 1u, 1u, 1u, 1u,
        1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 2u, 2u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 4u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 5u, 5u, 5u,
        5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 6u, 6u,
        6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 7u,
        7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u,
        8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u,
        8u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u,
        9u, 9u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u,
        10u, 10u, 10u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u,
        11u, 11u, 11u, 11u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u,
        12u, 12u, 12u, 12u, 12u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u,
        13u, 13u, 13u, 13u, 13u, 13u, 14u, 14u, 14u, 14u, 14u, 14u, 14u, 14u, 14u, 14u,
        14u, 14u, 14u, 14u, 14u, 14u, 14u, 15u, 15u, 15u, 15u, 15u, 15u, 15u, 15u, 15u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 0u, 0u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u,
        8u, 8u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u,
        16u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 2u, 6u, 6u, 6u, 6u, 6u, 6u,
        6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u,
        10u, 10u, 10u, 10u, 10u, 14u, 14u, 14u, 14u, 14u, 14u, 14u, 14u, 14u, 14u, 14u,
        14u, 14u, 14u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 18u,
        19u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 15u, 15u, 15u,
        15u, 15u, 15u, 15u, 15u, 15u, 15u, 15u, 15u, 15u, 15u, 11u, 11u, 11u, 11u, 11u,
        11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u,
        7u, 7u, 7u, 7u, 7u, 7u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 17u,
        13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 9u, 9u,
        9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 5u, 5u, 5u, 5u, 5u,
        5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 1u, 1u, 1u, 1u, 1u, 1u, 1u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 0u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u, 8u,
        8u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 2u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 2u, 2u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 10u,
        10u, 10u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 4u, 4u, 4u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u,
        12u, 12u, 12u, 20u, 20u, 20u, 20u, 20u, 20u, 20u, 20u, 20u, 20u, 20u, 6u, 6u,
        6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 6u, 14u, 14u, 14u, 14u, 14u, 14u, 14u,
        14u, 14u, 14u, 14u, 22u, 22u, 22u, 22u, 22u, 22u, 22u, 22u, 22u, 22u, 22u, 22u,
        23u, 23u, 23u, 23u, 23u, 23u, 23u, 23u, 23u, 23u, 23u, 23u, 15u, 15u, 15u, 15u,
        15u, 15u, 15u, 15u, 15u, 15u, 15u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u,
        7u, 7u, 21u, 21u, 21u, 21u, 21u, 21u, 21u, 21u, 21u, 21u, 21u, 13u, 13u, 13u,
        13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u,
        5u, 5u, 5u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 11u, 11u,
        11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 3u, 3u, 3u, 3u, 3u, 3u, 3u,
        3u, 3u, 3u, 3u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 9u,
        9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 1u, 1u, 1u, 1u, 1u, 1u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 0u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 1u, 2u, 2u, 2u,
        2u, 2u, 2u, 2u, 2u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 3u, 4u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 4u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 5u, 6u, 6u,
        6u, 6u, 6u, 6u, 6u, 6u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 7u, 8u, 8u,
        8u, 8u, 8u, 8u, 8u, 8u, 8u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 9u, 10u,
        10u, 10u, 10u, 10u, 10u, 10u, 10u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 12u,
        12u, 12u, 12u, 12u, 12u, 12u, 12u, 12u, 13u, 13u, 13u, 13u, 13u, 13u, 13u, 13u,
        14u, 14u, 14u, 14u, 14u, 14u, 14u, 14u, 15u, 15u, 15u, 15u, 15u, 15u, 15u, 15u,
        16u, 16u, 16u, 16u, 16u, 16u, 16u, 16u, 17u, 17u, 17u, 17u, 17u, 17u, 17u, 17u,
        18u, 18u, 18u, 18u, 18u, 18u, 18u, 18u, 19u, 19u, 19u, 19u, 19u, 19u, 19u, 19u,
        19u, 20u, 20u, 20u, 20u, 20u, 20u, 20u, 20u, 21u, 21u, 21u, 21u, 21u, 21u, 21u,
        21u, 22u, 22u, 22u, 22u, 22u, 22u, 22u, 22u, 23u, 23u, 23u, 23u, 23u, 23u, 23u,
        23u, 23u, 24u, 24u, 24u, 24u, 24u, 24u, 24u, 24u, 25u, 25u, 25u, 25u, 25u, 25u,
        25u, 25u, 26u, 26u, 26u, 26u, 26u, 26u, 26u, 26u, 27u, 27u, 27u, 27u, 27u, 27u,
        27u, 27u, 27u, 28u, 28u, 28u, 28u, 28u, 28u, 28u, 28u, 29u, 29u, 29u, 29u, 29u,
        29u, 29u, 29u, 30u, 30u, 30u, 30u, 30u, 30u, 30u, 30u, 31u, 31u, 31u, 31u, 31u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 0u, 8u, 8u, 8u, 8u, 8u, 8u, 16u, 16u, 16u, 16u, 16u, 16u,
        16u, 24u, 24u, 24u, 24u, 24u, 24u, 32u, 32u, 32u, 32u, 32u, 32u, 32u, 2u, 2u,
        2u, 2u, 2u, 2u, 10u, 10u, 10u, 10u, 10u, 10u, 10u, 18u, 18u, 18u, 18u, 18u,
        18u, 26u, 26u, 26u, 26u, 26u, 26u, 26u, 34u, 34u, 34u, 34u, 34u, 34u, 4u, 4u,
        4u, 4u, 4u, 4u, 4u, 12u, 12u, 12u, 12u, 12u, 12u, 20u, 20u, 20u, 20u, 20u,
        20u, 20u, 28u, 28u, 28u, 28u, 28u, 28u, 36u, 36u, 36u, 36u, 36u, 36u, 36u, 6u,
        6u, 6u, 6u, 6u, 6u, 14u, 14u, 14u, 14u, 14u, 14u, 14u, 22u, 22u, 22u, 22u,
        22u, 22u, 30u, 30u, 30u, 30u, 30u, 30u, 30u, 38u, 38u, 38u, 38u, 38u, 38u, 38u,
        39u, 39u, 39u, 39u, 39u, 39u, 39u, 31u, 31u, 31u, 31u, 31u, 31u, 31u, 23u, 23u,
        23u, 23u, 23u, 23u, 15u, 15u, 15u, 15u, 15u, 15u, 15u, 7u, 7u, 7u, 7u, 7u,
        7u, 37u, 37u, 37u, 37u, 37u, 37u, 37u, 29u, 29u, 29u, 29u, 29u, 29u, 21u, 21u,
        21u, 21u, 21u, 21u, 21u, 13u, 13u, 13u, 13u, 13u, 13u, 5u, 5u, 5u, 5u, 5u,
        5u, 5u, 35u, 35u, 35u, 35u, 35u, 35u, 27u, 27u, 27u, 27u, 27u, 27u, 27u, 19u,
        19u, 19u, 19u, 19u, 19u, 11u, 11u, 11u, 11u, 11u, 11u, 11u, 3u, 3u, 3u, 3u,
        3u, 3u, 33u, 33u, 33u, 33u, 33u, 33u, 33u, 25u, 25u, 25u, 25u, 25u, 25u, 17u,
        17u, 17u, 17u, 17u, 17u, 17u, 9u, 9u, 9u, 9u, 9u, 9u, 1u, 1u, 1u, 1u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 16u, 16u, 16u, 16u, 16u, 16u, 32u, 32u, 32u, 32u, 32u, 2u, 2u,
        2u, 2u, 2u, 18u, 18u, 18u, 18u, 18u, 18u, 34u, 34u, 34u, 34u, 34u, 4u, 4u,
        4u, 4u, 4u, 4u, 20u, 20u, 20u, 20u, 20u, 36u, 36u, 36u, 36u, 36u, 6u, 6u,
        6u, 6u, 6u, 6u, 22u, 22u, 22u, 22u, 22u, 38u, 38u, 38u, 38u, 38u, 38u, 8u,
        8u, 8u, 8u, 8u, 24u, 24u, 24u, 24u, 24u, 24u, 40u, 40u, 40u, 40u, 40u, 10u,
        10u, 10u, 10u, 10u, 26u, 26u, 26u, 26u, 26u, 26u, 42u, 42u, 42u, 42u, 42u, 12u,
        12u, 12u, 12u, 12u, 12u, 28u, 28u, 28u, 28u, 28u, 44u, 44u, 44u, 44u, 44u, 14u,
        14u, 14u, 14u, 14u, 14u, 30u, 30u, 30u, 30u, 30u, 46u, 46u, 46u, 46u, 46u, 46u,
        47u, 47u, 47u, 47u, 47u, 47u, 31u, 31u, 31u, 31u, 31u, 15u, 15u, 15u, 15u, 15u,
        15u, 45u, 45u, 45u, 45u, 45u, 29u, 29u, 29u, 29u, 29u, 13u, 13u, 13u, 13u, 13u,
        13u, 43u, 43u, 43u, 43u, 43u, 27u, 27u, 27u, 27u, 27u, 27u, 11u, 11u, 11u, 11u,
        11u, 41u, 41u, 41u, 41u, 41u, 25u, 25u, 25u, 25u, 25u, 25u, 9u, 9u, 9u, 9u,
        9u, 39u, 39u, 39u, 39u, 39u, 39u, 23u, 23u, 23u, 23u, 23u, 7u, 7u, 7u, 7u,
        7u, 7u, 37u, 37u, 37u, 37u, 37u, 21u, 21u, 21u, 21u, 21u, 5u, 5u, 5u, 5u,
        5u, 5u, 35u, 35u, 35u, 35u, 35u, 19u, 19u, 19u, 19u, 19u, 19u, 3u, 3u, 3u,
        3u, 3u, 33u, 33u, 33u, 33u, 33u, 17u, 17u, 17u, 17u, 17u, 17u, 1u, 1u, 1u
    ),
    ubyteArrayOf(
        0u, 0u, 0u, 1u, 1u, 1u, 1u, 2u, 2u, 2u, 2u, 3u, 3u, 3u, 3u, 4u,
        4u, 4u, 4u, 5u, 5u, 5u, 5u, 6u, 6u, 6u, 6u, 7u, 7u, 7u, 7u, 8u,
        8u, 8u, 8u, 9u, 9u, 9u, 9u, 10u, 10u, 10u, 10u, 11u, 11u, 11u, 11u, 12u,
        12u, 12u, 12u, 13u, 13u, 13u, 13u, 14u, 14u, 14u, 14u, 15u, 15u, 15u, 15u, 16u,
        16u, 16u, 16u, 16u, 17u, 17u, 17u, 17u, 18u, 18u, 18u, 18u, 19u, 19u, 19u, 19u,
        20u, 20u, 20u, 20u, 21u, 21u, 21u, 21u, 22u, 22u, 22u, 22u, 23u, 23u, 23u, 23u,
        24u, 24u, 24u, 24u, 25u, 25u, 25u, 25u, 26u, 26u, 26u, 26u, 27u, 27u, 27u, 27u,
        28u, 28u, 28u, 28u, 29u, 29u, 29u, 29u, 30u, 30u, 30u, 30u, 31u, 31u, 31u, 31u,
        32u, 32u, 32u, 32u, 33u, 33u, 33u, 33u, 34u, 34u, 34u, 34u, 35u, 35u, 35u, 35u,
        36u, 36u, 36u, 36u, 37u, 37u, 37u, 37u, 38u, 38u, 38u, 38u, 39u, 39u, 39u, 39u,
        40u, 40u, 40u, 40u, 41u, 41u, 41u, 41u, 42u, 42u, 42u, 42u, 43u, 43u, 43u, 43u,
        44u, 44u, 44u, 44u, 45u, 45u, 45u, 45u, 46u, 46u, 46u, 46u, 47u, 47u, 47u, 47u,
        47u, 48u, 48u, 48u, 48u, 49u, 49u, 49u, 49u, 50u, 50u, 50u, 50u, 51u, 51u, 51u,
        51u, 52u, 52u, 52u, 52u, 53u, 53u, 53u, 53u, 54u, 54u, 54u, 54u, 55u, 55u, 55u,
        55u, 56u, 56u, 56u, 56u, 57u, 57u, 57u, 57u, 58u, 58u, 58u, 58u, 59u, 59u, 59u,
        59u, 60u, 60u, 60u, 60u, 61u, 61u, 61u, 61u, 62u, 62u, 62u, 62u, 63u, 63u, 63u
    ),
    ubyteArrayOf(
        0u, 0u, 16u, 16u, 16u, 32u, 32u, 32u, 48u, 48u, 48u, 48u, 64u, 64u, 64u, 2u,
        2u, 2u, 18u, 18u, 18u, 34u, 34u, 34u, 50u, 50u, 50u, 50u, 66u, 66u, 66u, 4u,
        4u, 4u, 20u, 20u, 20u, 36u, 36u, 36u, 36u, 52u, 52u, 52u, 68u, 68u, 68u, 6u,
        6u, 6u, 22u, 22u, 22u, 38u, 38u, 38u, 38u, 54u, 54u, 54u, 70u, 70u, 70u, 8u,
        8u, 8u, 24u, 24u, 24u, 24u, 40u, 40u, 40u, 56u, 56u, 56u, 72u, 72u, 72u, 10u,
        10u, 10u, 26u, 26u, 26u, 26u, 42u, 42u, 42u, 58u, 58u, 58u, 74u, 74u, 74u, 12u,
        12u, 12u, 12u, 28u, 28u, 28u, 44u, 44u, 44u, 60u, 60u, 60u, 76u, 76u, 76u, 14u,
        14u, 14u, 14u, 30u, 30u, 30u, 46u, 46u, 46u, 62u, 62u, 62u, 78u, 78u, 78u, 78u,
        79u, 79u, 79u, 79u, 63u, 63u, 63u, 47u, 47u, 47u, 31u, 31u, 31u, 15u, 15u, 15u,
        15u, 77u, 77u, 77u, 61u, 61u, 61u, 45u, 45u, 45u, 29u, 29u, 29u, 13u, 13u, 13u,
        13u, 75u, 75u, 75u, 59u, 59u, 59u, 43u, 43u, 43u, 27u, 27u, 27u, 27u, 11u, 11u,
        11u, 73u, 73u, 73u, 57u, 57u, 57u, 41u, 41u, 41u, 25u, 25u, 25u, 25u, 9u, 9u,
        9u, 71u, 71u, 71u, 55u, 55u, 55u, 39u, 39u, 39u, 39u, 23u, 23u, 23u, 7u, 7u,
        7u, 69u, 69u, 69u, 53u, 53u, 53u, 37u, 37u, 37u, 37u, 21u, 21u, 21u, 5u, 5u,
        5u, 67u, 67u, 67u, 51u, 51u, 51u, 51u, 35u, 35u, 35u, 19u, 19u, 19u, 3u, 3u,
        3u, 65u, 65u, 65u, 49u, 49u, 49u, 49u, 33u, 33u, 33u, 17u, 17u, 17u, 1u, 1u
    ),
    ubyteArrayOf(
        0u, 0u, 32u, 32u, 64u, 64u, 64u, 2u, 2u, 2u, 34u, 34u, 66u, 66u, 66u, 4u,
        4u, 4u, 36u, 36u, 68u, 68u, 68u, 6u, 6u, 6u, 38u, 38u, 70u, 70u, 70u, 8u,
        8u, 8u, 40u, 40u, 40u, 72u, 72u, 10u, 10u, 10u, 42u, 42u, 42u, 74u, 74u, 12u,
        12u, 12u, 44u, 44u, 44u, 76u, 76u, 14u, 14u, 14u, 46u, 46u, 46u, 78u, 78u, 16u,
        16u, 16u, 48u, 48u, 48u, 80u, 80u, 80u, 18u, 18u, 50u, 50u, 50u, 82u, 82u, 82u,
        20u, 20u, 52u, 52u, 52u, 84u, 84u, 84u, 22u, 22u, 54u, 54u, 54u, 86u, 86u, 86u,
        24u, 24u, 56u, 56u, 56u, 88u, 88u, 88u, 26u, 26u, 58u, 58u, 58u, 90u, 90u, 90u,
        28u, 28u, 60u, 60u, 60u, 92u, 92u, 92u, 30u, 30u, 62u, 62u, 62u, 94u, 94u, 94u,
        95u, 95u, 95u, 63u, 63u, 63u, 31u, 31u, 93u, 93u, 93u, 61u, 61u, 61u, 29u, 29u,
        91u, 91u, 91u, 59u, 59u, 59u, 27u, 27u, 89u, 89u, 89u, 57u, 57u, 57u, 25u, 25u,
        87u, 87u, 87u, 55u, 55u, 55u, 23u, 23u, 85u, 85u, 85u, 53u, 53u, 53u, 21u, 21u,
        83u, 83u, 83u, 51u, 51u, 51u, 19u, 19u, 81u, 81u, 81u, 49u, 49u, 49u, 17u, 17u,
        17u, 79u, 79u, 47u, 47u, 47u, 15u, 15u, 15u, 77u, 77u, 45u, 45u, 45u, 13u, 13u,
        13u, 75u, 75u, 43u, 43u, 43u, 11u, 11u, 11u, 73u, 73u, 41u, 41u, 41u, 9u, 9u,
        9u, 71u, 71u, 71u, 39u, 39u, 7u, 7u, 7u, 69u, 69u, 69u, 37u, 37u, 5u, 5u,
        5u, 67u, 67u, 67u, 35u, 35u, 3u, 3u, 3u, 65u, 65u, 65u, 33u, 33u, 1u, 1u
    ),
    ubyteArrayOf(
        0u, 0u, 1u, 1u, 2u, 2u, 3u, 3u, 4u, 4u, 5u, 5u, 6u, 6u, 7u, 7u,
        8u, 8u, 9u, 9u, 10u, 10u, 11u, 11u, 12u, 12u, 13u, 13u, 14u, 14u, 15u, 15u,
        16u, 16u, 17u, 17u, 18u, 18u, 19u, 19u, 20u, 20u, 21u, 21u, 22u, 22u, 23u, 23u,
        24u, 24u, 25u, 25u, 26u, 26u, 27u, 27u, 28u, 28u, 29u, 29u, 30u, 30u, 31u, 31u,
        32u, 32u, 33u, 33u, 34u, 34u, 35u, 35u, 36u, 36u, 37u, 37u, 38u, 38u, 39u, 39u,
        40u, 40u, 41u, 41u, 42u, 42u, 43u, 43u, 44u, 44u, 45u, 45u, 46u, 46u, 47u, 47u,
        48u, 48u, 49u, 49u, 50u, 50u, 51u, 51u, 52u, 52u, 53u, 53u, 54u, 54u, 55u, 55u,
        56u, 56u, 57u, 57u, 58u, 58u, 59u, 59u, 60u, 60u, 61u, 61u, 62u, 62u, 63u, 63u,
        64u, 64u, 65u, 65u, 66u, 66u, 67u, 67u, 68u, 68u, 69u, 69u, 70u, 70u, 71u, 71u,
        72u, 72u, 73u, 73u, 74u, 74u, 75u, 75u, 76u, 76u, 77u, 77u, 78u, 78u, 79u, 79u,
        80u, 80u, 81u, 81u, 82u, 82u, 83u, 83u, 84u, 84u, 85u, 85u, 86u, 86u, 87u, 87u,
        88u, 88u, 89u, 89u, 90u, 90u, 91u, 91u, 92u, 92u, 93u, 93u, 94u, 94u, 95u, 95u,
        96u, 96u, 97u, 97u, 98u, 98u, 99u, 99u, 100u, 100u, 101u, 101u, 102u, 102u, 103u, 103u,
        104u, 104u, 105u, 105u, 106u, 106u, 107u, 107u, 108u, 108u, 109u, 109u, 110u, 110u, 111u, 111u,
        112u, 112u, 113u, 113u, 114u, 114u, 115u, 115u, 116u, 116u, 117u, 117u, 118u, 118u, 119u, 119u,
        120u, 120u, 121u, 121u, 122u, 122u, 123u, 123u, 124u, 124u, 125u, 125u, 126u, 126u, 127u, 127u
    ),
    ubyteArrayOf(
        0u, 32u, 32u, 64u, 96u, 96u, 128u, 128u, 2u, 34u, 34u, 66u, 98u, 98u, 130u, 130u,
        4u, 36u, 36u, 68u, 100u, 100u, 132u, 132u, 6u, 38u, 38u, 70u, 102u, 102u, 134u, 134u,
        8u, 40u, 40u, 72u, 104u, 104u, 136u, 136u, 10u, 42u, 42u, 74u, 106u, 106u, 138u, 138u,
        12u, 44u, 44u, 76u, 108u, 108u, 140u, 140u, 14u, 46u, 46u, 78u, 110u, 110u, 142u, 142u,
        16u, 48u, 48u, 80u, 112u, 112u, 144u, 144u, 18u, 50u, 50u, 82u, 114u, 114u, 146u, 146u,
        20u, 52u, 52u, 84u, 116u, 116u, 148u, 148u, 22u, 54u, 54u, 86u, 118u, 118u, 150u, 150u,
        24u, 56u, 56u, 88u, 120u, 120u, 152u, 152u, 26u, 58u, 58u, 90u, 122u, 122u, 154u, 154u,
        28u, 60u, 60u, 92u, 124u, 124u, 156u, 156u, 30u, 62u, 62u, 94u, 126u, 126u, 158u, 158u,
        159u, 159u, 127u, 127u, 95u, 63u, 63u, 31u, 157u, 157u, 125u, 125u, 93u, 61u, 61u, 29u,
        155u, 155u, 123u, 123u, 91u, 59u, 59u, 27u, 153u, 153u, 121u, 121u, 89u, 57u, 57u, 25u,
        151u, 151u, 119u, 119u, 87u, 55u, 55u, 23u, 149u, 149u, 117u, 117u, 85u, 53u, 53u, 21u,
        147u, 147u, 115u, 115u, 83u, 51u, 51u, 19u, 145u, 145u, 113u, 113u, 81u, 49u, 49u, 17u,
        143u, 143u, 111u, 111u, 79u, 47u, 47u, 15u, 141u, 141u, 109u, 109u, 77u, 45u, 45u, 13u,
        139u, 139u, 107u, 107u, 75u, 43u, 43u, 11u, 137u, 137u, 105u, 105u, 73u, 41u, 41u, 9u,
        135u, 135u, 103u, 103u, 71u, 39u, 39u, 7u, 133u, 133u, 101u, 101u, 69u, 37u, 37u, 5u,
        131u, 131u, 99u, 99u, 67u, 35u, 35u, 3u, 129u, 129u, 97u, 97u, 65u, 33u, 33u, 1u
    ),
    ubyteArrayOf(
        0u, 64u, 128u, 128u, 2u, 66u, 130u, 130u, 4u, 68u, 132u, 132u, 6u, 70u, 134u, 134u,
        8u, 72u, 136u, 136u, 10u, 74u, 138u, 138u, 12u, 76u, 140u, 140u, 14u, 78u, 142u, 142u,
        16u, 80u, 144u, 144u, 18u, 82u, 146u, 146u, 20u, 84u, 148u, 148u, 22u, 86u, 150u, 150u,
        24u, 88u, 152u, 152u, 26u, 90u, 154u, 154u, 28u, 92u, 156u, 156u, 30u, 94u, 158u, 158u,
        32u, 96u, 160u, 160u, 34u, 98u, 162u, 162u, 36u, 100u, 164u, 164u, 38u, 102u, 166u, 166u,
        40u, 104u, 168u, 168u, 42u, 106u, 170u, 170u, 44u, 108u, 172u, 172u, 46u, 110u, 174u, 174u,
        48u, 112u, 176u, 176u, 50u, 114u, 178u, 178u, 52u, 116u, 180u, 180u, 54u, 118u, 182u, 182u,
        56u, 120u, 184u, 184u, 58u, 122u, 186u, 186u, 60u, 124u, 188u, 188u, 62u, 126u, 190u, 190u,
        191u, 191u, 127u, 63u, 189u, 189u, 125u, 61u, 187u, 187u, 123u, 59u, 185u, 185u, 121u, 57u,
        183u, 183u, 119u, 55u, 181u, 181u, 117u, 53u, 179u, 179u, 115u, 51u, 177u, 177u, 113u, 49u,
        175u, 175u, 111u, 47u, 173u, 173u, 109u, 45u, 171u, 171u, 107u, 43u, 169u, 169u, 105u, 41u,
        167u, 167u, 103u, 39u, 165u, 165u, 101u, 37u, 163u, 163u, 99u, 35u, 161u, 161u, 97u, 33u,
        159u, 159u, 95u, 31u, 157u, 157u, 93u, 29u, 155u, 155u, 91u, 27u, 153u, 153u, 89u, 25u,
        151u, 151u, 87u, 23u, 149u, 149u, 85u, 21u, 147u, 147u, 83u, 19u, 145u, 145u, 81u, 17u,
        143u, 143u, 79u, 15u, 141u, 141u, 77u, 13u, 139u, 139u, 75u, 11u, 137u, 137u, 73u, 9u,
        135u, 135u, 71u, 7u, 133u, 133u, 69u, 5u, 131u, 131u, 67u, 3u, 129u, 129u, 65u, 1u
    ),
    ubyteArrayOf(
        0u, 1u, 2u, 3u, 4u, 5u, 6u, 7u, 8u, 9u, 10u, 11u, 12u, 13u, 14u, 15u,
        16u, 17u, 18u, 19u, 20u, 21u, 22u, 23u, 24u, 25u, 26u, 27u, 28u, 29u, 30u, 31u,
        32u, 33u, 34u, 35u, 36u, 37u, 38u, 39u, 40u, 41u, 42u, 43u, 44u, 45u, 46u, 47u,
        48u, 49u, 50u, 51u, 52u, 53u, 54u, 55u, 56u, 57u, 58u, 59u, 60u, 61u, 62u, 63u,
        64u, 65u, 66u, 67u, 68u, 69u, 70u, 71u, 72u, 73u, 74u, 75u, 76u, 77u, 78u, 79u,
        80u, 81u, 82u, 83u, 84u, 85u, 86u, 87u, 88u, 89u, 90u, 91u, 92u, 93u, 94u, 95u,
        96u, 97u, 98u, 99u, 100u, 101u, 102u, 103u, 104u, 105u, 106u, 107u, 108u, 109u, 110u, 111u,
        112u, 113u, 114u, 115u, 116u, 117u, 118u, 119u, 120u, 121u, 122u, 123u, 124u, 125u, 126u, 127u,
        128u, 129u, 130u, 131u, 132u, 133u, 134u, 135u, 136u, 137u, 138u, 139u, 140u, 141u, 142u, 143u,
        144u, 145u, 146u, 147u, 148u, 149u, 150u, 151u, 152u, 153u, 154u, 155u, 156u, 157u, 158u, 159u,
        160u, 161u, 162u, 163u, 164u, 165u, 166u, 167u, 168u, 169u, 170u, 171u, 172u, 173u, 174u, 175u,
        176u, 177u, 178u, 179u, 180u, 181u, 182u, 183u, 184u, 185u, 186u, 187u, 188u, 189u, 190u, 191u,
        192u, 193u, 194u, 195u, 196u, 197u, 198u, 199u, 200u, 201u, 202u, 203u, 204u, 205u, 206u, 207u,
        208u, 209u, 210u, 211u, 212u, 213u, 214u, 215u, 216u, 217u, 218u, 219u, 220u, 221u, 222u, 223u,
        224u, 225u, 226u, 227u, 228u, 229u, 230u, 231u, 232u, 233u, 234u, 235u, 236u, 237u, 238u, 239u,
        240u, 241u, 242u, 243u, 244u, 245u, 246u, 247u, 248u, 249u, 250u, 251u, 252u, 253u, 254u, 255u
    )
)

val colorUnquantizationTables : Array<UByteArray> = arrayOf(
    ubyteArrayOf(
        0u, 255u
        ),
    ubyteArrayOf(
        0u, 128u, 255u
        ),
    ubyteArrayOf(
        0u, 85u, 170u, 255u
        ),
    ubyteArrayOf(
        0u, 64u, 128u, 192u, 255u
        ),
    ubyteArrayOf(
        0u, 255u, 51u, 204u, 102u, 153u
        ),
    ubyteArrayOf(
        0u, 36u, 73u, 109u, 146u, 182u, 219u, 255u
        ),
    ubyteArrayOf(
        0u, 255u, 28u, 227u, 56u, 199u, 84u, 171u, 113u, 142u
        ),
    ubyteArrayOf(
        0u, 255u, 69u, 186u, 23u, 232u, 92u, 163u, 46u, 209u, 116u, 139u
    ),
    ubyteArrayOf(
        0u, 17u, 34u, 51u, 68u, 85u, 102u, 119u, 136u, 153u, 170u, 187u, 204u, 221u, 238u, 255u
    ),
    ubyteArrayOf(
        0u, 255u, 67u, 188u, 13u, 242u, 80u, 175u, 27u, 228u, 94u, 161u, 40u, 215u, 107u, 148u,
        54u, 201u, 121u, 134u
    ),
    ubyteArrayOf(
        0u, 255u, 33u, 222u, 66u, 189u, 99u, 156u, 11u, 244u, 44u, 211u, 77u, 178u, 110u, 145u,
        22u, 233u, 55u, 200u, 88u, 167u, 121u, 134u
    ),
    ubyteArrayOf(
        0u, 8u, 16u, 24u, 33u, 41u, 49u, 57u, 66u, 74u, 82u, 90u, 99u, 107u, 115u, 123u,
        132u, 140u, 148u, 156u, 165u, 173u, 181u, 189u, 198u, 206u, 214u, 222u, 231u, 239u, 247u, 255u
    ),
    ubyteArrayOf(
        0u, 255u, 32u, 223u, 65u, 190u, 97u, 158u, 6u, 249u, 39u, 216u, 71u, 184u, 104u, 151u,
        13u, 242u, 45u, 210u, 78u, 177u, 110u, 145u, 19u, 236u, 52u, 203u, 84u, 171u, 117u, 138u,
        26u, 229u, 58u, 197u, 91u, 164u, 123u, 132u
    ),
    ubyteArrayOf(
        0u, 255u, 16u, 239u, 32u, 223u, 48u, 207u, 65u, 190u, 81u, 174u, 97u, 158u, 113u, 142u,
        5u, 250u, 21u, 234u, 38u, 217u, 54u, 201u, 70u, 185u, 86u, 169u, 103u, 152u, 119u, 136u,
        11u, 244u, 27u, 228u, 43u, 212u, 59u, 196u, 76u, 179u, 92u, 163u, 108u, 147u, 124u, 131u
    ),
    ubyteArrayOf(
        0u, 4u, 8u, 12u, 16u, 20u, 24u, 28u, 32u, 36u, 40u, 44u, 48u, 52u, 56u, 60u,
        65u, 69u, 73u, 77u, 81u, 85u, 89u, 93u, 97u, 101u, 105u, 109u, 113u, 117u, 121u, 125u,
        130u, 134u, 138u, 142u, 146u, 150u, 154u, 158u, 162u, 166u, 170u, 174u, 178u, 182u, 186u, 190u,
        195u, 199u, 203u, 207u, 211u, 215u, 219u, 223u, 227u, 231u, 235u, 239u, 243u, 247u, 251u, 255u
    ),
    ubyteArrayOf(
        0u, 255u, 16u, 239u, 32u, 223u, 48u, 207u, 64u, 191u, 80u, 175u, 96u, 159u, 112u, 143u,
        3u, 252u, 19u, 236u, 35u, 220u, 51u, 204u, 67u, 188u, 83u, 172u, 100u, 155u, 116u, 139u,
        6u, 249u, 22u, 233u, 38u, 217u, 54u, 201u, 71u, 184u, 87u, 168u, 103u, 152u, 119u, 136u,
        9u, 246u, 25u, 230u, 42u, 213u, 58u, 197u, 74u, 181u, 90u, 165u, 106u, 149u, 122u, 133u,
        13u, 242u, 29u, 226u, 45u, 210u, 61u, 194u, 77u, 178u, 93u, 162u, 109u, 146u, 125u, 130u
    ),
    ubyteArrayOf(
        0u, 255u, 8u, 247u, 16u, 239u, 24u, 231u, 32u, 223u, 40u, 215u, 48u, 207u, 56u, 199u,
        64u, 191u, 72u, 183u, 80u, 175u, 88u, 167u, 96u, 159u, 104u, 151u, 112u, 143u, 120u, 135u,
        2u, 253u, 10u, 245u, 18u, 237u, 26u, 229u, 35u, 220u, 43u, 212u, 51u, 204u, 59u, 196u,
        67u, 188u, 75u, 180u, 83u, 172u, 91u, 164u, 99u, 156u, 107u, 148u, 115u, 140u, 123u, 132u,
        5u, 250u, 13u, 242u, 21u, 234u, 29u, 226u, 37u, 218u, 45u, 210u, 53u, 202u, 61u, 194u,
        70u, 185u, 78u, 177u, 86u, 169u, 94u, 161u, 102u, 153u, 110u, 145u, 118u, 137u, 126u, 129u
    ),
    ubyteArrayOf(
        0u, 2u, 4u, 6u, 8u, 10u, 12u, 14u, 16u, 18u, 20u, 22u, 24u, 26u, 28u, 30u,
        32u, 34u, 36u, 38u, 40u, 42u, 44u, 46u, 48u, 50u, 52u, 54u, 56u, 58u, 60u, 62u,
        64u, 66u, 68u, 70u, 72u, 74u, 76u, 78u, 80u, 82u, 84u, 86u, 88u, 90u, 92u, 94u,
        96u, 98u, 100u, 102u, 104u, 106u, 108u, 110u, 112u, 114u, 116u, 118u, 120u, 122u, 124u, 126u,
        129u, 131u, 133u, 135u, 137u, 139u, 141u, 143u, 145u, 147u, 149u, 151u, 153u, 155u, 157u, 159u,
        161u, 163u, 165u, 167u, 169u, 171u, 173u, 175u, 177u, 179u, 181u, 183u, 185u, 187u, 189u, 191u,
        193u, 195u, 197u, 199u, 201u, 203u, 205u, 207u, 209u, 211u, 213u, 215u, 217u, 219u, 221u, 223u,
        225u, 227u, 229u, 231u, 233u, 235u, 237u, 239u, 241u, 243u, 245u, 247u, 249u, 251u, 253u, 255u
    ),
    ubyteArrayOf(
        0u, 255u, 8u, 247u, 16u, 239u, 24u, 231u, 32u, 223u, 40u, 215u, 48u, 207u, 56u, 199u,
        64u, 191u, 72u, 183u, 80u, 175u, 88u, 167u, 96u, 159u, 104u, 151u, 112u, 143u, 120u, 135u,
        1u, 254u, 9u, 246u, 17u, 238u, 25u, 230u, 33u, 222u, 41u, 214u, 49u, 206u, 57u, 198u,
        65u, 190u, 73u, 182u, 81u, 174u, 89u, 166u, 97u, 158u, 105u, 150u, 113u, 142u, 121u, 134u,
        3u, 252u, 11u, 244u, 19u, 236u, 27u, 228u, 35u, 220u, 43u, 212u, 51u, 204u, 59u, 196u,
        67u, 188u, 75u, 180u, 83u, 172u, 91u, 164u, 99u, 156u, 107u, 148u, 115u, 140u, 123u, 132u,
        4u, 251u, 12u, 243u, 20u, 235u, 28u, 227u, 36u, 219u, 44u, 211u, 52u, 203u, 60u, 195u,
        68u, 187u, 76u, 179u, 84u, 171u, 92u, 163u, 100u, 155u, 108u, 147u, 116u, 139u, 124u, 131u,
        6u, 249u, 14u, 241u, 22u, 233u, 30u, 225u, 38u, 217u, 46u, 209u, 54u, 201u, 62u, 193u,
        70u, 185u, 78u, 177u, 86u, 169u, 94u, 161u, 102u, 153u, 110u, 145u, 118u, 137u, 126u, 129u
    ),
    ubyteArrayOf(
        0u, 255u, 4u, 251u, 8u, 247u, 12u, 243u, 16u, 239u, 20u, 235u, 24u, 231u, 28u, 227u,
        32u, 223u, 36u, 219u, 40u, 215u, 44u, 211u, 48u, 207u, 52u, 203u, 56u, 199u, 60u, 195u,
        64u, 191u, 68u, 187u, 72u, 183u, 76u, 179u, 80u, 175u, 84u, 171u, 88u, 167u, 92u, 163u,
        96u, 159u, 100u, 155u, 104u, 151u, 108u, 147u, 112u, 143u, 116u, 139u, 120u, 135u, 124u, 131u,
        1u, 254u, 5u, 250u, 9u, 246u, 13u, 242u, 17u, 238u, 21u, 234u, 25u, 230u, 29u, 226u,
        33u, 222u, 37u, 218u, 41u, 214u, 45u, 210u, 49u, 206u, 53u, 202u, 57u, 198u, 61u, 194u,
        65u, 190u, 69u, 186u, 73u, 182u, 77u, 178u, 81u, 174u, 85u, 170u, 89u, 166u, 93u, 162u,
        97u, 158u, 101u, 154u, 105u, 150u, 109u, 146u, 113u, 142u, 117u, 138u, 121u, 134u, 125u, 130u,
        2u, 253u, 6u, 249u, 10u, 245u, 14u, 241u, 18u, 237u, 22u, 233u, 26u, 229u, 30u, 225u,
        34u, 221u, 38u, 217u, 42u, 213u, 46u, 209u, 50u, 205u, 54u, 201u, 58u, 197u, 62u, 193u,
        66u, 189u, 70u, 185u, 74u, 181u, 78u, 177u, 82u, 173u, 86u, 169u, 90u, 165u, 94u, 161u,
        98u, 157u, 102u, 153u, 106u, 149u, 110u, 145u, 114u, 141u, 118u, 137u, 122u, 133u, 126u, 129u
    ),
    ubyteArrayOf(
        0u, 1u, 2u, 3u, 4u, 5u, 6u, 7u, 8u, 9u, 10u, 11u, 12u, 13u, 14u, 15u,
        16u, 17u, 18u, 19u, 20u, 21u, 22u, 23u, 24u, 25u, 26u, 27u, 28u, 29u, 30u, 31u,
        32u, 33u, 34u, 35u, 36u, 37u, 38u, 39u, 40u, 41u, 42u, 43u, 44u, 45u, 46u, 47u,
        48u, 49u, 50u, 51u, 52u, 53u, 54u, 55u, 56u, 57u, 58u, 59u, 60u, 61u, 62u, 63u,
        64u, 65u, 66u, 67u, 68u, 69u, 70u, 71u, 72u, 73u, 74u, 75u, 76u, 77u, 78u, 79u,
        80u, 81u, 82u, 83u, 84u, 85u, 86u, 87u, 88u, 89u, 90u, 91u, 92u, 93u, 94u, 95u,
        96u, 97u, 98u, 99u, 100u, 101u, 102u, 103u, 104u, 105u, 106u, 107u, 108u, 109u, 110u, 111u,
        112u, 113u, 114u, 115u, 116u, 117u, 118u, 119u, 120u, 121u, 122u, 123u, 124u, 125u, 126u, 127u,
        128u, 129u, 130u, 131u, 132u, 133u, 134u, 135u, 136u, 137u, 138u, 139u, 140u, 141u, 142u, 143u,
        144u, 145u, 146u, 147u, 148u, 149u, 150u, 151u, 152u, 153u, 154u, 155u, 156u, 157u, 158u, 159u,
        160u, 161u, 162u, 163u, 164u, 165u, 166u, 167u, 168u, 169u, 170u, 171u, 172u, 173u, 174u, 175u,
        176u, 177u, 178u, 179u, 180u, 181u, 182u, 183u, 184u, 185u, 186u, 187u, 188u, 189u, 190u, 191u,
        192u, 193u, 194u, 195u, 196u, 197u, 198u, 199u, 200u, 201u, 202u, 203u, 204u, 205u, 206u, 207u,
        208u, 209u, 210u, 211u, 212u, 213u, 214u, 215u, 216u, 217u, 218u, 219u, 220u, 221u, 222u, 223u,
        224u, 225u, 226u, 227u, 228u, 229u, 230u, 231u, 232u, 233u, 234u, 235u, 236u, 237u, 238u, 239u,
        240u, 241u, 242u, 243u, 244u, 245u, 246u, 247u, 248u, 249u, 250u, 251u, 252u, 253u, 254u, 255u
    )
)

// quantization_mode_table[integercount/2][bits] gives
// us the quantization level for a given integer count and number of bits that
// the integer may fit into. This is needed for color decoding,
// and for the color encoding.
var quantizationModeTable = buildQuantizationModeTable()

fun buildQuantizationModeTable() : Array<Array<Int>> {
    val quantizationModeTable = Array(17) {Array(128) {0} }
    for (i in 0..16)
        for (j in 0 until 128)
            quantizationModeTable[i][j] = -1

    for (i in 0 until 21)
        for (j in 1..16) {
            val p = computeIseBitcount(
                2 * j,
                QuantizationMethod.getByValue(i)
            )
            if(p < 128)
                quantizationModeTable[j][p] = i
        }
    for (i in 0..16) {
        var largestValueSoFar = -1
        for (j in 0 until 128) {
            if(quantizationModeTable[i][j] > largestValueSoFar)
                largestValueSoFar = quantizationModeTable[i][j]
            else
                quantizationModeTable[i][j] = largestValueSoFar
        }
    }
    return quantizationModeTable
}