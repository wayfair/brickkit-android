/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.brick;

import android.view.View;
import android.widget.TextView;

import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkit.viewholder.BrickViewHolder;
import com.wayfair.brickkit.R;

/**
 * Simple Brick with a single text view.
 */
public class TextBrick extends BaseBrick {
    private final CharSequence text;
    private Runnable onDismiss;

    /**
     * Constructor which uses the default padding.
     *
     * @param spanSize size information for this brick
     * @param text text to display on this brick
     */
    public TextBrick(BrickSize spanSize, CharSequence text) {
        super(spanSize);
        this.text = text;
    }

    /**
     * Constructor.
     *
     * @param spanSize size information for this brick
     * @param padding padding for this brick
     * @param text text to display on this brick
     */
    public TextBrick(BrickSize spanSize, BrickPadding padding, CharSequence text) {
        super(spanSize, padding);
        this.text = text;
    }

    @Override
    public void onBindData(BrickViewHolder viewHolder) {
        TextViewHolder editTextViewHolder = (TextViewHolder) viewHolder;
        editTextViewHolder.textView.setText(text);
    }

    @Override
    public int getLayout() {
        return R.layout.text_brick;
    }

    @Override
    public BrickViewHolder createViewHolder(View itemView) {
        return new TextViewHolder(itemView);
    }

    @Override
    public void dismissed(int direction) {
        if (onDismiss != null) {
            onDismiss.run();
        }
    }

    /**
     * This sets the runnable that will be executed by swiping the brick away.
     *
     * @param onDismiss The runnable to be run.
     */
    public void setOnDismiss(Runnable onDismiss) {
        this.onDismiss = onDismiss;
    }

    /**
     * Get the current text in the brick.
     *
     * @return The current text in the brick.
     */
    public CharSequence getText() {
        return text;
    }

    /**
     * {@link BrickViewHolder} for TextBrick.
     */
    static class TextViewHolder extends BrickViewHolder {
        TextView textView;

        /**
         * Constructor for TextViewHolder.
         *
         * @param itemView view for this brick
         */
        TextViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
