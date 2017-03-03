package com.example.alv_chi.improject.xmpp;

import com.example.alv_chi.improject.bean.ContactItem;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.exception.LoginNameOrPasswordException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.Set;

/**
 * Created by Alv_chi on 2017/2/16.
 */

public interface XMPP {
    void login(String userName, String password) throws LoginNameOrPasswordException, ConnectException;
    void setStatu(XMPPTCPConnection xmppConnectionInstance, Presence.Type type, String statuString) throws SmackException.NotConnectedException;
    void logOut(String userName, String password) throws ConnectException, SmackException.NotConnectedException;
    void sendMessage(ContactItem contactItem, String msg, ChatManagerListener listener) throws ConnectException, XmppStringprepException, SmackException.NotConnectedException, InterruptedException;

    Set<RosterEntry> getContacts() throws ConnectException;

}
