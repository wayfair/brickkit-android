package com.wayfair.brickkit.animator;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

public class MyItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX,
            int fromY, int toX, int toY) {

        setChangeDuration(0);
        setSupportsChangeAnimations(false);

        return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY);
    }
}
