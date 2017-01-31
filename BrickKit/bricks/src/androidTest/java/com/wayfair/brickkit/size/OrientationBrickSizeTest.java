/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OrientationBrickSizeTest {
    private static final int MAX_SPAN_COUNT = 8;
    private static final int LANDSCAPE_SIZE = 7;
    private static final int PORTRAIT_SIZE = 3;

    private TestOrientationBrickSize brickSize;

    @Before
    public void setup() {
        brickSize = new TestOrientationBrickSize(MAX_SPAN_COUNT);
    }

    @Test
    public void testLandscapeTablet() {
        assertEquals(LANDSCAPE_SIZE, brickSize.landscapeTablet());
    }

    @Test
    public void testLandscapePhone() {
        assertEquals(LANDSCAPE_SIZE, brickSize.landscapePhone());
    }

    @Test
    public void testPortraitTablet() {
        assertEquals(PORTRAIT_SIZE, brickSize.portraitTablet());
    }

    @Test
    public void testPortraitPhone() {
        assertEquals(PORTRAIT_SIZE, brickSize.portraitPhone());
    }

    private static final class TestOrientationBrickSize extends OrientationBrickSize {
        private TestOrientationBrickSize(int maxSpanCount) {
            super(maxSpanCount);
        }

        @Override
        protected int landscape() {
            return LANDSCAPE_SIZE;
        }

        @Override
        protected int portrait() {
            return PORTRAIT_SIZE;
        }
    }
}
