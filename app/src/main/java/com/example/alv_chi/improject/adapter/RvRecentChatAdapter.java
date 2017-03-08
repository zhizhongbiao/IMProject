package com.example.alv_chi.improject.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.bean.RecentChatItem;
import com.example.alv_chi.improject.custom.CircleImageView;
import com.example.alv_chi.improject.data.DataManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Alv_chi on 2017/2/22.
 */

public class RvRecentChatAdapter extends RecyclerView.Adapter<RvRecentChatAdapter.RecentChatItemViewHolder> {


    public static final String TAG = "RvRecentChatAdapter";
    private final ArrayList<RecentChatItem> recentChats;

    private Context context;
    private LayoutInflater layoutInflater;


    public RvRecentChatAdapter(Context context) {
        this.context = context;
        this.recentChats = DataManager.getDataManagerInstance().getRecentChats();
        layoutInflater = LayoutInflater.from(this.context);
    }


    @Override
    public RecentChatItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_recent_chat, parent, false);
        return new RecentChatItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecentChatItemViewHolder holder, int position) {
        RecentChatItem recentChatItem = recentChats.get(position);
        holder.tvTheLatestMessageTimeStamp.setText(recentChatItem.getLastMessageTimeStamp());
        holder.tvContactName.setText(recentChatItem.getUserName());
        holder.tvTheLatestMassage.setText(recentChatItem.getLastMessage());

        holder.cvRecentChatItemRootViewView.setOnClickListener(new OnRvRecentChatAndRvContactsAdapterItemClickListener(context, recentChatItem));

    }


    @Override
    public int getItemCount() {
        return recentChats == null ? 0 : recentChats.size();
    }


    class RecentChatItemViewHolder extends RecyclerView.ViewHolder {
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

        RecentChatItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
