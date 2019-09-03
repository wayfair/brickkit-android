/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.util;

import android.content.Context;
import android.view.View;

import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.padding.SimpleBrickPadding;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkit.size.SimpleBrickSize;

import androidx.recyclerview.widget.RecyclerView;

public class BrickTestHelper {

    private static final int HALF_SPAN = 4;
    private static final int PADDING = 2;
    private static final char MAX = '\u007f';
    private static final int MAX_SPANS = 8;

    private Context context;

    public BrickTestHelper(Context context) {
        this.context = context;
    }

    public static final class TestBrick extends BaseBrick {
        private final int layoutId;

        private TestBrick(Context context, BrickSize spanSize, BrickPadding padding, int layoutId) {
            super(spanSize, padding);
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

        private TestBrick2(Context context, BrickSize spanSize, BrickPadding padding) {
            super(spanSize, padding);
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

        private TestPlaceholderBrick(Context context, BrickSize spanSize, BrickPadding padding, int placeholderLayoutId, boolean isDataReady) {
            super(spanSize, padding);
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
        return new TestBrick(context, new SimpleBrickSize(MAX) {
            @Override
            public int getSpans(Context context) {
                return HALF_SPAN;
            }

            @Override
            protected int size() {
                return HALF_SPAN;
            }

        }, new SimpleBrickPadding(PADDING), layoutId);
    }

    public BaseBrick generateBrickWithPlaceholderLayoutId(int placeholderLayoutId, boolean isDataReady) {
        return new TestPlaceholderBrick(context, new SimpleBrickSize(MAX) {
            @Override
            public int getSpans(Context context) {
                return HALF_SPAN;
            }

            @Override
            protected int size() {
                return HALF_SPAN;
            }

        }, new SimpleBrickPadding(PADDING), placeholderLayoutId, isDataReady);
    }

    public BaseBrick generateBrick() {
        return new TestBrick(context, new SimpleBrickSize(MAX_SPANS) {
            @Override
            public int getSpans(Context context) {
                return HALF_SPAN;
            }

            @Override
            protected int size() {
                return HALF_SPAN;
            }

        }, new SimpleBrickPadding(PADDING), 0);
    }

    public BaseBrick generateOtherBrick() {
        return new TestBrick2(context, new SimpleBrickSize(MAX_SPANS) {
            @Override
            public int getSpans(Context context) {
                return HALF_SPAN;
            }

            @Override
            protected int size() {
                return HALF_SPAN;
            }
        }, new SimpleBrickPadding(PADDING));
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
