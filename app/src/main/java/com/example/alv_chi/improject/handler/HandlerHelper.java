package com.example.alv_chi.improject.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Alv_chi on 2017/2/17.
 */

public class HandlerHelper {


    public static void sendMessageByHandler(Handler handler, Bundle moreDataPutInBundle, String listenerKeyByObj, int messageTypeByParamWhat, int Arg1, int Agr2) {
        Message message = handler.obtainMessage();
        message.what = messageTypeByParamWhat;
        message.arg1 = Arg1;
        message.arg2 = Agr2;
        message.obj = listenerKeyByObj;
        if (moreDataPutInBundle != null) {
            message.setData(moreDataPutInBundle);
        }

        handler.sendMessage(message);
    }

    public static void sendMessageByHandler(Handler handler, Bundle moreDataPutInBundle, String listenerKeyByObj, int messageTypeByParamWhat) {
        sendMessageByHandler(handler, moreDataPutInBundle, listenerKeyByObj, messageTypeByParamWhat, 0, 0);
    }

    public static void sendMessageByHandler(Handler handler, String listenerKeyByObj, int messageTypeByParamWhat) {
        sendMessageByHandler(handler, null, listenerKeyByObj, messageTypeByParamWhat, 0, 0);
    }
}
