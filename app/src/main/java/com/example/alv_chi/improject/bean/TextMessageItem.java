package com.example.alv_chi.improject.bean;

import android.graphics.Bitmap;

/**
 * Created by Alv_chi on 2017/2/22.
 */

public class TextMessageItem {

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
