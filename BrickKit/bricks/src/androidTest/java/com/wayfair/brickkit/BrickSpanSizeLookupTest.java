/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.size.BrickSize;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BrickSpanSizeLookupTest {
    private static final int DEFAULT_SPANS = 1;
    private static final int SPANS = 5;
    private static final int POSITION = 4;

    private BrickDataManager manager;
    private LinkedList<BaseBrick> bricks;
    private BrickSpanSizeLookup brickSpanSizeLookup;

    @Before
    public void setup() {
        manager = mock(BrickDataManager.class);

        bricks = mock(LinkedList.class);

        when(manager.getRecyclerViewItems()).thenReturn(bricks);

        brickSpanSizeLookup = new BrickSpanSizeLookup(InstrumentationRegistry.getTargetContext(), manager);
    }

    @Test
    public void testDefaultSpanSize() {
        when(bricks.get(POSITION)).thenReturn(null);

        assertEquals(DEFAULT_SPANS, brickSpanSizeLookup.getSpanSize(POSITION));
    }

    @Test
    public void testBrickSpanSize() {
        BrickSize brickSize = mock(BrickSize.class);
        when(brickSize.getSpans(any(Context.class))).thenReturn(SPANS);

        BaseBrick brick = mock(BaseBrick.class);
        when(brick.getSpanSize()).thenReturn(brickSize);

        when(bricks.get(POSITION)).thenReturn(brick);

        when(manager.getRecyclerViewItems()).thenReturn(bricks);
        assertEquals(SPANS, brickSpanSizeLookup.getSpanSize(POSITION));
    }
}
