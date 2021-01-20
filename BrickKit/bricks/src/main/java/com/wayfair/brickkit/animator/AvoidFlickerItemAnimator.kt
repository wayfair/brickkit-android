package com.wayfair.brickkit.animator

import androidx.recyclerview.widget.DefaultItemAnimator

internal class AvoidFlickerItemAnimator : DefaultItemAnimator() {
    init {
        changeDuration = 0
        supportsChangeAnimations = false
    }
}
