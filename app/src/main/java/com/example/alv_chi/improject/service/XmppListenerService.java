package com.example.alv_chi.improject.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.activity.MainActivity;
import com.example.alv_chi.improject.adapter.MessageRvAdapter;
import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.bean.ContactItem;
import com.example.alv_chi.improject.bean.RecentChatItem;
import com.example.alv_chi.improject.bean.TextMessageItem;
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.eventbus.OnDatasArrivedChattingFragmentEvent;
import com.example.alv_chi.improject.eventbus.EventBusHelper;
import com.example.alv_chi.improject.eventbus.MessageCreatedEvent;
import com.example.alv_chi.improject.eventbus.OnUserStatusChangeEvent;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.ChattingRoomFragment;
import com.example.alv_chi.improject.util.ChineseToPinyinHelper;
import com.example.alv_chi.improject.util.SystemUtil;
import com.example.alv_chi.improject.xmpp.XmppHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class XmppListenerService extends Service implements ChatManagerListener, ChatMessageListener, RosterListener {

    private static final String TAG = "InComingMessageService";
    private BaseActivity currentActivity;
    private DataManager dataManagerInstance;
    private int singleUserJIDMessageId;
    private NotificationManager notificationManager;
    private Roster roster;


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            XmppHelper.getXmppHelperInStance().getChatManager().addChatListener(XmppListenerService.this);
            Log.e(TAG, "onCreate: 服务活了");
            initialize();
        } catch (ConnectException e) {
            e.printStackTrace();
            Log.e(TAG, "XmppListenerService onCreate: ConnectException=" + e.getMessage());
        }
    }


    private void initialize() {
        Constants.AppConfigConstants.isNeedToLogin = false;
        dataManagerInstance = DataManager.getDataManagerInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {

        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().register(this);
        return new MyBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().register(this);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().unregister(this);
//        this return true for calling rebind() method when next time activity bind this service ;
        return true;
    }

    /**
     * this method may be called many times ,so be carefully to ues it .
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initializeContactsData();
        return START_STICKY;// this value for : this service will reCreate after this service is killed;
    }

    private void initializeContactsData() {

        try {
            Set<RosterEntry> contacts = XmppHelper.getXmppHelperInStance().getContacts();

            roster = XmppHelper.getXmppHelperInStance().getRoster();
            roster.addRosterListener(this);

            DataManager.getDataManagerInstance().getContactItems().clear();

            Iterator<RosterEntry> iterator = contacts.iterator();
            while (iterator.hasNext()) {
                RosterEntry rosterEntry = iterator.next();
                if (rosterEntry == null) continue;
                String name = rosterEntry.getName();
                String userJID = rosterEntry.getUser();
                String navigationLetter = null;
                boolean isOnline = false;


                if (name != null) {
                    String pingYin = ChineseToPinyinHelper.getInstance().getPinyin(name).toUpperCase();
                    navigationLetter = pingYin.charAt(0) + "";
                    if (!navigationLetter.matches("[A-Z]")) {
                        navigationLetter = "#";
                    }
                } else {
                    name = "Unknown";
                    navigationLetter = "#";
                }

                Presence presence = XmppHelper.getXmppHelperInStance().getRoster().getPresence(userJID+"/Smack");
                isOnline = getUserStatus(isOnline, presence);
                DataManager.getDataManagerInstance().getIsOnline().put(userJID, isOnline);

                DataManager.getDataManagerInstance().getContactItems().add(new ContactItem(userJID, navigationLetter, name
                        , null, DataManager.getDataManagerInstance().getIsOnline().get(userJID)));//avatar temporary set null
            }

            Collections.sort(DataManager.getDataManagerInstance().getContactItems());//sort the ContactItems

            Log.e(TAG, "initializeContactsData: 联系人在服务初始化了DataManager.getDataManagerInstance().getContactItems().size()="+DataManager.getDataManagerInstance().getContactItems().size() );

        } catch (ConnectException e) {
            e.printStackTrace();
            Log.e(TAG, "initializeContactsData: ConnectException=" + e.getMessage());
        }


    }

    private boolean getUserStatus(boolean isOnline, Presence presence) {
        if (presence != null) {
            Presence.Type type = presence.getType();

            switch (type) {
                case available:
                    isOnline = true;
                    break;
                default:
                    isOnline = false;
                    break;
            }

        }
        return isOnline;
    }


    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        Log.e(TAG, "chatCreated: createdLocally=" + createdLocally);
        chat.addMessageListener(this);
    }


    public synchronized void sendMessage(BaseItem baseItem) throws ConnectException, SmackException.NotConnectedException {
        Chat chat;
        if (dataManagerInstance.getChats().containsKey(baseItem.getUserJID())) {
            chat = dataManagerInstance.getChats().get(baseItem.getUserJID());
        } else {
            chat = XmppHelper.getXmppHelperInStance().getChatManager().createChat(baseItem.getUserJID());
            dataManagerInstance.getChats().put(baseItem.getUserJID(), chat);
        }

        Message message = new Message();
//       this is the name of which send this message;
        message.setSubject(DataManager.getDataManagerInstance().getCurrentMasterUserName());////此处应该获取正在登陆的用户信息：
        message.setBody(baseItem.getMesage());
        message.setFrom(Constants.AppConfigConstants.CLIENT_EMAIL);///此处应该获取正在登陆的用户信息：
        message.setTo(baseItem.getUserJID());
        chat.sendMessage(message);

        dataManagerInstance.collectMessages(dataManagerInstance.getAllUsersMessageRecords(), baseItem);

        createRecentChatRecord(baseItem);
    }


    @Override
    public void processMessage(Chat chat, Message message) {
        String JIDFromUserSendMsg = message.getFrom();
        if (JIDFromUserSendMsg.contains("/")) {
            JIDFromUserSendMsg = JIDFromUserSendMsg.split("/")[0];
        }
// Using a hashmap to manage the chats , which is very conenient to reuse;
        dataManagerInstance.getChats().put(JIDFromUserSendMsg, chat);

        String receivedMsg = message.getBody();
        String userNameFrom = message.getSubject();
        String stanzaId = message.getStanzaId();
        Boolean isOnline = dataManagerInstance.getIsOnline().get(JIDFromUserSendMsg);
        if (isOnline==null)
        {
            isOnline=false;
        }

        if (userNameFrom == null || userNameFrom.trim().equals("")) {
            userNameFrom = JIDFromUserSendMsg.split("@")[0];
        }
        if (receivedMsg != null) {
            TextMessageItem messageItem = new TextMessageItem(userNameFrom
                    , SystemUtil.getCurrentSystemTime()
                    , receivedMsg, null, JIDFromUserSendMsg, MessageRvAdapter.TEXT_MESSAGE_VIEW_TYPE, true, isOnline);

            if (!dataManagerInstance.getMessageNotificationIds().containsKey(JIDFromUserSendMsg)) {
                dataManagerInstance.getMessageNotificationIds().put(JIDFromUserSendMsg, ++singleUserJIDMessageId);
            }

            dataManagerInstance.collectMessages(dataManagerInstance.getAllUsersMessageRecords(), messageItem);

            createRecentChatRecord(messageItem);

            if (currentActivity != null && JIDFromUserSendMsg.equals(DataManager.getDataManagerInstance().getCurrentChattingUserJID())) {
                BaseFragment currentFragment = currentActivity.getmCurrentFragment();
                if (currentFragment instanceof ChattingRoomFragment) {
                    ((ChattingRoomFragment) currentFragment).refreshMessageContainer(messageItem);
                }
            } else {

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.KeyConstants.IS_THIS_INTEN_FROM_PENDING_INTENT, true);
                intent.putParcelableArrayListExtra(Constants.KeyConstants.USER_MESSAGES_RECORD
                        , dataManagerInstance.getAllUsersMessageRecords().get(JIDFromUserSendMsg));

                PendingIntent pendingIntent = PendingIntent.getActivity(this, dataManagerInstance.getMessageNotificationIds().get(JIDFromUserSendMsg), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                Notification notification = new Notification.Builder(this)
                        .setAutoCancel(true)
                        .setContentText(receivedMsg)
                        .setContentTitle(userNameFrom)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.ic_launcher)//UserAvatar
                        .setContentIntent(pendingIntent)
                        .build();

                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(dataManagerInstance.getMessageNotificationIds().get(JIDFromUserSendMsg), notification);

            }


        }
    }

    // notify the RecentChatFragment create recentChatItem;
    private void createRecentChatRecord(BaseItem baseItem) {
        RecentChatItem recentChatItem = new RecentChatItem(baseItem.getUserName()
                , baseItem.getCurrentTimeStamp()
                , baseItem.getMesage()
                , baseItem.getUserAvatar()
                , baseItem.getUserJID()
                , baseItem.isOnline());
//        this is to RecentChatFragment
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().postSticky(new MessageCreatedEvent(recentChatItem));
    }


    public void setCurrentActivity(BaseActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

//      this event is posted by ChattingRoomFragment  ;
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 10, sticky = true)
    public void onDatasHaveArrivedChattingFragmnet(OnDatasArrivedChattingFragmentEvent event) {
        if (notificationManager != null && dataManagerInstance.getMessageNotificationIds().containsKey(event.getUserJIDOfDatas())) {
            notificationManager.cancel(dataManagerInstance.getMessageNotificationIds().get(event.getUserJIDOfDatas()));
        }
        dataManagerInstance.getMessageNotificationIds().remove(event.getUserJIDOfDatas());
        if (dataManagerInstance.getAllUsersMessageRecords().get(event.getUserJIDOfDatas()).size() > 100) {
//            此处将超出100的部分数据保存进数据库，并将将多出部分删除；
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        release resource
        roster.removeRosterListener(this);
        try {
            XmppHelper.getXmppHelperInStance().logOut();
            XmppHelper.getXmppHelperInStance().getChatManager().removeChatListener(this);
            XmppHelper.getXmppHelperInStance().clearXmppSetup();
        } catch (Exception e) {
            e.printStackTrace();
        }
//       clear all notifications if there is any;
        clearNotification();

        dataManagerInstance.clearDatas();
        Constants.AppConfigConstants.isNeedToLogin = true;

        Log.e(TAG, "onDestroy: 服务死了");

    }

    private void clearNotification() {
        if (notificationManager != null && !dataManagerInstance.getMessageNotificationIds().isEmpty()) {
            Set<Map.Entry<String, Integer>> entries = dataManagerInstance.getMessageNotificationIds().entrySet();
            for (Map.Entry<String, Integer> entry : entries) {
                notificationManager.cancel(entry.getValue());
            }
        }
    }



//    RosterListener needs to implement those methods

    @Override
    public void entriesAdded(Collection<String> addresses) {

    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {

    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {

    }

    @Override
    public void presenceChanged(Presence presence) {

        String JIDFromUser = presence.getFrom();

        if (JIDFromUser.contains("/"))
        {
            JIDFromUser=JIDFromUser.split("/")[0];
        }
        Log.e(TAG, "presenceChanged: JIDFromUser=" + JIDFromUser);
        boolean isOnline = false;
        isOnline = getUserStatus(isOnline, presence);
        DataManager.getDataManagerInstance().getIsOnline().put(JIDFromUser, isOnline);

//      this event is posted to  notify the ChattingActivity to change its toolbar status and update DataMangae;
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().postSticky(new OnUserStatusChangeEvent(presence));

    }

    public class MyBinder extends Binder {
        public XmppListenerService getInComingMessageListenerService() {
            return XmppListenerService.this;
        }
    }
}
