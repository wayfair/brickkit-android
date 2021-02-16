/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.addremove

import android.os.Bundle
import com.wayfair.brickkit.brick.ViewModelBrick
import com.wayfair.brickkit.padding.BrickPaddingFactory
import com.wayfair.brickkit.size.FullWidthBrickSize
import com.wayfair.brickkitdemo.BR
import com.wayfair.brickkitdemo.BrickFragment
import com.wayfair.brickkitdemo.R
import com.wayfair.brickkitdemo.addremove.datamodel.ControllerDataModel
import com.wayfair.brickkitdemo.addremove.viewmodel.ControllerViewModel
import com.wayfair.brickkitdemo.addremove.viewmodel.ControllerViewModel.Interactions
import com.wayfair.brickkitdemo.bricks.TextBrick

/**
 * Demo fragment that allows you to add bricks to the end and remove bricks from the beginning.
 */
class AddRemoveBrickFragment : BrickFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val brickPaddingFactory = BrickPaddingFactory(resources)
        val dataModel = ControllerDataModel(NUMBER_OF_BRICKS)

        ViewModelBrick.Builder(R.layout.controller_brick_vm)
            .setSpanSize(FullWidthBrickSize())
            .setPadding(brickPaddingFactory.getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.eight_dp))
            .addViewModel(
                BR.viewModel,
                ControllerViewModel(
                    dataModel,
                    object : Interactions {
                        override fun onRemoveClicked() {
                            dataManager.recyclerViewItems.firstOrNull { brick -> brick is TextBrick }?.let { dataManager.removeItem(it) }
                        }

                        override fun onAddClicked() {
                            dataManager.addLast(TextBrick(FullWidthBrickSize(), brickPaddingFactory, "Brick: ${dataModel.value++}"))
                        }
                    }
                )
            )
            .build()
            .addLastTo(dataManager)

        dataManager.addLast(
            (0 until NUMBER_OF_BRICKS).map { i -> TextBrick(FullWidthBrickSize(), brickPaddingFactory, "Brick: $i") }
        )
    }

    companion object {
        private const val NUMBER_OF_BRICKS = 20
    }
}
