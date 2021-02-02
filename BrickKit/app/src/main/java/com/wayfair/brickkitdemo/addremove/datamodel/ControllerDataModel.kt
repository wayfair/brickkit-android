/*
 * Copyright © 2017-2021 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.addremove.datamodel

import com.wayfair.brickkit.brick.DataModel

/**
 * Data model for a controller brick.
 */
class ControllerDataModel(initialValue: Int) : DataModel() {

    var value: Int = initialValue
        set(value) {
            field = value
            notifyChange()
        }
}
