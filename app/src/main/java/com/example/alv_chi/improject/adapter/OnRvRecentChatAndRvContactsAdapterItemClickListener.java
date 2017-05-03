package com.example.alv_chi.improject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.alv_chi.improject.activity.ChatRoomActivity;
import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.data.constant.Constants;
import com.example.alv_chi.improject.data.DataManager;

/**
 * Created by Alv_chi on 2017/2/27.
 */

public  class OnRvRecentChatAndRvContactsAdapterItemClickListener implements View.OnClickListener{
    public static final String TAG = "OnRvRecentChatAndRvContactsAdapterItemClickListener";
    private final Context context;
    private  BaseItem baseItem;


    public OnRvRecentChatAndRvContactsAdapterItemClickListener(Context context, BaseItem baseItem)
    {
        this.context=context;
        this.baseItem=baseItem;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        DataManager.getDataManagerInstance().collectMessages(DataManager.getDataManagerInstance().getAllUsersMessageRecords()
                ,baseItem);
        intent.putExtra(Constants.KeyConstants.USER_MESSAGES_RECORD
                ,DataManager.getDataManagerInstance().getAllUsersMessageRecords().get(baseItem.getUserJID()) );
        context.startActivity(intent);
    }
}
