package com.wayfair.brickkitdemo;

import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;

import com.wayfair.brickkit.brick.ViewModel;

/**
 * A sample brick.
 *
 * Copyright © 2017 Wayfair. All rights reserved.
 */
public class ControllerViewModel extends ViewModel<ControllerDataModel> {
    private final Runnable removeAction;
    private final Runnable addAction;

    /**
     * Constructor.
     *
     * @param dataModel the data model to drive the view model
     * @param removeAction the runnable for removing an item
     * @param addAction the runnable for adding an item
     */
    public ControllerViewModel(ControllerDataModel dataModel, Runnable removeAction, Runnable addAction) {
        super(dataModel);

        this.removeAction = removeAction;
        this.addAction = addAction;
    }

    /**
     * Get the value of the {@link ControllerDataModel#getValue()}.
     *
     * @return the {@link ControllerDataModel#getValue()}
     */
    @Bindable
    public String getValue() {
        return String.valueOf(getDataModel().getValue());
    }

    /**
     * Run the remove runnable.
     */
    public void remove() {
        removeAction.run();
    }

    /**
     * Run the add runnable.
     */
    public void add() {
        addAction.run();
    }

    /**
     * Create a new {@link TextWatcher}.
     *
     * @return the new {@link TextWatcher}
     */
    public TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    getDataModel().setValue(Integer.valueOf(charSequence.toString()));
                } catch (NumberFormatException e) {
                    getDataModel().setValue(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
}
