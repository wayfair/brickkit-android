/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BrickFragmentTest {

    private TestBrickFragment testBrickFragment;
    private LayoutInflater inflater;

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        testBrickFragment = new TestBrickFragment();
        BrickDataManager brickDataManager = mock(BrickDataManager.class);
        RecyclerView recyclerView = mock(RecyclerView.class);
        when(recyclerView.getBackground()).thenReturn(new ColorDrawable(Color.WHITE));
        when(brickDataManager.getRecyclerView()).thenReturn(recyclerView);
        testBrickFragment.dataManager = brickDataManager;
        inflater = LayoutInflater.from(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void onCreate() {
        testBrickFragment.onCreate(new Bundle());
        assertTrue(testBrickFragment.onCreateCalled);
    }

    @Test
    public void testOnCreateView() {
        testBrickFragment.setOrientation(OrientationHelper.HORIZONTAL);
        View view = testBrickFragment.onCreateView(inflater, null, null);
        assertNotNull(view);

        testBrickFragment.setOrientation(OrientationHelper.VERTICAL);
        view = testBrickFragment.onCreateView(inflater, null, null);
        assertNotNull(view);
    }

    @Test
    public void testOnDestroyView() {
        View view = testBrickFragment.onCreateView(inflater, null, null);
        assertNotNull(view);
        testBrickFragment.onDestroyView();
    }

    @Test
    public void testOrientation() {
        int orientation = (testBrickFragment.getDefaultOrientation());
        assertEquals(orientation, GridLayoutManager.VERTICAL);
    }

    @Test
    public void testReverse() {
        boolean isReverse = testBrickFragment.reverse();
        assertEquals(isReverse, false);
    }

    @Test
    public void setRecyclerViewBackground() {
        testBrickFragment.setRecyclerViewBackground(Color.WHITE);
        assertEquals(Color.WHITE, testBrickFragment.getRecyclerViewBackground());
    }

    public static final class TestBrickFragment extends BrickFragment {
        private int orientation;
        private boolean onCreateCalled;

        public void setOrientation(int orientation) {
            this.orientation = orientation;
        }

        @Override
        public int orientation() {
            return orientation;
        }

        public int getDefaultOrientation() {
            return super.orientation();
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            onCreateCalled = true;
        }
    }
}
