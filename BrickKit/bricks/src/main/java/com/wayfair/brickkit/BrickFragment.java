/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment which provides a simple interface for adding bricks / behaviors.
 */
public abstract class BrickFragment extends Fragment {
    public BrickDataManager dataManager = new BrickDataManager(maxSpans());
    @ColorInt private int recyclerViewBackground = Color.TRANSPARENT;

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (orientation() == OrientationHelper.VERTICAL) {
            view = inflater.inflate(R.layout.vertical_fragment_brick, container, false);
        } else {
            view = inflater.inflate(R.layout.horizontal_fragment_brick, container, false);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setBackgroundColor(recyclerViewBackground);
        dataManager.setRecyclerView(getContext(), recyclerView, orientation(), reverse(), view);
        addBehaviors();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataManager.onDestroyView();
    }

    /**
     * Get the max spans for this fragment.
     *
     * @return the max spans for this fragment.
     */
    public int maxSpans() {
        return 240;
    }

    /**
     * Method called to add behaviors to this fragment.
     */
    public void addBehaviors() { }

    /**
     * Get the orientation to lay out this fragment.
     *
     * @return the orientation to lay out this fragment.
     */
    public int orientation() {
        return GridLayoutManager.VERTICAL;
    }

    /**
     * Whether or not to reverse the layout of this fragment.
     *
     * @return true if this fragment should be laid out in reverse
     */
    public boolean reverse() {
        return false;
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
    @ColorInt public int getRecyclerViewBackground() {
        return recyclerViewBackground;
    }
}
