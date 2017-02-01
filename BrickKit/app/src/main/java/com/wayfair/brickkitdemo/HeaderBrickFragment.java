/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.os.Bundle;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.OrientationBrickSize;
import com.wayfair.brickkit.behavior.StickyHeaderBehavior;
import com.wayfair.brickkit.brick.TextBrick;
import com.wayfair.brickkit.StickyScrollMode;

/**
 * Fragment which shows the brick header behavior.
 *
 * Every tenth brick is a "header" which means it will remain on screen until
 * another "header" brick is scrolled into the header area.
 */
public class HeaderBrickFragment extends BrickFragment {
    private static final int HALF = 120;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 100; i++) {
            TextBrick unusedBrick2 = new TextBrick(
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

            if (i % 10 == 0) {
                unusedBrick2.setHeader(true);
                unusedBrick2.setStickyScrollMode(StickyScrollMode.SHOW_ON_SCROLL_DOWN);
            }

            dataManager.addLast(unusedBrick2);
        }
    }

    @Override
    public void addBehaviors() {
        dataManager.addBehavior(new StickyHeaderBehavior(dataManager));
    }
}
