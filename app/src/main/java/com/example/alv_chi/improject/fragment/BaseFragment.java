package com.example.alv_chi.improject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alv_chi.improject.activity.BaseActivity;


/**
 * Created by Alv_chi on 2017/2/20.
 */

public abstract class BaseFragment extends Fragment {

    private View mContentView;
    private BaseActivity mHoldingActivity;
    private static final String TAG = "BaseFragment";

    //   this method is for getting subClass LayoutResId to  be loaded in this basic Fragment:
    public abstract int getLayoutId();

    //    this method is for the subClasses to handle their contentView:
    protected abstract void initializeView(View view, Bundle savedInstanceState);

    protected BaseActivity getHoldingActivity() {
        return mHoldingActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHoldingActivity = (BaseActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(getLayoutId(), container, false);
            initializeView(mContentView, savedInstanceState);
        }
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHoldingActivity.setmCurrentFragment(this);
    }


}
