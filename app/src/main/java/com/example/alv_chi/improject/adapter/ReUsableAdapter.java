package com.example.alv_chi.improject.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.bean.ContactItem;
import com.example.alv_chi.improject.bean.GroupChatItem;
import com.example.alv_chi.improject.custom.CircleImageView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Alv_chi on 2017/2/22.
 */

public class ReUsableAdapter extends RecyclerView.Adapter {

    public static final int CONTACTS_ITEM_VIEW_TYPE = 0;
    public static final int GROUP_CHAT_ITEM_VIEW_TYPE = 1;
    public static final String TAG = "ReUsableAdapter";

    private int viewType = -1;
    private Context context;
    private List<BaseItem> datas;
    private HashMap<String, Integer> navigationPositions = new HashMap();
    private LayoutInflater layoutInflater;


    public List<BaseItem> getDatas() {
        return datas;
    }

    public void setDatas(List<BaseItem> datas) {
        this.datas = datas;
        pickTheNavigationLetterOfFirstContactItem(datas);
    }

    public HashMap<String, Integer> getNavigationPositions() {
        return navigationPositions;
    }

    public ReUsableAdapter(Context context, List<BaseItem> datas, int viewType) {
        this.context = context;
        this.viewType = viewType;
        this.datas = datas;
        layoutInflater = LayoutInflater.from(this.context);
        if (viewType==CONTACTS_ITEM_VIEW_TYPE)
        {
            pickTheNavigationLetterOfFirstContactItem(datas);
        }
    }

    private void pickTheNavigationLetterOfFirstContactItem(List<BaseItem> contactItems) {
        if (contactItems == null) {
            Log.e(TAG, "ReUsableAdapter: datas=" + contactItems);
            return;
        }

        for (int i = 0; i < contactItems.size(); i++) {
            String navigationLetter = ((ContactItem) contactItems.get(i)).getNavigationLetter();
            if (!navigationPositions.containsKey(navigationLetter)) {
                navigationPositions.put(navigationLetter, i);
            }
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case CONTACTS_ITEM_VIEW_TYPE:
                itemView = layoutInflater.inflate(R.layout.item_contact, parent, false);
                viewHolder = new ContactsViewHolder(itemView);
                break;
            case GROUP_CHAT_ITEM_VIEW_TYPE:
//                Share the item layout with recentChatRecord
                itemView = layoutInflater.inflate(R.layout.item_recent_chat, parent, false);
                viewHolder = new GroupChatViewHolder(itemView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (getItemViewType(position)) {
            case CONTACTS_ITEM_VIEW_TYPE:
                initContactItem((ContactsViewHolder) holder, position);
                break;
            case GROUP_CHAT_ITEM_VIEW_TYPE:
                GroupChatViewHolder groupChatViewHolder = (GroupChatViewHolder) holder;
                GroupChatItem groupChatItem = (GroupChatItem) datas.get(position);
                groupChatViewHolder.tvContactName.setText(groupChatItem.getRoomName());
                groupChatViewHolder.civChatMateAvatar.setImageResource(R.mipmap.meinv4);
                groupChatViewHolder.tvTheLatestMassage.setText(groupChatItem.getUserNameTheMsgFrom()+" : "+groupChatItem.getLatestMessage());

                break;
        }
    }

    private void initContactItem(ContactsViewHolder holder, final int position) {
        final ContactItem contactItem = ((ContactItem) datas.get(position));
        String navigationLetter = contactItem.getNavigationLetter();
        String name = contactItem.getUserName();
        ContactsViewHolder contactsViewHolder = holder;
        contactsViewHolder.llContactItemRoot.setOnClickListener(new OnRvRecentChatAndRvContactsAdapterItemClickListener(context, contactItem));
        contactsViewHolder.tvContactName.setText(name);
        Integer naviLetterPosition = navigationPositions.get(navigationLetter);
        if (naviLetterPosition != null && naviLetterPosition == position) {
            contactsViewHolder.tvLetterNavigation.setVisibility(View.VISIBLE);
            contactsViewHolder.tvLetterNavigation.setText(navigationLetter);
        } else {
            contactsViewHolder.tvLetterNavigation.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    @Override
    public int getItemViewType(int position) {
        return this.viewType;
    }


    class ContactsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvLetterNavigation)
        TextView tvLetterNavigation;
        @BindView(R.id.civChatMateAvatar)
        ImageView civChatMateAvatar;
        @BindView(R.id.tvContactName)
        TextView tvContactName;

        @BindView(R.id.llContactItemRoot)
        LinearLayout llContactItemRoot;

        ContactsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

     class GroupChatViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civChatMateAvatar)
        CircleImageView civChatMateAvatar;
        @BindView(R.id.tvContactName)
        TextView tvContactName;
        @BindView(R.id.tvTheLatestMassage)
        TextView tvTheLatestMassage;
        @BindView(R.id.tvTheLatestMessageTimeStamp)
        TextView tvTheLatestMessageTimeStamp;
        @BindView(R.id.cvRecentChatItemRootViewView)
        CardView cvRecentChatItemRootViewView;

         GroupChatViewHolder(View view) {
             super(view);
             ButterKnife.bind(this, view);
        }
    }
}
