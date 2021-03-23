/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

import android.widget.GridLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wayfair.brickkit.animator.AvoidFlickerItemAnimator
import com.wayfair.brickkit.brick.BaseBrick
import java.io.Serializable

/**
 * Class which maintains a collection of bricks and manages how they are laid out in an provided RecyclerView.
 *
 * This class maintains the bricks and handles notifying the underlying adapter when items are updated.
 */
open class BrickDataManager : Serializable {
    private var brickRecyclerAdapter: BrickRecyclerAdapter? = null
    private val currentlyVisibleItems = mutableListOf<BaseBrick>()
    private val currentItems = mutableListOf<BaseBrick>()
    private var dataHasChanged = false
    private var vertical = false
    private var dataSetChangedListener: DataSetChangedListener? = null
    private var recyclerView: RecyclerView? = null

    /**
     * Get all bricks.
     *
     * @return List of all bricks.
     */
    open val dataManagerItems: List<BaseBrick> = currentItems

    /**
     *
     * @return true if no items have been added to the BrickDataManager
     */
    open val isEmpty: Boolean
        get() = currentItems.isEmpty()

    /**
     * Get the items visible in the [RecyclerView].
     *
     * @return List of visible bricks.
     */
    open val recyclerViewItems: List<BaseBrick>
        get() {
            if (dataHasChanged) {
                currentlyVisibleItems.clear()
                currentlyVisibleItems.addAll(currentItems.filter { item -> !item.isHidden })
                dataHasChanged = false
            }
            return currentlyVisibleItems
        }

    /**
     * Set the recycler view for this BrickDataManager, this will setup the underlying adapter and begin
     * displaying any bricks Use setHorizontalRecyclerView instead if you want a horizontal RecyclerView.
     *
     * @param recyclerView [RecyclerView] to put views in
     */
    open fun setRecyclerView(recyclerView: RecyclerView) {
        setRecyclerView(recyclerView, RecyclerView.VERTICAL)
    }

    /**
     * Set the recycler view for this BrickDataManager for a horizontal RecyclerView, this will setup the
     * underlying adapter and begin displaying any bricks.
     *
     * @param recyclerView [RecyclerView] to put views in
     */
    open fun setHorizontalRecyclerView(recyclerView: RecyclerView) {
        setRecyclerView(recyclerView, RecyclerView.HORIZONTAL)
    }

    /**
     * Get the recycler view if available.
     *
     * @return the attached recycler view, null if none has been attached
     */
    open fun getRecyclerView(): RecyclerView? = recyclerView

    /**
     * Allows the user to set a listener that will get called whenever the data set changes.
     *
     * @param listener listener which will be notified whenever the data set changes
     */
    open fun setDataSetChangedListener(listener: DataSetChangedListener?) {
        dataSetChangedListener = listener
    }

    /**
     * Set an [OnReachedItemAtPosition].
     *
     * @param onReachedItemAtPosition [OnReachedItemAtPosition] to set
     */
    open fun setOnReachedItemAtPosition(onReachedItemAtPosition: OnReachedItemAtPosition?) {
        brickRecyclerAdapter?.setOnReachedItemAtPosition(onReachedItemAtPosition)
    }

    /**
     * Use DiffUtils to update the recycler view.
     *
     * @param bricks the new list of bricks.
     */
    open fun updateBricks(bricks: List<BaseBrick>) {
        updateBricks(bricks, BrickDiffUtilCallback(currentlyVisibleItems, bricks.filter { brick -> !brick.isHidden }))
    }

    /**
     * Use DiffUtils to update the recycler view.
     *
     * @param bricks the new list of bricks.
     * @param diffUtilCallback [DiffUtil.Callback] to use
     */
    open fun updateBricks(bricks: List<BaseBrick>, diffUtilCallback: DiffUtil.Callback) {
        currentItems.forEach { item -> item.setDataManager(null) }
        currentItems.clear()

        bricks.forEach { item -> item.setDataManager(this) }
        currentItems.addAll(bricks)

        dataHasChanged()
        brickRecyclerAdapter?.let {
            DiffUtil.calculateDiff(diffUtilCallback).dispatchUpdatesTo(
                SafeAdapterListUpdateCallback(it)
            )
        }
    }

    /**
     * Inserts brick after all other bricks.
     *
     * @param item the brick to add
     */
    open fun addLast(item: BaseBrick?) {
        if (null == item) {
            return
        }
        currentItems.add(item)
        item.setDataManager(this)

        if (!item.isHidden) {
            dataHasChanged()
            brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                val refreshStartIndex = computePaddingPosition(item)
                val itemCount = recyclerViewItems.size
                val position = itemCount - 1
                brickRecyclerAdapter.safeNotifyItemInserted(position)
                val newItemCount = itemCount - 1 - refreshStartIndex
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, newItemCount)
            }
        }
    }

    /**
     * Inserts brick before all other bricks.
     *
     * @param item the brick to add
     */
    open fun addFirst(item: BaseBrick?) {
        if (null == item) {
            return
        }
        currentItems.add(0, item)
        item.setDataManager(this)

        if (!item.isHidden) {
            dataHasChanged()
            brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                computePaddingPosition(item)
                brickRecyclerAdapter.safeNotifyItemInserted(0)
                val refreshStartIndex = 1
                val itemCount = recyclerViewItems.size - refreshStartIndex
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount)
            }
        }
    }

    /**
     * Inserts collection of bricks after all other bricks.
     *
     * @param items the bricks to add
     */
    open fun addLast(items: Collection<BaseBrick>?) {
        if (null == items) {
            return
        }
        val initialSize = recyclerViewItems.size
        currentItems.addAll(items)
        items.forEach { item -> item.setDataManager(this) }
        val visibleCount = items.count { brick -> !brick.isHidden }
        if (visibleCount > 0) {
            dataHasChanged()
            brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                val item = recyclerViewItems[initialSize]
                val refreshStartIndex = computePaddingPosition(item)
                brickRecyclerAdapter.safeNotifyItemRangeInserted(initialSize, visibleCount)
                val itemCount = recyclerViewItems.size - visibleCount - refreshStartIndex
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount)
            }
        }
    }

    /**
     * Inserts brick before the anchor brick.
     *
     * @param anchor brick to insert before
     * @param item   the brick to add
     */
    open fun addBeforeItem(anchor: BaseBrick, item: BaseBrick) {
        val anchorDataManagerIndex = currentItems.indexOf(anchor)
        if (anchorDataManagerIndex == -1) {
            currentItems.add(0, item)
        } else {
            currentItems.add(anchorDataManagerIndex, item)
        }
        item.setDataManager(this)
        if (!item.isHidden) {
            dataHasChanged()
            brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                val refreshStartIndex = computePaddingPosition(item)
                val adapterIndex = recyclerViewItems.indexOf(item)
                brickRecyclerAdapter.safeNotifyItemInserted(adapterIndex)
                val itemCount = recyclerViewItems.size - refreshStartIndex
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount)
            }
        }
    }

    /**
     * Inserts brick before the anchor brick.
     *
     * @param anchor brick to insert before
     * @param items  the bricks to add
     */
    open fun addBeforeItem(anchor: BaseBrick, items: Collection<BaseBrick>) {
        var index = currentItems.indexOf(anchor)
        if (index == -1) {
            index = 0
        }
        currentItems.addAll(index, items)
        items.forEach { item -> item.setDataManager(this) }
        val visibleNewItems = items.filter { brick -> !brick.isHidden }
        if (visibleNewItems.isNotEmpty()) {
            dataHasChanged()
            brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                val firstVisibleItem = visibleNewItems.first()
                val refreshStartIndex = computePaddingPosition(firstVisibleItem)
                val adapterIndex = recyclerViewItems.indexOf(firstVisibleItem)
                brickRecyclerAdapter.safeNotifyItemRangeInserted(adapterIndex, visibleNewItems.size)
                val itemCount = recyclerViewItems.size - visibleNewItems.size - refreshStartIndex
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount)
            }
        }
    }

    /**
     * Inserts brick after the anchor brick.
     *
     * @param anchor brick to insert after
     * @param item   the brick to add
     */
    open fun addAfterItem(anchor: BaseBrick, item: BaseBrick) {
        val anchorDataManagerIndex = currentItems.indexOf(anchor)
        if (anchorDataManagerIndex == -1) {
            currentItems.add(item)
        } else {
            currentItems.add(anchorDataManagerIndex + 1, item)
        }
        item.setDataManager(this)
        if (!item.isHidden) {
            dataHasChanged()
            brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                val refreshStartIndex = computePaddingPosition(item)
                val adapterIndex = recyclerViewItems.indexOf(item)
                brickRecyclerAdapter.safeNotifyItemInserted(adapterIndex)
                val itemCount = recyclerViewItems.size - refreshStartIndex
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount)
            }
        }
    }

    /**
     * Remove a brick.
     *
     * @param item the brick to remove
     */
    open fun removeItem(item: BaseBrick?) {
        if (null == item) {
            return
        }
        currentItems.remove(item)
        item.setDataManager(null)
        if (!item.isHidden && recyclerViewItems.contains(item)) {
            val index = recyclerViewItems.indexOf(item)
            dataHasChanged()
            brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                brickRecyclerAdapter.safeNotifyItemRemoved(index)
                if (index < recyclerViewItems.size) {
                    val refreshStartIndex = computePaddingPosition(recyclerViewItems[index])
                    val itemCount = recyclerViewItems.size - refreshStartIndex
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount)
                }
            }
        }
    }

    /**
     * Remove a collection of bricks.
     *
     * @param items the bricks to remove
     */
    open fun removeItems(items: Collection<BaseBrick>) {
        currentItems.removeAll(items)
        items.forEach { item -> item.setDataManager(null) }
        if (items.any { brick -> !brick.isHidden }) {
            dataHasChanged()
            brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                if (recyclerViewItems.isNotEmpty()) {
                    computePaddingPosition(recyclerViewItems[0])
                }
                brickRecyclerAdapter.safeNotifyDataSetChanged()
            }
        }
    }

    /**
     * Remove all bricks.
     */
    open fun clear() {
        val startCount = recyclerViewItems.size
        currentItems.clear()
        dataHasChanged()
        brickRecyclerAdapter?.safeNotifyItemRangeRemoved(0, startCount)
    }

    /**
     * Replace a target brick with a replacement brick.
     *
     * @param target      brick to replace
     * @param replacement the brick being added
     */
    @Synchronized
    open fun replaceItem(target: BaseBrick, replacement: BaseBrick) {
        val targetIndexInItems = currentItems.indexOf(target)
        if (targetIndexInItems == -1) {
            return
        }
        val targetIndexInAdapter = recyclerViewItems.indexOf(target)
        val indexNotFound = -1 == targetIndexInAdapter
        if (indexNotFound == replacement.isHidden) {
            if (!target.isHidden) {
                val brickToRemove = currentItems[targetIndexInItems]
                currentItems.remove(brickToRemove)
                brickToRemove.setDataManager(null)

                currentItems.add(targetIndexInItems, replacement)
                replacement.setDataManager(this)
                dataHasChanged()

                brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                    val refreshStartIndex = computePaddingPosition(replacement)
                    brickRecyclerAdapter.safeNotifyItemChanged(targetIndexInAdapter)
                    val itemCount = recyclerViewItems.size - refreshStartIndex
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount)
                }
            }
        } else {
            val brickToRemove = currentItems[targetIndexInItems]
            currentItems.remove(brickToRemove)
            brickToRemove.setDataManager(null)

            currentItems.add(targetIndexInItems, replacement)
            replacement.setDataManager(this)
            dataHasChanged()

            brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                val refreshStartIndex = computePaddingPosition(target)
                if (replacement.isHidden) {
                    brickRecyclerAdapter.safeNotifyItemRemoved(targetIndexInAdapter)
                    val itemCount = recyclerViewItems.size - refreshStartIndex
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount)
                } else {
                    val adapterIndex = recyclerViewItems.indexOf(replacement)
                    brickRecyclerAdapter.safeNotifyItemInserted(adapterIndex)
                    val itemCount = recyclerViewItems.size - refreshStartIndex
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount)
                }
            }
        }
    }

    /**
     * Method called to release any related resources.
     */
    open fun onDestroyView() {
        recyclerView?.adapter = null
        brickRecyclerAdapter = null
        recyclerView = null
    }

    /**
     * Refresh a brick.
     *
     * @param item the brick to refresh
     */
    internal open fun refreshItem(item: BaseBrick) {
        if (currentItems.contains(item) && recyclerViewItems.contains(item)) {
            brickRecyclerAdapter?.safeNotifyItemChanged(recyclerViewItems.indexOf(item))
        }
    }

    /**
     * Update the visibility of the brick and set it invisible.
     *
     * @param item the brick to be hided
     */
    internal open fun hideItem(item: BaseBrick) {
        if (currentItems.contains(item) && recyclerViewItems.contains(item)) {
            val index = recyclerViewItems.indexOf(item)
            dataHasChanged()
            brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                brickRecyclerAdapter.safeNotifyItemRemoved(index)
                if (index < recyclerViewItems.size) {
                    val refreshStartIndex = computePaddingPosition(recyclerViewItems[index])
                    val itemCount = recyclerViewItems.size - refreshStartIndex
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount)
                }
            }
        }
    }

    /**
     * Update the visibility of the brick and set it visible.
     *
     * @param item the brick to be showed
     */
    internal open fun showItem(item: BaseBrick) {
        if (currentItems.contains(item) && !recyclerViewItems.contains(item)) {
            dataHasChanged()
            brickRecyclerAdapter?.let { brickRecyclerAdapter ->
                val index = recyclerViewItems.indexOf(item)
                val refreshStartIndex = computePaddingPosition(item)
                brickRecyclerAdapter.safeNotifyItemInserted(index)
                val itemCount = recyclerViewItems.size - refreshStartIndex
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, itemCount)
            }
        }
    }

    /**
     * Smooth scroll to the given brick.
     *
     * @param item brick to smooth scroll to
     */
    internal open fun smoothScrollToBrick(item: BaseBrick?) {
        val index = recyclerViewItems.indexOf(item)
        if (index != -1) {
            recyclerView?.smoothScrollToPosition(index)
        }
    }

    private fun setRecyclerView(recyclerView: RecyclerView, orientation: Int) {
        brickRecyclerAdapter = BrickRecyclerAdapter(this, recyclerView)
        vertical = orientation == GridLayout.VERTICAL

        this.recyclerView = recyclerView.apply {
            adapter = brickRecyclerAdapter
            while (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }
            addItemDecoration(BrickRecyclerItemDecoration(this@BrickDataManager))
            itemAnimator = AvoidFlickerItemAnimator()
            layoutManager = object : GridLayoutManager(context, SPAN_COUNT, orientation, false) {
                @Suppress("TooGenericExceptionCaught")
                override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                    try {
                        super.onLayoutChildren(recycler, state)
                    } catch (e: IndexOutOfBoundsException) {
                        // Exception is thrown when dataSet gets updated synchronously at different thread {@link GridLayoutManager}
                    }
                }
            }.apply { spanSizeLookup = BrickSpanSizeLookup(recyclerView.context, this@BrickDataManager) }
        }

        if (recyclerViewItems.isNotEmpty()) {
            computePaddingPosition(recyclerViewItems[0])
            brickRecyclerAdapter?.notifyItemRangeInserted(0, recyclerViewItems.size)
        }
    }

    /**
     * Helper method to tell manager to update the items returned from getRecyclerViewItems().
     */
    private fun dataHasChanged() {
        dataHasChanged = true
        dataSetChangedListener?.onDataSetChanged()
    }

    /**
     * Checks / Determines if the brick is on the left wall, first row, right wall, last row.
     *
     * @param initialBrick BaseBrick item that was changed / added / removed
     * @return index of first modified, non-null item or 0 for a null
     * brick.
     */
    @Suppress("ComplexMethod", "LongMethod")
    private fun computePaddingPosition(initialBrick: BaseBrick): Int {
        var currentBrick = initialBrick
        val context = recyclerView!!.context

        var currentRow = 0
        var startingBrickIndex = recyclerViewItems.indexOf(currentBrick)
        if (startingBrickIndex < 0) {
            startingBrickIndex = 0
        }
        val iterator = recyclerViewItems.listIterator(startingBrickIndex)
        while (iterator.hasPrevious()) {
            currentBrick = iterator.previous()
            if (vertical && currentBrick.isOnLeftWall) {
                break
            } else if (!vertical && currentBrick.isInFirstRow) {
                break
            }
        }
        var topRow = false
        if (!iterator.hasPrevious()) {
            topRow = true
        }
        if (vertical) {
            currentBrick.isOnLeftWall = true
            currentBrick.isInFirstRow = false
        } else {
            currentBrick.isOnLeftWall = false
            currentBrick.isInFirstRow = true
        }
        currentBrick.isOnRightWall = false
        currentBrick.isInLastRow = false
        currentRow += currentBrick.spanSize.getSpans(context)
        if (currentRow == SPAN_COUNT) {
            if (vertical) {
                currentBrick.isOnRightWall = true
            } else {
                currentBrick.isInLastRow = true
            }
        }
        if (topRow) {
            if (vertical) {
                currentBrick.isInFirstRow = true
            } else {
                currentBrick.isOnLeftWall = true
            }
        }
        if (iterator.hasNext()) {
            iterator.next()
        }
        while (iterator.hasNext()) {
            currentBrick = iterator.next()
            currentBrick.isOnLeftWall = false
            currentBrick.isInFirstRow = false
            currentBrick.isOnRightWall = false
            currentBrick.isInLastRow = false
            if (currentRow == 0) {
                if (vertical) {
                    currentBrick.isOnLeftWall = true
                } else {
                    currentBrick.isInFirstRow = true
                }
                topRow = false
            }
            if (currentRow > SPAN_COUNT) {
                currentRow = 0
            }
            currentRow += currentBrick.spanSize.getSpans(context)
            if (currentRow > SPAN_COUNT) {
                if (vertical) {
                    currentBrick.isOnLeftWall = true
                } else {
                    currentBrick.isInFirstRow = true
                }
                currentRow = currentBrick.spanSize.getSpans(context)
                topRow = false
            }
            if (currentRow == SPAN_COUNT) {
                if (vertical) {
                    currentBrick.isOnRightWall = true
                } else {
                    currentBrick.isInLastRow = true
                }
                currentRow = 0
            }
            if (topRow) {
                if (vertical) {
                    currentBrick.isInFirstRow = true
                } else {
                    currentBrick.isOnLeftWall = true
                }
            }
        }
        addBottomToRowEndingWithItem(iterator)
        return startingBrickIndex
    }

    /**
     * Sets all items to being in the last row from the current brick to the left most brick in that row.
     *
     * @param iterator iterator to use to find items in the row
     */
    private fun addBottomToRowEndingWithItem(iterator: ListIterator<BaseBrick>) {
        while (iterator.hasPrevious()) {
            val currentBrick = iterator.previous()
            if (vertical) {
                currentBrick.isInLastRow = true
                if (currentBrick.isOnLeftWall) {
                    break
                }
            } else {
                currentBrick.isOnRightWall = true
                if (currentBrick.isInFirstRow) {
                    break
                }
            }
        }
    }

    companion object {
        const val SPAN_COUNT = 240
    }
}
