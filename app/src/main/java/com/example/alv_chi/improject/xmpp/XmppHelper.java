package com.example.alv_chi.improject.xmpp;

import android.util.Log;

import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.exception.LoginException;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;


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
                .setConnectTimeout(15000)
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
            throw new ConnectException();
        }

    }


    @Override
    public void login(String userName, String password) throws LoginException, ConnectException {
        XMPPTCPConnection xmppConnectionInstance = getXMPPConnectionInstance();
        try {
            xmppConnectionInstance.login(userName, password);
            Log.e(TAG, "login: login success!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "login: Exception=" + e.getMessage());
            throw new LoginException();
        }
    }

    @Override
    public void logOut(String userName, String password) {

    }

    @Override
    public void sendMessage(String Message) {

    }



}
