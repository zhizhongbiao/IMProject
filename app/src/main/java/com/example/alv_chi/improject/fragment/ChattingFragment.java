package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.alv_chi.improject.R;

import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class ChattingFragment extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.chatting_fragment;
    }

    @Override
    protected void initializeView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(view);
    }

}
