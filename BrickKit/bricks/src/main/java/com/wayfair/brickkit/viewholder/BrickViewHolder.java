/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Base ViewHolder for bricks. It extends {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} with
 * one additional method that is called when the view holder is detached from the {@link RecyclerView} so
 * that views can be released.
 */
public class BrickViewHolder extends RecyclerView.ViewHolder {
    /**
     * Constructor.
     *
     * @param itemView view used for binding
     */
    public BrickViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * Called when {@link RecyclerView.Adapter#onViewAttachedToWindow(RecyclerView.ViewHolder)} is invoked.
     */
    public void onViewAttachedToWindow() {
    }

    /**
     * Method called when the view is detached from the {@link RecyclerView}. All views that have resources
     * should release them here (e.g. ImageView).
     */
    public void releaseViewsOnDetach() {
    }
}
