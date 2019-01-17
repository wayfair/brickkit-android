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
public class InnerOuterBrickPaddingTest {
    private static final int INNER_PADDING = 1;
    private static final int OUTER_PADDING = 2;

    private InnerOuterBrickPadding brickPadding;

    @Before
    public void setup() {
        brickPadding = new InnerOuterBrickPadding(INNER_PADDING, OUTER_PADDING);
    }

    @Test
    public void testGetInnerLeftPadding() {
        assertEquals(INNER_PADDING, brickPadding.getInnerLeftPadding());
    }

    @Test
    public void testGetInnerTopPadding() {
        assertEquals(INNER_PADDING, brickPadding.getInnerTopPadding());
    }

    @Test
    public void testGetInnerRightPadding() {
        assertEquals(INNER_PADDING, brickPadding.getInnerRightPadding());
    }

    @Test
    public void testGetInnerBottomPadding() {
        assertEquals(INNER_PADDING, brickPadding.getInnerBottomPadding());
    }

    @Test
    public void testGetOuterLeftPadding() {
        assertEquals(OUTER_PADDING, brickPadding.getOuterLeftPadding());
    }

    @Test
    public void testGetOuterTopPadding() {
        assertEquals(OUTER_PADDING, brickPadding.getOuterTopPadding());
    }

    @Test
    public void testGetOuterRightPadding() {
        assertEquals(OUTER_PADDING, brickPadding.getOuterRightPadding());
    }

    @Test
    public void testGetOuterBottomPadding() {
        assertEquals(OUTER_PADDING, brickPadding.getOuterBottomPadding());
    }
}
