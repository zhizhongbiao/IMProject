package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.alv_chi.improject.R;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class GroupsFragment extends BaseFragment {

    private View mContentView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_group_chat;
    }

    @Override
    protected void initializeView(View view, Bundle savedInstanceState) {

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {
//        if (mContentView ==null)
//        {
//            mContentView = inflater.inflate(R.layout.fragment_group_chat, container, false);
//        }
//        return mContentView;
//    }
}
