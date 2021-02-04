package com.wayfair.brickkit.brick

import androidx.databinding.BaseObservable
import com.wayfair.brickkit.brick.DataModel.DataModelUpdateListener
import java.io.Serializable

/**
 * The object used to bind information in a [ViewModelBrick].
 */
abstract class ViewModel<DM : DataModel> protected constructor(
    open val dataModel: DM
) : BaseObservable(), DataModelUpdateListener, Serializable {

    @Transient
    private var updateListeners = mutableSetOf<ViewModelUpdateListener>()

    init {
        dataModel.addUpdateListener(this)
    }

    /**
     * Add an [ViewModelUpdateListener] to the list of items watching for changes.
     *
     * @param updateListener the object that is watching
     */
    open fun addUpdateListener(updateListener: ViewModelUpdateListener) {
        updateListeners.add(updateListener)
    }

    override fun notifyChange() {
        super.notifyChange()
        updateListeners.forEach { updateListener -> updateListener.onChange() }
    }

    /**
     * Determines if the [DataModel] is ready, meaning the ViewModel is ready.
     *
     * @return if the data model is ready, IE fully populated
     */
    open val isDataModelReady: Boolean
        get() = dataModel.isReady

    /**
     * Interface for listening to changes.
     */
    interface ViewModelUpdateListener {
        /**
         * Called when notify change is called.
         */
        fun onChange()
    }
}
