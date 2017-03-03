package com.example.alv_chi.improject.xmpp;

import android.util.Log;

import com.example.alv_chi.improject.bean.ContactItem;
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.exception.LoginNameOrPasswordException;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * Created by Alv_chi on 2017/2/16.
 */

public class XmppHelper implements XMPP {

    private static final String TAG = "XmppHelper";

    private static XmppHelper xmppHelperInstance;
    private XMPPTCPConnection xmppTcpConnectionInstance;
    private XMPPTCPConnectionConfiguration xmppConfigBuilder;
    private Chat chat;


    public static XmppHelper getXmppHelperInStance() {
        if (xmppHelperInstance == null) {
            xmppHelperInstance = new XmppHelper();
        }

        return xmppHelperInstance;
    }

    private XmppHelper() {
        initial();
    }

    //          initialize XMPP
    private void initial() {

        xmppConfigBuilder = XMPPTCPConnectionConfiguration.builder()
                .setHost(Constants.AppConfigConstants.OPEN_FIRE_SERVER_IP)
                .setConnectTimeout(2000)
                .setDebuggerEnabled(true)
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
            xmppTcpConnectionInstance = null;
            throw new ConnectException();
        }

    }


    @Override
    public synchronized void login(String userName, String password) throws LoginNameOrPasswordException, ConnectException {

        XMPPTCPConnection xmppConnectionInstance = getXMPPConnectionInstance();

        try {
            xmppConnectionInstance.login(userName, password);
            Log.e(TAG, "login: login success!");
            setStatu(xmppConnectionInstance,Presence.Type.available,"I m Online");
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
    public void setStatu(XMPPTCPConnection xmppConnectionInstance, Presence.Type type, String statuString) throws SmackException.NotConnectedException {
        Presence presence = new Presence(type);
        presence.setStatus(statuString);
        xmppConnectionInstance.sendStanza(presence);
    }

    @Override
    public void logOut(String userName, String password) throws ConnectException, SmackException.NotConnectedException {
        getXMPPConnectionInstance().disconnect(new Presence(Presence.Type.unavailable));
    }

    @Override
    public void sendMessage(ContactItem contactItem, String msg, ChatManagerListener listener) throws ConnectException, XmppStringprepException, SmackException.NotConnectedException, InterruptedException {
        ChatManager chatManager = ChatManager.getInstanceFor(getXMPPConnectionInstance());
        if (chatManager.getChatListeners().size()==0)
        {
            chatManager.addChatListener(listener);
            chat = chatManager.createChat(contactItem.getUserJID());
        }


        Message message = new Message();
        message.setSubject(contactItem.getUserName());
        message.setBody(msg);
        message.setFrom(Constants.AppConfigConstants.CLIENT_EMAIL);
        message.setTo(contactItem.getUserJID());

        chat.sendMessage(message);
    }


    @Override
    public Set<RosterEntry> getContacts() throws ConnectException {


        return getRoster().getEntries();
    }


    protected Roster getRoster() throws ConnectException {

        return Roster.getInstanceFor(getXMPPConnectionInstance());
    }


    public void getOffLineMessage() throws ConnectException, SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        OfflineMessageManager offlineMessageManager = new OfflineMessageManager(getXMPPConnectionInstance());
        List<Message> offlineMessages = offlineMessageManager.getMessages();
        for (Message offlineMessage : offlineMessages) {
//            offlineMessage.get
//            offlineMessage.getBody()
        }
    }


}
