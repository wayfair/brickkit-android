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
public class DeviceBrickSizeTest {
    private static final int MAX_SPAN_COUNT = 8;
    private static final int TABLET_SIZE = 7;
    private static final int PHONE_SIZE = 3;

    private TestDeviceBrickSize brickSize;

    @Before
    public void setup() {
        brickSize = new TestDeviceBrickSize(MAX_SPAN_COUNT);
    }

    @Test
    public void testLandscapeTablet() {
        assertEquals(TABLET_SIZE, brickSize.landscapeTablet());
    }

    @Test
    public void testLandscapePhone() {
        assertEquals(PHONE_SIZE, brickSize.landscapePhone());
    }

    @Test
    public void testPortraitTablet() {
        assertEquals(TABLET_SIZE, brickSize.portraitTablet());
    }

    @Test
    public void testPortraitPhone() {
        assertEquals(PHONE_SIZE, brickSize.portraitPhone());
    }

    private static final class TestDeviceBrickSize extends DeviceBrickSize {
        private TestDeviceBrickSize(int maxSpanCount) {
            super(maxSpanCount);
        }

        @Override
        protected int tablet() {
            return TABLET_SIZE;
        }

        @Override
        protected int phone() {
            return PHONE_SIZE;
        }
    }
}
