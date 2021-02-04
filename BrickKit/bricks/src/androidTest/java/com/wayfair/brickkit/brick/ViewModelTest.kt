package com.wayfair.brickkit.brick

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ViewModelTest {
    private val dataModel: DataModel = mock()
    private lateinit var viewModel: ViewModel<DataModel>

    @Before
    fun setup() {
        viewModel = object : ViewModel<DataModel>(dataModel) { }

        verify(dataModel).addUpdateListener(viewModel)
    }

    @Test
    fun testNotifyChange() {
        val updateListener = mock<ViewModel.ViewModelUpdateListener>()

        viewModel.addUpdateListener(updateListener)

        viewModel.notifyChange()

        verify(updateListener).onChange()
    }

    @Test
    fun testIsDataModelReady_dataModelReady() {
        whenever(dataModel.isReady).thenReturn(true)

        assertTrue(viewModel.isDataModelReady)
    }

    @Test
    fun testIsDataModelReady_dataModelNotReady() {
        whenever(dataModel.isReady).thenReturn(false)

        assertFalse(viewModel.isDataModelReady)
    }
}
