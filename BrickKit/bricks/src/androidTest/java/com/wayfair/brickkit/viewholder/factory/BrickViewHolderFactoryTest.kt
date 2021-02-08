/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.viewholder.factory

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wayfair.brickkit.BrickRecyclerAdapter
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.test.R
import com.wayfair.brickkit.viewholder.BrickViewHolder
import com.wayfair.brickkit.viewholder.factory.BrickViewHolderFactory.EmptyBrickViewHolder
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrickViewHolderFactoryTest {
    private val factory = BrickViewHolderFactory()
    private val provider: BrickProvider = mock()
    private lateinit var parentView: ViewGroup

    @Before
    fun setup() {
        val brick = mock<BaseBrick>()
        whenever(brick.createViewHolder(any())).thenAnswer { answer -> TestBrickViewHolder(answer.arguments[0] as View) }

        val placeholderBrick = mock<BaseBrick>()
        whenever(placeholderBrick.createViewHolder(any())).thenAnswer { answer -> PlaceholderBrickViewHolder(answer.arguments[0] as View) }

        whenever(provider.brickWithLayout(VALID_RES_ID)).thenReturn(brick)
        whenever(provider.brickWithLayout(EXCEPTIONAL_RES_ID)).thenThrow(Resources.NotFoundException())
        whenever(provider.brickWithPlaceholderLayout(PLACEHOLDER_RES_ID)).thenReturn(placeholderBrick)

        parentView = FrameLayout(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testCreateBrickViewHolder_defaultResId() {
        assertTrue(factory.createBrickViewHolder(parentView, BrickRecyclerAdapter.DEFAULT_LAYOUT_RES_ID, provider) is EmptyBrickViewHolder)
    }

    @Test
    fun testCreateBrickViewHolder_provideNeither() {
        assertTrue(factory.createBrickViewHolder(parentView, VALID_NON_MATCHED_RES_ID, provider) is EmptyBrickViewHolder)
    }

    @Test
    fun testCreateBrickViewHolder_resourceNotFoundException() {
        assertTrue(factory.createBrickViewHolder(parentView, EXCEPTIONAL_RES_ID, provider) is EmptyBrickViewHolder)
    }

    @Test
    fun testCreateBrickViewHolder_provideBrickWithLayout() {
        assertTrue(factory.createBrickViewHolder(parentView, VALID_RES_ID, provider) is TestBrickViewHolder)
    }

    @Test
    fun testCreateBrickViewHolder_provideBrickWithPlaceholderLayout() {
        assertTrue(factory.createBrickViewHolder(parentView, PLACEHOLDER_RES_ID, provider) is PlaceholderBrickViewHolder)
    }

    private class TestBrickViewHolder(itemView: View) : BrickViewHolder(itemView)
    private class PlaceholderBrickViewHolder(itemView: View) : BrickViewHolder(itemView)

    companion object {
        private val VALID_RES_ID = R.layout.text_brick_vm
        private val PLACEHOLDER_RES_ID = R.layout.text_brick_vm_placeholder
        private const val VALID_NON_MATCHED_RES_ID = 1
        private const val EXCEPTIONAL_RES_ID = 2
    }
}
