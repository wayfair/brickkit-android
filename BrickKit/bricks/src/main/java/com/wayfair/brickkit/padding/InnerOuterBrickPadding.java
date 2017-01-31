/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding;

import android.graphics.Rect;

/**
 * {@link BrickPadding} which uses the values from innerPadding() and outerPadding().
 */
public class InnerOuterBrickPadding extends BrickPadding {

    /**
     * Constructor for inner and outer padding that are different but symmetrical on all sides.
     *
     * @param innerPadding The inner padding value.
     * @param outerPadding The outer padding value.
     */
    public InnerOuterBrickPadding(int innerPadding, int outerPadding) {
        super(
                new Rect(innerPadding, innerPadding, innerPadding, innerPadding),
                new Rect(outerPadding, outerPadding, outerPadding, outerPadding)
        );
    }
}
