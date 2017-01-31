/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.size.SimpleBrickSize;
import com.wayfair.brickkitdemo.bricks.FragmentBrick;

/**
 * Example of fragment containing {@link FragmentBrick}'s containing {@link SimpleBrickFragment}'s.
 */
public class FragmentBrickFragment extends BrickFragment {
    private int[] colors = new int[3];

    @Override
    public void createBricks() {
        colors[0] = getResources().getColor(R.color.colorAccent);
        colors[1] = getResources().getColor(R.color.colorPrimary);
        colors[2] = getResources().getColor(R.color.colorPrimaryDark);

        for (int i = 0; i < colors.length; i++) {
            FragmentBrick brick = new FragmentBrick(
                    getContext(),
                    new SimpleBrickSize(maxSpans()) {
                        @Override
                        protected int size() {
                            return dataManager.getMaxSpanCount() / 2;
                        }
                    },
                    getChildFragmentManager(),
                    SimpleBrickFragment.newInstance(i + 1),
                    "simple" + i
            );
            brick.setBackgroundColor(colors[i]);
            dataManager.addLast(brick);
        }
    }
}
