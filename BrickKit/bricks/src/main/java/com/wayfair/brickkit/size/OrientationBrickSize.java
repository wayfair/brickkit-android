/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size;

/**
 * Simple {@link BrickSize} which returns size based off of device orientation.
 */
public abstract class OrientationBrickSize extends BrickSize {
    /**
     * Constructor.
     *
     * @param maxSpanCount span count to use
     */
    protected OrientationBrickSize(int maxSpanCount) {
        super(maxSpanCount);
    }

    @Override
    protected int landscapeTablet() {
        return landscape();
    }

    @Override
    protected int portraitTablet() {
        return portrait();
    }

    @Override
    protected int landscapePhone() {
        return landscape();
    }

    @Override
    protected int portraitPhone() {
        return portrait();
    }

    /**
     * Method to return the size to use for this brick when in portrait orientation.
     *
     * @return size to use for this brick when in portrait orientation.
     */
    protected abstract int portrait();

    /**
     * Method to return the size to use for this brick when in landscape orientation.
     *
     * @return size to use for this brick when in landscape orientation.
     */
    protected abstract int landscape();
}
