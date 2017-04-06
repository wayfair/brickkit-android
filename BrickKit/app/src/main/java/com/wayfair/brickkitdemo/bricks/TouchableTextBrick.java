package com.wayfair.brickkitdemo.bricks;

import android.content.Context;
import android.view.View;

import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.brick.TextBrick;
import com.wayfair.brickkit.brick.TouchableBrick;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;

/**
 * A simple text brick that is touchable and accepts an OnClickListener.
 */
public class TouchableTextBrick extends TextBrick implements TouchableBrick {
    private View.OnClickListener listener;


    /**
     * Constructor.
     *
     * @param context context this brick exists in
     * @param spanSize size information for this brick
     * @param padding padding for this brick
     * @param text text to display on this brick
     * @param listener the listener to call when the brick is touched
     */
    public TouchableTextBrick(Context context, BrickSize spanSize, BrickPadding padding, CharSequence text, View.OnClickListener listener) {
        super(context, spanSize, padding, text);
        this.listener = listener;
    }

    @Override
    public void onBindData(BrickViewHolder viewHolder) {
        super.onBindData(viewHolder);
        viewHolder.itemView.setOnClickListener(onTouch());
    }

    @Override
    public View.OnClickListener onTouch() {
        return listener;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
