/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.os.Handler;
import android.view.ViewGroup;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.viewholder.BrickViewHolder;
import com.wayfair.brickkit.viewholder.factory.BrickViewHolderFactory;
import com.wayfair.brickkit.viewholder.factory.BrickViewHolderFactoryData;

import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Extension of {@link androidx.recyclerview.widget.RecyclerView.Adapter} which combines a given
 * {@link BrickDataManager} to a given {@link RecyclerView}.
 */
public class BrickRecyclerAdapter extends RecyclerView.Adapter<BrickViewHolder> {
    private static final String TAG = BrickRecyclerAdapter.class.getName();

    private final BrickDataManager dataManager;
    private OnReachedItemAtPosition onReachedItemAtPosition;
    private RecyclerView recyclerView;
    private Handler handler;

    /**
     * Constructor.
     *
     * @param brickDataManager {@link BrickDataManager} for this adapter
     * @param recyclerView {@link RecyclerView} for this adapter
     */
    public BrickRecyclerAdapter(BrickDataManager brickDataManager, RecyclerView recyclerView) {
        if (recyclerView == null) {
            throw new IllegalArgumentException("Recycler View cannot be null");
        }
        this.recyclerView = recyclerView;
        this.dataManager = brickDataManager;
        handler = new Handler();
    }

    /**
     * Get brickDataManager {@link BrickDataManager}.
     *
     * @return brickDataManager {@link BrickDataManager} for this adapter
     */
    public BrickDataManager getBrickDataManager() {
        return dataManager;
    }

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyDataSetChanged()}.
     */
    public void safeNotifyDataSetChanged() {
        if (recyclerView.isComputingLayout()) {
            handler.post(new NotifyDataSetChangedRunnable(this));
        } else {
            super.notifyDataSetChanged();
        }
    }

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemChanged(int, Object)}.
     *
     * @param position Position of the item that has changed
     * @param payload Optional parameter, use null to identify a "full" update
     */
    public void safeNotifyItemChanged(final int position, @Nullable final Object payload) {
        if (position >= 0) {
            if (recyclerView.isComputingLayout()) {
                handler.post(new NotifyItemChangedWithPayloadRunnable(this, position, payload));
            } else {
                notifyItemChanged(position, payload);
            }
        }
    }

    /**
     *  Safe version of {@link RecyclerView.Adapter#notifyItemChanged(int)}.
     *
     * @param position Position of the item that has changed
     */
    public void safeNotifyItemChanged(final int position) {
        if (position >= 0) {
            if (recyclerView.isComputingLayout()) {
                handler.post(new NotifyItemChangedRunnable(this, position));
            } else {
                notifyItemChanged(position);
            }
        }
    }

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemInserted(int)}.
     *
     * @param position Position of the newly inserted item in the data set

     */
    public void safeNotifyItemInserted(final int position) {
        if (position >= 0) {
            if (recyclerView.isComputingLayout()) {
                handler.post(new NotifyItemInsertedRunnable(this, position));
            } else {
                notifyItemInserted(position);
            }
        }
    }

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemMoved(int, int)}.
     *
     * @param fromPosition Previous position of the item.
     * @param toPosition New position of the item.
     */
    public void safeNotifyItemMoved(final int fromPosition, final int toPosition) {
        if (fromPosition >= 0 && toPosition >= 0) {
            if (recyclerView.isComputingLayout()) {
                handler.post(new NotifyItemMovedRunnable(this, fromPosition, toPosition));
            } else {
                notifyItemMoved(fromPosition, toPosition);
            }
        }
    }

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemRangeChanged(int, int, Object)}.
     *
     * @param positionStart Position of the first item that has changed
     * @param itemCount Number of items that have changed
     * @param payload  Optional parameter, use null to identify a "full" update
     */
    public void safeNotifyItemRangeChanged(final int positionStart, final int itemCount, final Object payload) {
        if (positionStart >= 0 && itemCount >= 0) {
            if (recyclerView.isComputingLayout()) {
                handler.post(new NotifyItemRangeChangedWithPayloadRunnable(this, positionStart, itemCount, payload));
            } else {
                notifyItemRangeChanged(positionStart, itemCount, payload);
            }
        }
    }

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemRangeChanged(int, int)}.
     *
     * @param positionStart Position of the first item that has changed
     * @param itemCount Number of items that have changed
     */
    public void safeNotifyItemRangeChanged(final int positionStart, final int itemCount) {
        if (positionStart >= 0 && itemCount >= 0) {
            if (recyclerView.isComputingLayout()) {
                handler.post(new NotifyItemRangeChangedRunnable(this, positionStart, itemCount));
            } else {
                notifyItemRangeChanged(positionStart, itemCount);
            }
        }
    }

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemRangeInserted(int, int)}.
     *
     * @param positionStart Position of the first item that was inserted
     * @param itemCount Number of items inserted
     */
    public void safeNotifyItemRangeInserted(final int positionStart, final int itemCount) {
        if (positionStart >= 0 && itemCount >= 0) {
            if (recyclerView.isComputingLayout()) {
                handler.post(new NotifyItemRangeInsertedRunnable(this, positionStart, itemCount));
            } else {
                notifyItemRangeInserted(positionStart, itemCount);
            }
        }
    }

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemRangeRemoved(int, int)}.
     *
     * @param positionStart Previous position of the first item that was removed
     * @param itemCount Number of items removed from the data set
     */
    public void safeNotifyItemRangeRemoved(final int positionStart, final int itemCount) {
        if (positionStart >= 0 && itemCount >= 0) {
            if (recyclerView.isComputingLayout()) {
                handler.post(new NotifyItemRangeRemovedRunnable(this, positionStart, itemCount));
            } else {
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        }
    }

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemRemoved(int)}.
     *
     * @param position Position of the item that has now been removed
     */
    public void safeNotifyItemRemoved(final int position) {
        if (position >= 0) {
            if (recyclerView.isComputingLayout()) {
                handler.post(new NotifyItemRemovedRunnable(this, position));
            } else {
                notifyItemRemoved(position);
            }
        }
    }

    @NonNull
    @Override
    public BrickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // With there being multiple view holder types, a factory is used to create them.
        BrickViewHolderFactoryData data =
                new BrickViewHolderFactoryData(TAG, parent, viewType, dataManager);
        BrickViewHolderFactory factory = new BrickViewHolderFactory();
        return factory.createBrickViewHolder(data);
    }

    @Override
    public void onBindViewHolder(BrickViewHolder holder, int position) {
        BaseBrick baseBrick = dataManager.brickAtPosition(position);
        if (baseBrick != null) {
            if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                    ((StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams()).setFullSpan(
                            baseBrick.getSpanSize().getSpans(recyclerView.getContext()) == dataManager.getMaxSpanCount());
                }
            }

            if (baseBrick.isDataReady()) {
                baseBrick.onBindData(holder);
            } else {
                baseBrick.onBindPlaceholder(holder);
            }
            if (onReachedItemAtPosition != null) {
                onReachedItemAtPosition.bindingItemAtPosition(position);
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(BrickViewHolder holder) {
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(BrickViewHolder holder) {
        holder.releaseViewsOnDetach();
    }

    @Override
    public int getItemCount() {
        return dataManager.getRecyclerViewItems().size();
    }

    @Override
    public int getItemViewType(int position) {
        BaseBrick brick = dataManager.brickAtPosition(position);

        if (brick == null) {
            return BaseBrick.DEFAULT_LAYOUT_RES_ID;
        }

        if (!brick.isDataReady()) {
            return brick.getPlaceholderLayout();
        }

        return brick.getLayout();
    }

    /**
     * Get brick at the given position.
     *
     * @param position position of the brick to get
     * @return the brick at the given position
     */
    public BaseBrick get(int position) {
        return dataManager.getRecyclerViewItems().get(position);
    }

    /**
     * Get the index of the given brick.
     * @param brick brick to get the index of
     * @return index of the given brick
     */
    public int indexOf(BaseBrick brick) {
        return dataManager.getRecyclerViewItems().indexOf(brick);
    }

    /**
     * Get the first header before the given position.
     * @param position position before which to find the next header
     * @return the first header before the given position.
     */
    public BaseBrick getSectionHeader(int position) {
        if (position >= 0) {
            BaseBrick brick = dataManager.getRecyclerViewItems().get(position);
            if (brick != null && brick.isHeader()) {
                return brick;
            }

            ListIterator<BaseBrick> iterator = dataManager.getRecyclerViewItems().listIterator(position);

            while (iterator.hasPrevious()) {
                brick = iterator.previous();
                if (brick != null && brick.isHeader()) {
                    return brick;
                }
            }
        }

        return null;
    }

    /**
     * Get the first footer after the given position unless the given position is the last element.
     * @param position position after which to find the next footer
     * @return the first footer after the given position.
     */
    public BaseBrick getSectionFooter(int position) {
        if (position >= 0 && dataManager.getRecyclerViewItems().size() - 1 > position) {
            BaseBrick brick = dataManager.getRecyclerViewItems().get(position);
            if (brick != null && brick.isFooter()) {
                return brick;
            }

            ListIterator<BaseBrick> iterator = dataManager.getRecyclerViewItems().listIterator(position);

            while (iterator.hasNext()) {
                brick = iterator.next();
                if (brick != null && brick.isFooter()) {
                    return brick;
                }
            }
        }

        return null;
    }

    /**
     * Set an {@link OnReachedItemAtPosition}.
     *
     * @param onReachedItemAtPosition {@link OnReachedItemAtPosition} to set
     */
    public void setOnReachedItemAtPosition(OnReachedItemAtPosition onReachedItemAtPosition) {
        this.onReachedItemAtPosition = onReachedItemAtPosition;
    }

    /**
     * Get the {@link RecyclerView} for this adapter.
     * @return the {@link RecyclerView} for this adapter
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * Runnable which will call {@link BrickRecyclerAdapter#notifyDataSetChanged()} on the adapter.
     */
    static final class NotifyDataSetChangedRunnable implements Runnable {
        private BrickRecyclerAdapter adapter;

        /**
         * Constructor.
         *
         * @param adapter {@link BrickRecyclerAdapter} to notify
         */
        NotifyDataSetChangedRunnable(BrickRecyclerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void run() {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Runnable which will call {@link BrickRecyclerAdapter#notifyItemChanged(int, Object)} on the adapter.
     */
    static final class NotifyItemChangedWithPayloadRunnable implements Runnable {
        private BrickRecyclerAdapter adapter;
        private int position;
        private Object payload;

        /**
         * Constructor.
         *
         * @param adapter {@link BrickRecyclerAdapter} to notify
         * @param position Position of the item that has changed
         * @param payload Optional parameter, use null to identify a "full" update
         */
        NotifyItemChangedWithPayloadRunnable(BrickRecyclerAdapter adapter, final int position, final Object payload) {
            this.adapter = adapter;
            this.position = position;
            this.payload = payload;
        }

        @Override
        public void run() {
            adapter.notifyItemChanged(position, payload);
        }
    }

    /**
     * Runnable which will call {@link BrickRecyclerAdapter#notifyItemChanged(int)} on the adapter.
     */
    static final class NotifyItemChangedRunnable implements Runnable {
        private BrickRecyclerAdapter adapter;
        private int position;

        /**
         * Constructor.
         *
         * @param adapter {@link BrickRecyclerAdapter} to notify
         * @param position Position of the item that has changed
         */
        NotifyItemChangedRunnable(BrickRecyclerAdapter adapter, final int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        public void run() {
            adapter.notifyItemChanged(position);
        }
    }

    /**
     * Runnable which will call {@link BrickRecyclerAdapter#notifyItemInserted(int)} on the adapter.
     */
    static final class NotifyItemInsertedRunnable implements Runnable {
        private BrickRecyclerAdapter adapter;
        private int position;

        /**
         * Constructor.
         *
         * @param adapter {@link BrickRecyclerAdapter} to notify
         * @param position Position of the newly inserted item in the data set
         */
        NotifyItemInsertedRunnable(BrickRecyclerAdapter adapter, final int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        public void run() {
            adapter.notifyItemInserted(position);
        }
    }

    /**
     * Runnable which will call {@link BrickRecyclerAdapter#notifyItemMoved(int, int)} on the adapter.
     */
    static final class NotifyItemMovedRunnable implements Runnable {
        private BrickRecyclerAdapter adapter;
        private int fromPosition;
        private int toPosition;

        /**
         * Constructor.
         *
         * @param adapter {@link BrickRecyclerAdapter} to notify
         * @param fromPosition Previous position of the item.
         * @param toPosition New position of the item.
         */
        NotifyItemMovedRunnable(BrickRecyclerAdapter adapter, final int fromPosition, final int toPosition) {
            this.adapter = adapter;
            this.fromPosition = fromPosition;
            this.toPosition = toPosition;

        }

        @Override
        public void run() {
            adapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    /**
     * Runnable which will call {@link BrickRecyclerAdapter#notifyItemRangeChanged(int, int, Object)} on the adapter.
     */
    static final class NotifyItemRangeChangedWithPayloadRunnable implements Runnable {
        private BrickRecyclerAdapter adapter;
        private int positionStart;
        private int itemCount;
        private Object payload;

        /**
         * Constructor.
         *
         * @param adapter {@link BrickRecyclerAdapter} to notify
         * @param positionStart Position of the first item that has changed
         * @param itemCount Number of items that have changed
         * @param payload  Optional parameter, use null to identify a "full" update
         */
        NotifyItemRangeChangedWithPayloadRunnable(BrickRecyclerAdapter adapter, final int positionStart, final int itemCount, final Object payload) {
            this.adapter = adapter;
            this.positionStart = positionStart;
            this.itemCount = itemCount;
            this.payload = payload;
        }

        @Override
        public void run() {
            adapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }
    }

    /**
     * Runnable which will call {@link BrickRecyclerAdapter#notifyItemRangeChanged(int, int)} on the adapter.
     */
    static final class NotifyItemRangeChangedRunnable implements Runnable {
        private BrickRecyclerAdapter adapter;
        private int positionStart;
        private int itemCount;

        /**
         * Constructor.
         *
         * @param adapter {@link BrickRecyclerAdapter} to notify
         * @param positionStart Position of the first item that has changed
         * @param itemCount Number of items that have changed
         */
        NotifyItemRangeChangedRunnable(BrickRecyclerAdapter adapter, final int positionStart, final int itemCount) {
            this.adapter = adapter;
            this.positionStart = positionStart;
            this.itemCount = itemCount;
        }

        @Override
        public void run() {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    /**
     * Runnable which will call {@link BrickRecyclerAdapter#notifyItemRangeInserted(int, int)} on the adapter.
     */
    static final class NotifyItemRangeInsertedRunnable implements Runnable {
        private BrickRecyclerAdapter adapter;
        private int positionStart;
        private int itemCount;

        /**
         * Constructor.
         *
         * @param adapter {@link BrickRecyclerAdapter} to notify
         * @param positionStart Position of the first item that has inserted
         * @param itemCount Number of items that have inserted
         */
        NotifyItemRangeInsertedRunnable(BrickRecyclerAdapter adapter, final int positionStart, final int itemCount) {
            this.adapter = adapter;
            this.positionStart = positionStart;
            this.itemCount = itemCount;
        }

        @Override
        public void run() {
            adapter.notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    /**
     * Runnable which will call {@link BrickRecyclerAdapter#notifyItemRangeRemoved(int, int)} on the adapter.
     */
    static final class NotifyItemRangeRemovedRunnable implements Runnable {
        private BrickRecyclerAdapter adapter;
        private int positionStart;
        private int itemCount;

        /**
         * Constructor.
         *
         * @param adapter {@link BrickRecyclerAdapter} to notify
         * @param positionStart Previous position of the first item that was removed
         * @param itemCount Number of items removed from the data set
         */
        NotifyItemRangeRemovedRunnable(BrickRecyclerAdapter adapter, final int positionStart, final int itemCount) {
            this.adapter = adapter;
            this.positionStart = positionStart;
            this.itemCount = itemCount;
        }

        @Override
        public void run() {
            adapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    /**
     * Runnable which will call {@link BrickRecyclerAdapter#notifyItemRemoved(int)} on the adapter.
     */
    static final class NotifyItemRemovedRunnable implements Runnable {
        private BrickRecyclerAdapter adapter;
        private int position;

        /**
         * Constructor.
         *
         * @param adapter {@link BrickRecyclerAdapter} to notify
         * @param position Position of the item that has now been removed
         */
        NotifyItemRemovedRunnable(BrickRecyclerAdapter adapter, final int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        public void run() {
            adapter.notifyItemRemoved(position);
        }
    }
}
