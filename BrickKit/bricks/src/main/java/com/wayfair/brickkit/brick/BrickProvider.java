package com.wayfair.brickkit.brick;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

/**
 * Provides {@link BaseBrick}s based on resource ids.
 * <p>
 * Copyright © 2017 Wayfair. All rights reserved.
 */
public interface BrickProvider {
    /**
     *  Gets the brick, with the matching layout res id.
     * @param layoutResId - matching the res id for the brick to find
     * @return the found brick, or null
     */
    @Nullable
    BaseBrick brickWithLayout(@LayoutRes int layoutResId);

    /**
     * Gets the brick, with the matching placeholder layout res id.
     * @param placeholderLayoutResId - matching the res id for the brick to find
     * @return the found brick, or null
     */
    @Nullable
    BaseBrick brickWithPlaceholderLayout(@LayoutRes int placeholderLayoutResId);
}
