package com.wayfair.brickkit.brick

import androidx.annotation.LayoutRes

/**
 * Provides [BaseBrick]s based on resource ids.
 *
 * Copyright © 2017 Wayfair. All rights reserved.
 */
internal interface BrickProvider {
    /**
     * Gets the brick, with the matching layout res id.
     * @param layoutResId - matching the res id for the brick to find
     * @return the found brick, or null
     */
    fun brickWithLayout(@LayoutRes layoutResId: Int): BaseBrick?

    /**
     * Gets the brick, with the matching placeholder layout res id.
     * @param placeholderLayoutResId - matching the res id for the brick to find
     * @return the found brick, or null
     */
    fun brickWithPlaceholderLayout(@LayoutRes placeholderLayoutResId: Int): BaseBrick?
}
