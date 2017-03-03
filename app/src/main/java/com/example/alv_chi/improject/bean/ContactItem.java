package com.example.alv_chi.improject.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Alv_chi on 2017/2/22.
 */

public class ContactItem implements Comparable<ContactItem> ,Parcelable{

    private static final String TAG="ContactItem";
    private  String userJID;
    private String navigationLetter;
    private String userName;
    private Bitmap avatar;

    private TextMessageItem textMessageItem;

    public ContactItem(String userJID, String navigationLetter, String userName, Bitmap avatar, TextMessageItem textMessageItem) {
        this.userJID = userJID;
        this.navigationLetter = navigationLetter;
        this.userName = userName;
        this.avatar = avatar;
        this.textMessageItem = textMessageItem;
    }

    protected ContactItem(Parcel in) {
        userJID = in.readString();
        navigationLetter = in.readString();
        userName = in.readString();
        avatar = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<ContactItem> CREATOR = new Creator<ContactItem>() {
        @Override
        public ContactItem createFromParcel(Parcel in) {
            return new ContactItem(in);
        }

        @Override
        public ContactItem[] newArray(int size) {
            return new ContactItem[size];
        }
    };

    public String getUserJID() {
        return userJID;
    }

    public void setUserJID(String userJID) {
        this.userJID = userJID;
    }

    public String getNavigationLetter() {
        return navigationLetter;
    }

    public void setNavigationLetter(String navigationLetter) {
        this.navigationLetter = navigationLetter;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public TextMessageItem getTextMessageItem() {
        return textMessageItem;
    }

    public void setTextMessageItem(TextMessageItem textMessageItem) {
        this.textMessageItem = textMessageItem;
    }

    @Override
    public int compareTo(ContactItem item) {

        if(getNavigationLetter().equals("#")){

            return 1;
        }
        if (item.getNavigationLetter().matches("[A-Z]"))
        {
            return getNavigationLetter().compareTo(item.getNavigationLetter());
        }

        return -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userJID);
        dest.writeString(navigationLetter);
        dest.writeString(userName);
        dest.writeParcelable(avatar, flags);
    }


    class TextMessageItem {

        private String userName;
        private String currentTimeStamp;
        private String mesage;
        private Bitmap userAvatar;
        private int typeView;
        private boolean isReceivedMessage;

        public TextMessageItem(String userName, String currentTimeStamp, String mesage, Bitmap userAvatar, int typeView, boolean isReceivedMessage) {
            this.userName = userName;
            this.currentTimeStamp = currentTimeStamp;
            this.mesage = mesage;
            this.userAvatar = userAvatar;
            this.typeView = typeView;
            this.isReceivedMessage = isReceivedMessage;
        }


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
    }
}
