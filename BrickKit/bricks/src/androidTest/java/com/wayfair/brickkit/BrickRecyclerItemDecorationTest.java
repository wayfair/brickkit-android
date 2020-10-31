/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;

import androidx.recyclerview.widget.RecyclerView;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BrickRecyclerItemDecorationTest {

    private static final int INNER_LEFT = 1;
    private static final int INNER_TOP = 2;
    private static final int INNER_RIGHT = 3;
    private static final int INNER_BOTTOM = 4;
    private static final int OUTER_LEFT = 5;
    private static final int OUTER_TOP = 6;
    private static final int OUTER_RIGHT = 7;
    private static final int OUTER_BOTTOM = 8;
    private static final int SINGLE_COLUMN = BrickDataManager.SPAN_COUNT;
    private static final int MULTI_COLUMN = BrickDataManager.SPAN_COUNT - 1;

    private BrickRecyclerItemDecoration itemDecoration;
    private RecyclerView parent;
    private Rect outRect;
    private View view;
    private RecyclerView.State state;
    @Mock
    private LinkedList<BaseBrick> bricks;
    private BaseBrick brick;
    private BrickSize brickSize;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        view = mock(View.class);

        state = mock(RecyclerView.State.class);

        parent = mock(RecyclerView.class);
        when(parent.getChildAdapterPosition(any(View.class))).thenReturn(0);

        BrickDataManager brickDataManager = mock(BrickDataManager.class);
        when(brickDataManager.getRecyclerViewItems()).thenReturn(bricks);

        BrickRecyclerAdapter adapter = mock(BrickRecyclerAdapter.class);
        when(parent.getAdapter()).thenReturn(adapter);
        when(adapter.getBrickDataManager()).thenReturn(brickDataManager);
        itemDecoration = new BrickRecyclerItemDecoration(brickDataManager);

        outRect = new Rect();

        brick = mock(BaseBrick.class);

        BrickPadding brickPadding = mock(BrickPadding.class);
        when(brickPadding.getInnerLeftPadding()).thenReturn(INNER_LEFT);
        when(brickPadding.getInnerTopPadding()).thenReturn(INNER_TOP);
        when(brickPadding.getInnerRightPadding()).thenReturn(INNER_RIGHT);
        when(brickPadding.getInnerBottomPadding()).thenReturn(INNER_BOTTOM);
        when(brickPadding.getOuterLeftPadding()).thenReturn(OUTER_LEFT);
        when(brickPadding.getOuterTopPadding()).thenReturn(OUTER_TOP);
        when(brickPadding.getOuterRightPadding()).thenReturn(OUTER_RIGHT);
        when(brickPadding.getOuterBottomPadding()).thenReturn(OUTER_BOTTOM);
        when(brick.getPadding()).thenReturn(brickPadding);

        brickSize = mock(BrickSize.class);
        when(brick.getSpanSize()).thenReturn(brickSize);

        when(parent.getChildAdapterPosition(any(View.class))).thenReturn(0);
        when(bricks.get(0)).thenReturn(brick);
    }

    @Test
    public void testInvalidPosition() {
        when(parent.getChildAdapterPosition(any(View.class))).thenReturn(-1);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(0, outRect.left);
        assertEquals(0, outRect.top);
        assertEquals(0, outRect.right);
        assertEquals(0, outRect.bottom);
    }

    @Test
    public void testInvalidNullItem() {
        when(bricks.get(0)).thenReturn(null);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(0, outRect.left);
        assertEquals(0, outRect.top);
        assertEquals(0, outRect.right);
        assertEquals(0, outRect.bottom);
    }

    @Test
    public void testSingleColumnFirstRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(SINGLE_COLUMN);
        when(brick.isInFirstRow()).thenReturn(true);
        when(brick.isInLastRow()).thenReturn(false);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(OUTER_LEFT, outRect.left);
        assertEquals(OUTER_TOP, outRect.top);
        assertEquals(OUTER_RIGHT, outRect.right);
        assertEquals(INNER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testSingleColumnLastRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(SINGLE_COLUMN);
        when(brick.isInFirstRow()).thenReturn(false);
        when(brick.isInLastRow()).thenReturn(true);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(OUTER_LEFT, outRect.left);
        assertEquals(INNER_TOP, outRect.top);
        assertEquals(OUTER_RIGHT, outRect.right);
        assertEquals(OUTER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testSingleColumnFirstRowLastRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(SINGLE_COLUMN);
        when(brick.isInFirstRow()).thenReturn(true);
        when(brick.isInLastRow()).thenReturn(true);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(OUTER_LEFT, outRect.left);
        assertEquals(OUTER_TOP, outRect.top);
        assertEquals(OUTER_RIGHT, outRect.right);
        assertEquals(OUTER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testSingleColumn() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(SINGLE_COLUMN);
        when(brick.isInFirstRow()).thenReturn(false);
        when(brick.isInLastRow()).thenReturn(false);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(OUTER_LEFT, outRect.left);
        assertEquals(INNER_TOP, outRect.top);
        assertEquals(OUTER_RIGHT, outRect.right);
        assertEquals(INNER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnLeftColumnFirstRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(true);
        when(brick.isInLastRow()).thenReturn(false);
        when(brick.isOnLeftWall()).thenReturn(true);
        when(brick.isOnRightWall()).thenReturn(false);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(OUTER_LEFT, outRect.left);
        assertEquals(OUTER_TOP, outRect.top);
        assertEquals(INNER_RIGHT, outRect.right);
        assertEquals(INNER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnMiddleColumnFirstRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(true);
        when(brick.isInLastRow()).thenReturn(false);
        when(brick.isOnLeftWall()).thenReturn(false);
        when(brick.isOnRightWall()).thenReturn(false);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(INNER_LEFT, outRect.left);
        assertEquals(OUTER_TOP, outRect.top);
        assertEquals(INNER_RIGHT, outRect.right);
        assertEquals(INNER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnRightColumnFirstRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(true);
        when(brick.isInLastRow()).thenReturn(false);
        when(brick.isOnLeftWall()).thenReturn(false);
        when(brick.isOnRightWall()).thenReturn(true);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(INNER_LEFT, outRect.left);
        assertEquals(OUTER_TOP, outRect.top);
        assertEquals(OUTER_RIGHT, outRect.right);
        assertEquals(INNER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnLeftColumnLastRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(false);
        when(brick.isInLastRow()).thenReturn(true);
        when(brick.isOnLeftWall()).thenReturn(true);
        when(brick.isOnRightWall()).thenReturn(false);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(OUTER_LEFT, outRect.left);
        assertEquals(INNER_TOP, outRect.top);
        assertEquals(INNER_RIGHT, outRect.right);
        assertEquals(OUTER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnMiddleColumnLastRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(false);
        when(brick.isInLastRow()).thenReturn(true);
        when(brick.isOnLeftWall()).thenReturn(false);
        when(brick.isOnRightWall()).thenReturn(false);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(INNER_LEFT, outRect.left);
        assertEquals(INNER_TOP, outRect.top);
        assertEquals(INNER_RIGHT, outRect.right);
        assertEquals(OUTER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnRightColumnLastRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(false);
        when(brick.isInLastRow()).thenReturn(true);
        when(brick.isOnLeftWall()).thenReturn(false);
        when(brick.isOnRightWall()).thenReturn(true);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(INNER_LEFT, outRect.left);
        assertEquals(INNER_TOP, outRect.top);
        assertEquals(OUTER_RIGHT, outRect.right);
        assertEquals(OUTER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnLeftColumnFirstRowLastRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(true);
        when(brick.isInLastRow()).thenReturn(true);
        when(brick.isOnLeftWall()).thenReturn(true);
        when(brick.isOnRightWall()).thenReturn(false);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(OUTER_LEFT, outRect.left);
        assertEquals(OUTER_TOP, outRect.top);
        assertEquals(INNER_RIGHT, outRect.right);
        assertEquals(OUTER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnMiddleColumnFirstRowLastRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(true);
        when(brick.isInLastRow()).thenReturn(true);
        when(brick.isOnLeftWall()).thenReturn(false);
        when(brick.isOnRightWall()).thenReturn(false);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(INNER_LEFT, outRect.left);
        assertEquals(OUTER_TOP, outRect.top);
        assertEquals(INNER_RIGHT, outRect.right);
        assertEquals(OUTER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnRightColumnFirstRowLastRow() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(true);
        when(brick.isInLastRow()).thenReturn(true);
        when(brick.isOnLeftWall()).thenReturn(false);
        when(brick.isOnRightWall()).thenReturn(true);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(INNER_LEFT, outRect.left);
        assertEquals(OUTER_TOP, outRect.top);
        assertEquals(OUTER_RIGHT, outRect.right);
        assertEquals(OUTER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnLeftColumn() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(false);
        when(brick.isInLastRow()).thenReturn(false);
        when(brick.isOnLeftWall()).thenReturn(true);
        when(brick.isOnRightWall()).thenReturn(false);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(OUTER_LEFT, outRect.left);
        assertEquals(INNER_TOP, outRect.top);
        assertEquals(INNER_RIGHT, outRect.right);
        assertEquals(INNER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnMiddleColumn() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(false);
        when(brick.isInLastRow()).thenReturn(false);
        when(brick.isOnLeftWall()).thenReturn(false);
        when(brick.isOnRightWall()).thenReturn(false);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(INNER_LEFT, outRect.left);
        assertEquals(INNER_TOP, outRect.top);
        assertEquals(INNER_RIGHT, outRect.right);
        assertEquals(INNER_BOTTOM, outRect.bottom);
    }

    @Test
    public void testMultiColumnRightColumn() {
        when(brickSize.getSpans(nullable(Context.class))).thenReturn(MULTI_COLUMN);
        when(brick.isInFirstRow()).thenReturn(false);
        when(brick.isInLastRow()).thenReturn(false);
        when(brick.isOnLeftWall()).thenReturn(false);
        when(brick.isOnRightWall()).thenReturn(true);

        itemDecoration.getItemOffsets(outRect, view, parent, state);

        assertEquals(INNER_LEFT, outRect.left);
        assertEquals(INNER_TOP, outRect.top);
        assertEquals(OUTER_RIGHT, outRect.right);
        assertEquals(INNER_BOTTOM, outRect.bottom);
    }
}
