package com.wayfair.brickkit.brick

import android.util.SparseArray
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.util.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.wayfair.brickkit.padding.BrickPadding
import com.wayfair.brickkit.padding.ZeroBrickPadding
import com.wayfair.brickkit.size.BrickSize
import com.wayfair.brickkit.size.FullWidthBrickSize
import com.wayfair.brickkit.viewholder.BrickViewHolder

/**
 * This class is used as a Generic Brick that can take in any XML Layout and use DataBinding to
 * insert information from a [ViewModel].
 */
class ViewModelBrick private constructor(
    private val layoutId: Int,
    private val placeholderLayoutId: Int,
    val viewModels: SparseArray<ViewModel<*>>,
    spanSize: BrickSize,
    padding: BrickPadding
) : BaseBrick(spanSize, padding), ViewModel.ViewModelUpdateListener {

    init {
        (0 until viewModels.size()).forEach { i -> viewModels.valueAt(i).addUpdateListener(this) }
    }

    /**
     * Gets the appropriate [ViewModel] for the given binding id.
     *
     * @param bindId the binding id
     * @return a [ViewModel] for the binding id
     */
    fun getViewModel(bindId: Int): ViewModel<*> = viewModels[bindId]

    /**
     * Add a view model to the Brick.
     *
     * @param bindingId the binding ID of the view model
     * @param viewModel the view model
     */
    fun addViewModel(bindingId: Int, viewModel: ViewModel<*>) {
        viewModel.addUpdateListener(this)
        viewModels.put(bindingId, viewModel)
        onChange()
    }

    /**
     * {@inheritDoc}
     */
    override fun onBindData(holder: BrickViewHolder) {
        val binding = (holder as ViewModelBrickViewHolder).viewDataBinding

        (0 until viewModels.size()).forEach { i ->
            binding.setVariable(viewModels.keyAt(i), viewModels.valueAt(i))
        }

        binding.executePendingBindings()
    }

    /**
     * {@inheritDoc}
     */
    override fun getLayout(): Int = layoutId

    /**
     * {@inheritDoc}
     */
    override fun getPlaceholderLayout(): Int = placeholderLayoutId

    /**
     * {@inheritDoc}
     */
    override fun createViewHolder(itemView: View): BrickViewHolder = ViewModelBrickViewHolder(DataBindingUtil.bind(itemView)!!)

    /**
     * {@inheritDoc}
     */
    override fun onChange() {
        isHidden = !isDataReady
        refreshItem()
    }

    /**
     * {@inheritDoc}
     */
    override fun isDataReady(): Boolean {
        var isDataReady = viewModels.isNotEmpty()

        var i = 0
        while (isDataReady && i < viewModels.size()) {
            isDataReady = viewModels.valueAt(i++).isDataModelReady
        }

        return isDataReady
    }

    override fun hashCode(): Int = super.hashCode()

    override fun equals(other: Any?): Boolean {
        var areContentsTheSame = true
        if (other is ViewModelBrick) {
            (0 until viewModels.size()).forEach { i ->
                (0 until other.viewModels.size()).forEach { j ->
                    if (viewModels.keyAt(i) == other.viewModels.keyAt(j) && viewModels.valueAt(i) != other.viewModels.valueAt(j)) {
                        areContentsTheSame = false
                    }
                }
            }
        } else {
            areContentsTheSame = false
        }
        return areContentsTheSame
    }

    /**
     * A builder class for [ViewModelBrick], this makes it clearer what is required and what you are actually doing when creating
     * [ViewModelBrick]s.
     */
    class Builder(@LayoutRes private val layoutId: Int) {
        private var placeholderLayoutId = 0
        private var viewModels = SparseArray<ViewModel<*>>()
        private var spanSize: BrickSize = FullWidthBrickSize()
        private var padding: BrickPadding = ZeroBrickPadding()

        /**
         * Set the placeholder for this brick.
         *
         * @param placeholderLayoutId the placeholder layout id to be used
         * @return the builder
         */
        fun setPlaceholder(@LayoutRes placeholderLayoutId: Int): Builder {
            this.placeholderLayoutId = placeholderLayoutId
            return this
        }

        /**
         * Add a [ViewModel] with a binding Id for the layout already defined.
         *
         * @param bindingId the binding Id of the view model
         * @param viewModel the view model to be bound, extends [ViewModel]
         * @return the builder
         */
        fun addViewModel(bindingId: Int, viewModel: ViewModel<*>?): Builder {
            if (viewModel != null) {
                viewModels.put(bindingId, viewModel)
            }
            return this
        }

        /**
         * Set the [BrickSize].
         *
         * @param spanSize the [BrickSize]
         * @return the builder
         */
        fun setSpanSize(spanSize: BrickSize): Builder {
            this.spanSize = spanSize
            return this
        }

        /**
         * Set the [BrickPadding].
         *
         * @param padding the [BrickPadding]
         * @return the builder
         */
        fun setPadding(padding: BrickPadding): Builder {
            this.padding = padding
            return this
        }

        /**
         * Assemble the [ViewModelBrick].
         *
         * @return the complete [ViewModelBrick]
         */
        fun build(): ViewModelBrick = ViewModelBrick(layoutId, placeholderLayoutId, viewModels, spanSize, padding)
    }

    /**
     * A special [BrickViewHolder] that can handle binding [ViewModel]s to layouts.
     */
    private class ViewModelBrickViewHolder(val viewDataBinding: ViewDataBinding) : BrickViewHolder(viewDataBinding.root)
}
