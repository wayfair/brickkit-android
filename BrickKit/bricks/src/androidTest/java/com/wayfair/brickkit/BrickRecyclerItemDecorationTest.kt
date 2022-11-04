/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.padding.BrickPadding
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BrickRecyclerItemDecorationTest {
    private val parent: RecyclerView = mock()
    private val outRect: Rect = Rect()
    private val view: View = mock()
    private val state: RecyclerView.State = mock()
    private val bricks = mutableListOf<BaseBrick>()
    private val brick: BaseBrick = mock()
    private lateinit var itemDecoration: BrickRecyclerItemDecoration

    @Before
    fun setup() {
        whenever(parent.getChildAdapterPosition(view)).thenReturn(0)

        val brickDataManager = mock<BrickDataManager>()
        whenever(brickDataManager.recyclerViewItems).thenReturn(bricks)

        val adapter = mock<BrickRecyclerAdapter>()
        whenever(adapter.brickDataManager).thenReturn(brickDataManager)
        whenever(parent.adapter).thenReturn(adapter)

        itemDecoration = BrickRecyclerItemDecoration(brickDataManager)

        val brickPadding = mock<BrickPadding>()
        whenever(brickPadding.innerLeftPadding).thenReturn(INNER_LEFT)
        whenever(brickPadding.innerTopPadding).thenReturn(INNER_TOP)
        whenever(brickPadding.innerRightPadding).thenReturn(INNER_RIGHT)
        whenever(brickPadding.innerBottomPadding).thenReturn(INNER_BOTTOM)
        whenever(brickPadding.outerLeftPadding).thenReturn(OUTER_LEFT)
        whenever(brickPadding.outerTopPadding).thenReturn(OUTER_TOP)
        whenever(brickPadding.outerRightPadding).thenReturn(OUTER_RIGHT)
        whenever(brickPadding.outerBottomPadding).thenReturn(OUTER_BOTTOM)
        whenever(brick.padding).thenReturn(brickPadding)
        whenever(parent.getChildAdapterPosition(view)).thenReturn(0)
        bricks.add(brick)
    }

    @Test
    fun testInvalidNullItem() {
        bricks.clear()

        itemDecoration.getItemOffsets(outRect, view, parent, state)

        assertEquals(0, outRect.left.toLong())
        assertEquals(0, outRect.top.toLong())
        assertEquals(0, outRect.right.toLong())
        assertEquals(0, outRect.bottom.toLong())
    }

    @Test
    fun testInvalidWrongDataManager() {
        val adapter = mock<BrickRecyclerAdapter>()
        whenever(parent.adapter).thenReturn(adapter)
        whenever(adapter.brickDataManager).thenReturn(mock())

        itemDecoration.getItemOffsets(outRect, view, parent, state)

        assertEquals(0, outRect.left.toLong())
        assertEquals(0, outRect.top.toLong())
        assertEquals(0, outRect.right.toLong())
        assertEquals(0, outRect.bottom.toLong())
    }

    @Test
    fun testInvalidWrongAdapterType() {
        whenever(parent.adapter).thenReturn(mock())

        itemDecoration.getItemOffsets(outRect, view, parent, state)

        assertEquals(0, outRect.left.toLong())
        assertEquals(0, outRect.top.toLong())
        assertEquals(0, outRect.right.toLong())
        assertEquals(0, outRect.bottom.toLong())
    }

    @Test
    fun testAllOuter() {
        whenever(brick.isInFirstRow).thenReturn(true)
        whenever(brick.isInLastRow).thenReturn(true)
        whenever(brick.isOnLeftWall).thenReturn(true)
        whenever(brick.isOnRightWall).thenReturn(true)

        itemDecoration.getItemOffsets(outRect, view, parent, state)

        assertEquals(OUTER_LEFT.toLong(), outRect.left.toLong())
        assertEquals(OUTER_TOP.toLong(), outRect.top.toLong())
        assertEquals(OUTER_RIGHT.toLong(), outRect.right.toLong())
        assertEquals(OUTER_BOTTOM.toLong(), outRect.bottom.toLong())
    }

    @Test
    fun testAllInner() {
        whenever(brick.isInFirstRow).thenReturn(false)
        whenever(brick.isInLastRow).thenReturn(false)
        whenever(brick.isOnLeftWall).thenReturn(false)
        whenever(brick.isOnRightWall).thenReturn(false)

        itemDecoration.getItemOffsets(outRect, view, parent, state)

        assertEquals(INNER_LEFT.toLong(), outRect.left.toLong())
        assertEquals(INNER_TOP.toLong(), outRect.top.toLong())
        assertEquals(INNER_RIGHT.toLong(), outRect.right.toLong())
        assertEquals(INNER_BOTTOM.toLong(), outRect.bottom.toLong())
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
