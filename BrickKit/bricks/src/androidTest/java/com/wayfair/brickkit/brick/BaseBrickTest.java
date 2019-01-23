/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.brick;

import android.content.Context;
import android.view.View;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.StickyScrollMode;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkit.size.SimpleBrickSize;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BaseBrickTest {
    private static final int PADDING = 3;
    private Context context;
    private BrickSize brickSize;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        brickSize = mock(BrickSize.class);
    }

    @Test
    public void testTwoArgumentConstructor() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertEquals(brickSize, brick.getSpanSize());
        assertEquals(0, brick.getPadding().getInnerBottomPadding());
    }

    @Test
    public void testThreeArgumentConstructor() {
        BrickPadding padding = mock(BrickPadding.class);
        when(padding.getInnerBottomPadding()).thenReturn(PADDING);

        TestBaseBrick brick = new TestBaseBrick(context, brickSize, padding);

        assertEquals(brickSize, brick.getSpanSize());
        assertEquals(PADDING, brick.getPadding().getInnerBottomPadding());
    }

    @Test
    public void testHidden() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isHidden());

        brick.setHidden(true);
        assertTrue(brick.isHidden());
    }

    @Test
    public void testHeader() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isHeader());

        brick.setHeader(true);
        assertTrue(brick.isHeader());
    }

    @Test
    public void testFooter() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isFooter());

        brick.setFooter(true);
        assertTrue(brick.isFooter());
    }

    @Test
    public void testStickyScrollMode() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertEquals(StickyScrollMode.SHOW_ON_SCROLL, brick.getStickyScrollMode());

        brick.setStickyScrollMode(StickyScrollMode.SHOW_ON_SCROLL_DOWN);

        assertEquals(StickyScrollMode.SHOW_ON_SCROLL_DOWN, brick.getStickyScrollMode());

    }

    @Test
    public void testInFirstRow() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isInFirstRow());

        brick.setInFirstRow(true);
        assertTrue(brick.isInFirstRow());
    }

    @Test
    public void testInLastRow() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isInLastRow());

        brick.setInLastRow(true);
        assertTrue(brick.isInLastRow());
    }

    @Test
    public void testOnLeftWall() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isOnLeftWall());

        brick.setOnLeftWall(true);
        assertTrue(brick.isOnLeftWall());
    }

    @Test
    public void testOnRightWall() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isOnRightWall());

        brick.setOnRightWall(true);
        assertTrue(brick.isOnRightWall());
    }

    @Test
    public void testMovedTo() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        brick.movedTo(1);

        // nothing to verify
    }

    @Test
    public void testDismissed() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        brick.dismissed(ItemTouchHelper.UP);

        // nothing to verify
    }

    @Test
    public void testToStringAllOuterPosition1Spans1() {
        LinkedList<BaseBrick> bricks = mock(LinkedList.class);
        when(bricks.indexOf(any(BaseBrick.class))).thenReturn(1);

        BrickDataManager manager = mock(BrickDataManager.class);
        when(manager.getRecyclerViewItems()).thenReturn(bricks);

        BrickSize smallSize = new SimpleBrickSize(1) {
            @Override
            protected int size() {
                return 1;
            }
        };

        BaseBrick brick = new TestBaseBrick(context, smallSize, new TestBrickPadding(1, 2));
        brick.setInFirstRow(true);
        brick.setInLastRow(true);
        brick.setOnLeftWall(true);
        brick.setOnRightWall(true);

        assertEquals("--2--\n| 1 |\n2 1 2\n|   |\n--2--", brick.toString(context, manager));
    }

    @Test
    public void testToStringAllInnerPosition111Spans111() {
        LinkedList<BaseBrick> bricks = mock(LinkedList.class);
        when(bricks.indexOf(any(BaseBrick.class))).thenReturn(111);

        BrickDataManager manager = mock(BrickDataManager.class);
        when(manager.getRecyclerViewItems()).thenReturn(bricks);

        BrickSize smallSize = new SimpleBrickSize(111) {
            @Override
            protected int size() {
                return 111;
            }
        };

        BaseBrick brick = new TestBaseBrick(context, smallSize, new TestBrickPadding(1, 2));
        brick.setInFirstRow(false);
        brick.setInLastRow(false);
        brick.setOnLeftWall(false);
        brick.setOnRightWall(false);

        assertEquals("--1--\n|111|\n11111\n|   |\n--1--", brick.toString(context, manager));
    }

    private static final class TestBaseBrick extends BaseBrick {

        private TestBaseBrick(Context context, BrickSize spanSize, BrickPadding padding) {
            super(spanSize, padding);
        }

        private TestBaseBrick(Context context, BrickSize spanSize) {
            super(spanSize);
        }

        @Override
        public void onBindData(BrickViewHolder holder) {

        }

        @Override
        public int getLayout() {
            return 0;
        }

        @Override
        public int getPlaceholderLayout() {
            return 0;
        }

        @Override
        public BrickViewHolder createViewHolder(View itemView) {
            return null;
        }
    }

    private static final class TestBrickPadding extends InnerOuterBrickPadding {

        /**
         * Constructor for inner and outer padding that are different but symmetrical on all sides.
         *
         * @param innerPadding The inner padding value.
         * @param outerPadding The outer padding value.
         */
        public TestBrickPadding(int innerPadding, int outerPadding) {
            super(innerPadding, outerPadding);
        }
    }
}
