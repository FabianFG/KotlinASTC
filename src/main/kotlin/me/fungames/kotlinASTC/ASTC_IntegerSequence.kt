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
 *	@brief	Functions to encode/decode data using Bounded Integer Sequence
 *			Encoding.
 *	@author rewritten to Kotlin by FunGames
 */
/*----------------------------------------------------------------------------*/

@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")
package me.fungames.kotlinASTC


// unpacked quint triplets <low,middle,high> for each packed-quint value
val quintsOfInteger = arrayOf(
    ubyteArrayOf(0u, 0u, 0u),	ubyteArrayOf(1u, 0u, 0u),	ubyteArrayOf(2u, 0u, 0u),	ubyteArrayOf(3u, 0u, 0u),
    ubyteArrayOf(4u, 0u, 0u),	ubyteArrayOf(0u, 4u, 0u),	ubyteArrayOf(4u, 4u, 0u),	ubyteArrayOf(4u, 4u, 4u),
    ubyteArrayOf(0u, 1u, 0u),	ubyteArrayOf(1u, 1u, 0u),	ubyteArrayOf(2u, 1u, 0u),	ubyteArrayOf(3u, 1u, 0u),
    ubyteArrayOf(4u, 1u, 0u),	ubyteArrayOf(1u, 4u, 0u),	ubyteArrayOf(4u, 4u, 1u),	ubyteArrayOf(4u, 4u, 4u),
    ubyteArrayOf(0u, 2u, 0u),	ubyteArrayOf(1u, 2u, 0u),	ubyteArrayOf(2u, 2u, 0u),	ubyteArrayOf(3u, 2u, 0u),
    ubyteArrayOf(4u, 2u, 0u),	ubyteArrayOf(2u, 4u, 0u),	ubyteArrayOf(4u, 4u, 2u),	ubyteArrayOf(4u, 4u, 4u),
    ubyteArrayOf(0u, 3u, 0u),	ubyteArrayOf(1u, 3u, 0u),	ubyteArrayOf(2u, 3u, 0u),	ubyteArrayOf(3u, 3u, 0u),
    ubyteArrayOf(4u, 3u, 0u),	ubyteArrayOf(3u, 4u, 0u),	ubyteArrayOf(4u, 4u, 3u),	ubyteArrayOf(4u, 4u, 4u),
    ubyteArrayOf(0u, 0u, 1u),	ubyteArrayOf(1u, 0u, 1u),	ubyteArrayOf(2u, 0u, 1u),	ubyteArrayOf(3u, 0u, 1u),
    ubyteArrayOf(4u, 0u, 1u),	ubyteArrayOf(0u, 4u, 1u),	ubyteArrayOf(4u, 0u, 4u),	ubyteArrayOf(0u, 4u, 4u),
    ubyteArrayOf(0u, 1u, 1u),	ubyteArrayOf(1u, 1u, 1u),	ubyteArrayOf(2u, 1u, 1u),	ubyteArrayOf(3u, 1u, 1u),
    ubyteArrayOf(4u, 1u, 1u),	ubyteArrayOf(1u, 4u, 1u),	ubyteArrayOf(4u, 1u, 4u),	ubyteArrayOf(1u, 4u, 4u),
    ubyteArrayOf(0u, 2u, 1u),	ubyteArrayOf(1u, 2u, 1u),	ubyteArrayOf(2u, 2u, 1u),	ubyteArrayOf(3u, 2u, 1u),
    ubyteArrayOf(4u, 2u, 1u),	ubyteArrayOf(2u, 4u, 1u),	ubyteArrayOf(4u, 2u, 4u),	ubyteArrayOf(2u, 4u, 4u),
    ubyteArrayOf(0u, 3u, 1u),	ubyteArrayOf(1u, 3u, 1u),	ubyteArrayOf(2u, 3u, 1u),	ubyteArrayOf(3u, 3u, 1u),
    ubyteArrayOf(4u, 3u, 1u),	ubyteArrayOf(3u, 4u, 1u),	ubyteArrayOf(4u, 3u, 4u),	ubyteArrayOf(3u, 4u, 4u),
    ubyteArrayOf(0u, 0u, 2u),	ubyteArrayOf(1u, 0u, 2u),	ubyteArrayOf(2u, 0u, 2u),	ubyteArrayOf(3u, 0u, 2u),
    ubyteArrayOf(4u, 0u, 2u),	ubyteArrayOf(0u, 4u, 2u),	ubyteArrayOf(2u, 0u, 4u),	ubyteArrayOf(3u, 0u, 4u),
    ubyteArrayOf(0u, 1u, 2u),	ubyteArrayOf(1u, 1u, 2u),	ubyteArrayOf(2u, 1u, 2u),	ubyteArrayOf(3u, 1u, 2u),
    ubyteArrayOf(4u, 1u, 2u),	ubyteArrayOf(1u, 4u, 2u),	ubyteArrayOf(2u, 1u, 4u),	ubyteArrayOf(3u, 1u, 4u),
    ubyteArrayOf(0u, 2u, 2u),	ubyteArrayOf(1u, 2u, 2u),	ubyteArrayOf(2u, 2u, 2u),	ubyteArrayOf(3u, 2u, 2u),
    ubyteArrayOf(4u, 2u, 2u),	ubyteArrayOf(2u, 4u, 2u),	ubyteArrayOf(2u, 2u, 4u),	ubyteArrayOf(3u, 2u, 4u),
    ubyteArrayOf(0u, 3u, 2u),	ubyteArrayOf(1u, 3u, 2u),	ubyteArrayOf(2u, 3u, 2u),	ubyteArrayOf(3u, 3u, 2u),
    ubyteArrayOf(4u, 3u, 2u),	ubyteArrayOf(3u, 4u, 2u),	ubyteArrayOf(2u, 3u, 4u),	ubyteArrayOf(3u, 3u, 4u),
    ubyteArrayOf(0u, 0u, 3u),	ubyteArrayOf(1u, 0u, 3u),	ubyteArrayOf(2u, 0u, 3u),	ubyteArrayOf(3u, 0u, 3u),
    ubyteArrayOf(4u, 0u, 3u),	ubyteArrayOf(0u, 4u, 3u),	ubyteArrayOf(0u, 0u, 4u),	ubyteArrayOf(1u, 0u, 4u),
    ubyteArrayOf(0u, 1u, 3u),	ubyteArrayOf(1u, 1u, 3u),	ubyteArrayOf(2u, 1u, 3u),	ubyteArrayOf(3u, 1u, 3u),
    ubyteArrayOf(4u, 1u, 3u),	ubyteArrayOf(1u, 4u, 3u),	ubyteArrayOf(0u, 1u, 4u),	ubyteArrayOf(1u, 1u, 4u),
    ubyteArrayOf(0u, 2u, 3u),	ubyteArrayOf(1u, 2u, 3u),	ubyteArrayOf(2u, 2u, 3u),	ubyteArrayOf(3u, 2u, 3u),
    ubyteArrayOf(4u, 2u, 3u),	ubyteArrayOf(2u, 4u, 3u),	ubyteArrayOf(0u, 2u, 4u),	ubyteArrayOf(1u, 2u, 4u),
    ubyteArrayOf(0u, 3u, 3u),	ubyteArrayOf(1u, 3u, 3u),	ubyteArrayOf(2u, 3u, 3u),	ubyteArrayOf(3u, 3u, 3u),
    ubyteArrayOf(4u, 3u, 3u),	ubyteArrayOf(3u, 4u, 3u),	ubyteArrayOf(0u, 3u, 4u),	ubyteArrayOf(1u, 3u, 4u)
)

// unpacked trit quintuplets <low,_,_,_,high> for each packed-quint value
val tritsOfInteger = arrayOf(
    ubyteArrayOf(0u, 0u, 0u, 0u, 0u),	ubyteArrayOf(1u, 0u, 0u, 0u, 0u),	ubyteArrayOf(2u, 0u, 0u, 0u, 0u),	ubyteArrayOf(0u, 0u, 2u, 0u, 0u),
    ubyteArrayOf(0u, 1u, 0u, 0u, 0u),	ubyteArrayOf(1u, 1u, 0u, 0u, 0u),	ubyteArrayOf(2u, 1u, 0u, 0u, 0u),	ubyteArrayOf(1u, 0u, 2u, 0u, 0u),
    ubyteArrayOf(0u, 2u, 0u, 0u, 0u),	ubyteArrayOf(1u, 2u, 0u, 0u, 0u),	ubyteArrayOf(2u, 2u, 0u, 0u, 0u),	ubyteArrayOf(2u, 0u, 2u, 0u, 0u),
    ubyteArrayOf(0u, 2u, 2u, 0u, 0u),	ubyteArrayOf(1u, 2u, 2u, 0u, 0u),	ubyteArrayOf(2u, 2u, 2u, 0u, 0u),	ubyteArrayOf(2u, 0u, 2u, 0u, 0u),
    ubyteArrayOf(0u, 0u, 1u, 0u, 0u),	ubyteArrayOf(1u, 0u, 1u, 0u, 0u),	ubyteArrayOf(2u, 0u, 1u, 0u, 0u),	ubyteArrayOf(0u, 1u, 2u, 0u, 0u),
    ubyteArrayOf(0u, 1u, 1u, 0u, 0u),	ubyteArrayOf(1u, 1u, 1u, 0u, 0u),	ubyteArrayOf(2u, 1u, 1u, 0u, 0u),	ubyteArrayOf(1u, 1u, 2u, 0u, 0u),
    ubyteArrayOf(0u, 2u, 1u, 0u, 0u),	ubyteArrayOf(1u, 2u, 1u, 0u, 0u),	ubyteArrayOf(2u, 2u, 1u, 0u, 0u),	ubyteArrayOf(2u, 1u, 2u, 0u, 0u),
    ubyteArrayOf(0u, 0u, 0u, 2u, 2u),	ubyteArrayOf(1u, 0u, 0u, 2u, 2u),	ubyteArrayOf(2u, 0u, 0u, 2u, 2u),	ubyteArrayOf(0u, 0u, 2u, 2u, 2u),
    ubyteArrayOf(0u, 0u, 0u, 1u, 0u),	ubyteArrayOf(1u, 0u, 0u, 1u, 0u),	ubyteArrayOf(2u, 0u, 0u, 1u, 0u),	ubyteArrayOf(0u, 0u, 2u, 1u, 0u),
    ubyteArrayOf(0u, 1u, 0u, 1u, 0u),	ubyteArrayOf(1u, 1u, 0u, 1u, 0u),	ubyteArrayOf(2u, 1u, 0u, 1u, 0u),	ubyteArrayOf(1u, 0u, 2u, 1u, 0u),
    ubyteArrayOf(0u, 2u, 0u, 1u, 0u),	ubyteArrayOf(1u, 2u, 0u, 1u, 0u),	ubyteArrayOf(2u, 2u, 0u, 1u, 0u),	ubyteArrayOf(2u, 0u, 2u, 1u, 0u),
    ubyteArrayOf(0u, 2u, 2u, 1u, 0u),	ubyteArrayOf(1u, 2u, 2u, 1u, 0u),	ubyteArrayOf(2u, 2u, 2u, 1u, 0u),	ubyteArrayOf(2u, 0u, 2u, 1u, 0u),
    ubyteArrayOf(0u, 0u, 1u, 1u, 0u),	ubyteArrayOf(1u, 0u, 1u, 1u, 0u),	ubyteArrayOf(2u, 0u, 1u, 1u, 0u),	ubyteArrayOf(0u, 1u, 2u, 1u, 0u),
    ubyteArrayOf(0u, 1u, 1u, 1u, 0u),	ubyteArrayOf(1u, 1u, 1u, 1u, 0u),	ubyteArrayOf(2u, 1u, 1u, 1u, 0u),	ubyteArrayOf(1u, 1u, 2u, 1u, 0u),
    ubyteArrayOf(0u, 2u, 1u, 1u, 0u),	ubyteArrayOf(1u, 2u, 1u, 1u, 0u),	ubyteArrayOf(2u, 2u, 1u, 1u, 0u),	ubyteArrayOf(2u, 1u, 2u, 1u, 0u),
    ubyteArrayOf(0u, 1u, 0u, 2u, 2u),	ubyteArrayOf(1u, 1u, 0u, 2u, 2u),	ubyteArrayOf(2u, 1u, 0u, 2u, 2u),	ubyteArrayOf(1u, 0u, 2u, 2u, 2u),
    ubyteArrayOf(0u, 0u, 0u, 2u, 0u),	ubyteArrayOf(1u, 0u, 0u, 2u, 0u),	ubyteArrayOf(2u, 0u, 0u, 2u, 0u),	ubyteArrayOf(0u, 0u, 2u, 2u, 0u),
    ubyteArrayOf(0u, 1u, 0u, 2u, 0u),	ubyteArrayOf(1u, 1u, 0u, 2u, 0u),	ubyteArrayOf(2u, 1u, 0u, 2u, 0u),	ubyteArrayOf(1u, 0u, 2u, 2u, 0u),
    ubyteArrayOf(0u, 2u, 0u, 2u, 0u),	ubyteArrayOf(1u, 2u, 0u, 2u, 0u),	ubyteArrayOf(2u, 2u, 0u, 2u, 0u),	ubyteArrayOf(2u, 0u, 2u, 2u, 0u),
    ubyteArrayOf(0u, 2u, 2u, 2u, 0u),	ubyteArrayOf(1u, 2u, 2u, 2u, 0u),	ubyteArrayOf(2u, 2u, 2u, 2u, 0u),	ubyteArrayOf(2u, 0u, 2u, 2u, 0u),
    ubyteArrayOf(0u, 0u, 1u, 2u, 0u),	ubyteArrayOf(1u, 0u, 1u, 2u, 0u),	ubyteArrayOf(2u, 0u, 1u, 2u, 0u),	ubyteArrayOf(0u, 1u, 2u, 2u, 0u),
    ubyteArrayOf(0u, 1u, 1u, 2u, 0u),	ubyteArrayOf(1u, 1u, 1u, 2u, 0u),	ubyteArrayOf(2u, 1u, 1u, 2u, 0u),	ubyteArrayOf(1u, 1u, 2u, 2u, 0u),
    ubyteArrayOf(0u, 2u, 1u, 2u, 0u),	ubyteArrayOf(1u, 2u, 1u, 2u, 0u),	ubyteArrayOf(2u, 2u, 1u, 2u, 0u),	ubyteArrayOf(2u, 1u, 2u, 2u, 0u),
    ubyteArrayOf(0u, 2u, 0u, 2u, 2u),	ubyteArrayOf(1u, 2u, 0u, 2u, 2u),	ubyteArrayOf(2u, 2u, 0u, 2u, 2u),	ubyteArrayOf(2u, 0u, 2u, 2u, 2u),
    ubyteArrayOf(0u, 0u, 0u, 0u, 2u),	ubyteArrayOf(1u, 0u, 0u, 0u, 2u),	ubyteArrayOf(2u, 0u, 0u, 0u, 2u),	ubyteArrayOf(0u, 0u, 2u, 0u, 2u),
    ubyteArrayOf(0u, 1u, 0u, 0u, 2u),	ubyteArrayOf(1u, 1u, 0u, 0u, 2u),	ubyteArrayOf(2u, 1u, 0u, 0u, 2u),	ubyteArrayOf(1u, 0u, 2u, 0u, 2u),
    ubyteArrayOf(0u, 2u, 0u, 0u, 2u),	ubyteArrayOf(1u, 2u, 0u, 0u, 2u),	ubyteArrayOf(2u, 2u, 0u, 0u, 2u),	ubyteArrayOf(2u, 0u, 2u, 0u, 2u),
    ubyteArrayOf(0u, 2u, 2u, 0u, 2u),	ubyteArrayOf(1u, 2u, 2u, 0u, 2u),	ubyteArrayOf(2u, 2u, 2u, 0u, 2u),	ubyteArrayOf(2u, 0u, 2u, 0u, 2u),
    ubyteArrayOf(0u, 0u, 1u, 0u, 2u),	ubyteArrayOf(1u, 0u, 1u, 0u, 2u),	ubyteArrayOf(2u, 0u, 1u, 0u, 2u),	ubyteArrayOf(0u, 1u, 2u, 0u, 2u),
    ubyteArrayOf(0u, 1u, 1u, 0u, 2u),	ubyteArrayOf(1u, 1u, 1u, 0u, 2u),	ubyteArrayOf(2u, 1u, 1u, 0u, 2u),	ubyteArrayOf(1u, 1u, 2u, 0u, 2u),
    ubyteArrayOf(0u, 2u, 1u, 0u, 2u),	ubyteArrayOf(1u, 2u, 1u, 0u, 2u),	ubyteArrayOf(2u, 2u, 1u, 0u, 2u),	ubyteArrayOf(2u, 1u, 2u, 0u, 2u),
    ubyteArrayOf(0u, 2u, 2u, 2u, 2u),	ubyteArrayOf(1u, 2u, 2u, 2u, 2u),	ubyteArrayOf(2u, 2u, 2u, 2u, 2u),	ubyteArrayOf(2u, 0u, 2u, 2u, 2u),
    ubyteArrayOf(0u, 0u, 0u, 0u, 1u),	ubyteArrayOf(1u, 0u, 0u, 0u, 1u),	ubyteArrayOf(2u, 0u, 0u, 0u, 1u),	ubyteArrayOf(0u, 0u, 2u, 0u, 1u),
    ubyteArrayOf(0u, 1u, 0u, 0u, 1u),	ubyteArrayOf(1u, 1u, 0u, 0u, 1u),	ubyteArrayOf(2u, 1u, 0u, 0u, 1u),	ubyteArrayOf(1u, 0u, 2u, 0u, 1u),
    ubyteArrayOf(0u, 2u, 0u, 0u, 1u),	ubyteArrayOf(1u, 2u, 0u, 0u, 1u),	ubyteArrayOf(2u, 2u, 0u, 0u, 1u),	ubyteArrayOf(2u, 0u, 2u, 0u, 1u),
    ubyteArrayOf(0u, 2u, 2u, 0u, 1u),	ubyteArrayOf(1u, 2u, 2u, 0u, 1u),	ubyteArrayOf(2u, 2u, 2u, 0u, 1u),	ubyteArrayOf(2u, 0u, 2u, 0u, 1u),
    ubyteArrayOf(0u, 0u, 1u, 0u, 1u),	ubyteArrayOf(1u, 0u, 1u, 0u, 1u),	ubyteArrayOf(2u, 0u, 1u, 0u, 1u),	ubyteArrayOf(0u, 1u, 2u, 0u, 1u),
    ubyteArrayOf(0u, 1u, 1u, 0u, 1u),	ubyteArrayOf(1u, 1u, 1u, 0u, 1u),	ubyteArrayOf(2u, 1u, 1u, 0u, 1u),	ubyteArrayOf(1u, 1u, 2u, 0u, 1u),
    ubyteArrayOf(0u, 2u, 1u, 0u, 1u),	ubyteArrayOf(1u, 2u, 1u, 0u, 1u),	ubyteArrayOf(2u, 2u, 1u, 0u, 1u),	ubyteArrayOf(2u, 1u, 2u, 0u, 1u),
    ubyteArrayOf(0u, 0u, 1u, 2u, 2u),	ubyteArrayOf(1u, 0u, 1u, 2u, 2u),	ubyteArrayOf(2u, 0u, 1u, 2u, 2u),	ubyteArrayOf(0u, 1u, 2u, 2u, 2u),
    ubyteArrayOf(0u, 0u, 0u, 1u, 1u),	ubyteArrayOf(1u, 0u, 0u, 1u, 1u),	ubyteArrayOf(2u, 0u, 0u, 1u, 1u),	ubyteArrayOf(0u, 0u, 2u, 1u, 1u),
    ubyteArrayOf(0u, 1u, 0u, 1u, 1u),	ubyteArrayOf(1u, 1u, 0u, 1u, 1u),	ubyteArrayOf(2u, 1u, 0u, 1u, 1u),	ubyteArrayOf(1u, 0u, 2u, 1u, 1u),
    ubyteArrayOf(0u, 2u, 0u, 1u, 1u),	ubyteArrayOf(1u, 2u, 0u, 1u, 1u),	ubyteArrayOf(2u, 2u, 0u, 1u, 1u),	ubyteArrayOf(2u, 0u, 2u, 1u, 1u),
    ubyteArrayOf(0u, 2u, 2u, 1u, 1u),	ubyteArrayOf(1u, 2u, 2u, 1u, 1u),	ubyteArrayOf(2u, 2u, 2u, 1u, 1u),	ubyteArrayOf(2u, 0u, 2u, 1u, 1u),
    ubyteArrayOf(0u, 0u, 1u, 1u, 1u),	ubyteArrayOf(1u, 0u, 1u, 1u, 1u),	ubyteArrayOf(2u, 0u, 1u, 1u, 1u),	ubyteArrayOf(0u, 1u, 2u, 1u, 1u),
    ubyteArrayOf(0u, 1u, 1u, 1u, 1u),	ubyteArrayOf(1u, 1u, 1u, 1u, 1u),	ubyteArrayOf(2u, 1u, 1u, 1u, 1u),	ubyteArrayOf(1u, 1u, 2u, 1u, 1u),
    ubyteArrayOf(0u, 2u, 1u, 1u, 1u),	ubyteArrayOf(1u, 2u, 1u, 1u, 1u),	ubyteArrayOf(2u, 2u, 1u, 1u, 1u),	ubyteArrayOf(2u, 1u, 2u, 1u, 1u),
    ubyteArrayOf(0u, 1u, 1u, 2u, 2u),	ubyteArrayOf(1u, 1u, 1u, 2u, 2u),	ubyteArrayOf(2u, 1u, 1u, 2u, 2u),	ubyteArrayOf(1u, 1u, 2u, 2u, 2u),
    ubyteArrayOf(0u, 0u, 0u, 2u, 1u),	ubyteArrayOf(1u, 0u, 0u, 2u, 1u),	ubyteArrayOf(2u, 0u, 0u, 2u, 1u),	ubyteArrayOf(0u, 0u, 2u, 2u, 1u),
    ubyteArrayOf(0u, 1u, 0u, 2u, 1u),	ubyteArrayOf(1u, 1u, 0u, 2u, 1u),	ubyteArrayOf(2u, 1u, 0u, 2u, 1u),	ubyteArrayOf(1u, 0u, 2u, 2u, 1u),
    ubyteArrayOf(0u, 2u, 0u, 2u, 1u),	ubyteArrayOf(1u, 2u, 0u, 2u, 1u),	ubyteArrayOf(2u, 2u, 0u, 2u, 1u),	ubyteArrayOf(2u, 0u, 2u, 2u, 1u),
    ubyteArrayOf(0u, 2u, 2u, 2u, 1u),	ubyteArrayOf(1u, 2u, 2u, 2u, 1u),	ubyteArrayOf(2u, 2u, 2u, 2u, 1u),	ubyteArrayOf(2u, 0u, 2u, 2u, 1u),
    ubyteArrayOf(0u, 0u, 1u, 2u, 1u),	ubyteArrayOf(1u, 0u, 1u, 2u, 1u),	ubyteArrayOf(2u, 0u, 1u, 2u, 1u),	ubyteArrayOf(0u, 1u, 2u, 2u, 1u),
    ubyteArrayOf(0u, 1u, 1u, 2u, 1u),	ubyteArrayOf(1u, 1u, 1u, 2u, 1u),	ubyteArrayOf(2u, 1u, 1u, 2u, 1u),	ubyteArrayOf(1u, 1u, 2u, 2u, 1u),
    ubyteArrayOf(0u, 2u, 1u, 2u, 1u),	ubyteArrayOf(1u, 2u, 1u, 2u, 1u),	ubyteArrayOf(2u, 2u, 1u, 2u, 1u),	ubyteArrayOf(2u, 1u, 2u, 2u, 1u),
    ubyteArrayOf(0u, 2u, 1u, 2u, 2u),	ubyteArrayOf(1u, 2u, 1u, 2u, 2u),	ubyteArrayOf(2u, 2u, 1u, 2u, 2u),	ubyteArrayOf(2u, 1u, 2u, 2u, 2u),
    ubyteArrayOf(0u, 0u, 0u, 1u, 2u),	ubyteArrayOf(1u, 0u, 0u, 1u, 2u),	ubyteArrayOf(2u, 0u, 0u, 1u, 2u),	ubyteArrayOf(0u, 0u, 2u, 1u, 2u),
    ubyteArrayOf(0u, 1u, 0u, 1u, 2u),	ubyteArrayOf(1u, 1u, 0u, 1u, 2u),	ubyteArrayOf(2u, 1u, 0u, 1u, 2u),	ubyteArrayOf(1u, 0u, 2u, 1u, 2u),
    ubyteArrayOf(0u, 2u, 0u, 1u, 2u),	ubyteArrayOf(1u, 2u, 0u, 1u, 2u),	ubyteArrayOf(2u, 2u, 0u, 1u, 2u),	ubyteArrayOf(2u, 0u, 2u, 1u, 2u),
    ubyteArrayOf(0u, 2u, 2u, 1u, 2u),	ubyteArrayOf(1u, 2u, 2u, 1u, 2u),	ubyteArrayOf(2u, 2u, 2u, 1u, 2u),	ubyteArrayOf(2u, 0u, 2u, 1u, 2u),
    ubyteArrayOf(0u, 0u, 1u, 1u, 2u),	ubyteArrayOf(1u, 0u, 1u, 1u, 2u),	ubyteArrayOf(2u, 0u, 1u, 1u, 2u),	ubyteArrayOf(0u, 1u, 2u, 1u, 2u),
    ubyteArrayOf(0u, 1u, 1u, 1u, 2u),	ubyteArrayOf(1u, 1u, 1u, 1u, 2u),	ubyteArrayOf(2u, 1u, 1u, 1u, 2u),	ubyteArrayOf(1u, 1u, 2u, 1u, 2u),
    ubyteArrayOf(0u, 2u, 1u, 1u, 2u),	ubyteArrayOf(1u, 2u, 1u, 1u, 2u),	ubyteArrayOf(2u, 2u, 1u, 1u, 2u),	ubyteArrayOf(2u, 1u, 2u, 1u, 2u),
    ubyteArrayOf(0u, 2u, 2u, 2u, 2u),	ubyteArrayOf(1u, 2u, 2u, 2u, 2u),	ubyteArrayOf(2u, 2u, 2u, 2u, 2u),	ubyteArrayOf(2u, 1u, 2u, 2u, 2u)
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

fun decodeIse(quantizationLevel : QuantizationMethod, elements : Int, inputData : UByteArray, outputData : UByteArray, bitOffset : Int) {
    var bitOffset1 = bitOffset
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
        results[i] = readBits(bits, bitOffset1, inputData).toUByte()
        bitOffset1 += bits
        if(trits != 0) {
            val bitsToRead = arrayOf(2, 2, 1, 2, 1)
            val blockShift = arrayOf(0, 2, 4, 5, 7)
            val next1Counter = arrayOf(1, 2, 3, 4, 0)
            val hcounterIncr = arrayOf(0, 0, 0, 0, 1)
            val tdata = readBits(bitsToRead[lCounter], bitOffset1, inputData)
            bitOffset1 += bitsToRead[lCounter]
            tqBlocks[hCounter] = (tqBlocks[hCounter] or (tdata shl blockShift[lCounter]).toUByte())
            hCounter += hcounterIncr[lCounter]
            lCounter = next1Counter[lCounter]
        }
        if(quints != 0) {
            val bitsToRead = arrayOf(3, 2, 2)
            val blockShift = arrayOf(0, 3, 5)
            val next1Counter = arrayOf(1, 2, 0)
            val hcounterIncr = arrayOf(0, 0, 1)
            val tdata = readBits(bitsToRead[lCounter], bitOffset1, inputData)
            bitOffset1 += bitsToRead[lCounter]
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
            results[5 * i] = (results[5 * i] or ((tritPtr[0].toInt() shl bits).toUByte()))
            results[5 * i + 1] = (results[5 * i + 1] or ((tritPtr[1].toInt() shl bits).toUByte()))
            results[5 * i + 2] = (results[5 * i + 2] or ((tritPtr[2].toInt() shl bits).toUByte()))
            results[5 * i + 3] = (results[5 * i + 3] or ((tritPtr[3].toInt() shl bits).toUByte()))
            results[5 * i + 4] = (results[5 * i + 4] or ((tritPtr[4].toInt() shl bits).toUByte()))
        }
    }

    if(quints != 0) {
        val quintsBlocks = (elements + 2) / 3
        for(i in 0 until quintsBlocks) {
            val quintPtr = quintsOfInteger[tqBlocks[i].toInt()]
            results[3 * i] = results[3 * i] or (quintPtr[0].toInt() shl bits).toUByte()
            results[3 * i + 1] = results[3 * i + 1] or (quintPtr[1].toInt() shl bits).toUByte()
            results[3 * i + 2] = results[3 * i + 2] or (quintPtr[2].toInt() shl bits).toUByte()
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