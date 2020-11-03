/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import com.wayfair.brickkit.BrickDataManager

class PercentageBrickSize(percentage: Float) : BrickSize(
    (BrickDataManager.SPAN_COUNT * percentage).toInt(),
    (BrickDataManager.SPAN_COUNT * percentage).toInt(),
    (BrickDataManager.SPAN_COUNT * percentage).toInt(),
    (BrickDataManager.SPAN_COUNT * percentage).toInt()
)
