package com.example.alv_chi.improject.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alv_chi on 2017/3/7.
 */

public class RecentChatItem implements BaseItem, Parcelable {



    private String userName;
    private String latestMessageTimeStamp;
    private String latestMessage;

    private String currentTimeStamp;
    private String mesage;
    private Bitmap userAvatar;
    private String userJID;
    private int typeView;
    private boolean isReceivedMessage;
    private boolean isOnline;

    public RecentChatItem(String userJID, String userName
            , Bitmap userAvatar, String latestMessage
            , String latestMessageTimeStamp, int typeView
            , boolean isReceivedMessage) {
        this.userJID = userJID;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.latestMessage = latestMessage;
        this.latestMessageTimeStamp = latestMessageTimeStamp;
        this.typeView = typeView;
        this.isReceivedMessage = isReceivedMessage;
    }

    protected RecentChatItem(Parcel in) {
        userName = in.readString();
        latestMessageTimeStamp = in.readString();
        latestMessage = in.readString();
        currentTimeStamp = in.readString();
        mesage = in.readString();
        userAvatar = in.readParcelable(Bitmap.class.getClassLoader());
        userJID = in.readString();
        typeView = in.readInt();
        isReceivedMessage = in.readByte() != 0;
        isOnline = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(latestMessageTimeStamp);
        dest.writeString(latestMessage);
        dest.writeString(currentTimeStamp);
        dest.writeString(mesage);
        dest.writeParcelable(userAvatar, flags);
        dest.writeString(userJID);
        dest.writeInt(typeView);
        dest.writeByte((byte) (isReceivedMessage ? 1 : 0));
        dest.writeByte((byte) (isOnline ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getLatestMessageTimeStamp() {
        return latestMessageTimeStamp;
    }

    public void setLatestMessageTimeStamp(String latestMessageTimeStamp) {
        this.latestMessageTimeStamp = latestMessageTimeStamp;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    @Override
    public String getCurrentTimeStamp() {
        return currentTimeStamp;
    }

    @Override
    public void setCurrentTimeStamp(String currentTimeStamp) {
        this.currentTimeStamp = currentTimeStamp;
    }

    @Override
    public String getMesage() {
        return mesage;
    }

    @Override
    public void setMesage(String mesage) {
        this.mesage = mesage;
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
        return typeView;
    }

    @Override
    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }

    @Override
    public boolean isReceivedMessage() {
        return isReceivedMessage;
    }

    @Override
    public void setReceivedMessage(boolean receivedMessage) {
        isReceivedMessage = receivedMessage;
    }

    @Override
    public boolean isOnline() {
        return isOnline;
    }

    @Override
    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public String getImagePath() {
        return "getImagePath";
    }


//    override this method to differ the different message
    @Override
    public boolean equals(Object obj) {

        return getUserJID().equals(((RecentChatItem) obj).getUserJID());
    }
}
