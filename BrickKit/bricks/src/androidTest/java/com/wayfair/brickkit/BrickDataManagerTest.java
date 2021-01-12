/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.content.Context;
import android.os.Looper;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.util.BrickTestHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class BrickDataManagerTest {
    private static final int STARTING_BRICKS = 4;
    public static final String TAG = "Tag";

    private BrickDataManager manager;
    private BrickTestHelper.TestAdapterDataObserver observer;
    private DataSetChangedListener dataSetChangedListener;
    private BrickTestHelper brickTestHelper;

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        Context context = InstrumentationRegistry.getTargetContext();
        manager = new BrickDataManager();
        manager.setRecyclerView(new RecyclerView(context));
        brickTestHelper = new BrickTestHelper();

        for (int i = 0; i < STARTING_BRICKS; i++) {
            manager.addLast(brickTestHelper.generateBrick());
        }

        dataSetChangedListener = mock(DataSetChangedListener.class);
        manager.setDataSetChangedListener(dataSetChangedListener);

        observer = new BrickTestHelper.TestAdapterDataObserver();
        manager.getBrickRecyclerAdapter().registerAdapterDataObserver(observer);
    }

    @Test
    public void testSetItems() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(brickTestHelper.generateBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateBrick());

        manager.setItems(newItems);

        assertEquals(3, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeRemovedPositionStart());
        assertEquals(4, observer.getItemRangeRemovedItemCount());

        assertEquals(0, observer.getItemRangeInsertedPositionStart());
        assertEquals(3, observer.getItemRangeInsertedItemCount());

        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged();
    }

    @Test
    public void testAddLastVisible() {
        manager.addLast(brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(4, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(4, observer.getItemRangeChangedPositionStart());
        assertEquals(0, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testUpdateBricks() {
        LinkedList<BaseBrick> bricks = new LinkedList<>(manager.getDataManagerItems());

        bricks.addLast(brickTestHelper.generateBrick());
        bricks.add(1, brickTestHelper.generateBrick());
        bricks.add(3, brickTestHelper.generateBrick());

        manager.updateBricks(bricks);

        assertEquals(7, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testItemExist() {
        assertTrue(manager.hasInstanceOf(BaseBrick.class));
        manager.clear();
        assertFalse(manager.hasInstanceOf(BaseBrick.class));
    }

    @Test
    public void testAddLastHidden() {
        manager.addLast(brickTestHelper.generateHiddenBrick());

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener, never()).onDataSetChanged();
    }

    @Test
    public void testAddLastCollection() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(brickTestHelper.generateBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateBrick());

        manager.addLast(newItems);

        assertEquals(7, manager.getRecyclerViewItems().size());
        assertEquals(9, manager.getDataManagerItems().size());

        assertEquals(4, observer.getItemRangeInsertedPositionStart());
        assertEquals(3, observer.getItemRangeInsertedItemCount());

        assertEquals(4, observer.getItemRangeChangedPositionStart());
        assertEquals(0, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddLastCollectionAllHidden() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());

        manager.addLast(newItems);

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(9, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener, never()).onDataSetChanged();
    }

    @Test
    public void testAddFirstVisible() {
        BaseBrick newBrick = brickTestHelper.generateBrick();
        manager.addFirst(newBrick);

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(1, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        assertEquals(newBrick, manager.getDataManagerItems().get(0));

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddFirstVisibleHorizontal() {
        Context context = InstrumentationRegistry.getTargetContext();
        BrickDataManager manager = new BrickDataManager();
        manager.setHorizontalRecyclerView(new RecyclerView(context));
        BrickTestHelper brickTestHelper = new BrickTestHelper();

        DataSetChangedListener dataSetChangedListener = mock(DataSetChangedListener.class);
        manager.setDataSetChangedListener(dataSetChangedListener);

        manager.addLast(brickTestHelper.generateBrick());
        manager.addLast(brickTestHelper.generateBrick());
        manager.addLast(brickTestHelper.generateBrick());
        manager.addLast(brickTestHelper.generateBrick());

        BrickTestHelper.TestAdapterDataObserver observer = new BrickTestHelper.TestAdapterDataObserver();
        manager.getBrickRecyclerAdapter().registerAdapterDataObserver(observer);

        BaseBrick newBrick = brickTestHelper.generateBrick();
        manager.addFirst(newBrick);
        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(1, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        assertEquals(newBrick, manager.getDataManagerItems().get(0));

        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged();
    }

    @Test
    public void testAddFirstHidden() {
        manager.addFirst(brickTestHelper.generateHiddenBrick());

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener, never()).onDataSetChanged();
    }

    @Test
    public void testAddFirstCollection() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(brickTestHelper.generateBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateBrick());

        manager.addFirst(newItems);

        assertEquals(7, manager.getRecyclerViewItems().size());
        assertEquals(9, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeInsertedPositionStart());
        assertEquals(3, observer.getItemRangeInsertedItemCount());

        assertEquals(3, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddFirstCollectionAllHidden() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());
        newItems.add(brickTestHelper.generateHiddenBrick());

        manager.addFirst(newItems);

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(9, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener, never()).onDataSetChanged();
    }

    @Test
    public void testAddBeforeFirstItem() {
        manager.addBeforeItem(manager.getRecyclerViewItems().get(0), brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(0, observer.getItemRangeChangedPositionStart());
        assertEquals(5, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddBeforeWithHiddenItem() {
        manager.addBeforeItem(manager.getRecyclerViewItems().get(1), brickTestHelper.generateHiddenBrick());

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener, never()).onDataSetChanged();
    }

    @Test
    public void testAddBeforeHiddenItemsAtEnd() {
        manager.addLast(brickTestHelper.generateHiddenBrick());
        BaseBrick lastHiddenBrick = brickTestHelper.generateHiddenBrick();
        manager.addLast(lastHiddenBrick);
        manager.addBeforeItem(lastHiddenBrick, brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        assertEquals(4, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(4, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddBeforeHiddenItemsInMiddle() {
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickTestHelper.generateHiddenBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickTestHelper.generateHiddenBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickTestHelper.generateHiddenBrick());

        manager.addBeforeItem(manager.getDataManagerItems().get(3), brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(8, manager.getDataManagerItems().size());

        assertEquals(1, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(1, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddBeforeHiddenItemsAtBeginning() {
        manager.addFirst(brickTestHelper.generateHiddenBrick());
        manager.addFirst(brickTestHelper.generateHiddenBrick());
        manager.addFirst(brickTestHelper.generateHiddenBrick());

        manager.addBeforeItem(manager.getRecyclerViewItems().get(0), brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(8, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(0, observer.getItemRangeChangedPositionStart());
        assertEquals(5, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddAfterWithHiddenItem() {
        manager.addAfterItem(manager.getRecyclerViewItems().get(1), brickTestHelper.generateHiddenBrick());

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener, never()).onDataSetChanged();
    }

    @Test
    public void testAddAfterHiddenItemsAtEnd() {
        manager.addLast(brickTestHelper.generateHiddenBrick());
        BaseBrick lastHiddenBrick = brickTestHelper.generateHiddenBrick();
        manager.addLast(lastHiddenBrick);
        manager.addAfterItem(lastHiddenBrick, brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        assertEquals(4, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(4, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddAfterHiddenItemsInMiddle() {
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickTestHelper.generateHiddenBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickTestHelper.generateHiddenBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickTestHelper.generateHiddenBrick());

        manager.addAfterItem(manager.getDataManagerItems().get(3), brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(8, manager.getDataManagerItems().size());

        assertEquals(1, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(1, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddAfterHiddenItemsAtBeginning() {
        manager.addFirst(brickTestHelper.generateHiddenBrick());
        manager.addFirst(brickTestHelper.generateHiddenBrick());
        manager.addFirst(brickTestHelper.generateHiddenBrick());

        manager.addBeforeItem(manager.getRecyclerViewItems().get(0), brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(8, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(0, observer.getItemRangeChangedPositionStart());
        assertEquals(5, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddBeforeLastItem() {
        manager.addBeforeItem(manager.getRecyclerViewItems().get(3), brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(3, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(3, observer.getItemRangeChangedPositionStart());
        assertEquals(2, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddBeforeInvalidItem() {
        manager.addBeforeItem(brickTestHelper.generateBrick(), brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(0, observer.getItemRangeChangedPositionStart());
        assertEquals(5, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddAfterFirstItem() {
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(1, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(1, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddAfterLastItem() {
        manager.addAfterItem(manager.getRecyclerViewItems().get(3), brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(4, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(4, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddAfterLastHiddenItem() {
        manager.addLast(brickTestHelper.generateHiddenBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(3), brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(6, manager.getDataManagerItems().size());

        assertEquals(4, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(4, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddAfterInvalidItem() {
        manager.addAfterItem(brickTestHelper.generateBrick(), brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(4, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(4, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddItemsBeforeLastItem() {
        ArrayList<BaseBrick> items = new ArrayList<>();
        items.add(brickTestHelper.generateBrick());
        items.add(brickTestHelper.generateBrick());
        items.add(brickTestHelper.generateBrick());
        manager.addBeforeItem(manager.getRecyclerViewItems().get(3), items);

        assertEquals(7, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        assertEquals(3, observer.getItemRangeInsertedPositionStart());
        assertEquals(3, observer.getItemRangeInsertedItemCount());

        assertEquals(3, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddItemsWithOneHiddenBeforeLastItem() {
        ArrayList<BaseBrick> items = new ArrayList<>();
        items.add(brickTestHelper.generateBrick());
        items.add(brickTestHelper.generateHiddenBrick());
        items.add(brickTestHelper.generateBrick());
        manager.addBeforeItem(manager.getRecyclerViewItems().get(3), items);

        assertEquals(6, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        assertEquals(3, observer.getItemRangeInsertedPositionStart());
        assertEquals(2, observer.getItemRangeInsertedItemCount());

        assertEquals(3, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddItemsWithAllHiddenBeforeLastItem() {
        ArrayList<BaseBrick> items = new ArrayList<>();
        items.add(brickTestHelper.generateHiddenBrick());
        items.add(brickTestHelper.generateHiddenBrick());
        items.add(brickTestHelper.generateHiddenBrick());
        manager.addBeforeItem(manager.getRecyclerViewItems().get(3), items);

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener, never()).onDataSetChanged();
    }

    @Test
    public void testAddItemsBeforeMissingItem() {
        ArrayList<BaseBrick> items = new ArrayList<>();
        items.add(brickTestHelper.generateBrick());
        items.add(brickTestHelper.generateBrick());
        items.add(brickTestHelper.generateBrick());
        manager.addBeforeItem(brickTestHelper.generateBrick(), items);

        assertEquals(7, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeInsertedPositionStart());
        assertEquals(3, observer.getItemRangeInsertedItemCount());

        assertEquals(0, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddItemsAfterLastItem() {
        ArrayList<BaseBrick> items = new ArrayList<>();
        items.add(brickTestHelper.generateBrick());
        items.add(brickTestHelper.generateBrick());
        items.add(brickTestHelper.generateBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(3), items);

        assertEquals(7, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        assertEquals(4, observer.getItemRangeInsertedPositionStart());
        assertEquals(3, observer.getItemRangeInsertedItemCount());

        assertEquals(4, observer.getItemRangeChangedPositionStart());
        assertEquals(0, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddItemsWithOneHiddenAfterLastItem() {
        ArrayList<BaseBrick> items = new ArrayList<>();
        items.add(brickTestHelper.generateBrick());
        items.add(brickTestHelper.generateHiddenBrick());
        items.add(brickTestHelper.generateBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(3), items);

        assertEquals(6, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        assertEquals(4, observer.getItemRangeInsertedPositionStart());
        assertEquals(2, observer.getItemRangeInsertedItemCount());

        assertEquals(4, observer.getItemRangeChangedPositionStart());
        assertEquals(0, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddItemsWithAllHiddenAfterLastItem() {
        ArrayList<BaseBrick> items = new ArrayList<>();
        items.add(brickTestHelper.generateHiddenBrick());
        items.add(brickTestHelper.generateHiddenBrick());
        items.add(brickTestHelper.generateHiddenBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(3), items);

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener, never()).onDataSetChanged();
    }

    @Test
    public void testAddItemsAfterMissingItem() {
        ArrayList<BaseBrick> items = new ArrayList<>();
        items.add(brickTestHelper.generateBrick());
        items.add(brickTestHelper.generateBrick());
        items.add(brickTestHelper.generateBrick());
        manager.addAfterItem(brickTestHelper.generateBrick(), items);

        assertEquals(7, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        assertEquals(4, observer.getItemRangeInsertedPositionStart());
        assertEquals(3, observer.getItemRangeInsertedItemCount());

        assertEquals(4, observer.getItemRangeChangedPositionStart());
        assertEquals(0, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testRemoveFirstItem() {
        manager.removeItem(manager.getRecyclerViewItems().get(0));

        assertEquals(3, manager.getRecyclerViewItems().size());
        assertEquals(3, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeRemovedPositionStart());
        assertEquals(1, observer.getItemRangeRemovedItemCount());

        assertEquals(0, observer.getItemRangeChangedPositionStart());
        assertEquals(3, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testRemoveLastItem() {
        manager.removeItem(manager.getRecyclerViewItems().get(3));

        assertEquals(3, manager.getRecyclerViewItems().size());
        assertEquals(3, manager.getDataManagerItems().size());

        assertEquals(3, observer.getItemRangeRemovedPositionStart());
        assertEquals(1, observer.getItemRangeRemovedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testRemoveMiddleItem() {
        manager.removeItem(manager.getRecyclerViewItems().get(1));

        assertEquals(3, manager.getRecyclerViewItems().size());
        assertEquals(3, manager.getDataManagerItems().size());

        assertEquals(1, observer.getItemRangeRemovedPositionStart());
        assertEquals(1, observer.getItemRangeRemovedItemCount());

        assertEquals(1, observer.getItemRangeChangedPositionStart());
        assertEquals(2, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testRemoveHiddenItem() {
        manager.addLast(brickTestHelper.generateHiddenBrick());
        manager.addLast(brickTestHelper.generateBrick());

        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.removeItem(manager.getDataManagerItems().get(4));

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeRemovedPositionStart());
        assertEquals(-1, observer.getItemRangeRemovedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testMoveThirdItemToBeginning() {
        BaseBrick toMove = manager.getRecyclerViewItems().get(2);
        BaseBrick positionBrick = manager.getRecyclerViewItems().get(0);
        manager.moveItem(toMove, positionBrick);

        assertEquals(0, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        assertEquals(0, manager.getRecyclerViewItems().indexOf(toMove));
        assertEquals(1, manager.getRecyclerViewItems().indexOf(positionBrick));
    }

    @Test
    public void testMoveFirstItemToEnd() {
        BaseBrick toMove = manager.getRecyclerViewItems().get(0);
        BaseBrick positionBrick = manager.getRecyclerViewItems().get(3);
        manager.moveItem(toMove, positionBrick);

        assertEquals(0, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        assertEquals(3, manager.getRecyclerViewItems().indexOf(toMove));
        assertEquals(2, manager.getRecyclerViewItems().indexOf(positionBrick));
    }

    @Test
    public void testRemoveSomeItems() {
        List<BaseBrick> itemsToRemove = new LinkedList<>();
        itemsToRemove.add(manager.getRecyclerViewItems().get(1));
        itemsToRemove.add(manager.getRecyclerViewItems().get(2));

        manager.removeItems(itemsToRemove);

        assertEquals(2, manager.getRecyclerViewItems().size());
        assertEquals(2, manager.getDataManagerItems().size());

        assertTrue(observer.isChanged());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testRemoveAllItems() {
        List<BaseBrick> itemsToRemove = new LinkedList<>();
        itemsToRemove.add(manager.getRecyclerViewItems().get(0));
        itemsToRemove.add(manager.getRecyclerViewItems().get(1));
        itemsToRemove.add(manager.getRecyclerViewItems().get(2));
        itemsToRemove.add(manager.getRecyclerViewItems().get(3));

        manager.removeItems(itemsToRemove);

        assertEquals(0, manager.getRecyclerViewItems().size());
        assertEquals(0, manager.getDataManagerItems().size());

        assertTrue(observer.isChanged());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testRemoveAllTestBrick() {
        manager.addLast(brickTestHelper.generateOtherBrick());

        manager.removeAll(BrickTestHelper.TestBrick.class);

        assertEquals(1, manager.getRecyclerViewItems().size());
        assertEquals(1, manager.getDataManagerItems().size());

        assertTrue(observer.isChanged());

        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged();
    }

    @Test
    public void testRemoveHiddenItems() {
        manager.addLast(brickTestHelper.generateHiddenBrick());
        manager.addLast(brickTestHelper.generateHiddenBrick());
        manager.addLast(brickTestHelper.generateBrick());

        List<BaseBrick> itemsToRemove = new LinkedList<>();
        itemsToRemove.add(manager.getDataManagerItems().get(4));
        itemsToRemove.add(manager.getDataManagerItems().get(5));

        manager.removeItems(itemsToRemove);

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertFalse(observer.isChanged());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testClear() {
        manager.clear();

        assertEquals(0, manager.getRecyclerViewItems().size());
        assertEquals(0, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeRemovedPositionStart());
        assertEquals(4, observer.getItemRangeRemovedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testReplaceItemBothHidden() {
        BaseBrick brickToReplace = brickTestHelper.generateHiddenBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToReplace);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.replaceItem(brickToReplace, brickTestHelper.generateHiddenBrick());

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        assertEquals(-1, observer.getItemRangeRemovedPositionStart());
        assertEquals(-1, observer.getItemRangeRemovedItemCount());

        verify(dataSetChangedListener, never()).onDataSetChanged();
    }

    private void replaceItemBothVisible(int replaceCount) {
        BaseBrick brickToReplace = brickTestHelper.generateBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToReplace);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        for (int i = 0; i < replaceCount; i++) {
            manager.replaceItem(brickToReplace, brickTestHelper.generateBrick());
        }

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(1, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        assertEquals(-1, observer.getItemRangeRemovedPositionStart());
        assertEquals(-1, observer.getItemRangeRemovedItemCount());

        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged();
    }

    @Test
    public void testReplaceItemBothVisible() {
        replaceItemBothVisible(1);
    }

    @Test
    public void testReplaceItemBothVisibleDoubleTap() {
        replaceItemBothVisible(2);
    }

    @Test
    public void testReplaceHiddenItemWithVisibleItem() {
        BaseBrick brickToReplace = brickTestHelper.generateHiddenBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToReplace);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.replaceItem(brickToReplace, brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(1, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(0, observer.getItemRangeChangedPositionStart());
        assertEquals(5, observer.getItemRangeChangedItemCount());

        assertEquals(-1, observer.getItemRangeRemovedPositionStart());
        assertEquals(-1, observer.getItemRangeRemovedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testReplaceVisibleItemWithHiddenItem() {
        BaseBrick brickToReplace = brickTestHelper.generateBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToReplace);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.replaceItem(brickToReplace, brickTestHelper.generateHiddenBrick());

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(0, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        assertEquals(1, observer.getItemRangeRemovedPositionStart());
        assertEquals(1, observer.getItemRangeRemovedItemCount());

        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged();
    }

    @Test
    public void testRefreshItemBothHidden() {
        BaseBrick brickToRefresh = brickTestHelper.generateHiddenBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToRefresh);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.refreshItem(brickToRefresh);

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        assertEquals(-1, observer.getItemRangeRemovedPositionStart());
        assertEquals(-1, observer.getItemRangeRemovedItemCount());

        verify(dataSetChangedListener, never()).onDataSetChanged();
    }

    @Test
    public void testRefreshItemBothVisible() {
        BaseBrick brickToRefresh = brickTestHelper.generateBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToRefresh);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.refreshItem(brickToRefresh);

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(1, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());

        assertEquals(-1, observer.getItemRangeRemovedPositionStart());
        assertEquals(-1, observer.getItemRangeRemovedItemCount());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testRefreshHiddenItemWithVisibleItem() {
        BaseBrick brickToRefresh = brickTestHelper.generateHiddenBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToRefresh);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.showItem(brickToRefresh);

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(1, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());

        assertEquals(1, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        assertEquals(-1, observer.getItemRangeRemovedPositionStart());
        assertEquals(-1, observer.getItemRangeRemovedItemCount());

        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged();
    }

    @Test
    public void testRefreshVisibleItemWithHiddenItem() {
        BaseBrick brickToRefresh = brickTestHelper.generateBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToRefresh);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.hideItem(brickToRefresh);

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(1, observer.getItemRangeChangedPositionStart());
        assertEquals(3, observer.getItemRangeChangedItemCount());

        assertEquals(1, observer.getItemRangeRemovedPositionStart());
        assertEquals(1, observer.getItemRangeRemovedItemCount());

        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged();
    }

    @Test
    public void testRefreshInvalidItem() {
        manager.refreshItem(brickTestHelper.generateBrick());

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(4, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        assertEquals(-1, observer.getItemRangeRemovedPositionStart());
        assertEquals(-1, observer.getItemRangeRemovedItemCount());
    }

    @Test
    public void testOnDestroy() {
        manager.onDestroyView();

        assertNull(manager.getRecyclerView());
    }

    @Test
    public void testBrickWithLayout() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(brickTestHelper.generateBrickWithLayoutId(1));

        manager.setItems(newItems);

        @LayoutRes int layoutRes = 1;
        assertNotNull(manager.brickWithLayout(layoutRes));
    }

    @Test
    public void testBrickWithLayoutInvalidLayout() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(brickTestHelper.generateBrickWithLayoutId(1));

        manager.setItems(newItems);

        @LayoutRes int layoutRes = 2;
        assertNull(manager.brickWithLayout(layoutRes));
    }

    @Test
    public void testBrickWithPlaceholderLayoutWhenDataIsReady() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(brickTestHelper.generateBrickWithPlaceholderLayoutId(1, true));

        manager.setItems(newItems);

        @LayoutRes int layoutRes = 1;
        assertNull(manager.brickWithPlaceholderLayout(layoutRes));
    }

    @Test
    public void testBrickWithPlaceholderLayoutWhenDataIsNotReady() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(brickTestHelper.generateBrickWithPlaceholderLayoutId(1, false));

        manager.setItems(newItems);

        @LayoutRes int layoutRes = 1;
        assertNotNull(manager.brickWithPlaceholderLayout(layoutRes));
    }

    @Test
    public void testBrickWithPlaceholderLayoutInvalidLayoutWhenDataIsNotReady() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(brickTestHelper.generateBrickWithPlaceholderLayoutId(1, false));

        manager.setItems(newItems);

        @LayoutRes int layoutRes = 2;
        assertNull(manager.brickWithPlaceholderLayout(layoutRes));
    }

    @Test
    public void testBrickWithPlaceholderLayoutInvalidLayoutWhenDataIsReady() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(brickTestHelper.generateBrickWithPlaceholderLayoutId(1, true));

        manager.setItems(newItems);

        @LayoutRes int layoutRes = 2;
        assertNull(manager.brickWithPlaceholderLayout(layoutRes));
    }

    @Test
    public void testBrickAtPosition() {
        assertNotNull(manager.brickAtPosition(manager.getDataManagerItems().size() - 1));
        assertNull(manager.brickAtPosition(manager.getDataManagerItems().size()));
        assertNull(manager.brickAtPosition(-1));
    }

    @Test
    public void testSmoothScrollToBrick() {
        manager.smoothScrollToBrick(manager.brickAtPosition(manager.getRecyclerViewItems().size() - 1));
        manager.smoothScrollToBrick(brickTestHelper.generateBrick());
    }

    @Test
    public void testAddToTagCache() {
        BaseBrick baseBrick = brickTestHelper.generateBrick();
        baseBrick.setTag(TAG);
        manager.addLast(baseBrick);

        assertEquals(1, manager.getBricksByTag(TAG).size());
    }

    @Test
    public void testAddMultipleToTagCache() {
        BaseBrick baseBrick = brickTestHelper.generateBrick();
        baseBrick.setTag(TAG);
        manager.addLast(baseBrick);

        BaseBrick baseBrick2 = brickTestHelper.generateBrick();
        baseBrick.setTag(TAG);
        manager.addLast(baseBrick2);

        assertEquals(2, manager.getBricksByTag(TAG).size());
    }

    @Test
    public void testAddNullToTagCache() {
        BaseBrick baseBrick = brickTestHelper.generateBrick();
        baseBrick.setTag(TAG);
        manager.addLast(baseBrick);
        baseBrick.setTag(null);

        assertNull(manager.getBricksByTag(TAG));
    }

    @Test
    public void testRemoveFromTagCache() {
        BaseBrick baseBrick = brickTestHelper.generateBrick();
        baseBrick.setTag(TAG);
        manager.addLast(baseBrick);

        assertEquals(1, manager.getBricksByTag(TAG).size());

        manager.removeAllByTag(TAG);

        assertNull(manager.getBricksByTag(TAG));
    }

    @Test
    public void testAddToLayoutIdCache() {
        BaseBrick baseBrick = brickTestHelper.generateBrick();
        manager.addLast(baseBrick);

        assertEquals(STARTING_BRICKS + 1, manager.getBricksByLayoutId(baseBrick.getLayout()).size());
    }

    @Test
    public void testAddMultipleToLayoutIdCache() {
        BaseBrick baseBrick = brickTestHelper.generateBrick();
        manager.addLast(baseBrick);

        BaseBrick baseBrick2 = brickTestHelper.generateBrick();
        manager.addLast(baseBrick2);

        assertEquals(STARTING_BRICKS + 2, manager.getBricksByLayoutId(baseBrick.getLayout()).size());
    }

    @Test
    public void testAddNullToLayoutIdCache() {
        BaseBrick baseBrick = brickTestHelper.generateBrick();
        manager.addLast(baseBrick);

        assertNull(manager.getBricksByLayoutId(-1));
    }

    @Test
    public void testRemoveFromLayoutIdCache() {
        BaseBrick baseBrick = brickTestHelper.generateBrick();
        manager.addLast(baseBrick);

        assertEquals(STARTING_BRICKS + 1, manager.getBricksByLayoutId(baseBrick.getLayout()).size());

        manager.removeAllByLayoutId(baseBrick.getLayout());

        assertNull(manager.getBricksByLayoutId(baseBrick.getLayout()));
    }

    @Test
    public void testMethodGetPaddingOrDefaults_resultsInPassedInValue() {
        // Given
        BrickDataManager dataManager = new BrickDataManager();
        int expectedPaddingPosition = 2;

        // When
        int paddingPosition = dataManager.getPaddingPositionOrDefault(expectedPaddingPosition);

        // Verify
        assertNotEquals(BrickDataManager.DEFAULT_BRICK_POSITION, paddingPosition);
        assertEquals(expectedPaddingPosition, paddingPosition);
    }

    @Test
    public void testMethodGetPaddingOrDefault_with_NO_PADDING_POSITION_forPosition_resultsInDefault() {
        // Given
        BrickDataManager dataManager = new BrickDataManager();

        // When
        int expectedPaddingPosition = BrickDataManager.NO_PADDING_POSITION;
        int paddingPosition = dataManager.getPaddingPositionOrDefault(expectedPaddingPosition);

        // Verify
        assertEquals(BrickDataManager.DEFAULT_BRICK_POSITION, paddingPosition);
    }

    @Test
    public void testComputePaddingPositionSafelyForFirstItem_withZeroItems_resultsIn_NO_PADDING_POSITION_Value() {
        // Given
        BrickDataManager dataManager = new BrickDataManager();

        // When
        int paddingPosition = dataManager.computePaddingPositionSafelyForFirstItem();

        // Verify
        assertEquals(BrickDataManager.NO_PADDING_POSITION, paddingPosition);
    }

    @Test
    public void testComputePaddingPositionSafelyForFirstItem_withMultipleItems_resultsInAccuratePosition() {
        // Given
        BrickDataManager dataManager = new BrickDataManager();
        Context context = InstrumentationRegistry.getTargetContext();
        dataManager.setHorizontalRecyclerView(new RecyclerView(context));

        // When
        List<BaseBrick> newItems = new LinkedList<>();

        BaseBrick brick1 = brickTestHelper.generateBrick();
        brick1.setHidden(false);
        newItems.add(brick1);

        BaseBrick brick2 = brickTestHelper.generateBrick();
        brick2.setHidden(false);
        newItems.add(brick1);

        dataManager.setItems(newItems);
        dataManager.dataHasChanged();
        int paddingPosition = dataManager.computePaddingPositionSafelyForFirstItem();

        // Verify
        assertNotEquals(BrickDataManager.NO_PADDING_POSITION, paddingPosition);
    }

    @Test
    public void testSafeNotifyItemInserted() {
        // Given
        List<BaseBrick> items = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            items.add(brickTestHelper.generateBrick());
        }

        // When
        manager.setItems(items);


        // Verify
        assertEquals(0, observer.getItemRangeInsertedPositionStart());
        assertEquals(5, observer.getItemRangeInsertedItemCount());

        // When
        manager.safeNotifyItemInserted(items.get(1));

        // Verifiy
        assertEquals(1, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());
    }

    @Test
    public void testSafeNotifyItemRangeInserted() {
        // Given
        List<BaseBrick> items = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            items.add(brickTestHelper.generateBrick());
        }

        // When
        manager.setItems(items);
        manager.safeNotifyItemRangeInserted(items.get(3), 3);

        // Verify
        assertEquals(3, observer.getItemRangeInsertedPositionStart());
        assertEquals(3, observer.getItemRangeInsertedItemCount());
    }

    @Test
    public void testSetDataSetChangedListener() {
        DataSetChangedListener listener = mock(DataSetChangedListener.class);

        manager.setDataSetChangedListener(listener);

        manager.clear();

        verify(listener).onDataSetChanged();
    }
}
