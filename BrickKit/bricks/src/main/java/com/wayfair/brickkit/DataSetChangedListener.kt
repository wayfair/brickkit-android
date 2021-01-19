/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit

/**
 * Listener which can be used to listen for data set changes.
 */
interface DataSetChangedListener {
    /**
     * Callback that will be triggered when data set changes occur.
     */
    fun onDataSetChanged()
}
