/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.mainfragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.size.BrickSize
import com.wayfair.brickkit.size.PercentageBrickSize
import com.wayfair.brickkitdemo.AddRemoveBrickFragment
import com.wayfair.brickkitdemo.BrickFragment
import com.wayfair.brickkitdemo.infinitescroll.InfiniteScrollBrickFragment
import com.wayfair.brickkitdemo.R
import com.wayfair.brickkitdemo.simplebrick.SimpleBrickFragment
import com.wayfair.brickkitdemo.StaggeredInfiniteScrollBrickFragment
import com.wayfair.brickkitdemo.fragmentbrick.FragmentBrickFragment
import com.wayfair.brickkitdemo.mainfragment.bricks.ActiveBrick
import com.wayfair.brickkitdemo.mainfragment.bricks.PassiveBrick
import java.util.concurrent.TimeUnit

/**
 * Main fragment that links to the other child fragments. Additionally this shows an example of placeholders.
 */
class MainActivityFragment : BrickFragment() {
    private var rowNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val placeholderBrick = ActiveBrick(resources, R.string.empty) { changeFragment(SimpleBrickFragment.newInstance()) }
            .also {
                // Fake waiting as in a API request to show off placeholder when data is not ready
                Handler(Looper.getMainLooper()).postDelayed(
                    { it.text = getString(R.string.simple_brick_title) },
                    TimeUnit.SECONDS.toMillis(2)
                )
            }

        addRow(PassiveBrick(TWO_FIFTH, resources))
        addRow(placeholderBrick)
        addRow(ActiveBrick(resources, R.string.add_remove_brick_title) { changeFragment(AddRemoveBrickFragment()) })
        addRow(ActiveBrick(resources, R.string.infinite_scroll_brick_title) { changeFragment(InfiniteScrollBrickFragment()) })
        addRow(ActiveBrick(resources, R.string.staggered_infinite_scroll_brick_title) { changeFragment(StaggeredInfiniteScrollBrickFragment()) })
        addRow(ActiveBrick(resources, R.string.fragment_brick_title) { changeFragment(FragmentBrickFragment()) })
        addRow(PassiveBrick(TWO_FIFTH, resources))
    }

    private fun addRow(middleBrick: BaseBrick) {
        dataManager.addLast(PassiveBrick(if (rowNumber % 2 == 0) ONE_FIFTH else TWO_FIFTH, resources))
        dataManager.addLast(middleBrick)
        dataManager.addLast(PassiveBrick(if (rowNumber % 2 == 0) TWO_FIFTH else ONE_FIFTH, resources))
        rowNumber += 1
    }

    private fun changeFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()?.replace(R.id.content, fragment)?.addToBackStack(null)?.commit()
    }

    companion object {
        private val ONE_FIFTH: BrickSize = PercentageBrickSize(.2f)
        private val TWO_FIFTH: BrickSize = PercentageBrickSize(.4f)
    }
}
