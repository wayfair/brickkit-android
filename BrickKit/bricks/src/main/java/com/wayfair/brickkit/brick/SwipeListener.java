package com.wayfair.brickkit.brick;

/**
 * An interface for handling swipes on a {@link BaseBrick}.
 */
public interface SwipeListener {

    /**
     * Called when an item is swiped-to-dismiss.
     *
     * @param direction one of {@link androidx.recyclerview.widget.ItemTouchHelper.UP},
     *                  {@link androidx.recyclerview.widget.ItemTouchHelper.RIGHT},
     *                  {@link androidx.recyclerview.widget.ItemTouchHelper.DOWN},
     *                  {@link androidx.recyclerview.widget.ItemTouchHelper.LEFT},
     *                  {@link androidx.recyclerview.widget.ItemTouchHelper.START},
     *                  {@link androidx.recyclerview.widget.ItemTouchHelper.END}
     *
     */
    void swiped(int direction);
}
