package com.wayfair.brickkit.brick;

import android.support.v4.util.ArraySet;

import java.util.Set;

/**
 * An abstract class that all Data Models should be based on. This is use tightly
 * with {@link ViewModel}s and will automatically notify update listeners
 * when {@link #notifyChange()} is called.
 */
public abstract class DataModel {
    private final Set<DataModelUpdateListener> updateListeners = new ArraySet<>();

    /**
     * Add an {@link DataModelUpdateListener} to the list of listeners.
     *
     * @param updateListener the listener to add
     */
    public void addUpdateListener(DataModelUpdateListener updateListener) {
        updateListeners.add(updateListener);
    }

    /**
     * This function is called when you are ready to notify listeners that the data has changed.
     */
    public void notifyChange() {
        for (DataModelUpdateListener updateListener : updateListeners) {
            updateListener.notifyChange();
        }
    }

    /**
     * The interface required to be implemented oin order to listen for changes
     * on {@link DataModel}s.
     */
    public interface DataModelUpdateListener {
        /**
         * Should handle updating whatever is listening to a {@link DataModel}.
         */
        void notifyChange();
    }
}
