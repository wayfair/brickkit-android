/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.SimpleBrickSize;
import com.wayfair.brickkitdemo.bricks.UnusedBrick;
import com.wayfair.brickkitdemo.bricks.UsedBrick;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BrickFragment {
    private static final int ONE_FIFTH = 48;
    private static final int TWO_FIFTH = 96;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InnerOuterBrickPadding padding = new InnerOuterBrickPadding(8, 0);

        ArrayList<BaseBrick> usedBricks = new ArrayList<>();

        usedBricks.add(
                new UsedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        padding,
                        "Simple Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new SimpleBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );
        usedBricks.add(
                new UsedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        padding,
                        "Header Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new HeaderBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );
        usedBricks.add(
                new UsedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        padding,
                        "Footer Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new FooterBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );
        usedBricks.add(
                new UsedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
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
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
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
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
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
            SimpleBrickSize first;
            SimpleBrickSize last;

            if (i % 2 == 0) {
                first =
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return ONE_FIFTH;
                            }
                        };
                last =
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        };
            } else {
                first =
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        };
                last =
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return ONE_FIFTH;
                            }
                        };
            }

            UnusedBrick unusedBrick1 = new UnusedBrick(getContext(), first, padding);
            dataManager.addLast(unusedBrick1);

            if (i == 0 || i == usedBricks.size() + 1) {
                UnusedBrick usedBrick = new UnusedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        padding
                );
                dataManager.addLast(usedBrick);
            } else {
                dataManager.addLast(usedBricks.get(i - 1));
            }

            UnusedBrick unusedBrick2 = new UnusedBrick(getContext(), last, padding);
            dataManager.addLast(unusedBrick2);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        dataManager.setDragAndDrop(true);

        return view;
    }
}
