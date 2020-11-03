/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import com.wayfair.brickkit.BrickDataManager

class FullPhoneFullHalfTabletBrickSize : BrickSize(
    BrickDataManager.SPAN_COUNT,
    BrickDataManager.SPAN_COUNT,
    BrickDataManager.SPAN_COUNT,
    BrickDataManager.SPAN_COUNT / 2
)
