/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class FullWidthBrickSizeTest {
    @Test
    fun testGetSpans() {
        FullWidthBrickSize().verifyGetSpans(240, 240, 240, 240)
    }
}
