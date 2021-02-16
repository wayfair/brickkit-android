/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.content.Context;
import android.util.Log;
import android.widget.GridLayout;

import com.wayfair.brickkit.animator.AvoidFlickerItemAnimator;
import com.wayfair.brickkit.brick.BaseBrick;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Class which maintains a collection of bricks and manages how they are laid out in an provided RecyclerView.
 * <p>
 * This class maintains the bricks and handles notifying the underlying adapter when items are updated.
 */
public class BrickDataManager implements Serializable {
    private static final String TAG = BrickDataManager.class.getSimpleName();
    public static final int SPAN_COUNT = 240;

    private static final int DEFAULT_BRICK_POSITION = 0;

    @Nullable
    private BrickRecyclerAdapter brickRecyclerAdapter;
    private List<BaseBrick> items = new LinkedList<>();
    private List<BaseBrick> currentlyVisibleItems = new LinkedList<>();
    private boolean dataHasChanged;
    private boolean vertical;
    private RecyclerView recyclerView;
    private DataSetChangedListener dataSetChangedListener;

    /**
     *
     * @return true if no items have been added to the BrickDataManager
     */
    public boolean isEmpty() {
        return getDataManagerItems().isEmpty();
    }

    /**
     * Set the recycler view for this BrickDataManager, this will setup the underlying adapter and begin
     * displaying any bricks Use setHorizontalRecyclerView instead if you want a horizontal RecyclerView.
     *
     * @param recyclerView {@link RecyclerView} to put views in
     */
    public void setRecyclerView(@NonNull RecyclerView recyclerView) {
        setRecyclerView(recyclerView, RecyclerView.VERTICAL);
    }

    /**
     * Set the recycler view for this BrickDataManager for a horizontal RecyclerView, this will setup the
     * underlying adapter and begin displaying any bricks.
     *
     * @param recyclerView {@link RecyclerView} to put views in
     */
    public void setHorizontalRecyclerView(@NonNull RecyclerView recyclerView) {
        setRecyclerView(recyclerView, RecyclerView.HORIZONTAL);
    }

    private void setRecyclerView(@NonNull RecyclerView recyclerView, int orientation) {
        this.brickRecyclerAdapter = new BrickRecyclerAdapter(this, recyclerView);
        this.vertical = orientation == GridLayout.VERTICAL;

        this.recyclerView = recyclerView;
        this.recyclerView.setAdapter(brickRecyclerAdapter);
        while (recyclerView.getItemDecorationCount() > 0) {
            recyclerView.removeItemDecorationAt(0);
        }
        this.recyclerView.addItemDecoration(new BrickRecyclerItemDecoration(this));
        this.recyclerView.setItemAnimator(new AvoidFlickerItemAnimator());

        GridLayoutManager gridLayoutManager = new WFGridLayoutManager(recyclerView.getContext(), orientation);
        gridLayoutManager.setSpanSizeLookup(new BrickSpanSizeLookup(recyclerView.getContext(), this));
        recyclerView.setLayoutManager(gridLayoutManager);

        List<BaseBrick> recyclerViewItems = getRecyclerViewItems();
        int itemCount = recyclerViewItems.size();
        if (itemCount > 0) {
            computePaddingPosition(recyclerViewItems.get(0));
            brickRecyclerAdapter.notifyItemRangeInserted(0, itemCount);
        }
    }

    /**
     * Get the recycler view if available.
     *
     * @return the attached recycler view, null if none has been attached
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * Get the items visible in the {@link RecyclerView}.
     *
     * @return List of visible bricks.
     */
    @NonNull
    public List<BaseBrick> getRecyclerViewItems() {
        if (dataHasChanged) {
            currentlyVisibleItems = new LinkedList<>();

            for (BaseBrick item : items) {
                if (!item.isHidden()) {
                    currentlyVisibleItems.add(item);
                }
            }

            dataHasChanged = false;
        }

        return currentlyVisibleItems;
    }

    /**
     * Use DiffUtils to update the recycler view.
     *
     * @param bricks the new list of bricks.
     */
    public void updateBricks(List<BaseBrick> bricks) {
        List<BaseBrick> newVisibleBricks = new LinkedList<>();
        for (BaseBrick brick : bricks) {
            if (!brick.isHidden()) {
                newVisibleBricks.add(brick);
            }
        }

        updateBricks(bricks, new BrickDiffUtilCallback(currentlyVisibleItems, newVisibleBricks));
    }

    /**
     * Use DiffUtils to update the recycler view.
     *
     * @param bricks the new list of bricks.
     * @param diffUtilCallback {@link DiffUtil.Callback} to use
     */
    public void updateBricks(List<BaseBrick> bricks, DiffUtil.Callback diffUtilCallback) {
        // clean caches and the DataManager reference on each brick
        for (BaseBrick item : items) {
            item.setDataManager(null);
        }

        for (BaseBrick item : bricks) {
            item.setDataManager(this);
        }

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        items.clear();
        items.addAll(bricks);
        dataHasChanged();
        if (brickRecyclerAdapter != null) {
            diffResult.dispatchUpdatesTo(brickRecyclerAdapter);
        }
    }

    /**
     * Get all bricks.
     *
     * @return List of all bricks.
     */
    public List<BaseBrick> getDataManagerItems() {
        return items;
    }

    /**
     * Inserts brick after all other bricks.
     *
     * @param item the brick to add
     */
    public void addLast(@Nullable BaseBrick item) {
        if (null == item) {
            Log.w(TAG, "addLast: The item is null.");
            return; // safety
        }

        this.items.add(item);
        item.setDataManager(this);

        if (!item.isHidden()) {
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                int refreshStartIndex = computePaddingPosition(item);

                int itemCount = getRecyclerViewItems().size();
                int position = itemCount - 1;
                brickRecyclerAdapter.safeNotifyItemInserted(position);

                int newItemCount = itemCount - 1 - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, newItemCount);
            }
        }
    }

    /**
     * Inserts brick before all other bricks.
     *
     * @param item the brick to add
     */
    public void addFirst(@Nullable BaseBrick item) {
        if (null == item) {
            Log.w(TAG, "addFirst: The item is null.");
            return; // safety
        }

        this.items.add(0, item);
        item.setDataManager(this);

        if (!item.isHidden()) {
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                computePaddingPosition(item);
                brickRecyclerAdapter.safeNotifyItemInserted(0);

                int refreshStartIndex = 1;
                int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
            }
        }
    }

    /**
     * Inserts collection of bricks after all other bricks.
     *
     * @param items the bricks to add
     */
    public void addLast(@Nullable Collection<? extends BaseBrick> items) {
        if (null == items) {
            Log.w(TAG, "addLast(Collection): The items are null.");
            return; // safety
        }

        int index = getRecyclerViewItems().size();
        this.items.addAll(items);

        for (BaseBrick item : items) {
            item.setDataManager(this);
        }

        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            dataHasChanged();

            List<BaseBrick> brickItems = getRecyclerViewItems();

            if (brickRecyclerAdapter != null && index < brickItems.size()) {
                BaseBrick item = brickItems.get(index);

                int refreshStartIndex = computePaddingPosition(item);
                brickRecyclerAdapter.safeNotifyItemRangeInserted(index, visibleCount);

                int itemCount = brickItems.size() - visibleCount - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
            }
        }
    }

    /**
     * Gets count of visible bricks in a collecton of bricks.
     *
     * @param items collection of bricks to get visible count from
     * @return number of visible bricks in the collection
     */
    private int getVisibleCount(Collection<? extends BaseBrick> items) {
        int visibleCount = 0;
        for (BaseBrick brick : items) {
            if (!brick.isHidden()) {
                visibleCount++;
            }
        }

        return visibleCount;
    }

    /**
     * Gets first visible brick in a collecton of bricks.
     *
     * @param items collection of bricks to get visible brick from
     * @return first visible brick in the collection
     */
    private BaseBrick getFirstVisibleItem(Collection<? extends BaseBrick> items) {
        for (BaseBrick brick : items) {
            if (!brick.isHidden()) {
                return brick;
            }
        }

        return null;
    }

    /**
     * Inserts brick before the anchor brick.
     *
     * @param anchor brick to insert before
     * @param item   the brick to add
     */
    public void addBeforeItem(BaseBrick anchor, BaseBrick item) {
        int anchorDataManagerIndex = items.indexOf(anchor);

        if (anchorDataManagerIndex == -1) {
            items.add(0, item);
        } else {
            items.add(anchorDataManagerIndex, item);
        }

        item.setDataManager(this);

        if (!item.isHidden()) {
            dataHasChanged();
            if (brickRecyclerAdapter != null) {
                int refreshStartIndex = computePaddingPosition(item);
                int adapterIndex = getRecyclerViewItems().indexOf(item);
                if (adapterIndex != -1) {
                    brickRecyclerAdapter.safeNotifyItemInserted(adapterIndex);
                }
                int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
            }
        }
    }

    /**
     * Inserts brick before the anchor brick.
     *
     * @param anchor brick to insert before
     * @param items  the bricks to add
     */
    public void addBeforeItem(BaseBrick anchor, Collection<? extends BaseBrick> items) {
        int index = this.items.indexOf(anchor);

        if (index == -1) {
            index = 0;
        }
        this.items.addAll(index, items);
        for (BaseBrick item : items) {
            item.setDataManager(this);
        }

        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            BaseBrick firstVisibleItem = getFirstVisibleItem(items);
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                int refreshStartIndex = computePaddingPosition(firstVisibleItem);
                int adapterIndex = getRecyclerViewItems().indexOf(firstVisibleItem);
                if (adapterIndex != -1) {
                    brickRecyclerAdapter.safeNotifyItemRangeInserted(adapterIndex, visibleCount);
                }

                int itemCount = getRecyclerViewItems().size() - visibleCount - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
            }
        }
    }

    /**
     * Inserts brick after the anchor brick.
     *
     * @param anchor brick to insert after
     * @param item   the brick to add
     */
    public void addAfterItem(BaseBrick anchor, BaseBrick item) {
        int anchorDataManagerIndex = this.items.indexOf(anchor);

        if (anchorDataManagerIndex == -1) {
            this.items.add(item);
        } else {
            this.items.add(anchorDataManagerIndex + 1, item);
        }

        item.setDataManager(this);

        if (!item.isHidden()) {
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                int refreshStartIndex = computePaddingPosition(item);
                int adapterIndex = getRecyclerViewItems().indexOf(item);
                if (adapterIndex != -1) {
                    brickRecyclerAdapter.safeNotifyItemInserted(adapterIndex);
                }
                int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
            }
        }
    }

    /**
     * Remove a brick.
     *
     * @param item the brick to remove
     */
    public void removeItem(BaseBrick item) {
        this.items.remove(item);

        item.setDataManager(null);

        if (!item.isHidden()) {
            int index = getRecyclerViewItems().indexOf(item);
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                brickRecyclerAdapter.safeNotifyItemRemoved(index);
                List<BaseBrick> recyclerViewItems = getRecyclerViewItems();

                if (index >= 0 && index < recyclerViewItems.size()) {
                    int refreshStartIndex = computePaddingPosition(recyclerViewItems.get(index));
                    int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
                }
            }
        }
    }

    /**
     * Remove a collection of bricks.
     *
     * @param items the bricks to remove
     */
    public void removeItems(Collection<? extends BaseBrick> items) {
        this.items.removeAll(items);
        for (BaseBrick item : items) {
            item.setDataManager(null);
        }
        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            dataHasChanged();
            if (brickRecyclerAdapter != null) {
                if (!getRecyclerViewItems().isEmpty()) {
                    computePaddingPosition(getRecyclerViewItems().get(0));
                }

                brickRecyclerAdapter.safeNotifyDataSetChanged();
            }
        }
    }

    /**
     * Remove all bricks.
     */
    public void clear() {
        int startCount = getRecyclerViewItems().size();
        this.items = new LinkedList<>();
        dataHasChanged();
        if (brickRecyclerAdapter != null) {
            brickRecyclerAdapter.safeNotifyItemRangeRemoved(0, startCount);
        }
    }

    /**
     * Replace a target brick with a replacement brick.
     *
     * @param target      brick to replace
     * @param replacement the brick being added
     */
    public synchronized void replaceItem(BaseBrick target, BaseBrick replacement) {
        int targetIndexInItems = items.indexOf(target);

        if (targetIndexInItems == -1) {
            Log.w(TAG, "replaceItem: the target index is -1");
            return; // safety: avoid an index out of bounds exception
        }

        int targetIndexInAdapter = getRecyclerViewItems().indexOf(target);
        boolean indexNotFound = -1 == targetIndexInAdapter;

        if (indexNotFound == replacement.isHidden()) {
            if (!target.isHidden()) {
                replaceBrick(targetIndexInItems, replacement);

                if (brickRecyclerAdapter == null) {
                    return;  // safety: avoid a null pointer exception
                }

                // item "change" notification
                int refreshStartIndex = computePaddingPosition(replacement);
                brickRecyclerAdapter.safeNotifyItemChanged(targetIndexInAdapter);
                int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
            }
        } else {
            replaceBrick(targetIndexInItems, replacement);

            if (brickRecyclerAdapter == null) {
                return;  // safety: avoid a null pointer exception
            }

            if (replacement.isHidden()) {
                // item "removed" notification
                int refreshStartIndex = computePaddingPosition(target);
                brickRecyclerAdapter.safeNotifyItemRemoved(targetIndexInAdapter);
                int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
            } else {
                // item "inserted" notification
                int adapterIndex = getRecyclerViewItems().indexOf(replacement);
                int refreshStartIndex = computePaddingPosition(target);
                brickRecyclerAdapter.safeNotifyItemInserted(adapterIndex);
                int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
            }
        }
    }

    /**
     * Replaces a brick, located at the targetBrickIndex, with the replacement brick.
     *
     * @param targetBrickIndex the index of the brick to replace
     * @param replacement      the brick to replace with
     */
    private void replaceBrick(int targetBrickIndex, BaseBrick replacement) {
        BaseBrick brickToRemove = items.get(targetBrickIndex);
        items.remove(brickToRemove);
        brickToRemove.setDataManager(null);
        items.add(targetBrickIndex, replacement);
        replacement.setDataManager(this);
        dataHasChanged();
    }

    /**
     * Refresh a brick.
     *
     * @param item the brick to refresh
     */
    public void refreshItem(BaseBrick item) {
        int index = getRecyclerViewItems().indexOf(item);
        if (items.contains(item) && index != -1 && !item.isHidden() && brickRecyclerAdapter != null) {
            brickRecyclerAdapter.safeNotifyItemChanged(index);
        }
    }

    /**
     * Update the visibility of the brick and set it invisible.
     *
     * @param item the brick to be hided
     */
    public void hideItem(BaseBrick item) {
        item.setHidden(true);
        int index = getRecyclerViewItems().indexOf(item);
        if (items.contains(item) && index != -1) {
            dataHasChanged();
            if (brickRecyclerAdapter != null) {
                brickRecyclerAdapter.safeNotifyItemRemoved(index);
                if (index < getRecyclerViewItems().size()) {
                    BaseBrick brick = getRecyclerViewItems().get(index);
                    int refreshStartIndex = computePaddingPosition(brick);
                    int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
                }
            }
        }
    }

    /**
     * Update the visibility of the brick and set it visible.
     *
     * @param item the brick to be showed
     */
    public void showItem(BaseBrick item) {
        item.setHidden(false);
        if (items.contains(item) && !getRecyclerViewItems().contains(item)) {
            dataHasChanged();
            if (brickRecyclerAdapter != null) {
                int index = getRecyclerViewItems().indexOf(item);
                int refreshStartIndex = computePaddingPosition(item);
                brickRecyclerAdapter.safeNotifyItemInserted(index);
                int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
            }
        }
    }

    /**
     * Helper method to tell manager to update the items returned from getRecyclerViewItems().
     */
    private void dataHasChanged() {
        dataHasChanged = true;
        if (dataSetChangedListener != null) {
            dataSetChangedListener.onDataSetChanged();
        }
    }

    /**
     * Smooth scroll to the given brick.
     *
     * @param item brick to smooth scroll to
     */
    public void smoothScrollToBrick(BaseBrick item) {
        int index = getRecyclerViewItems().indexOf(item);

        if (index != -1) {
            recyclerView.smoothScrollToPosition(index);
        }
    }

    /**
     * Checks / Determines if the brick is on the left wall, first row, right wall, last row.
     *
     * @param currentBrick BaseBrick item that was changed / added / removed
     * @return index of first modified, non-null item or {@link #DEFAULT_BRICK_POSITION} for a null
     * brick.
     */
    private int computePaddingPosition(@Nullable BaseBrick currentBrick) {
        if (null == currentBrick) {
            Log.w(TAG, "computePaddingPosition: currentBrick is null");
            // When the brick is null, return the default position value to avoid potential issues
            // with consuming code.
            return DEFAULT_BRICK_POSITION;
        }

        if (recyclerView == null) {
            return DEFAULT_BRICK_POSITION;
        }
        Context context = recyclerView.getContext();

        int currentRow = 0;
        int startingBrickIndex = getRecyclerViewItems().indexOf(currentBrick);

        if (startingBrickIndex < 0) {
            startingBrickIndex = DEFAULT_BRICK_POSITION;
        }

        ListIterator<BaseBrick> iterator = getRecyclerViewItems().listIterator(startingBrickIndex);

        while (iterator.hasPrevious()) {
            currentBrick = iterator.previous();
            if ((vertical && currentBrick.isOnLeftWall())
                    || (!vertical && currentBrick.isInFirstRow())) {
                break;
            }
        }

        boolean topRow = false;
        if (!iterator.hasPrevious()) {
            topRow = true;
        }

        if (vertical) {
            currentBrick.setOnLeftWall(true);
            currentBrick.setInFirstRow(false);
        } else {
            currentBrick.setOnLeftWall(false);
            currentBrick.setInFirstRow(true);
        }
        currentBrick.setOnRightWall(false);
        currentBrick.setInLastRow(false);

        currentRow += currentBrick.getSpanSize().getSpans(context);

        if (currentRow == SPAN_COUNT) {
            if (vertical) {
                currentBrick.setOnRightWall(true);
            } else {
                currentBrick.setInLastRow(true);
            }
        }

        if (topRow) {
            if (vertical) {
                currentBrick.setInFirstRow(true);
            } else {
                currentBrick.setOnLeftWall(true);
            }
        }

        if (iterator.hasNext()) {
            iterator.next();
        }

        while (iterator.hasNext()) {
            currentBrick = iterator.next();

            currentBrick.setOnLeftWall(false);
            currentBrick.setInFirstRow(false);
            currentBrick.setOnRightWall(false);
            currentBrick.setInLastRow(false);

            if (currentRow == 0) {
                if (vertical) {
                    currentBrick.setOnLeftWall(true);
                } else {
                    currentBrick.setInFirstRow(true);
                }
                topRow = false;
            }

            if (currentRow > SPAN_COUNT) {
                currentRow = 0;
            }

            currentRow += currentBrick.getSpanSize().getSpans(context);

            if (currentRow > SPAN_COUNT) {
                if (vertical) {
                    currentBrick.setOnLeftWall(true);
                } else {
                    currentBrick.setInFirstRow(true);
                }
                currentRow = currentBrick.getSpanSize().getSpans(context);
                topRow = false;
            }

            if (currentRow == SPAN_COUNT) {
                if (vertical) {
                    currentBrick.setOnRightWall(true);
                } else {
                    currentBrick.setInLastRow(true);
                }
                currentRow = 0;
            }

            if (topRow) {
                if (vertical) {
                    currentBrick.setInFirstRow(true);
                } else {
                    currentBrick.setOnLeftWall(true);
                }
            }
        }

        addBottomToRowEndingWithItem(iterator);

        return startingBrickIndex;
    }

    /**
     * Sets all items to being in the last row from the current brick to the left most brick in that row.
     *
     * @param iterator iterator to use to find items in the row
     */
    private void addBottomToRowEndingWithItem(ListIterator<BaseBrick> iterator) {
        while (iterator.hasPrevious()) {
            BaseBrick currentBrick = iterator.previous();

            if (vertical) {
                currentBrick.setInLastRow(true);
            } else {
                currentBrick.setOnRightWall(true);
            }

            if ((vertical && currentBrick.isOnLeftWall())
                    || (!vertical && currentBrick.isInFirstRow())) {
                break;
            }
        }
    }

    /**
     * Method called to release any related resources.
     */
    public void onDestroyView() {
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        brickRecyclerAdapter = null;
        recyclerView = null;
    }

    /**
     * Allows the user to set a listener that will get called whenever the data set changes.
     *
     * @param listener listener which will be notified whenever the data set changes
     */
    public void setDataSetChangedListener(DataSetChangedListener listener) {
        this.dataSetChangedListener = listener;
    }

    /**
     * Set an [OnReachedItemAtPosition].
     *
     * @param onReachedItemAtPosition [OnReachedItemAtPosition] to set
     */
    public void setOnReachedItemAtPosition(@Nullable OnReachedItemAtPosition onReachedItemAtPosition) {
        if (brickRecyclerAdapter != null) {
            brickRecyclerAdapter.setOnReachedItemAtPosition(onReachedItemAtPosition);
        }
    }

    /**
     * Exception is thrown when dataSet gets updated synchronously at different thread {@link GridLayoutManager}.
     */
    private static class WFGridLayoutManager extends GridLayoutManager {

        /**
         * Constructor for the WFGridLayoutManager.
         *
         * @param context       {@link Context} to use
         * @param orientation   Layout orientation. Should be {@link GridLayoutManager#HORIZONTAL} or {@link GridLayoutManager#VERTICAL}.
         */
        WFGridLayoutManager(Context context, int orientation) {
            super(context, BrickDataManager.SPAN_COUNT, orientation, false);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }
}
