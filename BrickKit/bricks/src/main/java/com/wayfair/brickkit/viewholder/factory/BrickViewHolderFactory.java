package com.wayfair.brickkit.viewholder.factory;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.wayfair.brickkit.viewholder.BrickViewHolder;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.brick.BrickProvider;
import com.wayfair.brickkit.view.empty.EmptyBrickView;
import com.wayfair.brickkit.viewholder.EmptyBrickViewHolder;

/**
 * A factory used to create various types of {@link BrickViewHolder} objects.
 * <p>
 * This class takes it's roots from the  Factory Design Pattern.  Instead of using an interface, it
 * is using the {@link BrickViewHolder} abstract class.  For more information on the pattern,
 * refer to:
 * <a href="https://www.tutorialspoint.com/design_pattern/factory_pattern.htm">Factory Design Pattern</a>.
 * <p>
 * Copyright © 2017 Wayfair. All rights reserved.
 */
public class BrickViewHolderFactory {

    /**
     * Creates {@link BrickViewHolder} objects based on the {@param #data}.
     *
     * @param viewHolderFactoryData - used for instantiation of {@link BrickViewHolder} objects.
     * @return the created {@link BrickViewHolder}
     */
    public BrickViewHolder createBrickViewHolder(
            @NonNull BrickViewHolderFactoryData viewHolderFactoryData) {

        Context context = viewHolderFactoryData.getParent().getContext();
        BrickViewHolder viewHolder = null;
        try {
            // The following creates the correct view holder, based on the layout res id.  The
            // strategy is for view types of the default value / zero, an empty brick view holder
            // is created, providing for it to be returned by the Recycler View adapter instead and
            // for avoiding app crashes.
            if (BaseBrick.DEFAULT_LAYOUT_RES_ID >= viewHolderFactoryData.getViewType()) {
                // By creating and using an empty brick view holder, the view holder can be in
                // place in scenarios.  i.e. when bricks are null and a view holder cannot be
                // created.
                viewHolder = createEmptyBrickViewHolder(context);
            } else {
                // Since the view type (layout id) is set, create a standard brick view holder.
                viewHolder = createViewHolderWithViewType(
                        viewHolderFactoryData.getParent(),
                        viewHolderFactoryData.getViewType(),
                        viewHolderFactoryData.getBrickProvider());
            }
        } catch (AssertionError ae) {
            // This shouldn't happen
            Log.wtf(viewHolderFactoryData.getLogTag(), "Unable to get the layout inflater. " +
                    viewHolderFactoryData, ae);
        } catch (Resources.NotFoundException nfe) {
            Log.w(viewHolderFactoryData.getLogTag(), "Unable to find the resource. " +
                    viewHolderFactoryData, nfe);
        } catch (NullPointerException npe) {
            Log.w(viewHolderFactoryData.getLogTag(), "The brick is null and shouldn't be. " +
                    viewHolderFactoryData, npe);
        }

        // Since the view holder could be null, if it is, create an empty version and return
        // its reference instead of returning a null object reference.
        return null == viewHolder ? createEmptyBrickViewHolder(context) : viewHolder;
    }

    /**
     * Creates an {@link com.wayfair.brickkit.viewholder.EmptyBrickViewHolder }
     *
     * @param context used to create the {@link EmptyBrickView}
     * @return the newly created EmptyBrickViewHolder
     */
    /* private package */
    BrickViewHolder createEmptyBrickViewHolder(@NonNull Context context) {
        EmptyBrickView emptyBrickView = new EmptyBrickView(context);
        return new EmptyBrickViewHolder(emptyBrickView);
    }

    /**
     * Creates a {@link BrickViewHolder} based on the {@param #viewType}.
     *
     * @param parent   of the view, passed into the {@link BrickViewHolder}'s constructor.
     * @param viewType of the view to create.  It can be a @LayoutRes id.
     * @param provider provides bricks, based on resource id
     * @return a newly created {@link BrickViewHolder}
     * @throws AssertionError              thrown when the layout inflater can't be created
     * @throws Resources.NotFoundException when the view type is zero or the resource cannot be
     *                                     located
     * @throws NullPointerException        in cases, such as brick creation. (Just a fail safe)
     */
    /* private package */
    BrickViewHolder createViewHolderWithViewType(@NonNull ViewGroup parent,
                                                 int viewType,
                                                 @NonNull BrickProvider provider)
            throws AssertionError, Resources.NotFoundException, NullPointerException {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(viewType, parent, false);
        BaseBrick brick = provider.brickWithLayout(viewType);
        brick = brick == null ? provider.brickWithPlaceholderLayout(viewType) : brick;

        // Since the brick could be null, if it is, create an empty brick view holder and
        // return its reference to avoid null pointer exceptions being thrown.
        return null == brick ?
                createEmptyBrickViewHolder(context) :
                brick.createViewHolder(itemView);
    }
}
