/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.R;

/**
 * This class defines an abstract implementation of brick size.
 *
 * It is used to determine the amount of the screen to use when laying out the brick. The amount
 * of screen that is used is relative to the max span count grabbed from the BrickDataManager.
 *
 * An example would be a brick whose size was 2 that was being laid out in a BrickDataManager whose max span count
 * is 5. The brick would take 40% (2 / 5) of the screen width.
 */
public abstract class BrickSize {
    private int maxSpan;
    private BaseBrick baseBrick;

    /**
     * Constructor.
     *
     * @param maxSpanCount span count to use
     */
    public BrickSize(int maxSpanCount) {
        this.maxSpan = maxSpanCount;
    }

    /**
     * Set the {@link BaseBrick} to use for this brick size.
     *
     * @param baseBrick {@link BaseBrick} to use for this brick size
     */
    public void setBaseBrick(BaseBrick baseBrick) {
        this.baseBrick = baseBrick;
    }

    /**
     * Calculates the spans for this brick based off the device type and orientation.
     *
     * @param context the context to use to get resources
     * @return the number of spans this brick will take up
     */
    public int getSpans(Context context) {
        int spans;

        if (baseBrick.isHeader() || baseBrick.isFooter()) {
            spans = maxSpan;
        } else {
            if (context.getResources().getBoolean(R.bool.tablet)) {
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    spans = landscapeTablet();
                } else {
                    spans = portraitTablet();
                }
            } else {
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    spans = landscapePhone();
                } else {
                    spans = portraitPhone();
                }
            }

            if (spans > maxSpan) {
                Log.i(getClass().getSimpleName(), "Span needs to be less than or equal to: " + maxSpan);
                spans = maxSpan;
            }
        }

        return spans;
    }

    /**
     * Method to return the size to use for this brick on tablets in landscape orientation.
     *
     * @return size to use for this brick on tablets in landscape orientation.
     */
    protected abstract int landscapeTablet();

    /**
     * Method to return the size to use for this brick on tablets in portrait orientation.
     *
     * @return size to use for this brick on tablets in portrait orientation.
     */
    protected abstract int portraitTablet();

    /**
     * Method to return the size to use for this brick on phones in landscape orientation.
     *
     * @return size to use for this brick on phones in landscape orientation.
     */
    protected abstract int landscapePhone();

    /**
     * Method to return the size to use for this brick on phones in portrait orientation.
     *
     * @return size to use for this brick on phones in portrait orientation.
     */
    protected abstract int portraitPhone();
}
