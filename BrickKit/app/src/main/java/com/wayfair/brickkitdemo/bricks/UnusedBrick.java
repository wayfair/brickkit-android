/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.bricks;

import android.view.View;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkit.viewholder.BrickViewHolder;
import com.wayfair.brickkitdemo.R;

/**
 * Passive brick used {@link com.wayfair.brickkitdemo.MainActivityFragment} to fill screen.
 */
public class UnusedBrick extends BaseBrick {
    /**
     * Constructor for UnusedBrick.
     *
     * @param spanSize size information for this brick
     * @param padding padding for this brick
     */
    public UnusedBrick(BrickSize spanSize, BrickPadding padding) {
        super(spanSize, padding);
    }


    @Override
    public void onBindData(BrickViewHolder holder) {
    }

    @Override
    public int getLayout() {
        return R.layout.unused_brick;
    }

    @Override
    public BrickViewHolder createViewHolder(View itemView) {
        return new UnusedBrickHolder(itemView);
    }

    /**
     * {@link BrickViewHolder} for UnusedBrick.
     */
    private static class UnusedBrickHolder extends BrickViewHolder {

        /**
         * Constructor for UnusedBrickHolder.
         *
         * @param itemView view for this brick
         */
        UnusedBrickHolder(View itemView) {
            super(itemView);
        }
    }
}
