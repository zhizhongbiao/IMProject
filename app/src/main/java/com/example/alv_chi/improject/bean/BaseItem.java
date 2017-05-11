package com.example.alv_chi.improject.bean;

import android.graphics.Bitmap;
import android.os.Parcelable;

/**
 * Created by Alv_chi on 2017/3/3.
 */

public interface BaseItem extends Parcelable{



    public String getUserName();

    public void setUserName(String userName);

    public String getCurrentTimeStamp();

    public void setCurrentTimeStamp(String currentTimeStamp);

    public String getMesage();

    public void setMesage(String mesage);

    public Bitmap getUserAvatar();

    public void setUserAvatar(Bitmap userAvatar);

    public String getUserJID();

    public void setUserJID(String userJID);

    public int getTypeView();

    public void setTypeView(int typeView);

    public boolean isReceivedMessage();

    public void setReceivedMessage(boolean receivedMessage);

    public boolean isOnline();
    public void setOnline(boolean online);

    String getImagePath();
}
