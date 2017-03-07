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
import com.example.alv_chi.improject.bean.RecentChatItem;
import com.example.alv_chi.improject.bean.TextMessageItem;
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.eventbus.DatasHaveArrivedChattingFragmentEvent;
import com.example.alv_chi.improject.eventbus.EventBusHelper;
import com.example.alv_chi.improject.eventbus.MessageCreatedEvent;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.ChattingRoomFragment;
import com.example.alv_chi.improject.util.SystemUtil;
import com.example.alv_chi.improject.xmpp.XmppHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

public class InComingMessageListenerService extends Service implements ChatManagerListener, ChatMessageListener {

    private static final String TAG = "InComingMessageService";
    private BaseActivity currentActivity;
    private DataManager dataManagerInstance;
    private int singleUserJIDMessageId;


    @Override
    public void onCreate() {
        super.onCreate();

        try {
            XmppHelper.getXmppHelperInStance().getChatManager().addChatListener(InComingMessageListenerService.this);
            Constants.AppConfigConstants.isNeedToLogin = false;
            dataManagerInstance = DataManager.getDataManagerInstance();
        } catch (ConnectException e) {
            e.printStackTrace();
            Log.e(TAG, "InComingMessageListenerService onCreate: ConnectException=" + e.getMessage());
        }


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

        return START_STICKY;// this value for : this service will reCreate after this service is killed;
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
        message.setSubject(baseItem.getUserName());
        message.setBody(baseItem.getMesage());
        message.setFrom(Constants.AppConfigConstants.CLIENT_EMAIL);
        message.setTo(baseItem.getUserJID());
        chat.sendMessage(message);

        dataManagerInstance.collectMessages(dataManagerInstance.getAllUsersMessageRecords(), baseItem);

        createRecentChatRecord(baseItem);
    }


    @Override
    public void processMessage(Chat chat, Message message) {
        String JIDFromSingleUser = message.getFrom();
        if (JIDFromSingleUser.contains("/")) {
            JIDFromSingleUser = JIDFromSingleUser.split("/")[0];
        }
// Using a hashmap to manage the chats , which is very conenient to reuse;
        dataManagerInstance.getChats().put(JIDFromSingleUser, chat);

        String receivedMsg = message.getBody();
        String userName = message.getSubject();
        String stanzaId = message.getStanzaId();///////////////////////////////////////////


        if (userName == null || userName.trim().equals("")) {
            userName = JIDFromSingleUser.split("@")[0];
        }
        if (receivedMsg != null) {
            TextMessageItem messageItem = new TextMessageItem(userName
                    , SystemUtil.getCurrentSystemTime()
                    , receivedMsg, null, JIDFromSingleUser, MessageRvAdapter.TEXT_MESSAGE_VIEW_TYPE, true);

            if (!dataManagerInstance.getMessageNotificationIds().containsKey(JIDFromSingleUser)) {
                dataManagerInstance.getMessageNotificationIds().put(JIDFromSingleUser, ++singleUserJIDMessageId);
            }

            dataManagerInstance.collectMessages(dataManagerInstance.getAllUsersMessageRecords(), messageItem);

            createRecentChatRecord(messageItem);

            if (currentActivity != null && JIDFromSingleUser.equals(XmppHelper.getXmppHelperInStance().getCurrentChattingUserJID())) {
                BaseFragment currentFragment = currentActivity.getmCurrentFragment();
                if (currentFragment instanceof ChattingRoomFragment) {
                    ((ChattingRoomFragment) currentFragment).refreshMessageContainer(messageItem);
                }
            } else {

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.KeyConstants.IS_THIS_INTEN_FROM_PENDING_INTENT, true);
                intent.putParcelableArrayListExtra(Constants.KeyConstants.USER_MESSAGES_RECORD
                        , dataManagerInstance.getAllUsersMessageRecords().get(JIDFromSingleUser));

                PendingIntent pendingIntent = PendingIntent.getActivity(this, dataManagerInstance.getMessageNotificationIds().get(JIDFromSingleUser), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                Notification notification = new Notification.Builder(this)
                        .setAutoCancel(true)
                        .setContentText(receivedMsg)
                        .setContentTitle(userName)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.ic_launcher)//UserAvatar
                        .setContentIntent(pendingIntent)
                        .build();


                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(dataManagerInstance.getMessageNotificationIds().get(JIDFromSingleUser), notification);

            }


        }
    }

    // notify the RecentChatFragment create recentChatItem;
    private void createRecentChatRecord(BaseItem baseItem) {
        RecentChatItem recentChatItem = new RecentChatItem(baseItem.getUserName(), baseItem.getCurrentTimeStamp(), baseItem.getMesage(), baseItem.getUserAvatar(), baseItem.getUserJID());
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().postSticky(new MessageCreatedEvent(recentChatItem));
    }


    public void setCurrentActivity(BaseActivity currentActivity) {
        this.currentActivity = currentActivity;
    }


    public class MyBinder extends Binder {
        public InComingMessageListenerService getInComingMessageListenerService() {
            return InComingMessageListenerService.this;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 10, sticky = true)
    public void onDatasHaveArrivedChattingFragmnet(DatasHaveArrivedChattingFragmentEvent event) {
        dataManagerInstance.getMessageNotificationIds().remove(event.getUserJIDOfDatas());
        if (dataManagerInstance.getAllUsersMessageRecords().get(event.getUserJIDOfDatas()).size() > 100) {
//            此处将超出100的部分数据保存进数据库，并将将多出部分删除；
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        release resource

        try {
            XmppHelper.getXmppHelperInStance().logOut();
            XmppHelper.getXmppHelperInStance().getChatManager().removeChatListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataManagerInstance.clearDatas();
        Constants.AppConfigConstants.isNeedToLogin = true;

    }
}
