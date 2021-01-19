/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

/**
 * Callback interface used to notify when binding items.
 */
interface OnReachedItemAtPosition {
    /**
     * Callback when binding items.
     *
     * @param position position of item that was bound
     */
    fun bindingItemAtPosition(position: Int)
}
