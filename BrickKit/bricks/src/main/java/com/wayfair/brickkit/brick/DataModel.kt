package com.wayfair.brickkit.brick

import android.os.Handler
import android.os.Looper
import java.io.Serializable

/**
 * An abstract class that all Data Models should be based on. This is use tightly
 * with [ViewModel]s and will automatically notify update listeners
 * when [notifyChange] is called.
 */
abstract class DataModel : Serializable {
    @Transient
    private val updateListeners = mutableSetOf<DataModelUpdateListener>()

    /**
     * Add a [DataModelUpdateListener] to the list of listeners.
     *
     * @param updateListener the listener to add
     */
    fun addUpdateListener(updateListener: DataModelUpdateListener) {
        updateListeners.add(updateListener)
    }

    /**
     * Remove a [DataModelUpdateListener] to the list of listeners.
     *
     * @param updateListener the listener to remove
     */
    fun removeUpdateListener(updateListener: DataModelUpdateListener) {
        updateListeners.remove(updateListener)
    }

    /**
     * This function is called when you are ready to notify listeners that the data has changed.
     */
    fun notifyChange() {
        val handler = Handler(Looper.getMainLooper())

        updateListeners.forEach { updateListener ->
            handler.post { updateListener.notifyChange() }
        }
    }

    /**
     * Determines if the DataModel is ready to be processed by a [ViewModel].
     *
     * @return true if the data is ready, defaults to true
     */
    open fun isReady(): Boolean = true

    /**
     * The interface required to be implemented in order to listen for changes on [DataModel]s.
     */
    interface DataModelUpdateListener {

        /**
         * Should handle updating whatever is listening to a [DataModel].
         */
        fun notifyChange()
    }
}
