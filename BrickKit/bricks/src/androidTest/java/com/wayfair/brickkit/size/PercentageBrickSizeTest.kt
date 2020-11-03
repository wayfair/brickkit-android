/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class PercentageBrickSizeTest {
    @Test
    fun testGetSpans() {
        PercentageBrickSize(.2f).verifyGetSpans(48, 48, 48, 48)
        PercentageBrickSize(.4f).verifyGetSpans(96, 96, 96, 96)
        PercentageBrickSize(.5f).verifyGetSpans(120, 120, 120, 120)
    }
}
