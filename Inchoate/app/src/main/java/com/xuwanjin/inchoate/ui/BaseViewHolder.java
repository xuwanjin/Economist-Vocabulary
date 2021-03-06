package com.xuwanjin.inchoate.ui;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder{
    protected Context mContext;
    public BaseViewHolder(@NonNull View itemView, boolean isHeaderOrFooter) {
        super(itemView);
        if (!isHeaderOrFooter){
            initView();
        }
    }

    /**
     * find ViewHolder 的所有的 item1
     */
    protected abstract void initView();

}
