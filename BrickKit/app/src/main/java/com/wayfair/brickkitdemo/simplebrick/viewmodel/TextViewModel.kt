package com.wayfair.brickkitdemo.simplebrick.viewmodel

import androidx.databinding.Bindable
import com.wayfair.brickkit.brick.ViewModel
import com.wayfair.brickkitdemo.simplebrick.datamodel.TextDataModel

/**
 * A view model for a text brick.
 */
class TextViewModel(dataModel: TextDataModel) : ViewModel<TextDataModel>(dataModel) {
    /**
     * Method that data binding uses to set the view's text.
     *
     * @return the text
     */
    @get:Bindable
    val text: String
        get() = dataModel.text
}
