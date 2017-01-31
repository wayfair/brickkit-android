/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.behavior;

import android.support.v7.widget.RecyclerView;

import com.wayfair.brickkit.BrickRecyclerAdapter;

/**
 * Abstract class to extend if you want to implement behaviors which work on
 * bricks, like {@link StickyHeaderBehavior} and {@link StickyFooterBehavior}.
 */
public abstract class BrickBehavior extends RecyclerView.OnScrollListener {
    /**
     * Method that is called when the dataset for {@link BrickRecyclerAdapter} has changed.
     */
    public abstract void onDataSetChanged();

    /**
     * Method that is called when the {@link android.support.v7.widget.RecyclerView} has scrolled.
     */
    public abstract void onScroll();

    /**
     * Method that is called when this behavior is attached to the {@link android.support.v7.widget.RecyclerView}.
     */
    public abstract void attachToRecyclerView();

    /**
     * Method that is called when this behavior is detached to the {@link android.support.v7.widget.RecyclerView}.
     */
    public abstract void detachFromRecyclerView();
}
