package com.example.alv_chi.improject.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.alv_chi.improject.R;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class TabView extends FrameLayout {

    private TextView mCommonTextView;
    private View mTabView;
    private IconfontTextView mIconfontTextView;
    private int mViewColor;
    private float mViewSize;
    private String mCommonTextViewText;
    private String mIconfontTextViewText;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialTabView(context, attrs, defStyleAttr);
    }

    private void initialTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        mTabView = LayoutInflater.from(context).inflate(R.layout.tab_view, this, true);
        mCommonTextView = (TextView) mTabView.findViewById(R.id.tvCommon);
        mIconfontTextView = (IconfontTextView) mTabView.findViewById(R.id.iconfontTv);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabView, defStyleAttr, 0);
        mViewColor = typedArray.getColor(R.styleable.TabView_viewColor, Color.BLACK);
        mViewSize = typedArray.getDimension(R.styleable.TabView_viewSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 4, context.getResources().getDisplayMetrics()));
        mCommonTextViewText = typedArray.getString(R.styleable.TabView_commonTextViewText);
        mIconfontTextViewText = typedArray.getString(R.styleable.TabView_iconfontTextViewText);

        setViewText(mIconfontTextViewText, mCommonTextViewText);
        setViewSize(mViewSize);
        setViewColor(mViewColor);

        typedArray.recycle();
    }

    public void setViewColor(int color) {
        mIconfontTextView.setTextColor(color);
        mCommonTextView.setTextColor(color);
    }

    public void setViewSize(float size) {
        mIconfontTextView.setTextSize(size);
        mCommonTextView.setTextSize(size);
    }

    public void setViewText(String textForIconfontTextView, String textForCommonTextView) {
        mCommonTextView.setText(textForCommonTextView);
        mIconfontTextView.setText(textForIconfontTextView);
    }
}
