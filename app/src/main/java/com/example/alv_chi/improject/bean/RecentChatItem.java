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
    private String latestMessageTimeStamp;
    private String latestMessage;

    private String currentTimeStamp;
    private String mesage;
    private Bitmap userAvatar;
    private String userJID;
    private int typeView;
    private boolean isReceivedMessage;
    private boolean isOnline;

    public RecentChatItem(String userName, String latestMessageTimeStamp, String latestMessage, Bitmap userAvatar, String userJID, boolean isOnline) {
        this.userName = userName;
        this.latestMessageTimeStamp = latestMessageTimeStamp;
        this.latestMessage = latestMessage;
        this.userAvatar = userAvatar;
        this.userJID = userJID;
        this.isOnline = isOnline;
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
    public boolean isOnline() {
        return isOnline;
    }

    @Override
    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public String getImagePath() {
        return null;
    }

    //    override this method to differ the different message
    @Override
    public boolean equals(Object obj) {

        return getUserJID().equals(((RecentChatItem) obj).getUserJID());
    }

}
