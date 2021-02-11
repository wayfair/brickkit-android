/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.brick

import android.view.View
import androidx.annotation.LayoutRes
import com.wayfair.brickkit.BrickDataManager
import com.wayfair.brickkit.padding.BrickPadding
import com.wayfair.brickkit.size.BrickSize
import com.wayfair.brickkit.viewholder.BrickViewHolder
import java.lang.UnsupportedOperationException

/**
 * Abstract class which defines Bricks.
 */
abstract class BaseBrick constructor(
    open val spanSize: BrickSize,
    internal open val padding: BrickPadding
) {
    /**
     * The brick's tag. This is similar to a [View.getTag].
     */
    var tag: Any? = null

    /**
     * Whether the brick should be hidden.
     */
    var isHidden = false
        set(hidden) {
            val wasHidden = isHidden

            field = hidden

            if (wasHidden != hidden) {
                if (!wasHidden) {
                    dataManager?.hideItem(this)
                } else {
                    dataManager?.showItem(this)
                }
            }
        }
    /**
     * Whether the brick should act as it is in the first row.
     */
    open var isInFirstRow = false

    /**
     * Whether the brick should act as it is in the last row.
     */
    open var isInLastRow = false

    /**
     * Whether the brick should act as it is on the left wall.
     */
    open var isOnLeftWall = false

    /**
     * Whether the brick should act as it is on the right wall.
     */
    open var isOnRightWall = false

    private var dataManager: BrickDataManager? = null

    /**
     * Set the current DataManager this brick is connected to.
     * @param brickDataManager The current [BrickDataManager] for this brick
     */
    fun setDataManager(brickDataManager: BrickDataManager?) {
        dataManager = brickDataManager
    }

    /**
     * Called to display the information in this brick to the specified ViewHolder.
     *
     * @param holder BrickViewHolder which should be updated.
     */
    abstract fun onBindData(holder: BrickViewHolder)

    /**
     * Get layout resource id for this brick.
     *
     * @return the layout resource id for this brick
     */
    @get:LayoutRes
    abstract val layout: Int

    /**
     * Current brick must override this in order for the adapter to inflate
     * either placeholder or brick layout. If not overridden we always assume data is ready.
     *
     * @return True if data is ready
     */
    open val isDataReady: Boolean
        get() = true

    /**
     * Get layout resource id for this brick's placeholder.
     *
     * @return The layout resource id for this brick's placeholder
     */
    @get:LayoutRes
    open val placeholderLayout: Int
        get() = throw UnsupportedOperationException(
            "${javaClass.simpleName}.placeholderLayout must be overridden within brick extending BaseBrick"
        )

    /**
     * Creates an instance of the [BrickViewHolder] for this class.
     *
     * @param itemView view to pass into the [BrickViewHolder]
     * @return the [BrickViewHolder]
     */
    abstract fun createViewHolder(itemView: View): BrickViewHolder

    /**
     * If this brick as attached to a DataManager, refresh this brick in that DataManager.
     */
    open fun refreshItem() {
        dataManager?.refreshItem(this)
    }

    /**
     * Adds this brick to the end of the supplied [BrickDataManager].
     *
     * @param dataManager The [BrickDataManager] to add to
     */
    fun addLastTo(dataManager: BrickDataManager) {
        dataManager.addLast(this)
    }

    /**
     * Adds this brick to the end of the supplied [BrickDataManager].
     *
     * @param dataManager The [BrickDataManager] to add to
     */
    fun addFirstTo(dataManager: BrickDataManager) {
        dataManager.addFirst(this)
    }
}
