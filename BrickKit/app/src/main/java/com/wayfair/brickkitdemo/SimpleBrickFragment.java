package com.wayfair.brickkitdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.brick.SwipeListener;
import com.wayfair.brickkit.brick.ViewModelBrick;
import com.wayfair.brickkit.models.TextDataModel;
import com.wayfair.brickkit.models.TextViewModel;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.OrientationBrickSize;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Example fragment which shows text bricks.
 *
 * In portrait, the bricks are full width.
 * In landscape the bricks are half width.
 *
 * Copyright © 2017 Wayfair. All rights reserved.
 */
public class SimpleBrickFragment extends BrickFragment {
    private static final int HALF = 120;

    private int numberOfBricks = 100;

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

            ViewModelBrick[] bricks = new ViewModelBrick[numberOfBricks];

        for (int i = 0; i < numberOfBricks; i++) {
            final TextDataModel dataModel = new TextDataModel("Brick: " + i);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    dataModel.appendText(" Hello");
                }

            }, 0, 1000);

            bricks[i] = new ViewModelBrick(
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
                    R.layout.text_brick_vm,
                    BR.textViewModel,
                    new TextViewModel(dataModel)
            );
            bricks[i].setOnDismiss(
                    new SwipeListener() {
                        @Override
                        public void swiped(int direction) {
                            if (direction == ItemTouchHelper.RIGHT
                                    || direction == ItemTouchHelper.END) {
                                Snackbar.make(
                                        dataManager.getBrickRecyclerAdapter().getRecyclerView(),
                                        "Moved " + dataModel.getText(),
                                        Snackbar.LENGTH_SHORT
                                ).show();
                            } else if (direction == ItemTouchHelper.LEFT
                                    || direction == ItemTouchHelper.START) {
                                Snackbar.make(
                                        dataManager.getBrickRecyclerAdapter().getRecyclerView(),
                                        "Deleted " + dataModel.getText(),
                                        Snackbar.LENGTH_SHORT
                                ).show();
                            }
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
