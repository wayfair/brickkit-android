/**
 * Copyright © 2017 Wayfair. All rights reserved.
 */
package com.wayfair.brickkit.behavior;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.BrickRecyclerAdapter;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.StickyScrollMode;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPadding;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Abstract parent for {@link StickyHeaderBehavior} and {@link StickyFooterBehavior}. This class contains
 * the logic for managing the sticky view and updating it when appropriate.
 */
abstract class StickyViewBehavior extends BrickBehavior {
    private boolean dataSetChanged;
    protected BrickDataManager brickDataManager;
    ViewGroup stickyHolderContainer;
    ViewGroup stickyHolderLayout;
    protected ImageView stickyHolderShadowImage;
    int stickyPosition = RecyclerView.NO_POSITION;
    BrickViewHolder stickyViewHolder;
    private int stickyViewContainerId;
    private int stickyViewLayoutId;
    private int stickyShadowImageId;
    @StickyScrollMode
    int stickyScrollMode = StickyScrollMode.SHOW_ON_SCROLL;
    @ColorInt
    private int stickyBackgroundColor = Color.TRANSPARENT;

    /**
     * Constructor.
     *
     * @param brickDataManager      {@link BrickDataManager} whose adapter is used for finding bricks.
     * @param stickyViewContainerId The ID of the wrapping view containing the layout view and the shadow.
     * @param stickyViewLayoutId    The ID of the view that gets the contents of the sticky item.
     * @param stickyShadowImageId   The ID of the shadow image view to accent the sticky view.
     */
    StickyViewBehavior(BrickDataManager brickDataManager, int stickyViewContainerId, int stickyViewLayoutId, int stickyShadowImageId) {
        this.stickyViewContainerId = stickyViewContainerId;
        this.stickyViewLayoutId = stickyViewLayoutId;
        this.stickyShadowImageId = stickyShadowImageId;
        this.brickDataManager = brickDataManager;
        attachToRecyclerView(brickDataManager.getRecyclerView());
    }

    /**
     * Constructor for Unit Tests.
     *
     * @param brickDataManager      {@link BrickDataManager} whose adapter is used for finding bricks
     * @param stickyHolderContainer sticky layout needed for the behavior
     * @param stickyViewLayoutId    The ID of the view that gets the contents of the sticky item.
     * @param stickyShadowImageId   The ID of the shadow image view to accent the sticky view.
     */
    StickyViewBehavior(BrickDataManager brickDataManager, ViewGroup stickyHolderContainer, int stickyViewLayoutId, int stickyShadowImageId) {
        this.stickyHolderContainer = stickyHolderContainer;

        if (stickyHolderContainer != null) {
            this.stickyHolderLayout = (ViewGroup) stickyHolderContainer.findViewById(stickyViewLayoutId);
            this.stickyHolderShadowImage = (ImageView) stickyHolderContainer.findViewById(stickyShadowImageId);
        }

        this.brickDataManager = brickDataManager;
        attachToRecyclerView(brickDataManager.getRecyclerView());
    }

    /**
     * Get the sticky view based off of this position.
     *
     * @param adapterPosHere Adapter position to start search from for sticky view
     * @return sticky view for this position if one exists, null otherwise
     */
    protected abstract int getStickyViewPosition(int adapterPosHere);

    /**
     * Translate the stickyView based off of the translation of the RecyclerView.
     */
    protected abstract void translateStickyView();

    @Override
    public void onDataSetChanged() {
        dataSetChanged = true;
        updateOrClearStickyView(true);
    }

    @Override
    public void onScroll() {
        BrickRecyclerAdapter adapter = brickDataManager.getBrickRecyclerAdapter();
        if (stickyHolderContainer == null && adapter.getRecyclerView() != null && brickDataManager.getRecyclerViewParent() != null) {
            stickyHolderContainer = (ViewGroup) (brickDataManager.getRecyclerViewParent()).findViewById(stickyViewContainerId);
        }

        //Initialize Holder Layout and show sticky view if exists already, the null condition for holder layout is for the unit tests.
        if (stickyHolderContainer != null && stickyHolderLayout == null) {
            stickyHolderLayout = (ViewGroup) stickyHolderContainer.findViewById(stickyViewLayoutId);
            stickyHolderLayout.setBackgroundColor(stickyBackgroundColor);
        }

        if (stickyHolderContainer != null && stickyHolderShadowImage == null) {
            stickyHolderShadowImage = (ImageView) stickyHolderContainer.findViewById(stickyShadowImageId);
        }

        if (stickyHolderLayout != null) {
            if (stickyHolderLayout.getLayoutParams() == null) {
                stickyHolderLayout.setLayoutParams(
                        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //TODO: Animate Layout change when attach and detach
            }
            updateOrClearStickyView(dataSetChanged);
        } else {
            Log.w(this.getClass().getSimpleName(), "WARNING! ViewGroup for Sticky View unspecified! You must include a view with a sticky layout");
        }
    }

    @Override
    protected boolean attach(RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.addOnScrollListener(this);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    onScroll();
                }
            });
        }

        return true;
    }

    @Override
    protected boolean detach(RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.removeOnScrollListener(this);
            clearStickyView();
        }

        return true;
    }

    /**
     * Fade in/out the stickyView based on stickScrollMode{@link com.wayfair.brickkit.StickyScrollMode}.
     *
     * @param dy scrolled distance on axis y
     */
    protected abstract void stickyViewFadeTranslate(int dy);

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (Math.abs(dx) + Math.abs(dy) != 0) {
            updateOrClearStickyView(dataSetChanged);
        }

        stickyViewFadeTranslate(dy);
    }

    /**
     * Getter for sticky view holder.
     *
     * @return BrickViewHolder
     */
    public BrickViewHolder getStickyViewHolder() {
        return stickyViewHolder;
    }

    /**
     * Getter for stickyPosition.
     *
     * @return sticky position in recycler view
     */
    public int getStickyPosition() {
        return stickyPosition;
    }

    /**
     * Getter for stickyHolderLayout.
     *
     * @return stickyHolderLayout of the stickyHolder
     */
    public ViewGroup getStickyHolderLayout() {
        return stickyHolderLayout;
    }

    /**
     * Static helper method to get the orientation of a recycler view.
     *
     * @param recyclerView {@link RecyclerView} whose orientation we are getting
     * @return the orientation of the given RecyclerView if it can be found, LinearLayoutManager.HORIZONTAL otherwise
     */
    static int getOrientation(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation();
        }
        return LinearLayoutManager.HORIZONTAL;
    }

    /**
     * Removes the sticky view from the container and reset the positioning of the container.
     *
     * @param stickyView stickyView to reset
     */
    private static void resetStickyView(RecyclerView.ViewHolder stickyView) {
        final View view = stickyView.itemView;
        removeViewFromParent(view);
        //Reset transformation on removed stickyView
        view.setTranslationX(0);
        view.setTranslationY(0);
        stickyView.setIsRecyclable(true);
    }

    /**
     * Remove the given view from its parent.
     *
     * @param view view to remove from its parent
     */
    private static void removeViewFromParent(final View view) {
        final ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
    }

    /**
     * Replace the current stickyView with the new sticky View.
     *
     * @param newStickyView the new sticky view to use
     */
    protected void swapStickyView(BrickViewHolder newStickyView) {
        if (stickyViewHolder != null) {
            resetStickyView(stickyViewHolder);
        }
        stickyViewHolder = newStickyView;
        if (stickyViewHolder != null) {
            stickyViewHolder.setIsRecyclable(false);
            ensureStickyViewParent();
        }
    }

    /**
     * Makes sure that the current stickyViewHolder is the view in the stickyHolderLayout.
     */
    private void ensureStickyViewParent() {
        final View view = stickyViewHolder.itemView;
        ViewGroup.LayoutParams params = stickyHolderLayout.getLayoutParams();
        params.width = view.getMeasuredWidth();
        params.height = view.getMeasuredHeight();
        removeViewFromParent(view);
        stickyHolderLayout.addView(view);
    }

    /**
     * Remove the current sticky view and reset the stickyViewHolder and stickyPosition.
     */
    private void clearStickyView() {
        if (stickyViewHolder != null) {
            resetStickyView(stickyViewHolder);
            stickyViewHolder = null;
            stickyPosition = RecyclerView.NO_POSITION;
        }

        if (stickyHolderContainer != null) {
            stickyHolderContainer.setVisibility(View.INVISIBLE);
            stickyHolderContainer = null;
        }
    }

    /**
     * Method to make sure the stickyview is up to date or clears it if appropriate.
     *
     * @param updateStickyContent whether or not to force an update to the sticky container content
     */
    private void updateOrClearStickyView(boolean updateStickyContent) {
        BrickRecyclerAdapter adapter = brickDataManager.getBrickRecyclerAdapter();

        if (stickyHolderLayout == null || adapter == null || adapter.getRecyclerView() == null || adapter.getRecyclerView().getChildCount() == 0
            || brickDataManager.getDataManagerItems().size() == 0) {
            clearStickyView();
        } else {
            int stickyPosition = getStickyViewPosition(RecyclerView.NO_POSITION);
            if (stickyPosition >= 0 && stickyPosition < adapter.getItemCount()) {
                updateStickyView(stickyPosition, updateStickyContent);
            } else {
                clearStickyView();
            }
        }
        dataSetChanged = false;
    }

    /**
     * Updates the stickyView. This will replace the sticky view if the position has changed or update the content
     * if the position is teh same but you have flagged to force the update.
     *
     * @param stickyPosition      new sticky position to use for the stickyView
     * @param updateStickyContent whether or not to force a re-bind a of the sticky view holder
     */
    private void updateStickyView(int stickyPosition, boolean updateStickyContent) {
        BrickRecyclerAdapter adapter = brickDataManager.getBrickRecyclerAdapter();

        if (stickyHolderContainer != null) {
            stickyHolderContainer.setVisibility(View.VISIBLE);
        }

        // Check if there is a new sticky view should be sticky
        if (this.stickyPosition != stickyPosition) {
            this.stickyPosition = stickyPosition;
            BrickViewHolder holder = getStickyViewHolder(stickyPosition);
            if (stickyViewHolder != holder) {
                swapStickyView(holder);
            }
        } else if (updateStickyContent && stickyViewHolder != null) {
            adapter.onBindViewHolder(stickyViewHolder, this.stickyPosition);
            ensureStickyViewParent();
        }

        translateStickyView();
    }

    /**
     * Get {@link BrickViewHolder} for a given position in the adapter's RecyclerView. The view will be bound, measured and laid out.
     *
     * @param position position to get a {@link BrickViewHolder} for
     * @return a {@link BrickViewHolder}
     */
    private BrickViewHolder getStickyViewHolder(int position) {
        // We want to create this view outside of the RecyclerView to avoid any issues with the sticky view getting recycled
        BrickViewHolder holder = null;

        BaseBrick stickyBrick = brickDataManager.brickAtPosition(position);
        if (stickyBrick != null) {
            RecyclerView recyclerView = brickDataManager.getRecyclerView();
            BrickRecyclerAdapter adapter = brickDataManager.getBrickRecyclerAdapter();

            View stickyBrickView = inflateStickyView(stickyBrick, recyclerView);
            holder = stickyBrick.createViewHolder(stickyBrickView);
            stickyBrick.onBindData(holder);

            //Calculate width and height
            int widthSpec;
            int heightSpec;

            //Need to set the height and width exactly to ensure header size when changing layout
            widthSpec = View.MeasureSpec.makeMeasureSpec(adapter.getRecyclerView().getWidth(), View.MeasureSpec.EXACTLY);
            heightSpec = View.MeasureSpec.makeMeasureSpec(adapter.getRecyclerView().getHeight(), View.MeasureSpec.EXACTLY);

            //Measure and Layout the itemView
            final View stickyView = holder.itemView;
            BrickPadding brickPadding = brickDataManager.brickAtPosition(position).getPadding();
            stickyView.setPadding(brickPadding.getOuterLeftPadding(), 0, brickPadding.getOuterRightPadding(), 0);
            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    adapter.getRecyclerView().getPaddingLeft() + adapter.getRecyclerView().getPaddingRight(),
                    stickyView.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    adapter.getRecyclerView().getPaddingTop() + adapter.getRecyclerView().getPaddingBottom(),
                    stickyView.getLayoutParams().height);

            stickyView.measure(childWidth, childHeight);
            stickyView.layout(0, 0, stickyView.getMeasuredWidth(), stickyView.getMeasuredHeight());
        }
        return holder;
    }

    /**
     * Inflate sticky brick.
     * <p>
     * Public for testing.
     *
     * @param stickyBrick  The brick that we are inflating
     * @param recyclerView The parent recyclerview
     * @return Inflated view for the given brick
     */
    public View inflateStickyView(BaseBrick stickyBrick, RecyclerView recyclerView) {
        return LayoutInflater.from(recyclerView.getContext()).inflate(stickyBrick.getLayout(), recyclerView, false);
    }

    /**
     * Set the background color of the sticky view.
     *
     * @param backgroundColor The background color to use as the BG of the sticky view.
     */
    public void setStickyBackgroundColor(@ColorInt int backgroundColor) {
        stickyBackgroundColor = backgroundColor;

        if (stickyHolderLayout != null) {
            stickyHolderLayout.setBackgroundColor(stickyBackgroundColor);
        }
    }
}
