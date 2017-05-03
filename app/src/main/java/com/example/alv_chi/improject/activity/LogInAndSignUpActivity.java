package com.example.alv_chi.improject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.LoginFragment;

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
    }



    @Override
    protected void intializeToolbar(ToolbarViewHolder toolbarViewHolder) {
        toolbarViewHolder.toolbar.setVisibility(View.GONE);
    }

    @Override
    protected int getActivityLayoutId() {
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
