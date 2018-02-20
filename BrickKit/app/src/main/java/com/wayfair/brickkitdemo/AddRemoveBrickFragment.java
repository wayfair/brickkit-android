/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.brick.ViewModelBrick;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.SimpleBrickSize;
import com.wayfair.brickkit.behavior.StickyFooterBehavior;
import com.wayfair.brickkit.behavior.StickyHeaderBehavior;
import com.wayfair.brickkit.brick.TextBrick;

import java.util.Locale;

/**
 * Demo fragment that allows you to add and remove bricks at a given position.
 */
public class AddRemoveBrickFragment extends BrickFragment {
    private static final int NUMBER_OF_BRICKS = 20;
    private static final String FORMAT = "Brick: %d";
    private ControllerDataModel dataModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < NUMBER_OF_BRICKS; i++) {

            if (i == 0) {
                dataModel = new ControllerDataModel(NUMBER_OF_BRICKS);

                ViewModelBrick viewModelBrick = new ViewModelBrick(
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return dataManager.getMaxSpanCount();
                            }
                        },
                        new InnerOuterBrickPadding(5, 10),
                        R.layout.controller_brick_vm,
                        BR.controllerViewModel,
                        new ControllerViewModel(
                                dataModel,
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dataModel.getValue() < 0 || dataModel.getValue() >= dataManager.getRecyclerViewItems().size()) {
                                            return;
                                        }

                                        dataManager.removeItem(dataManager.getRecyclerViewItems().get(dataModel.getValue()));
                                    }
                                },
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dataModel.getValue() < 0 || dataModel.getValue() > dataManager.getRecyclerViewItems().size()) {
                                            return;
                                        }

                                        if (dataModel.getValue() == dataManager.getRecyclerViewItems().size()) {
                                            dataManager.addLast(
                                                    new TextBrick(
                                                            new SimpleBrickSize(maxSpans()) {
                                                                @Override
                                                                protected int size() {
                                                                    return dataManager.getMaxSpanCount();
                                                                }
                                                            },
                                                            new InnerOuterBrickPadding(5, 10),
                                                            String.format(Locale.getDefault(), FORMAT, dataModel.getValue())
                                                    )
                                            );
                                        } else {
                                            dataManager.addBeforeItem(
                                                    dataManager.getRecyclerViewItems().get(dataModel.getValue()),
                                                    new TextBrick(
                                                            new SimpleBrickSize(maxSpans()) {
                                                                @Override
                                                                protected int size() {
                                                                    return dataManager.getMaxSpanCount();
                                                                }
                                                            },
                                                            new InnerOuterBrickPadding(5, 10),
                                                            String.format(Locale.getDefault(), FORMAT, dataModel.getValue())
                                                    )
                                            );
                                        }

                                        dataModel.setValue(dataModel.getValue() + 1);
                                    }
                                }
                        )
                );

                viewModelBrick.setHeader(true);
                viewModelBrick.setFooter(true);

                dataManager.addLast(viewModelBrick);
            } else {
                @SuppressLint("DefaultLocale") BaseBrick brick = new TextBrick(
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

        dataManager.addBehavior(new StickyHeaderBehavior(dataManager));
        dataManager.addBehavior(new StickyFooterBehavior(dataManager));
    }
}
