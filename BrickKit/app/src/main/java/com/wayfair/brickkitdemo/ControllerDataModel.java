package com.wayfair.brickkitdemo;

import com.wayfair.brickkit.brick.DataModel;

/**
 * Data model for a controller brick.
 */
public class ControllerDataModel extends DataModel {
    private int value;

    /**
     * Constructor with a starting value.
     *
     * @param value the starting value
     */
    public ControllerDataModel(int value) {
        this.value = value;
    }

    /**
     * Change the value.
     *
     * @param value the new value
     */
    public void setValue(int value) {
        this.value = value;
        notifyChange();
    }

    /**
     * Get the current value.
     *
     * @return the value
     */
    public int getValue() {
        return this.value;
    }
}
