/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.wayfair.brickkit.BrickDataManager;

/**
 * Fragment which provides a simple interface for adding bricks / behaviors.
 */
public class BrickFragment extends Fragment {
    public BrickDataManager dataManager = new BrickDataManager();

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vertical_fragment_brick, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        dataManager.setRecyclerView(recyclerView);
        return view;
    }

    @Override
    public void onDestroyView() {
        dataManager.onDestroyView();
        super.onDestroyView();
    }
}
