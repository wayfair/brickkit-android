/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding;

import android.graphics.Rect;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class BrickPaddingTest {
    private static final int INNER_LEFT = 1;
    private static final int INNER_TOP = 2;
    private static final int INNER_RIGHT = 3;
    private static final int INNER_BOTTOM = 4;
    private static final int OUTER_LEFT = 5;
    private static final int OUTER_TOP = 6;
    private static final int OUTER_RIGHT = 7;
    private static final int OUTER_BOTTOM = 8;

    private BrickPadding brickPadding;

    @Before
    public void setup() {
        brickPadding = new BrickPadding(
                new Rect(INNER_LEFT, INNER_TOP, INNER_RIGHT, INNER_BOTTOM),
                new Rect(OUTER_LEFT, OUTER_TOP, OUTER_RIGHT, OUTER_BOTTOM)
        );
    }

    @Test
    public void testGetInnerLeftPadding() {
        assertEquals(INNER_LEFT, brickPadding.getInnerLeftPadding());
    }

    @Test
    public void testGetInnerTopPadding() {
        assertEquals(INNER_TOP, brickPadding.getInnerTopPadding());
    }

    @Test
    public void testGetInnerRightPadding() {
        assertEquals(INNER_RIGHT, brickPadding.getInnerRightPadding());
    }

    @Test
    public void testGetInnerBottomPadding() {
        assertEquals(INNER_BOTTOM, brickPadding.getInnerBottomPadding());
    }

    @Test
    public void testGetOuterLeftPadding() {
        assertEquals(OUTER_LEFT, brickPadding.getOuterLeftPadding());
    }

    @Test
    public void testGetOuterTopPadding() {
        assertEquals(OUTER_TOP, brickPadding.getOuterTopPadding());
    }

    @Test
    public void testGetOuterRightPadding() {
        assertEquals(OUTER_RIGHT, brickPadding.getOuterRightPadding());
    }

    @Test
    public void testGetOuterBottomPadding() {
        assertEquals(OUTER_BOTTOM, brickPadding.getOuterBottomPadding());
    }
}
