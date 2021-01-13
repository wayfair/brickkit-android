package com.wayfair.brickkit.animator

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

internal class AvoidFlickerItemAnimator : DefaultItemAnimator() {
    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        changeDuration = 0
        supportsChangeAnimations = false
        return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY)
    }
}
