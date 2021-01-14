/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.os.Bundle;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.brick.ViewModelBrick;
import com.wayfair.brickkit.padding.BrickPaddingFactory;
import com.wayfair.brickkit.size.FullPhoneFullHalfTabletBrickSize;
import com.wayfair.brickkitdemo.datamodel.TextDataModel;
import com.wayfair.brickkitdemo.viewmodel.TextViewModel;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Example fragment which shows text bricks.
 * <p>
 * In portrait, the bricks are full width.
 * In landscape the bricks are half width.
 */
public class SimpleBrickFragment extends BrickFragment {
    private int numberOfBricks = 100;

    LinkedList<BaseBrick> bricks;

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

        BrickPaddingFactory brickPaddingFactory = new BrickPaddingFactory(getResources());
        bricks = new LinkedList<>();

        for (int i = 0; i < numberOfBricks; i++) {
            ViewModelBrick brick = genBrick(i, brickPaddingFactory);

            bricks.add(brick);
            dataManager.addLast(brick);
        }
    }

    /**
     * Generate a brick.
     *
     * @param i the index of that brick, also uses in the text of the brick
     * @param brickPaddingFactory {@link BrickPaddingFactory} to generate padding with
     * @return the brick
     */
    public ViewModelBrick genBrick(int i, BrickPaddingFactory brickPaddingFactory) {
        final TextDataModel dataModel = new TextDataModel("Brick: " + i);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                dataModel.appendText(" Hello");
            }

        }, 0, 1000);

        return new ViewModelBrick.Builder(R.layout.text_brick_vm)
                .setPadding(brickPaddingFactory.getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.eight_dp))
                .setSpanSize(new FullPhoneFullHalfTabletBrickSize())
                .addViewModel(
                        BR.textViewModel,
                        new TextViewModel(dataModel)
                )
                .build();
    }
}
