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
import com.example.alv_chi.improject.eventbus.EventBusHelper;
import com.example.alv_chi.improject.eventbus.OnUserChattingToStatusEvent;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.ChattingRoomFragment;
import com.example.alv_chi.improject.service.InComingMessageListenerService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;

public class ChatRoomActivity extends BaseActivity {


    private static final String TAG = "ChatRoomActivity";

    private InComingMessageListenerService inComingMessageListenerService;
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

        boolean isOnline = baseItem.isOnline();

        setUserChattingToIsOnline(isOnline);


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


    //    this event is posted by ContactsFragment  ;
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 10, sticky = false)
    public void onUserChattingToChangeStatus(OnUserChattingToStatusEvent event) {
        Presence presence = event.getPresence();
        if (baseItem.getUserJID().equals(presence.getFrom())) {
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
        serviceIntent = new Intent(this, InComingMessageListenerService.class);
        bindInComingMessageListenerService(serviceIntent);
    }

    private void setCurrentActivityToListenerService(ChatRoomActivity currentActivity) {
        if (getInComingMessageListenerService() != null) {
            getInComingMessageListenerService().setCurrentActivity(currentActivity);
        }
    }

    public void bindInComingMessageListenerService(Intent serviceIntent) {

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                inComingMessageListenerService = ((InComingMessageListenerService.MyBinder) iBinder).getInComingMessageListenerService();
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

    public InComingMessageListenerService getInComingMessageListenerService() {
        return inComingMessageListenerService;
    }

}
