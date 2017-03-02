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
import com.example.alv_chi.improject.util.GetSystemParameterUtil;
import com.example.alv_chi.improject.xmpp.XmppHelper;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class ChattingRoomFragment extends BaseFragment implements View.OnClickListener, ChatManagerListener, ChatMessageListener {

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
        contactItem = (ContactItem) bundle.getParcelable(Constants.KeyConstants.PARCELABLE_CONTACTIEM_KEY);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chatting_room;
    }

    @Override
    protected void initializeView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        btnSend.setOnClickListener(this);
        initMessageRecyclerView();

    }

    private void initMessageRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(mHoldingActivity, LinearLayoutManager.VERTICAL, false);
        rvMessageContainer.setLayoutManager(linearLayoutManager);
        textMessageItems = new ArrayList<>();
        messageRvAdapter = new MessageRvAdapter(mHoldingActivity, textMessageItems);
        rvMessageContainer.setAdapter(messageRvAdapter);
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
                    XmppHelper.getXmppHelperInStance().sendMessage(contactItem.getUserJID(), message, this);
                    Log.e(TAG, "sendMessage: contactItem.getUserJID()=" + contactItem.getUserJID());
                    Log.e(TAG, "onClick: 发送成功");
                    TextMessageItem textMessageItem = new TextMessageItem(Constants.AppConfigConstants.CLIENT_USER_NAME
                            , GetSystemParameterUtil.getCurrentSystemTime()
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


    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        chat.addMessageListener(this);
        Log.e(TAG, "chatCreated: createdLocally=" + createdLocally);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        String receivedMsg = message.getBody();
        if (receivedMsg != null) {
            TextMessageItem textMessageItem = new TextMessageItem(contactItem.getUserName()
                    , GetSystemParameterUtil.getCurrentSystemTime()
                    , receivedMsg, null, MessageRvAdapter.TEXT_MESSAGE_VIEW_TYPE, true);

            refreshMessageContainer(textMessageItem);
        }
        Log.e(TAG, "processMessage: message=" + message);
    }

    private void refreshMessageContainer(final TextMessageItem textMessageItem) {
        getHoldingActivity().getActivityHandler().post(new Runnable() {
            @Override
            public void run() {
                textMessageItems.add(textMessageItem);
                messageRvAdapter.notifyDataSetChanged();
                linearLayoutManager.scrollToPosition(textMessageItems.size() - 1);
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
