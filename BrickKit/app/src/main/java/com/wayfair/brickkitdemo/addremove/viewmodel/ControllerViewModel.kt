/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.addremove.viewmodel

import android.view.View
import androidx.databinding.Bindable
import com.wayfair.brickkit.brick.ViewModel
import com.wayfair.brickkitdemo.addremove.datamodel.ControllerDataModel

/**
 * A sample brick.
 */
class ControllerViewModel(
    dataModel: ControllerDataModel,
    private val interactions: Interactions
) : ViewModel<ControllerDataModel>(dataModel) {

    @get:Bindable
    val value: String
        get() = dataModel.value.toString()

    val addClickListener = View.OnClickListener { interactions.onAddClicked() }
    val removeClickListener = View.OnClickListener { interactions.onRemoveClicked() }

    interface Interactions {
        fun onAddClicked()
        fun onRemoveClicked()
    }
}
