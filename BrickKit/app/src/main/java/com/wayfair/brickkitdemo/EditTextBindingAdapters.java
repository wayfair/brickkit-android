package com.wayfair.brickkitdemo;

import com.google.android.material.textfield.TextInputEditText;

import androidx.databinding.BindingAdapter;

/**
 * Binding adapters for {@link TextInputEditText}.
 */
public final class EditTextBindingAdapters {

    /**
     * Private Constructor.
     */
    private EditTextBindingAdapters() {

    }

    /**
     * Bind the {@link android.text.TextWatcher} to the {@link TextInputEditText}.
     *
     * @param editText the {@link TextInputEditText}
     * @param controllerViewModel the {@link ControllerViewModel} that provides
     *                            a {@link android.text.TextWatcher}
     */
    @BindingAdapter("textChangedListener")
    public static void bindTextWatcher(TextInputEditText editText, ControllerViewModel controllerViewModel) {
        editText.addTextChangedListener(controllerViewModel.getTextWatcher());
        editText.setSelection(controllerViewModel.getValue().length());
    }
}