/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.OnReachedItemAtPosition;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPaddingFactory;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkit.size.FullWidthBrickSize;
import com.wayfair.brickkit.size.HalfWidthBrickSize;
import com.wayfair.brickkitdemo.bricks.TextBrick;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Example fragment which loads more bricks when scrolling to the bottom.
 * <p>
 * This fragment takes advantage of the {@link OnReachedItemAtPosition} which calls back when
 * items are bound in the adapter.
 */
public class StaggeredInfiniteScrollBrickFragment extends BrickFragment {
    private int page = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addNewBricks(new BrickPaddingFactory(getResources()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setItemPrefetchEnabled(false);
        dataManager.getRecyclerView().setLayoutManager(staggeredGridLayoutManager);

        dataManager.getBrickRecyclerAdapter().setOnReachedItemAtPosition(
                position -> {
                    if (position == dataManager.getRecyclerViewItems().size() - 1) {
                        page++;
                        addNewBricks(new BrickPaddingFactory(getResources()));
                    }
                }
        );

        return view;
    }

    /**
     * Method to add 100 new text bricks to the data manager.
     *
     * @param brickPaddingFactory {@link BrickPaddingFactory} to generate padding with
     */
    private void addNewBricks(BrickPaddingFactory brickPaddingFactory) {
        String text = "Brick: " + page + " ";
        String textToAppend;

        for (int i = 0; i < 100; i++) {
            final BrickSize brickSize;
            if (i % 19 == 0) {
                textToAppend = "fullsize ";
                brickSize = new FullWidthBrickSize();
            } else if (i % 4 == 0) {
                brickSize = new HalfWidthBrickSize();
                textToAppend = "multi\nline ";
            } else {
                brickSize = new HalfWidthBrickSize();
                textToAppend = "";
            }
            textToAppend += String.valueOf(i);

            BaseBrick unusedBrick2 = new TextBrick(
                    brickSize,
                    brickPaddingFactory.getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.eight_dp),
                    text + textToAppend
            );

            dataManager.addLast(unusedBrick2);
        }
    }
}
