/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.behavior;

import com.wayfair.brickkit.BrickRecyclerAdapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Abstract class to extend if you want to implement behaviors which work on
 * bricks, like {@link StickyHeaderBehavior} and {@link StickyFooterBehavior}.
 */
public abstract class BrickBehavior extends RecyclerView.OnScrollListener {
    private boolean attached = false;

    /**
     * Method that is called when the dataset for {@link BrickRecyclerAdapter} has changed.
     */
    public abstract void onDataSetChanged();

    /**
     * Method that is called when the {@link RecyclerView} has scrolled.
     */
    public abstract void onScroll();

    /**
     * Implement this method to attach the behaviour to the given {@link RecyclerView}.
     *
     * @param recyclerView The {@link RecyclerView} to attach to.
     *
     * @return Return TRUE when the behaviour was successfully attached to the recycler view.
     */
    protected abstract boolean attach(RecyclerView recyclerView);

    /**
     * Method that is called by {@link com.wayfair.brickkit.BrickDataManager} when a behaviour is added and it needs to be attached to the
     * {@link RecyclerView}.
     *
     * @param recyclerView The {@link RecyclerView} to attach to.
     */
    public void attachToRecyclerView(RecyclerView recyclerView) {
        if (attached || recyclerView == null) {
            return;
        }

        attached = attach(recyclerView);
    }

    /**
     * Implement this method to attach the behaviour to the given {@link RecyclerView}.
     *
     * @param recyclerView The {@link RecyclerView} to attach to.
     *
     * @return Return TRUE when the behaviour was successfully detached from the RecyclerView
     */
    protected abstract boolean detach(RecyclerView recyclerView);

    /**
     * Method that is called by {@link com.wayfair.brickkit.BrickDataManager} when a behaviour is removed and it needs to be detached from the
     * {@link RecyclerView}.
     *
     * @param recyclerView The {@link RecyclerView} to attach to.
     */
    public void detachFromRecyclerView(RecyclerView recyclerView) {
        if (!attached || recyclerView == null) {
            return;
        }

        attached = !detach(recyclerView);
    }
}
