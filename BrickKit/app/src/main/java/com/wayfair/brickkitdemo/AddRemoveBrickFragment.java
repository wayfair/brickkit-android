/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.os.Bundle;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPaddingFactory;
import com.wayfair.brickkit.size.FullWidthBrickSize;
import com.wayfair.brickkitdemo.bricks.TextBrick;
import com.wayfair.brickkit.brick.ViewModelBrick;

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
        BrickPaddingFactory brickPaddingFactory = new BrickPaddingFactory(getResources());

        for (int i = 0; i < NUMBER_OF_BRICKS; i++) {

            if (i == 0) {
                dataModel = new ControllerDataModel(NUMBER_OF_BRICKS);

                ViewModelBrick viewModelBrick = new ViewModelBrick.Builder(R.layout.controller_brick_vm)
                        .setSpanSize(new FullWidthBrickSize())
                        .setPadding(brickPaddingFactory.getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.eight_dp))
                        .addViewModel(
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
                                                                    new FullWidthBrickSize(),
                                                                    brickPaddingFactory.getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.eight_dp),
                                                                    String.format(Locale.getDefault(), FORMAT, dataModel.getValue())
                                                            )
                                                    );
                                                } else {
                                                    dataManager.addBeforeItem(
                                                            dataManager.getRecyclerViewItems().get(dataModel.getValue()),
                                                            new TextBrick(
                                                                    new FullWidthBrickSize(),
                                                                    brickPaddingFactory.getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.eight_dp),
                                                                    String.format(Locale.getDefault(), FORMAT, dataModel.getValue())
                                                            )
                                                    );
                                                }

                                                dataModel.setValue(dataModel.getValue() + 1);
                                            }
                                        }
                                )
                        )
                        .build();

                dataManager.addLast(viewModelBrick);
            } else {
                BaseBrick brick = new TextBrick(
                        new FullWidthBrickSize(),
                        brickPaddingFactory.getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.eight_dp),
                        String.format(Locale.getDefault(), FORMAT, i)
                );

                dataManager.addLast(brick);
            }
        }
    }
}
