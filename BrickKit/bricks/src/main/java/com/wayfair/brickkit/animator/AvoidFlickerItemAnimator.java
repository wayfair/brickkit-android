package com.wayfair.brickkit.animator;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Item animator which overrides
 * {@link DefaultItemAnimator#animateChange(RecyclerView.ViewHolder, RecyclerView.ViewHolder, int, int, int, int)}
 * in order to avoid flickering when replacing ViewHolders.
 */
public class AvoidFlickerItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX,
            int fromY, int toX, int toY) {

        setChangeDuration(0);
        setSupportsChangeAnimations(false);

        return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY);
    }
}
