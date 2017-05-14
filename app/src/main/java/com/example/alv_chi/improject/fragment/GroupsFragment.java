package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.activity.MainActivity;
import com.example.alv_chi.improject.adapter.ReUsableAdapter;
import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.bean.GroupChatItem;
import com.example.alv_chi.improject.data.DataManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class GroupsFragment extends BaseFragment {

    private static final String TAG = "GroupsFragment";
    @BindView(R.id.rvGroupChat)
    RecyclerView rvGroupChat;
    private MainActivity mHoldingActivity;
    public static final String MULTI_CHAT_ROOM_NAME="HeroMeeting";
    public static final String MULTI_CHAT_ROOM_PASSWORD="英雄大会";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_group_chat;
    }

    @Override
    protected void castActivity(BaseActivity baseActivity) {
        mHoldingActivity = (MainActivity) baseActivity;
    }

    @Override
    protected void initializeView(View rootView, Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);

        try {
            DataManager
                    .getDataManagerInstance()
                    .getXmppListenerService()
                    .createChatRoom(MULTI_CHAT_ROOM_NAME
                            ,DataManager.getDataManagerInstance().getCurrentMasterUserName()
                    ,MULTI_CHAT_ROOM_PASSWORD);
            ArrayList<BaseItem> groupChatItems = new ArrayList<>();
            BaseItem groupChatItem = new GroupChatItem(MULTI_CHAT_ROOM_NAME
                    , DataManager.getDataManagerInstance().getCurrentMasterUserName()
                    , "", "");
            groupChatItems.add(groupChatItem);
            ReUsableAdapter groupChatAdpter = new ReUsableAdapter(mHoldingActivity
                    , groupChatItems
                    , ReUsableAdapter.GROUP_CHAT_ITEM_VIEW_TYPE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mHoldingActivity, LinearLayoutManager.VERTICAL, false);
            rvGroupChat.setAdapter(groupChatAdpter);
            rvGroupChat.setLayoutManager(linearLayoutManager);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "initializeView: Exception="+e.getMessage() );
        } 
    }


}
