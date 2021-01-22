/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.brick

import android.view.View
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wayfair.brickkit.BrickDataManager
import com.wayfair.brickkit.padding.BrickPadding
import com.wayfair.brickkit.size.BrickSize
import com.wayfair.brickkit.viewholder.BrickViewHolder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BaseBrickTest {
    private val brickSize: BrickSize = mock()
    private val brickDataManager: BrickDataManager = mock()
    private val brick: BaseBrick = TestBaseBrick(brickSize)

    @Test
    fun testBrickSizeBrickPaddingConstructor() {
        val padding = mock<BrickPadding>()
        whenever(padding.innerBottomPadding).thenReturn(PADDING)

        val brick = TestBaseBrick(brickSize, padding)
        assertEquals(brickSize, brick.spanSize)
        assertEquals(PADDING, brick.padding.innerBottomPadding)
    }

    @Test
    fun testHidden() {
        assertFalse(brick.isHidden)
        brick.isHidden = true
        assertTrue(brick.isHidden)
    }

    @Test
    fun testInFirstRow() {
        assertFalse(brick.isInFirstRow)
        brick.isInFirstRow = true
        assertTrue(brick.isInFirstRow)
    }

    @Test
    fun testInLastRow() {
        assertFalse(brick.isInLastRow)
        brick.isInLastRow = true
        assertTrue(brick.isInLastRow)
    }

    @Test
    fun testOnLeftWall() {
        assertFalse(brick.isOnLeftWall)
        brick.isOnLeftWall = true
        assertTrue(brick.isOnLeftWall)
    }

    @Test
    fun testOnRightWall() {
        assertFalse(brick.isOnRightWall)
        brick.isOnRightWall = true
        assertTrue(brick.isOnRightWall)
    }

    @Test
    fun testRefreshItem() {
        brick.refreshItem()
        brick.setDataManager(brickDataManager)
        brick.refreshItem()

        verify(brickDataManager).refreshItem(brick)
    }

    @Test
    fun testAddLastTo() {
        brick.addLastTo(brickDataManager)

        verify(brickDataManager).addLast(brick)
    }

    @Test
    fun testAddFirstTo() {
        brick.addFirstTo(brickDataManager)

        verify(brickDataManager).addFirst(brick)
    }

    private class TestBaseBrick : BaseBrick {
        constructor(spanSize: BrickSize, padding: BrickPadding) : super(spanSize, padding)
        constructor(spanSize: BrickSize) : super(spanSize)

        override fun onBindData(holder: BrickViewHolder) {}
        override fun getLayout(): Int = 0
        override fun createViewHolder(itemView: View): BrickViewHolder = mock()
    }

    companion object {
        private const val PADDING = 3
    }
}
