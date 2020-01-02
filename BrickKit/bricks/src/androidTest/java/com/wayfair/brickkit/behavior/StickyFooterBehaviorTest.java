/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.behavior;

import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.BrickRecyclerAdapter;
import com.wayfair.brickkit.viewholder.BrickViewHolder;
import com.wayfair.brickkit.R;
import com.wayfair.brickkit.StickyScrollMode;
import com.wayfair.brickkit.brick.BaseBrick;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class StickyFooterBehaviorTest {

    private BrickDataManager dataManager;
    private TestStickyFooterBehavior footerBehavior;
    private RecyclerView recyclerView;
    private BrickRecyclerAdapter adapter;
    private ViewGroup stickyHolderLayout;
    private ViewGroup stickyContainer;
    private Context context;
    private View view;
    private BrickViewHolder stickyViewHolder;
    private View itemView;
    private static int MOCK_VIEW_SIZE = 10;
    private static int ADAPTER_COUNT = 10;
    private static int FOOTER_INDEX = 10;
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

        footerBehavior = spy(new TestStickyFooterBehavior(dataManager));

        itemView = new TextView(context);
        ((TextView) itemView).setText(TEST);
        itemView.measure(MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        stickyViewHolder = new BrickViewHolder(itemView);

        stickyContainer = spy((ViewGroup) LayoutInflater.from(context).inflate(R.layout.vertical_footer, new LinearLayout(context), false));
        stickyContainer.layout(0, 0, MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        stickyContainer.setLayoutParams(new ViewGroup.LayoutParams(MOCK_VIEW_SIZE, MOCK_VIEW_SIZE));

        stickyHolderLayout = spy((ViewGroup) LayoutInflater.from(context).inflate(R.layout.text_brick, new LinearLayout(context), false));
        stickyHolderLayout.layout(0, 0, MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        stickyHolderLayout.setLayoutParams(new ViewGroup.LayoutParams(MOCK_VIEW_SIZE, MOCK_VIEW_SIZE));

        footerBehavior = spy(new TestStickyFooterBehavior(dataManager, stickyContainer));
        footerBehavior.swapStickyView(stickyViewHolder);
        footerBehavior.translateStickyView();
    }

    @Test
    public void testSimpleHeaderBehaviour() {
        StickyFooterBehavior footerBehavior = spy(new TestStickyFooterBehavior(dataManager));
        footerBehavior.onScroll();
    }

    @Test
    public void testSimpleHeaderBehaviourWithContatiner() {
        StickyFooterBehavior footerBehavior = spy(new TestStickyFooterBehavior(dataManager, stickyContainer));
        footerBehavior.onScroll();
    }

    @Test
    public void testGetStickyViewPosition() {
        when(adapter.getRecyclerView().getChildCount() - 1).thenReturn(ADAPTER_COUNT);
        when(adapter.getRecyclerView().getChildAt(ADAPTER_COUNT)).thenReturn(view);
        when(adapter.getRecyclerView().getChildAdapterPosition(view)).thenReturn(FOOTER_INDEX);
        when(adapter.getSectionFooter(FOOTER_INDEX)).thenReturn(null);

        int position = footerBehavior.getStickyViewPosition(RecyclerView.NO_POSITION);
        assertEquals(RecyclerView.NO_POSITION, position);

        BaseBrick footer = mock(BaseBrick.class);
        when(adapter.getSectionFooter(FOOTER_INDEX)).thenReturn(footer);
        when(footer.getStickyScrollMode()).thenReturn(StickyScrollMode.SHOW_ON_SCROLL_UP);
        when(adapter.indexOf(footer)).thenReturn(FOOTER_INDEX);

        position = footerBehavior.getStickyViewPosition(FOOTER_INDEX);
        assertEquals(FOOTER_INDEX, position);
    }

    @Test
    public void testStickyViewFadeTranslate() {
        BaseBrick footer = mock(BaseBrick.class);
        when(adapter.getSectionFooter(FOOTER_INDEX)).thenReturn(footer);
        when(footer.getStickyScrollMode()).thenReturn(StickyScrollMode.SHOW_ON_SCROLL_UP);
        when(adapter.indexOf(footer)).thenReturn(FOOTER_INDEX);
        footerBehavior.getStickyViewPosition(FOOTER_INDEX);

        footerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
        assertEquals(0f, stickyContainer.getY());

        footerBehavior.stickyViewFadeTranslate(-SCROLL_DISTANCE);
        assertEquals(10f, stickyContainer.getY());

        when(footer.getStickyScrollMode()).thenReturn(StickyScrollMode.SHOW_ON_SCROLL_DOWN);
        footerBehavior.getStickyViewPosition(FOOTER_INDEX);
        footerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
        assertEquals(10f, stickyContainer.getY());

        footerBehavior.stickyViewFadeTranslate(-SCROLL_DISTANCE);
        assertEquals(0f, stickyContainer.getY());

        stickyContainer.layout(BOUNDARY_AXIS, BOUNDARY_AXIS, BOUNDARY_AXIS, BOUNDARY_AXIS);
        footerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
        assertEquals(1f, stickyContainer.getY());

        footerBehavior = new TestStickyFooterBehavior(dataManager, stickyContainer);
        footerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
    }

    @Test
    public void testTranslateStickyView() {
        when(adapter.getRecyclerView().getChildCount()).thenReturn(ADAPTER_COUNT);
        when(adapter.getItemCount()).thenReturn(ADAPTER_COUNT);
        when(adapter.getRecyclerView().getChildAt(8)).thenReturn(null);
        footerBehavior.translateStickyView();
        verify(recyclerView).getChildAt(8);
        assertNull(recyclerView.getChildAt(8));

        View textView = new TextView(context);
        when(adapter.getRecyclerView().getChildAt(8)).thenReturn(textView);
        when(adapter.getRecyclerView().getChildAdapterPosition(textView)).thenReturn(RecyclerView.NO_POSITION);
        footerBehavior.translateStickyView();
        verify(footerBehavior, atLeastOnce()).getStickyViewPosition(RecyclerView.NO_POSITION);

        when(adapter.getRecyclerView().getChildAdapterPosition(textView)).thenReturn(FOOTER_INDEX);
        BaseBrick footer = mock(BaseBrick.class);
        when(adapter.getSectionFooter(FOOTER_INDEX)).thenReturn(footer);
        when(adapter.indexOf(footer)).thenReturn(FOOTER_INDEX);
        textView.layout(-BOUNDARY_AXIS, -BOUNDARY_AXIS, -BOUNDARY_AXIS, -BOUNDARY_AXIS);
        footerBehavior.translateStickyView();
        verify(footerBehavior, atLeastOnce()).getStickyViewPosition(FOOTER_INDEX);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        when(recyclerView.getLayoutManager()).thenReturn(layoutManager);
        footerBehavior.translateStickyView();
        assertEquals(((LinearLayoutManager)recyclerView.getLayoutManager()).getOrientation(), OrientationHelper.VERTICAL);

        when(adapter.getRecyclerView()). thenReturn(null);
        footerBehavior.translateStickyView();
        assertNull(adapter.getRecyclerView());

        when(dataManager.getBrickRecyclerAdapter()).thenReturn(null);
        footerBehavior.translateStickyView();
        assertNull(dataManager.getBrickRecyclerAdapter());
    }

    public class TestStickyFooterBehavior extends StickyFooterBehavior {
        TestStickyFooterBehavior(BrickDataManager brickDataManager) {
            super(brickDataManager);
        }

        TestStickyFooterBehavior(BrickDataManager brickDataManager, ViewGroup stickyHolderContainer) {
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
        protected  void swapStickyView(BrickViewHolder newStickyView) {
             super.swapStickyView(newStickyView);
        }
    }

}


