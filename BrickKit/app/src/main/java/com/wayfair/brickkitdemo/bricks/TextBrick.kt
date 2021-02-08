/**
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.bricks

import android.view.View
import android.widget.TextView
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.padding.BrickPaddingFactory
import com.wayfair.brickkit.size.BrickSize
import com.wayfair.brickkit.viewholder.BrickViewHolder
import com.wayfair.brickkitdemo.R

/**
 * Simple Brick with a single text view.
 */
class TextBrick(
    spanSize: BrickSize,
    brickPaddingFactory: BrickPaddingFactory,
    private val text: CharSequence
) : BaseBrick(spanSize, brickPaddingFactory.getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.eight_dp)) {

    override fun onBindData(holder: BrickViewHolder) {
        val viewHolder = holder as TextViewHolder
        viewHolder.textView.text = text
    }

    override val layout = R.layout.text_brick

    override fun createViewHolder(itemView: View): BrickViewHolder = TextViewHolder(itemView)

    /**
     * [BrickViewHolder] for TextBrick.
     */
    private class TextViewHolder(itemView: View) : BrickViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text_view)
    }
}
