/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.size.BrickSize
import com.wayfair.brickkit.test.R
import com.wayfair.brickkit.viewholder.BrickViewHolder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrickRecyclerAdapterTest {
    private val observer: TestAdapterDataObserver = TestAdapterDataObserver()
    private var bricks = mutableListOf<BaseBrick>()
    private val recyclerView: RecyclerView = mock()
    private val dataManager: BrickDataManager = mock()
    private lateinit var adapter: BrickRecyclerAdapter

    @Before
    fun setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare()
        }
        whenever(dataManager.recyclerViewItems).thenReturn(bricks)
        whenever(recyclerView.isComputingLayout).thenReturn(false)

        adapter = BrickRecyclerAdapter(dataManager, recyclerView).apply {
            registerAdapterDataObserver(observer)
        }
    }

    @Test
    fun testGetBrickDataManager() {
        assertNotNull(adapter.brickDataManager)
    }

    @Test
    fun testSafeNotifyDataSetChanged() {
        adapter.safeNotifyDataSetChanged()

        assertTrue(observer.isChanged)
    }

    @Test
    fun testSafeNotifyDataSetChangedComputingLayout() {
        whenever(recyclerView.isComputingLayout).thenReturn(true)

        adapter.safeNotifyDataSetChanged()

        assertFalse(observer.isChanged)
    }

    @Test
    fun testSafeNotifyItemChanged() {
        adapter.safeNotifyItemChanged(POSITION)

        assertEquals(POSITION, observer.itemRangeChangedPositionStart)
        assertEquals(1, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemChanged_negativePosition() {
        adapter.safeNotifyItemChanged(-1)

        assertEquals(-1, observer.itemRangeChangedPositionStart)
        assertEquals(-1, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemChangedComputingLayout() {
        whenever(recyclerView.isComputingLayout).thenReturn(true)

        adapter.safeNotifyItemChanged(POSITION)

        assertEquals(-1, observer.itemRangeChangedPositionStart)
        assertEquals(-1, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemChangedWithPayload() {
        adapter.safeNotifyItemChanged(POSITION, PAYLOAD)

        assertEquals(POSITION, observer.itemRangeChangedPositionStart)
        assertEquals(1, observer.itemRangeChangedItemCount)
        assertNotNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemChangedWithPayload_negativePosition() {
        adapter.safeNotifyItemChanged(-1, PAYLOAD)

        assertEquals(-1, observer.itemRangeChangedPositionStart)
        assertEquals(-1, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemChangedWithPayloadComputingLayout() {
        whenever(recyclerView.isComputingLayout).thenReturn(true)

        adapter.safeNotifyItemChanged(POSITION, PAYLOAD)

        assertEquals(-1, observer.itemRangeChangedPositionStart)
        assertEquals(-1, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemInserted() {
        adapter.safeNotifyItemInserted(POSITION)

        assertEquals(POSITION, observer.itemRangeInsertedPositionStart)
        assertEquals(1, observer.itemRangeInsertedItemCount)
    }

    @Test
    fun testSafeNotifyItemInserted_negativePosition() {
        whenever(recyclerView.isComputingLayout).thenReturn(true)

        adapter.safeNotifyItemInserted(-1)

        assertEquals(-1, observer.itemRangeInsertedPositionStart)
        assertEquals(-1, observer.itemRangeInsertedItemCount)
    }

    @Test
    fun testSafeNotifyItemInsertedComputingLayout() {
        whenever(recyclerView.isComputingLayout).thenReturn(true)

        adapter.safeNotifyItemInserted(POSITION)

        assertEquals(-1, observer.itemRangeInsertedPositionStart)
        assertEquals(-1, observer.itemRangeInsertedItemCount)
    }

    @Test
    fun testSafeNotifyItemRangeInserted() {
        adapter.safeNotifyItemRangeInserted(POSITION, COUNT)

        assertEquals(POSITION, observer.itemRangeInsertedPositionStart)
        assertEquals(COUNT, observer.itemRangeInsertedItemCount)
    }

    @Test
    fun testSafeNotifyItemRangeInserted_negativePosition() {
        adapter.safeNotifyItemRangeInserted(-1, COUNT)

        assertEquals(-1, observer.itemRangeInsertedPositionStart)
        assertEquals(-1, observer.itemRangeInsertedItemCount)
    }

    @Test
    fun testSafeNotifyItemRangeInserted_negativeCount() {
        adapter.safeNotifyItemRangeInserted(POSITION, -1)

        assertEquals(-1, observer.itemRangeInsertedPositionStart)
        assertEquals(-1, observer.itemRangeInsertedItemCount)
    }

    @Test
    fun testSafeNotifyItemRangeInsertedComputingLayout() {
        whenever(recyclerView.isComputingLayout).thenReturn(true)

        adapter.safeNotifyItemRangeInserted(POSITION, COUNT)

        assertEquals(-1, observer.itemRangeInsertedPositionStart)
        assertEquals(-1, observer.itemRangeInsertedItemCount)
    }

    @Test
    fun testSafeNotifyItemMoved() {
        adapter.safeNotifyItemMoved(POSITION, TO_POSITION)

        assertEquals(POSITION, observer.itemRangeMovedFromPosition)
        assertEquals(TO_POSITION, observer.itemRangeMovedToPosition)
        assertEquals(1, observer.itemRangeMovedItemCount)
    }

    @Test
    fun testSafeNotifyItemMoved_negativeFromPosition() {
        adapter.safeNotifyItemMoved(-1, TO_POSITION)

        assertEquals(-1, observer.itemRangeMovedFromPosition)
        assertEquals(-1, observer.itemRangeMovedToPosition)
        assertEquals(-1, observer.itemRangeMovedItemCount)
    }

    @Test
    fun testSafeNotifyItemMoved_negativeToPosition() {
        adapter.safeNotifyItemMoved(POSITION, -1)

        assertEquals(-1, observer.itemRangeMovedFromPosition)
        assertEquals(-1, observer.itemRangeMovedToPosition)
        assertEquals(-1, observer.itemRangeMovedItemCount)
    }

    @Test
    fun testSafeNotifyItemMovedComputingLayout() {
        whenever(recyclerView.isComputingLayout).thenReturn(true)

        adapter.safeNotifyItemMoved(POSITION, TO_POSITION)

        assertEquals(-1, observer.itemRangeMovedFromPosition)
        assertEquals(-1, observer.itemRangeMovedToPosition)
        assertEquals(-1, observer.itemRangeMovedItemCount)
    }

    @Test
    fun testSafeNotifyItemRangeChangedWithPayload() {
        adapter.safeNotifyItemRangeChanged(POSITION, COUNT, PAYLOAD)

        assertEquals(POSITION, observer.itemRangeChangedPositionStart)
        assertEquals(COUNT, observer.itemRangeChangedItemCount)
        assertEquals(PAYLOAD, observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemRangeChangedWithPayload_negativePosition() {
        adapter.safeNotifyItemRangeChanged(-1, COUNT, PAYLOAD)

        assertEquals(-1, observer.itemRangeChangedPositionStart)
        assertEquals(-1, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemRangeChangedWithPayload_negativeCount() {
        adapter.safeNotifyItemRangeChanged(POSITION, -1, PAYLOAD)

        assertEquals(-1, observer.itemRangeChangedPositionStart)
        assertEquals(-1, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemRangeChangedWithPayloadComputingLayout() {
        whenever(recyclerView.isComputingLayout).thenReturn(true)

        adapter.safeNotifyItemRangeChanged(POSITION, COUNT, PAYLOAD)

        assertEquals(-1, observer.itemRangeChangedPositionStart)
        assertEquals(-1, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemRangeChanged() {
        adapter.safeNotifyItemRangeChanged(POSITION, COUNT)

        assertEquals(POSITION, observer.itemRangeChangedPositionStart)
        assertEquals(COUNT, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemRangeChanged_negativePosition() {
        adapter.safeNotifyItemRangeChanged(-1, COUNT)

        assertEquals(-1, observer.itemRangeChangedPositionStart)
        assertEquals(-1, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemRangeChanged_negativeCount() {
        adapter.safeNotifyItemRangeChanged(POSITION, -1)

        assertEquals(-1, observer.itemRangeChangedPositionStart)
        assertEquals(-1, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemRangeChangedComputingLayout() {
        whenever(recyclerView.isComputingLayout).thenReturn(true)

        adapter.safeNotifyItemRangeChanged(POSITION, COUNT)

        assertEquals(-1, observer.itemRangeChangedPositionStart)
        assertEquals(-1, observer.itemRangeChangedItemCount)
        assertNull(observer.itemRangeChangedPayload)
    }

    @Test
    fun testSafeNotifyItemRemoved() {
        adapter.safeNotifyItemRemoved(POSITION)

        assertEquals(POSITION, observer.itemRangeRemovedPositionStart)
        assertEquals(1, observer.itemRangeRemovedItemCount)
    }

    @Test
    fun testSafeNotifyItemRemoved_negativePosition() {
        adapter.safeNotifyItemRemoved(-1)

        assertEquals(-1, observer.itemRangeRemovedPositionStart)
        assertEquals(-1, observer.itemRangeRemovedItemCount)
    }

    @Test
    fun testSafeNotifyItemRemovedComputingLayout() {
        whenever(recyclerView.isComputingLayout).thenReturn(true)

        adapter.safeNotifyItemRemoved(POSITION)

        assertEquals(-1, observer.itemRangeRemovedPositionStart)
        assertEquals(-1, observer.itemRangeRemovedItemCount)
    }

    @Test
    fun testSafeNotifyItemRangeRemoved() {
        adapter.safeNotifyItemRangeRemoved(POSITION, COUNT)

        assertEquals(POSITION, observer.itemRangeRemovedPositionStart)
        assertEquals(COUNT, observer.itemRangeRemovedItemCount)
    }

    @Test
    fun testSafeNotifyItemRangeRemoved_negativePosition() {
        adapter.safeNotifyItemRangeRemoved(-1, COUNT)

        assertEquals(-1, observer.itemRangeRemovedPositionStart)
        assertEquals(-1, observer.itemRangeRemovedItemCount)
    }

    @Test
    fun testSafeNotifyItemRangeRemoved_negativeCount() {
        adapter.safeNotifyItemRangeRemoved(POSITION, -1)

        assertEquals(-1, observer.itemRangeRemovedPositionStart)
        assertEquals(-1, observer.itemRangeRemovedItemCount)
    }

    @Test
    fun testSafeNotifyItemRangeRemovedComputingLayout() {
        whenever(recyclerView.isComputingLayout).thenReturn(true)

        adapter.safeNotifyItemRangeRemoved(POSITION, COUNT)

        assertEquals(-1, observer.itemRangeRemovedPositionStart)
        assertEquals(-1, observer.itemRangeRemovedItemCount)
    }

    @Test
    fun testOnCreateViewHolder() {
        val brick = mock<BaseBrick>()
        whenever(brick.layout).thenReturn(R.layout.text_brick_vm)
        whenever(dataManager.recyclerViewItems).thenReturn(listOf(brick))

        adapter.onCreateViewHolder(LinearLayout(ApplicationProvider.getApplicationContext()), brick.layout)

        verify(brick).createViewHolder(any())
    }

    @Test
    fun testOnBindViewHolderNullBrick() {
        val listener = mock<OnReachedItemAtPosition>()
        adapter.setOnReachedItemAtPosition(listener)
        whenever(dataManager.recyclerViewItems).thenReturn(listOf())

        val holder = mock<BrickViewHolder>()

        adapter.onBindViewHolder(holder, 0)

        verify(listener, never()).bindingItemAtPosition(0)
    }

    @Test
    fun testOnBindViewHolderNullBindListenerWhenDataIsReady() {
        val brick = mock<BaseBrick>()
        whenever(dataManager.recyclerViewItems).thenReturn(listOf(brick))
        whenever(brick.isDataReady).thenReturn(true)

        val holder = BrickViewHolder(mock())

        adapter.onBindViewHolder(holder, 0)

        verify(brick).onBindData(holder)
    }

    @Test
    fun testOnBindViewHolderNullBindListenerWhenDataIsNotReady() {
        val brick = mock<BaseBrick>()
        whenever(dataManager.recyclerViewItems).thenReturn(listOf(brick))
        whenever(brick.isDataReady).thenReturn(false)

        val holder = BrickViewHolder(mock())

        adapter.onBindViewHolder(holder, 0)

        verify(brick, never()).onBindData(holder)
    }

    @Test
    fun testOnBindViewHolderWhenDataIsNotReady() {
        val brick = mock<BaseBrick>()
        val listener = mock<OnReachedItemAtPosition>()

        adapter.setOnReachedItemAtPosition(listener)

        whenever(dataManager.recyclerViewItems).thenReturn(listOf(brick))
        whenever(brick.isDataReady).thenReturn(false)

        val holder = BrickViewHolder(mock())

        adapter.onBindViewHolder(holder, 0)

        verify(brick, never()).onBindData(holder)
        verify(listener).bindingItemAtPosition(0)
    }

    @Test
    fun testOnBindViewHolderWhenDataIsReady() {
        val brick = mock<BaseBrick>()
        val listener = mock<OnReachedItemAtPosition>()

        adapter.setOnReachedItemAtPosition(listener)

        whenever(dataManager.recyclerViewItems).thenReturn(listOf(brick))
        whenever(brick.isDataReady).thenReturn(true)

        val holder = BrickViewHolder(mock())
        adapter.onBindViewHolder(holder, 0)

        verify(brick).onBindData(holder)
        verify(listener).bindingItemAtPosition(0)
    }

    @Test
    fun testOnBindViewHolderWhenDataIsReady_staggeredGrid_fullWidth() {
        val spanSize = mock<BrickSize>()
        whenever(spanSize.getSpans(any())).thenReturn(BrickDataManager.SPAN_COUNT)

        val brick = mock<BaseBrick>()
        whenever(brick.spanSize).thenReturn(spanSize)

        val listener = mock<OnReachedItemAtPosition>()
        adapter.setOnReachedItemAtPosition(listener)

        whenever(dataManager.recyclerViewItems).thenReturn(listOf(brick))
        whenever(brick.isDataReady).thenReturn(true)

        val layoutParams = mock<StaggeredGridLayoutManager.LayoutParams>()
        val holder = BrickViewHolder(View(ApplicationProvider.getApplicationContext()).apply { this.layoutParams = layoutParams })
        adapter.onBindViewHolder(holder, 0)

        verify(brick).onBindData(holder)
        verify(listener).bindingItemAtPosition(0)
        verify(layoutParams).isFullSpan = true
    }

    @Test
    fun testOnBindViewHolderWhenDataIsReady_staggeredGrid_notFullWidth() {
        val spanSize = mock<BrickSize>()
        whenever(spanSize.getSpans(any())).thenReturn(BrickDataManager.SPAN_COUNT - 1)

        val brick = mock<BaseBrick>()
        whenever(brick.spanSize).thenReturn(spanSize)

        val listener = mock<OnReachedItemAtPosition>()
        adapter.setOnReachedItemAtPosition(listener)

        whenever(dataManager.recyclerViewItems).thenReturn(listOf(brick))
        whenever(brick.isDataReady).thenReturn(true)

        val layoutParams = mock<StaggeredGridLayoutManager.LayoutParams>()
        val holder = BrickViewHolder(View(ApplicationProvider.getApplicationContext()).apply { this.layoutParams = layoutParams })
        adapter.onBindViewHolder(holder, 0)

        verify(brick).onBindData(holder)
        verify(listener).bindingItemAtPosition(0)
        verify(layoutParams).isFullSpan = false
    }

    @Test
    fun testOnBindViewHolderWhenDataIsReady_notStaggeredGrid() {
        val spanSize = mock<BrickSize>()
        whenever(spanSize.getSpans(any())).thenReturn(BrickDataManager.SPAN_COUNT)

        val brick = mock<BaseBrick>()
        whenever(brick.spanSize).thenReturn(spanSize)

        val listener = mock<OnReachedItemAtPosition>()
        adapter.setOnReachedItemAtPosition(listener)

        whenever(dataManager.recyclerViewItems).thenReturn(listOf(brick))
        whenever(brick.isDataReady).thenReturn(true)

        val holder = BrickViewHolder(
            View(ApplicationProvider.getApplicationContext()).apply {
                this.layoutParams = mock<GridLayoutManager.LayoutParams>()
            }
        )

        adapter.onBindViewHolder(holder, 0)

        verify(brick).onBindData(holder)
        verify(listener).bindingItemAtPosition(0)
    }

    @Test
    fun testOnViewAttachedToWindow() {
        val brickViewHolder = spy(BrickViewHolder(mock()))

        adapter.onViewAttachedToWindow(brickViewHolder)

        verify(brickViewHolder).onViewAttachedToWindow()
    }

    @Test
    fun testOnViewDetachedFromWindow() {
        val brickViewHolder = spy(BrickViewHolder(mock()))

        adapter.onViewDetachedFromWindow(brickViewHolder)

        verify(brickViewHolder).releaseViewsOnDetach()
    }

    @Test
    fun testGetItemCount() {
        repeat(BRICK_COUNT) { bricks.add(mock()) }

        assertEquals(BRICK_COUNT, adapter.itemCount)
    }

    @Test
    fun testGetItemViewTypeWhenDataIsReady() {
        val brick = mock<BaseBrick>()
        whenever(brick.layout).thenReturn(LAYOUT)
        whenever(brick.isDataReady).thenReturn(true)
        whenever(dataManager.recyclerViewItems).thenReturn(listOf(brick))

        assertEquals(LAYOUT, adapter.getItemViewType(0))
    }

    @Test
    fun testGetItemViewTypeWhenDataIsNotReady() {
        val brick = mock<BaseBrick>()
        whenever(brick.placeholderLayout).thenReturn(PLACEHOLDER_LAYOUT)
        whenever(brick.isDataReady).thenReturn(false)
        whenever(dataManager.recyclerViewItems).thenReturn(listOf(brick))

        assertEquals(PLACEHOLDER_LAYOUT, adapter.getItemViewType(0))
    }

    @Test
    fun testGetItemViewTypeInvalidPosition() {
        whenever(dataManager.dataManagerItems).thenReturn(listOf())

        assertEquals(0, adapter.getItemViewType(0))
    }

    companion object {
        private const val POSITION = 1
        private const val TO_POSITION = 2
        private const val COUNT = 3
        private val PAYLOAD = Any()
        private const val BRICK_COUNT = 3
        private const val LAYOUT = 7
        private const val PLACEHOLDER_LAYOUT = 8
    }

    private class TestAdapterDataObserver : AdapterDataObserver() {
        var isChanged = false
        var itemRangeChangedPositionStart = -1
        var itemRangeChangedItemCount = -1
        var itemRangeChangedPayload: Any? = null
        var itemRangeInsertedPositionStart = -1
        var itemRangeInsertedItemCount = -1
        var itemRangeRemovedPositionStart = -1
        var itemRangeRemovedItemCount = -1
        var itemRangeMovedFromPosition = -1
        var itemRangeMovedToPosition = -1
        var itemRangeMovedItemCount = -1

        override fun onChanged() {
            isChanged = true
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            itemRangeChangedPositionStart = positionStart
            itemRangeChangedItemCount = itemCount
            itemRangeChangedPayload = payload
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            itemRangeChangedPositionStart = positionStart
            itemRangeChangedItemCount = itemCount
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            itemRangeInsertedPositionStart = positionStart
            itemRangeInsertedItemCount = itemCount
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            itemRangeRemovedPositionStart = positionStart
            itemRangeRemovedItemCount = itemCount
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            itemRangeMovedFromPosition = fromPosition
            itemRangeMovedToPosition = toPosition
            itemRangeMovedItemCount = itemCount
        }
    }
}
