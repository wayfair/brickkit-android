package com.wayfair.brickkit.viewholder.factory;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.wayfair.brickkit.viewholder.BrickViewHolder;

/**
 * A data model class, used by the {@link BrickViewHolderFactory} with creation of
 * {@link BrickViewHolder} objects.
 * <p>
 * Note: Object references of this class should not be held onto since member "parent" has a
 * context.  The references should be able to be collected by the GC.
 * <p>
 * Copyright © 2017 Wayfair. All rights reserved.
 */
public class BrickViewHolderFactoryData {
    private String logTag;
    private ViewGroup parent;
    private int viewType;
    private BrickProvider brickProvider;

    /**
     * @param logTag   for the calling class
     * @param parent   of the view, passed into the {@link BrickViewHolder}'s constructor.
     * @param viewType of the view to create.  It can be a @LayoutRes id.
     * @param provider provides bricks, based on resource id
     */
    public BrickViewHolderFactoryData(@NonNull String logTag,
                                      @NonNull ViewGroup parent,
                                      int viewType,
                                      @NonNull BrickProvider provider) {
        this.logTag = logTag;
        this.parent = parent;
        this.viewType = viewType;
        brickProvider = provider;
    }

    /**
     * @return the tag, used with logging
     */
    @NonNull
    /* private package */ String getLogTag() {
        return logTag;
    }

    /**
     * @return the parent, used for creating item views for view holders
     */
    @NonNull
    /* private package */ ViewGroup getParent() {
        return parent;
    }

    /**
     * @return the view type int (a layout id), used to create view holders
     */
    /* private package */ int getViewType() {
        return viewType;
    }

    /**
     * @return the provider, which returns bricks based on layout ids
     */
    @NonNull
    /* private package */ BrickProvider getBrickProvider() {
        return brickProvider;
    }

    @Override
    public String toString() {
        return "BrickViewHolderFactoryData{"
                + "logTag='"
                + logTag
                + '\''
                + ", parent="
                + parent
                + ", viewType="
                + viewType
                + ", brickProvider="
                + brickProvider
                + '}';
    }
}
