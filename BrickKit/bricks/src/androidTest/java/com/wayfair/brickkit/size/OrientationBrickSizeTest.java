/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OrientationBrickSizeTest {
    private static final int LANDSCAPE_SIZE = 7;
    private static final int PORTRAIT_SIZE = 3;

    private TestOrientationBrickSize brickSize;

    @Before
    public void setup() {
        brickSize = new TestOrientationBrickSize();
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
