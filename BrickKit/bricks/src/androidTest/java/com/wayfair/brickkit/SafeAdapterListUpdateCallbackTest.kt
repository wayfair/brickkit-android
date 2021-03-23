package com.wayfair.brickkit

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.runner.RunWith
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class SafeAdapterListUpdateCallbackTest {

    private val brickRecyclerAdapter: BrickRecyclerAdapter = mock()
    private lateinit var adapter: SafeAdapterListUpdateCallback

    @Before
    fun setup() {
        adapter = SafeAdapterListUpdateCallback(brickRecyclerAdapter)
    }

    @Test
    fun testOnInserted() {
        val position = 3
        val count = 1
        adapter.onInserted(position, count)
        verify(brickRecyclerAdapter).safeNotifyItemRangeInserted(position, count)
    }

    @Test
    fun testOnRemoved() {
        val position = 4
        val count = 1
        adapter.onRemoved(position, count)
        verify(brickRecyclerAdapter).safeNotifyItemRangeRemoved(position, count)
    }

    @Test
    fun testOnMoved() {
        val position = 5
        val count = 9
        adapter.onMoved(position, count)
        verify(brickRecyclerAdapter).safeNotifyItemMoved(position, count)
    }

    @Test
    fun testOnChanged() {
        val position = 2
        val count = 6
        val any: Any = mock()
        adapter.onChanged(position, count, any)
        verify(brickRecyclerAdapter).safeNotifyItemRangeChanged(position, count, any)
    }
}
