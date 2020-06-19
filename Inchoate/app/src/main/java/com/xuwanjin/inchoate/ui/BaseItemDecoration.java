package com.xuwanjin.inchoate.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Matthew Xu
 */
public abstract class BaseItemDecoration<T extends BaseAdapter> extends RecyclerView.ItemDecoration {
    protected RecyclerView mRecyclerView;
    protected Context mContext;
    protected T mAdapter;

    protected BaseItemDecoration(Context context, RecyclerView recyclerView) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
        this.mAdapter = (T) recyclerView.getAdapter();
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = parent.getChildAt(i);
            int position = parent.getChildLayoutPosition(childView);
            if (!isSkipDraw(position)) {
                onDrawImpl(canvas, parent, childView, position);
            }
        }
    }

    protected abstract void onDrawImpl(Canvas canvas, RecyclerView parent, View childView, int position);

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        T adapter = (T) getAdapter(parent);
        if (!isSkipDraw(position)) {
            onDrawOverImpl(canvas, parent, state, adapter, position);
        }
    }

    protected abstract boolean isSkipDraw(int position);

    /**
     * onDrawOver 的具体实现
     *
     * @param canvas
     * @param parent
     * @param state
     */
    public abstract void onDrawOverImpl(@NonNull Canvas canvas, @NonNull RecyclerView parent,
                                        @NonNull RecyclerView.State state, T adapter, int position);


    public RecyclerView.Adapter getAdapter(RecyclerView parent) {
        return parent.getAdapter();
    }
}
