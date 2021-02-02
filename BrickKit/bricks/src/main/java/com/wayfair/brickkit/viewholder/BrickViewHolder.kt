/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Base ViewHolder for bricks. It extends [androidx.recyclerview.widget.RecyclerView.ViewHolder] with
 * one additional method that is called when the view holder is detached from the [RecyclerView] so
 * that views can be released.
 */
open class BrickViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * Called when [RecyclerView.Adapter.onViewAttachedToWindow] is invoked.
     */
    open fun onViewAttachedToWindow() = Unit

    /**
     * Method called when the view is detached from the [RecyclerView]. All views that have resources
     * should release them here (e.g. ImageView).
     */
    open fun releaseViewsOnDetach() = Unit
}
