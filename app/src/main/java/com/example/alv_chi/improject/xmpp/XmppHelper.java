package com.example.alv_chi.improject.xmpp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.exception.LoginNameOrPasswordException;
import com.example.alv_chi.improject.util.SystemUtil;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;


/**
 * Created by Alv_chi on 2017/2/16.
 */

public class XmppHelper implements XMPP {

    private static final String TAG = "XmppHelper";

    private static XmppHelper xmppHelperInstance;
    private XMPPTCPConnection xmppTcpConnectionInstance;
    private XMPPTCPConnectionConfiguration xmppConfigBuilder;



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
            setStatu(xmppConnectionInstance, Presence.Type.available, Constants.KeyConstants.IS_ONLINE);
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
    public void setStatu(XMPPTCPConnection xmppConnectionInstance, Presence.Type type, String statuString) throws SmackException.NotConnectedException {
        Presence presence = new Presence(type);
        presence.setStatus(statuString);
        xmppConnectionInstance.sendStanza(presence);
    }

    @Override
    public void logOut() throws ConnectException, SmackException.NotConnectedException {
        Presence unavailablePresence = new Presence(Presence.Type.unavailable);
        unavailablePresence.setStatus("I m offline");
        getXMPPConnectionInstance().disconnect(unavailablePresence);
    }


    @Override
    public Set<RosterEntry> getContacts() throws ConnectException {
        return getRoster().getEntries();
    }

    @Override
    public ChatManager getChatManager() throws ConnectException {
        return ChatManager.getInstanceFor(getXMPPConnectionInstance());
    }


    public Roster getRoster() throws ConnectException {
        return Roster.getInstanceFor(getXMPPConnectionInstance());
    }


//    public void getOffLineMessage() throws ConnectException, SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
//        OfflineMessageManager offlineMessageManager = new OfflineMessageManager(getXMPPConnectionInstance());
//        List<Message> offlineMessages = offlineMessageManager.getMessages();
//        for (Message offlineMessage : offlineMessages) {
//            offlineMessage.get
//            offlineMessage.getBody()
//        }
//    }


    //   you must clear your XmppSetup when you logout ,or you can not send and receive message when login again ;
    public void clearXmppSetup() {
        xmppTcpConnectionInstance = null;
        xmppConfigBuilder = null;
    }


    public VCardManager getVCardManage() throws ConnectException {
        return VCardManager.getInstanceFor(getXMPPConnectionInstance());
    }


    public VCard getUserVCard(String userJID) throws ConnectException, SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        return getVCardManage().loadVCard(userJID);
    }

    public AccountManager getAccountManager() throws ConnectException {
        return AccountManager.getInstance(getXMPPConnectionInstance());
    }

    /**
     * create a Map attributes first when create a new user acount and the standard is below:
     * <li>name -- the user's name.
     * <li>first -- the user's first name.
     * <li>last -- the user's last name.
     * <li>email -- the user's email address.
     * <li>city -- the user's city.
     * <li>state -- the user's state.
     * <li>zip -- the user's ZIP code.
     * <li>phone -- the user's phone number.
     * <li>url -- the user's website.
     * <li>date -- the date the registration took place.
     * <li>misc -- other miscellaneous information to associate with the account.
     * <li>text -- textual information to associate with the account.
     * <li>remove -- empty flag to remove account.
     *
     * @param loginName
     * @param password
     */

    public boolean userRegister(String loginName, String password, String email, String studentId) throws ConnectException, SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        AccountManager accountManager = getAccountManager();

        if (accountManager == null) {
            Log.e(TAG, "userRegister: accountManager == null" );
            return false;
        }

        boolean isSupportCreateNewAccount = accountManager.supportsAccountCreation();
        if (!isSupportCreateNewAccount) {
            Log.e(TAG, "userRegister: isSupportCreateNewAccount ="+isSupportCreateNewAccount );
            return false;
        }

        HashMap<String, String> accountAttributes = getaccountAttibutes(email, studentId);

        accountManager.createAccount(loginName, password, accountAttributes);


        return true;
    }


    public boolean changeAccountPassword(String newPassword) throws ConnectException {
        AccountManager accountManager = getAccountManager();
        if (accountManager == null) {
            Log.e(TAG, "changeAccountPassword: accountManager == null" );
            return false;
        }
        try {
            accountManager.changePassword(newPassword);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            Log.e(TAG, "updateAccountPassword: server may not support ,XMPPErrorException="+e.getMessage() );
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean deleteAccount() throws ConnectException {
        AccountManager accountManager = getAccountManager();
        if (accountManager == null) {
            Log.e(TAG, "changeAccountPassword: accountManager == null" );
            return false;
        }
        try {
            accountManager.deleteAccount();
            return true;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            Log.e(TAG, "updateAccountPassword: server may not support ,XMPPErrorException="+e.getMessage() );
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @NonNull
    private HashMap<String, String> getaccountAttibutes(String email, String studentId) {
        if (email==null||studentId==null)
        {
            Log.e(TAG, "getaccountAttibutes: email/studentId="+email+"/"+studentId );
            return null;
        }
        HashMap<String, String> accountAttributes = new HashMap<>();
        accountAttributes.put("name", "");
        accountAttributes.put("first", "temporaryNotSupport");
        accountAttributes.put("last", "temporaryNotSupport");
        accountAttributes.put("city", "temporaryNotSupport");
        accountAttributes.put("state", "temporaryNotSupport");
        accountAttributes.put("zip", "temporaryNotSupport");
        accountAttributes.put("phone", "");
        accountAttributes.put("url", "temporaryNotSupport");
        accountAttributes.put("remove", "temporaryNotSupport");
        accountAttributes.put("misc", "");
        accountAttributes.put(Constants.KeyConstants.STUDENT_REGISTER_DATE, studentId);
        accountAttributes.put(Constants.KeyConstants.STUDENT_REGISTER_DATE, SystemUtil.getCurrentSystemTime());
        accountAttributes.put(Constants.KeyConstants.STUDENT_EMAIL, email);
        return accountAttributes;
    }

}
