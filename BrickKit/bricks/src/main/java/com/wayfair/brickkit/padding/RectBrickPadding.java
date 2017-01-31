/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding;

import android.graphics.Rect;

/**
 * {@link BrickPadding} which uses the {@link Rect} to get padding values.
 */
public class RectBrickPadding extends BrickPadding {

    /**
     * Constructor for one Rect for both inner and outer padding.
     *
     * @param padding The Rect for padding the brick.
     */
    public RectBrickPadding(Rect padding) {
        super(padding, padding);
    }
}
