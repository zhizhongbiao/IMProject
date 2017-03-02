package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.activity.MainActivity;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class ShareFragment extends BaseFragment {

    private MainActivity mHoldingActivity;

    @Override
    public int getLayoutId() {
        return R.layout.fragmen_sharet;
    }

    @Override
    protected void initializeView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void castActivity(BaseActivity baseActivity) {
        mHoldingActivity = (MainActivity) baseActivity;
    }

}
