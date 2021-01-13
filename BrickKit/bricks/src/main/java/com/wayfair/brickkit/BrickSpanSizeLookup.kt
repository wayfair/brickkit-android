/**
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup

/**
 * [androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup] which grabs the span size
 * from the brick at the given position.
 */
internal class BrickSpanSizeLookup(
    private val context: Context,
    private val brickDataManager: BrickDataManager
) : SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int =
        brickDataManager.recyclerViewItems.getOrNull(position)?.spanSize?.getSpans(context) ?: 1
}
