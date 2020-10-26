/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.test.R;
import com.wayfair.brickkit.util.BrickTestHelper;
import com.wayfair.brickkit.viewholder.BrickViewHolder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BrickRecyclerAdapterTest {

    private static final int POSITION = 1;
    private static final int TO_POSITION = 2;
    private static final int COUNT = 3;
    private static final Object PAYLOAD = new Object();
    private static final int BRICK_COUNT = 3;
    private static final int LAYOUT = 7;
    private static final int PLACEHOLDER_LAYOUT = 8;
    private BrickRecyclerAdapter adapter;
    private BrickTestHelper.TestAdapterDataObserver observer;
    private LinkedList<BaseBrick> bricks;
    private RecyclerView recyclerView;
    private BrickDataManager dataManager;

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        dataManager = mock(BrickDataManager.class);

        bricks = new LinkedList<>();

        when(dataManager.getRecyclerViewItems()).thenReturn(bricks);

        recyclerView = mock(RecyclerView.class);
        when(recyclerView.isComputingLayout()).thenReturn(false);

        observer = new BrickTestHelper.TestAdapterDataObserver();

        adapter = new BrickRecyclerAdapter(dataManager, recyclerView);
        adapter.registerAdapterDataObserver(observer);
    }

    @Test
    public void testGetRecyclerView() {
        assertEquals(recyclerView, adapter.getRecyclerView());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullRecyclerView() {
        new BrickRecyclerAdapter(mock(BrickDataManager.class), null);
    }

    @Test
    public void testGetBrickDataManager() {
        assertNotNull(adapter.getBrickDataManager());
    }

    @Test
    public void testSafeNotifyDataSetChanged() {
        adapter.safeNotifyDataSetChanged();

        assertTrue(observer.isChanged());
    }

    @Test
    public void testNotifyDataSetChangedRunnable() {
        new BrickRecyclerAdapter.NotifyDataSetChangedRunnable(adapter).run();

        assertTrue(observer.isChanged());
    }

    @Test
    public void testSafeNotifyDataSetChangedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyDataSetChanged();

        assertFalse(observer.isChanged());
    }

    @Test
    public void testSafeNotifyItemChanged() {
        adapter.safeNotifyItemChanged(POSITION);

        assertEquals(POSITION, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());
        assertNull(observer.getItemRangeChangedPayload());
    }


    @Test
    public void testNotifyItemChangedRunnable() {
        new BrickRecyclerAdapter.NotifyItemChangedRunnable(adapter, POSITION).run();

        assertEquals(POSITION, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());
        assertNull(observer.getItemRangeChangedPayload());
    }

    @Test
    public void testSafeNotifyItemChangedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemChanged(POSITION);

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());
        assertNull(observer.getItemRangeChangedPayload());
    }

    @Test
    public void testSafeNotifyItemChangedWithPayload() {
        adapter.safeNotifyItemChanged(POSITION, PAYLOAD);

        assertEquals(POSITION, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());
        assertNotNull(observer.getItemRangeChangedPayload());
    }

    @Test
    public void testNotifyItemChangedWithPayloadRunnable() {
        new BrickRecyclerAdapter.NotifyItemChangedWithPayloadRunnable(adapter, POSITION, PAYLOAD).run();

        assertEquals(POSITION, observer.getItemRangeChangedPositionStart());
        assertEquals(1, observer.getItemRangeChangedItemCount());
        assertNotNull(observer.getItemRangeChangedPayload());
    }

    @Test
    public void testSafeNotifyItemChangedWithPayloadComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemChanged(POSITION, PAYLOAD);

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());
        assertNull(observer.getItemRangeChangedPayload());
    }

    @Test
    public void testSafeNotifyItemInserted() {
        adapter.safeNotifyItemInserted(POSITION);

        assertEquals(POSITION, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());
    }

    @Test
    public void testNotifyItemInsertedRunnable() {
        new BrickRecyclerAdapter.NotifyItemInsertedRunnable(adapter, POSITION).run();

        assertEquals(POSITION, observer.getItemRangeInsertedPositionStart());
        assertEquals(1, observer.getItemRangeInsertedItemCount());
    }

    @Test
    public void testSafeNotifyItemInsertedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemInserted(POSITION);

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());
    }

    @Test
    public void testSafeNotifyItemRangeInserted() {
        adapter.safeNotifyItemRangeInserted(POSITION, COUNT);

        assertEquals(POSITION, observer.getItemRangeInsertedPositionStart());
        assertEquals(COUNT, observer.getItemRangeInsertedItemCount());
    }

    @Test
    public void testNotifyItemRangeInsertedRunnable() {
        new BrickRecyclerAdapter.NotifyItemRangeInsertedRunnable(adapter, POSITION, COUNT).run();

        assertEquals(POSITION, observer.getItemRangeInsertedPositionStart());
        assertEquals(COUNT, observer.getItemRangeInsertedItemCount());
    }

    @Test
    public void testSafeNotifyItemRangeInsertedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemRangeInserted(POSITION, COUNT);

        assertEquals(-1, observer.getItemRangeInsertedPositionStart());
        assertEquals(-1, observer.getItemRangeInsertedItemCount());
    }

    @Test
    public void testSafeNotifyItemMoved() {
        adapter.safeNotifyItemMoved(POSITION, TO_POSITION);

        assertEquals(POSITION, observer.getItemRangeMovedFromPosition());
        assertEquals(TO_POSITION, observer.getItemRangeMovedToPosition());
        assertEquals(1, observer.getItemRangeMovedItemCount());
    }

    @Test
    public void testNotifyItemMovedRunnable() {
        new BrickRecyclerAdapter.NotifyItemMovedRunnable(adapter, POSITION, TO_POSITION).run();

        assertEquals(POSITION, observer.getItemRangeMovedFromPosition());
        assertEquals(TO_POSITION, observer.getItemRangeMovedToPosition());
        assertEquals(1, observer.getItemRangeMovedItemCount());
    }

    @Test
    public void testSafeNotifyItemMovedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemMoved(POSITION, TO_POSITION);

        assertEquals(-1, observer.getItemRangeMovedFromPosition());
        assertEquals(-1, observer.getItemRangeMovedToPosition());
        assertEquals(-1, observer.getItemRangeMovedItemCount());
    }

    @Test
    public void testSafeNotifyItemRangeChangedWithPayload() {
        adapter.safeNotifyItemRangeChanged(POSITION, COUNT, PAYLOAD);

        assertEquals(POSITION, observer.getItemRangeChangedPositionStart());
        assertEquals(COUNT, observer.getItemRangeChangedItemCount());
        assertEquals(PAYLOAD, observer.getItemRangeChangedPayload());
    }

    @Test
    public void testNotifyItemRangeChangedWithPayloadRunnable() {
        new BrickRecyclerAdapter.NotifyItemRangeChangedWithPayloadRunnable(adapter, POSITION, COUNT, PAYLOAD).run();

        assertEquals(POSITION, observer.getItemRangeChangedPositionStart());
        assertEquals(COUNT, observer.getItemRangeChangedItemCount());
        assertEquals(PAYLOAD, observer.getItemRangeChangedPayload());
    }

    @Test
    public void testSafeNotifyItemRangeChangedWithPayloadComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemRangeChanged(POSITION, COUNT, PAYLOAD);

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());
        assertNull(observer.getItemRangeChangedPayload());
    }

    @Test
    public void testSafeNotifyItemRangeChanged() {
        adapter.safeNotifyItemRangeChanged(POSITION, COUNT);

        assertEquals(POSITION, observer.getItemRangeChangedPositionStart());
        assertEquals(COUNT, observer.getItemRangeChangedItemCount());
        assertNull(observer.getItemRangeChangedPayload());
    }

    @Test
    public void testNotifyItemRangeChangedRunnable() {
        new BrickRecyclerAdapter.NotifyItemRangeChangedRunnable(adapter, POSITION, COUNT).run();

        assertEquals(POSITION, observer.getItemRangeChangedPositionStart());
        assertEquals(COUNT, observer.getItemRangeChangedItemCount());
        assertNull(observer.getItemRangeChangedPayload());
    }


    @Test
    public void testSafeNotifyItemRangeChangedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemRangeChanged(POSITION, COUNT);

        assertEquals(-1, observer.getItemRangeChangedPositionStart());
        assertEquals(-1, observer.getItemRangeChangedItemCount());
        assertNull(observer.getItemRangeChangedPayload());
    }

    @Test
    public void testSafeNotifyItemRemoved() {
        adapter.safeNotifyItemRemoved(POSITION);

        assertEquals(POSITION, observer.getItemRangeRemovedPositionStart());
        assertEquals(1, observer.getItemRangeRemovedItemCount());
    }

    @Test
    public void testNotifyItemRemovedRunnable() {
        new BrickRecyclerAdapter.NotifyItemRemovedRunnable(adapter, POSITION).run();

        assertEquals(POSITION, observer.getItemRangeRemovedPositionStart());
        assertEquals(1, observer.getItemRangeRemovedItemCount());
    }

    @Test
    public void testSafeNotifyItemRemovedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemRemoved(POSITION);

        assertEquals(-1, observer.getItemRangeRemovedPositionStart());
        assertEquals(-1, observer.getItemRangeRemovedItemCount());
    }

    @Test
    public void testSafeNotifyItemRangeRemoved() {
        adapter.safeNotifyItemRangeRemoved(POSITION, COUNT);

        assertEquals(POSITION, observer.getItemRangeRemovedPositionStart());
        assertEquals(COUNT, observer.getItemRangeRemovedItemCount());
    }

    @Test
    public void testNotifyItemRangeRemovedRunnable() {
        new BrickRecyclerAdapter.NotifyItemRangeRemovedRunnable(adapter, POSITION, COUNT).run();

        assertEquals(POSITION, observer.getItemRangeRemovedPositionStart());
        assertEquals(COUNT, observer.getItemRangeRemovedItemCount());
    }

    @Test
    public void testSafeNotifyItemRangeRemovedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);

        adapter.safeNotifyItemRangeRemoved(POSITION, COUNT);

        assertEquals(-1, observer.getItemRangeRemovedPositionStart());
        assertEquals(-1, observer.getItemRangeRemovedItemCount());
    }

    @Test
    public void testOnCreateViewHolder() {
        BaseBrick brick = mock(BaseBrick.class);
        when(brick.getLayout()).thenReturn(R.layout.test_layout);

        when(dataManager.brickWithLayout(anyInt())).thenReturn(brick);

        adapter.onCreateViewHolder(new LinearLayout(InstrumentationRegistry.getTargetContext()), brick.getLayout());

        verify(brick).createViewHolder(any(View.class));
    }

    @Test
    public void testOnBindViewHolderNullBrick() {
        OnReachedItemAtPosition listener = mock(OnReachedItemAtPosition.class);
        adapter.setOnReachedItemAtPosition(listener);

        when(dataManager.brickAtPosition(0)).thenReturn(null);

        BrickViewHolder holder = mock(BrickViewHolder.class);

        adapter.onBindViewHolder(holder, 0);

        verify(listener, never()).bindingItemAtPosition(0);
    }

    @Test
    public void testOnBindViewHolderNullBindListenerWhenDataIsReady() {
        BaseBrick brick = mock(BaseBrick.class);

        when(dataManager.brickAtPosition(0)).thenReturn(brick);
        when(brick.isDataReady()).thenReturn(true);

        BrickViewHolder holder = mock(BrickViewHolder.class);

        adapter.onBindViewHolder(holder, 0);

        verify(brick).onBindData(holder);
    }

    @Test
    public void testOnBindViewHolderNullBindListenerWhenDataIsNotReady() {
        BaseBrick brick = mock(BaseBrick.class);

        when(dataManager.brickAtPosition(0)).thenReturn(brick);
        when(brick.isDataReady()).thenReturn(false);

        BrickViewHolder holder = mock(BrickViewHolder.class);

        adapter.onBindViewHolder(holder, 0);

        verify(brick, never()).onBindData(holder);
    }

    @Test
    public void testOnBindViewHolderWhenDataIsNotReady() {
        BaseBrick brick = mock(BaseBrick.class);

        OnReachedItemAtPosition listener = mock(OnReachedItemAtPosition.class);
        adapter.setOnReachedItemAtPosition(listener);

        when(dataManager.brickAtPosition(0)).thenReturn(brick);
        when(brick.isDataReady()).thenReturn(false);

        BrickViewHolder holder = mock(BrickViewHolder.class);

        adapter.onBindViewHolder(holder, 0);

        verify(brick, never()).onBindData(holder);
        verify(listener).bindingItemAtPosition(0);
    }

    @Test
    public void testOnBindViewHolderWhenDataIsReady() {
        BaseBrick brick = mock(BaseBrick.class);

        OnReachedItemAtPosition listener = mock(OnReachedItemAtPosition.class);
        adapter.setOnReachedItemAtPosition(listener);

        when(dataManager.brickAtPosition(0)).thenReturn(brick);
        when(brick.isDataReady()).thenReturn(true);

        BrickViewHolder holder = mock(BrickViewHolder.class);

        adapter.onBindViewHolder(holder, 0);

        verify(brick).onBindData(holder);
        verify(listener).bindingItemAtPosition(0);
    }

    @Test
    public void testOnViewAttachedToWindow() {
        BrickViewHolder brickViewHolder = mock(BrickViewHolder.class);

        adapter.onViewAttachedToWindow(brickViewHolder);

        verify(brickViewHolder).onViewAttachedToWindow();
    }

    @Test
    public void testOnViewDetachedFromWindow() {
        BrickViewHolder brickViewHolder = mock(BrickViewHolder.class);

        adapter.onViewDetachedFromWindow(brickViewHolder);

        verify(brickViewHolder).releaseViewsOnDetach();
    }

    @Test
    public void testGetItemCount() {
        for (int i = 0; i < BRICK_COUNT; i++) {
            bricks.addFirst(mock(BaseBrick.class));
        }

        assertEquals(BRICK_COUNT, adapter.getItemCount());
    }

    @Test
    public void testGetItemViewTypeWhenDataIsReady() {
        BaseBrick brick = mock(BaseBrick.class);
        when(brick.getLayout()).thenReturn(LAYOUT);
        when(brick.isDataReady()).thenReturn(true);

        when(dataManager.brickAtPosition(0)).thenReturn(brick);

        assertEquals(LAYOUT, adapter.getItemViewType(0));
    }

    @Test
    public void testGetItemViewTypeWhenDataIsNotReady() {
        BaseBrick brick = mock(BaseBrick.class);
        when(brick.getPlaceholderLayout()).thenReturn(PLACEHOLDER_LAYOUT);
        when(brick.isDataReady()).thenReturn(false);

        when(dataManager.brickAtPosition(0)).thenReturn(brick);

        assertEquals(PLACEHOLDER_LAYOUT, adapter.getItemViewType(0));
    }

    @Test
    public void testGetItemViewTypeInvalidPosition() {
        when(dataManager.brickAtPosition(0)).thenReturn(null);

        assertEquals(0, adapter.getItemViewType(0));
    }

    @Test
    public void testGet() {
        bricks.addFirst(mock(BaseBrick.class));

        assertNotNull(adapter.get(0));
    }

    @Test
    public void testIndexOf() {
        BaseBrick brick = mock(BaseBrick.class);

        bricks.addFirst(brick);

        assertEquals(0, adapter.indexOf(brick));
    }
}
