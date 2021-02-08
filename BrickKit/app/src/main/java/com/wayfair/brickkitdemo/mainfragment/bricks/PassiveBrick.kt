/**
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.mainfragment.bricks

import android.content.res.Resources
import android.view.View
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.padding.BrickPaddingFactory
import com.wayfair.brickkit.size.BrickSize
import com.wayfair.brickkit.viewholder.BrickViewHolder
import com.wayfair.brickkitdemo.R

/**
 * Passive brick used [com.wayfair.brickkitdemo.mainfragment.MainActivityFragment] to fill screen.
 */
class PassiveBrick(
    spanSize: BrickSize,
    resources: Resources
) : BaseBrick(spanSize, BrickPaddingFactory(resources).getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.no_dp)) {

    override fun onBindData(holder: BrickViewHolder) = Unit

    override val layout = R.layout.passive_brick

    override fun createViewHolder(itemView: View): BrickViewHolder = BrickViewHolder(itemView)
}
