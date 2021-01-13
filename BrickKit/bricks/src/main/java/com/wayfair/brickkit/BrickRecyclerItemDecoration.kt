/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * [androidx.recyclerview.widget.RecyclerView.ItemDecoration] which applies padding to bricks
 * based off of their given [com.wayfair.brickkit.padding.BrickPadding] and location in on the screen.
 *
 * Dynamic padding takes into consideration the number of bricks in a group
 * and the span size to appropriately set the offsets when the section has
 * more than one brick. Using the traditional padding mechanism
 * duplicates the padding between bricks where only half is desired.
 */
internal class BrickRecyclerItemDecoration(
    private val brickDataManager: BrickDataManager
) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val brick = brickDataManager.recyclerViewItems.getOrNull(parent.getChildAdapterPosition(view))

        if (brick != null && brickDataManager == (parent.adapter as? BrickRecyclerAdapter)?.brickDataManager) {
            val padding = brick.padding

            outRect.bottom = if (brick.isInLastRow) padding.outerBottomPadding else padding.innerBottomPadding
            outRect.top = if (brick.isInFirstRow) padding.outerTopPadding else padding.innerTopPadding
            outRect.left = if (brick.isOnLeftWall) padding.outerLeftPadding else padding.innerLeftPadding
            outRect.right = if (brick.isOnRightWall) padding.outerRightPadding else padding.innerRightPadding
        }
    }
}
