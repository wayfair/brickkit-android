/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wayfair.brickkit.brick.BaseBrick
import com.wayfair.brickkit.viewholder.BrickViewHolder
import com.wayfair.brickkit.viewholder.factory.BrickViewHolderFactory

/**
 * Extension of [androidx.recyclerview.widget.RecyclerView.Adapter] which combines a given
 * [BrickDataManager] to a given [RecyclerView].
 */
@OpenForTesting
internal class BrickRecyclerAdapter(
    val brickDataManager: BrickDataManager,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<BrickViewHolder>() {

    private val handler: Handler = Handler()
    private var onReachedItemAtPosition: OnReachedItemAtPosition? = null

    /**
     * Safe version of [RecyclerView.Adapter.notifyDataSetChanged].
     */
    fun safeNotifyDataSetChanged() {
        runWhenNotComputingLayout { notifyDataSetChanged() }
    }

    /**
     * Safe version of [RecyclerView.Adapter.notifyItemChanged].
     *
     * @param position Position of the item that has changed
     * @param payload Optional parameter, use null to identify a "full" update
     */
    fun safeNotifyItemChanged(position: Int, payload: Any?) {
        if (position >= 0) {
            runWhenNotComputingLayout { notifyItemChanged(position, payload) }
        } else {
            Log.w(TAG, "safeNotifyItemChanged: position is negative")
        }
    }

    /**
     * Safe version of [RecyclerView.Adapter.notifyItemChanged].
     *
     * @param position Position of the item that has changed
     */
    fun safeNotifyItemChanged(position: Int) {
        if (position >= 0) {
            runWhenNotComputingLayout { notifyItemChanged(position) }
        } else {
            Log.w(TAG, "safeNotifyItemChanged: position is negative")
        }
    }

    /**
     * Safe version of [RecyclerView.Adapter.notifyItemInserted].
     *
     * @param position Position of the newly inserted item in the data set
     */
    fun safeNotifyItemInserted(position: Int) {
        if (position >= 0) {
            runWhenNotComputingLayout { notifyItemInserted(position) }
        } else {
            Log.w(TAG, "safeNotifyItemInserted: position is negative")
        }
    }

    /**
     * Safe version of [RecyclerView.Adapter.notifyItemMoved].
     *
     * @param fromPosition Previous position of the item.
     * @param toPosition New position of the item.
     */
    fun safeNotifyItemMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition >= 0 && toPosition >= 0) {
            runWhenNotComputingLayout { notifyItemMoved(fromPosition, toPosition) }
        } else {
            Log.w(TAG, "safeNotifyItemRangeChanged: fromPosition / toPosition is/are negative")
        }
    }

    /**
     * Safe version of [RecyclerView.Adapter.notifyItemRangeChanged].
     *
     * @param positionStart Position of the first item that has changed
     * @param itemCount Number of items that have changed
     * @param payload  Optional parameter, use null to identify a "full" update
     */
    fun safeNotifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        if (positionStart >= 0 && itemCount >= 0) {
            runWhenNotComputingLayout { notifyItemRangeChanged(positionStart, itemCount, payload) }
        } else {
            Log.w(TAG, "safeNotifyItemRangeChanged: positionStart / itemStart is/are negative")
        }
    }

    /**
     * Safe version of [RecyclerView.Adapter.notifyItemRangeChanged].
     *
     * @param positionStart Position of the first item that has changed
     * @param itemCount Number of items that have changed
     */
    fun safeNotifyItemRangeChanged(positionStart: Int, itemCount: Int) {
        safeNotifyItemRangeChanged(positionStart, itemCount, null)
        if (positionStart >= 0 && itemCount >= 0) {
            runWhenNotComputingLayout { notifyItemRangeChanged(positionStart, itemCount) }
        } else {
            Log.w(TAG, "safeNotifyItemRangeChanged: positionStart / itemStart is/are negative")
        }
    }

    /**
     * Safe version of [RecyclerView.Adapter.notifyItemRangeInserted].
     *
     * @param positionStart Position of the first item that was inserted
     * @param itemCount Number of items inserted
     */
    fun safeNotifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        if (positionStart >= 0 && itemCount >= 0) {
            runWhenNotComputingLayout { notifyItemRangeInserted(positionStart, itemCount) }
        } else {
            Log.w(TAG, "safeNotifyItemRangeInserted: positionStart / itemStart is/are negative")
        }
    }

    /**
     * Safe version of [RecyclerView.Adapter.notifyItemRangeRemoved].
     *
     * @param positionStart Previous position of the first item that was removed
     * @param itemCount Number of items removed from the data set
     */
    fun safeNotifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        if (positionStart >= 0 && itemCount >= 0) {
            runWhenNotComputingLayout { notifyItemRangeRemoved(positionStart, itemCount) }
        } else {
            Log.w(TAG, "safeNotifyItemRangeRemoved: positionStart / itemStart is/are negative")
        }
    }

    /**
     * Safe version of [RecyclerView.Adapter.notifyItemRemoved].
     *
     * @param position Position of the item that has now been removed
     */
    fun safeNotifyItemRemoved(position: Int) {
        if (position >= 0) {
            runWhenNotComputingLayout { notifyItemRemoved(position) }
        } else {
            Log.w(TAG, "safeNotifyItemRemoved: positionStart / itemStart is/are negative")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrickViewHolder =
        BrickViewHolderFactory().createBrickViewHolder(parent, viewType, brickDataManager)

    override fun onBindViewHolder(holder: BrickViewHolder, position: Int) {
        brickDataManager.brickAtPosition(position)?.let { baseBrick ->
            (holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.isFullSpan =
                baseBrick.spanSize.getSpans(holder.itemView.context) == BrickDataManager.SPAN_COUNT

            if (baseBrick.isDataReady) {
                baseBrick.onBindData(holder)
            }
            onReachedItemAtPosition?.bindingItemAtPosition(position)
        }
    }

    override fun onViewAttachedToWindow(holder: BrickViewHolder) = holder.onViewAttachedToWindow()

    override fun onViewDetachedFromWindow(holder: BrickViewHolder) = holder.releaseViewsOnDetach()

    override fun getItemCount(): Int = brickDataManager.recyclerViewItems.size

    override fun getItemViewType(position: Int): Int {
        val brick: BaseBrick? = brickDataManager.brickAtPosition(position)
        return when {
            brick == null -> BaseBrick.DEFAULT_LAYOUT_RES_ID
            brick.isDataReady -> brick.layout
            else -> brick.placeholderLayout
        }
    }

    /**
     * Set an [OnReachedItemAtPosition].
     *
     * @param onReachedItemAtPosition [OnReachedItemAtPosition] to set
     */
    fun setOnReachedItemAtPosition(onReachedItemAtPosition: OnReachedItemAtPosition?) {
        this.onReachedItemAtPosition = onReachedItemAtPosition
    }

    private fun runWhenNotComputingLayout(callToRun: RecyclerView.Adapter<BrickViewHolder>.() -> Unit) {
        if (recyclerView.isComputingLayout) {
            handler.post { callToRun.invoke(this) }
        } else {
            callToRun.invoke(this)
        }
    }

    companion object {
        private val TAG = BrickRecyclerAdapter::class.java.name
    }
}
