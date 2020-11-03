/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class FullPhoneFullHalfTabletBrickSizeTest {

    @Test
    fun testGetSpans() {
        FullPhoneFullHalfTabletBrickSize().verifyGetSpans(240, 240, 240, 120)
    }
}
