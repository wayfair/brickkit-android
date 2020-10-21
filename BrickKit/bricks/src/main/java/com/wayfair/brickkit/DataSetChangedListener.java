package com.wayfair.brickkit;

/**
 * Listener which can be used to listen for data set changes.
 */
public interface DataSetChangedListener {
    /**
     * Callback that will be triggered when data set changes occur.
     */
    void onDataSetChanged();
}
