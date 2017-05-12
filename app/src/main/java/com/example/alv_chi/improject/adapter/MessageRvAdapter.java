package com.example.alv_chi.improject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.bean.MessageItem;
import com.example.alv_chi.improject.custom.CircleImageView;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.util.GlideUtil;

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
    private LinearLayout rootItemView;

    private List<MessageItem> data;

    public MessageRvAdapter(Context context, List<MessageItem> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(this.context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rootItemView = (LinearLayout) layoutInflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolder(rootItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TEXT_MESSAGE_VIEW_TYPE:
                showTextMessageInTheMessageContainer(holder, data.get(position));
                break;
            case PICTURE_MESSAGE_VIEW_TYPE:
                showPicMessageInTheMessageContainer(holder, data.get(position));
                break;
        }


    }

    private void showTextMessageInTheMessageContainer(ViewHolder viewHolder, MessageItem messageItem) {
        if (messageItem.isReceivedMessage()) {
            viewHolder.llYourMsg.setVisibility(View.GONE);
            viewHolder.ivPicFromOthers.setVisibility(View.GONE);
            viewHolder.llOthersMsg.setVisibility(View.VISIBLE);


            viewHolder.tvMessageFromOthers.setText(messageItem.getMesage());
            viewHolder.tvOtherUserName.setText(messageItem.getUserName());
        } else {
            viewHolder.llOthersMsg.setVisibility(View.GONE);
            viewHolder.llYourMsg.setVisibility(View.VISIBLE);
            viewHolder.ivPicFromYou.setVisibility(View.GONE);



            viewHolder.tvMessageFromYou.setText(messageItem.getMesage());
            viewHolder.tvYourUserName.setText(DataManager.getDataManagerInstance().getCurrentMasterUserName());
        }
        viewHolder.tvCurrentTime.setText(messageItem.getCurrentTimeStamp());

    }

    private void showPicMessageInTheMessageContainer(ViewHolder viewHolder, MessageItem messageItem) {
        if (messageItem.isReceivedMessage()) {
            viewHolder.llYourMsg.setVisibility(View.GONE);
            viewHolder.llOthersMsg.setVisibility(View.VISIBLE);
            viewHolder.tvMessageFromOthers.setVisibility(View.GONE);



            GlideUtil.loadImage(context,
                    messageItem.getImagePath(),viewHolder.ivPicFromOthers);
            viewHolder.tvOtherUserName.setText(messageItem.getUserName());
        } else {
            viewHolder.llOthersMsg.setVisibility(View.GONE);
            viewHolder.tvMessageFromYou.setVisibility(View.GONE);
            viewHolder.llYourMsg.setVisibility(View.VISIBLE);


            GlideUtil.loadImage(context,
                    messageItem.getImagePath(),viewHolder.ivPicFromYou);
            viewHolder.tvYourUserName.setText(DataManager.getDataManagerInstance().getCurrentMasterUserName());
        }
        viewHolder.tvCurrentTime.setText(messageItem.getCurrentTimeStamp());
//        Log.e(TAG, "showPicMessageInTheMessageContainer: messageItem.getImagePath()="+ messageItem.getImagePath() );

    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data == null ? -1 : data.get(position).getTypeView();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMessageFromYou)
        TextView tvMessageFromYou;
        @BindView(R.id.ivPicFromYou)
        ImageView ivPicFromYou;
        @BindView(R.id.tvYourUserName)
        TextView tvYourUserName;
        @BindView(R.id.civYourAvatar)
        CircleImageView civYourAvatar;
        @BindView(R.id.llYourMsg)
        LinearLayout llYourMsg;
        @BindView(R.id.tvOtherUserName)
        TextView tvOtherUserName;
        @BindView(R.id.civOthersAvatar)
        CircleImageView civOthersAvatar;
        @BindView(R.id.ivPicFromOthers)
        ImageView ivPicFromOthers;
        @BindView(R.id.tvMessageFromOthers)
        TextView tvMessageFromOthers;
        @BindView(R.id.llOthersMsg)
        LinearLayout llOthersMsg;
        @BindView(R.id.tvCurrentTime)
        TextView tvCurrentTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
