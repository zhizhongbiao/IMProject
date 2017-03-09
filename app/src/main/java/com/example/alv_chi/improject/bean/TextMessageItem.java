package com.example.alv_chi.improject.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alv_chi on 2017/2/22.
 */

public class TextMessageItem implements Parcelable,BaseItem{

    private String userName;
    private String currentTimeStamp;
    private String mesage;
    private Bitmap userAvatar;
    private String userJID;
    private int typeView;
    private boolean isReceivedMessage;
    private boolean isOnline;

    public TextMessageItem(String userName, String currentTimeStamp, String mesage, Bitmap userAvatar, String userJID, int typeView, boolean isReceivedMessage, boolean isOnline) {
        this.userName = userName;
        this.currentTimeStamp = currentTimeStamp;
        this.mesage = mesage;
        this.userAvatar = userAvatar;
        this.userJID = userJID;
        this.typeView = typeView;
        this.isReceivedMessage = isReceivedMessage;
        this.isOnline = isOnline;
    }

    protected TextMessageItem(Parcel in) {
        userName = in.readString();
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

    public static final Creator<TextMessageItem> CREATOR = new Creator<TextMessageItem>() {
        @Override
        public TextMessageItem createFromParcel(Parcel in) {
            return new TextMessageItem(in);
        }

        @Override
        public TextMessageItem[] newArray(int size) {
            return new TextMessageItem[size];
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
}
