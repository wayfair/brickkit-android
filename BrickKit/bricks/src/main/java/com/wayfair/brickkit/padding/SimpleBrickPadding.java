/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding;

import android.graphics.Rect;

/**
 * {@link BrickPadding} which returns the result of padding() for all cases.
 */
public class SimpleBrickPadding extends BrickPadding {
    /**
     * Constructor for a simple int for all padding, inner/outer, on all sides.
     *
     * @param padding The value for padding.
     */
    public SimpleBrickPadding(int padding) {
        super(
                new Rect(padding, padding, padding, padding),
                new Rect(padding, padding, padding, padding)
        );
    }
}
