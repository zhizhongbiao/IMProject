package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alv_chi.improject.R;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class GroupsFragment extends Fragment {

    private View mContentView;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {
        if (mContentView ==null)
        {
            mContentView = inflater.inflate(R.layout.group_chat_fragment, container, false);
        }
        return mContentView;
    }
}
