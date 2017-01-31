/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.wayfair.brickkit.brick.BaseBrick;

/**
 * A helper to enable drag'n'drop and swipe-to-dismiss.
 */
class BrickItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private BrickDataManager dataManager;

    /**
     * The constructor for this helper. Requires a {@link BrickDataManager} for the {@link RecyclerView} and data within.
     *
     * @param dataManager A brick data manager we are listening to for swipe and drag events
     */
    BrickItemTouchHelperCallback(BrickDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (dataManager.getDragAndDrop() || dataManager.getSwipeToDismiss()) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                BaseBrick brick = dataManager.brickAtPosition(viewHolder.getAdapterPosition());

                if (brick.getSpanSize().getSpans(recyclerView.getContext()) == dataManager.getMaxSpanCount()) {
                    final int dragFlags;
                    final int swipeFlags;

                    if (dataManager.getDragAndDrop()) {
                        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    } else {
                        dragFlags = 0;
                    }

                    if (dataManager.getSwipeToDismiss()) {
                        swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    } else {
                        swipeFlags = 0;
                    }

                    return makeMovementFlags(dragFlags, swipeFlags);
                } else if (dataManager.getDragAndDrop()) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }
        }

        return makeMovementFlags(0, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        BaseBrick brick = dataManager.brickAtPosition(viewHolder.getAdapterPosition());
        int targetPosition = target.getAdapterPosition();

        dataManager.moveItem(brick, dataManager.brickAtPosition(targetPosition));
        brick.movedTo(targetPosition);

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        BaseBrick brick = dataManager.brickAtPosition(viewHolder.getAdapterPosition());
        dataManager.removeItem(brick);
        brick.dismissed();
    }
}
