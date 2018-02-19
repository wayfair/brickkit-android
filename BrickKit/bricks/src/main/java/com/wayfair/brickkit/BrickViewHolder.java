/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Base ViewHolder for bricks. It extends {@link android.support.v7.widget.RecyclerView.ViewHolder} with
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
    protected void onViewAttachedToWindow() {
    }

    /**
     * Method called when the view is detached from the {@link RecyclerView}. All views that have resources
     * should release them here (e.g. ImageView).
     */
    protected void releaseViewsOnDetach() {
    }
}
