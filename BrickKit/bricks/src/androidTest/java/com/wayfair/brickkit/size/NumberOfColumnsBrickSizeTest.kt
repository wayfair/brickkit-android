/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import org.junit.Test

class NumberOfColumnsBrickSizeTest {
    @Test
    fun testGetSpans() {
        NumberOfColumnsBrickSize(6).verifyGetSpans(40, 40, 40, 40)
        NumberOfColumnsBrickSize(2).verifyGetSpans(120, 120, 120, 120)
    }
}
