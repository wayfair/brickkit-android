/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class FullPhoneHalfTabletBrickSizeTest {
    @Test
    fun testGetSpans() {
        FullPhoneHalfTabletBrickSize().verifyGetSpans(240, 240, 120, 120)
    }
}
