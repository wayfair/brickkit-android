/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.viewholder.factory

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wayfair.brickkit.BrickRecyclerAdapter
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.viewholder.BrickViewHolder

/**
 * A factory used to create various types of [BrickViewHolder] objects.
 *
 * This class takes it's roots from the  Factory Design Pattern.  Instead of using an interface, it
 * is using the [BrickViewHolder] abstract class.  For more information on the pattern,
 * refer to:
 * [Factory Design Pattern](https://www.tutorialspoint.com/design_pattern/factory_pattern.htm).
 */
internal class BrickViewHolderFactory {
    /**
     * Creates [BrickViewHolder] objects based on the {@param #data}.
     *
     * @param parent   of the view, passed into the [BrickViewHolder]'s constructor.
     * @param viewType of the view to create.  It can be a @LayoutRes id.
     * @param items [BaseBrick]s to search within
     *
     * @return the created [BrickViewHolder]
     */
    fun createBrickViewHolder(parent: ViewGroup, viewType: Int, items: List<BaseBrick>): BrickViewHolder {
        var viewHolder: BrickViewHolder? = null
        try {
            if (viewType > BrickRecyclerAdapter.DEFAULT_LAYOUT_RES_ID) {
                val brick = brickWithLayout(items, viewType) ?: brickWithPlaceholderLayout(items, viewType)
                viewHolder = brick?.createViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            }
        } catch (nfe: Resources.NotFoundException) {
            Log.w(TAG, "Unable to find the resource. parent=$parent, viewType=$viewType", nfe)
        }

        return viewHolder ?: EmptyBrickViewHolder(View(parent.context))
    }

    private fun brickWithLayout(items: List<BaseBrick>, layoutResId: Int): BaseBrick? {
        return items.firstOrNull { brick -> brick.layout == layoutResId }
    }

    private fun brickWithPlaceholderLayout(items: List<BaseBrick>, layoutResId: Int): BaseBrick? {
        return items.firstOrNull { brick -> !brick.isDataReady && brick.placeholderLayout == layoutResId }
    }

    /**
     * A empty versions of a [BrickViewHolder], for use in scenarios, such as when a brick
     * object reference is null and a [BrickViewHolder] instance can't be created.
     */
    internal class EmptyBrickViewHolder(itemView: View) : BrickViewHolder(itemView)

    companion object {
        private const val TAG = "BrickViewHolderFactory"
    }
}
