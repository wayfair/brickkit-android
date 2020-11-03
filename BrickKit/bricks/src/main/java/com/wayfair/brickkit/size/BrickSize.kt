/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.wayfair.brickkit.BrickDataManager
import com.wayfair.brickkit.OpenForTesting
import com.wayfair.brickkit.R

/**
 * This class defines an abstract implementation of brick size.
 *
 * It is used to determine the amount of the screen to use when laying out the brick. The amount
 * of screen that is used is relative to the max span count grabbed from the BrickDataManager.
 *
 * An example would be a brick whose size was 2 that was being laid out in a BrickDataManager whose max span count
 * is 5. The brick would take 40% (2 / 5) of the screen width.
 */
@OpenForTesting
abstract class BrickSize internal constructor(
    private val portraitPhone: Int,
    private val landscapePhone: Int,
    private val portraitTablet: Int,
    private val landscapeTablet: Int
) {
    /**
     * Calculates the spans for this brick based off the device type and orientation.
     *
     * @param context the context to use to get resources
     * @return the number of spans this brick will take up
     */
    fun getSpans(context: Context): Int {
        val isTablet = context.resources.getBoolean(R.bool.tablet)
        val isLandscape = context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        val spans = if (isTablet) {
            if (isLandscape) landscapeTablet else portraitTablet
        } else {
            if (isLandscape) landscapePhone else portraitPhone
        }

        if (spans > BrickDataManager.SPAN_COUNT) {
            Log.i(javaClass.simpleName, "Span needs to be less than or equal to: " + BrickDataManager.SPAN_COUNT)
            return BrickDataManager.SPAN_COUNT
        }
        return spans
    }
}
