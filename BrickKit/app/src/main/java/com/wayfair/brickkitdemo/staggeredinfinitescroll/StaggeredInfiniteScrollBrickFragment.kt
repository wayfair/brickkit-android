/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.staggeredinfinitescroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wayfair.brickkit.OnReachedItemAtPosition
import com.wayfair.brickkit.padding.BrickPaddingFactory
import com.wayfair.brickkit.size.FullWidthBrickSize
import com.wayfair.brickkit.size.HalfWidthBrickSize
import com.wayfair.brickkitdemo.BrickFragment
import com.wayfair.brickkitdemo.R
import com.wayfair.brickkitdemo.bricks.TextBrick

/**
 * Example fragment which loads more bricks when scrolling to the bottom.
 *
 * This fragment takes advantage of the [OnReachedItemAtPosition] which calls back when
 * items are bound in the adapter.
 */
class StaggeredInfiniteScrollBrickFragment : BrickFragment() {
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addNewBricks()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        super.onCreateView(inflater, container, savedInstanceState)?.apply {
            findViewById<RecyclerView>(R.id.recycler_view).layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                    isItemPrefetchEnabled = false
                }
        }


    override fun onResume() {
        super.onResume()

        dataManager.setOnReachedItemAtPosition(
            object : OnReachedItemAtPosition {
                override fun bindingItemAtPosition(position: Int) {
                    if (position == dataManager.recyclerViewItems.lastIndex) {
                        page++
                        addNewBricks()
                    }
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()

        dataManager.setOnReachedItemAtPosition(null)
    }

    private fun addNewBricks() {
        dataManager.addLast(
            (0 until PAGE_SIZE).map { i ->
                TextBrick(
                    if (i % 19 == 0) FullWidthBrickSize() else HalfWidthBrickSize(),
                    BrickPaddingFactory(resources),
                    when {
                        i % 19 == 0 -> "Brick: $page fullsize $i"
                        i % 4 == 0 -> "Brick: $page multi\nline $i"
                        else -> "Brick: $page $i"
                    }
                )
            }
        )
    }

    companion object {
        private const val PAGE_SIZE = 100
    }
}
