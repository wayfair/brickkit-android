package com.wayfair.brickkit.animator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class AvoidFlickerItemAnimatorTest {
    @Test
    fun testConstructor() {
        AvoidFlickerItemAnimator().apply {
            assertFalse(supportsChangeAnimations)
            assertEquals(0, changeDuration)
        }
    }
}
