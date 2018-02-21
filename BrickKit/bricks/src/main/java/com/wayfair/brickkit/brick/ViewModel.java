package com.wayfair.brickkit.brick;

import android.databinding.BaseObservable;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * The object used to bind information in a {@link ViewModelBrick}.
 *
 * @param <DM> the {@link DataModel} type that drives this
 */
public abstract class ViewModel<DM extends DataModel> extends BaseObservable implements DataModel.DataModelUpdateListener {
    protected DM dataModel;
    protected final Set<ViewModelUpdateListener> updateListeners = new HashSet<>();

    /**
     * Constructor. Automatically gets tied to the {@link DataModel} as
     * a {@link com.wayfair.brickkit.brick.DataModel.DataModelUpdateListener}.
     *
     * @param dataModel the data model that is used to create the ViewModel
     */
    public ViewModel(@Nullable DM dataModel) {
        setDataModel(dataModel);
    }

    /**
     * Add an {@link ViewModelUpdateListener} to the list of items watching for changes
     *
     * @param updateListener the object that is watching
     */
    public void addUpdateListener(ViewModelUpdateListener updateListener) {
        updateListeners.add(updateListener);
    }

    @Override
    public void notifyChange() {
        for (ViewModelUpdateListener updateListener : updateListeners) {
            updateListener.onChange();
        }
    }

    /**
     * Gets the underlying {@link DataModel}.
     *
     * @return the {@link DataModel}
     */
    public DM getDataModel() {
        return dataModel;
    }

    /**
     * Sets the underlying {@link DataModel}.
     *
     * @param dataModel the new {@link DataModel}
     */
    public void setDataModel(DM dataModel) {
        if (dataModel == null && this.dataModel != null) {
            this.dataModel.removeUpdateListener(this);
        }

        this.dataModel = dataModel;

        if (dataModel != null) {
            this.dataModel.addUpdateListener(this);
        }
    }

    /**
     * Determines if the {@link DataModel} is ready, meaning the ViewModel is ready
     *
     * @return if the data model is ready, IE fully populated
     */
    public boolean isDataModelReady() {
        return dataModel != null && dataModel.isReady();
    }

    /**
     * Interface for listening to changes
     */
    public interface ViewModelUpdateListener {
        /**
         * Called when notify change is called
         */
        void onChange();
    }
}
