package com.example.alv_chi.improject.data;

import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.bean.ContactItem;
import com.example.alv_chi.improject.bean.RecentChatItem;
import com.example.alv_chi.improject.xmpp.service.XmppListenerService;

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
    private String serverIP;

    public XmppListenerService getXmppListenerService() {
        return xmppListenerService;
    }

    public void setXmppListenerService(XmppListenerService xmppListenerService) {
        this.xmppListenerService = xmppListenerService;
    }

    private XmppListenerService xmppListenerService;


    public static DataManager getDataManagerInstance() {
        if (dataManagerInstance == null) {
            dataManagerInstance = new DataManager();
        }

        return dataManagerInstance;
    }

    public void setCurrentChattingUserJID(String currentChattingUserJID) {
        this.currentChattingUserJID = currentChattingUserJID;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
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

    public ArrayList<RecentChatItem> collectRecentChatItems(RecentChatItem recentChatItem) {
        if (getRecentChats().contains(recentChatItem)) {
            getRecentChats().remove(recentChatItem);
        }
        getRecentChats().add(0, recentChatItem);

        return getRecentChats();
    }

    public HashMap<String, Chat> getChats() {
        return chats;
    }

    public void collectMessages(BaseItem baseItem) {
        if (!allUsersMessageRecords.containsKey(baseItem.getUserJID())) {
            ArrayList<BaseItem> baseItems = new ArrayList<>();
            allUsersMessageRecords.put(baseItem.getUserJID(), baseItems);
        }
        allUsersMessageRecords.get(baseItem.getUserJID()).add(baseItem);
    }


    public void clearDatas() {
        allUsersMessageRecords.clear();
        messageNotificationIds.clear();
        recentChats.clear();
        chats.clear();
        clearContactsData();
        setXmppListenerService(null);
        currentChattingUserJID = "";
//        System.gc();
    }

    public void clearContactsData()
    {
        contactItems.clear();
        isOnline.clear();
    }
}
