package com.example.alv_chi.improject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.LoginFragment;
import com.example.alv_chi.improject.handler.ActivityHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstStartActivity extends BaseActivity {

    private static final String TAG = "FirstStartActivity";

    @BindView(R.id.flFragmentContainer)
    FrameLayout flFragmentContainer;

    private ActivityHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);
        ButterKnife.bind(this);
        mHandler = getActivityHandler();
    }


    @Override
    protected void intializeToolbar(ToolbarViewHolder toolbarViewHolder) {
        toolbarViewHolder.toolbar.setVisibility(View.GONE);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_first_start;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.flFragmentContainer;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return LoginFragment.newInstance();
    }


}
