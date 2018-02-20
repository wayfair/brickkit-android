package com.wayfair.brickkit.brick;

/**
 * An interface for handling swipes on a {@link BaseBrick}.
 */
public interface SwipeListener {

    /**
     * Called when an item is swiped-to-dismiss.
     *
     * @param direction one of {@link android.support.v7.widget.helper.ItemTouchHelper.UP},
     *                  {@link android.support.v7.widget.helper.ItemTouchHelper.RIGHT},
     *                  {@link android.support.v7.widget.helper.ItemTouchHelper.DOWN},
     *                  {@link android.support.v7.widget.helper.ItemTouchHelper.LEFT},
     *                  {@link android.support.v7.widget.helper.ItemTouchHelper.START},
     *                  {@link android.support.v7.widget.helper.ItemTouchHelper.END}
     *
     */
    void swiped(int direction);
}
