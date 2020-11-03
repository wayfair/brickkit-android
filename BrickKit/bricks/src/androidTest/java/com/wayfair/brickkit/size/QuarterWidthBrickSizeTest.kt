/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class QuarterWidthBrickSizeTest {
    @Test
    fun testGetSpans() {
        QuarterWidthBrickSize().verifyGetSpans(60, 60, 60, 60)
    }
}
