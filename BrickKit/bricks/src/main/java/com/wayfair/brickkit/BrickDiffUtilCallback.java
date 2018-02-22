package com.wayfair.brickkit;

import android.support.v7.util.DiffUtil;

import com.wayfair.brickkit.brick.BaseBrick;

import java.util.LinkedList;

/**
 * A DuffUtil for Bricks.
 */
public class BrickDiffUtilCallback extends DiffUtil.Callback {

    private LinkedList<BaseBrick> oldList;
    private LinkedList<BaseBrick> newList;

    /**
     * Constructor.
     *
     * @param oldList the old list (usually the one in the {@link BrickDataManager}
     * @param newList the new list
     */
    BrickDiffUtilCallback(LinkedList<BaseBrick> oldList, LinkedList<BaseBrick> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getLayout() == newList.get(newItemPosition).getLayout();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
