/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding

import android.content.res.Resources
import androidx.annotation.DimenRes
import com.wayfair.brickkit.OpenForTesting
import com.wayfair.brickkit.R

@OpenForTesting
class BrickPaddingFactory(private val resources: Resources) {

    fun getSimpleBrickPadding(@DimenRes dimen: Int): BrickPadding {
        val padding = resources.getDimension(dimen).toInt()
        return BrickPadding(padding, padding, padding, padding,padding, padding, padding, padding)
    }

    fun getInnerOuterBrickPadding(
        @DimenRes innerPaddingDimensionRes: Int = R.dimen.no_dp,
        @DimenRes outerPaddingDimensionRes: Int = R.dimen.no_dp
    ): BrickPadding {
        val innerPadding = resources.getDimension(innerPaddingDimensionRes).toInt()
        val outerPadding = resources.getDimension(outerPaddingDimensionRes).toInt()
        return BrickPadding(innerPadding, innerPadding, innerPadding, innerPadding, outerPadding, outerPadding, outerPadding, outerPadding)
    }

    fun getRectBrickPadding(
        @DimenRes left: Int = R.dimen.no_dp,
        @DimenRes top: Int = R.dimen.no_dp,
        @DimenRes right: Int = R.dimen.no_dp,
        @DimenRes bottom: Int = R.dimen.no_dp
    ): BrickPadding {
        val leftPadding = resources.getDimension(left).toInt()
        val topPadding = resources.getDimension(top).toInt()
        val rightPadding = resources.getDimension(right).toInt()
        val bottomPadding = resources.getDimension(bottom).toInt()
        return BrickPadding(leftPadding, topPadding, rightPadding, bottomPadding, leftPadding, topPadding, rightPadding, bottomPadding)
    }

    fun getInnerOuterRectBrickPadding(
        @DimenRes innerLeft: Int = R.dimen.no_dp,
        @DimenRes innerTop: Int = R.dimen.no_dp,
        @DimenRes innerRight: Int = R.dimen.no_dp,
        @DimenRes innerBottom: Int = R.dimen.no_dp,
        @DimenRes outerLeft: Int = R.dimen.no_dp,
        @DimenRes outerTop: Int = R.dimen.no_dp,
        @DimenRes outerRight: Int = R.dimen.no_dp,
        @DimenRes outerBottom: Int = R.dimen.no_dp
    ): BrickPadding {
        return BrickPadding(
            resources.getDimension(innerLeft).toInt(),
            resources.getDimension(innerTop).toInt(),
            resources.getDimension(innerRight).toInt(),
            resources.getDimension(innerBottom).toInt(),
            resources.getDimension(outerLeft).toInt(),
            resources.getDimension(outerTop).toInt(),
            resources.getDimension(outerRight).toInt(),
            resources.getDimension(outerBottom).toInt()
        )
    }
}
