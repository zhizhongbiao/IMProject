package com.example.alv_chi.improject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.bean.ContactItem;
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.ChattingRoomFragment;

public class ChatRoomActivity extends BaseActivity {

    @Override
    protected void handleIntent(Intent intent) {
        ContactItem contactItem = (ContactItem) intent.getParcelableExtra(Constants.KeyConstants.PARCELABLE_CONTACTIEM_KEY);
        String userJID = contactItem.getUserJID();
        Bitmap avatar = contactItem.getAvatar();
        String userName = contactItem.getUserName();
        CurrentChattingDataHolder.setCurrentChattingData(contactItem,userJID, userName, avatar);
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
        toolbarViewHolder.tvToolbarCenter.setText(CurrentChattingDataHolder.getUserName());
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
    }

    @Override
    protected BaseFragment getFirstFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KeyConstants.PARCELABLE_CONTACTIEM_KEY,CurrentChattingDataHolder.getContactItem());
        return ChattingRoomFragment.newInstance(bundle);
    }


    static class CurrentChattingDataHolder {
        private static String userJID;
        private static String userName;
        private static Bitmap userAvatar;
        private static ContactItem contactItem;

        public static void setCurrentChattingData(ContactItem item,String JID, String name, Bitmap avatar) {
            userJID = JID;
            userName = name;
            userAvatar = avatar;
            contactItem=item;
        }

        public static String getUserJID() {
            return userJID;
        }

        public static String getUserName() {
            return userName;
        }

        public static Bitmap getUserAvatar() {
            return userAvatar;
        }

        public static ContactItem getContactItem() {
            return contactItem;
        }
    }
}
