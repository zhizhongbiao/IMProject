package com.example.alv_chi.improject.xmpp;

import android.util.Log;

import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.exception.LoginNameOrPasswordException;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;


/**
 * Created by Alv_chi on 2017/2/16.
 */

public class XmppHelper implements XMPP {

    private static final String TAG = "XmppHelper";

    private static XmppHelper xmppHelperInstance;
    private XMPPTCPConnection xmppTcpConnectionInstance;
    private XMPPTCPConnectionConfiguration xmppConfigBuilder;

    private XmppHelper() {
        initial();
    }


    public static XmppHelper getXmppHelperInStance() {
        if (xmppHelperInstance == null) {
            xmppHelperInstance = new XmppHelper();
        }

        return xmppHelperInstance;
    }

    //          initialize XMPP
    private void initial() {
        xmppConfigBuilder = XMPPTCPConnectionConfiguration.builder()
                .setConnectTimeout(2000)
                .setDebuggerEnabled(true)
                .setHost(Constants.AppConfigConstants.OPEN_FIRE_SERVER_IP)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setPort(Constants.AppConfigConstants.OPEN_FIRE_SERVER_LOGIN_PORT)
                .setSendPresence(true)
                .setServiceName(Constants.AppConfigConstants.OPEN_FIRE_SERVER_DOMAIN_NAME)
                .build();

    }


    private XMPPTCPConnection getXMPPConnectionInstance() throws ConnectException {
        if (xmppTcpConnectionInstance == null) {
            connectServer();
        }
        return xmppTcpConnectionInstance;
    }

    private void connectServer() throws ConnectException {
        if (xmppConfigBuilder == null) {
            initial();
        }
        try {
            xmppTcpConnectionInstance = new XMPPTCPConnection(xmppConfigBuilder);
            xmppTcpConnectionInstance.connect();
            Log.e(TAG, "connectServer: connectServer success!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "connectServer: Exception=" + e.getMessage());
            xmppTcpConnectionInstance=null;
            throw new ConnectException();
        }

    }


    @Override
    public synchronized void login(String userName, String password) throws LoginNameOrPasswordException, ConnectException {

        XMPPTCPConnection xmppConnectionInstance = getXMPPConnectionInstance();

        try {
            xmppConnectionInstance.login(userName, password);
            Log.e(TAG, "login: login success!");
        } catch (XMPPException e) {
            e.printStackTrace();
            Log.e(TAG, "login: Exception=" + e.getMessage());
            throw new LoginNameOrPasswordException();//此处有可能是登陆名或密码错误；
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void logOut(String userName, String password) {

    }

    @Override
    public void sendMessage(String Message) {

    }


}
