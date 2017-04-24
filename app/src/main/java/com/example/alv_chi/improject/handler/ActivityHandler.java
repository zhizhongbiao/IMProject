package com.example.alv_chi.improject.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.alv_chi.improject.constant.Constants;

import java.util.HashMap;


/**
 * Created by Alv_chi on 2017/2/17.
 */

public class ActivityHandler extends Handler {

    private static final String TAG = "ActivityHandler";
    private HashMap<String, OnThreadTaskFinishedListener> listeners = new HashMap();


    public void removeListener(String key) {
        listeners.remove(key);
    }

    public void addListeners(String key, OnThreadTaskFinishedListener listener) {
        listeners.put(key, listener);
    }


    @Override
    public void handleMessage(Message msg) {

        String listenerKey = (String) msg.obj;
        int messageType = msg.what;
        OnThreadTaskFinishedListener onThreadTaskFinishedListener = listeners.get(listenerKey);
        if (onThreadTaskFinishedListener == null) {
            Log.e(TAG, "handleMessage: onThreadTaskFinishedListener/listenerKey/messageType=" +onThreadTaskFinishedListener+"/"+ listenerKey + "/" + messageType);
            return;
        }
        switch (messageType) {
            case Constants.HandlerMessageType.SUCCESS:
                onThreadTaskFinishedListener.onThreadTaskFinished(messageType);
                break;
            case Constants.HandlerMessageType.FAILURE:
                onThreadTaskFinishedListener.onThreadTaskFinished(messageType);
                break;
        }

    }
}
