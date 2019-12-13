/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.runner.AndroidJUnit4;

import com.wayfair.brickkit.viewholder.BrickViewHolder;

import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class BrickViewHolderTest {

    @Test
    public void testReleaseViewsOnDetach() {
        BrickViewHolder brickViewHolder = new BrickViewHolder(mock(View.class));

        brickViewHolder.releaseViewsOnDetach();

        // nothing to verify
    }
}
