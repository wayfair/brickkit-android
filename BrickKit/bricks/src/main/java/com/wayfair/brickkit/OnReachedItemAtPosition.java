/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

/**
 * Callback interface used in {@link BrickRecyclerAdapter} when binding items.
 */
public interface OnReachedItemAtPosition {
    /**
     * Callback from {@link BrickRecyclerAdapter} when binding items.
     *
     * @param position position of item that was bound
     */
    void bindingItemAtPosition(int position);
}
