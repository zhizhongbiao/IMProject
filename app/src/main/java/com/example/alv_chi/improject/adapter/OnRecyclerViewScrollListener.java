package com.example.alv_chi.improject.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Alv_chi on 2017/2/27.
 */

public abstract class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    public static final String TAG = "OnRecyclerViewScrollListener";
    private static final int SCROLL_THRESHOLD=5;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > SCROLL_THRESHOLD) {
            scrollDown(recyclerView, dy);
        } else if (dy < -SCROLL_THRESHOLD) {
            scrollUp(recyclerView, dy);
        }
    }


    public abstract void scrollUp(RecyclerView recyclerView, int scrollYdistance);

    public abstract void scrollDown(RecyclerView recyclerView, int scrollYdistance);
}
