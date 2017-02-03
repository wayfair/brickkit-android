/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.OrientationBrickSize;
import com.wayfair.brickkit.brick.TextBrick;

/**
 * Example fragment which shows text bricks.
 *
 * In portrait, the bricks are full width.
 * In landscape the bricks are half width.
 */
public class SimpleBrickFragment extends BrickFragment {
    private static final int HALF = 120;

    private int numberOfBricks = 100;

    private TextBrick[] bricks;

    /**
     * Create a new instance of a SimpleBrickFragment.
     *
     * @param numberOfBricks The number of bricks you want in the sub-{@link com.wayfair.brickkit.BrickDataManager}
     * @return The SimpleBrickFragment your created
     */
    public static SimpleBrickFragment newInstance(int numberOfBricks) {
        SimpleBrickFragment fragment = new SimpleBrickFragment();
        fragment.numberOfBricks = numberOfBricks;
        return fragment;
    }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        bricks = new TextBrick[numberOfBricks];

        for (int i = 0; i < numberOfBricks; i++) {
            final int j = i;
            bricks[i] = new TextBrick(
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
                    "Brick: " + i
            );
            bricks[i].setOnDismiss(
                    new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(
                                    dataManager.getBrickRecyclerAdapter().getRecyclerView(),
                                    "Deleted " + bricks[j].getText(),
                                    Snackbar.LENGTH_SHORT
                            ).show();
                        }
                    }
            );
            dataManager.addLast(bricks[i]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        dataManager.setSwipeToDismiss(true);

        return view;
    }
}
