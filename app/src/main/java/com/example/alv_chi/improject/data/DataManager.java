package com.example.alv_chi.improject.data;

import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.bean.ContactItem;
import com.example.alv_chi.improject.bean.RecentChatItem;

import org.jivesoftware.smack.chat.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alv_chi on 2017/3/6.
 */

public class DataManager {

    private static final String TAG = "DataManager";

    private HashMap<String, Integer> messageNotificationIds = new HashMap<>();
    private HashMap<String, ArrayList<BaseItem>> allUsersMessageRecords = new HashMap<>();
    private HashMap<String, Chat> chats = new HashMap<>();
    private HashMap<String, Boolean> isOnline = new HashMap<>();
    private ArrayList<RecentChatItem> recentChats = new ArrayList<>();
    private List<ContactItem> contactItems = new ArrayList<>();

    private static DataManager dataManagerInstance;
    private String currentChattingUserJID;
    private String currentMasterUserName;
    private String currentMasterPassword;


    public static DataManager getDataManagerInstance() {
        if (dataManagerInstance == null) {
            dataManagerInstance = new DataManager();
        }

        return dataManagerInstance;
    }

    public void setCurrentChattingUserJID(String currentChattingUserJID) {
        this.currentChattingUserJID = currentChattingUserJID;
    }

    public String getCurrentChattingUserJID() {
        return currentChattingUserJID;
    }

    public String getCurrentMasterPassword() {
        return currentMasterPassword;
    }

    public void setCurrentMasterPassword(String currentMasterPassword) {
        this.currentMasterPassword = currentMasterPassword;
    }

    public String getCurrentMasterUserName() {
        return currentMasterUserName;
    }

    public void setCurrentMasterUserName(String currentMasterUserName) {
        this.currentMasterUserName = currentMasterUserName;
    }

    public List<ContactItem> getContactItems() {
        return contactItems;
    }

    public HashMap<String, Boolean> getIsOnline() {
        return isOnline;
    }

    public HashMap<String, Integer> getMessageNotificationIds() {
        return messageNotificationIds;
    }

    public HashMap<String, ArrayList<BaseItem>> getAllUsersMessageRecords() {
        return allUsersMessageRecords;
    }

    public ArrayList<RecentChatItem> getRecentChats() {
        return recentChats;
    }

    public ArrayList<RecentChatItem> collectRecentChat(RecentChatItem recentChatItem) {
        if (getRecentChats().contains(recentChatItem)) {
            getRecentChats().remove(recentChatItem);
        }
        getRecentChats().add(0, recentChatItem);

        return getRecentChats();
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


    public void clearDatas() {
        allUsersMessageRecords.clear();
        messageNotificationIds.clear();
        recentChats.clear();
        chats.clear();
        clearContactsData();
        currentChattingUserJID = "";
        System.gc();
    }

    public void clearContactsData()
    {
        contactItems.clear();
        isOnline.clear();

    }
}
