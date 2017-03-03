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

    private ContactItem contactItem;

    @Override
    protected void handleIntent(Intent intent) {

        contactItem = intent.getParcelableExtra(Constants.KeyConstants.PARCELABLE_CONTACT_ITEM_KEY);
        String userJID = contactItem.getUserJID();
        Bitmap avatar = contactItem.getAvatar();
        String userName = contactItem.getUserName();

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

        toolbarViewHolder.tvToolbarCenter.setText(contactItem.getUserName());
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
        bundle.putParcelable(Constants.KeyConstants.PARCELABLE_CONTACT_ITEM_KEY,contactItem);
        return ChattingRoomFragment.newInstance(bundle);
    }


}
