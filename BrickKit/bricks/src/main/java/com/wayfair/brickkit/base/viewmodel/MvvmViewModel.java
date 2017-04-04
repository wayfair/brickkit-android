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
 * @param <V>
 */
public interface MvvmViewModel<V extends MvvmView> extends Observable {

    void attachView(V view, Bundle savedInstanceState, boolean isAttachedOnBackPressed);

    void detachView();

    void saveInstanceState(@NonNull Bundle outState);

    void restoreInstanceState(@Nullable Bundle savedInstanceState);

    void addObservables();
}
