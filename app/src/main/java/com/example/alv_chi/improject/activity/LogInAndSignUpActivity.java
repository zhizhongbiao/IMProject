package com.example.alv_chi.improject.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.LoginFragment;
import com.example.alv_chi.improject.handler.HandlerHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogInAndSignUpActivity extends BaseActivity {

    private static final String TAG = "LogInAndSignUpActivity";

    @BindView(R.id.flFragmentContainer)
    FrameLayout flFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_signup);
        ButterKnife.bind(this);
        HandlerHelper.setIsActivityDestroyed(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HandlerHelper.setIsActivityDestroyed(true);
        Log.e(TAG, "onDestroy: this=" + this);
    }

    @Override
    protected void intializeToolbar(ToolbarViewHolder toolbarViewHolder) {
        toolbarViewHolder.toolbar.setVisibility(View.GONE);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login_and_signup;
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
