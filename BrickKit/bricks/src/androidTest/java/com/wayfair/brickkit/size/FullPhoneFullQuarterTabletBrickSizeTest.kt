/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class FullPhoneFullQuarterTabletBrickSizeTest {
    @Test
    fun testGetSpans() {
        FullPhoneFullQuarterTabletBrickSize().verifyGetSpans(240, 240, 240, 60)
    }
}
