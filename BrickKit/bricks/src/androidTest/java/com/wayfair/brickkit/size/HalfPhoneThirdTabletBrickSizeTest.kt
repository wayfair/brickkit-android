/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class HalfPhoneThirdTabletBrickSizeTest {
    @Test
    fun testGetSpans() {
        HalfPhoneThirdTabletBrickSize().verifyGetSpans(120, 120, 80, 80)
    }
}
