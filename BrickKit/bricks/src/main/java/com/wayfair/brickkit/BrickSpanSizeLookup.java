/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.content.Context;

import com.wayfair.brickkit.brick.BaseBrick;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * {@link androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup} which grabs the span size
 * from the brick at the given position.
 */
class BrickSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private final Context context;
    private final BrickDataManager brickDataManager;

    /**
     * Constructor.
     *
     * @param ctx context passed to bricks when looking up their span count
     * @param manager {@link} BrickDataManager to look up bricks in
     */
    BrickSpanSizeLookup(Context ctx, BrickDataManager manager) {
        context = ctx;
        brickDataManager = manager;
    }

    @Override
    public int getSpanSize(int position) {
        try {
            BaseBrick brick = brickDataManager.getRecyclerViewItems().get(position);

            return brick != null ? brick.getSpanSize().getSpans(context) : 1;
        } catch (IndexOutOfBoundsException e) {
            // Appears to be a bug in the support lib.
            return 1;
        }
    }
}
