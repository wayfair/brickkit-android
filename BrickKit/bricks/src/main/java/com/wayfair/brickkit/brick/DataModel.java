package com.wayfair.brickkit.brick;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.ArraySet;

import java.io.Serializable;
import java.util.Set;

/**
 * An abstract class that all Data Models should be based on. This is use tightly
 * with {@link ViewModel}s and will automatically notify update listeners
 * when {@link #notifyChange()} is called.
 */
public abstract class DataModel implements Serializable {
    protected transient final Set<DataModelUpdateListener> updateListeners = new ArraySet<>();

    /**
     * Add an {@link DataModelUpdateListener} to the list of listeners.
     *
     * @param updateListener the listener to add
     */
    public void addUpdateListener(DataModelUpdateListener updateListener) {
        updateListeners.add(updateListener);
    }

    /**
     * Remove a {@link DataModelUpdateListener} to the list of listeners.
     *
     * @param updateListener the listener to remove
     */
    public void removeUpdateListener(DataModelUpdateListener updateListener) {
        updateListeners.remove(updateListener);
    }

    /**
     * This function is called when you are ready to notify listeners that the data has changed.
     */
    public void notifyChange() {
        for (final DataModelUpdateListener updateListener : updateListeners) {
            new Handler(Looper.getMainLooper()).post(
                    new Runnable() {
                        @Override
                        public void run() {
                            updateListener.notifyChange();
                        }
                    }
            );
        }
    }

    /**
     * Determines if the DataModel is ready to be processed by a {@link ViewModel}.
     *
     * @return true if the data is ready, defaults to true
     */
    public boolean isReady() {
        return true;
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
