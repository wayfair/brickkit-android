/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.SimpleBrickSize;
import com.wayfair.brickkit.behavior.StickyFooterBehavior;
import com.wayfair.brickkit.behavior.StickyHeaderBehavior;
import com.wayfair.brickkitdemo.bricks.ControllerBrick;
import com.wayfair.brickkit.brick.TextBrick;

/**
 * Demo fragment that allows you to add and remove bricks at a given position.
 */
public class AddRemoveBrickFragment extends BrickFragment {
    private static final int NUMBER_OF_BRICKS = 20;
    private static final String FORMAT = "Brick: %d";
    private ControllerBrick controllerBrick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < NUMBER_OF_BRICKS; i++) {

            if (i == 0) {
                controllerBrick = new ControllerBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return dataManager.getMaxSpanCount();
                            }
                        },
                        new InnerOuterBrickPadding(5, 10),
                        String.valueOf(NUMBER_OF_BRICKS - 1),
                        "Index",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int index = Integer.parseInt(controllerBrick.value);

                                if (index < 0 || index >= dataManager.getRecyclerViewItems().size()) {
                                    return;
                                }

                                dataManager.removeItem(dataManager.getRecyclerViewItems().get(index));
                            }
                        },
                        new View.OnClickListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onClick(View v) {
                                int index = Integer.parseInt(controllerBrick.value);

                                if (index < 0 || index > dataManager.getRecyclerViewItems().size()) {
                                    return;
                                }

                                if (index == dataManager.getRecyclerViewItems().size()) {
                                    dataManager.addLast(
                                            new TextBrick(
                                                    getContext(),
                                                    new SimpleBrickSize(maxSpans()) {
                                                        @Override
                                                        protected int size() {
                                                            return dataManager.getMaxSpanCount();
                                                        }
                                                    },
                                                    new InnerOuterBrickPadding(5, 10),
                                                    String.format(FORMAT, index)
                                            )
                                    );
                                } else {
                                    dataManager.addBeforeItem(
                                            dataManager.getRecyclerViewItems().get(index),
                                            new TextBrick(
                                                    getContext(),
                                                    new SimpleBrickSize(maxSpans()) {
                                                        @Override
                                                        protected int size() {
                                                            return dataManager.getMaxSpanCount();
                                                        }
                                                    },
                                                    new InnerOuterBrickPadding(5, 10),
                                                    String.format(FORMAT, index)
                                            )
                                    );
                                }

                                controllerBrick.value = String.valueOf(index + 1);
                                dataManager.refreshItem(controllerBrick);
                            }
                        }
                );

                controllerBrick.setHeader(true);
                controllerBrick.setFooter(true);

                dataManager.addLast(controllerBrick);
            } else {
                @SuppressLint("DefaultLocale") BaseBrick brick = new TextBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return dataManager.getMaxSpanCount();
                            }
                        },
                        new InnerOuterBrickPadding(5, 10),
                        String.format(FORMAT, i)
                );

                dataManager.addLast(brick);
            }
        }
    }

    @Override
    public void addBehaviors() {
        dataManager.addBehavior(new StickyHeaderBehavior(dataManager));
        dataManager.addBehavior(new StickyFooterBehavior(dataManager));
    }
}
