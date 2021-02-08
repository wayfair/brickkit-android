/**
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.mainfragment.bricks

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.padding.BrickPaddingFactory
import com.wayfair.brickkit.size.PercentageBrickSize
import com.wayfair.brickkit.viewholder.BrickViewHolder
import com.wayfair.brickkitdemo.R

/**
 * Used in [com.wayfair.brickkitdemo.mainfragment.MainActivityFragment] to link to other fragments.
 */
class ActiveBrick(
    resources: Resources,
    @StringRes stringRes: Int,
    private val onTouch: () -> Unit
) : BaseBrick(PercentageBrickSize(.4f), BrickPaddingFactory(resources).getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.no_dp)) {

    var text: String = resources.getString(stringRes)
        set(value) {
            field = value
            refreshItem()
        }

    override fun onBindData(holder: BrickViewHolder) {
        val viewHolder = holder as ActiveBrickHolder
        viewHolder.textView.text = text
        viewHolder.itemView.setOnClickListener { onTouch.invoke() }
    }

    override val layout: Int = R.layout.active_brick

    override val placeholderLayout = R.layout.active_brick_placeholder

    override fun createViewHolder(itemView: View): BrickViewHolder = ActiveBrickHolder(itemView)

    override val isDataReady
        get() = text.isNotEmpty()

    /**
     * [BrickViewHolder] for ActiveBrick.
     */
    private class ActiveBrickHolder constructor(itemView: View) : BrickViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.label)
    }
}
