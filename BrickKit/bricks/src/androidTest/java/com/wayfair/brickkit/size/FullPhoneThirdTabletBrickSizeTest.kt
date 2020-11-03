/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class FullPhoneThirdTabletBrickSizeTest {
    @Test
    fun testGetSpans() {
        FullPhoneThirdTabletBrickSize().verifyGetSpans(240, 240, 80, 80)
    }
}
