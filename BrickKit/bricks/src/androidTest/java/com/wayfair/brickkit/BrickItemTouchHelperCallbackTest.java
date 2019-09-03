/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.content.Context;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.size.BrickSize;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BrickItemTouchHelperCallbackTest {
    private static final int DIRECTION = ItemTouchHelper.LEFT;
    private static final int MAX_SPANS = 5;
    private static final int NOT_MAX_SPANS = 6;
    private static final int SWIPE_FULL_WIDTH = 12336;
    private static final int DRAG_FULL_WIDTH = 196611;
    private static final int DRAG_NOT_FULL_WIDTH = 983055;

    private BrickDataManager dataManager;
    private RecyclerView.ViewHolder viewHolder;
    private BaseBrick brick;
    private BrickItemTouchHelperCallback callback;
    private RecyclerView.ViewHolder target;
    private BaseBrick targetBrick;
    private RecyclerView recyclerView;
    private BrickSize brickSize;

    @Before
    public void setup() {
        brickSize = mock(BrickSize.class);

        brick = mock(BaseBrick.class);
        when(brick.getSpanSize()).thenReturn(brickSize);

        targetBrick = mock(BaseBrick.class);

        dataManager = mock(BrickDataManager.class);
        when(dataManager.getDragAndDrop()).thenReturn(false);
        when(dataManager.getSwipeToDismiss()).thenReturn(false);
        when(dataManager.brickAtPosition(anyInt())).thenReturn(brick, targetBrick);
        when(dataManager.getMaxSpanCount()).thenReturn(MAX_SPANS);

        callback = new BrickItemTouchHelperCallback(dataManager);

        viewHolder = mock(RecyclerView.ViewHolder.class);
        target = mock(RecyclerView.ViewHolder.class);

        recyclerView = mock(RecyclerView.class);
        when(recyclerView.getLayoutManager()).thenReturn(mock(GridLayoutManager.class));
    }

    @Test
    public void testGetMovementFlagsNeitherEnabled() {
        assertEquals(0, callback.getMovementFlags(recyclerView, viewHolder));
    }

    @Test
    public void testGetMovementFlagsWrongLayoutManager() {
        when(dataManager.getDragAndDrop()).thenReturn(true);

        when(recyclerView.getLayoutManager()).thenReturn(mock(LinearLayoutManager.class));

        assertEquals(0, callback.getMovementFlags(recyclerView, viewHolder));
    }

    @Test
    public void testGetMovementFlagsSwipeFullWidth() {
        when(dataManager.getSwipeToDismiss()).thenReturn(true);

        when(brickSize.getSpans(any(Context.class))).thenReturn(MAX_SPANS);
        assertEquals(SWIPE_FULL_WIDTH, callback.getMovementFlags(recyclerView, viewHolder));
    }

    @Test
    public void testGetMovementFlagsSwipeNotFullWidth() {
        when(dataManager.getSwipeToDismiss()).thenReturn(true);

        when(brickSize.getSpans(any(Context.class))).thenReturn(NOT_MAX_SPANS);

        assertEquals(0, callback.getMovementFlags(recyclerView, viewHolder));
    }

    @Test
    public void testGetMovementFlagsDragFullWidth() {
        when(dataManager.getDragAndDrop()).thenReturn(true);

        when(brickSize.getSpans(any(Context.class))).thenReturn(MAX_SPANS);

        assertEquals(DRAG_FULL_WIDTH, callback.getMovementFlags(recyclerView, viewHolder));
    }

    @Test
    public void testGetMovementFlagsDragNotFullWidth() {
        when(dataManager.getDragAndDrop()).thenReturn(true);

        when(brickSize.getSpans(any(Context.class))).thenReturn(NOT_MAX_SPANS);

        assertEquals(DRAG_NOT_FULL_WIDTH, callback.getMovementFlags(recyclerView, viewHolder));
    }

    @Test
    public void onMove() {
        assertTrue(callback.onMove(recyclerView, viewHolder, target));

        verify(dataManager).moveItem(brick, targetBrick);
        verify(brick).movedTo(RecyclerView.NO_POSITION);
    }

    @Test
    public void testOnSwiped() {
        callback.onSwiped(viewHolder, DIRECTION);

        verify(dataManager).removeItem(brick);
        verify(brick).dismissed(DIRECTION);
    }
}
