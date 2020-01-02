/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.wayfair.brickkit.BrickRecyclerAdapter;

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
     * Called when {@link BrickRecyclerAdapter#onViewAttachedToWindow(BrickViewHolder)}
     * is invoked.
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
