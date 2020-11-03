/*
 * Copyright © 2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.size

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wayfair.brickkit.R
import org.junit.Assert

fun BrickSize.verifyGetSpans(portraitPhone: Int, landscapePhone: Int, portraitTablet: Int, landscapeTablet: Int) {
    val context = mock<Context>()
    val resources = mock<Resources>()
    val configuration = Configuration()

    whenever(resources.configuration).thenReturn(configuration)
    whenever(context.resources).thenReturn(resources)

    whenever(resources.getBoolean(R.bool.tablet)).thenReturn(false)
    configuration.orientation = Configuration.ORIENTATION_PORTRAIT
    Assert.assertEquals(portraitPhone, getSpans(context))

    whenever(resources.getBoolean(R.bool.tablet)).thenReturn(false)
    configuration.orientation = Configuration.ORIENTATION_LANDSCAPE
    Assert.assertEquals(landscapePhone, getSpans(context))

    whenever(resources.getBoolean(R.bool.tablet)).thenReturn(true)
    configuration.orientation = Configuration.ORIENTATION_PORTRAIT
    Assert.assertEquals(portraitTablet, getSpans(context))

    whenever(resources.getBoolean(R.bool.tablet)).thenReturn(true)
    configuration.orientation = Configuration.ORIENTATION_LANDSCAPE
    Assert.assertEquals(landscapeTablet, getSpans(context))
}
