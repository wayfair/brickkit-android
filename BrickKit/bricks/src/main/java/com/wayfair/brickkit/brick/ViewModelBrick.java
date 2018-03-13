package com.wayfair.brickkit.brick;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.View;

import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;

/**
 * This class is used as a Generic Brick that can take in any XML Layout and use DataBinding to
 * insert information from a {@link ViewModel}.
 */
public final class ViewModelBrick extends BaseBrick implements ViewModel.ViewModelUpdateListener {
    @LayoutRes
    private final int layoutId;
    @LayoutRes
    private final int placeholderLayoutId;
    private PlaceholderBinder placeholderBinder;
    protected final SparseArray<ViewModel> viewModels;

    protected SwipeListener onDismiss;

    /**
     * Private constructor that the {@link Builder} class uses.
     *
     * @param builder the builder
     */
    private ViewModelBrick(Builder builder) {
        super(builder.spanSize, builder.padding);

        this.layoutId = builder.layoutId;
        this.placeholderLayoutId = builder.placeholderLayoutId;
        this.placeholderBinder = builder.placeholderBinder;
        this.onDismiss = builder.onDismiss;
        this.viewModels = builder.viewModels;

        for (int i = 0; i < viewModels.size(); i++) {
            viewModels.valueAt(i).addUpdateListener(this);
        }
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
     * Gets all {@link ViewModel}s.
     *
     * @return a {@link ViewModel} for the binding id
     */
    public SparseArray<ViewModel> getViewModels() {
        return viewModels;
    }

    /**
     * Add a view model to the Brick.
     *
     * @param bindingId the binding ID of the view model
     * @param viewModel the view model
     */
    public void addViewModel(int bindingId, ViewModel viewModel) {
        viewModel.addUpdateListener(this);
        this.viewModels.put(bindingId, viewModel);
        onChange();
    }

    /**
     * Replace all the view models with these.
     *
     * @param viewModels the view models to replace existing view models
     */
    public void setViewModels(SparseArray<ViewModel> viewModels) {
        this.viewModels.clear();
        for (int i = 0; i < viewModels.size(); i++) {
            viewModels.valueAt(i).addUpdateListener(this);
            this.viewModels.put(viewModels.keyAt(i), viewModels.valueAt(i));
        }
        onChange();
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
    public void onBindPlaceholder(BrickViewHolder holder) {
        if (placeholderBinder != null) {
            placeholderBinder.onBindPlaceholder(holder);
        }
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
    public int getPlaceholderLayout() {
        return placeholderLayoutId;
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
     * {@inheritDoc}
     */
    @Override
    public void onChange() {
        setHidden(!isDataReady());

        refreshItem();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDataReady() {
        boolean isDataReady = viewModels.size() != 0;

        for (int i = 0; isDataReady && i < viewModels.size(); i++) {
            isDataReady = viewModels.valueAt(i).isDataModelReady();
        }

        return isDataReady;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean areContentsTheSame = true;

        if (obj instanceof ViewModelBrick) {
            for (int i = 0; i < getViewModels().size(); i++) {
                for (int j = 0; j < ((ViewModelBrick) obj).getViewModels().size(); j++) {
                    if (getViewModels().keyAt(i) == ((ViewModelBrick) obj).getViewModels().keyAt(j)) {
                        if (!getViewModels().valueAt(i).equals(((ViewModelBrick) obj).getViewModels().valueAt(j))) {
                            areContentsTheSame = false;
                        }
                    }
                }

            }
        } else {
            areContentsTheSame = false;
        }

        return areContentsTheSame;
    }

    /**
     * A builder class for {@link ViewModelBrick}, this makes it clearer what is required and what you are actually doing when creating
     * {@link ViewModelBrick}s.
     */
    public static class Builder {
        @LayoutRes
        int layoutId;
        @LayoutRes
        int placeholderLayoutId;
        PlaceholderBinder placeholderBinder = null;
        SparseArray<ViewModel> viewModels = new SparseArray<>();
        BrickSize spanSize = getDefaultSize();
        BrickPadding padding = getDefaultPadding();
        SwipeListener onDismiss = null;

        /**
         * Builder constructor, requires only a {@link LayoutRes} to work.
         *
         * @param layoutId a {@link LayoutRes} to use as a brick
         */
        public Builder(@LayoutRes int layoutId) {
            this.layoutId = layoutId;
        }

        /**
         * Set the placeholder for this brick.
         *
         * @param placeholderLayoutId the placeholder layout id to be used
         * @param placeholderBinder the object that helps bind the place holder
         * @return the builder
         */
        public Builder setPlaceholder(@LayoutRes int placeholderLayoutId, PlaceholderBinder placeholderBinder) {
            this.placeholderLayoutId = placeholderLayoutId;
            this.placeholderBinder = placeholderBinder;
            return this;
        }

        /**
         * Add a {@link ViewModel} with a binding Id for the layout already defined.
         *
         * @param bindingId the binding Id of the view model
         * @param viewModel the view model to be bound, extends {@link ViewModel}
         * @return the builder
         */
        public Builder addViewModel(int bindingId, ViewModel viewModel) {
            if (viewModel != null) {
                this.viewModels.put(bindingId, viewModel);
            }
            return this;
        }

        /**
         * Add a set of {@link ViewModel}s and their binding Ids.
         *
         * @param viewModels a {@link SparseArray} of binding Ids and {@link ViewModel}s
         * @return the builder
         */
        public Builder setViewModels(SparseArray<ViewModel> viewModels) {
            this.viewModels = viewModels;
            return this;
        }

        /**
         * Set the {@link BrickSize}.
         *
         * @param spanSize the {@link BrickSize}
         * @return the builder
         */
        public Builder setSpanSize(BrickSize spanSize) {
            this.spanSize = spanSize;
            return this;
        }

        /**
         * Set the {@link BrickPadding}.
         *
         * @param padding the {@link BrickPadding}
         * @return the builder
         */
        public Builder setPadding(BrickPadding padding) {
            this.padding = padding;
            return this;
        }

        /**
         * Set the {@link SwipeListener}.
         *
         * @param onDismiss the {@link SwipeListener}
         * @return the builder
         */
        public Builder setOnDismiss(SwipeListener onDismiss) {
            this.onDismiss = onDismiss;
            return this;
        }

        /**
         * Assemble the {@link ViewModelBrick}.
         *
         * @return the complete {@link ViewModelBrick}
         */
        public ViewModelBrick build() {
            return new ViewModelBrick(this);
        }

        /**
         * Get the default size.
         *
         * @return the default {@link BrickSize}
         */
        protected BrickSize getDefaultSize() {
            return DEFAULT_SIZE_FULL_WIDTH;
        }

        /**
         * Get the default padding.
         *
         * @return the default {@link BrickPadding}
         */
        protected BrickPadding getDefaultPadding() {
            return DEFAULT_PADDING_NONE;
        }
    }

    /**
     * A special {@link BrickViewHolder} that can handle binding {@link ViewModel}s to layouts.
     */
    public static final class ViewModelBrickViewHolder extends BrickViewHolder {
        public final ViewDataBinding viewDataBinding;

        /**
         * Constructor to set up the {@link BrickViewHolder} with the {@link ViewDataBinding}
         * from the right item view.
         *
         * @param viewDataBinding the {@link ViewDataBinding} object
         *                        from {@link #createViewHolder(View)}
         */
        public ViewModelBrickViewHolder(ViewDataBinding viewDataBinding) {
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
