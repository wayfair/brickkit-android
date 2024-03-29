/**
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.size.BrickSize
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrickSpanSizeLookupTest {

    private var manager: BrickDataManager = mock()
    private lateinit var bricks: MutableList<BaseBrick>
    private lateinit var brickSpanSizeLookup: BrickSpanSizeLookup

    @Before
    fun setup() {
        bricks = mutableListOf()
        whenever(manager.recyclerViewItems).thenReturn(bricks)

        brickSpanSizeLookup = BrickSpanSizeLookup(ApplicationProvider.getApplicationContext(), manager)
    }

    @Test
    fun testDefaultSpanSize_nullItem() {
        assertEquals(DEFAULT_SPANS, brickSpanSizeLookup.getSpanSize(0))
    }

    @Test
    fun testDefaultSpanSize_nullSpanSize() {
        val brick = mock<BaseBrick>()
        whenever(brick.spanSize).thenReturn(null)

        bricks.add(brick)

        assertEquals(DEFAULT_SPANS, brickSpanSizeLookup.getSpanSize(0))
    }

    @Test
    fun testBrickSpanSize() {
        val brickSize = mock<BrickSize>()
        whenever(brickSize.getSpans(any())).thenReturn(SPANS)
        val brick = mock<BaseBrick>()
        whenever(brick.spanSize).thenReturn(brickSize)

        bricks.add(brick)

        assertEquals(SPANS, brickSpanSizeLookup.getSpanSize(0))
    }

    companion object {
        private const val DEFAULT_SPANS = 1
        private const val SPANS = 5
    }
}
