package com.example.alv_chi.improject.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alv_chi on 2017/3/7.
 */

public class GroupChatItem implements BaseItem, Parcelable {



    private String roomName;
    private String userNameTheMsgFrom;
    private String latestMessageTimeStamp;
    private String latestMessage;
    private String mesage;
    private Bitmap userAvatar;
    private String userJID;
    private int typeView;
    private boolean isReceivedMessage;

    public GroupChatItem(String roomName, String userNameTheMsgFrom, String latestMessageTimeStamp, String latestMessage) {
        this.roomName = roomName;
        this.userNameTheMsgFrom = userNameTheMsgFrom;
        this.latestMessageTimeStamp = latestMessageTimeStamp;
        this.latestMessage = latestMessage;
    }

    protected GroupChatItem(Parcel in) {
        roomName = in.readString();
        userNameTheMsgFrom = in.readString();
        latestMessageTimeStamp = in.readString();
        latestMessage = in.readString();
        mesage = in.readString();
        userAvatar = in.readParcelable(Bitmap.class.getClassLoader());
        userJID = in.readString();
        typeView = in.readInt();
        isReceivedMessage = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomName);
        dest.writeString(userNameTheMsgFrom);
        dest.writeString(latestMessageTimeStamp);
        dest.writeString(latestMessage);
        dest.writeString(mesage);
        dest.writeParcelable(userAvatar, flags);
        dest.writeString(userJID);
        dest.writeInt(typeView);
        dest.writeByte((byte) (isReceivedMessage ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupChatItem> CREATOR = new Creator<GroupChatItem>() {
        @Override
        public GroupChatItem createFromParcel(Parcel in) {
            return new GroupChatItem(in);
        }

        @Override
        public GroupChatItem[] newArray(int size) {
            return new GroupChatItem[size];
        }
    };

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserNameTheMsgFrom() {
        return userNameTheMsgFrom;
    }

    public void setUserNameTheMsgFrom(String userNameTheMsgFrom) {
        this.userNameTheMsgFrom = userNameTheMsgFrom;
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
    public String getUserName() {
        return null;
    }

    @Override
    public void setUserName(String userName) {

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
        return false;
    }

    @Override
    public void setOnline(boolean online) {

    }

    @Override
    public String getImagePath() {
        return null;
    }


}
