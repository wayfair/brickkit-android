/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.base.viewmodel;

import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wayfair.brickkit.base.view.MvvmView;

/**
 * Class from which every view model in project should be extended.
 * @param <V> Define a View connected with ViewModel
 */
public abstract class BaseViewModel<V extends MvvmView> extends BaseObservable implements MvvmViewModel<V> {

    private V mView;

    /**
     * Return instance of view.
     * @return instance of view
     */
    public V getView() {
        return mView;
    }

    /**
     * Add logic on view attach.
     * @param view View that will be attached to ViewModel
     * @param savedInstanceState State that will be use for restoring
     */
    @Override
    @CallSuper
    public void attachView(@NonNull V view, @Nullable Bundle savedInstanceState) {
        mView = view;

        restoreInstanceState(savedInstanceState);

        addObservables();
    }

    /**
     * Add logic on view detach.
     */
    @Override
    @CallSuper
    public void detachView() {
        mView = null;
    }

    /**
     * Override saving state logic on ViewModel.
     * @param outState State that will be saved
     */
    @Override
    public void saveInstanceState(@NonNull Bundle outState) {
    }

    /**
     * Override restoring state logic on ViewModel.
     * @param savedInstanceState State that will restored
     */
    @Override
    @CallSuper
    public void restoreInstanceState(@Nullable Bundle savedInstanceState) {
    }

    /**
     * This is the place where you can observe for all events related for this ViewModel.
     */
    @Override
    @CallSuper
    public void addObservables() {
    }
}
