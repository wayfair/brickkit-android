/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size;

/**
 * {@link BrickSize} which returns the result of size() for all cases.
 */
public abstract class SimpleBrickSize extends BrickSize {
    @Override
    protected int landscapeTablet() {
        return size();
    }

    @Override
    protected int portraitTablet() {
        return size();
    }

    @Override
    protected int landscapePhone() {
        return size();
    }

    @Override
    protected int portraitPhone() {
        return size();
    }

    /**
     * Method to return the size to use for this brick.
     *
     * @return size to use for this brick.
     */
    protected abstract int size();
}
