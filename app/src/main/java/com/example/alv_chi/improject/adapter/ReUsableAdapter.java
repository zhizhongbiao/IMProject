package com.example.alv_chi.improject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.bean.ContactItem;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Alv_chi on 2017/2/22.
 */

public class ReUsableAdapter extends RecyclerView.Adapter {

    public static final int CONTACTS_ITEM_VIEW_TYPE = 0;
    public static final String TAG = "ReUsableAdapter";

    private int viewType = -1;
    private Context context;
    private List<ContactItem> contactItems;
    private HashMap<String, Integer> navigationPositions = new HashMap();
    private LayoutInflater layoutInflater;


    public List<ContactItem> getContactItems() {
        return contactItems;
    }
    public void setContactItems(List<ContactItem> contactItems) {
        this.contactItems = contactItems;
        pickTheNavigationLetterOfFirstContactItem(contactItems);
    }

    public HashMap<String, Integer> getNavigationPositions() {
        return navigationPositions;
    }

    public ReUsableAdapter(Context context, List<ContactItem> contactItems, int viewType) {
        this.context = context;
        this.contactItems=contactItems;
        this.viewType = viewType;
        layoutInflater = LayoutInflater.from(this.context);


        pickTheNavigationLetterOfFirstContactItem(contactItems);

    }

    private void pickTheNavigationLetterOfFirstContactItem(List<ContactItem> contactItems) {
        if (contactItems==null)
        {
            Log.e(TAG, "ReUsableAdapter: contactItems="+contactItems );
            return;
        }

        for (int i = 0; i < contactItems.size(); i++) {
            String navigationLetter = contactItems.get(i).getNavigationLetter();
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
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (getItemViewType(position)) {
            case CONTACTS_ITEM_VIEW_TYPE:
                initContactItem((ContactsViewHolder) holder, position);
                break;
        }
    }

    private void initContactItem(ContactsViewHolder holder, final int position) {
        final ContactItem contactItem = contactItems.get(position);
        String navigationLetter = contactItem.getNavigationLetter();
        String name = contactItem.getUserName();
        ContactsViewHolder contactsViewHolder = holder;
        contactsViewHolder.llContactItemRoot.setOnClickListener(new OnRvRecentChatAndRvContactsAdapterItemClickListener(context, contactItem));
        contactsViewHolder.tvContactName.setText(name);
        Integer naviLetterPosition = navigationPositions.get(navigationLetter);
        if (naviLetterPosition !=null&& naviLetterPosition== position) {
            contactsViewHolder.tvLetterNavigation.setVisibility(View.VISIBLE);
            contactsViewHolder.tvLetterNavigation.setText(navigationLetter);
        } else {
            contactsViewHolder.tvLetterNavigation.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return contactItems == null ? 0 : contactItems.size();
    }


    @Override
    public int getItemViewType(int position) {
        return this.viewType;
    }

//    @Override
//    public void onClick(View v) {
//        switch (getItemViewType(currentPosition))
//        {
//            case CONTACTS_ITEM_VIEW_TYPE:
//                Intent intent = new Intent(context, ChatRoomActivity.class);
////                Bundle bundle = new Bundle();
////                bundle.putParcelable("ContactItem",contactItems.get(currentPosition));
//                intent.putExtra(Constants.KeyConstants.PARCELABLE_BASE_ITEM_KEY,contactItems.get(currentPosition));
//                context.startActivity(intent);
//                break;
//        }
//    }

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

}
