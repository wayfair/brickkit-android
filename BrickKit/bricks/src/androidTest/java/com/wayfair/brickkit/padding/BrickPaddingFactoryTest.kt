/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding

import android.content.res.Resources
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wayfair.brickkit.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BrickPaddingFactoryTest {
    private lateinit var brickPaddingFactory: BrickPaddingFactory

    @Before
    fun setup() {
        val resources = mock<Resources>()
        whenever(resources.getDimension(R.dimen.no_dp)).thenReturn(0f)
        whenever(resources.getDimension(ONE_DP)).thenReturn(1f)
        whenever(resources.getDimension(TWO_DP)).thenReturn(2f)
        whenever(resources.getDimension(THREE_DP)).thenReturn(3f)
        whenever(resources.getDimension(FOUR_DP)).thenReturn(4f)
        whenever(resources.getDimension(FIVE_DP)).thenReturn(5f)
        whenever(resources.getDimension(SIX_DP)).thenReturn(6f)
        whenever(resources.getDimension(SEVEN_DP)).thenReturn(7f)
        whenever(resources.getDimension(EIGHT_DP)).thenReturn(8f)
        whenever(resources.getDimension(R.dimen.standard_margin_named_default_half))
                .thenReturn(11f)
        whenever(resources.getDimension(R.dimen.standard_margin_named_view_inset))
                .thenReturn(12f)
        brickPaddingFactory = BrickPaddingFactory(resources)
    }

    @Test
    fun testGetSimpleBrickPadding() {
        brickPaddingFactory.getSimpleBrickPadding(ONE_DP).apply {
            assertEquals(1, innerLeftPadding)
            assertEquals(1, innerTopPadding)
            assertEquals(1, innerRightPadding)
            assertEquals(1, innerBottomPadding)
            assertEquals(1, outerLeftPadding)
            assertEquals(1, outerTopPadding)
            assertEquals(1, outerRightPadding)
            assertEquals(1, outerBottomPadding)
        }
    }

    @Test
    fun testGetInnerOuterBrickPadding_defaults() {
        brickPaddingFactory.getInnerOuterBrickPadding().apply {
            assertEquals(0, innerLeftPadding)
            assertEquals(0, innerTopPadding)
            assertEquals(0, innerRightPadding)
            assertEquals(0, innerBottomPadding)
            assertEquals(0, outerLeftPadding)
            assertEquals(0, outerTopPadding)
            assertEquals(0, outerRightPadding)
            assertEquals(0, outerBottomPadding)
        }
    }

    @Test
    fun testGetInnerOuterBrickPadding() {
        brickPaddingFactory.getInnerOuterBrickPadding(ONE_DP, TWO_DP).apply {
            assertEquals(1, innerLeftPadding)
            assertEquals(1, innerTopPadding)
            assertEquals(1, innerRightPadding)
            assertEquals(1, innerBottomPadding)
            assertEquals(2, outerLeftPadding)
            assertEquals(2, outerTopPadding)
            assertEquals(2, outerRightPadding)
            assertEquals(2, outerBottomPadding)
        }
    }

    @Test
    fun testGetRectBrickPadding_defaults() {
        brickPaddingFactory.getRectBrickPadding().apply {
            assertEquals(0, innerLeftPadding)
            assertEquals(0, innerTopPadding)
            assertEquals(0, innerRightPadding)
            assertEquals(0, innerBottomPadding)
            assertEquals(0, outerLeftPadding)
            assertEquals(0, outerTopPadding)
            assertEquals(0, outerRightPadding)
            assertEquals(0, outerBottomPadding)
        }
    }

    @Test
    fun testGetRectBrickPaddinh() {
        brickPaddingFactory.getRectBrickPadding(ONE_DP, TWO_DP, THREE_DP, FOUR_DP).apply {
            assertEquals(1, innerLeftPadding)
            assertEquals(2, innerTopPadding)
            assertEquals(3, innerRightPadding)
            assertEquals(4, innerBottomPadding)
            assertEquals(1, outerLeftPadding)
            assertEquals(2, outerTopPadding)
            assertEquals(3, outerRightPadding)
            assertEquals(4, outerBottomPadding)
        }
    }

    @Test
    fun testGetInnerOuterRectBrickPadding_defaults() {
        brickPaddingFactory.getInnerOuterRectBrickPadding().apply {
            assertEquals(0, innerLeftPadding)
            assertEquals(0, innerTopPadding)
            assertEquals(0, innerRightPadding)
            assertEquals(0, innerBottomPadding)
            assertEquals(0, outerLeftPadding)
            assertEquals(0, outerTopPadding)
            assertEquals(0, outerRightPadding)
            assertEquals(0, outerBottomPadding)
        }
    }

    @Test
    fun testGetInnerOuterRectBrickPadding() {
        brickPaddingFactory.getInnerOuterRectBrickPadding(
                ONE_DP,
                TWO_DP,
                THREE_DP,
                FOUR_DP,
                FIVE_DP,
                SIX_DP,
                SEVEN_DP,
                EIGHT_DP
        ).apply {
            assertEquals(1, innerLeftPadding)
            assertEquals(2, innerTopPadding)
            assertEquals(3, innerRightPadding)
            assertEquals(4, innerBottomPadding)
            assertEquals(5, outerLeftPadding)
            assertEquals(6, outerTopPadding)
            assertEquals(7, outerRightPadding)
            assertEquals(8, outerBottomPadding)
        }
    }

    @Test
    fun testViewInsetBrickPadding() {
        brickPaddingFactory.getViewInsetPadding(
                ONE_DP,
                TWO_DP,
                THREE_DP,
                FOUR_DP
        ).apply {
            assertEquals(1, innerLeftPadding)
            assertEquals(2, innerTopPadding)
            assertEquals(3, innerRightPadding)
            assertEquals(4, innerBottomPadding)
            assertEquals(12, outerLeftPadding)
            assertEquals(12, outerTopPadding)
            assertEquals(12, outerRightPadding)
            assertEquals(12, outerBottomPadding)
        }
    }

    @Test
    fun testViewInsetBrickPaddingDefault() {
        brickPaddingFactory.getViewInsetPadding().apply {
            assertEquals(11, innerLeftPadding)
            assertEquals(11, innerTopPadding)
            assertEquals(11, innerRightPadding)
            assertEquals(11, innerBottomPadding)
            assertEquals(12, outerLeftPadding)
            assertEquals(12, outerTopPadding)
            assertEquals(12, outerRightPadding)
            assertEquals(12, outerBottomPadding)
        }
    }

    companion object {
        private const val ONE_DP = 1111
        private const val TWO_DP = 2222
        private const val THREE_DP = 3333
        private const val FOUR_DP = 4444
        private const val FIVE_DP = 5555
        private const val SIX_DP = 6666
        private const val SEVEN_DP = 7777
        private const val EIGHT_DP = 8888

    }
}
