/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.behavior;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.BrickRecyclerAdapter;
import com.wayfair.brickkit.StickyScrollMode;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.R;

/**
 * {@link BrickBehavior} that will provide a sticky footer view. Sticky footer views will remain
 * on screen in the footer position until another sticky footer view is scrolled into that area.
 */
public class StickyFooterBehavior extends StickyViewBehavior {
    /**
     * Constructor.
     *
     * @param brickDataManager {@link BrickDataManager} whose adapter is used for finding bricks
     */
    public StickyFooterBehavior(BrickDataManager brickDataManager) {
        super(brickDataManager, R.id.footer_sticky_container, R.id.footer_sticky_layout, R.id.footer_bar_shadow);
    }

    /**
     * Constructor for Unit Tests.
     *
     * @param brickDataManager   {@link BrickDataManager} whose adapter is used for finding bricks
     * @param stickyHolderContainer sticky layout needed for the behavior
     */
    public StickyFooterBehavior(BrickDataManager brickDataManager, ViewGroup stickyHolderContainer) {
        super(brickDataManager, stickyHolderContainer, R.id.footer_sticky_layout, R.id.footer_bar_shadow);
    }

    @Override
    protected void stickyViewFadeTranslate(int dy) {
        if (stickyHolderContainer != null && stickyHolderContainer.getHeight() > 0 && stickyScrollMode == StickyScrollMode.SHOW_ON_SCROLL_UP) {
            float headerY = stickyHolderContainer.getY();
            if (dy > 0) {
                stickyHolderContainer.setTranslationY(Math.max(headerY - dy - stickyHolderContainer.getTop(), 0));
            } else {
                stickyHolderContainer.setTranslationY(Math.min(headerY - dy - stickyHolderContainer.getTop(), stickyHolderContainer.getHeight()));
            }
        }

        if (stickyHolderContainer != null && stickyHolderContainer.getHeight() > 0 && stickyScrollMode == StickyScrollMode.SHOW_ON_SCROLL_DOWN) {
            float headerY = stickyHolderContainer.getY();
            if (dy > 0) {
                stickyHolderContainer.setTranslationY(Math.min(headerY + dy - stickyHolderContainer.getTop(), stickyHolderContainer.getHeight()));
            } else {
                stickyHolderContainer.setTranslationY(Math.max(headerY + dy - stickyHolderContainer.getTop(), 0));
            }
        }
    }

    @Override
    protected int getStickyViewPosition(int adapterPosHere) {
        BrickRecyclerAdapter adapter = brickDataManager.getBrickRecyclerAdapter();
        if (adapterPosHere == RecyclerView.NO_POSITION) {
            View lastChild = adapter.getRecyclerView().getChildAt(adapter.getRecyclerView().getChildCount() - 1);
            adapterPosHere = adapter.getRecyclerView().getChildAdapterPosition(lastChild);
        }
        BaseBrick footer = adapter.getSectionFooter(adapterPosHere);
        //Footer cannot be sticky if it's also an Expandable in collapsed status, RV will raise an exception
        if (footer == null) {
            stickyScrollMode = StickyScrollMode.SHOW_ON_SCROLL;
            return RecyclerView.NO_POSITION;
        } else {
            stickyScrollMode = footer.getStickyScrollMode();
        }
        return adapter.indexOf(footer);
    }

    @Override
    protected void translateStickyView() {
        BrickRecyclerAdapter adapter = brickDataManager.getBrickRecyclerAdapter();

        if (stickyViewHolder == null || adapter == null || adapter.getRecyclerView() == null) {
            return;
        }

        int footerOffsetX = 0, footerOffsetY = 0;

        //Search for the position where the next footer item is found and take the new offset
        for (int i = adapter.getRecyclerView().getChildCount() - 2; i < adapter.getItemCount(); i++) {
            final View nextChild = adapter.getRecyclerView().getChildAt(i);
            if (nextChild != null) {
                int adapterPos = adapter.getRecyclerView().getChildAdapterPosition(nextChild);
                int nextFooterPosition = getStickyViewPosition(adapterPos);
                if (stickyPosition != nextFooterPosition) {
                    if (getOrientation(adapter.getRecyclerView()) == LinearLayoutManager.HORIZONTAL) {
                        if (nextChild.getRight() < adapter.getRecyclerView().getRight()) {
                            int footerWidth = stickyViewHolder.itemView.getMeasuredWidth();
                            footerOffsetX = Math.max(footerWidth - (adapter.getRecyclerView().getRight() - nextChild.getRight()), 0);
                            if (footerOffsetX > 0) {
                                break;
                            }
                        }
                    } else {
                        if (nextChild.getBottom() < adapter.getRecyclerView().getBottom()) {
                            int footerHeight = stickyViewHolder.itemView.getMeasuredHeight();
                            footerOffsetY = Math.max(footerHeight - (adapter.getRecyclerView().getBottom() - nextChild.getBottom()), 0);
                            if (footerOffsetY > 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        //Apply translation
        stickyViewHolder.itemView.setTranslationX(footerOffsetX);
        stickyViewHolder.itemView.setTranslationY(footerOffsetY);
        if (stickyHolderShadowImage != null) {
            stickyHolderShadowImage.setTranslationX(footerOffsetX);
            stickyHolderShadowImage.setTranslationY(footerOffsetY);
        }
    }
}
