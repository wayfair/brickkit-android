/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.padding.SimpleBrickPadding;
import com.wayfair.brickkit.size.SimpleBrickSize;
import com.wayfair.brickkitdemo.bricks.UnusedBrick;
import com.wayfair.brickkitdemo.bricks.UsedBrick;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BrickFragment {
    private static final int ONE_FIFTH = BaseBrick.DEFAULT_MAX_SPAN_COUNT / 5;
    private static final int TWO_FIFTH = ONE_FIFTH * 2;

    SimpleBrickPadding standardPadding = new SimpleBrickPadding(4);
    SimpleBrickSize spanSizeTwoFifths = new SimpleBrickSize(maxSpans()) {
        @Override
        protected int size() {
            return TWO_FIFTH;
        }
    };

    // Padding brick to start the line
    private SimpleBrickSize spanSizeOneFifth = new SimpleBrickSize(maxSpans()) {
        @Override
        protected int size() {
            return ONE_FIFTH;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<BaseBrick> usedBricks = new ArrayList<>();
        usedBricks.add(getNavigationBrick(standardPadding,
                "Simple Brick View",
                new SimpleBrickFragment()));
        usedBricks.add(getNavigationBrick(standardPadding,
                "Header Brick View",
                new HeaderBrickFragment()));
        usedBricks.add(getNavigationBrick(standardPadding,
                "Placeholder Brick View",
                new PlaceholderBrickFragment()));
        usedBricks.add(getNavigationBrick(standardPadding,
                "Footer Brick View",
                new FooterBrickFragment()));
        usedBricks.add(getNavigationBrick(standardPadding,
                "Expandable Brick View",
                new ExpandableHeaderFooterFragment()));
        usedBricks.add(getNavigationBrick(standardPadding,
                "Add/Remove Brick View",
                new AddRemoveBrickFragment()));
        usedBricks.add(getNavigationBrick(standardPadding,
                "Infinite Scroll Brick View",
                new InfiniteScrollBrickFragment()));
        usedBricks.add(getNavigationBrick(standardPadding,
                "Staggered Infinite Scroll Brick View",
                new StaggeredInfiniteScrollBrickFragment()));
        usedBricks.add(getNavigationBrick(standardPadding,
                "Fragment Brick View",
                new FragmentBrickFragment()));
        addBricksToDataManager(usedBricks);
    }

    /**
     * Adds bricks to the datamanager. Includes some generated bricks for a masonry pattern.
     * @param usedBricks The bricks which have real content, and are to be added to the screen.
     */
    private void addBricksToDataManager(ArrayList<BaseBrick> usedBricks) {
        final UnusedBrick oneFifthBrick = new UnusedBrick(spanSizeOneFifth, standardPadding);
        final UnusedBrick twoFifthsBrick = new UnusedBrick(spanSizeTwoFifths, standardPadding);

        for (int i = 0; i < usedBricks.size() + 2; i++) {

            final BaseBrick first;
            final BaseBrick last;

            if (i % 2 == 0) {
                first = oneFifthBrick;
                last = twoFifthsBrick;
            } else {
                first = twoFifthsBrick;
                last = oneFifthBrick;
            }

            dataManager.addLast(first);
            if (i == 0 || i == usedBricks.size() + 1) {
                dataManager.addLast(new UnusedBrick(spanSizeTwoFifths, standardPadding));
            } else {
                dataManager.addLast(usedBricks.get(i - 1));
            }
            dataManager.addLast(last);
        }
    }

    /**
     * Used to return the navigation brick which is used in multiple places on this page.
     *
     * @param padding  The standardPadding which will be applied to the brick
     * @param text     The text which will show up on the generated text brick
     * @param fragment The fragment which the brick will navigate to, after it is clicked.
     * @return Returns a brick with text which will navigate to a given fragment when clicked.
     */
    @NonNull
    private UsedBrick getNavigationBrick(BrickPadding padding,
                                         String text,
                                         final BrickFragment fragment) {
        return new UsedBrick(
                spanSizeTwoFifths,
                padding,
                text,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.content, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        dataManager.setDragAndDrop(true);

        return view;
    }
}
