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
public class SimpleBrickSizeTest {
    private static final int SIZE = 7;
    private TestSimpleBrickSize brickSize;

    @Before
    public void setup() {
        brickSize = new TestSimpleBrickSize();
    }

    @Test
    public void testLandscapeTablet() {
        assertEquals(SIZE, brickSize.landscapeTablet());
    }

    @Test
    public void testLandscapePhone() {
        assertEquals(SIZE, brickSize.landscapePhone());
    }

    @Test
    public void testPortraitTablet() {
        assertEquals(SIZE, brickSize.portraitTablet());
    }

    @Test
    public void testPortraitPhone() {
        assertEquals(SIZE, brickSize.portraitPhone());
    }

    private static final class TestSimpleBrickSize extends SimpleBrickSize {
        @Override
        protected int size() {
            return SIZE;
        }
    }
}
