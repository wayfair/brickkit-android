package com.wayfair.brickkit.brick;

import com.wayfair.brickkit.BrickViewHolder;

/**
 * An object that will help bind the holder to the place holder view.
 */
public interface PlaceholderBinder {
    /**
     * Called when a placeholder from {@link ViewModelBrick} is going to be bound.
     *
     * @param holder the holder to bind with
     */
    void onBindPlaceholder(BrickViewHolder holder);
}
