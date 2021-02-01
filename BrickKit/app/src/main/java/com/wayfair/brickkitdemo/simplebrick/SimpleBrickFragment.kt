/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.simplebrick

import android.os.Bundle
import com.wayfair.brickkit.brick.ViewModelBrick
import com.wayfair.brickkit.padding.BrickPaddingFactory
import com.wayfair.brickkit.size.FullPhoneFullHalfTabletBrickSize
import com.wayfair.brickkitdemo.BR
import com.wayfair.brickkitdemo.BrickFragment
import com.wayfair.brickkitdemo.R
import com.wayfair.brickkitdemo.simplebrick.datamodel.TextDataModel
import com.wayfair.brickkitdemo.simplebrick.viewmodel.TextViewModel
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Example fragment which shows text bricks and updates each second.
 */
class SimpleBrickFragment : BrickFragment() {
    private val executor = ScheduledThreadPoolExecutor(1)

    private val dataModels by lazy {
        (0 until (arguments?.getInt(NUMBER_OF_BRICKS) ?: DEFAULT_BRICK_COUNT)).map { i ->
            TextDataModel("Brick: $i")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataModels.forEach { dataModel ->
            ViewModelBrick.Builder(R.layout.text_brick_vm)
                .setPadding(BrickPaddingFactory(resources).getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.eight_dp))
                .setSpanSize(FullPhoneFullHalfTabletBrickSize())
                .addViewModel(BR.viewModel, TextViewModel(dataModel))
                .build()
                .addLastTo(dataManager)
        }
    }

    override fun onResume() {
        super.onResume()

        dataModels.forEach { dataModel ->
            executor.scheduleAtFixedRate({ dataModel.appendText(" Hello") }, 0, 1, TimeUnit.SECONDS)
        }
    }

    override fun onPause() {
        super.onPause()

        executor.purge()
    }

    companion object {
        private const val NUMBER_OF_BRICKS = "number_of_bricks"
        private const val DEFAULT_BRICK_COUNT = 100

        /**
         * Create a new instance of a SimpleBrickFragment.
         *
         * @param numberOfBricks The number of bricks you want in the fragment
         * @return The SimpleBrickFragment you created
         */
        fun newInstance(numberOfBricks: Int = DEFAULT_BRICK_COUNT) = SimpleBrickFragment().apply {
            arguments = Bundle().apply {
                putInt(NUMBER_OF_BRICKS, numberOfBricks)
            }
        }
    }
}
