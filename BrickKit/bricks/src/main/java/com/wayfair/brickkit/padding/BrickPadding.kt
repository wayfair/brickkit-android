/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding

import com.wayfair.brickkit.OpenForTesting

/**
 * This class defines an abstract implementation of brick padding.
 *
 * It is used to determine the amount of the padding to apply to this brick based off of the location in the screen.
 * Different padding is allowed on sides that are on the outside edge of the screen or any inner edges.
 */
@OpenForTesting
open class BrickPadding internal constructor(
    val innerLeftPadding: Int,
    val innerTopPadding: Int,
    val innerRightPadding: Int,
    val innerBottomPadding: Int,
    val outerLeftPadding: Int,
    val outerTopPadding: Int,
    val outerRightPadding: Int,
    val outerBottomPadding: Int
)
