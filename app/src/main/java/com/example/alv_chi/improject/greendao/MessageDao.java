package com.example.alv_chi.improject.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Alv_chi on 2017/4/24.
 */
@Entity
public class MessageDao {
    @Id(autoincrement = true)
    private Long id;
    private String userName;
    private String latestMessageTimeStamp;
    private String latestMessage;

    private String currentTimeStamp;
    private String mesage;
//    private Bitmap userAvatar;
    private String userJID;
    private int typeView;
    private boolean isReceivedMessage;
    private boolean isOnline;
    @Generated(hash = 853197296)
    public MessageDao(Long id, String userName, String latestMessageTimeStamp,
            String latestMessage, String currentTimeStamp, String mesage,
            String userJID, int typeView, boolean isReceivedMessage,
            boolean isOnline) {
        this.id = id;
        this.userName = userName;
        this.latestMessageTimeStamp = latestMessageTimeStamp;
        this.latestMessage = latestMessage;
        this.currentTimeStamp = currentTimeStamp;
        this.mesage = mesage;
        this.userJID = userJID;
        this.typeView = typeView;
        this.isReceivedMessage = isReceivedMessage;
        this.isOnline = isOnline;
    }
    @Generated(hash = 1332182039)
    public MessageDao() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getLatestMessageTimeStamp() {
        return this.latestMessageTimeStamp;
    }
    public void setLatestMessageTimeStamp(String latestMessageTimeStamp) {
        this.latestMessageTimeStamp = latestMessageTimeStamp;
    }
    public String getLatestMessage() {
        return this.latestMessage;
    }
    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }
    public String getCurrentTimeStamp() {
        return this.currentTimeStamp;
    }
    public void setCurrentTimeStamp(String currentTimeStamp) {
        this.currentTimeStamp = currentTimeStamp;
    }
    public String getMesage() {
        return this.mesage;
    }
    public void setMesage(String mesage) {
        this.mesage = mesage;
    }
    public String getUserJID() {
        return this.userJID;
    }
    public void setUserJID(String userJID) {
        this.userJID = userJID;
    }
    public int getTypeView() {
        return this.typeView;
    }
    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }
    public boolean getIsReceivedMessage() {
        return this.isReceivedMessage;
    }
    public void setIsReceivedMessage(boolean isReceivedMessage) {
        this.isReceivedMessage = isReceivedMessage;
    }
    public boolean getIsOnline() {
        return this.isOnline;
    }
    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
}
