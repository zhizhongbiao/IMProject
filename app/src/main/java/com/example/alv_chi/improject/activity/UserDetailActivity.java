package com.example.alv_chi.improject.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.fragment.BaseFragment;

public class UserDetailActivity extends BaseActivity {

    @Override
    protected void handleIntent(Intent intentFromLastContext) {

    }

    @Override
    protected void intializeToolbar(ToolbarViewHolder toolbarViewHolder) {

    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }
}
