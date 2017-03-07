package com.example.alv_chi.improject.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alv_chi on 2017/3/7.
 */

public class RecentChatItem implements BaseItem, Parcelable {

    private static final String TAG="RecentChatItem";

    private String userName;
    private String lastMessageTimeStamp;
    private String lastMessage;

    private String currentTimeStamp;
    private String mesage;
    private Bitmap userAvatar;
    private String userJID;
    private int typeView;
    private boolean isReceivedMessage;

    public RecentChatItem(String userName, String lastMessageTimeStamp, String lastMessage, Bitmap userAvatar, String userJID) {
        this.userName = userName;
        this.lastMessageTimeStamp = lastMessageTimeStamp;
        this.lastMessage = lastMessage;
        this.userAvatar = userAvatar;
        this.userJID = userJID;
    }

    protected RecentChatItem(Parcel in) {
        userName = in.readString();
        lastMessageTimeStamp = in.readString();
        lastMessage = in.readString();
        currentTimeStamp = in.readString();
        mesage = in.readString();
        userAvatar = in.readParcelable(Bitmap.class.getClassLoader());
        userJID = in.readString();
        typeView = in.readInt();
        isReceivedMessage = in.readByte() != 0;
    }

    public static final Creator<RecentChatItem> CREATOR = new Creator<RecentChatItem>() {
        @Override
        public RecentChatItem createFromParcel(Parcel in) {
            return new RecentChatItem(in);
        }

        @Override
        public RecentChatItem[] newArray(int size) {
            return new RecentChatItem[size];
        }
    };

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getCurrentTimeStamp() {
        return null;
    }

    @Override
    public void setCurrentTimeStamp(String currentTimeStamp) {

    }

    @Override
    public String getMesage() {
        return null;
    }

    @Override
    public void setMesage(String mesage) {

    }

    public String getLastMessageTimeStamp() {
        return lastMessageTimeStamp;
    }

    public void setLastMessageTimeStamp(String lastMessageTimeStamp) {
        this.lastMessageTimeStamp = lastMessageTimeStamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public Bitmap getUserAvatar() {
        return userAvatar;
    }

    @Override
    public void setUserAvatar(Bitmap userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public String getUserJID() {
        return userJID;
    }

    @Override
    public void setUserJID(String userJID) {
        this.userJID = userJID;
    }

    @Override
    public int getTypeView() {
        return 0;
    }

    @Override
    public void setTypeView(int typeView) {

    }

    @Override
    public boolean isReceivedMessage() {
        return false;
    }

    @Override
    public void setReceivedMessage(boolean receivedMessage) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(lastMessageTimeStamp);
        dest.writeString(lastMessage);
        dest.writeString(currentTimeStamp);
        dest.writeString(mesage);
        dest.writeParcelable(userAvatar, flags);
        dest.writeString(userJID);
        dest.writeInt(typeView);
        dest.writeByte((byte) (isReceivedMessage ? 1 : 0));
    }


    @Override
    public boolean equals(Object obj) {

        return getUserJID().equals(((RecentChatItem) obj).getUserJID());
    }

}
