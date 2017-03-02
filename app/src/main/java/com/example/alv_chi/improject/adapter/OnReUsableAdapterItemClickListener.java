package com.example.alv_chi.improject.adapter;

import android.view.View;

import java.util.List;

/**
 * Created by Alv_chi on 2017/2/27.
 */

public abstract class OnReUsableAdapterItemClickListener implements View.OnClickListener{
    public static final String TAG = "OnReUsableAdapterItemClickListener";

    abstract void setRecyclerViewItemData(View view,List<Object> list, int position,int itemType);
    abstract void onRecyclerViewItemClick(View view,List<Object> list, int position,int itemType);

    @Override
    public void onClick(View v) {

    }
}
