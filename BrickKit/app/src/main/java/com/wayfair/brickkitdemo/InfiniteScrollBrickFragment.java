/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPaddingFactory;
import com.wayfair.brickkit.OnReachedItemAtPosition;
import com.wayfair.brickkit.size.FullPhoneFullHalfTabletBrickSize;
import com.wayfair.brickkitdemo.bricks.TextBrick;

import java.util.ArrayList;

/**
 * Example fragment which loads more bricks when scrolling to the bottom.
 *
 * This fragment takes advantage of the {@link OnReachedItemAtPosition} which calls back when
 * items are bound in the adapter.
 */
public class InfiniteScrollBrickFragment extends BrickFragment {
    private int page = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addNewBricks(new BrickPaddingFactory(getResources()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        dataManager.getBrickRecyclerAdapter().setOnReachedItemAtPosition(
                new OnReachedItemAtPosition() {
                    @Override
                    public void bindingItemAtPosition(int position) {
                        if (position == dataManager.getRecyclerViewItems().size() - 1) {
                            page++;
                            addNewBricks(new BrickPaddingFactory(getResources()));
                        }
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
        ArrayList<BaseBrick> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add(new TextBrick(
                    new FullPhoneFullHalfTabletBrickSize(),
                    brickPaddingFactory.getViewInsetPadding(),
                    "Brick: " + page + " " + i
            ));
        }
        dataManager.addLast(items);
    }
}
