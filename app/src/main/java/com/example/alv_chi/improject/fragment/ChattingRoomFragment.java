package com.example.alv_chi.improject.fragment;

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
import com.example.alv_chi.improject.bean.ContactItem;
import com.example.alv_chi.improject.bean.TextMessageItem;
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.custom.IconfontTextView;
import com.example.alv_chi.improject.util.SystemUtil;
import com.example.alv_chi.improject.xmpp.XmppHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class ChattingRoomFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "ChattingRoomFragment";

    @BindView(R.id.itvMoreMessageType)
    IconfontTextView itvMoreMessageType;
    @BindView(R.id.etPenddingMessage)
    EditText etPenddingMessage;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.rvMessageContainer)
    RecyclerView rvMessageContainer;


    private ChatRoomActivity mHoldingActivity;
    private ContactItem contactItem;
    private String readyToBeSentMessage;

    private LinearLayoutManager linearLayoutManager;
    private MessageRvAdapter messageRvAdapter;
    private ArrayList<TextMessageItem> textMessageItems;

    public static ChattingRoomFragment newInstance(Bundle bundle) {
        ChattingRoomFragment chattingRoomFragment = new ChattingRoomFragment();
        chattingRoomFragment.setArguments(bundle);
        return chattingRoomFragment;
    }

    @Override
    protected void handleBundleFromOutside(Bundle bundle) {
        contactItem = bundle.getParcelable(Constants.KeyConstants.PARCELABLE_CONTACT_ITEM_KEY);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chatting_room;
    }

    @Override
    protected void initializeView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        btnSend.setOnClickListener(this);
        initViews();

    }

    private void initViews() {
        linearLayoutManager = new LinearLayoutManager(mHoldingActivity, LinearLayoutManager.VERTICAL, false);
        rvMessageContainer.setLayoutManager(linearLayoutManager);
        textMessageItems = new ArrayList<>();
        messageRvAdapter = new MessageRvAdapter(mHoldingActivity, textMessageItems);
        rvMessageContainer.setAdapter(messageRvAdapter);

        etPenddingMessage.setOnFocusChangeListener(this);
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
                String message = getReadyToBeSentMessage();
                etPenddingMessage.setText("");
                if (message == null || message.equals("")) {
                    Toast.makeText(mHoldingActivity, "不能发空信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    XmppHelper.getXmppHelperInStance().sendMessage(contactItem.getUserJID(), message, mHoldingActivity.getInComingMessageListenerService());
                    Log.e(TAG, "sendMessage: contactItem.getUserJID()=" + contactItem.getUserJID());
                    Log.e(TAG, "onClick: 发送成功");
                    TextMessageItem textMessageItem = new TextMessageItem(Constants.AppConfigConstants.CLIENT_USER_NAME
                            , SystemUtil.getCurrentSystemTime()
                            , message, null, MessageRvAdapter.TEXT_MESSAGE_VIEW_TYPE, false);
                    refreshMessageContainer(textMessageItem);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onClick: 发送shibai  ： " + e.getMessage());
                }
                break;
        }
    }


    public String getReadyToBeSentMessage() {
        Editable editableMessage = etPenddingMessage.getText();
        readyToBeSentMessage = editableMessage.toString();
        readyToBeSentMessage = readyToBeSentMessage.trim();
        return readyToBeSentMessage;
    }



    public void refreshMessageContainer(final TextMessageItem textMessageItem) {
        getHoldingActivity().getActivityHandler().post(new Runnable() {
            @Override
            public void run() {
                textMessageItems.add(textMessageItem);
                messageRvAdapter.notifyDataSetChanged();
                linearLayoutManager.scrollToPosition(textMessageItems.size() - 1);
            }
        });

    }




}
