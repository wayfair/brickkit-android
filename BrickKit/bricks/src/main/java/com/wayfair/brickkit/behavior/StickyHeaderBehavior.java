/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.behavior;

import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.BrickRecyclerAdapter;
import com.wayfair.brickkit.R;
import com.wayfair.brickkit.StickyScrollMode;
import com.wayfair.brickkit.brick.BaseBrick;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link BrickBehavior} that will provide a sticky header view. Sticky header views will remain
 * on screen in the header position until another sticky header view is scrolled into that area.
 */
public class StickyHeaderBehavior extends StickyViewBehavior {
    /**
     * Constructor.
     *
     * @param brickDataManager {@link BrickDataManager} whose adapter is used for finding bricks
     */
    public StickyHeaderBehavior(BrickDataManager brickDataManager) {
        super(brickDataManager, R.id.header_sticky_container, R.id.header_sticky_layout, R.id.header_bar_shadow);
    }

    /**
     * Constructor for Unit Tests.
     *
     * @param brickDataManager   {@link BrickDataManager} whose adapter is used for finding bricks
     * @param stickyHolderContainer sticky layout needed for the behavior
     */
    public StickyHeaderBehavior(BrickDataManager brickDataManager, ViewGroup stickyHolderContainer) {
        super(brickDataManager, stickyHolderContainer, R.id.header_sticky_layout, R.id.header_bar_shadow);
    }

    @Override
    protected int getStickyViewPosition(int adapterPosHere) {
        BrickRecyclerAdapter adapter = brickDataManager.getBrickRecyclerAdapter();

        if (adapterPosHere == RecyclerView.NO_POSITION) {
            View firstChild = adapter.getRecyclerView().getChildAt(0);
            adapterPosHere = adapter.getRecyclerView().getChildAdapterPosition(firstChild);
        }
        
        // Following condition happens only when removing items and having sticky header behavior.
        // The child at position 0, which is the first VISIBLE view is one of the views that just got removed.
        // For some reason that view is still alive within the recyclerView(probably computing layout).
        // When getSectionHeader is trying to access that position within the recyclerViewItems, that position
        // won't exist since data manager recyclerViewItems linked list just removed it. That will follow up
        // with an indexOfBoundsException and crash the app.
        if (adapterPosHere >= adapter.getItemCount()) {
            return RecyclerView.NO_POSITION;
        }
        
        BaseBrick header = adapter.getSectionHeader(adapterPosHere);
        //Header cannot be sticky if it's also an Expandable in collapsed status, RV will raise an exception
        if (header == null) {
            stickyScrollMode = StickyScrollMode.SHOW_ON_SCROLL;
            return RecyclerView.NO_POSITION;
        } else {
            stickyScrollMode = header.getStickyScrollMode();
        }
        return adapter.indexOf(header);
    }

    @Override
    protected void translateStickyView() {
        BrickRecyclerAdapter adapter = brickDataManager.getBrickRecyclerAdapter();

        if (stickyViewHolder == null || adapter == null || adapter.getRecyclerView() == null) {
            return;
        }

        int headerOffsetX = 0, headerOffsetY = 0;

        //Search for the position where the next header item is found and take the new offset
        for (int i = 1; i > 0; i--) {
            final View nextChild = adapter.getRecyclerView().getChildAt(i);
            if (nextChild != null) {
                int adapterPos = adapter.getRecyclerView().getChildAdapterPosition(nextChild);
                int nextHeaderPosition = getStickyViewPosition(adapterPos);
                if (stickyPosition != nextHeaderPosition) {
                    if (getOrientation(adapter.getRecyclerView()) == LinearLayoutManager.HORIZONTAL) {
                        if (nextChild.getLeft() > 0) {
                            int headerWidth = stickyViewHolder.itemView.getMeasuredWidth();
                            headerOffsetX = Math.min(nextChild.getLeft() - headerWidth, 0);
                            if (headerOffsetX < 0) {
                                break;
                            }
                        }
                    } else {
                        if (nextChild.getTop() > 0) {
                            int headerHeight = stickyViewHolder.itemView.getMeasuredHeight();
                            headerOffsetY = Math.min(nextChild.getTop() - headerHeight, 0);
                            if (headerOffsetY < 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        //Apply translation
        stickyViewHolder.itemView.setTranslationX(headerOffsetX);
        stickyViewHolder.itemView.setTranslationY(headerOffsetY);
        if (stickyHolderShadowImage != null) {
            stickyHolderShadowImage.setTranslationX(headerOffsetX);
            stickyHolderShadowImage.setTranslationY(headerOffsetY);
        }
    }

    @Override
    protected void stickyViewFadeTranslate(int dy) {
        if (stickyHolderContainer != null && stickyHolderContainer.getHeight() > 0 && stickyScrollMode == StickyScrollMode.SHOW_ON_SCROLL_UP) {
            float headerY = stickyHolderContainer.getY();
            if (dy > 0) {
                stickyHolderContainer.setTranslationY(Math.min(headerY + dy, 0));
            } else {
                stickyHolderContainer.setTranslationY(Math.max(headerY + dy, -stickyHolderContainer.getHeight()));
            }
        }

        if (stickyHolderContainer != null && stickyHolderContainer.getHeight() > 0 && stickyScrollMode == StickyScrollMode.SHOW_ON_SCROLL_DOWN) {
            float headerY = stickyHolderContainer.getY();
            if (dy > 0) {
                stickyHolderContainer.setTranslationY(Math.max(headerY - dy, -stickyHolderContainer.getHeight()));
            } else {
                stickyHolderContainer.setTranslationY(Math.min(headerY - dy, 0));
            }
        }
    }
}
