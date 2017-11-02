package com.wayfair.brickkit.brick;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.wayfair.brickkit.BR;
import com.wayfair.brickkit.R;
import com.wayfair.brickkit.databinding.TextBrickVmBinding;
import com.wayfair.brickkit.models.TextDataModel;
import com.wayfair.brickkit.models.TextViewModel;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Not;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ViewModelBrickTest {
    private final String TEXT = "Test Text...";
    private final String APPENDING_TEXT = " DONE";
    private final int LAYOUT_ID = R.layout.text_brick_vm;
    private final int BIND_ID = BR.textViewModel;

    private Context context;
    private BrickSize brickSize;
    private BrickPadding brickPadding;

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        brickSize = mock(BrickSize.class);
        brickPadding = mock(BrickPadding.class);
    }

    @Test
    public void ViewModelBrick_SingleViewModel_Test() throws Throwable {
        uiThreadTestRule.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        TextDataModel textDataModel = new TextDataModel(TEXT);
                        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

                        ViewModelBrick viewModelBrick = new ViewModelBrick(
                                brickSize,
                                brickPadding,
                                LAYOUT_ID,
                                BIND_ID,
                                textViewModel
                        );

                        LinearLayout parent = new LinearLayout(context);
                        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewModelBrick.getLayout(), parent, false);

                        ViewModelBrick.ViewModelBrickViewHolder holder = (ViewModelBrick.ViewModelBrickViewHolder) viewModelBrick.createViewHolder(itemView);
                        viewModelBrick.onBindData(holder);

                        textDataModel.appendText(APPENDING_TEXT);

                        verify((TextViewModel) viewModelBrick.getViewModel(BIND_ID)).getText();
                    }
                }
        );
    }

    @Test
    public void ViewModelBrick_MultiViewModel_Test() throws Throwable {
        uiThreadTestRule.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        TextDataModel textDataModel = new TextDataModel(TEXT);
                        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

                        SparseArray<ViewModel> viewModels = new SparseArray<>();
                        viewModels.put(BIND_ID, textViewModel);

                        ViewModelBrick viewModelBrick = new ViewModelBrick(
                                brickSize,
                                brickPadding,
                                LAYOUT_ID,
                                viewModels
                        );

                        LinearLayout parent = new LinearLayout(context);
                        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewModelBrick.getLayout(), parent, false);

                        ViewModelBrick.ViewModelBrickViewHolder holder = (ViewModelBrick.ViewModelBrickViewHolder) viewModelBrick.createViewHolder(itemView);
                        viewModelBrick.onBindData(holder);

                        textDataModel.appendText(APPENDING_TEXT);

                        verify((TextViewModel) viewModelBrick.getViewModel(BIND_ID)).getText();
                    }
                }
        );
    }

    @Test
    public void ViewModelBrick_Dismissed_Test() throws Throwable {
        uiThreadTestRule.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        TextDataModel textDataModel = new TextDataModel(TEXT);
                        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

                        ViewModelBrick viewModelBrick = new ViewModelBrick(
                                brickSize,
                                brickPadding,
                                LAYOUT_ID,
                                BIND_ID,
                                textViewModel
                        );

                        SwipeListener swipeListener = mock(SwipeListener.class);

                        viewModelBrick.setOnDismiss(swipeListener);

                        LinearLayout parent = new LinearLayout(context);
                        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewModelBrick.getLayout(), parent, false);

                        ViewModelBrick.ViewModelBrickViewHolder holder = (ViewModelBrick.ViewModelBrickViewHolder) viewModelBrick.createViewHolder(itemView);
                        viewModelBrick.onBindData(holder);

                        viewModelBrick.dismissed(ItemTouchHelper.RIGHT);

                        verify(swipeListener).swiped(ItemTouchHelper.RIGHT);
                    }
                }
        );
    }

    @Test
    public void ViewModelBrick_Dismissed_NotSet_Test() throws Throwable {
        uiThreadTestRule.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        TextDataModel textDataModel = new TextDataModel(TEXT);
                        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

                        ViewModelBrick viewModelBrick = new ViewModelBrick(
                                brickSize,
                                brickPadding,
                                LAYOUT_ID,
                                BIND_ID,
                                textViewModel
                        );

                        SwipeListener swipeListener = mock(SwipeListener.class);

                        viewModelBrick.setOnDismiss(swipeListener);
                        viewModelBrick.setOnDismiss(null);

                        LinearLayout parent = new LinearLayout(context);
                        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewModelBrick.getLayout(), parent, false);

                        ViewModelBrick.ViewModelBrickViewHolder holder = (ViewModelBrick.ViewModelBrickViewHolder) viewModelBrick.createViewHolder(itemView);
                        viewModelBrick.onBindData(holder);

                        viewModelBrick.dismissed(ItemTouchHelper.RIGHT);

                        verify(swipeListener, never()).swiped(ItemTouchHelper.RIGHT);
                    }
                }
        );
    }

}