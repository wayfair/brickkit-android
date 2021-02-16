/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.padding.ZeroBrickPadding
import com.wayfair.brickkit.size.FullWidthBrickSize
import com.wayfair.brickkit.viewholder.BrickViewHolder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrickDataManagerTest {
    private val observer: RecyclerView.AdapterDataObserver = mock()
    private val dataSetChangedListener: DataSetChangedListener = mock()
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: BrickDataManager

    @Before
    fun setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare()
        }

        recyclerView = spy(RecyclerView(ApplicationProvider.getApplicationContext()))
        manager = BrickDataManager().apply {
            setRecyclerView(recyclerView)

            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }

            setDataSetChangedListener(dataSetChangedListener)
            getRecyclerView()?.adapter?.registerAdapterDataObserver(observer)
        }
    }

    @Test
    fun testSetOnReachedItemAtPosition() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        val listener = mock<OnReachedItemAtPosition>()

        manager.setOnReachedItemAtPosition(listener)

        manager.setRecyclerView(recyclerView)

        manager.setOnReachedItemAtPosition(listener)
    }

    @Test
    fun testAddLastVisible() {
        manager.addLast(TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)

        verify(observer).onItemRangeInserted(4, 1)
        verify(observer).onItemRangeChanged(4, 0, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testUpdateBricks() {
        val bricks = mutableListOf<BaseBrick>()
        bricks.addAll(manager.dataManagerItems)
        bricks.add(TestBrick())
        bricks.add(1, TestBrick())
        bricks.add(3, TestBrick())

        manager.updateBricks(bricks)

        assertEquals(7, manager.recyclerViewItems.size)
        assertEquals(7, manager.dataManagerItems.size)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testUpdateBricks_nullRecyclerView() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        val bricks = mutableListOf<BaseBrick>()
        bricks.addAll(manager.dataManagerItems)
        bricks.add(TestBrick())
        bricks.add(HiddenTestBrick())
        bricks.add(1, TestBrick())
        bricks.add(3, TestBrick())

        manager.updateBricks(bricks)

        assertEquals(7, manager.recyclerViewItems.size)
        assertEquals(8, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener)
    }

    @Test
    fun testAddLastHidden() {
        manager.addLast(HiddenTestBrick())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)

        verifyZeroInteractions(observer)
        verify(dataSetChangedListener, never()).onDataSetChanged()
    }

    @Test
    fun testAddLast_null() {
        val baseBrick: BaseBrick? = null
        manager.addLast(baseBrick)

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)

        verifyZeroInteractions(observer, dataSetChangedListener)
    }

    @Test
    fun testAddLastCollection() {
        manager.addLast(
            listOf(
                TestBrick(),
                HiddenTestBrick(),
                TestBrick(),
                HiddenTestBrick(),
                TestBrick()
            )
        )

        assertEquals(7, manager.recyclerViewItems.size)
        assertEquals(9, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(4, 3)
        verify(observer).onItemRangeChanged(4, 0, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddLastCollection_nullAdapter() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        manager.addLast(
            listOf(
                TestBrick(),
                HiddenTestBrick(),
                TestBrick(),
                HiddenTestBrick(),
                TestBrick()
            )
        )

        assertEquals(7, manager.recyclerViewItems.size)
        assertEquals(9, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testAddLastCollection_emptyCollection() {
        manager.addLast(listOf())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testAddLastCollection_null() {
        val bricks: List<BaseBrick>? = null
        manager.addLast(bricks)

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)

        verifyZeroInteractions(observer, dataSetChangedListener)
    }

    @Test
    fun testAddLastCollectionAllHidden() {
        manager.addLast(
            listOf(
                HiddenTestBrick(),
                HiddenTestBrick(),
                HiddenTestBrick(),
                HiddenTestBrick(),
                HiddenTestBrick()
            )
        )

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(9, manager.dataManagerItems.size)
        verifyZeroInteractions(observer)
        verify(dataSetChangedListener, never()).onDataSetChanged()
    }

    @Test
    fun testAddFirst_null() {
        val baseBrick: BaseBrick? = null
        manager.addFirst(baseBrick)

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)

        verifyZeroInteractions(observer, dataSetChangedListener)
    }

    @Test
    fun testAddFirstVisible() {
        val newBrick = TestBrick()

        manager.addFirst(newBrick)

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(0, 1)
        verify(observer).onItemRangeChanged(1, 4, null)
        assertEquals(newBrick, manager.dataManagerItems[0])
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddFirstVisible_nullAdapter() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        val newBrick = TestBrick()

        manager.addFirst(newBrick)

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        assertEquals(newBrick, manager.dataManagerItems[0])
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testAddFirstVisibleHorizontal() {
        val dataSetChangedListener = mock<DataSetChangedListener>()
        val observer = mock<RecyclerView.AdapterDataObserver>()

        val manager = BrickDataManager().apply {
            setHorizontalRecyclerView(RecyclerView(ApplicationProvider.getApplicationContext()))
            setDataSetChangedListener(dataSetChangedListener)

            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }

            getRecyclerView()?.adapter?.registerAdapterDataObserver(observer)
        }

        val newBrick = TestBrick()
        manager.addFirst(newBrick)

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(0, 1)
        verify(observer).onItemRangeChanged(1, 4, null)
        assertEquals(newBrick, manager.dataManagerItems[0])
        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged()
    }

    @Test
    fun testAddFirstHidden() {
        manager.addFirst(HiddenTestBrick())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verifyZeroInteractions(observer)
        verify(dataSetChangedListener, never()).onDataSetChanged()
    }

    @Test
    fun testAddBeforeFirstItem() {
        manager.addBeforeItem(manager.recyclerViewItems[0], TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(0, 1)
        verify(observer).onItemRangeChanged(0, 5, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddBeforeFirstItem_nullAdapter() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        manager.addBeforeItem(manager.recyclerViewItems[0], TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testAddBeforeUnexpectedItem() {
        manager.addBeforeItem(TestBrick(), TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(0, 1)
        verify(observer).onItemRangeChanged(0, 5, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddBeforeWithHiddenItem() {
        manager.addBeforeItem(manager.recyclerViewItems[1], HiddenTestBrick())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verifyZeroInteractions(observer)
        verify(dataSetChangedListener, never()).onDataSetChanged()
    }

    @Test
    fun testAddBeforeHiddenItemsAtEnd() {
        val lastHiddenBrick = HiddenTestBrick()

        manager.addLast(HiddenTestBrick())
        manager.addLast(lastHiddenBrick)
        manager.addBeforeItem(lastHiddenBrick, TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(7, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(4, 1)
        verify(observer).onItemRangeChanged(4, 1, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddBeforeHiddenItemsInMiddle() {
        manager.addAfterItem(manager.recyclerViewItems[0], HiddenTestBrick())
        manager.addAfterItem(manager.recyclerViewItems[0], HiddenTestBrick())
        manager.addAfterItem(manager.recyclerViewItems[0], HiddenTestBrick())
        manager.addBeforeItem(manager.dataManagerItems[3], TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(8, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(1, 1)
        verify(observer).onItemRangeChanged(1, 4, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddBeforeHiddenItemsAtBeginning() {
        manager.addFirst(HiddenTestBrick())
        manager.addFirst(HiddenTestBrick())
        manager.addFirst(HiddenTestBrick())
        manager.addBeforeItem(manager.recyclerViewItems[0], TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(8, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(0, 1)
        verify(observer).onItemRangeChanged(0, 5, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddAfterWithHiddenItem() {
        manager.addAfterItem(manager.recyclerViewItems[1], HiddenTestBrick())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verifyZeroInteractions(observer)
        verify(dataSetChangedListener, never()).onDataSetChanged()
    }

    @Test
    fun testAddAfterHiddenItemsAtEnd() {
        val lastHiddenBrick = HiddenTestBrick()
        manager.addLast(HiddenTestBrick())
        manager.addLast(lastHiddenBrick)
        manager.addAfterItem(lastHiddenBrick, TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(7, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(4, 1)
        verify(observer).onItemRangeChanged(4, 1, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddAfterHiddenItemsInMiddle() {
        manager.addAfterItem(manager.recyclerViewItems[0], HiddenTestBrick())
        manager.addAfterItem(manager.recyclerViewItems[0], HiddenTestBrick())
        manager.addAfterItem(manager.recyclerViewItems[0], HiddenTestBrick())
        manager.addAfterItem(manager.dataManagerItems[3], TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(8, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(1, 1)
        verify(observer).onItemRangeChanged(1, 4, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddAfterHiddenItemsAtBeginning() {
        manager.addFirst(HiddenTestBrick())
        manager.addFirst(HiddenTestBrick())
        manager.addFirst(HiddenTestBrick())
        manager.addBeforeItem(manager.recyclerViewItems[0], TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(8, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(0, 1)
        verify(observer).onItemRangeChanged(0, 5, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddBeforeLastItem() {
        manager.addBeforeItem(manager.recyclerViewItems[3], TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(3, 1)
        verify(observer).onItemRangeChanged(3, 2, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddBeforeInvalidItem() {
        manager.addBeforeItem(TestBrick(), TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(0, 1)
        verify(observer).onItemRangeChanged(0, 5, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddAfterFirstItem() {
        manager.addAfterItem(manager.recyclerViewItems[0], TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(1, 1)
        verify(observer).onItemRangeChanged(1, 4, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddAfterFirstItem_nullAdapter() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        manager.addAfterItem(manager.recyclerViewItems[0], TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testAddAfterLastItem() {
        manager.addAfterItem(manager.recyclerViewItems[3], TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(4, 1)
        verify(observer).onItemRangeChanged(4, 1, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddAfterLastHiddenItem() {
        manager.addLast(HiddenTestBrick())
        manager.addAfterItem(manager.recyclerViewItems[3], TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(6, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(4, 1)
        verify(observer).onItemRangeChanged(4, 1, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddAfterInvalidItem() {
        manager.addAfterItem(TestBrick(), TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(4, 1)
        verify(observer).onItemRangeChanged(4, 1, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddItemsBeforeLastItem() {
        manager.addBeforeItem(
            manager.recyclerViewItems[3],
            listOf(
                TestBrick(),
                TestBrick(),
                TestBrick()
            )
        )

        assertEquals(7, manager.recyclerViewItems.size)
        assertEquals(7, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(3, 3)
        verify(observer).onItemRangeChanged(3, 1, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddItemsBeforeLastItem_nullAdapter() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        manager.addBeforeItem(
            manager.recyclerViewItems[3],
            listOf(
                TestBrick(),
                TestBrick(),
                TestBrick()
            )
        )

        assertEquals(7, manager.recyclerViewItems.size)
        assertEquals(7, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testAddItemsBeforeLastItem_emptyList() {
        manager.addBeforeItem(manager.recyclerViewItems[3], listOf())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testAddItemsWithOneHiddenBeforeLastItem() {
        manager.addBeforeItem(
            manager.recyclerViewItems[3],
            listOf(
                TestBrick(),
                HiddenTestBrick(),
                TestBrick()
            )
        )

        assertEquals(6, manager.recyclerViewItems.size)
        assertEquals(7, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(3, 2)
        verify(observer).onItemRangeChanged(3, 1, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testAddItemsWithAllHiddenBeforeLastItem() {
        manager.addBeforeItem(
            manager.recyclerViewItems[3],
            listOf(
                HiddenTestBrick(),
                HiddenTestBrick(),
                HiddenTestBrick()
            )
        )

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(7, manager.dataManagerItems.size)
        verifyZeroInteractions(observer)
        verify(dataSetChangedListener, never()).onDataSetChanged()
    }

    @Test
    fun testAddItemsBeforeMissingItem() {
        manager.addBeforeItem(TestBrick(), listOf(TestBrick(), TestBrick(), TestBrick()))

        assertEquals(7, manager.recyclerViewItems.size)
        assertEquals(7, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(0, 3)
        verify(observer).onItemRangeChanged(0, 4, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testRemoveItem_null() {
        manager.removeItem(null)

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)

        verifyZeroInteractions(observer, dataSetChangedListener)
    }

    @Test
    fun testRemoveFirstItem_nullAdapter() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        manager.removeItem(manager.recyclerViewItems[0])

        assertEquals(3, manager.recyclerViewItems.size)
        assertEquals(3, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testRemoveFirstItem() {
        manager.removeItem(manager.recyclerViewItems[0])

        assertEquals(3, manager.recyclerViewItems.size)
        assertEquals(3, manager.dataManagerItems.size)
        verify(observer).onItemRangeRemoved(0, 1)
        verify(observer).onItemRangeChanged(0, 3, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testRemoveInvalidItem() {
        manager.removeItem(TestBrick())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testRemoveLastItem() {
        manager.removeItem(manager.recyclerViewItems[3])

        assertEquals(3, manager.recyclerViewItems.size)
        assertEquals(3, manager.dataManagerItems.size)
        verify(observer).onItemRangeRemoved(3, 1)
        verify(observer, never()).onItemRangeChanged(any(), any(), any())
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testRemoveMiddleItem() {
        manager.removeItem(manager.recyclerViewItems[1])

        assertEquals(3, manager.recyclerViewItems.size)
        assertEquals(3, manager.dataManagerItems.size)
        verify(observer).onItemRangeRemoved(1, 1)
        verify(observer).onItemRangeChanged(1, 2, null)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testRemoveHiddenItem() {
        manager.addLast(listOf(HiddenTestBrick(), TestBrick()))

        reset(observer)

        manager.removeItem(manager.dataManagerItems[4])

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verifyZeroInteractions(observer)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testRemoveNoItems() {
        manager.removeItems(listOf())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testRemoveSomeItems() {
        manager.removeItems(listOf(manager.recyclerViewItems[1], manager.recyclerViewItems[2]))

        assertEquals(2, manager.recyclerViewItems.size)
        assertEquals(2, manager.dataManagerItems.size)
        verify(observer).onChanged()
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testRemoveSomeItems_nullAdapter() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        manager.removeItems(listOf(manager.recyclerViewItems[1], manager.recyclerViewItems[2]))

        assertEquals(2, manager.recyclerViewItems.size)
        assertEquals(2, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testRemoveAllItems() {
        manager.removeItems(mutableListOf<BaseBrick>().apply { addAll(manager.recyclerViewItems) })

        assertEquals(0, manager.recyclerViewItems.size)
        assertEquals(0, manager.dataManagerItems.size)
        verify(observer).onChanged()
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testRemoveHiddenItems() {
        val hiddenBrick1 = HiddenTestBrick()
        val hiddenBrick2 = HiddenTestBrick()

        manager.addLast(listOf(hiddenBrick1, hiddenBrick2, TestBrick()))
        manager.removeItems(listOf(hiddenBrick1, hiddenBrick2))

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer, never()).onChanged()
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testClear() {
        manager.clear()

        assertEquals(0, manager.recyclerViewItems.size)
        assertEquals(0, manager.dataManagerItems.size)
        verify(observer).onItemRangeRemoved(0, 4)
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testClear_nullAdapter() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        assertEquals(STARTING_BRICKS, manager.dataManagerItems.size)
        assertEquals(STARTING_BRICKS, manager.recyclerViewItems.size)

        manager.clear()

        assertEquals(0, manager.dataManagerItems.size)
        assertEquals(0, manager.recyclerViewItems.size)
    }

    @Test
    fun testReplaceItemBothHidden() {
        val brickToReplace = HiddenTestBrick()

        manager.addAfterItem(manager.recyclerViewItems[0], brickToReplace)

        reset(observer)

        manager.replaceItem(brickToReplace, HiddenTestBrick())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verifyZeroInteractions(observer)
        verify(dataSetChangedListener, never()).onDataSetChanged()
    }

    private fun replaceItemBothVisible(replaceCount: Int) {
        val brickToReplace = TestBrick()
        manager.addAfterItem(manager.recyclerViewItems[0], brickToReplace)

        reset(observer)

        repeat((0 until replaceCount).count()) { manager.replaceItem(brickToReplace, TestBrick()) }

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer, never()).onItemRangeInserted(any(), any())
        verify(observer).onItemRangeChanged(1, 4, null)
        verify(observer, never()).onItemRangeRemoved(any(), any())
        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged()
    }

    @Test
    fun testReplaceItemBothVisible() {
        replaceItemBothVisible(1)
    }

    @Test
    fun testReplaceItemBothVisibleDoubleTap() {
        replaceItemBothVisible(2)
    }

    @Test
    fun testReplaceHiddenItemWithVisibleItem() {
        val brickToReplace = HiddenTestBrick()

        manager.addAfterItem(manager.recyclerViewItems[0], brickToReplace)

        reset(observer)

        manager.replaceItem(brickToReplace, TestBrick())

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(1, 1)
        verify(observer).onItemRangeChanged(0, 5, null)
        verify(observer, never()).onItemRangeRemoved(any(), any())
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testReplaceVisibleItemWithHiddenItem() {
        val brickToReplace = TestBrick()

        manager.addAfterItem(manager.recyclerViewItems[0], brickToReplace)

        reset(observer)

        manager.replaceItem(brickToReplace, HiddenTestBrick())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer, never()).onItemRangeInserted(any(), any())
        verify(observer).onItemRangeChanged(0, 4, null)
        verify(observer).onItemRangeRemoved(1, 1)
        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged()
    }

    @Test
    fun testReplaceInvalidItem() {
        manager.replaceItem(TestBrick(), HiddenTestBrick())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)
        verifyZeroInteractions(dataSetChangedListener, observer)
    }

    @Test
    fun testRefreshItemBothHidden() {
        val brickToRefresh = HiddenTestBrick()

        manager.addAfterItem(manager.recyclerViewItems[0], brickToRefresh)

        reset(observer)

        manager.refreshItem(brickToRefresh)

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verifyZeroInteractions(observer)
        verify(dataSetChangedListener, never()).onDataSetChanged()
    }

    @Test
    fun testRefreshItemBothVisible() {
        val brickToRefresh = TestBrick()

        manager.addAfterItem(manager.recyclerViewItems[0], brickToRefresh)

        reset(observer)

        manager.refreshItem(brickToRefresh)

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer, never()).onItemRangeInserted(any(), any())
        verify(observer).onItemRangeChanged(1, 1, null)
        verify(observer, never()).onItemRangeRemoved(any(), any())
        verify(dataSetChangedListener).onDataSetChanged()
    }

    @Test
    fun testShowItem_second() {
        val brickToRefresh = HiddenTestBrick()

        manager.addAfterItem(manager.recyclerViewItems[0], brickToRefresh)

        reset(observer)

        brickToRefresh.isHidden = false

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(1, 1)
        verify(observer).onItemRangeChanged(1, 4, null)
        verify(observer, never()).onItemRangeRemoved(any(), any())
        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged()
    }

    @Test
    fun testShowItem_last() {
        val brickToRefresh = HiddenTestBrick()

        manager.addLast(brickToRefresh)

        reset(observer)

        brickToRefresh.isHidden = false

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer).onItemRangeInserted(4, 1)
        verify(observer, never()).onItemRangeChanged(any(), any(), any())
        verify(observer, never()).onItemRangeRemoved(any(), any())
        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged()
    }

    @Test
    fun testShowItem_nullAdapter() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        val brickToRefresh = HiddenTestBrick()

        manager.addLast(brickToRefresh)

        brickToRefresh.isHidden = false

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
    }

    @Test
    fun testShowItem_unexpectedBrick() {
        val brickToRefresh = TestBrick()

        manager.showItem(brickToRefresh)

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)
        verifyZeroInteractions(observer, dataSetChangedListener)
    }

    @Test
    fun testShowItem_alreadyVisible() {
        val brickToRefresh = TestBrick()

        manager.addLast(brickToRefresh)

        reset(observer)
        reset(dataSetChangedListener)

        manager.showItem(brickToRefresh)

        assertEquals(5, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verifyZeroInteractions(observer, dataSetChangedListener)
    }

    @Test
    fun testHideItem_second() {
        val brickToRefresh = TestBrick()

        manager.addAfterItem(manager.recyclerViewItems[0], brickToRefresh)

        reset(observer)

        brickToRefresh.isHidden = true

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer, never()).onItemRangeInserted(any(), any())
        verify(observer).onItemRangeChanged(1, 3, null)
        verify(observer).onItemRangeRemoved(1, 1)
        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged()
    }

    @Test
    fun testHideItem_last() {
        val brickToRefresh = TestBrick()

        manager.addLast(brickToRefresh)

        reset(observer)

        brickToRefresh.isHidden = true

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verify(observer, never()).onItemRangeInserted(any(), any())
        verify(observer, never()).onItemRangeChanged(any(), any(), any())
        verify(observer).onItemRangeRemoved(4, 1)
        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged()
    }

    @Test
    fun testHideItem_nullAdapter() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        val brickToRefresh = TestBrick()

        manager.addLast(brickToRefresh)

        brickToRefresh.isHidden = true

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
    }

    @Test
    fun testHideItem_unexpectedBrick() {
        val brickToRefresh = TestBrick()

        reset(observer)

        manager.hideItem(brickToRefresh)

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)
        verifyZeroInteractions(observer, dataSetChangedListener)
    }

    @Test
    fun testHideItem_alreadyHidden() {
        val brickToRefresh = HiddenTestBrick()

        manager.addLast(brickToRefresh)

        reset(observer)
        reset(dataSetChangedListener)

        manager.hideItem(brickToRefresh)

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(5, manager.dataManagerItems.size)
        verifyZeroInteractions(observer, dataSetChangedListener)
    }

    @Test
    fun testRefreshInvalidItem() {
        manager.refreshItem(TestBrick())

        assertEquals(4, manager.recyclerViewItems.size)
        assertEquals(4, manager.dataManagerItems.size)
        verifyZeroInteractions(observer)
    }

    @Test
    fun testRefreshItem_nullAdapter() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        manager.refreshItem(manager.dataManagerItems.first())
    }

    @Test
    fun testOnDestroy() {
        manager.onDestroyView()
        manager.onDestroyView()

        assertNull(manager.getRecyclerView())
    }

    @Test
    fun testSmoothScrollToBrick_recyclerViewNotSet() {
        val manager = BrickDataManager().apply {
            repeat((0 until STARTING_BRICKS).count()) { addLast(TestBrick()) }
        }

        manager.smoothScrollToBrick(manager.recyclerViewItems.last())

        verify(recyclerView, never()).smoothScrollToPosition(any())
    }

    @Test
    fun testSmoothScrollToBrick() {
        manager.smoothScrollToBrick(manager.recyclerViewItems.last())

        verify(recyclerView).smoothScrollToPosition(manager.recyclerViewItems.lastIndex)
    }

    @Test
    fun testSmoothScrollToBrick_missingBrick() {
        manager.smoothScrollToBrick(TestBrick())

        verify(recyclerView, never()).smoothScrollToPosition(any())
    }

    @Test
    fun testSetDataSetChangedListener() {
        val listener = mock<DataSetChangedListener>()

        manager.setDataSetChangedListener(listener)
        manager.clear()

        verify(listener).onDataSetChanged()
    }

    @Test
    fun testIsEmpty() {
        val manager = BrickDataManager()
        assertTrue(manager.isEmpty)

        manager.addLast(mock<BaseBrick>())
        assertFalse(manager.isEmpty)
    }

    class HiddenTestBrick : TestBrick() {
        init {
            isHidden = true
        }
    }

    open class TestBrick : BaseBrick(FullWidthBrickSize(), ZeroBrickPadding()) {
        override val layout = 1
        override fun onBindData(holder: BrickViewHolder) = Unit
        override fun createViewHolder(itemView: View) = BrickViewHolder(itemView)
    }

    class TestPlaceholderBrick(override val isDataReady: Boolean) : BaseBrick(FullWidthBrickSize(), ZeroBrickPadding()) {
        override val placeholderLayout = 1
        override fun onBindData(holder: BrickViewHolder) = Unit
        override val layout = 0
        override fun createViewHolder(itemView: View) = BrickViewHolder(itemView)
    }

    companion object {
        private const val STARTING_BRICKS = 4
    }
}
