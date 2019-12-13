package com.wayfair.brickkit.viewholder.factory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.test.R;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.brick.BrickProvider;
import com.wayfair.brickkit.view.empty.EmptyBrickView;
import com.wayfair.brickkit.viewholder.EmptyBrickViewHolder;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link BrickViewHolderFactory}'s functionality.
 * <p>
 * Copyright © 2017 Wayfair. All rights reserved.
 */
@RunWith(AndroidJUnit4.class)
public class BrickViewHolderFactoryTest {

    // Used for by the BrickProvider for a brick to test with
    private final BaseBrick brickWithLayout = new BaseBrick() {
        @Override
        public void onBindData(BrickViewHolder holder) { /* no operation */ }

        @Override
        public int getLayout() {
            return 1;
        }

        @Override
        public BrickViewHolder createViewHolder(View itemView) {
            return new BrickViewHolder(new View(itemView.getContext()));
        }
    };

    // Used for by the BrickProvider for a placeholder brick to test with
    private final BaseBrick brickWithPlaceHolderLayout = new BaseBrick() {
        @Override
        public void onBindData(BrickViewHolder holder) { /* no operation */ }

        @Override
        public int getLayout() {
            return 2;
        }

        @Override
        public BrickViewHolder createViewHolder(View itemView) {
            return new BrickViewHolder(new View((itemView.getContext())));
        }
    };

    // Used for testing non-null brick object reference scenarios.
    private BrickProvider nonNullBrickProvider = new BrickProvider() {
        @Override
        public BaseBrick brickWithLayout(int layoutResId) {
            return brickWithLayout;
        }

        @Override
        public BaseBrick brickWithPlaceholderLayout(int placeholderLayoutResId) {
            return brickWithPlaceHolderLayout;
        }
    };

    // Used for testing null brick object reference scenarios.
    private BrickProvider nullBrickProvider = new BrickProvider() {
        @Override
        public BaseBrick brickWithLayout(int layoutResId) {
            return null;
        }

        @Override
        public BaseBrick brickWithPlaceholderLayout(int placeholderLayoutResId) {
            return null;
        }
    };

    /**
     * With the default layout res id set, the goal is to have an empty view holder created
     * instead of exceptions being thrown.
     * <p>
     * Verify that this occurs.
     */
    @Test
    public void testFactory_WithDefaultResId_ResultsInEmptyBrickViewHolder() {
        // Given
        String logTag = "test1";
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        ViewGroup parent = new FrameLayout(context);
        int viewType = BaseBrick.DEFAULT_LAYOUT_RES_ID; // 0

        // When
        BrickViewHolderFactoryData data = new BrickViewHolderFactoryData(logTag, parent, viewType,
                nonNullBrickProvider);
        BrickViewHolderFactory factory = new BrickViewHolderFactory();
        BrickViewHolder viewHolder = factory.createBrickViewHolder(data);

        // Then
        assertTrue(viewHolder instanceof EmptyBrickViewHolder);
        assertTrue(viewHolder.itemView instanceof EmptyBrickView);
    }

    /**
     * With the layout res id set to a non-default value, the goal is to have a standard
     * brick view holder is created.
     * <p>
     * Verify that this occurs.
     */
    @Test
    public void testFactory_WithNonDefaultIdValid_ResultsInNonEmptyBrickViewHolder() {
        // Given
        String logTag = "test2";
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        ViewGroup parent = new FrameLayout(context);
        int viewType = R.layout.test_layout;

        // When
        BrickViewHolderFactoryData data = new BrickViewHolderFactoryData(logTag, parent, viewType,
                nonNullBrickProvider);
        BrickViewHolderFactory factory = new BrickViewHolderFactory();
        BrickViewHolder viewHolder = factory.createBrickViewHolder(data);

        // Then
        assertFalse(viewHolder instanceof EmptyBrickViewHolder);
        assertFalse(viewHolder.itemView instanceof EmptyBrickView);
    }

    /**
     * With the null brick object references being provided, the goal is to have an empty brick
     * view holder created instead of having a Resource.NotFoundException being thrown.
     * <p>
     * Verify that this occurs.
     */
    @Test
    public void testFactory_WithNullBricksProvided_ResultsInAnEmptyBrickViewHolder() {
        // Given
        String logTag = "test3";
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        ViewGroup parent = new FrameLayout(context);
        int viewType = R.layout.test_layout;

        // When
        BrickViewHolderFactoryData data = new BrickViewHolderFactoryData(logTag, parent, viewType,
                nullBrickProvider);
        BrickViewHolderFactory factory = new BrickViewHolderFactory();
        BrickViewHolder viewHolder = factory.createBrickViewHolder(data);

        // Then
        assertTrue(viewHolder instanceof EmptyBrickViewHolder);
        assertTrue(viewHolder.itemView instanceof EmptyBrickView);
    }

    /**
     * Verify that the factory correctly produces an
     * {@link com.wayfair.brickkit.viewholder.EmptyBrickViewHolder } with a call to
     * {@link BrickViewHolderFactory#createEmptyBrickViewHolder(Context)}.
     */
    @Test
    public void testCreateEmptyBrickViewHolder() {
        // Given
        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        // When
        BrickViewHolderFactory factory = new BrickViewHolderFactory();
        BrickViewHolder viewHolder = factory.createEmptyBrickViewHolder(context);

        // Then
        assertTrue(viewHolder instanceof EmptyBrickViewHolder);
        assertTrue(viewHolder.itemView instanceof EmptyBrickView);
    }

    /**
     * Verify that the factory correctly produces an
     * {@link BrickViewHolder} with a call to
     * {@link BrickViewHolderFactory#createBrickViewHolder(BrickViewHolderFactoryData)}.
     */
    @Test
    public void testCreateViewHolderWithViewType() {
        // Given
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        ViewGroup parent = new FrameLayout(context);
        int viewType = R.layout.test_layout;

        // When
        BrickViewHolderFactory factory = new BrickViewHolderFactory();
        BrickViewHolder viewHolder = factory.createViewHolderWithViewType(parent, viewType,
                nonNullBrickProvider);

        // Then
        assertFalse(viewHolder instanceof EmptyBrickViewHolder);
        assertFalse(viewHolder.itemView instanceof EmptyBrickView);
    }
}
