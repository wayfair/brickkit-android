package com.wayfair.brickkitdemo.simplebrick.datamodel

import com.wayfair.brickkit.brick.DataModel

/**
 * Data model for text bricks.
 */
class TextDataModel(initialText: String) : DataModel() {

    var text: String = initialText
        private set(value) {
            field = value
            notifyChange()
        }

    /**
     * Append the string to the existing text.
     *
     * @param newText the new string to be appended
     */
    fun appendText(newText: String) {
        text += newText
    }
}
