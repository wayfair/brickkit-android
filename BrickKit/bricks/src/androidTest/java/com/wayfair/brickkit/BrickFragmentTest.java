/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Looper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BrickFragmentTest {

    private BrickFragment testBrickFragment;

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        testBrickFragment = new BrickFragment() { };
        BrickDataManager brickDataManager = mock(BrickDataManager.class);
        RecyclerView recyclerView = mock(RecyclerView.class);
        when(recyclerView.getBackground()).thenReturn(new ColorDrawable(Color.WHITE));
        when(brickDataManager.getRecyclerView()).thenReturn(recyclerView);
        testBrickFragment.dataManager = brickDataManager;
    }

    @Test
    public void testOnDestroyView() {
        testBrickFragment.onDestroyView();

        verify(testBrickFragment.dataManager).onDestroyView();
    }

    @Test
    public void setRecyclerViewBackground() {
        testBrickFragment.setRecyclerViewBackground(Color.WHITE);

        assertEquals(Color.WHITE, testBrickFragment.getRecyclerViewBackground());
    }
}
