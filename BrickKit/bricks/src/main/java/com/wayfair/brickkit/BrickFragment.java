/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.brick.BaseBrick;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Fragment which provides a simple interface for adding bricks / behaviors.
 */
public abstract class BrickFragment extends Fragment {

    public BrickDataManager dataManager = new BrickDataManager(maxSpans());
    @ColorInt
    private int recyclerViewBackground = Color.TRANSPARENT;
    private boolean useBricks = true;

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (useBricks) {
            View view;
            if (orientation() == OrientationHelper.VERTICAL) {
                view = inflater.inflate(R.layout.vertical_fragment_brick, container, false);
            } else {
                view = inflater.inflate(R.layout.horizontal_fragment_brick, container, false);
            }

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            recyclerView.setBackgroundColor(recyclerViewBackground);
            ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            dataManager.setRecyclerView(getContext(), recyclerView, orientation(), reverse(), view);
            return view;
        } else {
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        if (useBricks) {
            dataManager.onDestroyView();
        }
        super.onDestroyView();
    }

    /**
     * Get the max spans for this fragment.
     *
     * @return the max spans for this fragment.
     */
    public int maxSpans() {
        return BaseBrick.DEFAULT_MAX_SPAN_COUNT;
    }

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
    @ColorInt
    public int getRecyclerViewBackground() {
        return recyclerViewBackground;
    }

    public void setUseBricks(boolean useBricks) {
        this.useBricks = useBricks;
    }
}
