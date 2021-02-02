/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding

import org.junit.Assert.assertEquals
import org.junit.Test

class BrickPaddingTest {
    private val brickPadding = BrickPadding(
        INNER_LEFT,
        INNER_TOP,
        INNER_RIGHT,
        INNER_BOTTOM,
        OUTER_LEFT,
        OUTER_TOP,
        OUTER_RIGHT,
        OUTER_BOTTOM
    )

    @Test
    fun testGetInnerLeftPadding() {
        assertEquals(INNER_LEFT, brickPadding.innerLeftPadding)
    }

    @Test
    fun testGetInnerTopPadding() {
        assertEquals(INNER_TOP, brickPadding.innerTopPadding)
    }

    @Test
    fun testGetInnerRightPadding() {
        assertEquals(INNER_RIGHT, brickPadding.innerRightPadding)
    }

    @Test
    fun testGetInnerBottomPadding() {
        assertEquals(INNER_BOTTOM, brickPadding.innerBottomPadding)
    }

    @Test
    fun testGetOuterLeftPadding() {
        assertEquals(OUTER_LEFT, brickPadding.outerLeftPadding)
    }

    @Test
    fun testGetOuterTopPadding() {
        assertEquals(OUTER_TOP, brickPadding.outerTopPadding)
    }

    @Test
    fun testGetOuterRightPadding() {
        assertEquals(OUTER_RIGHT, brickPadding.outerRightPadding)
    }

    @Test
    fun testGetOuterBottomPadding() {
        assertEquals(OUTER_BOTTOM, brickPadding.outerBottomPadding)
    }

    companion object {
        private const val INNER_LEFT = 1
        private const val INNER_TOP = 2
        private const val INNER_RIGHT = 3
        private const val INNER_BOTTOM = 4
        private const val OUTER_LEFT = 5
        private const val OUTER_TOP = 6
        private const val OUTER_RIGHT = 7
        private const val OUTER_BOTTOM = 8
    }
}
