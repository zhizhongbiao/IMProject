package com.example.alv_chi.improject.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Alv_chi on 2017/3/7.
 */

public class RecentChatItem_copy implements BaseItem, Parcelable, Comparable<RecentChatItem_copy> {

    private static final String TAG = "RecentChatItem";

    private String userName;
    private String lastMessageTimeStamp;
    private String lastMessage;

    private String currentTimeStamp;
    private String mesage;
    private Bitmap userAvatar;
    private String userJID;
    private int typeView;
    private boolean isReceivedMessage;

    public RecentChatItem_copy(String userName, String lastMessageTimeStamp, String lastMessage, Bitmap userAvatar, String userJID) {
        this.userName = userName;
        this.lastMessageTimeStamp = lastMessageTimeStamp;
        this.lastMessage = lastMessage;
        this.userAvatar = userAvatar;
        this.userJID = userJID;
        this.currentTimeStamp = System.currentTimeMillis() + "";
    }


    //    Parcelable needs to implement those methods


    protected RecentChatItem_copy(Parcel in) {
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

    public static final Creator<RecentChatItem_copy> CREATOR = new Creator<RecentChatItem_copy>() {
        @Override
        public RecentChatItem_copy createFromParcel(Parcel in) {
            return new RecentChatItem_copy(in);
        }

        @Override
        public RecentChatItem_copy[] newArray(int size) {
            return new RecentChatItem_copy[size];
        }
    };

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


//    BaseItem needs to implement those methods

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


//    Others needs to implement those methods

    @Override
    public boolean equals(Object obj) {
        Log.e(TAG, "equals: getUserJID()/((RecentChatItem) obj).getUserJID()=" + getUserJID() + "/" + ((RecentChatItem_copy) obj).getUserJID() + "-----" + getUserJID().equals(((RecentChatItem_copy) obj).getUserJID()));
        return getUserJID().equals(((RecentChatItem_copy) obj).getUserJID());
    }

    @Override
    public int hashCode() {
        return 0;
    }
    //    Comparable needs to implement those methods
    @Override
    public int compareTo(RecentChatItem_copy item) {
        return Integer.parseInt(item.currentTimeStamp) - Integer.parseInt(getCurrentTimeStamp());
    }
}
