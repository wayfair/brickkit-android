/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding;

import android.graphics.Rect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RectBrickPaddingTest {
    private static final int LEFT = 1;
    private static final int TOP = 2;
    private static final int RIGHT = 3;
    private static final int BOTTOM = 4;
    private static final Rect PADDING_RECT = new Rect(LEFT, TOP, RIGHT, BOTTOM);

    private RectBrickPadding brickPadding;

    @Before
    public void setup() {
        brickPadding = new RectBrickPadding(PADDING_RECT);
    }

    @Test
    public void testGetInnerLeftPadding() {
        assertEquals(LEFT, brickPadding.getInnerLeftPadding());
    }

    @Test
    public void testGetInnerTopPadding() {
        assertEquals(TOP, brickPadding.getInnerTopPadding());
    }

    @Test
    public void testGetInnerRightPadding() {
        assertEquals(RIGHT, brickPadding.getInnerRightPadding());
    }

    @Test
    public void testGetInnerBottomPadding() {
        assertEquals(BOTTOM, brickPadding.getInnerBottomPadding());
    }

    @Test
    public void testGetOuterLeftPadding() {
        assertEquals(LEFT, brickPadding.getOuterLeftPadding());
    }

    @Test
    public void testGetOuterTopPadding() {
        assertEquals(TOP, brickPadding.getOuterTopPadding());
    }

    @Test
    public void testGetOuterRightPadding() {
        assertEquals(RIGHT, brickPadding.getOuterRightPadding());
    }

    @Test
    public void testGetOuterBottomPadding() {
        assertEquals(BOTTOM, brickPadding.getOuterBottomPadding());
    }
}
