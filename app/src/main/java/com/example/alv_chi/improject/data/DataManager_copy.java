package com.example.alv_chi.improject.data;

import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.bean.RecentChatItem;

import org.jivesoftware.smack.chat.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Alv_chi on 2017/3/6.
 */

public class DataManager_copy {

    private static final String TAG = "DataManager";

    private HashMap<String, Integer> messageNotificationIds = new HashMap<>();
    private HashMap<String, ArrayList<BaseItem>> allUsersMessageRecords = new HashMap<>();
    private HashMap<String, Chat> chats = new HashMap<>();
    private HashSet<RecentChatItem> recentChats = new HashSet<>();

    private static DataManager_copy dataManagerInstance;

    public static DataManager_copy getDataManagerInstance() {
        if (dataManagerInstance == null) {
            dataManagerInstance = new DataManager_copy();
        }

        return dataManagerInstance;
    }


    public HashMap<String, Integer> getMessageNotificationIds() {
        return messageNotificationIds;
    }

    public HashMap<String, ArrayList<BaseItem>> getAllUsersMessageRecords() {
        return allUsersMessageRecords;
    }

    public HashSet<RecentChatItem> getRecentChats() {
        return recentChats;
    }

//    public HashSet<RecentChatItem> collectRecentChat(RecentChatItem recentChatItem) {
//        if (getRecentChats().contains(recentChatItem) && getRecentChats().indexOf(recentChatItem) != 0) {/////////////////////
//            getRecentChats().remove(recentChatItem);
//        }
//        getRecentChats().add( recentChatItem);
//
//        return getRecentChats();
//    }

    public HashMap<String, Chat> getChats() {
        return chats;
    }

    public void collectMessages(HashMap<String, ArrayList<BaseItem>> map, BaseItem baseItem) {
        if (!map.containsKey(baseItem.getUserJID())) {
            ArrayList<BaseItem> baseItems = new ArrayList<>();
            map.put(baseItem.getUserJID(), baseItems);
        }
        map.get(baseItem.getUserJID()).add(baseItem);
    }


    public void clearDatas() {
        allUsersMessageRecords.clear();
        allUsersMessageRecords = null;
        messageNotificationIds.clear();
        messageNotificationIds = null;
        chats.clear();
        chats = null;
        System.gc();
    }
}
