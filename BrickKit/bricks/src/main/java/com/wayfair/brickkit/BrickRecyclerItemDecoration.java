/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPadding;

/**
 * {@link android.support.v7.widget.RecyclerView.ItemDecoration} which applies padding to bricks
 * based off of their given {@link BrickPadding} and location in on the screen.
 */
class BrickRecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private BrickDataManager brickDataManager;

    /**
     * Constructor.
     *
     * @param dataManager {@link BrickDataManager} to use to get bricks for getting offsets
     */
    BrickRecyclerItemDecoration(BrickDataManager dataManager) {
        this.brickDataManager = dataManager;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == -1
                || brickDataManager != ((BrickRecyclerAdapter) parent.getAdapter()).getBrickDataManager()
                || brickDataManager.getRecyclerViewItems().get(parent.getChildAdapterPosition(view)) == null) {
            return;
        }

        BaseBrick brick = brickDataManager.getRecyclerViewItems().get(parent.getChildAdapterPosition(view));
        applyDynamicPadding(view.getContext(), outRect, brick);
    }

    /**
     * Applies dynamic padding to a brick
     *
     * Dynamic padding takes into consideration the number of bricks in a group
     * and the span size to appropriately set the offsets when the section has
     * more than one brick. Using the traditional padding mechanism
     * duplicates the padding between bricks where only half is desired.
     *
     * @param context           A context
     * @param outRect           The Rect provided by {@link #getItemOffsets(Rect, View, RecyclerView, RecyclerView.State)}
     * @param brick             The brick
     */
    private void applyDynamicPadding(Context context, Rect outRect, BaseBrick brick) {
        int innerPaddingLeft = brick.getPadding().getInnerLeftPadding();
        int innerPaddingTop = brick.getPadding().getInnerTopPadding();
        int innerPaddingRight = brick.getPadding().getInnerRightPadding();
        int innerPaddingBottom = brick.getPadding().getInnerBottomPadding();

        int outerPaddingLeft = brick.getPadding().getOuterLeftPadding();
        int outerPaddingTop = brick.getPadding().getOuterTopPadding();
        int outerPaddingRight = brick.getPadding().getOuterRightPadding();
        int outerPaddingBottom = brick.getPadding().getOuterBottomPadding();

        // Apply padding
        if (brick.getSpanSize().getSpans(context) == brickDataManager.getMaxSpanCount()) {
            // Single column
            setOutRect(outRect, brick, innerPaddingTop, innerPaddingBottom, outerPaddingLeft, outerPaddingTop, outerPaddingRight, outerPaddingBottom);

        } else {
            // Multi-column
            if (brick.isOnLeftWall()) {
                setOutRect(outRect, brick, innerPaddingTop, innerPaddingBottom, outerPaddingLeft, outerPaddingTop, innerPaddingRight, outerPaddingBottom);
            } else if (brick.isOnRightWall()) {
                setOutRect(outRect, brick, innerPaddingTop, innerPaddingBottom, innerPaddingLeft, outerPaddingTop, outerPaddingRight, outerPaddingBottom);
            } else {
                setOutRect(outRect, brick, innerPaddingTop, innerPaddingBottom, innerPaddingLeft, outerPaddingTop, innerPaddingRight, outerPaddingBottom);
            }
        }
    }

    private void setOutRect(Rect outRect, BaseBrick brick, int innerPaddingTop, int innerPaddingBottom, int outerPaddingLeft, int outerPaddingTop, int outerPaddingRight, int outerPaddingBottom) {
        if (brick.isInFirstRow()) {
            outRect.set(outerPaddingLeft, outerPaddingTop, outerPaddingRight, brick.isInLastRow() ? outerPaddingBottom : innerPaddingBottom);
        } else {
            outRect.set(outerPaddingLeft, innerPaddingTop, outerPaddingRight, brick.isInLastRow() ? outerPaddingBottom : innerPaddingBottom);
        }
    }
}
