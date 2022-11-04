/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

import com.wayfair.brickkit.brick.BaseBrick
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class BrickDiffUtilCallbackTest {
    private lateinit var callback: BrickDiffUtilCallback

    @Before
    fun setup() {
        val brick1 = mock<BaseBrick>()
        whenever(brick1.layout).thenReturn(LAYOUT_1)

        val brick2 = mock<BaseBrick>()
        whenever(brick2.layout).thenReturn(LAYOUT_2)

        callback = BrickDiffUtilCallback(listOf(brick1), listOf(brick1, brick2))
    }

    @Test
    fun testGetOldListSize() {
        assertEquals(1, callback.oldListSize)
    }

    @Test
    fun testGetNewListSize() {
        assertEquals(2, callback.newListSize)
    }

    @Test
    fun testAreItemsTheSame_same() {
        assertTrue(callback.areItemsTheSame(0, 0))
    }

    @Test
    fun testAreItemsTheSame_different() {
        assertFalse(callback.areItemsTheSame(0, 1))
    }

    @Test
    fun testAreContentsTheSame_same() {
        assertTrue(callback.areContentsTheSame(0, 0))
    }

    @Test
    fun testAreContentsTheSame_different() {
        assertFalse(callback.areContentsTheSame(0, 1))
    }

    companion object {
        private const val LAYOUT_1 = 1
        private const val LAYOUT_2 = 2
    }
}
