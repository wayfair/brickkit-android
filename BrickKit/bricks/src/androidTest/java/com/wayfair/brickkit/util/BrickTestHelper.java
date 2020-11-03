/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.util;

import android.view.View;

import com.wayfair.brickkit.padding.ZeroBrickPadding;
import com.wayfair.brickkit.size.HalfWidthBrickSize;
import com.wayfair.brickkit.viewholder.BrickViewHolder;
import com.wayfair.brickkit.brick.BaseBrick;

import androidx.recyclerview.widget.RecyclerView;

public class BrickTestHelper {
    public static final class TestBrick extends BaseBrick {
        private final int layoutId;

        private TestBrick(int layoutId) {
            super(new HalfWidthBrickSize(), new ZeroBrickPadding());
            this.layoutId = layoutId;
        }

        @Override
        public void onBindData(BrickViewHolder holder) {

        }

        @Override
        public int getLayout() {
            return layoutId;
        }

        @Override
        public BrickViewHolder createViewHolder(View itemView) {
            return null;
        }
    }

    private static final class TestBrick2 extends BaseBrick {

        private TestBrick2() {
            super(new HalfWidthBrickSize(), new ZeroBrickPadding());
        }

        @Override
        public void onBindData(BrickViewHolder holder) {

        }

        @Override
        public int getLayout() {
            return 0;
        }

        @Override
        public BrickViewHolder createViewHolder(View itemView) {
            return null;
        }
    }

    public static final class TestPlaceholderBrick extends BaseBrick {
        private final int placeholderLayoutId;
        private final boolean isDataReady;

        private TestPlaceholderBrick(int placeholderLayoutId, boolean isDataReady) {
            super(new HalfWidthBrickSize(), new ZeroBrickPadding());
            this.placeholderLayoutId = placeholderLayoutId;
            this.isDataReady = isDataReady;
        }

        @Override
        public void onBindData(BrickViewHolder holder) {

        }

        @Override
        public boolean isDataReady() {
            return isDataReady;
        }

        @Override
        public int getPlaceholderLayout() {
            return placeholderLayoutId;
        }

        @Override
        public int getLayout() {
            return 0;
        }

        @Override
        public BrickViewHolder createViewHolder(View itemView) {
            return null;
        }
    }

    public BaseBrick generateHiddenBrick() {
        BaseBrick brick = generateBrick();
        brick.setHidden(true);

        return brick;
    }

    public BaseBrick generateBrickWithLayoutId(int layoutId) {
        return new TestBrick(layoutId);
    }

    public BaseBrick generateBrickWithPlaceholderLayoutId(int placeholderLayoutId, boolean isDataReady) {
        return new TestPlaceholderBrick(placeholderLayoutId, isDataReady);
    }

    public BaseBrick generateBrick() {
        return new TestBrick(0);
    }

    public BaseBrick generateOtherBrick() {
        return new TestBrick2();
    }

    public static class TestAdapterDataObserver extends RecyclerView.AdapterDataObserver {

        private boolean changed = false;

        private int itemRangeChangedPositionStart = -1;
        private int itemRangeChangedItemCount = -1;
        private Object itemRangeChangedPayload;

        private int itemRangeInsertedPositionStart = -1;
        private int itemRangeInsertedItemCount = -1;

        private int itemRangeRemovedPositionStart = -1;
        private int itemRangeRemovedItemCount = -1;

        private int itemRangeMovedFromPosition = -1;
        private int itemRangeMovedToPosition = -1;
        private int itemRangeMovedItemCount = -1;


        public void onChanged() {
            changed = true;
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            itemRangeChangedPositionStart = positionStart;
            itemRangeChangedItemCount = itemCount;
            itemRangeChangedPayload = payload;
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            itemRangeChangedPositionStart = positionStart;
            itemRangeChangedItemCount = itemCount;
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            itemRangeInsertedPositionStart = positionStart;
            itemRangeInsertedItemCount = itemCount;
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            itemRangeRemovedPositionStart = positionStart;
            itemRangeRemovedItemCount = itemCount;
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            itemRangeMovedFromPosition = fromPosition;
            itemRangeMovedToPosition = toPosition;
            itemRangeMovedItemCount = itemCount;
        }

        public boolean isChanged() {
            return changed;
        }

        public int getItemRangeChangedPositionStart() {
            return itemRangeChangedPositionStart;
        }

        public int getItemRangeChangedItemCount() {
            return itemRangeChangedItemCount;
        }

        public Object getItemRangeChangedPayload() {
            return itemRangeChangedPayload;
        }

        public int getItemRangeInsertedPositionStart() {
            return itemRangeInsertedPositionStart;
        }

        public int getItemRangeInsertedItemCount() {
            return itemRangeInsertedItemCount;
        }

        public int getItemRangeRemovedPositionStart() {
            return itemRangeRemovedPositionStart;
        }

        public int getItemRangeRemovedItemCount() {
            return itemRangeRemovedItemCount;
        }

        public int getItemRangeMovedFromPosition() {
            return itemRangeMovedFromPosition;
        }

        public int getItemRangeMovedToPosition() {
            return itemRangeMovedToPosition;
        }

        public int getItemRangeMovedItemCount() {
            return itemRangeMovedItemCount;
        }

        public void setItemRangeChangedPositionStart(int itemRangeChangedPositionStart) {
            this.itemRangeChangedPositionStart = itemRangeChangedPositionStart;
        }

        public void setItemRangeChangedItemCount(int itemRangeChangedItemCount) {
            this.itemRangeChangedItemCount = itemRangeChangedItemCount;
        }

        public void setItemRangeInsertedPositionStart(int itemRangeInsertedPositionStart) {
            this.itemRangeInsertedPositionStart = itemRangeInsertedPositionStart;
        }

        public void setItemRangeInsertedItemCount(int itemRangeInsertedItemCount) {
            this.itemRangeInsertedItemCount = itemRangeInsertedItemCount;
        }
    }
}
