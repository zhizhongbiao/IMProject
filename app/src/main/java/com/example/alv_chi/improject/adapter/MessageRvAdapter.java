package com.example.alv_chi.improject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.bean.TextMessageItem;
import com.example.alv_chi.improject.custom.CircleImageView;
import com.example.alv_chi.improject.data.DataManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Alv_chi on 2017/2/22.
 */

public class MessageRvAdapter extends RecyclerView.Adapter<MessageRvAdapter.ViewHolder> {

    public static final String TAG = "MessageRvAdapter";
    public static final int TEXT_MESSAGE_VIEW_TYPE = 3;
    public static final int PICTURE_MESSAGE_VIEW_TYPE = 4;
    private Context context;
    private LayoutInflater layoutInflater;
    private FrameLayout rootItemView;

    private List<TextMessageItem> data;

    public MessageRvAdapter(Context context, List<TextMessageItem> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(this.context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rootItemView = (FrameLayout) layoutInflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolder(rootItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position))
        {
            case TEXT_MESSAGE_VIEW_TYPE:
                showTextMessageInTheMessageContainer(holder,data.get(position));
                break;
        }
    }

    private void showTextMessageInTheMessageContainer(ViewHolder viewHolder, TextMessageItem textMessageItem) {
        if (!textMessageItem.isReceivedMessage()) {
            viewHolder.llOtherTextMessage.setVisibility(View.GONE);
            viewHolder.llYourTextMessage.setVisibility(View.VISIBLE);

            viewHolder.tvYourCurrentTime.setText(textMessageItem.getCurrentTimeStamp());
            viewHolder.tvYourChattingMessage.setText(textMessageItem.getMesage());
            viewHolder.tvYourUserName.setText(DataManager.getDataManagerInstance().getCurrentMasterUserName());
        } else {
            viewHolder.llYourTextMessage.setVisibility(View.GONE);
            viewHolder.llOtherTextMessage.setVisibility(View.VISIBLE);

            viewHolder.tvOtherCurrentTime.setText(textMessageItem.getCurrentTimeStamp());
            viewHolder.tvOtherChattingMessage.setText(textMessageItem.getMesage());
            viewHolder.tvOtherUserName.setText(textMessageItem.getUserName());
        }


    }


    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data==null?-1: data.get(position).getTypeView();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOtherCurrentTime)
        TextView tvOtherCurrentTime;
        @BindView(R.id.tvOtherUserName)
        TextView tvOtherUserName;
        @BindView(R.id.civOtherAvatar)
        CircleImageView civOtherAvatar;
        @BindView(R.id.tvOtherChattingMessage)
        TextView tvOtherChattingMessage;
        @BindView(R.id.llOtherTextMessage)
        LinearLayout llOtherTextMessage;
        @BindView(R.id.tvYourCurrentTime)
        TextView tvYourCurrentTime;
        @BindView(R.id.tvYourUserName)
        TextView tvYourUserName;
        @BindView(R.id.tvYourChattingMessage)
        TextView tvYourChattingMessage;
        @BindView(R.id.civYourAvatar)
        CircleImageView civYourAvatar;
        @BindView(R.id.llYourTextMessage)
        LinearLayout llYourTextMessage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
