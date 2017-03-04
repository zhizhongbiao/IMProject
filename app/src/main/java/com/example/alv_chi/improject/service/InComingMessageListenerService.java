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
import com.example.alv_chi.improject.bean.TextMessageItem;
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.eventbus.DatasHaveArrivedChattingFragmentEvent;
import com.example.alv_chi.improject.eventbus.EventBusHelper;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.ChattingRoomFragment;
import com.example.alv_chi.improject.util.SystemUtil;
import com.example.alv_chi.improject.xmpp.XmppHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InComingMessageListenerService extends Service implements ChatManagerListener, ChatMessageListener {

    private static final String TAG = "InComingMessageService";
    private BaseActivity currentActivity;
    private int singleUserJIDMessageId;
    private HashMap<String, Integer> messageIdsFromAllUserJID = new HashMap<>();
    private HashMap<String, ArrayList<BaseItem>> messagesFromAllUserJID = new HashMap<>();


    @Override
    public void onCreate() {
        super.onCreate();

        try {
            XmppHelper.getXmppHelperInStance().getChatManager().addChatListener(InComingMessageListenerService.this);
            Log.e(TAG, "InComingMessageListenerService onCreate: 看看执行几次");
            Constants.AppConfigConstants.isNeedToLogin = false;
        } catch (ConnectException e) {
            e.printStackTrace();
            Log.e(TAG, "InComingMessageListenerService onCreate: ConnectException=" + e.getMessage());
        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().register(this);
        Log.e(TAG, "onBind: ");
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().unregister(this);
        Log.e(TAG, "onUnbind: ");
        return super.onUnbind(intent);
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
        chat.addMessageListener(this);
        Log.e(TAG, "chatCreated: createdLocally=" + createdLocally);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        String receivedMsg = message.getBody();
        String messageFromUserJID = message.getFrom();
        String userName = message.getSubject();
        String stanzaId = message.getStanzaId();


        if (userName == null || userName.trim().equals("")) {
            userName = messageFromUserJID.split("@")[0];
        }
        if (receivedMsg != null) {
            TextMessageItem messageItem = new TextMessageItem(userName
                    , SystemUtil.getCurrentSystemTime()
                    , receivedMsg, null, messageFromUserJID, MessageRvAdapter.TEXT_MESSAGE_VIEW_TYPE, true);


            if (!messageIdsFromAllUserJID.containsKey(messageFromUserJID)) {
                messageIdsFromAllUserJID.put(messageFromUserJID, ++singleUserJIDMessageId);
                ArrayList<BaseItem> singleJIDMessages = new ArrayList<>();
                singleJIDMessages.add(messageItem);
                messagesFromAllUserJID.put(messageFromUserJID, singleJIDMessages);
            } else {
                for (Map.Entry<String, ArrayList<BaseItem>> userJIDEntrySet : messagesFromAllUserJID.entrySet()) {

                    if (userJIDEntrySet.getKey().equals(messageFromUserJID)) {
                        userJIDEntrySet.getValue().add(messageItem);
                    }
                }
            }

            if (currentActivity != null && XmppHelper.getXmppHelperInStance().getCurrentChattingUserJID().equals(messageFromUserJID)) {
                BaseFragment currentFragment = currentActivity.getmCurrentFragment();
                if (currentFragment instanceof ChattingRoomFragment) {
                    ((ChattingRoomFragment) currentFragment).refreshMessageContainer(messageItem);
                }
            } else {

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.KeyConstants.IS_THIS_INTEN_FROM_PENDING_INTENT, true);
                intent.putParcelableArrayListExtra(Constants.KeyConstants.PARCELABLE_A_SERISE_MESSAGE_ITEM_KEY, messagesFromAllUserJID.get(messageFromUserJID));

                PendingIntent pendingIntent = PendingIntent.getActivity(this, messageIdsFromAllUserJID.get(messageFromUserJID), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                Notification notification = new Notification.Builder(this)
                        .setAutoCancel(true)
                        .setContentText(receivedMsg)
                        .setContentTitle(userName)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.ic_launcher)//UserAvatar
                        .setContentIntent(pendingIntent)
                        .build();


                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(messageIdsFromAllUserJID.get(messageFromUserJID), notification);

            }


        }
        Log.e(TAG, "processMessage: message=" + message);
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
        messagesFromAllUserJID.get(event.getUserJIDOfDatas()).clear();
        Log.e(TAG, "onDatasHaveArrivedChattingFragmnet:  messages.clear()");//这里不知道为什么只是执行第一次？？？？？？？？？？？？？？？？？？
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
        messageIdsFromAllUserJID = null;
        messagesFromAllUserJID = null;
        Constants.AppConfigConstants.isNeedToLogin = true;
        System.gc();
    }
}
