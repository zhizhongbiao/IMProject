package com.example.alv_chi.improject.handler;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.alv_chi.improject.constant.Constants;

import java.lang.ref.SoftReference;
import java.util.HashMap;


/**
 * Created by Alv_chi on 2017/2/17.
 */

public class ActivityHandler extends Handler {
    private static final String TAG = "ActivityHandler";
    private SoftReference<AppCompatActivity> appCompatActivitySoftReference;
    private HashMap<String, OnThreadTaskFinishedListener> listeners = new HashMap();


    public void removeListener(String key) {
        listeners.remove(key);
    }

    public void addListeners(String key, OnThreadTaskFinishedListener value) {
        listeners.put(key, value);
    }


    public ActivityHandler(AppCompatActivity activity) {
        appCompatActivitySoftReference = new SoftReference<>(activity);

    }

    @Override
    public void handleMessage(Message msg) {

        if (appCompatActivitySoftReference.get() == null)
        {
            Log.e(TAG, "handleMessage: the problem is caused by SoftReference " );
            return;
        }

        String listenerKey = (String) msg.obj;
        int messageType = msg.what;
        OnThreadTaskFinishedListener onThreadTaskFinishedListener = listeners.get(listenerKey);
        if (onThreadTaskFinishedListener == null) {
            Log.e(TAG, "handleMessage: listenerKey/messageType=" + listenerKey + "/" + messageType);
            return;
        }
        switch (messageType) {
            case Constants.HandlerMessageType.LOGIN_SUCCESS:
                onThreadTaskFinishedListener.loginSuccess();
                break;
        }

    }
}
