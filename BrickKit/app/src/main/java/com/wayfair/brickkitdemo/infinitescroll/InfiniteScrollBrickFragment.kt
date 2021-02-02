/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.infinitescroll

import android.os.Bundle
import com.wayfair.brickkit.OnReachedItemAtPosition
import com.wayfair.brickkit.padding.BrickPaddingFactory
import com.wayfair.brickkit.size.FullPhoneFullHalfTabletBrickSize
import com.wayfair.brickkitdemo.BrickFragment
import com.wayfair.brickkitdemo.bricks.TextBrick

/**
 * Example fragment which loads more bricks when scrolling to the bottom.
 *
 * This fragment takes advantage of the [OnReachedItemAtPosition] which calls back when
 * items are bound in the adapter.
 */
class InfiniteScrollBrickFragment : BrickFragment() {
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addNewBricks()
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
            (0 until PAGE_SIZE).map { i -> TextBrick(FullPhoneFullHalfTabletBrickSize(), BrickPaddingFactory(resources), "Brick: $page $i") }
        )
    }

    companion object {
        private const val PAGE_SIZE = 100
    }
}
