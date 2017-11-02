package com.wayfair.brickkit.brick;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.VisibleForTesting;
import android.util.SparseArray;
import android.view.View;

import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;

/**
 * This class is used as a Generic Brick that can take in any XML Layout and use DataBinding to
 * insert information from a {@link ViewModel}.
 */
public class ViewModelBrick extends BaseBrick {
    @LayoutRes
    private final int layoutId;
    private final SparseArray<ViewModel> viewModels;

    private SwipeListener onDismiss;

    /**
     * Constructor for setting up a ViewModelBrick with only one {@link ViewModel}.
     *
     * @param spanSize size information for this brick
     * @param padding padding for this brick
     * @param layoutId the id {@link LayoutRes} for the XML
     * @param bindId the id generated for the "variable" from {@link ViewDataBinding}
     * @param viewModel the {@link ViewModel} to bind the the XML
     */
    public ViewModelBrick(BrickSize spanSize, BrickPadding padding,
                          @LayoutRes int layoutId, int bindId, ViewModel viewModel) {
        super(spanSize, padding);

        this.layoutId = layoutId;
        this.viewModels = new SparseArray<>();
        this.viewModels.put(bindId, viewModel);
    }


    /**
     * Constructor for setting up a ViewModelBrick with only one {@link ViewModel}.
     *
     * @param spanSize size information for this brick
     * @param padding padding for this brick
     * @param layoutId the id {@link LayoutRes} for the XML
     * @param viewModels the {@link ViewModel}s and ids generated for the "variable"
     *                   from {@link ViewDataBinding} to bind the the XML
     */
    public ViewModelBrick(BrickSize spanSize, BrickPadding padding,
                          @LayoutRes int layoutId, SparseArray<ViewModel> viewModels) {
        super(spanSize, padding);

        this.layoutId = layoutId;
        this.viewModels = viewModels;
    }

    /**
     * Gets the appropriate {@link ViewModel} for the given binding id.
     *
     * @param bindId the binding id
     * @return a {@link ViewModel} for the binding id
     */
    public ViewModel getViewModel(int bindId) {
        return viewModels.get(bindId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindData(BrickViewHolder holder) {
        ViewModelBrickViewHolder viewModelBrickViewHolder = (ViewModelBrickViewHolder) holder;

        for (int i = 0; i < viewModels.size(); i++) {
            viewModelBrickViewHolder.bind(viewModels.keyAt(i), viewModels.valueAt(i));
        }

        viewModelBrickViewHolder.getViewDataBinding().executePendingBindings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLayout() {
        return layoutId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BrickViewHolder createViewHolder(View itemView) {
        return new ViewModelBrickViewHolder(DataBindingUtil.bind(itemView));
    }

    /**
     * Run the {@link SwipeListener} when a swipe happens on this brick.
     *
     * @param direction one of {@link android.support.v7.widget.helper.ItemTouchHelper.UP},
     *                  {@link android.support.v7.widget.helper.ItemTouchHelper.RIGHT},
     *                  {@link android.support.v7.widget.helper.ItemTouchHelper.DOWN},
     *                  {@link android.support.v7.widget.helper.ItemTouchHelper.LEFT}
     */
    @SuppressWarnings("JavadocReference")
    @Override
    public void dismissed(int direction) {
        if (onDismiss != null) {
            onDismiss.swiped(direction);
        }
    }

    /**
     * Set the action for this brick being swiped.
     *
     * @param onDismiss the {@link SwipeListener} to be run on swipe
     */
    public void setOnDismiss(SwipeListener onDismiss) {
        this.onDismiss = onDismiss;
    }

    /**
     * A special {@link BrickViewHolder} that can handle binding {@link ViewModel}s to layouts.
     */
    @VisibleForTesting
    public static final class ViewModelBrickViewHolder extends BrickViewHolder {
        private final ViewDataBinding viewDataBinding;

        /**
         * Constructor to set up the {@link BrickViewHolder} with the {@link ViewDataBinding}
         * from the right item view.
         *
         * @param viewDataBinding the {@link ViewDataBinding} object
         *                        from {@link #createViewHolder(View)}
         */
        ViewModelBrickViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());

            this.viewDataBinding = viewDataBinding;
        }

        /**
         * Sets the {@link ViewModel} to be bound for the given id.
         *
         * @param bindId the id
         * @param viewModel the {@link ViewModel}
         */
        void bind(int bindId, ViewModel viewModel) {
            viewDataBinding.setVariable(bindId, viewModel);
        }

        /**
         * gets the {@link ViewDataBinding} object in order to execute them
         * {@link #onBindData(BrickViewHolder)}.
         *
         * @return the {@link ViewDataBinding}
         */
        ViewDataBinding getViewDataBinding() {
            return viewDataBinding;
        }
    }
}
