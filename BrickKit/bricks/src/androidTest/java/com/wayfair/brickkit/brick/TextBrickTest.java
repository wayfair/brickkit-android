/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.brick;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class TextBrickTest {
    private static final String TEXT = "text";
    private Context context;
    private BrickSize brickSize;
    private BrickPadding brickPadding;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        brickSize = mock(BrickSize.class);
        brickPadding = mock(BrickPadding.class);
    }

    @Test
    public void testThreeArgumentConstructor() {
        assertNotNull(new TextBrick(brickSize, TEXT));
    }

    @Test
    public void testFourArgumentConstructor() {
        assertNotNull(new TextBrick(brickSize, brickPadding, TEXT));
    }

    @Test
    public void testOnBindData() {
        TextBrick brick = new TextBrick(brickSize, TEXT);

        View itemView = LayoutInflater.from(context).inflate(brick.getLayout(), new LinearLayout(context), false);

        TextBrick.TextViewHolder holder = (TextBrick.TextViewHolder) brick.createViewHolder(itemView);

        brick.onBindData(holder);

        assertEquals(TEXT, holder.textView.getText().toString());
        assertEquals(TEXT, brick.getText());
    }

    @Test
    public void testDismissed() {
        Runnable runnable = mock(Runnable.class);

        TextBrick brick = new TextBrick(brickSize, TEXT);

        brick.dismissed();

        brick.setOnDismiss(runnable);

        brick.dismissed();

        verify(runnable).run();
    }
}
