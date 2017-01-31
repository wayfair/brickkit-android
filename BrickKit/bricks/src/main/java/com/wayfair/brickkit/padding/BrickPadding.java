/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding;

import android.graphics.Rect;

/**
 * This class defines an abstract implementation of brick padding.
 *
 * It is used to determine the amount of the padding to apply to this brick based off of the location in the screen.
 * Different padding is allowed on sides that are on the outside edge of the screen or any inner edges.
 */
public class BrickPadding {
    private Rect innerPadding;
    private Rect outerPadding;

    /**
     * Constructor for creating padding based on two Rects.
     *
     * @param innerPadding The inner padding Rect.
     * @param outerPadding The outer padding Rect.
     */
    public BrickPadding(Rect innerPadding, Rect outerPadding) {
        this.innerPadding = innerPadding;
        this.outerPadding = outerPadding;
    }

    /**
     * Gets the padding to be used on the left side for inner edges.
     *
     * @return the padding to be used on the left side for inner edges.
     */
    public int getInnerLeftPadding() {
        return innerPadding.left;
    }

    /**
     * Gets the padding to be used on the top side for inner edges.
     *
     * @return the padding to be used on the top side for inner edges.
     */
    public int getInnerTopPadding() {
        return innerPadding.top;
    }

    /**
     * Gets the padding to be used on the right side for inner edges.
     *
     * @return the padding to be used on the right side for inner edges.
     */
    public int getInnerRightPadding() {
        return innerPadding.right;
    }

    /**
     * Gets the padding to be used on the bottom side for inner edges.
     *
     * @return the padding to be used on the bottom side for inner edges.
     */
    public int getInnerBottomPadding() {
        return innerPadding.bottom;
    }

    /**
     * Gets the padding to be used on the left side for outer edges.
     *
     * @return the padding to be used on the left side for outer edges.
     */
    public int getOuterLeftPadding() {
        return outerPadding.left;
    }

    /**
     * Gets the padding to be used on the top side for outer edges.
     *
     * @return the padding to be used on the top side for outer edges.
     */
    public int getOuterTopPadding() {
        return outerPadding.top;
    }

    /**
     * Gets the padding to be used on the right side for outer edges.
     *
     * @return the padding to be used on the right side for outer edges.
     */
    public int getOuterRightPadding() {
        return outerPadding.right;
    }

    /**
     * Gets the padding to be used on the bottom side for outer edges.
     *
     * @return the padding to be used on the bottom side for outer edges.
     */
    public int getOuterBottomPadding() {
        return outerPadding.bottom;
    }

}
