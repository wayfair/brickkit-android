/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.brick

import android.view.View
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.wayfair.brickkit.BrickDataManager
import com.wayfair.brickkit.viewholder.BrickViewHolder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.UnsupportedOperationException

class BaseBrickTest {
    private val brickDataManager: BrickDataManager = mock()

    private val brick: BaseBrick = object : BaseBrick(mock(), mock()) {
        override fun onBindData(holder: BrickViewHolder) {}
        override val layout = LAYOUT
        override fun createViewHolder(itemView: View): BrickViewHolder = mock()
    }

    @Test
    fun testHidden() {
        brick.setDataManager(brickDataManager)
        brick.setDataManager(null)

        assertFalse(brick.isHidden)

        brick.isHidden = true
        assertTrue(brick.isHidden)

        brick.isHidden = false
        assertFalse(brick.isHidden)

        verifyZeroInteractions(brickDataManager)
    }

    @Test
    fun testHidden_falseToTrue() {
        brick.isHidden = false
        brick.setDataManager(brickDataManager)

        brick.isHidden = true
        assertTrue(brick.isHidden)
        verify(brickDataManager).hideItem(brick)
    }

    @Test
    fun testHidden_falseToFalse() {
        brick.isHidden = false
        brick.setDataManager(brickDataManager)

        brick.isHidden = true
        assertTrue(brick.isHidden)
        verify(brickDataManager, never()).showItem(brick)
    }

    @Test
    fun testTag_nullDataManager() {
        brick.setDataManager(brickDataManager)
        brick.setDataManager(null)

        brick.tag = null
        brick.tag = TAG_1
        brick.tag = TAG_2
        brick.tag = null

        verifyZeroInteractions(brickDataManager)
    }

    @Test
    fun testTag_notNullToNotNull() {
        brick.tag = TAG_1

        brick.setDataManager(brickDataManager)

        brick.tag = TAG_2

        verify(brickDataManager).removeFromTagCache(brick)
        verify(brickDataManager).addToTagCache(brick)
    }

    @Test
    fun testTag_nullToNotNull() {
        brick.tag = null

        brick.setDataManager(brickDataManager)

        brick.tag = TAG_2

        verify(brickDataManager, never()).removeFromTagCache(brick)
        verify(brickDataManager).addToTagCache(brick)
    }

    @Test
    fun testTag_notNullToNull() {
        brick.tag = TAG_1

        brick.setDataManager(brickDataManager)

        brick.tag = null

        verify(brickDataManager).removeFromTagCache(brick)
        verify(brickDataManager, never()).addToTagCache(brick)
    }

    @Test
    fun testTag_nullToNull() {
        brick.tag = null

        brick.setDataManager(brickDataManager)

        brick.tag = null

        verifyZeroInteractions(brickDataManager)
    }

    @Test
    fun testTag_sameTag() {
        brick.tag = TAG_1

        brick.setDataManager(brickDataManager)

        brick.tag = TAG_1

        verifyZeroInteractions(brickDataManager)
    }

    @Test
    fun testLayout() {
        assertEquals(LAYOUT, brick.layout)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testPlaceholderLayout() {
        brick.placeholderLayout
    }

    @Test
    fun testInFirstRow() {
        assertFalse(brick.isInFirstRow)
        brick.isInFirstRow = true
        assertTrue(brick.isInFirstRow)
    }

    @Test
    fun testInLastRow() {
        assertFalse(brick.isInLastRow)
        brick.isInLastRow = true
        assertTrue(brick.isInLastRow)
    }

    @Test
    fun testOnLeftWall() {
        assertFalse(brick.isOnLeftWall)
        brick.isOnLeftWall = true
        assertTrue(brick.isOnLeftWall)
    }

    @Test
    fun testOnRightWall() {
        assertFalse(brick.isOnRightWall)
        brick.isOnRightWall = true
        assertTrue(brick.isOnRightWall)
    }

    @Test
    fun testRefreshItem() {
        brick.refreshItem()
        brick.setDataManager(brickDataManager)
        brick.refreshItem()

        verify(brickDataManager).refreshItem(brick)
    }

    @Test
    fun testAddLastTo() {
        brick.addLastTo(brickDataManager)

        verify(brickDataManager).addLast(brick)
    }

    @Test
    fun testAddFirstTo() {
        brick.addFirstTo(brickDataManager)

        verify(brickDataManager).addFirst(brick)
    }

    companion object {
        private const val LAYOUT = 1234
        private const val TAG_1 = "tag 1"
        private const val TAG_2 = "tag 2"
    }
}
