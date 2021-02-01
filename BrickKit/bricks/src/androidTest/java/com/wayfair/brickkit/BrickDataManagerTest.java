/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.os.Looper;
import android.view.View;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.ZeroBrickPadding;
import com.wayfair.brickkit.size.HalfWidthBrickSize;
import com.wayfair.brickkit.viewholder.BrickViewHolder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
    private TestAdapterDataObserver observer;
    private DataSetChangedListener dataSetChangedListener;

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        manager = new BrickDataManager();
        manager.setRecyclerView(new RecyclerView(ApplicationProvider.getApplicationContext()));

        for (int i = 0; i < STARTING_BRICKS; i++) {
            manager.addLast(generateBrick());
        }

        dataSetChangedListener = mock(DataSetChangedListener.class);
        manager.setDataSetChangedListener(dataSetChangedListener);

        observer = new TestAdapterDataObserver();
        manager.getRecyclerView().getAdapter().registerAdapterDataObserver(observer);
    }

    @Test
    public void testAddLastVisible() {
        manager.addLast(generateBrick());

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

        bricks.addLast(generateBrick());
        bricks.add(1, generateBrick());
        bricks.add(3, generateBrick());

        manager.updateBricks(bricks);

        assertEquals(7, manager.getRecyclerViewItems().size());
        assertEquals(7, manager.getDataManagerItems().size());

        verify(dataSetChangedListener).onDataSetChanged();
    }

    @Test
    public void testAddLastHidden() {
        manager.addLast(generateHiddenBrick());

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
        newItems.add(generateBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateBrick());

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
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());

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
        BaseBrick newBrick = generateBrick();
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
        BrickDataManager manager = new BrickDataManager();
        manager.setHorizontalRecyclerView(new RecyclerView(ApplicationProvider.getApplicationContext()));

        DataSetChangedListener dataSetChangedListener = mock(DataSetChangedListener.class);
        manager.setDataSetChangedListener(dataSetChangedListener);

        manager.addLast(generateBrick());
        manager.addLast(generateBrick());
        manager.addLast(generateBrick());
        manager.addLast(generateBrick());

        TestAdapterDataObserver observer = new TestAdapterDataObserver();
        manager.getRecyclerView().getAdapter().registerAdapterDataObserver(observer);

        BaseBrick newBrick = generateBrick();
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
        manager.addFirst(generateHiddenBrick());

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());

        verify(dataSetChangedListener, never()).onDataSetChanged();
    }

    @Test
    public void testAddBeforeFirstItem() {
        manager.addBeforeItem(manager.getRecyclerViewItems().get(0), generateBrick());

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
        manager.addBeforeItem(manager.getRecyclerViewItems().get(1), generateHiddenBrick());

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
        manager.addLast(generateHiddenBrick());
        BaseBrick lastHiddenBrick = generateHiddenBrick();
        manager.addLast(lastHiddenBrick);
        manager.addBeforeItem(lastHiddenBrick, generateBrick());

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
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), generateHiddenBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), generateHiddenBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), generateHiddenBrick());

        manager.addBeforeItem(manager.getDataManagerItems().get(3), generateBrick());

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
        manager.addFirst(generateHiddenBrick());
        manager.addFirst(generateHiddenBrick());
        manager.addFirst(generateHiddenBrick());

        manager.addBeforeItem(manager.getRecyclerViewItems().get(0), generateBrick());

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
        manager.addAfterItem(manager.getRecyclerViewItems().get(1), generateHiddenBrick());

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
        manager.addLast(generateHiddenBrick());
        BaseBrick lastHiddenBrick = generateHiddenBrick();
        manager.addLast(lastHiddenBrick);
        manager.addAfterItem(lastHiddenBrick, generateBrick());

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
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), generateHiddenBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), generateHiddenBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), generateHiddenBrick());

        manager.addAfterItem(manager.getDataManagerItems().get(3), generateBrick());

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
        manager.addFirst(generateHiddenBrick());
        manager.addFirst(generateHiddenBrick());
        manager.addFirst(generateHiddenBrick());

        manager.addBeforeItem(manager.getRecyclerViewItems().get(0), generateBrick());

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
        manager.addBeforeItem(manager.getRecyclerViewItems().get(3), generateBrick());

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
        manager.addBeforeItem(generateBrick(), generateBrick());

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
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), generateBrick());

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
        manager.addAfterItem(manager.getRecyclerViewItems().get(3), generateBrick());

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
        manager.addLast(generateHiddenBrick());
        manager.addAfterItem(manager.getRecyclerViewItems().get(3), generateBrick());

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
        manager.addAfterItem(generateBrick(), generateBrick());

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
        items.add(generateBrick());
        items.add(generateBrick());
        items.add(generateBrick());
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
        items.add(generateBrick());
        items.add(generateHiddenBrick());
        items.add(generateBrick());
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
        items.add(generateHiddenBrick());
        items.add(generateHiddenBrick());
        items.add(generateHiddenBrick());
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
        items.add(generateBrick());
        items.add(generateBrick());
        items.add(generateBrick());
        manager.addBeforeItem(generateBrick(), items);

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
        items.add(generateBrick());
        items.add(generateBrick());
        items.add(generateBrick());
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
        items.add(generateBrick());
        items.add(generateHiddenBrick());
        items.add(generateBrick());
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
        items.add(generateHiddenBrick());
        items.add(generateHiddenBrick());
        items.add(generateHiddenBrick());
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
        items.add(generateBrick());
        items.add(generateBrick());
        items.add(generateBrick());
        manager.addAfterItem(generateBrick(), items);

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
        manager.addLast(generateHiddenBrick());
        manager.addLast(generateBrick());

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
        manager.addLast(generateOtherBrick());

        manager.removeAll(TestBrick.class);

        assertEquals(1, manager.getRecyclerViewItems().size());
        assertEquals(1, manager.getDataManagerItems().size());

        assertTrue(observer.isChanged());

        verify(dataSetChangedListener, atLeastOnce()).onDataSetChanged();
    }

    @Test
    public void testRemoveHiddenItems() {
        manager.addLast(generateHiddenBrick());
        manager.addLast(generateHiddenBrick());
        manager.addLast(generateBrick());

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
        BaseBrick brickToReplace = generateHiddenBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToReplace);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.replaceItem(brickToReplace, generateHiddenBrick());

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
        BaseBrick brickToReplace = generateBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToReplace);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        for (int i = 0; i < replaceCount; i++) {
            manager.replaceItem(brickToReplace, generateBrick());
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
        BaseBrick brickToReplace = generateHiddenBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToReplace);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.replaceItem(brickToReplace, generateBrick());

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
        BaseBrick brickToReplace = generateBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToReplace);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.replaceItem(brickToReplace, generateHiddenBrick());

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
        BaseBrick brickToRefresh = generateHiddenBrick();
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
        BaseBrick brickToRefresh = generateBrick();
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
        BaseBrick brickToRefresh = generateHiddenBrick();
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
        BaseBrick brickToRefresh = generateBrick();
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
        manager.refreshItem(generateBrick());

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
        newItems.add(generateBrickWithLayoutId(1));

        manager.addLast(newItems);

        @LayoutRes int layoutRes = 1;
        assertNotNull(manager.brickWithLayout(layoutRes));
    }

    @Test
    public void testBrickWithLayoutInvalidLayout() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(generateBrickWithLayoutId(1));

        manager.addLast(newItems);

        @LayoutRes int layoutRes = 2;
        assertNull(manager.brickWithLayout(layoutRes));
    }

    @Test
    public void testBrickWithPlaceholderLayoutWhenDataIsReady() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(generateBrickWithPlaceholderLayoutId(1, true));

        manager.addLast(newItems);

        @LayoutRes int layoutRes = 1;
        assertNull(manager.brickWithPlaceholderLayout(layoutRes));
    }

    @Test
    public void testBrickWithPlaceholderLayoutWhenDataIsNotReady() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(generateBrickWithPlaceholderLayoutId(1, false));

        manager.addLast(newItems);

        @LayoutRes int layoutRes = 1;
        assertNotNull(manager.brickWithPlaceholderLayout(layoutRes));
    }

    @Test
    public void testBrickWithPlaceholderLayoutInvalidLayoutWhenDataIsNotReady() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(generateBrickWithPlaceholderLayoutId(1, false));

        manager.addLast(newItems);

        @LayoutRes int layoutRes = 2;
        assertNull(manager.brickWithPlaceholderLayout(layoutRes));
    }

    @Test
    public void testBrickWithPlaceholderLayoutInvalidLayoutWhenDataIsReady() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(generateBrickWithPlaceholderLayoutId(1, true));

        manager.addLast(newItems);

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
        manager.smoothScrollToBrick(generateBrick());
    }

    @Test
    public void testAddToTagCache() {
        BaseBrick baseBrick = generateBrick();
        baseBrick.setTag(TAG);
        manager.addLast(baseBrick);

        assertEquals(1, manager.getBricksByTag(TAG).size());
    }

    @Test
    public void testAddMultipleToTagCache() {
        BaseBrick baseBrick = generateBrick();
        baseBrick.setTag(TAG);
        manager.addLast(baseBrick);

        BaseBrick baseBrick2 = generateBrick();
        baseBrick.setTag(TAG);
        manager.addLast(baseBrick2);

        assertEquals(2, manager.getBricksByTag(TAG).size());
    }

    @Test
    public void testAddNullToTagCache() {
        BaseBrick baseBrick = generateBrick();
        baseBrick.setTag(TAG);
        manager.addLast(baseBrick);
        baseBrick.setTag(null);

        assertNull(manager.getBricksByTag(TAG));
    }

    @Test
    public void testRemoveFromTagCache() {
        BaseBrick baseBrick = generateBrick();
        baseBrick.setTag(TAG);
        manager.addLast(baseBrick);

        assertEquals(1, manager.getBricksByTag(TAG).size());

        manager.removeAllByTag(TAG);

        assertNull(manager.getBricksByTag(TAG));
    }

    @Test
    public void testAddToLayoutIdCache() {
        BaseBrick baseBrick = generateBrick();
        manager.addLast(baseBrick);

        assertEquals(STARTING_BRICKS + 1, manager.getBricksByLayoutId(baseBrick.getLayout()).size());
    }

    @Test
    public void testAddMultipleToLayoutIdCache() {
        BaseBrick baseBrick = generateBrick();
        manager.addLast(baseBrick);

        BaseBrick baseBrick2 = generateBrick();
        manager.addLast(baseBrick2);

        assertEquals(STARTING_BRICKS + 2, manager.getBricksByLayoutId(baseBrick.getLayout()).size());
    }

    @Test
    public void testAddNullToLayoutIdCache() {
        BaseBrick baseBrick = generateBrick();
        manager.addLast(baseBrick);

        assertNull(manager.getBricksByLayoutId(-1));
    }

    @Test
    public void testRemoveFromLayoutIdCache() {
        BaseBrick baseBrick = generateBrick();
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
    public void testSafeNotifyItemInserted() {
        // Given
        List<BaseBrick> items = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            items.add(generateBrick());
        }

        // When
        manager.clear();
        manager.addLast(items);


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
            items.add(generateBrick());
        }

        // When
        manager.clear();
        manager.addLast(items);
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

    public BaseBrick generateBrick() {
        return new TestBrick(0);
    }

    public BaseBrick generateBrickWithLayoutId(int layoutId) {
        return new TestBrick(layoutId);
    }

    public BaseBrick generateHiddenBrick() {
        BaseBrick brick = generateBrick();
        brick.setHidden(true);

        return brick;
    }

    public BaseBrick generateBrickWithPlaceholderLayoutId(int placeholderLayoutId, boolean isDataReady) {
        return new TestPlaceholderBrick(placeholderLayoutId, isDataReady);
    }

    public BaseBrick generateOtherBrick() {
        return new TestBrick2();
    }

    public static final class TestBrick extends BaseBrick {
        private final int layoutId;

        private TestBrick(int layoutId) {
            super(new HalfWidthBrickSize(), new ZeroBrickPadding());
            this.layoutId = layoutId;
        }

        @Override
        public void onBindData(BrickViewHolder holder) {

        }

        @Override
        public int getLayout() {
            return layoutId;
        }

        @Override
        public BrickViewHolder createViewHolder(View itemView) {
            return null;
        }
    }

    private static final class TestBrick2 extends BaseBrick {

        private TestBrick2() {
            super(new HalfWidthBrickSize(), new ZeroBrickPadding());
        }

        @Override
        public void onBindData(BrickViewHolder holder) {

        }

        @Override
        public int getLayout() {
            return 0;
        }

        @Override
        public BrickViewHolder createViewHolder(View itemView) {
            return null;
        }
    }

    public static final class TestPlaceholderBrick extends BaseBrick {
        private final int placeholderLayoutId;
        private final boolean isDataReady;

        private TestPlaceholderBrick(int placeholderLayoutId, boolean isDataReady) {
            super(new HalfWidthBrickSize(), new ZeroBrickPadding());
            this.placeholderLayoutId = placeholderLayoutId;
            this.isDataReady = isDataReady;
        }

        @Override
        public void onBindData(BrickViewHolder holder) {

        }

        @Override
        public boolean isDataReady() {
            return isDataReady;
        }

        @Override
        public int getPlaceholderLayout() {
            return placeholderLayoutId;
        }

        @Override
        public int getLayout() {
            return 0;
        }

        @Override
        public BrickViewHolder createViewHolder(View itemView) {
            return null;
        }
    }

    public static class TestAdapterDataObserver extends RecyclerView.AdapterDataObserver {

        private boolean changed = false;

        private int itemRangeChangedPositionStart = -1;
        private int itemRangeChangedItemCount = -1;

        private int itemRangeInsertedPositionStart = -1;
        private int itemRangeInsertedItemCount = -1;

        private int itemRangeRemovedPositionStart = -1;
        private int itemRangeRemovedItemCount = -1;

        public void onChanged() {
            changed = true;
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            itemRangeChangedPositionStart = positionStart;
            itemRangeChangedItemCount = itemCount;
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            itemRangeChangedPositionStart = positionStart;
            itemRangeChangedItemCount = itemCount;
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            itemRangeInsertedPositionStart = positionStart;
            itemRangeInsertedItemCount = itemCount;
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            itemRangeRemovedPositionStart = positionStart;
            itemRangeRemovedItemCount = itemCount;
        }

        public boolean isChanged() {
            return changed;
        }

        public int getItemRangeChangedPositionStart() {
            return itemRangeChangedPositionStart;
        }

        public int getItemRangeChangedItemCount() {
            return itemRangeChangedItemCount;
        }

        public int getItemRangeInsertedPositionStart() {
            return itemRangeInsertedPositionStart;
        }

        public int getItemRangeInsertedItemCount() {
            return itemRangeInsertedItemCount;
        }

        public int getItemRangeRemovedPositionStart() {
            return itemRangeRemovedPositionStart;
        }

        public int getItemRangeRemovedItemCount() {
            return itemRangeRemovedItemCount;
        }


        public void setItemRangeChangedPositionStart(int itemRangeChangedPositionStart) {
            this.itemRangeChangedPositionStart = itemRangeChangedPositionStart;
        }

        public void setItemRangeChangedItemCount(int itemRangeChangedItemCount) {
            this.itemRangeChangedItemCount = itemRangeChangedItemCount;
        }

        public void setItemRangeInsertedPositionStart(int itemRangeInsertedPositionStart) {
            this.itemRangeInsertedPositionStart = itemRangeInsertedPositionStart;
        }

        public void setItemRangeInsertedItemCount(int itemRangeInsertedItemCount) {
            this.itemRangeInsertedItemCount = itemRangeInsertedItemCount;
        }
    }
}
