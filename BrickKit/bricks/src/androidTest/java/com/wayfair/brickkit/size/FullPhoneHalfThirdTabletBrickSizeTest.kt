/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class FullPhoneHalfThirdTabletBrickSizeTest {
    @Test
    fun testGetSpans() {
        FullPhoneHalfThirdTabletBrickSize().verifyGetSpans(240, 240, 120, 80)
    }
}
