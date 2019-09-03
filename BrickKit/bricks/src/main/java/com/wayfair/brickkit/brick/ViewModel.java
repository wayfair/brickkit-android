package com.wayfair.brickkit.brick;

import java.io.Serializable;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;
import androidx.databinding.BaseObservable;

/**
 * The object used to bind information in a {@link ViewModelBrick}.
 *
 * @param <DM> the {@link DataModel} type that drives this
 */
public abstract class ViewModel<DM extends DataModel> extends BaseObservable implements DataModel.DataModelUpdateListener, Serializable {
    protected DM dataModel;
    private transient Set<ViewModelUpdateListener> updateListeners;

    /**
     * Get the {@link ViewModelUpdateListener}. This is required in order to protect from a NPE.
     *
     * @return the set of {@link ViewModelUpdateListener}
     */
    protected @NonNull Set<ViewModelUpdateListener> getUpdateListeners() {
        if (updateListeners == null) {
            updateListeners = new ArraySet<>();
        }

        return updateListeners;
    }

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
     * Add an {@link ViewModelUpdateListener} to the list of items watching for changes.
     *
     * @param updateListener the object that is watching
     */
    public void addUpdateListener(ViewModelUpdateListener updateListener) {
        getUpdateListeners().add(updateListener);
    }

    @Override
    public void notifyChange() {
        super.notifyChange();
        for (ViewModelUpdateListener updateListener : getUpdateListeners()) {
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
     * Determines if the {@link DataModel} is ready, meaning the ViewModel is ready.
     *
     * @return if the data model is ready, IE fully populated
     */
    public boolean isDataModelReady() {
        return dataModel != null && dataModel.isReady();
    }

    /**
     * Interface for listening to changes.
     */
    public interface ViewModelUpdateListener {
        /**
         * Called when notify change is called.
         */
        void onChange();
    }
}
