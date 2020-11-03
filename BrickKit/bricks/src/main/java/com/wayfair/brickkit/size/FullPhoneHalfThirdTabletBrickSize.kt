/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import com.wayfair.brickkit.BrickDataManager

class FullPhoneHalfThirdTabletBrickSize : BrickSize(
    BrickDataManager.SPAN_COUNT,
    BrickDataManager.SPAN_COUNT,
    BrickDataManager.SPAN_COUNT / 2,
    BrickDataManager.SPAN_COUNT / 3
)
