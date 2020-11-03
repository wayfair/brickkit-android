/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class ThirdWidthBrickSizeTest {
    @Test
    fun testGetSpans() {
        ThirdWidthBrickSize().verifyGetSpans(80, 80, 80, 80)
    }
}
