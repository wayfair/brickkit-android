package com.wayfair.brickkit.viewholder;

import android.view.View;

import com.wayfair.brickkit.BrickViewHolder;

/**
 * A empty versions of a {@link BrickViewHolder}, for use in scenarios, such as when a brick
 * object reference is null and a {@link BrickViewHolder} instance can't be created.
 * <p>
 * Copyright © 2017 Wayfair. All rights reserved.
 */
public class EmptyBrickViewHolder extends BrickViewHolder {
    /**
     * Constructor.
     *
     * @param itemView view used for binding
     */
    public EmptyBrickViewHolder(View itemView) {
        super(itemView);
    }
}
