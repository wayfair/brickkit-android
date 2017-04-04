/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wayfair.brickkit.base.viewmodel.MvvmViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * Base ViewHolder for bricks. It extends {@link android.support.v7.widget.RecyclerView.ViewHolder} with
 * one additional method that is called when the view holder is detached from the {@link RecyclerView} so
 * that views can be released.
 */
public class BrickViewHolder<B extends ViewDataBinding, V extends MvvmViewModel> extends RecyclerView.ViewHolder {

    protected B binding;

    protected V viewModel;

    /**
     * Constructor.
     *
     * @param itemView view used for binding
     */
    public BrickViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * Called when {@link BrickRecyclerAdapter#onViewAttachedToWindow(BrickViewHolder)}
     * is invoked.
     */
    protected void onViewAttachedToWindow() {
    }

    /**
     * Method called when the view is detached from the {@link RecyclerView}. All views that have resources
     * should release them here (e.g. ImageView).
     */
    protected void releaseViewsOnDetach() {
    }

    /* Use this method to create the binding for the ViewHolder. This method also handles
     * setting the view model on the binding and attaching the view. */
    protected final void bindContentView(@IntegerRes int vm, @NonNull View view, @NotNull V viewModel) {
        this.viewModel = viewModel;
        binding = DataBindingUtil.bind(view);
        binding.setVariable(vm, viewModel);
    }
}
