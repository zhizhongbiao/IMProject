package com.example.alv_chi.improject.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Alv_chi on 2017/2/22.
 */

public class ContactItemCOPY implements Comparable<ContactItemCOPY> ,Parcelable{

    private static final String TAG="ContactItem";
    private  String userJID;
    private String navigationLetter;
    private String userName;
    private Bitmap avatar;



    public ContactItemCOPY(String userJID, String navigationLetter, String userName, Bitmap avatar) {
        this.userJID = userJID;
        this.navigationLetter = navigationLetter;
        this.userName = userName;
        this.avatar = avatar;
    }

    protected ContactItemCOPY(Parcel in) {
        userJID = in.readString();
        navigationLetter = in.readString();
        userName = in.readString();
        avatar = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<ContactItemCOPY> CREATOR = new Creator<ContactItemCOPY>() {
        @Override
        public ContactItemCOPY createFromParcel(Parcel in) {
            return new ContactItemCOPY(in);
        }

        @Override
        public ContactItemCOPY[] newArray(int size) {
            return new ContactItemCOPY[size];
        }
    };

    public static String getTAG() {
        return TAG;
    }

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




    @Override
    public int compareTo(ContactItemCOPY item) {

        if(getNavigationLetter().equals("#")){

            return 1;
        }
        if (item.getNavigationLetter().matches("[A-Z]"))
        {
            return getNavigationLetter().compareTo(item.getNavigationLetter());
        }

        return -1;
    }
}
