/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class HalfWidthBrickSizeTest {
    @Test
    fun testGetSpans() {
        HalfWidthBrickSize().verifyGetSpans(120, 120, 120, 120)
    }
}
