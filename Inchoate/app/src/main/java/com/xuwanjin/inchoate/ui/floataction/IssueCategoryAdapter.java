package com.xuwanjin.inchoate.ui.floataction;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.xuwanjin.inchoate.InchoateApp;
import com.xuwanjin.inchoate.R;
import com.xuwanjin.inchoate.ui.BaseAdapter;

import java.util.List;

/**
 * @author Matthew Xu
 */
public class IssueCategoryAdapter extends BaseAdapter<IssueCategoryViewHolder, String> {
    public static final String TAG = "IssueCategoryAdapter";

    public IssueCategoryAdapter(Context context, List<String> sectionList) {
        super(context, sectionList);
    }

    @Override
    protected int getLayoutItemResId() {
        return R.layout.issue_category_item;
    }

    @Override
    protected IssueCategoryViewHolder getViewHolder(View view, boolean isHeaderOrFooter) {
        return new IssueCategoryViewHolder(view);
    }

    @Override
    public String getGroupName(int position) {
        return null;
    }

    @Override
    public void onBindViewHolderImpl(@NonNull IssueCategoryViewHolder holder, final int position, String sectionName) {
        holder.categoryMenu.setText(sectionName);
        holder.categoryMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InchoateApp.setScrollToPosition(position);
                Log.d(TAG, "onClick:  position = " + position);
                navigationToFragment(R.id.navigation_weekly);
            }
        });
    }


    @Override
    public boolean isItemHeader(int position) {
        return false;
    }
}
