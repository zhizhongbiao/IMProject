package com.example.alv_chi.improject.xmpp;

import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.exception.LoginNameOrPasswordException;

/**
 * Created by Alv_chi on 2017/2/16.
 */

public interface XMPP {
    void login(String userName,String password) throws LoginNameOrPasswordException, ConnectException;
    void logOut(String userName,String password);
    void sendMessage(String Message);

}
