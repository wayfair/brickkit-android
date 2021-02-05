package com.wayfair.brickkit.brick

import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.Bindable
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.wayfair.brickkit.OpenForTesting
import com.wayfair.brickkit.padding.BrickPadding
import com.wayfair.brickkit.size.BrickSize
import com.wayfair.brickkit.test.BR
import com.wayfair.brickkit.test.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class ViewModelBrickTest {

    @Test
    fun testSingleViewModel_Test() {
        val textDataModel = TextDataModel(TEXT)
        val textViewModel = spy(TextViewModel(textDataModel))
        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, textViewModel)
            .build()

        viewModelBrick.onBindData(
            viewModelBrick.createViewHolder(
                LayoutInflater.from(ApplicationProvider.getApplicationContext()).inflate(
                    viewModelBrick.layout,
                    LinearLayout(ApplicationProvider.getApplicationContext()),
                    false
                )
            )
        )

        verify(viewModelBrick.getViewModel(BR.viewModel) as TextViewModel).text
    }

    @Test
    fun testEquals_dataModelsAreEqual() {
        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, TextViewModel(TextDataModel(TEXT)))
            .build()

        val viewModelBrick2 = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, TextViewModel(TextDataModel(TEXT)))
            .build()

        assertTrue(viewModelBrick == viewModelBrick2)
    }

    @Test
    fun testEquals_dataModelsAreNotEqual() {
        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, TextViewModel(TextDataModel(TEXT)))
            .build()

        val viewModelBrick2 = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, TextViewModel(TextDataModel(TEXT_2)))
            .build()

        assertFalse(viewModelBrick == viewModelBrick2)
    }

    @Test
    fun testEquals_dataModelsAreEqualDifferentOrder() {
        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, TextViewModel(TextDataModel(TEXT)))
            .addViewModel(BR.text, TextViewModel(TextDataModel(TEXT_2)))
            .build()

        val viewModelBrick2 = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.text, TextViewModel(TextDataModel(TEXT_2)))
            .addViewModel(BR.viewModel, TextViewModel(TextDataModel(TEXT)))
            .build()

        assertTrue(viewModelBrick == viewModelBrick2)
    }

    @Test
    fun testEquals_viewModelsAreDifferentTypes() {
        assertFalse(ViewModelBrick.Builder(R.layout.text_brick_vm).build() == mock<BaseBrick>())
    }

    @Test
    fun testAddViewModel() {
        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, TextViewModel(TextDataModel(TEXT)))
            .build()

        viewModelBrick.addViewModel(BR.text, TextViewModel(TextDataModel(TEXT_2)))

        assertEquals(2, viewModelBrick.viewModels.size())
    }

    @Test
    fun testIsDataReady_emptyViewModels() {
        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .build()

        assertFalse(viewModelBrick.isDataReady)
    }

    @Test
    fun testIsDataReady_firstNotReady() {
        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, TextViewModel(TextDataModel("")))
            .addViewModel(BR.text, TextViewModel(TextDataModel(TEXT)))
            .build()

        assertFalse(viewModelBrick.isDataReady)
    }

    @Test
    fun testIsDataReady_lastNotReady() {
        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.text, TextViewModel(TextDataModel(TEXT)))
            .addViewModel(BR.viewModel, TextViewModel(TextDataModel("")))
            .build()

        assertFalse(viewModelBrick.isDataReady)
    }

    @Test
    fun testIsDataReady_ready() {
        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.text, TextViewModel(TextDataModel(TEXT)))
            .addViewModel(BR.viewModel, TextViewModel(TextDataModel(TEXT)))
            .build()

        assertTrue(viewModelBrick.isDataReady)
    }

    @Test
    fun testBuilder_setPlaceholder() {
        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, TextViewModel(TextDataModel("")))
            .setPlaceholder(R.layout.text_brick_vm_placeholder)
            .build()

        assertEquals(R.layout.text_brick_vm_placeholder, viewModelBrick.placeholderLayout)
    }

    @Test
    fun testBuilder_setPadding() {
        val padding = mock<BrickPadding>()

        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .setPadding(padding)
            .build()

        assertEquals(padding, viewModelBrick.padding)
    }

    @Test
    fun testBuilder_setSpanSize() {
        val spanSize = mock<BrickSize>()

        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .setSpanSize(spanSize)
            .build()

        assertEquals(spanSize, viewModelBrick.spanSize)
    }

    @Test
    fun testBuilder_addViewModel() {
        val viewModel = mock<ViewModel<DataModel>>()

        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, viewModel)
            .build()

        assertEquals(viewModel, viewModelBrick.viewModels[BR.viewModel])
    }

    @Test
    fun testBuilder_addNullViewModel() {
        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, null)
            .build()

        assertNull(viewModelBrick.viewModels[BR.viewModel])
    }

    @Test
    fun testOnChange() {
        val dataModel = TextDataModel(TEXT)
        val viewModel = TextViewModel(dataModel)

        val viewModelBrick = ViewModelBrick.Builder(R.layout.text_brick_vm)
            .addViewModel(BR.viewModel, viewModel)
            .setPlaceholder(R.layout.text_brick_vm_placeholder)
            .build()

        val countDownLatch = CountDownLatch(1)
        viewModel.addUpdateListener(
            object : ViewModel.ViewModelUpdateListener {
                override fun onChange() {
                    countDownLatch.countDown()
                }
            }
        )

        assertFalse(viewModelBrick.isHidden)

        dataModel.text = ""

        countDownLatch.await()

        assertTrue(viewModelBrick.isHidden)
    }

    @OpenForTesting
    class TextViewModel(dataModel: TextDataModel) : ViewModel<TextDataModel>(dataModel) {
        @get:Bindable
        val text: String
            get() = dataModel.text

        override fun equals(other: Any?): Boolean = other is TextViewModel && dataModel.text == other.dataModel.text

        override fun hashCode(): Int = 1
    }

    class TextDataModel(initialText: String) : DataModel() {

        var text: String = initialText
            set(value) {
                field = value
                notifyChange()
            }

        override val isReady: Boolean
            get() = text.isNotEmpty()
    }

    companion object {
        private const val TEXT = "Test Text..."
        private const val TEXT_2 = "Not Test Text..."
    }
}
