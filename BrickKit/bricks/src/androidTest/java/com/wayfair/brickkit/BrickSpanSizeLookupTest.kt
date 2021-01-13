/**
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.size.BrickSize
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import java.util.LinkedList

@RunWith(AndroidJUnit4::class)
class BrickSpanSizeLookupTest {

    private var manager: BrickDataManager = mock()
    private lateinit var bricks: LinkedList<BaseBrick>
    private lateinit var brickSpanSizeLookup: BrickSpanSizeLookup

    @Before
    fun setup() {
        bricks = LinkedList()
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
