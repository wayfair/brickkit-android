/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.wayfair.brickkit.BrickDataManager

/**
 * Fragment which provides a simple interface for adding bricks / behaviors.
 */
open class BrickFragment : Fragment() {
    protected val dataManager = BrickDataManager()

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.vertical_fragment_brick, container, false).apply {
            findViewById<RecyclerView>(R.id.recycler_view).apply {
                (itemAnimator as DefaultItemAnimator?)?.supportsChangeAnimations = false
                dataManager.recyclerView = this
            }
        }

    override fun onDestroyView() {
        dataManager.onDestroyView()
        super.onDestroyView()
    }
}
