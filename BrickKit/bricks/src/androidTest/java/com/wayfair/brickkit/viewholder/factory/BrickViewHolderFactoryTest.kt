/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.viewholder.factory

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.mockito.kotlin.mock
import com.wayfair.brickkit.BrickRecyclerAdapter
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.test.R
import com.wayfair.brickkit.viewholder.factory.BrickViewHolderFactory.EmptyBrickViewHolder
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrickViewHolderFactoryTest {
    private val factory = BrickViewHolderFactory()
    private lateinit var bricks: List<BaseBrick>
    private lateinit var parentView: ViewGroup

    @Before
    fun setup() {
        val brick = mock<BaseBrick>()
        bricks = listOf(brick)
        parentView = FrameLayout(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testCreateBrickViewHolder_defaultResId() {
        assertTrue(factory.createBrickViewHolder(parentView, BrickRecyclerAdapter.DEFAULT_LAYOUT_RES_ID, bricks) is EmptyBrickViewHolder)
    }

    @Test
    fun testCreateBrickViewHolder_provideNeither() {
        assertTrue(factory.createBrickViewHolder(parentView, VALID_NON_MATCHED_RES_ID, bricks) is EmptyBrickViewHolder)
    }

    @Test
    fun testCreateBrickViewHolder_resourceNotFoundException() {
        assertTrue(factory.createBrickViewHolder(parentView, EXCEPTIONAL_RES_ID, bricks) is EmptyBrickViewHolder)
    }

    @Test
    fun testCreateBrickViewHolder_provideBrickWithLayout() {
        assertTrue(factory.createBrickViewHolder(parentView, VALID_RES_ID, bricks) is EmptyBrickViewHolder)
    }

    @Test
    fun testCreateBrickViewHolder_provideBrickWithPlaceholderLayout() {
        assertTrue(factory.createBrickViewHolder(parentView, PLACEHOLDER_RES_ID, bricks) is EmptyBrickViewHolder)
    }

    companion object {
        private val VALID_RES_ID = R.layout.text_brick_vm
        private val PLACEHOLDER_RES_ID = R.layout.text_brick_vm_placeholder
        private const val VALID_NON_MATCHED_RES_ID = 1
        private const val EXCEPTIONAL_RES_ID = 2
    }
}
