package com.example.alv_chi.improject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.fragment.BaseFragment;

public class StartActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, LogInAndSignUpActivity.class);
                startAnotherActivity(StartActivity.this, intent);
                StartActivity.this.finish();
            }
        }, 1000);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

    @Override
    protected void intializeToolbar(ToolbarViewHolder toolbarViewHolder) {
        toolbarViewHolder.toolbar.setVisibility(View.GONE);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.flFragmentContainer;
    }


}
