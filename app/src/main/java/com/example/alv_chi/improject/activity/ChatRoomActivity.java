package com.example.alv_chi.improject.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.custom.IconfontTextView;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.eventbus.EventBusHelper;
import com.example.alv_chi.improject.eventbus.OnUserStatusChangeEvent;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.ChattingRoomFragment;
import com.example.alv_chi.improject.service.XmppListenerService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChatRoomActivity extends BaseActivity {


    private static final String TAG = "ChatRoomActivity";

    private XmppListenerService xmppListenerService;
    private ServiceConnection serviceConnection;
    private Intent serviceIntent;

    private BaseItem baseItem;
    private ArrayList<BaseItem> messages;
    private IconfontTextView itvToolbarRight;


    @Override
    protected void handleIntent(Intent intentFromLastContext) {

        messages = intentFromLastContext.getParcelableArrayListExtra(Constants.KeyConstants.USER_MESSAGES_RECORD);
        baseItem = messages.get(0);


    }

    @Override
    protected void intializeToolbar(ToolbarViewHolder toolbarViewHolder) {
        toolbarViewHolder.itvToolbarLeft.setText(R.string.left_arrow);
        toolbarViewHolder.itvToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTheTopFragmentFromBackStack();
            }
        });


        toolbarViewHolder.tvToolbarCenter.setText(baseItem.getUserName());
        itvToolbarRight = toolbarViewHolder.itvToolbarRight;

        itvToolbarRight.setText(R.string.is_user_online);

        HashMap<String, Boolean> isOnline = DataManager.getDataManagerInstance().getIsOnline();
        int size = isOnline .size();

        Log.e(TAG, "intializeToolbar: DataManager.getDataManagerInstance().getIsOnline().size="+ size);

        Set<Map.Entry<String, Boolean>> entries = isOnline.entrySet();
        for (Map.Entry<String, Boolean> entry : entries) {
            Log.e(TAG, "intializeToolbar:key/value ="+entry.getKey()+"/"+entry.getValue() );
        }

        setUserChattingToIsOnline(DataManager.getDataManagerInstance().getIsOnline().get(baseItem.getUserJID()));///////////？？？？？？？？？？有可能取出null


    }

    private void setUserChattingToIsOnline(boolean isOnline) {
        int color = -1;
        if (isOnline) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }

        itvToolbarRight.setTextColor(color);
    }


    //    this event is posted by XmppListenerService  ;
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 10, sticky = true)
    public void onUserStatusChange(OnUserStatusChangeEvent event) {
        Presence presence = event.getPresence();
        String JIDFrom = presence.getFrom();

        if (JIDFrom.contains("/"))
        {
            JIDFrom=JIDFrom.split("/")[0];
        }
//      update the DataManage
        DataManager.getDataManagerInstance().getIsOnline().put(JIDFrom, presence.isAvailable());
        if (baseItem.getUserJID().equals(JIDFrom)) {
            setUserChattingToIsOnline(presence.isAvailable());
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat_room;
    }

    @Override
    protected int getFragmentContainerId() {

        return R.id.flFragmentContainer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        initService();
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().register(this);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.KeyConstants.USER_MESSAGES_RECORD, messages);
        return ChattingRoomFragment.newInstance(bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentActivityToListenerService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        make the service let go the activity referrence
        setCurrentActivityToListenerService(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbind service release resource;
        unbindInComingMessageListenerService(serviceConnection);
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().unregister(this);
    }

    private void initService() {
        serviceIntent = new Intent(this, XmppListenerService.class);
        bindInComingMessageListenerService(serviceIntent);
    }

    private void setCurrentActivityToListenerService(ChatRoomActivity currentActivity) {
        if (getXmppListenerService() != null) {
            getXmppListenerService().setCurrentActivity(currentActivity);
        }
    }

    public void bindInComingMessageListenerService(Intent serviceIntent) {

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                xmppListenerService = ((XmppListenerService.MyBinder) iBinder).getInComingMessageListenerService();
                setCurrentActivityToListenerService(ChatRoomActivity.this);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, "onServiceDisconnected: ComponentName=" + name.toString());
            }
        };

        boolean isBindServiceSuccess = bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);


    }

    public void unbindInComingMessageListenerService(ServiceConnection serviceConn) {

        unbindService(serviceConn);
    }

    public XmppListenerService getXmppListenerService() {
        return xmppListenerService;
    }

}
