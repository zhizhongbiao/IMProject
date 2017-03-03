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
import com.example.alv_chi.improject.bean.TextMessageItem;
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.ChattingRoomFragment;
import com.example.alv_chi.improject.util.SystemUtil;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

public class InComingMessageListenerService extends Service implements ChatManagerListener, ChatMessageListener {

    private static final String TAG = "InComingMessageService";
    private static final int  DEFAULT_REQUEST_CODE =0;
    private BaseActivity currentActivity;

    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    /**
     *    this method may be called many times ,so be carefully to ues it .
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        chat.addMessageListener(this);
        Log.e(TAG, "chatCreated: createdLocally=" + createdLocally);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        String receivedMsg = message.getBody();
        String messageFrom = message.getFrom();
        String userName = message.getSubject();
        String stanzaId = message.getStanzaId();
        if (receivedMsg != null) {
            TextMessageItem messageItem = new TextMessageItem(userName
                    , SystemUtil.getCurrentSystemTime()
                    , receivedMsg, null, MessageRvAdapter.TEXT_MESSAGE_VIEW_TYPE, true);

            if (currentActivity!=null)
            {
                BaseFragment currentFragment = currentActivity.getmCurrentFragment();
                if (currentFragment instanceof ChattingRoomFragment)
                {
                    ((ChattingRoomFragment)currentFragment).refreshMessageContainer(messageItem);
                }
            }else
            {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.KeyConstants.IS_THIS_INTEN_FROM_PENDING_INTENT,true);
                intent.putExtra(Constants.KeyConstants.PARCELABLE_CONTACT_ITEM_KEY,)
                PendingIntent pendingIntent = PendingIntent.getActivity(this, DEFAULT_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                Notification notification = new Notification.Builder(this)
                        .setAutoCancel(true)
                        .setContentText(receivedMsg)
                        .setContentTitle(userName)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.meinv4)//UserAvatar
                        .setContentIntent(pendingIntent)
                        .build();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(DEFAULT_REQUEST_CODE,notification);
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

}
