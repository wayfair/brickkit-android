/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import com.wayfair.brickkit.BrickDataManager

class ThirdWidthBrickSize : BrickSize(
    BrickDataManager.SPAN_COUNT / 3,
    BrickDataManager.SPAN_COUNT / 3,
    BrickDataManager.SPAN_COUNT / 3,
    BrickDataManager.SPAN_COUNT / 3
)
