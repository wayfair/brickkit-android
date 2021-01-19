/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.brick;

import android.view.View;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.padding.ZeroBrickPadding;
import com.wayfair.brickkit.size.FullWidthBrickSize;
import com.wayfair.brickkit.viewholder.BrickViewHolder;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;

import androidx.annotation.LayoutRes;

/**
 * Abstract class which defines Bricks.
 */
public abstract class BaseBrick {
    public static final int DEFAULT_LAYOUT_RES_ID = 0;

    private final BrickPadding padding;
    private final BrickSize spanSize;

    private Object tag;
    private boolean hidden = false;
    private boolean isInFirstRow;
    private boolean isInLastRow;
    private boolean isOnLeftWall;
    private boolean isOnRightWall;
    private BrickDataManager dataManager;

    /**
     * Constructor.
     */
    public BaseBrick() {
        this(new FullWidthBrickSize(), new ZeroBrickPadding());
    }

    /**
     * Constructor.
     *
     * @param spanSize size information for this brick
     * @param padding  padding for this brick
     */
    public BaseBrick(BrickSize spanSize, BrickPadding padding) {
        this.spanSize = spanSize;
        this.padding = padding;
    }

    /**
     * Constructor which uses the default padding.
     *
     * @param spanSize size information for this brick
     */
    public BaseBrick(BrickSize spanSize) {
        this(spanSize, new ZeroBrickPadding());
    }

    /**
     * Constructor which uses the default padding.
     *
     * @param padding  padding for this brick
     */
    public BaseBrick(BrickPadding padding) {
        this(new FullWidthBrickSize(), padding);
    }

    /**
     * Called to display the information in this brick to the specified ViewHolder.
     *
     * @param holder BrickViewHolder which should be updated.
     */
    public abstract void onBindData(BrickViewHolder holder);

    /**
     * Get layout resource id for this brick.
     *
     * @return the layout resource id for this brick
     */
    @LayoutRes
    public abstract int getLayout();

    /**
     * Current brick must implement this method in order for the adapter to inflate
     * either placeholder or brick layout. If method is not overridden we always assume data is ready.
     *
     * @return True if data is ready
     */
    public boolean isDataReady() {
        return true;
    }

    /**
     * Get layout resource id for this brick's placeholder.
     *
     * @return The layout resource id for this brick's placeholder
     */
    @LayoutRes
    public int getPlaceholderLayout() {
        throw new UnsupportedOperationException(getClass().getSimpleName()
                + " getPlaceholderLayout() method must be overridden within brick extending BaseBrick");
    }

    /**
     * Set the brick's tag. This is similar to a {@link View#setTag(Object)}.
     *
     * @param tag Set the tag that can be used to ID the brick later
     */
    public void setTag(Object tag) {
        if (dataManager != null && this.tag != null && !this.tag.equals(tag)) {
            dataManager.removeFromTagCache(this);
        }

        this.tag = tag;

        if (dataManager != null && tag != null) {
            dataManager.addToTagCache(this);
        }
    }

    /**
     * Get's the brick's tag. This is similar to a {@link View#getTag()}.
     *
     * @return The tag for the brick
     */
    public Object getTag() {
        return tag;
    }

    /**
     * Creates an instance of the {@link BrickViewHolder} for this class.
     *
     * @param itemView view to pass into the {@link BrickViewHolder}
     * @return the {@link BrickViewHolder}
     */
    public abstract BrickViewHolder createViewHolder(View itemView);

    /**
     * Gets the {@link BrickSize} for this brick.
     *
     * @return the {@link BrickSize} for this brick
     */
    public BrickSize getSpanSize() {
        return spanSize;
    }

    /**
     * Gets the {@link BrickPadding} for this brick.
     *
     * @return the {@link BrickPadding} for this brick
     */
    public BrickPadding getPadding() {
        return padding;
    }

    /**
     * Whether or not this brick should be hidden.
     *
     * @return true if brick should be hidden, false otherwise
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Set whether the brick should be hidden.
     *
     * @param hidden whether the brick should be hidden
     */
    public void setHidden(boolean hidden) {
        boolean wasHidden = this.hidden;
        this.hidden = hidden;
        if (wasHidden != hidden && dataManager != null) {
            if (!wasHidden) {
                dataManager.hideItem(this);
            } else {
                dataManager.showItem(this);
            }
        }

    }

    /**
     * If this brick as attached to a DataManager, refresh this brick in that DataManager.
     */
    public void refreshItem() {
        if (dataManager != null) {
            dataManager.refreshItem(this);
        }
    }

    /**
     * If this brick as attached to a DataManager, smooth scroll this this brick in that DataManager.
     */
    public void smoothScroll() {
        if (dataManager != null) {
            dataManager.smoothScrollToBrick(this);
        }
    }

    /**
     * Adds this brick to the end of the supplied {@link BrickDataManager}.
     *
     * @param dataManager The {@link BrickDataManager} to add to
     */
    public void addLastTo(BrickDataManager dataManager) {
        dataManager.addLast(this);
    }

    /**
     * Adds this brick to the end of the supplied {@link BrickDataManager}.
     *
     * @param dataManager The {@link BrickDataManager} to add to
     */
    public void addFirstTo(BrickDataManager dataManager) {
        dataManager.addFirst(this);
    }

    /**
     * Set whether the brick should act as it is in the first row.
     *
     * @return true if brick is in the first row, false otherwise
     */
    public boolean isInFirstRow() {
        return isInFirstRow;
    }

    /**
     * Set whether the brick should act as it is in the first row.
     *
     * @param inFirstRow whether the brick should act as it is in the first row
     */
    public void setInFirstRow(boolean inFirstRow) {
        isInFirstRow = inFirstRow;
    }

    /**
     * Set whether the brick should act as it is in the last row.
     *
     * @return true if brick is in the last row, false otherwise
     */
    public boolean isInLastRow() {
        return isInLastRow;
    }

    /**
     * Set whether the brick should act as it is in the last row.
     *
     * @param inLastRow whether the brick should act as it is in the last row
     */
    public void setInLastRow(boolean inLastRow) {
        isInLastRow = inLastRow;
    }

    /**
     * Set whether the brick should act as it is on the left wall.
     *
     * @return true if brick is is on the left wall, false otherwise
     */
    public boolean isOnLeftWall() {
        return isOnLeftWall;
    }

    /**
     * Set whether the brick should act as it is on the left wall.
     *
     * @param onLeftWall whether the brick should act as it is on the left wall
     */
    public void setOnLeftWall(boolean onLeftWall) {
        isOnLeftWall = onLeftWall;
    }

    /**
     * Set whether the brick should act as it is on the right wall.
     *
     * @return true if brick is is on the right wall, false otherwise
     */
    public boolean isOnRightWall() {
        return isOnRightWall;
    }

    /**
     * Set whether the brick should act as it is on the right wall.
     *
     * @param onRightWall whether the brick should act as it is on the right wall
     */
    public void setOnRightWall(boolean onRightWall) {
        isOnRightWall = onRightWall;
    }

    /**
     * Set the current DataManager this brick is connected to.
     * @param dataManager The current DataManager for this brick
     */
    public void setDataManager(BrickDataManager dataManager) {
        this.dataManager = dataManager;
    }
}
