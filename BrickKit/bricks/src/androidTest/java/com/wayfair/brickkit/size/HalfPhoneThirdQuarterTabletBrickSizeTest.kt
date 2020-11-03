/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class HalfPhoneThirdQuarterTabletBrickSizeTest {
    @Test
    fun testGetSpans() {
        HalfPhoneThirdQuarterTabletBrickSize().verifyGetSpans(120, 120, 80, 60)
    }
}
