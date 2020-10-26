package com.wayfair.brickkitdemo.datamodel;

import com.wayfair.brickkit.brick.DataModel;

/**
 * Data model for text bricks.
 */
public class TextDataModel extends DataModel {
    private String text;

    /**
     * Constructor.
     *
     * @param text the text
     */
    public TextDataModel(String text) {
        this.text = text;
    }

    /**
     * Return the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Set a new value for text.
     *
     * @param text the new text
     */
    public void setText(String text) {
        this.text = text;
        notifyChange();
    }

    /**
     * Append the string to the existing text.
     *
     * @param newText the new sting to be appended
     */
    public void appendText(String newText) {
        setText(text + newText);
    }
}
