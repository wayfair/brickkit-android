/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.bricks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wayfair.brickkit.viewholder.BrickViewHolder;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkitdemo.R;

import androidx.annotation.ColorInt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Brick whose content is a fragment.
 */
public class FragmentBrick extends BaseBrick {
    public static final int NO_BG_COLOR = -1;
    private final Context context;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private String tag;
    @ColorInt private int backgroundColor = NO_BG_COLOR;

    /**
     * Constructor.
     *
     * @param context context this brick exists in
     * @param spanSize size information for this brick
     * @param fragmentManager fragmentManager to add the fragment to
     * @param fragment fragment to display in this brick
     * @param tag tag to use in {@link androidx.fragment.app.FragmentTransaction}'s on the fragment
     */
    public FragmentBrick(
            Context context, BrickSize spanSize, FragmentManager fragmentManager, Fragment fragment, String tag
    ) {
        super(spanSize);

        this.context = context;
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        this.tag = tag;
    }

    @Override
    public void onBindData(BrickViewHolder holder) {
        if (holder instanceof FragmentBrickViewHolder) {
            if (backgroundColor != NO_BG_COLOR) {
                holder.itemView.setBackgroundColor(backgroundColor);
            }

            View view;

            if (!fragment.isAdded()) {
                fragmentManager.beginTransaction().add(fragment, tag).commit();
                fragmentManager.executePendingTransactions();
                fragment.onAttach(context);
                view = fragment.onCreateView(
                        LayoutInflater.from(holder.itemView.getContext()),
                        ((FragmentBrickViewHolder) holder).frameLayout,
                        null
                );
            } else {
                view = fragment.getView();
            }

            if (view != null && view.getParent() != null && view.getParent() instanceof ViewGroup) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

            ((FragmentBrickViewHolder) holder).frameLayout.addView(view);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_brick;
    }

    @Override
    public BrickViewHolder createViewHolder(View itemView) {
        return new FragmentBrickViewHolder(itemView);
    }

    /**
     * Set the background color of the nested brick view.
     *
     * @param backgroundColor The color int for the background.
     */
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * {@link BrickViewHolder} for FragmentBrick.
     */
    private static final class FragmentBrickViewHolder extends BrickViewHolder {
        private final FrameLayout frameLayout;

        /**
         * Constructor for FragmentBrickViewHolder.
         *
         * @param itemView view for this brick
         */
        private FragmentBrickViewHolder(View itemView) {
            super(itemView);

            frameLayout = (FrameLayout) itemView.findViewById(R.id.fragment_container);
        }
    }
}