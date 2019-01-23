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
public class BrickDialogFragmentTest {

    private TestBrickDialogFragment testBrickDialogFragment;
    private LayoutInflater inflater;

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        testBrickDialogFragment = new TestBrickDialogFragment();
        BrickDataManager brickDataManager = mock(BrickDataManager.class);
        RecyclerView recyclerView = mock(RecyclerView.class);
        when(recyclerView.getBackground()).thenReturn(new ColorDrawable(Color.WHITE));
        when(brickDataManager.getRecyclerView()).thenReturn(recyclerView);
        testBrickDialogFragment.dataManager = brickDataManager;
        inflater = LayoutInflater.from(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void onCreate() {
        testBrickDialogFragment.onCreate(new Bundle());
        assertTrue(testBrickDialogFragment.onCreateCalled);
    }

    @Test
    public void testOnCreateView() {
        testBrickDialogFragment.setOrientation(OrientationHelper.HORIZONTAL);
        View view = testBrickDialogFragment.onCreateView(inflater, null, null);
        assertNotNull(view);

        testBrickDialogFragment.setOrientation(OrientationHelper.VERTICAL);
        view = testBrickDialogFragment.onCreateView(inflater, null, null);
        assertNotNull(view);
    }

    @Test
    public void testOnDestroyView() {
        View view = testBrickDialogFragment.onCreateView(inflater, null, null);
        assertNotNull(view);
        testBrickDialogFragment.onDestroyView();
    }

    @Test
    public void testOrientation() {
        int orientation = (testBrickDialogFragment.getDefaultOrientation());
        assertEquals(orientation, GridLayoutManager.VERTICAL);
    }

    @Test
    public void testReverse() {
        boolean isReverse = testBrickDialogFragment.reverse();
        assertEquals(isReverse, false);
    }

    @Test
    public void setRecyclerViewBackground() {
        testBrickDialogFragment.setRecyclerViewBackground(Color.WHITE);
        assertEquals(Color.WHITE, testBrickDialogFragment.getRecyclerViewBackground());
    }

    public static final class TestBrickDialogFragment extends BrickDialogFragment {
        private int orientation;
        private boolean onCreateCalled;

        public void setOrientation(int orientation) {
            this.orientation = orientation;
        }

        @Override
        public int maxSpans() {
            return super.maxSpans();
        }

        @Override
        public int orientation() {
            return orientation;
        }

        @Override
        public boolean reverse() {
            return super.reverse();
        }

        public int getDefaultOrientation() {
            return super.orientation();
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            onCreateCalled = true;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }
}
