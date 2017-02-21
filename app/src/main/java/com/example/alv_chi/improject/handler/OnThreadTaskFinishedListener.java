package com.example.alv_chi.improject.handler;

/**
 * Created by Alv_chi on 2017/2/17.
 */

public interface OnThreadTaskFinishedListener {
    void loginSuccess();
    void addThisOnThreadTaskFinishedListenerToActivityHandler();
    void removeThisOnThreadTaskFinishedListenerFromActivityHandler();
}
