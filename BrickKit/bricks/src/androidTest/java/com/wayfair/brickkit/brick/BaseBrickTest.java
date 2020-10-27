/*
 * Copyright © 2017-2020 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.brick;

import android.view.View;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.viewholder.BrickViewHolder;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BaseBrickTest {
    private static final int PADDING = 3;
    private final BrickSize brickSize = mock(BrickSize.class);
    private final BrickDataManager brickDataManager = mock(BrickDataManager.class);
    private BaseBrick brick = new TestBaseBrick(brickSize);

    @Test
    public void testBrickSizeBrickPaddingConstructor() {
        BrickPadding padding = mock(BrickPadding.class);
        when(padding.getInnerBottomPadding()).thenReturn(PADDING);

        TestBaseBrick brick = new TestBaseBrick(brickSize, padding);

        assertEquals(brickSize, brick.getSpanSize());
        assertEquals(PADDING, brick.getPadding().getInnerBottomPadding());
    }

    @Test
    public void testHidden() {
        assertFalse(brick.isHidden());

        brick.setHidden(true);
        assertTrue(brick.isHidden());
    }

    @Test
    public void testInFirstRow() {
        assertFalse(brick.isInFirstRow());

        brick.setInFirstRow(true);
        assertTrue(brick.isInFirstRow());
    }

    @Test
    public void testInLastRow() {
        assertFalse(brick.isInLastRow());

        brick.setInLastRow(true);
        assertTrue(brick.isInLastRow());
    }

    @Test
    public void testOnLeftWall() {
        assertFalse(brick.isOnLeftWall());

        brick.setOnLeftWall(true);
        assertTrue(brick.isOnLeftWall());
    }

    @Test
    public void testOnRightWall() {
        assertFalse(brick.isOnRightWall());

        brick.setOnRightWall(true);
        assertTrue(brick.isOnRightWall());
    }

    @Test
    public void testRefreshItem() {
        brick.refreshItem();

        brick.setDataManager(brickDataManager);

        brick.refreshItem();
        verify(brickDataManager).refreshItem(brick);
    }

    @Test
    public void testSmoothScroll() {
        brick.smoothScroll();

        brick.setDataManager(brickDataManager);

        brick.smoothScroll();
        verify(brickDataManager).smoothScrollToBrick(brick);
    }

    @Test
    public void testAddLastTo() {
        brick.addLastTo(brickDataManager);

        verify(brickDataManager).addLast(brick);
    }

    @Test
    public void testAddFirstTo() {
        brick.addFirstTo(brickDataManager);

        verify(brickDataManager).addFirst(brick);
    }

    private static final class TestBaseBrick extends BaseBrick {

        private TestBaseBrick(BrickSize spanSize, BrickPadding padding) {
            super(spanSize, padding);
        }

        private TestBaseBrick(BrickSize spanSize) {
            super(spanSize);
        }

        @Override
        public void onBindData(BrickViewHolder holder) { }

        @Override
        public int getLayout() {
            return 0;
        }

        @Override
        public BrickViewHolder createViewHolder(View itemView) {
            return null;
        }
    }
}
