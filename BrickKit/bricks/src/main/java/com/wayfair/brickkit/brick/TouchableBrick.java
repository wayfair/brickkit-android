/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.brick;

import android.view.View;

/**
 * Interface defining a brick that can be clicked.
 */
public interface TouchableBrick {
    /**
     * {@link android.view.View.OnClickListener} to be called when the brick is clicked.
     *
     * @return {@link android.view.View.OnClickListener}
     */
    View.OnClickListener onTouch();

    /**
     * Whether or not the brick is currently handling click events.
     *
     * @return true if the brick is currently handling click events, false otherwise
     */
    boolean isEnabled();
}
