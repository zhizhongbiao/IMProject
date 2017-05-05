package com.example.alv_chi.improject.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.activity.ChatRoomActivity;
import com.example.alv_chi.improject.adapter.MessageRvAdapter;
import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.bean.TextMessageItem;
import com.example.alv_chi.improject.custom.IconfontTextView;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.data.constant.Constants;
import com.example.alv_chi.improject.greendao.DataBaseUtil;
import com.example.alv_chi.improject.greendao.MessageRecord;
import com.example.alv_chi.improject.handler.HandlerHelper;
import com.example.alv_chi.improject.handler.OnThreadTaskFinishedListener;
import com.example.alv_chi.improject.util.SystemUtil;
import com.example.alv_chi.improject.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class ChattingRoomFragment extends BaseFragment implements View.OnClickListener
        , View.OnFocusChangeListener, OnThreadTaskFinishedListener {

    private static final String TAG = "ChattingRoomFragment";

    @BindView(R.id.itvMoreMessageType)
    IconfontTextView itvMoreMessageType;
    @BindView(R.id.etPenddingMessage)
    EditText etPenddingMessage;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.rvMessageContainer)
    RecyclerView rvMessageContainer;
    @BindView(R.id.pcfl)
    PtrClassicFrameLayout pcfl;

    private int factor = 1;



    private ChatRoomActivity mHoldingActivity;

    private String readyToBeSentMessage;

    private LinearLayoutManager linearLayoutManager;
    private MessageRvAdapter messageRvAdapter;
    private ArrayList<TextMessageItem> textMessageItems;
    private BaseItem baseItem;
    private List<MessageRecord> messageRecords;
    private int beforeSize;
    private int afterSize;


    public static ChattingRoomFragment newInstance(Bundle bundle) {
        ChattingRoomFragment chattingRoomFragment = new ChattingRoomFragment();
        chattingRoomFragment.setArguments(bundle);
        return chattingRoomFragment;
    }

    @Override
    protected void handleBundleFromOutside(Bundle bundle) {
        ArrayList<BaseItem> messages = bundle.getParcelableArrayList(Constants.KeyConstants.USER_MESSAGES_RECORD);
        if (messages != null) {

            boolean ifNeedGetMsgsFromDB = true;
            for (BaseItem message : messages) {
                if (message instanceof TextMessageItem) {
                    refreshMessageContainer(false, (TextMessageItem) message);
                    ifNeedGetMsgsFromDB = false;
                }
            }

            if (ifNeedGetMsgsFromDB) {
                getMsgRecords();
            }


            baseItem = messages.get(0);
//            XmppListenerService calls this method to clean the notification in the statebar
            DataManager.getDataManagerInstance().getXmppListenerService().cleanCurrentMessageNotification(baseItem.getUserJID());
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chatting_room;
    }

    @Override
    protected void initializeView(View rootView, Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        addThisOnThreadTaskFinishedListenerToActivityHandler();
        initViews();

    }

    private void initViews() {
        btnSend.setOnClickListener(this);
        etPenddingMessage.setOnFocusChangeListener(this);

        initialUltraPTR();

        linearLayoutManager = new LinearLayoutManager(mHoldingActivity, LinearLayoutManager.VERTICAL, false);
        rvMessageContainer.setLayoutManager(linearLayoutManager);
        textMessageItems = new ArrayList<>();
        messageRvAdapter = new MessageRvAdapter(mHoldingActivity, textMessageItems);
        rvMessageContainer.setAdapter(messageRvAdapter);

    }

    private void initialUltraPTR() {
//        PtrClassicDefaultHeader ptrClassicDefaultHeader = new PtrClassicDefaultHeader(mHoldingActivity);
        StoreHouseHeader storeHouseHeader = new StoreHouseHeader(mHoldingActivity);
        storeHouseHeader.setDropHeight(5)
                .setLineWidth(3)
                .setTextColor(Color.RED)
                .initWithString("load more Msgs...");

//        MaterialHeader materialHeader = new MaterialHeader(mHoldingActivity);
//        materialHeader.setColorSchemeColors(new int[]{Color.RED,Color.GREEN,Color.BLUE});
//        materialHeader.invalidateDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(),R.mipmap.meinv4)));

        pcfl.setHeaderView(storeHouseHeader);
        pcfl.addPtrUIHandler(storeHouseHeader);

        pcfl.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getMsgRecords();
            }
        });
    }

    private void getMsgRecords() {
        ThreadUtil.executeThreadTask(new Runnable() {
            @Override
            public void run() {
                if (baseItem != null) {

                    messageRecords = DataBaseUtil.getDataBaseInstance(mHoldingActivity.getApplicationContext()).retrive(20 * factor, new MessageRecord(null, baseItem.getUserName()
                            , DataManager.getDataManagerInstance().getCurrentMasterUserName(),
                            baseItem.getCurrentTimeStamp(), baseItem.getMesage(),
                            baseItem.getCurrentTimeStamp(), baseItem.getMesage(), baseItem.getUserJID(), baseItem.getTypeView()
                            , baseItem.isReceivedMessage(), baseItem.isOnline()));
                    if (messageRecords == null) {
                        HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.FAILURE);
                        Log.e(TAG, "initialUltraPTR run: messageRecords==null");
                    } else {
                        HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.SUCCESS);
                    }

                } else {
                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.FAILURE);
                    Log.e(TAG, "initialUltraPTR run: baseItem==null");
                }

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        DataManager.getDataManagerInstance().setCurrentChattingUserJID(baseItem.getUserJID());
    }


    @Override
    public void onStop() {
        super.onStop();
        DataManager.getDataManagerInstance().setCurrentChattingUserJID("");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            SystemUtil.showSoftInput(mHoldingActivity, v);
//            linearLayoutManager.scrollToPosition(textMessageItems.size()-1);
        } else {
            SystemUtil.hideSoftInput(mHoldingActivity, v);
//            linearLayoutManager.scrollToPosition(textMessageItems.size()-1);
        }

    }

    @Override
    protected void castActivity(BaseActivity baseActivity) {
        mHoldingActivity = (ChatRoomActivity) baseActivity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                sendMsgs();
                break;
        }
    }

    private void sendMsgs() {
        String message = getReadyToBeSentMessage();
        etPenddingMessage.setText("");
        if (message == null || message.equals("")) {
            Toast.makeText(mHoldingActivity, "不能发空信息", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            TextMessageItem textMessageItem = new TextMessageItem(baseItem.getUserName()
                    , SystemUtil.getCurrentSystemTime()
                    , message, null, baseItem.getUserJID(), MessageRvAdapter.TEXT_MESSAGE_VIEW_TYPE, false, baseItem.isOnline());

            DataManager.getDataManagerInstance().getXmppListenerService().sendMessage(textMessageItem);
            refreshMessageContainer(false, textMessageItem);
//                    Log.e(TAG, "onClick: 发送成功 sendMessage: contactItem.getUserJID()=" + baseItem.getUserJID());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "sendMsgs: 发送消息失败 Exception=" + e.getMessage());
            Toast.makeText(mHoldingActivity, "your account might have been logined in other device or Network is unavailable", Toast.LENGTH_SHORT).show();
            mHoldingActivity.showTheWarningText("you are offline , please login again");
        }

    }


    public String getReadyToBeSentMessage() {
        Editable editableMessage = etPenddingMessage.getText();
        readyToBeSentMessage = editableMessage.toString();
        readyToBeSentMessage = readyToBeSentMessage.trim();
        return readyToBeSentMessage;
    }


    public synchronized void refreshMessageContainer(final boolean isFromDatabase, final TextMessageItem textMessageItem) {
        getHoldingActivity().getMyHandler().post(new Runnable() {
            @Override
            public void run() {
                if (isFromDatabase) {
                    textMessageItems.add(0, textMessageItem);
                    int currentSize = textMessageItems.size();
                    if (factor == 2) {
                        linearLayoutManager.scrollToPosition(textMessageItems.size() - 1);
//                        Log.e(TAG, "run:  showMsgInTheRvBottom factor="+factor );
                    } else if (currentSize > beforeSize) {
                        linearLayoutManager.scrollToPosition(currentSize - beforeSize);//不滚动到相应的位置问题有待解决
//                        Log.e(TAG, "run: deltaSize="+(currentSize-beforeSize) );
                    }
                } else {
                    textMessageItems.add(textMessageItem);
                    linearLayoutManager.scrollToPosition(textMessageItems.size() - 1);

                }
                messageRvAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onThreadTaskFinished(int messageType) {

        switch (messageType) {
            case Constants.HandlerMessageType.SUCCESS:
                factor++;
                showTheMsgRecordFromDB();
                break;
            case Constants.HandlerMessageType.FAILURE:
                Toast.makeText(mHoldingActivity, "加载消息记录出错", Toast.LENGTH_SHORT).show();
                break;
        }

        pcfl.refreshComplete();
    }

    private void showTheMsgRecordFromDB() {
        afterSize = messageRecords.size();
        beforeSize = textMessageItems.size();
        if (afterSize == beforeSize) {
            Toast.makeText(mHoldingActivity, "无更多消息记录", Toast.LENGTH_SHORT).show();
        } else {
            textMessageItems.clear();
            DataManager.getDataManagerInstance().getAllUsersMessageRecords().get(baseItem.getUserJID()).clear();
            for (MessageRecord messageRecord : messageRecords) {
                TextMessageItem textMessageItem = new TextMessageItem(messageRecord.getUserName(), messageRecord.getLatestMessageTimeStamp()
                        , messageRecord.getMesage(), null, messageRecord.getUserJID(), messageRecord.getTypeView()
                        , messageRecord.getIsReceivedMessage(), messageRecord.getIsOnline());
                refreshMessageContainer(true, textMessageItem);
                DataManager.getDataManagerInstance().getAllUsersMessageRecords().get(baseItem.getUserJID()).add(0, textMessageItem);
            }
        }
    }

    @Override
    public void addThisOnThreadTaskFinishedListenerToActivityHandler() {
        mHandler.addListeners(TAG, this);
    }

    @Override
    public void removeThisOnThreadTaskFinishedListenerFromActivityHandler() {
        mHandler.removeListener(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeThisOnThreadTaskFinishedListenerFromActivityHandler();
        msgRecordsBackToOriginalState();
    }

    private void msgRecordsBackToOriginalState() {
        factor = 1;
        ArrayList<BaseItem> msgRecords = DataManager.getDataManagerInstance().getAllUsersMessageRecords().get(baseItem.getUserJID());
        while (msgRecords != null && msgRecords.size() > 30) {
            msgRecords.remove(0);
        }
    }
}
