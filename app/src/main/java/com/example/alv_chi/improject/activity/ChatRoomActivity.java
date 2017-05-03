package com.example.alv_chi.improject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.custom.IconfontTextView;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.data.constant.Constants;
import com.example.alv_chi.improject.eventbus.OnUserStatusChangeEvent;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.ChattingRoomFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;

public class ChatRoomActivity extends BaseActivity {


    private static final String TAG = "ChatRoomActivity";


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


//        just for testing
//        HashMap<String, Boolean> isOnLines = DataManager.getDataManagerInstance().getIsOnline();
//        Set<Map.Entry<String, Boolean>> entries = isOnLines.entrySet();
//        for (Map.Entry<String, Boolean> entry : entries) {
//            Log.e(TAG, "intializeToolbar:key/value ="+entry.getKey()+"/"+entry.getValue() );
//        }
        Boolean isOnline = DataManager.getDataManagerInstance().getIsOnline().get(baseItem.getUserJID());

        if (isOnline == null) {
            Log.e(TAG, "intializeToolbar: isOnline==null --  baseItem.getUserJID()" +
                    "/DataManager.getDataManagerInstance().getIsOnline().size="
                    + baseItem.getUserJID()
                    + "/" + DataManager.getDataManagerInstance().getIsOnline().size());

            DataManager.getDataManagerInstance().getXmppListenerService().initializeContactsData();
            isOnline = DataManager.getDataManagerInstance().getIsOnline().get(baseItem.getUserJID());
        }else
        {
            Log.e(TAG, "intializeToolbar: "+baseItem.getUserJID()+"-isOnLine="+isOnline );
        }
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


    //    this event is posted by XmppListenerService  ;
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 10, sticky = true)
    public void onUserStatusChange(OnUserStatusChangeEvent event) {
//        consume the stiky event and no more notification from the same EventPost
        EventBus.getDefault().removeStickyEvent(event);
        Presence presence = event.getPresence();
        String JIDFrom = presence.getFrom();

        if (JIDFrom.contains("/")) {
            JIDFrom = JIDFrom.split("/")[0];
        }

        if (baseItem.getUserJID().equals(JIDFrom)) {
            setUserChattingToIsOnline(presence.isAvailable());
        }
    }

    @Override
    protected int getActivityLayoutId() {
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
        EventBus.getDefault().register(this);
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
        DataManager.getDataManagerInstance().getXmppListenerService().setCurrentActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        make the service let go the activity referrence
        DataManager.getDataManagerInstance().getXmppListenerService().setCurrentActivity(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
