/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class HalfPhoneHalfQuarterTabletBrickSizeTest {
    @Test
    fun testGetSpans() {
        HalfPhoneHalfQuarterTabletBrickSize().verifyGetSpans(120, 120, 120, 60)
    }
}
