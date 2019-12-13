/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkitdemo.bricks;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.brick.TouchableBrick;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkitdemo.R;

/**
 * {@link com.wayfair.brickkit.brick.TouchableBrick} used in {@link com.wayfair.brickkitdemo.MainActivityFragment} to
 * link to other fragments.
 */
public class UsedBrick extends BaseBrick implements TouchableBrick {

    private CharSequence text;
    private View.OnClickListener onTouch;

    /**
     * Constructor for UsedBrick.
     *
     * @param spanSize size information for this brick
     * @param padding  padding for this brick
     * @param onTouch  listener for click events on this brick
     * @param text     the text to display in brick
     */
    public UsedBrick(BrickSize spanSize, BrickPadding padding, CharSequence text, View.OnClickListener onTouch) {
        super(spanSize, padding);
        this.onTouch = onTouch;
        this.text = text;
    }

    /**
     * Constructor for UsedBrick.
     *
     * @param spanSize size information for this brick
     * @param padding  padding for this brick
     * @param onTouch  listener for click events on this brick
     */
    public UsedBrick(BrickSize spanSize, BrickPadding padding, View.OnClickListener onTouch) {
        this(spanSize, padding, null, onTouch);
    }

    /**
     * Sets the text to be displayed in brick.
     *
     * @param text the text to be set
     */
    public void setText(CharSequence text) {
        this.text = text;
    }

    @Override
    public void onBindData(final BrickViewHolder viewHolder) {
        if (viewHolder instanceof UsedBrickHolder) {
            UsedBrickHolder holder = (UsedBrickHolder) viewHolder;
            holder.textView.setText(text);
            if (isEnabled()) {
                holder.itemView.setOnClickListener(onTouch());
            }
        }
    }

    @Override
    public void onBindPlaceholder(BrickViewHolder holder) {
        // Here we would modify placeholder views dimensions if necessary
    }

    @Override
    public int getLayout() {
        return R.layout.used_brick;
    }

    @Override
    public int getPlaceholderLayout() {
        return R.layout.used_brick_placeholder;
    }

    @Override
    public BrickViewHolder createViewHolder(View itemView) {
        return new UsedBrickHolder(itemView);
    }

    @Override
    public boolean isDataReady() {
        return !TextUtils.isEmpty(text);
    }

    @Override
    public View.OnClickListener onTouch() {
        return onTouch;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * {@link BrickViewHolder} for UsedBrick.
     */
    private static class UsedBrickHolder extends BrickViewHolder {
        private final TextView textView;

        /**
         * Constructor for UsedBrickHolder.
         *
         * @param itemView view for this brick
         */
        UsedBrickHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.label);
        }
    }
}
