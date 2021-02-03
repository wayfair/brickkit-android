package com.wayfair.brickkit.brick

import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class DataModelTest {

    private val dataModel: DataModel = object : DataModel() { }

    @Test
    fun testIsReady() {
        assertTrue(dataModel.isReady)
    }

    @Test
    fun testNotifyChange() {
        val countDownLatch = CountDownLatch(2)
        val listener1 = object : DataModel.DataModelUpdateListener {
            override fun notifyChange() {
                countDownLatch.countDown()
            }
        }

        val listener2 = object : DataModel.DataModelUpdateListener {
            override fun notifyChange() {
                countDownLatch.countDown()
            }
        }

        val removedListener = object : DataModel.DataModelUpdateListener {
            override fun notifyChange() {
                fail()
            }
        }

        dataModel.addUpdateListener(listener1)
        dataModel.addUpdateListener(listener2)
        dataModel.addUpdateListener(removedListener)
        dataModel.removeUpdateListener(removedListener)

        dataModel.notifyChange()

        countDownLatch.await(5, TimeUnit.SECONDS)
    }
}
