package com.example.alv_chi.improject.xmpp;

import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.exception.LoginNameOrPasswordException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.Set;

/**
 * Created by Alv_chi on 2017/2/16.
 */

public interface XMPP {
    void login(String userName, String password) throws LoginNameOrPasswordException, ConnectException;
    void logOut(String userName,String password) throws ConnectException, SmackException.NotConnectedException;
    void sendMessage(String JID,String msg,ChatManagerListener listener) throws ConnectException, SmackException.NotConnectedException, XmppStringprepException, InterruptedException;
    Set<RosterEntry> getContacts() throws ConnectException;

}
