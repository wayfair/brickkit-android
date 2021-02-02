/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.fragmentbrick

import android.os.Bundle
import com.wayfair.brickkitdemo.BrickFragment
import com.wayfair.brickkitdemo.R
import com.wayfair.brickkitdemo.fragmentbrick.bricks.FragmentBrick
import com.wayfair.brickkitdemo.simplebrick.SimpleBrickFragment

/**
 * Example of fragment containing [FragmentBrick]'s containing [SimpleBrickFragment]'s.
 */
class FragmentBrickFragment : BrickFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FragmentBrick(childFragmentManager, SimpleBrickFragment.newInstance(1), R.color.colorAccent).addLastTo(dataManager)
        FragmentBrick(childFragmentManager, SimpleBrickFragment.newInstance(2), R.color.colorPrimary).addLastTo(dataManager)
        FragmentBrick(childFragmentManager, SimpleBrickFragment.newInstance(3), R.color.colorPrimaryDark).addLastTo(dataManager)
    }
}
