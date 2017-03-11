package com.example.alv_chi.improject.eventbus;

/**
 * Created by Alv_chi on 2017/3/4.
 */

public class OnDatasArrivedChattingFragmentEvent {

    private String userJIDOfDatas;

    public OnDatasArrivedChattingFragmentEvent(String userJIDOfDatas) {
        this.userJIDOfDatas = userJIDOfDatas;
    }

    public String getUserJIDOfDatas() {
        return userJIDOfDatas;
    }

    public void setUserJIDOfDatas(String userJIDOfDatas) {
        this.userJIDOfDatas = userJIDOfDatas;
    }
}
