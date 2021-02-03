/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

import androidx.recyclerview.widget.DiffUtil
import com.wayfair.brickkit.brick.BaseBrick

/**
 * A DiffUtil for Bricks.
 */
internal class BrickDiffUtilCallback internal constructor(
    private val oldList: List<BaseBrick>,
    private val newList: List<BaseBrick>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].layout == newList[newItemPosition].layout

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}
