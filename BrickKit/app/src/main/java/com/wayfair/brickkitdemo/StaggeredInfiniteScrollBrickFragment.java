package com.wayfair.brickkitdemo;

/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.OnReachedItemAtPosition;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.brick.TextBrick;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.OrientationBrickSize;

/**
 * Example fragment which loads more bricks when scrolling to the bottom.
 * <p>
 * This fragment takes advantage of the {@link OnReachedItemAtPosition} which calls back when
 * items are bound in the adapter.
 */
public class StaggeredInfiniteScrollBrickFragment extends BrickFragment {
    private static final int HALF = 120;

    private int page = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addNewBricks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        dataManager.applyStaggeredGridLayout(2, StaggeredGridLayoutManager.VERTICAL);
        dataManager.getBrickRecyclerAdapter().setOnReachedItemAtPosition(
                new OnReachedItemAtPosition() {
                    @Override
                    public void bindingItemAtPosition(int position) {
                        if (position == dataManager.getRecyclerViewItems().size() - 1) {
                            page++;
                            addNewBricks();
                        }
                    }
                }
        );

        return view;
    }

    /**
     * Method to add 100 new text bricks to the data manager.
     */
    private void addNewBricks() {
        String text = "Brick: " + page + " ";
        String textToAppend;
        for (int i = 0; i < 100; i++) {

            if (i % 19 == 0) {
                textToAppend = "fullsize ";
            } else if (i % 4 == 0) {
                textToAppend = "multi\nline ";
            } else {
                textToAppend = "";
            }
            textToAppend += String.valueOf(i);

            BaseBrick unusedBrick2 = new TextBrick(
                    getContext(),
                    new OrientationBrickSize(maxSpans()) {
                        @Override
                        protected int portrait() {
                            return dataManager.getMaxSpanCount();
                        }

                        @Override
                        protected int landscape() {
                            return HALF;
                        }
                    },
                    new InnerOuterBrickPadding(5, 10),
                    text + textToAppend
            );
            if (i % 19 == 0) {
                unusedBrick2.setFullSize(true);
            }
            dataManager.addLast(unusedBrick2);
        }
    }
}
