/**
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.fragmentbrick.bricks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.padding.ZeroBrickPadding
import com.wayfair.brickkit.size.HalfWidthBrickSize
import com.wayfair.brickkit.viewholder.BrickViewHolder
import com.wayfair.brickkitdemo.R

/**
 * Brick whose content is a fragment.
 */
class FragmentBrick(
    private val fragmentManager: FragmentManager,
    private val fragment: Fragment,
    @ColorRes private val backgroundColor: Int
) : BaseBrick(HalfWidthBrickSize(), ZeroBrickPadding()) {

    override fun onBindData(holder: BrickViewHolder) {
        val viewHolder = holder as FragmentBrickViewHolder

        val view: View? = if (!fragment.isAdded) {
            fragmentManager.beginTransaction().add(fragment, null).commit()
            fragmentManager.executePendingTransactions()
            fragment.onAttach(holder.itemView.context)
            fragment.onCreateView(LayoutInflater.from(holder.itemView.context), viewHolder.frameLayout, null)
        } else {
            fragment.view
        }

        (view?.parent as? ViewGroup)?.removeView(view)

        viewHolder.itemView.setBackgroundColor(holder.itemView.resources.getColor(backgroundColor, null))
        viewHolder.frameLayout.addView(view)
    }

    override val layout = R.layout.fragment_brick

    override fun createViewHolder(itemView: View): BrickViewHolder = FragmentBrickViewHolder(itemView)

    /**
     * [BrickViewHolder] for FragmentBrick.
     */
    private class FragmentBrickViewHolder constructor(itemView: View) : BrickViewHolder(itemView) {
        val frameLayout: FrameLayout = itemView.findViewById(R.id.fragment_container)
    }
}
