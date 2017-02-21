package com.example.alv_chi.improject.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.alv_chi.improject.util.AppTypeFace;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class IconfontTextView extends TextView {

    public IconfontTextView(Context context) {
        this(context, null);

    }

    public IconfontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconfontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public IconfontTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialView(context);
    }

    private void initialView(Context context) {
        this.setTypeface(AppTypeFace.getTypeFace(context, "iconfont/iconfont.ttf"));
    }
}
