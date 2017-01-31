/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wayfair.brickkit.behavior.BrickBehavior;
import com.wayfair.brickkit.behavior.StickyFooterBehavior;
import com.wayfair.brickkit.behavior.StickyHeaderBehavior;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.util.BrickTestHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class BrickDataManagerTest {
    private static final int MAX_SPANS = 8;

    private BrickDataManager manager;
    private BrickTestHelper.TestAdapterDataObserver observer;
    private BrickBehavior behavior;
    private BrickTestHelper brickTestHelper;
    private Context context;

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        context = InstrumentationRegistry.getTargetContext();
        manager = new BrickDataManager(MAX_SPANS);
        manager.addBehavior(mock(StickyHeaderBehavior.class));
        View parentView = mock(View.class);
        manager.setRecyclerView(context, new RecyclerView(context), GridLayoutManager.VERTICAL, false, parentView);
        brickTestHelper = new BrickTestHelper(context);

        manager.addLast(brickTestHelper.generateBrick());
        manager.addLast(brickTestHelper.generateBrick());
        manager.addLast(brickTestHelper.generateBrick());
        manager.addLast(brickTestHelper.generateBrick());

        behavior = mock(StickyFooterBehavior.class);

        manager.addBehavior(behavior);

        observer = new BrickTestHelper.TestAdapterDataObserver();
        manager.getBrickRecyclerAdapter().registerAdapterDataObserver(observer);
    }

    @Test
    public void testGetDragAndDrop() {
        manager.setDragAndDrop(false);

        assertFalse(manager.getDragAndDrop());

        manager.setDragAndDrop(true);

        assertTrue(manager.getDragAndDrop());
    }

    @Test
    public void testGetSwipeToDismiss() {
        manager.setSwipeToDismiss(false);

        assertFalse(manager.getSwipeToDismiss());

        manager.setSwipeToDismiss(true);

        assertTrue(manager.getSwipeToDismiss());
    }

    @Test
    public void testGetMaxSpanCount() {
        assertEquals(MAX_SPANS, manager.getMaxSpanCount());
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

        verify(behavior, atLeastOnce()).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior, never()).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior, never()).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
    }

    @Test
    public void testAddFirstVisibleHorizontal() {
        context = InstrumentationRegistry.getTargetContext();
        manager = new BrickDataManager(MAX_SPANS);
        manager.addBehavior(mock(BrickBehavior.class));
        View parentView = mock(View.class);
        manager.setRecyclerView(context, new RecyclerView(context), GridLayoutManager.HORIZONTAL, false, parentView);
        brickTestHelper = new BrickTestHelper(context);

        manager.addLast(brickTestHelper.generateBrick());
        manager.addLast(brickTestHelper.generateBrick());
        manager.addLast(brickTestHelper.generateBrick());
        manager.addLast(brickTestHelper.generateBrick());

        behavior = mock(StickyHeaderBehavior.class);

        manager.addBehavior(behavior);

        observer = new BrickTestHelper.TestAdapterDataObserver();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior, never()).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior, never()).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior, never()).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior, never()).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
    }

    @Test
    public void testRemoveAllTestBrick() {
        manager.addLast(brickTestHelper.generateOtherBrick());

        manager.removeAll(BrickTestHelper.TestBrick.class);

        assertEquals(1, manager.getRecyclerViewItems().size());
        assertEquals(1, manager.getDataManagerItems().size());

        assertTrue(observer.isChanged());

        verify(behavior, atLeastOnce()).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
    }

    @Test
    public void testClear() {
        manager.clear();

        assertEquals(0, manager.getRecyclerViewItems().size());
        assertEquals(0, manager.getDataManagerItems().size());

        assertEquals(0, observer.getItemRangeRemovedPositionStart());
        assertEquals(4, observer.getItemRangeRemovedItemCount());

        verify(behavior).onDataSetChanged();
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

        verify(behavior, never()).onDataSetChanged();
    }

    @Test
    public void testReplaceItemBothVisible() {
        BaseBrick brickToReplace = brickTestHelper.generateBrick();
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), brickToReplace);

        observer.setItemRangeInsertedPositionStart(-1);
        observer.setItemRangeInsertedItemCount(-1);
        observer.setItemRangeChangedPositionStart(-1);
        observer.setItemRangeChangedItemCount(-1);

        manager.replaceItem(brickToReplace, brickTestHelper.generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());

        assertEquals(1, observer.getItemRangeChangedPositionStart());
        assertEquals(4, observer.getItemRangeChangedItemCount());

        assertEquals(-1, observer.getItemRangeRemovedPositionStart());
        assertEquals(-1, observer.getItemRangeRemovedItemCount());

        verify(behavior, atLeastOnce()).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior, atLeastOnce()).onDataSetChanged();
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

        verify(behavior, never()).onDataSetChanged();
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

        verify(behavior).onDataSetChanged();
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

        verify(behavior, atLeastOnce()).onDataSetChanged();
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

        verify(behavior, atLeastOnce()).onDataSetChanged();
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

        verify(behavior).detachFromRecyclerView();
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
    public void testBrickAtPosition() {
        assertNotNull(manager.brickAtPosition(manager.getDataManagerItems().size() - 1));
        assertNull(manager.brickAtPosition(manager.getDataManagerItems().size()));
        assertNull(manager.brickAtPosition(-1));
    }

}
