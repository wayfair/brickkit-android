package com.wayfair.brickkit.brick;

import android.databinding.BaseObservable;

/**
 * The object used to bind information in a {@link ViewModelBrick}.
 *
 * @param <DM> the {@link DataModel} type that drives this
 */
public abstract class ViewModel<DM extends DataModel> extends BaseObservable implements DataModel.DataModelUpdateListener {
    private final DM dataModel;

    /**
     * Constructor. Automatically gets tied to the {@link DataModel} as
     * a {@link com.wayfair.brickkit.brick.DataModel.DataModelUpdateListener}.
     *
     * @param dataModel the data model that is used to create the ViewModel
     */
    public ViewModel(DM dataModel) {
        this.dataModel = dataModel;

        this.dataModel.addUpdateListener(this);
    }

    /**
     * Gets the underlying {@link DataModel}.
     *
     * @return the {@link DataModel}
     */
    public DM getDataModel() {
        return dataModel;
    }
}
