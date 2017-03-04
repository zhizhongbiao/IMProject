package com.example.alv_chi.improject.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.ChattingRoomFragment;
import com.example.alv_chi.improject.service.InComingMessageListenerService;

import java.util.ArrayList;

public class ChatRoomActivity extends BaseActivity {


    private static final String TAG = "ChatRoomActivity";

    private InComingMessageListenerService inComingMessageListenerService;
    private ServiceConnection serviceConnection;
    private Intent serviceIntent;

    private BaseItem baseItem;
    private ArrayList<BaseItem> messages;
    private boolean isFromPendingIntent;

    @Override
    protected void handleIntent(Intent intentFromLastContext) {
        isFromPendingIntent = intentFromLastContext.getBooleanExtra(Constants.KeyConstants.IS_THIS_INTEN_FROM_PENDING_INTENT, false);

        if (isFromPendingIntent) {
            messages = intentFromLastContext.getParcelableArrayListExtra(Constants.KeyConstants.PARCELABLE_A_SERISE_MESSAGE_ITEM_KEY);
            baseItem = messages.get(0);
        } else {
            baseItem = intentFromLastContext.getParcelableExtra(Constants.KeyConstants.PARCELABLE_BASE_ITEM_KEY);
        }




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
    }

    @Override
    protected BaseFragment getFirstFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KeyConstants.PARCELABLE_BASE_ITEM_KEY, baseItem);
        if (isFromPendingIntent)
        {
            bundle.putParcelableArrayList(Constants.KeyConstants.PARCELABLE_A_SERISE_MESSAGE_ITEM_KEY, messages);
            bundle.putBoolean(Constants.KeyConstants.IS_THIS_INTEN_FROM_PENDING_INTENT,true);
        }

        return ChattingRoomFragment.newInstance(bundle);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        make the service let go the activity referrence
        if (inComingMessageListenerService!=null)
        {
            inComingMessageListenerService.setCurrentActivity(null);

        }
        unbindInComingMessageListenerService(serviceConnection);

    }

    private void initService() {
        serviceIntent = new Intent(this, InComingMessageListenerService.class);
        bindInComingMessageListenerService(serviceIntent);
    }


    public void bindInComingMessageListenerService(Intent serviceIntent) {

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                inComingMessageListenerService = ((InComingMessageListenerService.MyBinder) iBinder).getInComingMessageListenerService();
                inComingMessageListenerService.setCurrentActivity(ChatRoomActivity.this);


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, "onServiceDisconnected: ComponentName=" + name.toString());
            }
        };

        boolean isBindServiceSuccess = bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        Log.e(TAG, "runService: isBindServiceSuccess=" + isBindServiceSuccess);

    }

    public void unbindInComingMessageListenerService(ServiceConnection serviceConn) {

        unbindService(serviceConn);

    }


}
