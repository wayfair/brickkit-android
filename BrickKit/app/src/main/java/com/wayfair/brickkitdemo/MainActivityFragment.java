/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.padding.BrickPaddingFactory;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkit.size.PercentageBrickSize;
import com.wayfair.brickkitdemo.bricks.UnusedBrick;
import com.wayfair.brickkitdemo.bricks.UsedBrick;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BrickFragment {
    private static final BrickSize ONE_FIFTH = new PercentageBrickSize(.2f);
    private static final BrickSize TWO_FIFTH = new PercentageBrickSize(.4f);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BrickPadding padding = new BrickPaddingFactory(getResources()).getInnerOuterBrickPadding(R.dimen.four_dp, R.dimen.no_dp);

        ArrayList<BaseBrick> usedBricks = new ArrayList<>();

        final UsedBrick usedBrick = new UsedBrick(
                TWO_FIFTH,
                padding,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.content, new SimpleBrickFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                }
        );
        usedBricks.add(usedBrick);

        // Fake waiting as in a API request to show off placeholder when data is not ready
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                usedBrick.setText("Simple Brick View");
                dataManager.refreshItem(usedBrick);
            }
        }, 2000);
        usedBricks.add(
                new UsedBrick(
                        TWO_FIFTH,
                        padding,
                        "Add/Remove Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new AddRemoveBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );
        usedBricks.add(
                new UsedBrick(
                        TWO_FIFTH,
                        padding,
                        "Infinite Scroll Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new InfiniteScrollBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );
        usedBricks.add(
                new UsedBrick(
                        TWO_FIFTH,
                        padding,
                        "Staggered Infinite Scroll Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new StaggeredInfiniteScrollBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );
        usedBricks.add(
                new UsedBrick(
                        TWO_FIFTH,
                        padding,
                        "Fragment Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new FragmentBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );

        for (int i = 0; i < usedBricks.size() + 2; i++) {
            BrickSize first;
            BrickSize last;

            if (i % 2 == 0) {
                first = ONE_FIFTH;
                last = TWO_FIFTH;
            } else {
                first = TWO_FIFTH;
                last = ONE_FIFTH;
            }

            UnusedBrick unusedBrick1 = new UnusedBrick(first, padding);
            dataManager.addLast(unusedBrick1);

            if (i == 0 || i == usedBricks.size() + 1) {
                UnusedBrick unusedBrick = new UnusedBrick(
                        TWO_FIFTH,
                        padding
                );
                dataManager.addLast(unusedBrick);
            } else {
                dataManager.addLast(usedBricks.get(i - 1));
            }

            UnusedBrick unusedBrick2 = new UnusedBrick(last, padding);
            dataManager.addLast(unusedBrick2);

        }

    }
}
