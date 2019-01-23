/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SimpleBrickPaddingTest {
    private static final int PADDING = 2;

    private SimpleBrickPadding brickPadding;

    @Before
    public void setup() {
        brickPadding = new SimpleBrickPadding(PADDING);
    }

    @Test
    public void testGetInnerLeftPadding() {
        assertEquals(PADDING, brickPadding.getInnerLeftPadding());
    }

    @Test
    public void testGetInnerTopPadding() {
        assertEquals(PADDING, brickPadding.getInnerTopPadding());
    }

    @Test
    public void testGetInnerRightPadding() {
        assertEquals(PADDING, brickPadding.getInnerRightPadding());
    }

    @Test
    public void testGetInnerBottomPadding() {
        assertEquals(PADDING, brickPadding.getInnerBottomPadding());
    }

    @Test
    public void testGetOuterLeftPadding() {
        assertEquals(PADDING, brickPadding.getOuterLeftPadding());
    }

    @Test
    public void testGetOuterTopPadding() {
        assertEquals(PADDING, brickPadding.getOuterTopPadding());
    }

    @Test
    public void testGetOuterRightPadding() {
        assertEquals(PADDING, brickPadding.getOuterRightPadding());
    }

    @Test
    public void testGetOuterBottomPadding() {
        assertEquals(PADDING, brickPadding.getOuterBottomPadding());
    }
}
