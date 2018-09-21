package com.wayfair.brickkitdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.brick.LayoutBinder;
import com.wayfair.brickkit.brick.ViewModel;
import com.wayfair.brickkit.brick.ViewModelBrick;
import com.wayfair.brickkit.models.TextDataModel;
import com.wayfair.brickkit.models.TextViewModel;
import com.wayfair.brickkit.padding.SimpleBrickPadding;
import com.wayfair.brickkit.size.SimpleBrickSize;
import com.wayfair.brickkitdemo.bricks.UnusedBrick;

import java.util.ArrayList;

/**
 * A fragment which shows off the {@link ViewModelBrick}'s ability to bind multiple times.
 * This allows for us to create placeholder bricks, and bind to them when the data is received.
 */
public class PlaceholderBrickFragment extends BrickFragment {
    private static final int ONE_FIFTH = BaseBrick.DEFAULT_MAX_SPAN_COUNT / 5;
    private static final int TWO_FIFTH = ONE_FIFTH * 2;
    private static final String PLACEHOLDER = "placeholder";
    private static final String PLACEHOLDER_MULTIBIND = "placeholder_multibind";
    SimpleBrickPadding standardPadding = new SimpleBrickPadding(4);
    UnusedBrick brickTwoFifths = getSizedBrick(TWO_FIFTH);
    UnusedBrick brickOneFifth = getSizedBrick(ONE_FIFTH);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<BaseBrick> bricks = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                bricks.add(brickOneFifth);
            } else {
                bricks.add(brickTwoFifths);
            }
            bricks.add(getPlaceholder(PLACEHOLDER));
            if (i % 2 == 0) {
                bricks.add(brickTwoFifths);
            } else {
                bricks.add(brickOneFifth);
            }
        }

        bricks.add(brickOneFifth);
        bricks.add(getPlaceholder(PLACEHOLDER_MULTIBIND));
        bricks.add(brickTwoFifths);

        dataManager.addLast(bricks);

        setPlaceholderDelayedAction(PLACEHOLDER, new BindingAction() {
            @Override
            public void perform(ViewModelBrick brick) {
                brick.replaceLayoutWith(
                        R.layout.text_brick_vm,
                        new SparseArray<ViewModel>() {{
                            append(
                                    BR.textViewModel,
                                    new TextViewModel(
                                            new TextDataModel("AFTER")
                                    )
                            );
                        }});
            }
        });

        startMultiBindingBrickSequence();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataManager.clear();
    }

    /**
     * Generating a placeholder brick. We can add a tag for the brick, so that we can grab it later.
     *
     * @param tag The tag that we want to associate with the placeholder brick.
     * @return A new placeholder ViewModelBrick.
     */
    private BaseBrick getPlaceholder(String tag) {
        BaseBrick brick = new ViewModelBrick.Builder(R.layout.text_brick_vm_placeholder)
                .setPadding(standardPadding)
                .setSpanSize(
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        }
                )
                .setCustomBinding(new LayoutBinder() {
                    @Override
                    public void onBindLayout(BrickViewHolder holder) {
                        setAlphaAnimation(holder.itemView);
                    }
                })
                .build();
        brick.setTag(tag);
        return brick;
    }

    /**
     * Takes a tag and an action, and calls the action on each of the bricks in the datamanager who
     * match the tag, after a 2 second delay. This is meant to simulate a long-running activity,
     * such as a network request.
     *
     * @param placeholder The tag that we want to associate with the brick
     * @param action The action that we want to perform on bricks that are matched.
     */
    private void setPlaceholderDelayedAction(final String placeholder, final BindingAction action) {
        // Fake waiting as in a API request to show off placeholder when data is not ready
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                for (BaseBrick brick : dataManager.getBricksByTag(placeholder)) {
                    ViewModelBrick viewModelBrick = (ViewModelBrick) brick;
                    action.perform(viewModelBrick);
                }
            }
        }, 2000);
    }

    /**
     * A convenience method which returns a brick which is fits the desired width.
     * @param spanSize The spansize that we would like the brick to be.
     *                 Check out {@link com.wayfair.brickkit.size.BrickSize} for more information.
     * @return An UnusedBrick which is sized correctly.
     */
    @NonNull
    private UnusedBrick getSizedBrick(final int spanSize) {
        return new UnusedBrick(
                new SimpleBrickSize(maxSpans()) {
                    @Override
                    protected int size() {
                        return spanSize;
                    }
                },
                standardPadding);
    }

    /**
     * Sets a fade in / fade out animation on a view.
     * @param v The view which is going to be animated.
     */
    public void setAlphaAnimation(View v) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha", 1f, .3f);
        fadeOut.setDuration(500);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", .3f, 1f);
        fadeIn.setDuration(500);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
    }

    /**
     * NOTE: This is to demonstrate binding multiple times, please don't code like this.
     * This method demonstrates binding to the same brick multiple times.
     */
    private void startMultiBindingBrickSequence() {
        final BindingAction replaceStage3 = new BindingAction() {
            @Override
            public void perform(ViewModelBrick brick) {
                brick.replaceLayoutWith(
                        R.layout.text_brick_vm,
                        new SparseArray<ViewModel>() {{
                            append(BR.textViewModel, new TextViewModel(
                                    new TextDataModel("Fourth Binding")));
                        }});
            }
        };
        final BindingAction replaceStage2 = new BindingAction() {
            @Override
            public void perform(ViewModelBrick brick) {
                brick.replaceLayoutWith(
                        R.layout.text_brick_vm_placeholder,
                        new SparseArray<ViewModel>() {{
                            append(BR.textViewModel, new TextViewModel(new TextDataModel("\n")));
                        }});
                setPlaceholderDelayedAction(PLACEHOLDER_MULTIBIND, replaceStage3);
            }
        };
        final BindingAction replaceStage1 = new BindingAction() {
            @Override
            public void perform(ViewModelBrick brick) {
                brick.replaceLayoutWith(
                        R.layout.text_brick_vm,
                        new SparseArray<ViewModel>() {{
                            append(BR.textViewModel, new TextViewModel(
                                    new TextDataModel("Second Binding")
                            ));
                        }});
                setPlaceholderDelayedAction(PLACEHOLDER_MULTIBIND, replaceStage2);
            }
        };
        setPlaceholderDelayedAction(PLACEHOLDER_MULTIBIND, replaceStage1);
    }

    /**
     * An action that will take place when we are binding a brick for the second time.
     */
    interface BindingAction {
        /**
         * The action that will be performed on a brick when it is bound.
         * @param brick The brick that the action will be performed on.
         */
        void perform(ViewModelBrick brick);
    }
}
