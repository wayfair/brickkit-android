package com.wayfair.brickkit.models;

import com.wayfair.brickkit.brick.ViewModel;

import androidx.databinding.Bindable;

/**
 * A view model for a text brick.
 */
public class TextViewModel extends ViewModel<TextDataModel> {

    /**
     * Constructor.
     *
     * @param dataModel the {@link TextDataModel}
     */
    public TextViewModel(TextDataModel dataModel) {
        super(dataModel);
    }

    /**
     * Method that data binding uses to set the view's text.
     *
     * @return the text
     */
    @Bindable
    public String getText() {
        return getDataModel().getText();
    }
}
