/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.padding

import org.junit.Assert.assertEquals
import org.junit.Test

class ZeroBrickPaddingTest {
    @Test
    fun testZeroBrickPadding() {
        ZeroBrickPadding().apply {
            assertEquals(0, innerLeftPadding)
            assertEquals(0, innerTopPadding)
            assertEquals(0, innerRightPadding)
            assertEquals(0, innerBottomPadding)
            assertEquals(0, outerLeftPadding)
            assertEquals(0, outerTopPadding)
            assertEquals(0, outerRightPadding)
            assertEquals(0, outerBottomPadding)
        }
    }
}
