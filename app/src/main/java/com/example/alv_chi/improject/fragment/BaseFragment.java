package com.example.alv_chi.improject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.handler.ActivityHandler;


/**
 * Created by Alv_chi on 2017/2/20.
 */

public abstract class BaseFragment extends Fragment {

    private View mContentView;
    private BaseActivity mHoldingActivity;
    private static final String TAG = "BaseFragment";
    protected ActivityHandler mHandler;


    //   this method is for getting subClass LayoutResId to  be loaded in this basic Fragment:
    public abstract int getLayoutId();

    //    this method is for the subClasses to handle their contentView:
    protected abstract void initializeView(View view, Bundle savedInstanceState);


    //    this method is for the subClasses to handle their contentView:
    protected abstract void castActivity(BaseActivity baseActivity);

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
            castActivity(getHoldingActivity());
            mHandler = getHoldingActivity().getActivityHandler();
            Bundle bundle = getArguments();
            if (bundle != null) {
                handleBundleFromOutside(bundle);
            }
            initializeView(mContentView, savedInstanceState);
        }
        return mContentView;
    }

//    subclass can override this method to handle bundleData from outside if needed;
    protected void handleBundleFromOutside(Bundle bundle) {
    }



    @Override
    public void onResume() {
        super.onResume();
        mHoldingActivity.setCurrentFragment(this);
    }


}
