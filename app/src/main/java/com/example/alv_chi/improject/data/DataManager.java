package com.example.alv_chi.improject.data;

import com.example.alv_chi.improject.bean.BaseItem;

import org.jivesoftware.smack.chat.Chat;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alv_chi on 2017/3/6.
 */

public class DataManager {

    private static final String TAG = "DataManager";

    private HashMap<String, Integer> messageNotificationIds = new HashMap<>();
    private HashMap<String, ArrayList<BaseItem>> allUsersMessageRecords = new HashMap<>();
    private HashMap<String, Chat> chats = new HashMap<>();

    private static DataManager dataManagerInstance;

    public static DataManager getDataManagerInstance() {
        if (dataManagerInstance == null) {
            dataManagerInstance = new DataManager();
        }

        return dataManagerInstance;
    }


    public HashMap<String, Integer> getMessageNotificationIds() {
        return messageNotificationIds;
    }

    public HashMap<String, ArrayList<BaseItem>> getAllUsersMessageRecords() {
        return allUsersMessageRecords;
    }

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


    public void clearDatas()
    {
        allUsersMessageRecords.clear();
        allUsersMessageRecords = null;
        messageNotificationIds.clear();
        messageNotificationIds = null;
        chats.clear();
        chats = null;
        System.gc();
    }
}
