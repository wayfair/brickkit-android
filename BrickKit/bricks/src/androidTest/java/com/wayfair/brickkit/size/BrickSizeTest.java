/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.wayfair.brickkit.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BrickSizeTest {
    private static final int MAX_SPANS = 4;
    private static final int LANDSCAPE_TABLET = 1;
    private static final int PORTRAIT_TABLET = 2;
    private static final int LANDSCAPE_PHONE = 3;
    private static final int PORTRAIT_PHONE = 5;

    private Context context;
    private TestBrickSize brickSize;
    private Resources resources;
    private Configuration configuration;

    @Before
    public void setup() {
        configuration = new Configuration();

        resources = mock(Resources.class);
        when(resources.getConfiguration()).thenReturn(configuration);

        context = mock(Context.class);
        when(context.getResources()).thenReturn(resources);

        brickSize = new TestBrickSize();
    }

    @Test
    public void testLandscapeTablet() {
        when(resources.getBoolean(R.bool.tablet)).thenReturn(true);
        configuration.orientation = Configuration.ORIENTATION_LANDSCAPE;

        assertEquals(LANDSCAPE_TABLET, brickSize.getSpans(context));
    }

    @Test
    public void testPortraitTablet() {
        when(resources.getBoolean(R.bool.tablet)).thenReturn(true);
        configuration.orientation = Configuration.ORIENTATION_PORTRAIT;

        assertEquals(PORTRAIT_TABLET, brickSize.getSpans(context));
    }

    @Test
    public void testLandscapePhone() {
        when(resources.getBoolean(R.bool.tablet)).thenReturn(false);
        configuration.orientation = Configuration.ORIENTATION_LANDSCAPE;

        assertEquals(LANDSCAPE_PHONE, brickSize.getSpans(context));
    }

    @Test
    public void testPortraitPhone() {
        when(resources.getBoolean(R.bool.tablet)).thenReturn(false);
        configuration.orientation = Configuration.ORIENTATION_PORTRAIT;

        assertEquals(MAX_SPANS, brickSize.getSpans(context));
    }

    private static final class TestBrickSize extends BrickSize {
        private TestBrickSize() {
            super(MAX_SPANS);
        }

        @Override
        protected int landscapeTablet() {
            return LANDSCAPE_TABLET;
        }

        @Override
        protected int portraitTablet() {
            return PORTRAIT_TABLET;
        }

        @Override
        protected int landscapePhone() {
            return LANDSCAPE_PHONE;
        }

        @Override
        protected int portraitPhone() {
            return PORTRAIT_PHONE;
        }
    }
}
