/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Fragment which provides a simple interface for adding bricks / behaviors.
 */
public abstract class BrickFragment extends Fragment {
    public BrickDataManager dataManager = new BrickDataManager();
    @ColorInt
    private int recyclerViewBackground = Color.TRANSPARENT;

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vertical_fragment_brick, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setBackgroundColor(recyclerViewBackground);
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        dataManager.setRecyclerView(recyclerView);
        return view;
    }

    @Override
    public void onDestroyView() {
        dataManager.onDestroyView();
        super.onDestroyView();
    }

    /**
     * Set the background color at anytime during the process. If the view is created we will set the BG on it now, if not, we'll store it and use it
     * later.
     *
     * @param backgroundColor The background color we want.
     */
    public void setRecyclerViewBackground(@ColorInt int backgroundColor) {
        recyclerViewBackground = backgroundColor;

        if (dataManager.getRecyclerView() != null) {
            dataManager.getRecyclerView().setBackgroundColor(recyclerViewBackground);
        }
    }

    /**
     * Get the background color for the recycler view.
     *
     * @return The color int used for the bg of the Recycler View
     */
    @ColorInt
    public int getRecyclerViewBackground() {
        return recyclerViewBackground;
    }
}
