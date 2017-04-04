/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.base.viewmodel;

import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wayfair.brickkit.base.view.MvvmView;

/**
 * This interface should be implemented by every ViewModel in project
 * @param <V> View that will be attach for ViewModel
 */
public interface MvvmViewModel<V extends MvvmView> extends Observable {

    /**
     * Add logic on view attach
     * @param view View that will be attached to ViewModel
     * @param savedInstanceState State that will be use for restoring
     */
    void attachView(V view, Bundle savedInstanceState);

    /**
     * Add logic on view detach
     */
    void detachView();

    /**
     * Override saving state logic on ViewModel
     * @param outState State that will be saved
     */
    void saveInstanceState(@NonNull Bundle outState);

    /**
     * Override restoring state logic on ViewModel
     * @param savedInstanceState State that will restored
     */
    void restoreInstanceState(@Nullable Bundle savedInstanceState);

    /**
     * This is the place where you can observe for all events related for this ViewModel
     */
    void addObservables();
}
