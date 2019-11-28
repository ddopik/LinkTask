package com.ddopik.linktask.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ddopik.linktask.R;


public class CustomRecyclerView extends RecyclerView {

    private static final int HORIZONTAL_RECYCLER_VIEW = 0;
    private static final int VERTICAL_RECYCLER_VIEW = 1;
    private static final int GRID_RECYCLER_VIEW = 2;

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        styleRecyclerView(context, attrs);
    }

    private void styleRecyclerView(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.CustomRecyclerView, 0, 0);
        int recyclerOrientation = typedArray.getInteger(R.styleable.CustomRecyclerView_orientation, 0);

        if (recyclerOrientation == HORIZONTAL_RECYCLER_VIEW) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            setLayoutManager(linearLayoutManager);
        } else if (recyclerOrientation == VERTICAL_RECYCLER_VIEW) {
            @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//            linearLayoutManager.setAutoMeasureEnabled(false);
            setLayoutManager(linearLayoutManager);
        } else if (recyclerOrientation == GRID_RECYCLER_VIEW) {
            int spanSize = typedArray.getInteger(R.styleable.CustomRecyclerView_span_size, 2);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, spanSize);
            setLayoutManager(gridLayoutManager);
        }
        typedArray.recycle();
    }


}