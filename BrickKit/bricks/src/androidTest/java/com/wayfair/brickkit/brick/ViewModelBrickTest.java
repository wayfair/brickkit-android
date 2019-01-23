package com.wayfair.brickkit.brick;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.wayfair.brickkit.BR;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.R;
import com.wayfair.brickkit.models.TextDataModel;
import com.wayfair.brickkit.models.TextViewModel;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ViewModelBrickTest {
    private final String TEXT = "Test Text...";
    private final String TEXT_2 = "Not Test Text...";
    private final String APPENDING_TEXT = " DONE";
    private final int LAYOUT_ID = R.layout.text_brick_vm;
    private final int BIND_ID = BR.textViewModel;
    private final int BIND_ID_2 = BR.text;

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
    public void ViewModelBrick_SingleViewModel_Test() {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        TextDataModel textDataModel = new TextDataModel(TEXT);
                        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

                        ViewModelBrick viewModelBrick = new ViewModelBrick.Builder(LAYOUT_ID)
                                .setSpanSize(brickSize)
                                .setPadding(brickPadding)
                                .addViewModel(BIND_ID, textViewModel)
                                .build();

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
    public void ViewModelBrick_MultiViewModel_Test() {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        TextDataModel textDataModel = new TextDataModel(TEXT);
                        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

                        SparseArray<ViewModel> viewModels = new SparseArray<>();
                        viewModels.put(BIND_ID, textViewModel);

                        ViewModelBrick viewModelBrick = new ViewModelBrick.Builder(LAYOUT_ID)
                                .setSpanSize(brickSize)
                                .setPadding(brickPadding)
                                .addViewModel(BIND_ID, textViewModel)
                                .build();

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
    public void ViewModelBrick_Dismissed_Test() {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        TextDataModel textDataModel = new TextDataModel(TEXT);
                        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

                        ViewModelBrick viewModelBrick = new ViewModelBrick.Builder(LAYOUT_ID)
                                .setSpanSize(brickSize)
                                .setPadding(brickPadding)
                                .addViewModel(BIND_ID, textViewModel)
                                .build();

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
    public void ViewModelBrick_Dismissed_NotSet_Test() {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        TextDataModel textDataModel = new TextDataModel(TEXT);
                        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

                        ViewModelBrick viewModelBrick = new ViewModelBrick.Builder(LAYOUT_ID)
                                .setSpanSize(brickSize)
                                .setPadding(brickPadding)
                                .addViewModel(BIND_ID, textViewModel)
                                .build();

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

    @Test
    public void ViewModelBrick_Equals_True_Test() {
        TextDataModel textDataModel = new TextDataModel(TEXT);
        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

        ViewModelBrick viewModelBrick = new ViewModelBrick.Builder(LAYOUT_ID)
                .setSpanSize(brickSize)
                .setPadding(brickPadding)
                .addViewModel(BIND_ID, textViewModel)
                .build();

        SparseArray<ViewModel> viewModelSparseArray = new SparseArray<>();
        viewModelSparseArray.put(BIND_ID, textViewModel);
        viewModelBrick.setViewModels(viewModelSparseArray);
        ViewModelBrick viewModelBrick2 = new ViewModelBrick.Builder(LAYOUT_ID)
                .setSpanSize(brickSize)
                .setPadding(brickPadding)
                .setViewModels(viewModelSparseArray)
                .build();

        Assert.assertTrue(viewModelBrick.equals(viewModelBrick2));
    }

    @Test
    public void ViewModelBrick_Equals_True_False() {
        TextDataModel textDataModel = new TextDataModel(TEXT);
        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

        ViewModelBrick viewModelBrick = new ViewModelBrick.Builder(LAYOUT_ID)
                .setSpanSize(brickSize)
                .setPadding(brickPadding)
                .addViewModel(BIND_ID, textViewModel)
                .build();

        TextDataModel textDataModel2 = new TextDataModel(TEXT_2);
        TextViewModel textViewModel2 = spy(new TextViewModel(textDataModel2));


        ViewModelBrick viewModelBrick2 = new ViewModelBrick.Builder(LAYOUT_ID)
                .setSpanSize(brickSize)
                .setPadding(brickPadding)
                .addViewModel(BIND_ID, textViewModel2)
                .build();


        Assert.assertFalse(viewModelBrick.equals(viewModelBrick2));
    }

    @Test
    public void ViewModelBrick_AddViewModel() {
        TextDataModel textDataModel = new TextDataModel(TEXT);
        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

        ViewModelBrick viewModelBrick = new ViewModelBrick.Builder(LAYOUT_ID)
                .setSpanSize(brickSize)
                .setPadding(brickPadding)
                .addViewModel(BIND_ID, textViewModel)
                .build();

        TextDataModel textDataModel2 = new TextDataModel(TEXT_2);
        TextViewModel textViewModel2 = spy(new TextViewModel(textDataModel2));

        viewModelBrick.addViewModel(BIND_ID_2, textViewModel2);

        Assert.assertEquals(2, viewModelBrick.getViewModels().size());
    }

    @Test
    public void ViewModelBrick_SetViewModels() {
        TextDataModel textDataModel = new TextDataModel(TEXT);
        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));

        ViewModelBrick viewModelBrick = new ViewModelBrick.Builder(LAYOUT_ID)
                .setSpanSize(brickSize)
                .setPadding(brickPadding)
                .addViewModel(BIND_ID, textViewModel)
                .build();

        TextDataModel textDataModel2 = new TextDataModel(TEXT_2);
        TextViewModel textViewModel2 = spy(new TextViewModel(textDataModel2));

        SparseArray<ViewModel> viewModelSparseArray = new SparseArray<>();
        viewModelSparseArray.put(BIND_ID_2, textViewModel2);
        viewModelBrick.setViewModels(viewModelSparseArray);

        Assert.assertEquals(1, viewModelBrick.getViewModels().size());
    }

    @Test
    public void ViewModelBrick_Placeholder() {
        TextDataModel textDataModel = new TextDataModel(TEXT);
        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));
        when(textViewModel.isDataModelReady()).thenReturn(false);

        ViewModelBrick viewModelBrick = new ViewModelBrick.Builder(LAYOUT_ID)
                .addViewModel(BIND_ID, textViewModel)
                .setPlaceholder(
                        R.layout.text_brick_vm_placeholder,
                        new PlaceholderBinder() {
                            @Override
                            public void onBindPlaceholder(BrickViewHolder holder) {

                            }
                        }
                )
                .build();


        LinearLayout parent = new LinearLayout(context);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewModelBrick.getLayout(), parent, false);

        ViewModelBrick.ViewModelBrickViewHolder holder = (ViewModelBrick.ViewModelBrickViewHolder) viewModelBrick.createViewHolder(itemView);
        viewModelBrick.onBindPlaceholder(holder);

        Assert.assertEquals(R.layout.text_brick_vm_placeholder, viewModelBrick.getPlaceholderLayout());
    }

    @Test
    public void ViewModelBrick_Placeholder_BinderIsNull() {
        TextDataModel textDataModel = new TextDataModel(TEXT);
        TextViewModel textViewModel = spy(new TextViewModel(textDataModel));
        when(textViewModel.isDataModelReady()).thenReturn(false);

        ViewModelBrick viewModelBrick = new ViewModelBrick.Builder(LAYOUT_ID)
                .addViewModel(BIND_ID, textViewModel)
                .setPlaceholder(R.layout.text_brick_vm_placeholder, null)
                .build();


        LinearLayout parent = new LinearLayout(context);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewModelBrick.getLayout(), parent, false);

        ViewModelBrick.ViewModelBrickViewHolder holder = (ViewModelBrick.ViewModelBrickViewHolder) viewModelBrick.createViewHolder(itemView);
        viewModelBrick.onBindPlaceholder(holder);

        Assert.assertEquals(R.layout.text_brick_vm_placeholder, viewModelBrick.getPlaceholderLayout());
    }
}