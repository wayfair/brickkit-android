package com.wayfair.brickkit

import androidx.recyclerview.widget.ListUpdateCallback

/**
 * A safe implementation of [androidx.recyclerview.widget.ListUpdateCallback],
 * which allows updates to the adapter during scrolling by mapping the calls
 * to safe versions.
 */
internal class SafeAdapterListUpdateCallback(
    private val adapter: BrickRecyclerAdapter
) : ListUpdateCallback {

    override fun onInserted(position: Int, count: Int) {
        adapter.safeNotifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.safeNotifyItemRangeRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.safeNotifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter.safeNotifyItemRangeChanged(position, count, payload)
    }
}
