/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.behavior;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.BrickRecyclerAdapter;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.R;
import com.wayfair.brickkit.StickyScrollMode;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPadding;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class StickyHeaderBehaviorTest {

    private BrickDataManager dataManager;
    private TestStickyHeaderBehavior headerBehavior;
    private RecyclerView recyclerView;
    private BrickRecyclerAdapter adapter;
    private ViewGroup stickyHolderLayout;
    private ViewGroup stickyHolderContainer;
    private Context context;
    private View view;
    private BrickViewHolder stickyViewHolder;
    private View itemView;
    private static int MOCK_VIEW_SIZE = 10;
    private static int ADAPTER_COUNT = 10;
    private static int HEADER_INDEX = 1;
    private static int SCROLL_DISTANCE = 10;
    private static int BOUNDARY_AXIS = 1;
    private static String TEST = "TEXT";

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        context = InstrumentationRegistry.getTargetContext();
        recyclerView = mock(RecyclerView.class);
        adapter = mock(BrickRecyclerAdapter.class);
        view = mock(View.class);
        dataManager = mock(BrickDataManager.class);

        when(dataManager.getBrickRecyclerAdapter()).thenReturn(adapter);
        when(adapter.getRecyclerView()).thenReturn(recyclerView);

        itemView = new TextView(context);
        ((TextView) itemView).setText(TEST);
        itemView.measure(MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);

        stickyViewHolder = new BrickViewHolder(itemView);

        stickyHolderContainer = spy((ViewGroup) LayoutInflater.from(context).inflate(R.layout.vertical_header, new LinearLayout(context), false));
        stickyHolderContainer.layout(0, 0, MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        stickyHolderContainer.setLayoutParams(new ViewGroup.LayoutParams(MOCK_VIEW_SIZE, MOCK_VIEW_SIZE));

        View header = spy((ViewGroup) LayoutInflater.from(context).inflate(R.layout.text_brick, new LinearLayout(context), false));
        header.layout(0, 0, MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        header.setLayoutParams(new ViewGroup.LayoutParams(MOCK_VIEW_SIZE, MOCK_VIEW_SIZE));

        stickyHolderLayout = ((ViewGroup) stickyHolderContainer.findViewById(R.id.header_sticky_layout));
        stickyHolderLayout.addView(header);

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager, stickyHolderContainer));
        headerBehavior.swapStickyView(stickyViewHolder);
        headerBehavior.translateStickyView();
        headerBehavior.setStickyBackgroundColor(Color.WHITE);
    }

    @Test
    public void testSimpleHeaderBehaviour() {
        StickyHeaderBehavior headerBehavior = spy(new TestStickyHeaderBehavior(dataManager));
        headerBehavior.onScroll();
    }

    @Test
    public void testGetStickyViewPosition() {
        when(adapter.getRecyclerView().getChildCount() - 1).thenReturn(ADAPTER_COUNT);
        when(adapter.getRecyclerView().getChildAt(ADAPTER_COUNT)).thenReturn(view);
        when(adapter.getRecyclerView().getChildAdapterPosition(view)).thenReturn(HEADER_INDEX);
        when(adapter.getSectionHeader(HEADER_INDEX)).thenReturn(null);

        int position = headerBehavior.getStickyViewPosition(RecyclerView.NO_POSITION);
        assertEquals(position, RecyclerView.NO_POSITION);

        BaseBrick header = mock(BaseBrick.class);
        when(adapter.getSectionHeader(HEADER_INDEX)).thenReturn(header);
        when(header.getStickyScrollMode()).thenReturn(StickyScrollMode.SHOW_ON_SCROLL_UP);
        when(adapter.indexOf(header)).thenReturn(HEADER_INDEX);

        position = headerBehavior.getStickyViewPosition(HEADER_INDEX);
        assertEquals(position, HEADER_INDEX);
    }

    @Test
    public void testStickyViewFadeTranslate() {
        BaseBrick header = mock(BaseBrick.class);
        when(adapter.getSectionHeader(HEADER_INDEX)).thenReturn(header);
        when(header.getStickyScrollMode()).thenReturn(StickyScrollMode.SHOW_ON_SCROLL_UP);
        when(adapter.indexOf(header)).thenReturn(HEADER_INDEX);
        headerBehavior.getStickyViewPosition(HEADER_INDEX);

        headerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
        assertEquals(0f, stickyHolderContainer.getY());

        headerBehavior.stickyViewFadeTranslate(-SCROLL_DISTANCE);
        assertEquals(-10f, stickyHolderContainer.getY());

        when(header.getStickyScrollMode()).thenReturn(StickyScrollMode.SHOW_ON_SCROLL_DOWN);
        headerBehavior.getStickyViewPosition(HEADER_INDEX);
        headerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
        assertEquals(-10f, stickyHolderContainer.getY());

        headerBehavior.stickyViewFadeTranslate(-SCROLL_DISTANCE);
        assertEquals(0f, stickyHolderContainer.getY());

        stickyHolderContainer.layout(BOUNDARY_AXIS, BOUNDARY_AXIS, BOUNDARY_AXIS, BOUNDARY_AXIS);
        headerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
        assertEquals(1f, stickyHolderContainer.getY());

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager, stickyHolderContainer));
        headerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
    }

    @Test
    public void testTranslateStickyView() {
        when(adapter.getRecyclerView().getChildAt(HEADER_INDEX)).thenReturn(null);
        headerBehavior.translateStickyView();
        verify(recyclerView, atLeastOnce()).getChildAt(HEADER_INDEX);
        assertNull(recyclerView.getChildAt(HEADER_INDEX));

        View textView = new TextView(context);
        when(adapter.getRecyclerView().getChildAt(HEADER_INDEX)).thenReturn(textView);
        when(adapter.getRecyclerView().getChildAdapterPosition(textView)).thenReturn(-RecyclerView.NO_POSITION);
        headerBehavior.translateStickyView();
        verify(headerBehavior, atLeastOnce()).getStickyViewPosition(-RecyclerView.NO_POSITION);

        when(adapter.getRecyclerView().getChildAdapterPosition(textView)).thenReturn(HEADER_INDEX);
        BaseBrick header = mock(BaseBrick.class);
        when(adapter.getSectionHeader(HEADER_INDEX)).thenReturn(header);
        when(adapter.indexOf(header)).thenReturn(HEADER_INDEX);
        textView.layout(BOUNDARY_AXIS, 0, MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        headerBehavior.translateStickyView();
        verify(headerBehavior, atLeastOnce()).getStickyViewPosition(HEADER_INDEX);

        textView.layout(-BOUNDARY_AXIS, BOUNDARY_AXIS, MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        headerBehavior.translateStickyView();
        assertEquals(-BOUNDARY_AXIS, textView.getLeft());

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        when(recyclerView.getLayoutManager()).thenReturn(layoutManager);
        headerBehavior.translateStickyView();
        assertEquals(OrientationHelper.VERTICAL, ((LinearLayoutManager)recyclerView.getLayoutManager()).getOrientation());

        textView.layout(-BOUNDARY_AXIS, -BOUNDARY_AXIS, MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        headerBehavior.translateStickyView();
        assertEquals(-BOUNDARY_AXIS, textView.getTop());

        when(adapter.getRecyclerView()). thenReturn(null);
        headerBehavior.translateStickyView();
        assertNull(adapter.getRecyclerView());

        when(dataManager.getBrickRecyclerAdapter()).thenReturn(null);
        headerBehavior.translateStickyView();
        assertNull(dataManager.getBrickRecyclerAdapter());
    }

    @Test
    public void testOnDataSetChanged() {
        headerBehavior.onDataSetChanged();
        verify(headerBehavior).onDataSetChanged();
    }

    @Test
    public void testAttachToRecyclerView() {
        when(adapter.getRecyclerView()).thenReturn(null);
        headerBehavior.attachToRecyclerView(recyclerView);
        verify(headerBehavior).attachToRecyclerView(recyclerView);

        when(dataManager.getBrickRecyclerAdapter()).thenReturn(null);
        headerBehavior.attachToRecyclerView(recyclerView);
        verify(headerBehavior, atLeastOnce()).attachToRecyclerView(recyclerView);
    }

    @Test
    public void testOnScroll() {
        headerBehavior.onScroll();
        verify(headerBehavior).onScroll();

        headerBehavior.onScroll();
        verify(headerBehavior, atLeastOnce()).onScroll();

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager, null));
        headerBehavior.onScroll();
        verify(headerBehavior).onScroll();

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager));
        View recyclerViewParent = mock(View.class);
        when(dataManager.getRecyclerViewParent()).thenReturn(recyclerViewParent);
        when((recyclerViewParent).findViewById(R.id.header_sticky_container)).thenReturn(stickyHolderContainer);
        ImageView imageView = mock(ImageView.class);
        when((stickyHolderContainer).findViewById(R.id.header_sticky_layout)).thenReturn(stickyHolderLayout);
        when((stickyHolderContainer).findViewById(R.id.header_bar_shadow)).thenReturn(imageView);
        headerBehavior.onScroll();

        when(dataManager.getRecyclerViewParent()).thenReturn(null);
        headerBehavior.onScroll();
        assertNull(dataManager.getRecyclerViewParent());

        when(adapter.getRecyclerView()).thenReturn(null);
        headerBehavior.onScroll();
        assertNull(adapter.getRecyclerView());
    }

    @Test
    public void testOnScrolled() {
        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager, stickyHolderContainer));
        headerBehavior.onScrolled(recyclerView, 0, 0);

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager));
        View recyclerViewParent = mock(View.class);
        when(dataManager.getRecyclerViewParent()).thenReturn(recyclerViewParent);
        when((recyclerViewParent).findViewById(R.id.header_sticky_container)).thenReturn(stickyHolderContainer);
        ImageView imageView = mock(ImageView.class);
        when((stickyHolderContainer).findViewById(R.id.header_sticky_layout)).thenReturn(stickyHolderLayout);
        when((stickyHolderContainer).findViewById(R.id.header_bar_shadow)).thenReturn(imageView);
        headerBehavior.onScroll();
        headerBehavior.onScrolled(recyclerView, BOUNDARY_AXIS, BOUNDARY_AXIS);
        assertEquals(stickyHolderLayout.getTop(), (int)stickyHolderLayout.getY());

        stickyHolderLayout.setTranslationY(2f);
        headerBehavior.onScroll();
        headerBehavior.onScrolled(recyclerView, BOUNDARY_AXIS, BOUNDARY_AXIS);
        assertEquals(stickyHolderLayout.getTop() + 2f, stickyHolderLayout.getY());

    }

    @Test
    public void testSwapStickyView() {
        headerBehavior.swapStickyView(null);
        verify(headerBehavior).swapStickyView(null);
    }

    @Test
    public void testDetachFromRecyclerView() {
        headerBehavior.attachToRecyclerView(recyclerView);
        headerBehavior.detachFromRecyclerView(recyclerView);
        verify(headerBehavior, atLeastOnce()).detachFromRecyclerView(recyclerView);

        headerBehavior.attachToRecyclerView(recyclerView);
        when(adapter.getRecyclerView()).thenReturn(null);
        headerBehavior.detachFromRecyclerView(recyclerView);
        verify(headerBehavior, atLeastOnce()).detachFromRecyclerView(recyclerView);

        when(dataManager.getBrickRecyclerAdapter()).thenReturn(null);
        headerBehavior.detachFromRecyclerView(recyclerView);
        verify(headerBehavior, atLeastOnce()).detachFromRecyclerView(recyclerView);
    }

    @Test
    public void testUpdateOrClearStickyView() {
        //have to set up stickyLayoutBottomLine
        LinkedList<BaseBrick> bricks = mock(LinkedList.class);
        headerBehavior.onScroll();
        assertEquals(stickyHolderLayout, headerBehavior.getStickyHolderLayout());
        when(dataManager.getDataManagerItems()).thenReturn(bricks);
        when(bricks.size()).thenReturn(ADAPTER_COUNT);
        when(recyclerView.getChildCount()).thenReturn(ADAPTER_COUNT);
        doReturn(RecyclerView.NO_POSITION).when(headerBehavior).getStickyViewPosition(RecyclerView.NO_POSITION);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);
        assertEquals(RecyclerView.NO_POSITION, headerBehavior.getStickyViewPosition(RecyclerView.NO_POSITION));

        doReturn(HEADER_INDEX).when(headerBehavior).getStickyViewPosition(RecyclerView.NO_POSITION);
        when(adapter.getItemCount()).thenReturn(0);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);

        doReturn(HEADER_INDEX).when(headerBehavior).getStickyViewPosition(RecyclerView.NO_POSITION);
        when(adapter.getItemCount()).thenReturn(ADAPTER_COUNT);
        when(adapter.getItemViewType(HEADER_INDEX)).thenReturn(HEADER_INDEX);
        when(adapter.onCreateViewHolder(recyclerView, HEADER_INDEX)).thenReturn(stickyViewHolder);
        BrickPadding padding = new BrickPadding(new Rect(1, 1, 1, 1), new Rect(1, 1, 1, 1));
        BaseBrick brick = mock(BaseBrick.class);
        when(dataManager.brickAtPosition(HEADER_INDEX)).thenReturn(brick);
        doReturn(stickyHolderLayout).when(headerBehavior).inflateStickyView(any(BaseBrick.class), any(RecyclerView.class));
        when(brick.createViewHolder(stickyHolderLayout)).thenReturn(stickyViewHolder);
        when(brick.getPadding()).thenReturn(padding);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);
        verify(dataManager.brickAtPosition(HEADER_INDEX), atLeastOnce()).getPadding();

        headerBehavior.onDataSetChanged();
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);

        doReturn(2).when(headerBehavior).getStickyViewPosition(RecyclerView.NO_POSITION);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        when(recyclerView.getLayoutManager()).thenReturn(layoutManager);
        when(adapter.getItemViewType(2)).thenReturn(2);
        when(adapter.onCreateViewHolder(recyclerView, 2)).thenReturn(stickyViewHolder);
        when(dataManager.brickAtPosition(2)).thenReturn(brick);
        when(brick.getPadding()).thenReturn(padding);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);
        verify(dataManager.brickAtPosition(2), atLeastOnce()).getPadding();
        assertEquals(OrientationHelper.VERTICAL, (((LinearLayoutManager)recyclerView.getLayoutManager()).getOrientation()));

        when(adapter.getRecyclerView().getChildCount()).thenReturn(0);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);
        assertEquals(0, adapter.getRecyclerView().getChildCount());

        when(adapter.getRecyclerView()).thenReturn(null);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);
        assertNull(adapter.getRecyclerView());

        when(dataManager.getBrickRecyclerAdapter()).thenReturn(null);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);
        assertNull(dataManager.getBrickRecyclerAdapter());

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager, null));
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);
        assertNull(headerBehavior.getStickyHolderLayout());
    }

    @Test
    public void testGetStickyViewHolder() {
        headerBehavior.getStickyViewHolder();
        verify(headerBehavior).getStickyViewHolder();
    }

    @Test
    public void getStickPosition() {
        headerBehavior.getStickyPosition();
        verify(headerBehavior).getStickyPosition();
    }

    public class TestStickyHeaderBehavior extends StickyHeaderBehavior {
        TestStickyHeaderBehavior(BrickDataManager brickDataManager) {
            super(brickDataManager);
        }

        TestStickyHeaderBehavior(BrickDataManager brickDataManager, ViewGroup stickyHolderContainer) {
            super(brickDataManager, stickyHolderContainer);
        }

        @Override
        protected int getStickyViewPosition(int adapterPosHere) {
            return super.getStickyViewPosition(adapterPosHere);
        }

        @Override
        protected void translateStickyView() {
            super.translateStickyView();
        }

        @Override
        protected void stickyViewFadeTranslate(int dy) {
            super.stickyViewFadeTranslate(dy);
        }

        @Override
        protected void swapStickyView(BrickViewHolder newStickyView) {
            super.swapStickyView(newStickyView);
        }
    }

}


