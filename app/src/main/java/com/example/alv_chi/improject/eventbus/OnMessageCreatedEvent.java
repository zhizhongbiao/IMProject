package com.example.alv_chi.improject.eventbus;

import com.example.alv_chi.improject.bean.RecentChatItem;

/**
 * Created by Alv_chi on 2017/3/7.
 */
public class OnMessageCreatedEvent {

    private RecentChatItem recentChatItem;

    public OnMessageCreatedEvent(RecentChatItem recentChatItem) {
        this.recentChatItem = recentChatItem;
    }

    public RecentChatItem getRecentChatItem() {
        return recentChatItem;
    }

    public void setRecentChatItem(RecentChatItem recentChatItem) {
        this.recentChatItem = recentChatItem;
    }
}
