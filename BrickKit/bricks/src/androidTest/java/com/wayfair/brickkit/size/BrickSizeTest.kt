/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import com.wayfair.brickkit.BrickDataManager
import org.junit.Test

class BrickSizeTest {
    @Test
    fun testGetSpans() {
        val brickSize = object : BrickSize(PORTRAIT_PHONE, LANDSCAPE_PHONE, PORTRAIT_TABLET, LANDSCAPE_TABLET) {}

        brickSize.verifyGetSpans(BrickDataManager.SPAN_COUNT, LANDSCAPE_PHONE, PORTRAIT_TABLET, LANDSCAPE_TABLET)
    }

    companion object {
        private const val LANDSCAPE_TABLET = 60
        private const val PORTRAIT_TABLET = 120
        private const val LANDSCAPE_PHONE = 180
        private const val PORTRAIT_PHONE = 250
    }
}
