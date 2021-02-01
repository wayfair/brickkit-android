/**
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wayfair.brickkitdemo.mainfragment.MainActivityFragment

/**
 * Main activity for the app.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.content, MainActivityFragment()).commit()
        }
    }
}
