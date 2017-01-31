/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size;

/**
 * Simple {@link BrickSize} which returns size based off of device type (phone vs tablet.
 */
public abstract class DeviceBrickSize extends BrickSize {
    /**
     * Constructor.
     *
     * @param maxSpanCount span count to use
     */
    protected DeviceBrickSize(int maxSpanCount) {
        super(maxSpanCount);
    }

    @Override
    protected int landscapeTablet() {
        return tablet();
    }

    @Override
    protected int portraitTablet() {
        return tablet();
    }

    @Override
    protected int landscapePhone() {
        return phone();
    }

    @Override
    protected int portraitPhone() {
        return phone();
    }

    /**
     * Method to return the size to use for this brick on phones.
     *
     * @return size to use for this brick on phones.
     */
    protected abstract int phone();

    /**
     * Method to return the size to use for this brick on tablets.
     *
     * @return size to use for this brick on tablets.
     */
    protected abstract int tablet();
}
