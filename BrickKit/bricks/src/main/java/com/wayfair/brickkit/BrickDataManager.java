package com.wayfair.brickkit;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.GridLayout;

import com.wayfair.brickkit.animator.AvoidFlickerItemAnimator;
import com.wayfair.brickkit.behavior.BrickBehavior;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.brick.BrickProvider;
import com.wayfair.brickkit.util.CollectionUtil;
import com.wayfair.brickkit.util.LinkedListUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Class which maintains a collection of bricks and manages how they are laid out in an provided RecyclerView.
 * <p>
 * This class maintains the bricks and handles notifying the underlying adapter when items are updated.
 * <p>
 * Copyright © 2017 Wayfair. All rights reserved.
 */
public class BrickDataManager implements Serializable, BrickProvider {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @SuppressWarnings("WeakerAccess")
    static final int NO_PADDING_POSITION = -1;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @SuppressWarnings("WeakerAccess")
    static final int DEFAULT_BRICK_POSITION = 0;

    private static final int NO_ADAPTER_INDEX = -1;

    private ArrayList<BrickBehavior> behaviors;
    private BrickRecyclerAdapter brickRecyclerAdapter;
    private final int maxSpanCount;
    private final SparseArray<LinkedList<BaseBrick>> idCache;
    private final HashMap<Object, LinkedList<BaseBrick>> tagCache;
    private LinkedList<BaseBrick> items;
    private LinkedList<BaseBrick> currentlyVisibleItems;
    private boolean dataHasChanged;
    private Context context;
    private boolean dragAndDrop;
    private boolean swipeToDismiss;
    private boolean vertical;
    private RecyclerView recyclerView;
    private View recyclerViewParent;
    private RecyclerView.ItemDecoration itemDecoration;

    /**
     * Constructor.
     *
     * @param maxSpanCount max spans used when laying out bricks
     */
    public BrickDataManager(int maxSpanCount) {
        this.maxSpanCount = maxSpanCount;
        this.items = new LinkedList<>();
        this.idCache = new SparseArray<>();
        this.tagCache = new HashMap<>();
        this.behaviors = new ArrayList<>();
        this.currentlyVisibleItems = new LinkedList<>();
    }

    /**
     * Sets the layout for the recycler view to be a GridLayout.
     *
     * @param orientation the orientation of the layout
     * @param reverse     whether the layout should make items appear in reverse order
     */
    public void applyGridLayout(int orientation, boolean reverse) {
        if (recyclerView != null) {
            GridLayoutManager gridLayoutManager = new WFGridLayoutManager(context, maxSpanCount, orientation, reverse);
            gridLayoutManager.setSpanSizeLookup(new BrickSpanSizeLookup(context, this));
            recyclerView.setLayoutManager(gridLayoutManager);
        }
    }

    /**
     * Sets the layout for the recyclerview to be a StaggeredGridLayout.
     * SetItemPrefetch is disabled because of a known android bug (March 2016).
     * It creates a fatal on staggeredGrid when scrolling quickly while paging.
     *
     * @param spanCount   the number of columns to which the bricks will conform
     * @param orientation the orientation of the layout
     */
    public void applyStaggeredGridLayout(int spanCount, int orientation) {
        if (recyclerView != null) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = new NpaStaggeredGridLayoutManager(spanCount, orientation);
            staggeredGridLayoutManager.setItemPrefetchEnabled(false);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
        }
    }

    /**
     * Set the recycler view for this BrickDataManager, this will setup the underlying adapter and begin displaying any bricks.
     *
     * @param context            {@link Context} to use
     * @param recyclerView       {@link RecyclerView} to put views in
     * @param orientation        Layout orientation. Should be {@link GridLayoutManager#HORIZONTAL} or {@link GridLayoutManager#VERTICAL}.
     * @param reverse            When set to true, layouts from end to start.
     * @param recyclerViewParent View RecyclerView's parent view
     */
    public void setRecyclerView(Context context, RecyclerView recyclerView, int orientation, boolean reverse, View recyclerViewParent) {
        this.context = context;
        this.brickRecyclerAdapter = new BrickRecyclerAdapter(this, recyclerView);
        this.vertical = orientation == GridLayout.VERTICAL;
        this.recyclerViewParent = recyclerViewParent;

        this.recyclerView = recyclerView;
        this.recyclerView.setAdapter(brickRecyclerAdapter);
        if (itemDecoration != null) {
            this.recyclerView.removeItemDecoration(itemDecoration);
        }
        this.itemDecoration = new BrickRecyclerItemDecoration(this);
        this.recyclerView.addItemDecoration(itemDecoration);
        this.recyclerView.setItemAnimator(new AvoidFlickerItemAnimator());

        applyGridLayout(orientation, reverse);

        for (BrickBehavior behavior : behaviors) {
            behavior.attachToRecyclerView(recyclerView);
        }

        LinkedList<BaseBrick> items = getRecyclerViewItems();
        int itemCount = items.size();
        if (itemCount > 0) {
            computePaddingPosition(items.get(0));
            brickRecyclerAdapter.notifyItemRangeInserted(0, itemCount);
        }
    }

    /**
     * Get the context.
     *
     * @return the context from getContext()
     */
    public Context getContext() {
        return context;
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
     * Toggle the ability to drag and drop bricks.
     *
     * @param dragAndDrop Toggle drag and drop for bricks.
     */
    public void setDragAndDrop(boolean dragAndDrop) {
        this.dragAndDrop = dragAndDrop;

        attachTouchHelper();
    }

    /**
     * Get the recycler view's parent.
     *
     * @return the attached recyclerview's parent, null if none has been attached
     */
    public View getRecyclerViewParent() {
        return recyclerViewParent;
    }

    /**
     * Toggle the ability to swipe to dismiss bricks.
     *
     * @param swipeToDismiss Toggle swipe to dismiss for bricks.
     */
    public void setSwipeToDismiss(boolean swipeToDismiss) {
        this.swipeToDismiss = swipeToDismiss;

        attachTouchHelper();
    }

    /**
     * Is drag and drop enabled.
     *
     * @return Is drag and drop enabled.
     */
    public boolean getDragAndDrop() {
        return dragAndDrop;
    }

    /**
     * Is swipe to dismiss enabled.
     *
     * @return Is swipe to dismiss enabled.
     */
    public boolean getSwipeToDismiss() {
        return swipeToDismiss;
    }

    /**
     * This attaches the touch helper to the recycler view if swipe or drag and drop are enabled.
     */
    private void attachTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BrickItemTouchHelperCallback(this));

        if ((dragAndDrop || swipeToDismiss) && brickRecyclerAdapter != null) {
            itemTouchHelper.attachToRecyclerView(brickRecyclerAdapter.getRecyclerView());
        } else {
            itemTouchHelper.attachToRecyclerView(null);
        }
    }

    /**
     * Get the items visible in the {@link RecyclerView}.
     *
     * @return LinkedList of visible bricks.
     */
    @NonNull
    public LinkedList<BaseBrick> getRecyclerViewItems() {
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
    public void updateBricks(LinkedList<BaseBrick> bricks) {
        LinkedList<BaseBrick> newVisibleBricks = new LinkedList<>();

        // clean caches and the DataManager reference on each brick
        idCache.clear();
        tagCache.clear();
        for (BaseBrick item : items) {
            item.setDataManager(null);
        }

        for (BaseBrick item : bricks) {
            addToIdCache(item);
            addToTagCache(item);
            item.setDataManager(this);
        }
        for (BaseBrick brick : bricks) {
            if (!brick.isHidden()) {
                newVisibleBricks.add(brick);
            }
        }
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BrickDiffUtilCallback(currentlyVisibleItems, newVisibleBricks));
        items.clear();
        items.addAll(bricks);
        dataHasChanged();
        diffResult.dispatchUpdatesTo(brickRecyclerAdapter);
    }

    /**
     * Get all bricks.
     *
     * @return LinkedList of all bricks.
     */
    public LinkedList<BaseBrick> getDataManagerItems() {
        return items;
    }

    /**
     * Replace current items with new {@link Collection} of bricks.
     *
     * @param items new bricks to be added.
     */
    public void setItems(Collection<? extends BaseBrick> items) {
        clear();

        this.items = new LinkedList<>(items);
        dataHasChanged();

        if (brickRecyclerAdapter != null) {
            computePaddingPositionSafelyForFirstItem();
            int itemCount = getRecyclerViewItems().size();
            brickRecyclerAdapter.safeNotifyItemRangeInserted(0, itemCount);
        }
    }

    /**
     * Adds a brick with a layout ID to the cache.
     *
     * @param item the Brick
     */
    private void addToIdCache(@NonNull BaseBrick item) {
        synchronized (this.idCache) {
            if (this.idCache.get(item.getLayout()) != null) {
                this.idCache.get(item.getLayout()).add(item);
            } else {
                LinkedList<BaseBrick> list = new LinkedList<>();
                list.add(item);
                this.idCache.put(item.getLayout(), list);
            }
        }
    }

    /**
     * Adds a brick with a tag to the cache.
     *
     * @param item the Brick
     */
    public void addToTagCache(BaseBrick item) {
        synchronized (this.tagCache) {
            if (item.getTag() != null) {
                if (this.tagCache.containsKey(item.getTag())) {
                    this.tagCache.get(item.getTag()).add(item);
                } else {
                    LinkedList<BaseBrick> list = new LinkedList<>();
                    list.add(item);
                    this.tagCache.put(item.getTag(), list);
                }
            }
        }
    }

    /**
     * Removes a brick with a layout ID from the cache.
     *
     * @param item the Brick
     */
    private void removeFromIdCache(@NonNull BaseBrick item) {
        synchronized (this.idCache) {
            if (this.idCache.get(item.getLayout()) != null) {
                LinkedList<BaseBrick> list = this.idCache.get(item.getLayout());
                list.remove(item);

                if (list.size() == 0) {
                    this.idCache.remove(item.getLayout());
                }
            }
        }
    }

    /**
     * Removes a brick with a tag from the cache.
     *
     * @param item the Brick
     */
    public void removeFromTagCache(@NonNull BaseBrick item) {
        synchronized (this.tagCache) {
            if (item.getTag() != null && this.tagCache.containsKey(item.getTag())) {
                LinkedList<BaseBrick> list = this.tagCache.get(item.getTag());
                list.remove(item);

                if (list.size() == 0) {
                    this.tagCache.remove(item.getTag());
                }
            }
        }
    }

    /**
     * Inserts brick after all other bricks.
     *
     * @param item the brick to add
     */
    public void addLast(@Nullable BaseBrick item) {
        if (null == item) {
            return; // safety
        }

        this.items.addLast(item);
        addToIdCache(item);
        addToTagCache(item);
        item.setDataManager(this);

        if (!item.isHidden()) {
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                int refreshStartIndex = computePaddingPosition(item);
                refreshStartIndex = getPaddingPositionOrDefault(refreshStartIndex);

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
            return; // safety
        }

        this.items.addFirst(item);
        addToIdCache(item);
        addToTagCache(item);
        item.setDataManager(this);

        if (!item.isHidden()) {
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                computePaddingPosition(item);
                brickRecyclerAdapter.safeNotifyItemInserted(0);

                int itemCount = getRecyclerViewItems().size() - 1;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(1, itemCount);
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
            return; // safety
        }

        int index = getRecyclerViewItems().size();
        this.items.addAll(items);

        for (BaseBrick item : items) {
            addToIdCache(item);
            addToTagCache(item);
            item.setDataManager(this);
        }

        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            dataHasChanged();

            LinkedList<BaseBrick> brickItems = getRecyclerViewItems();
            boolean isTheIndexWithinTheCollectionBounds =
                    CollectionUtil.isTheIndexWithinTheCollectionsBounds(index, brickItems);

            if (brickRecyclerAdapter != null && isTheIndexWithinTheCollectionBounds) {
                BaseBrick item = brickItems.get(index);

                int refreshStartIndex = getPaddingPositionOrDefault(computePaddingPosition(item));
                brickRecyclerAdapter.safeNotifyItemRangeInserted(index, visibleCount);

                int itemCount = brickItems.size() - visibleCount - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    int getPaddingPositionOrDefault(int paddingPosition) {
        return NO_PADDING_POSITION == paddingPosition
                ? DEFAULT_BRICK_POSITION
                : paddingPosition;
    }


    /**
     * Inserts collection of bricks before all other bricks.
     *
     * @param items the bricks to add
     */
    public void addFirst(@Nullable Collection<? extends BaseBrick> items) {
        if (null == items) {
            return; // safety
        }

        this.items.addAll(0, items);
        for (BaseBrick item : items) {
            addToIdCache(item);
            addToTagCache(item);
            item.setDataManager(this);
        }

        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                computePaddingPositionSafelyForFirstItem();
                brickRecyclerAdapter.safeNotifyItemRangeInserted(0, visibleCount);

                int itemCount = getRecyclerViewItems().size() - visibleCount;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(visibleCount, itemCount);
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
     * Return all the bricks that have the tag.
     *
     * @param tag The tag we are looking for
     * @return A list of all the bricks that have the same tag
     */
    public List<BaseBrick> getBricksByTag(@NonNull Object tag) {
        return tagCache.get(tag) == null ? null : (List<BaseBrick>) tagCache.get(tag).clone();
    }

    /**
     * Return all the bricks that have the layout.
     *
     * @param layoutId The layout id we are looking for
     * @return A list of all the bricks that have the same layout
     */
    public List<BaseBrick> getBricksByLayoutId(@LayoutRes int layoutId) {
        return idCache.get(layoutId) == null ? null : (List<BaseBrick>) idCache.get(layoutId).clone();
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
            items.addFirst(item);
        } else {
            items.add(anchorDataManagerIndex, item);
        }

        addToIdCache(item);
        addToTagCache(item);
        item.setDataManager(this);

        if (!item.isHidden()) {
            dataHasChanged();
            if (brickRecyclerAdapter != null) {
                int refreshStartIndex = getPaddingPositionOrDefault(computePaddingPosition(item));

                safeNotifyItemInserted(item);

                int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void safeNotifyItemInserted(@Nullable BaseBrick item) {
        int adapterIndex = adapterIndex(item);
        if (NO_ADAPTER_INDEX != adapterIndex) {
            brickRecyclerAdapter.safeNotifyItemInserted(adapterIndex);
        }
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void safeNotifyItemRangeInserted(@Nullable BaseBrick item, int visibleCount) {
        int adapterIndex = adapterIndex(item);
        if (NO_ADAPTER_INDEX != adapterIndex) {
            brickRecyclerAdapter.safeNotifyItemRangeInserted(adapterIndex, visibleCount);
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

        if (index == NO_ADAPTER_INDEX) {
            index = 0;
            this.items.addAll(index, items);
        } else {
            this.items.addAll(index, items);
        }
        for (BaseBrick item : items) {
            addToIdCache(item);
            addToTagCache(item);
            item.setDataManager(this);
        }

        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            BaseBrick firstVisibleItem = getFirstVisibleItem(items);
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                int refreshStartIndex = computePaddingPosition(firstVisibleItem);
                refreshStartIndex = getPaddingPositionOrDefault(refreshStartIndex);
                safeNotifyItemRangeInserted(firstVisibleItem, visibleCount);
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
            this.items.addLast(item);
        } else {
            this.items.add( anchorDataManagerIndex + 1, item);
        }

        addToIdCache(item);
        addToTagCache(item);
        item.setDataManager(this);

        if (!item.isHidden()) {
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                int refreshStartIndex = computePaddingPosition(item);
                refreshStartIndex = getPaddingPositionOrDefault(refreshStartIndex);
                safeNotifyItemInserted(item);
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
    public void addAfterItem(BaseBrick anchor, Collection<? extends BaseBrick> items) {
        int index = this.items.indexOf(anchor);

        if (index == -1) {
            index = getRecyclerViewItems().size();
            this.items.addAll(index, items);
        } else {
            index++;
            this.items.addAll(index, items);
        }
        for (BaseBrick item : items) {
            addToIdCache(item);
            addToTagCache(item);
            item.setDataManager(this);
        }

        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            BaseBrick firstVisibleItem = getFirstVisibleItem(items);
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                int refreshStartIndex = computePaddingPosition(firstVisibleItem);
                refreshStartIndex = getPaddingPositionOrDefault(refreshStartIndex);
                safeNotifyItemRangeInserted(firstVisibleItem, visibleCount);
                int itemCount = getRecyclerViewItems().size() - visibleCount - refreshStartIndex;
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

        removeFromIdCache(item);
        removeFromTagCache(item);
        item.setDataManager(null);

        if (!item.isHidden()) {
            int index = adapterIndex(item);
            dataHasChanged();

            if (brickRecyclerAdapter != null) {
                brickRecyclerAdapter.safeNotifyItemRemoved(index);
                LinkedList<BaseBrick> items = getRecyclerViewItems();

                if (CollectionUtil.isTheIndexWithinTheCollectionsBounds(index, items)) {
                    int refreshStartIndex = computePaddingPosition(items.get(index));
                    refreshStartIndex = getPaddingPositionOrDefault(refreshStartIndex);
                    int itemCount = items.size() - refreshStartIndex;
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
                }
            }
        }
    }

    /**
     * Moves an item from one location to the location of the other.
     *
     * @param fromBrick The brick to move
     * @param toBrick   The brick to move fromBrick to
     */
    public void moveItem(BaseBrick fromBrick, BaseBrick toBrick) {
        int fromPosition = this.items.indexOf(fromBrick);
        int toPosition = this.items.indexOf(toBrick);
        int startPosition = Math.min(fromPosition, toPosition);

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(this.items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(this.items, i, i - 1);
            }
        }

        dataHasChanged();

        if (brickRecyclerAdapter != null) {
            brickRecyclerAdapter.safeNotifyItemMoved(fromPosition, toPosition);
            int refreshStartIndex = computePaddingPosition(getRecyclerViewItems().get(startPosition));
            refreshStartIndex = getPaddingPositionOrDefault(refreshStartIndex);
            int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
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
            removeFromIdCache(item);
            removeFromTagCache(item);
            item.setDataManager(null);
        }
        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            dataHasChanged();
            if (brickRecyclerAdapter != null) {
                if (getRecyclerViewItems().size() > 0) {
                    computePaddingPosition(getRecyclerViewItems().getFirst());
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
        this.idCache.clear();
        this.tagCache.clear();
        dataHasChanged();
        if (brickRecyclerAdapter != null) {
            brickRecyclerAdapter.safeNotifyItemRangeRemoved(0, startCount);
        }
    }

    /**
     * Replace a target brick with a replacement.
     *
     * @param target      brick to replace
     * @param replacement the brick being added
     */
    public synchronized void replaceItem(BaseBrick target, BaseBrick replacement) {
        int index = adapterIndex(target);
        if ((index == -1) == replacement.isHidden()) {
            if (!target.isHidden()) {
                int dataIndex = items.indexOf(target);
                BaseBrick brickToRemove = items.get(dataIndex);
                items.remove(brickToRemove);
                removeFromIdCache(brickToRemove);
                removeFromTagCache(brickToRemove);
                brickToRemove.setDataManager(null);
                items.add(dataIndex, replacement);
                addToIdCache(replacement);
                addToTagCache(replacement);
                replacement.setDataManager(this);
                dataHasChanged();
                if (brickRecyclerAdapter != null) {
                    int refreshStartIndex = computePaddingPosition(replacement);
                    refreshStartIndex = getPaddingPositionOrDefault(refreshStartIndex);
                    brickRecyclerAdapter.safeNotifyItemChanged(index);
                    int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
                }
            }
        } else {
            if (replacement.isHidden()) {
                int dataIndex = items.indexOf(target);
                BaseBrick brickToRemove = items.get(dataIndex);
                items.remove(brickToRemove);
                removeFromIdCache(brickToRemove);
                removeFromTagCache(brickToRemove);
                brickToRemove.setDataManager(null);
                items.add(dataIndex, replacement);
                addToIdCache(replacement);
                addToTagCache(replacement);
                replacement.setDataManager(this);
                dataHasChanged();
                if (brickRecyclerAdapter != null) {
                    int refreshStartIndex = computePaddingPosition(target);
                    refreshStartIndex = getPaddingPositionOrDefault(refreshStartIndex);
                    brickRecyclerAdapter.safeNotifyItemRemoved(index);
                    int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
                }
            } else {
                int dataIndex = items.indexOf(target);
                if (dataIndex != -1) { // A double-tap can cause this
                    BaseBrick brickToRemove = items.get(dataIndex);
                    items.remove(brickToRemove);
                    removeFromIdCache(brickToRemove);
                    removeFromTagCache(brickToRemove);
                    brickToRemove.setDataManager(null);
                    items.add(dataIndex, replacement);
                    addToIdCache(replacement);
                    addToTagCache(replacement);
                    replacement.setDataManager(this);
                    dataHasChanged();
                    if (brickRecyclerAdapter != null) {
                        int adapterIndex = adapterIndex(replacement);
                        int refreshStartIndex = computePaddingPosition(target);
                        refreshStartIndex = getPaddingPositionOrDefault(refreshStartIndex);
                        brickRecyclerAdapter.safeNotifyItemInserted(adapterIndex);
                        int itemCount = getRecyclerViewItems().size() - refreshStartIndex;
                        brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount);
                    }
                }
            }
        }
    }

    /**
     * Refresh a brick.
     *
     * @param item the brick to refresh
     */
    public void refreshItem(BaseBrick item) {
        int index = adapterIndex(item);
        if (items.indexOf(item) != -1 && index != -1 && !item.isHidden()) {
            if (brickRecyclerAdapter != null) {
                brickRecyclerAdapter.safeNotifyItemChanged(index);
            }
        }
    }

    /**
     * Update the visibility of the brick and set it invisible.
     *
     * @param item the brick to be hided
     */
    public void hideItem(BaseBrick item) {
        item.setHidden(true);
        int index = adapterIndex(item);
        if (items.indexOf(item) != -1 && index != -1) {
            dataHasChanged();
            if (brickRecyclerAdapter != null) {
                brickRecyclerAdapter.safeNotifyItemRemoved(index);
                if (index < getRecyclerViewItems().size()) {
                    int refreshStartIndex = computePaddingPosition(getRecyclerViewItems().get(index));
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
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
        if (items.indexOf(item) != -1 && adapterIndex(item) == -1) {
            dataHasChanged();
            if (brickRecyclerAdapter != null) {
                int index = adapterIndex(item);
                int refreshStartIndex = computePaddingPosition(item);
                brickRecyclerAdapter.safeNotifyItemInserted(index);
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
            }
        }
    }

    /**
     * Helper method to tell manager to update the items returned from getRecyclerViewItems().
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void dataHasChanged() {
        dataHasChanged = true;
        for (BrickBehavior behavior : behaviors) {
            behavior.onDataSetChanged();
        }
    }

    /**
     * Method to get the index of the item in the visible items.
     *
     * @param item item to get the index of
     * @return index of the item in the visible items or {@link #NO_ADAPTER_INDEX}.
     */
    private int adapterIndex(@Nullable BaseBrick item) {
        return getRecyclerViewItems().indexOf(item);
    }

    /**
     * Removes all instances of a given class.
     *
     * @param clazz class to remove all instances of
     */
    public void removeAll(Class clazz) {
        ArrayList<BaseBrick> itemToRemove = new ArrayList<>();

        for (BaseBrick item : this.items) {
            if (clazz.isInstance(item)) {
                itemToRemove.add(item);
            }
        }

        removeItems(itemToRemove);
    }

    /**
     * Removes all instances of a given tag.
     *
     * @param tag tag to remove all instances of
     */
    public void removeAllByTag(@NonNull Object tag) {
        removeItems(getBricksByTag(tag));
    }

    /**
     * Removes all instances of a given layout Id.
     *
     * @param layoutId layout to remove all instances of
     */
    public void removeAllByLayoutId(@LayoutRes int layoutId) {
        removeItems(getBricksByLayoutId(layoutId));
    }

    /**
     * If all items have instance of clazz.
     *
     * @param clazz class to be found
     * @return whether the clazz is in the all items
     */
    public boolean hasInstanceOf(Class clazz) {
        for (BaseBrick item : this.items) {
            if (clazz.isInstance(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Smooth scroll to the given brick.
     *
     * @param item brick to smooth scroll to
     */
    public void smoothScrollToBrick(BaseBrick item) {
        if (getBrickRecyclerAdapter() != null) {
            int index = getBrickRecyclerAdapter().indexOf(item);
            if (index != -1) {
                recyclerView.smoothScrollToPosition(index);
            }
        }
    }

    /**
     * Safely checks / Determines if the brick is on the left wall, first row, right wall, last row.
     * If a {@link NoSuchElementException} is thrown when retrieving the first brick, it is caught.
     *
     * @return index of first modified, non-null item or {@link #NO_PADDING_POSITION} for a null
     * brick.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @SuppressWarnings("UnusedReturnValue")
    int computePaddingPositionSafelyForFirstItem() {
        LinkedList<BaseBrick> items = getRecyclerViewItems();
        BaseBrick firstBrick = LinkedListUtil.getFirstItemQuietly(items);
        return computePaddingPosition(firstBrick);
    }

    /**
     * Checks / Determines if the brick is on the left wall, first row, right wall, last row.
     *
     * @param currentBrick BaseBrick item that was changed / added / removed
     * @return index of first modified, non-null item or {@link #NO_PADDING_POSITION} for a null
     * brick.
     */
    private int computePaddingPosition(@Nullable BaseBrick currentBrick) {
        if (null == currentBrick) {
            // When the brick is null, return the default position value to avoid potential issues
            // with consuming code.
            return NO_PADDING_POSITION;
        }

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

        if (currentRow == maxSpanCount) {
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
            currentBrick = iterator.next();
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

            if (currentRow > maxSpanCount) {
                currentRow = 0;
            }

            currentRow += currentBrick.getSpanSize().getSpans(context);

            if (currentRow > maxSpanCount) {
                if (vertical) {
                    currentBrick.setOnLeftWall(true);
                } else {
                    currentBrick.setInFirstRow(true);
                }
                currentRow = currentBrick.getSpanSize().getSpans(context);
                topRow = false;
            }

            if (currentRow == maxSpanCount) {
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
     * Get the max span count.
     *
     * @return the max span count
     */
    public int getMaxSpanCount() {
        return maxSpanCount;
    }

    /**
     * Get the collection of behaviours currently in the BrickDataManager. Mostly for testing.
     *
     * @return The current behaviours attached tot he BrickDataManger
     */
    public Collection<BrickBehavior> getBehaviours() {
        return behaviors;
    }

    /**
     * Add a {@link BrickBehavior}.
     *
     * @param behavior {@link BrickBehavior} to add
     */
    public void addBehavior(BrickBehavior behavior) {
        for (BrickBehavior brickBehavior : behaviors) {
            if (brickBehavior.getClass().isInstance(behavior.getClass().getName())) {
                return;
            }
        }

        behaviors.add(behavior);
        behavior.attachToRecyclerView(getRecyclerView());
    }

    /**
     * Remove a {@link BrickBehavior}.
     *
     * @param behavior {@link BrickBehavior} to be removed
     */
    public void removeBehavior(BrickBehavior behavior) {
        behaviors.remove(behavior);
        behavior.detachFromRecyclerView(getRecyclerView());
    }

    /**
     * Get the {@link BrickRecyclerAdapter} for this instance.
     *
     * @return the {@link BrickRecyclerAdapter}.
     */
    public BrickRecyclerAdapter getBrickRecyclerAdapter() {
        return brickRecyclerAdapter;
    }

    /**
     * Method called to release any related resources.
     */
    public void onDestroyView() {
        for (BrickBehavior behavior : behaviors) {
            behavior.detachFromRecyclerView(getRecyclerView());
        }
        behaviors.clear();
        items.clear();
        idCache.clear();
        tagCache.clear();
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        brickRecyclerAdapter = null;
        recyclerViewParent = null;
        recyclerView = null;
        context = null;
    }

    /**
     * Retrieves a brick who's associated layout resource ID matches that of the parameter.
     *
     * @param layoutResId Layout resource ID
     * @return An instance of BaseBrick or null
     */
    @Nullable
    public BaseBrick brickWithLayout(@LayoutRes int layoutResId) {
        List<BaseBrick> visibleItems = getRecyclerViewItems();
        for (int i = 0; i < visibleItems.size(); i++) {
            if (visibleItems.get(i).getLayout() == layoutResId) {
                return visibleItems.get(i);
            }
        }

        return null;
    }

    /**
     * Retrieves a brick who's associated placeholder layout resource ID matches that of the parameter.
     *
     * @param placeholderLayoutResId Placeholder Layout resource ID
     * @return An instance of BaseBrick or null
     */
    @Nullable
    public BaseBrick brickWithPlaceholderLayout(@LayoutRes int placeholderLayoutResId) {
        List<BaseBrick> visibleItems = getRecyclerViewItems();
        for (int i = 0; i < visibleItems.size(); i++) {
            if (!visibleItems.get(i).isDataReady() && visibleItems.get(i).getPlaceholderLayout()
                    == placeholderLayoutResId) {
                return visibleItems.get(i);
            }
        }

        return null;
    }

    /**
     * Retrieves a brick at a specific position.
     *
     * @param position Position of the brick within the data set
     * @return An instance of BaseBrick or null
     */
    public BaseBrick brickAtPosition(int position) {
        if (position >= 0 && position < getRecyclerViewItems().size()) {
            return getRecyclerViewItems().get(position);
        }
        return null;
    }

    /**
     * Non-Predictive Animations {@link StaggeredGridLayoutManager}.
     */
    private static class NpaStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

        /**
         * Constructor for the NpaStaggeredGridLayoutManager.
         *
         * @param spanCount   the number of columns to use in the StaggeredGridLayoutManager.
         * @param orientation the orientation of the StaggeredGridLayoutManager.
         */
        NpaStaggeredGridLayoutManager(int spanCount, int orientation) {
            super(spanCount, orientation);
        }

        /**
         * There is a bug in recyclerviews (March 2017) which causes them to load animations for views which don't yet exist.
         * For us this is a race condition, it currently only occurs on the infinite brick pages.
         *
         * @return false
         */
        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
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

    /**
     * Exception is thrown when dataSet gets updated synchronously at different thread {@link GridLayoutManager}.
     */
    private static class WFGridLayoutManager extends GridLayoutManager {

        /**
         * Constructor for the WFGridLayoutManager.
         *
         * @param context       {@link Context} to use
         * @param spanCount     the number of columns to which the bricks will conform
         * @param orientation   Layout orientation. Should be {@link GridLayoutManager#HORIZONTAL} or {@link GridLayoutManager#VERTICAL}.
         * @param reverseLayout When set to true, layouts from end to start.
         */
        WFGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
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
