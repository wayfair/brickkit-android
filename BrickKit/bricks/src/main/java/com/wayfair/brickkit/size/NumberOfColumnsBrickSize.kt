/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import com.wayfair.brickkit.BrickDataManager

class NumberOfColumnsBrickSize(numberOfColumns: Int) : BrickSize(
    BrickDataManager.SPAN_COUNT / numberOfColumns,
    BrickDataManager.SPAN_COUNT / numberOfColumns,
    BrickDataManager.SPAN_COUNT / numberOfColumns,
    BrickDataManager.SPAN_COUNT / numberOfColumns
)
