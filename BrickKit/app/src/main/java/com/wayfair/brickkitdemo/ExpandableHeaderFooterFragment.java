/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo;

import android.os.Bundle;
import android.view.View;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.behavior.StickyFooterBehavior;
import com.wayfair.brickkit.behavior.StickyHeaderBehavior;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.brick.TextBrick;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.SimpleBrickSize;
import com.wayfair.brickkitdemo.bricks.TouchableTextBrick;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Fragment which shows the brick footer behavior when the items in the RecyclerView don't fill the screen.
 *
 * The first element in the unexpanded list is a header and the last element in the unexpanded list is a footer.
 */
public class ExpandableHeaderFooterFragment extends BrickFragment {
    boolean isAdd = true;
    Collection<BaseBrick> bricks = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 5; i++) {
            final TouchableTextBrick unusedBrick2 = new TouchableTextBrick(
                    new SimpleBrickSize(maxSpans()) {
                        @Override
                        protected int size() {
                            return dataManager.getMaxSpanCount();
                        }
                    },
                    new InnerOuterBrickPadding(5, 10),
                    "Brick: " + i,
                    getExpandableClickListener(i)
            );

            dataManager.addLast(unusedBrick2);
        }

        dataManager.getRecyclerViewItems().get(0).setHeader(true);
        dataManager.getRecyclerViewItems().get(dataManager.getRecyclerViewItems().size() - 1).setFooter(true);

        dataManager.addBehavior(new StickyFooterBehavior(dataManager));
        dataManager.addBehavior(new StickyHeaderBehavior(dataManager));
    }

    /**
     * Add or remove the child bricks in the expandable list.
     *
     * @param i The position of the clicked brick in the list
     * @return The click listener that adds or removes the child bricks
     */
    private View.OnClickListener getExpandableClickListener(final int i) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdd) {
                    TextBrick newBrick;
                    for (int j = 0; j < 8; j++) {
                        newBrick = new TextBrick(
                                new SimpleBrickSize(maxSpans()) {
                                    @Override
                                    protected int size() {
                                        return dataManager.getMaxSpanCount();
                                    }
                                },
                                new InnerOuterBrickPadding(15, 10),
                                "SubBrick: " + j
                        );
                        bricks.add(newBrick);
                        dataManager.addAfterItem(dataManager.brickAtPosition(i), newBrick);
                    }
                    isAdd = false;
                } else {
                    dataManager.removeItems(bricks);
                    bricks = new ArrayList<>();
                    isAdd = true;
                }
            }
        };
    }
}
