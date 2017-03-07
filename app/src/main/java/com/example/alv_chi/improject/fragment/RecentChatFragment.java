package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.activity.MainActivity;
import com.example.alv_chi.improject.adapter.RvRecentChatAdapter;
import com.example.alv_chi.improject.bean.RecentChatItem;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.eventbus.EventBusHelper;
import com.example.alv_chi.improject.eventbus.MessageCreatedEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class RecentChatFragment extends BaseFragment {

    @BindView(R.id.rvRecentChat)
    RecyclerView rvRecentChat;
    private MainActivity mHoldingActivity;
    private LinearLayoutManager linearLayoutManager;
    private RvRecentChatAdapter rvRecentChatAdapter;

    @Override
    protected void castActivity(BaseActivity baseActivity) {
        mHoldingActivity = (MainActivity) baseActivity;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recent_chat;
    }

    @Override
    protected void initializeView(View rootView, Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        linearLayoutManager = new LinearLayoutManager(mHoldingActivity, LinearLayoutManager.VERTICAL, false);
        rvRecentChat.setLayoutManager(linearLayoutManager);
        rvRecentChatAdapter = new RvRecentChatAdapter(mHoldingActivity, DataManager.getDataManagerInstance().getRecentChats());
        rvRecentChat.setAdapter(rvRecentChatAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 10, sticky = true)
    public void onMessageCreatedEvent(MessageCreatedEvent event) {
        RecentChatItem recentChatItem = event.getRecentChatItem();
        DataManager.getDataManagerInstance().collectRecentChat(recentChatItem);
        rvRecentChatAdapter.notifyDataSetChanged();
    }
}
