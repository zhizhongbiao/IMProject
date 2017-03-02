package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.activity.MainActivity;

import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class ChatFragment extends BaseFragment {

    private MainActivity mHoldingActivity;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initializeView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(view);
    }

    @Override
    protected void castActivity(BaseActivity baseActivity) {
        mHoldingActivity = (MainActivity) baseActivity;
    }

}
