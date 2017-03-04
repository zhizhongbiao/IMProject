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

    public TextMessageItem(String userName, String currentTimeStamp, String mesage, Bitmap userAvatar, String userJID, int typeView, boolean isReceivedMessage) {
        this.userName = userName;
        this.currentTimeStamp = currentTimeStamp;
        this.mesage = mesage;
        this.userAvatar = userAvatar;
        this.userJID = userJID;
        this.typeView = typeView;
        this.isReceivedMessage = isReceivedMessage;
    }

    protected TextMessageItem(Parcel in) {
        userName = in.readString();
        currentTimeStamp = in.readString();
        mesage = in.readString();
        userAvatar = in.readParcelable(Bitmap.class.getClassLoader());
        userJID = in.readString();
        typeView = in.readInt();
        isReceivedMessage = in.readByte() != 0;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCurrentTimeStamp() {
        return currentTimeStamp;
    }

    public void setCurrentTimeStamp(String currentTimeStamp) {
        this.currentTimeStamp = currentTimeStamp;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public Bitmap getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(Bitmap userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserJID() {
        return userJID;
    }

    public void setUserJID(String userJID) {
        this.userJID = userJID;
    }

    public int getTypeView() {
        return typeView;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }

    public boolean isReceivedMessage() {
        return isReceivedMessage;
    }

    public void setReceivedMessage(boolean receivedMessage) {
        isReceivedMessage = receivedMessage;
    }

    @Override
    public int describeContents() {
        return 0;
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
    }


}
