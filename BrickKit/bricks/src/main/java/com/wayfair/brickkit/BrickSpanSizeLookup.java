/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.wayfair.brickkit.brick.BaseBrick;

/**
 * {@link android.support.v7.widget.GridLayoutManager.SpanSizeLookup} which grabs the span size
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
        BaseBrick brick = brickDataManager.getRecyclerViewItems().get(position);

        return brick != null ? brick.getSpanSize().getSpans(context) : 1;
    }
}
