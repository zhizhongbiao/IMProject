package com.example.alv_chi.improject.xmpp;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Alv_chi on 2017/2/16.
 */

public interface XMPP {

    void connectOpenfireServer(String serverIP) throws IOException, XMPPException, SmackException;

    void login(String userName, String password, String serverIP) throws XMPPException, IOException, SmackException;

    void setStatu(XMPPTCPConnection xmppConnectionInstance, Presence.Type type, String statuString) throws SmackException.NotConnectedException;

    void logOut() throws SmackException.NotConnectedException;

    Set<RosterEntry> getContacts();

    ChatManager getChatManager();

    void cleanXMPPSetup();

    Roster getRoster();

    boolean userRegister(String loginName, String password, String email, String studentId,String serverIP) throws SmackException, XMPPException, IOException;

    HashMap<String, String> getaccountAttibutes(String email, String studentId);

    AccountManager getAccountManager();
}
