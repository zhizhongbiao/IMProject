package com.example.alv_chi.improject.xmpp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.activity.MainActivity;
import com.example.alv_chi.improject.adapter.MessageRvAdapter;
import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.bean.ContactItem;
import com.example.alv_chi.improject.bean.RecentChatItem;
import com.example.alv_chi.improject.bean.TextMessageItem;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.data.constant.Constants;
import com.example.alv_chi.improject.eventbus.OnMessageCreatedEvent;
import com.example.alv_chi.improject.eventbus.OnUserStatusChangeEvent;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.ChattingRoomFragment;
import com.example.alv_chi.improject.greendao.DataBaseUtil;
import com.example.alv_chi.improject.greendao.MessageRecord;
import com.example.alv_chi.improject.util.ChineseToPinyinHelper;
import com.example.alv_chi.improject.util.SystemUtil;
import com.example.alv_chi.improject.util.ThreadUtil;
import com.example.alv_chi.improject.xmpp.XMPP;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class XmppListenerService extends Service implements XMPP
        , ChatManagerListener, ChatMessageListener, RosterListener, ConnectionListener {

    private static final String TAG = "XmppListenerService";
    private BaseActivity currentActivity;
    private DataManager dataManagerInstance;
    private int singleUserJIDMessageId;
    private NotificationManager notificationManager;
    private XMPPTCPConnection xmppTcpConnectionInstance;
    private Timer scheduleTimer;
    private long reloginInterval = 2;

    public synchronized void setXmppTcpConnectionInstance(XMPPTCPConnection xmppTcpConnectionInstance) {
        this.xmppTcpConnectionInstance = xmppTcpConnectionInstance;
    }

    public synchronized XMPPTCPConnection getXmppTcpConnectionInstance() {
        return xmppTcpConnectionInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: 服务活了");
        initialize();
    }


    private void initialize() {
        Constants.AppConfigConstants.ifNeedToLoginManually = false;
        dataManagerInstance = DataManager.getDataManagerInstance();
        scheduleTimer = new Timer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        return new MyBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.e(TAG, "onRebind: ");
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        this return true for calling rebind() method when next time a new client bind this service
//        ,after all previous clients have disconnected from it;
        return true;
    }

    /**
     * this method may be called many times ,so be carefully to ues it .
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        return START_STICKY;// this value for : this service will reCreate after this service is killed illegally;
    }


    private boolean getUserStatus(boolean isOnline, Presence presence) {
        if (presence != null) {
            Presence.Type type = presence.getType();

            switch (type) {
                case available:
                    isOnline = true;
                    break;
                default:
                    isOnline = false;
                    break;
            }

        }
        return isOnline;
    }

    public void initializeContactsData() {

        Set<RosterEntry> contacts = getContacts();
        DataManager.getDataManagerInstance().getContactItems().clear();
        Iterator<RosterEntry> iterator = contacts.iterator();
//        Log.e(TAG, "initializeContactsData: 执行了contacts.size()="+contacts.size() );
        while (iterator.hasNext()) {
            RosterEntry rosterEntry = iterator.next();
            if (rosterEntry == null) continue;
            String name = rosterEntry.getName();
            String userJID = rosterEntry.getUser();
            String navigationLetter = null;
            boolean isOnline = false;
            if (name != null) {
                String pingYin = ChineseToPinyinHelper.getInstance().getPinyin(name).toUpperCase();
                navigationLetter = pingYin.charAt(0) + "";
                if (!navigationLetter.matches("[A-Z]")) {
                    navigationLetter = "#";
                }
            } else {
                name = "Unknown";
                navigationLetter = "#";
            }
            Presence presence = getRoster().getPresence(userJID + "/Smack");
//            Log.e(TAG, "initializeContactsData: userJID/getCurrentChattingUserJID()="+userJID+"/"+DataManager.getDataManagerInstance().getCurrentChattingUserJID()  );
            if (DataManager.getDataManagerInstance().getCurrentChattingUserJID() != null
                    && DataManager.getDataManagerInstance().getCurrentChattingUserJID().equals(userJID)) {
                updateCurrentChattingJIDUserState(presence);
                Log.e(TAG, "initializeContactsData updateCurrentChattingJIDUserState: userJID="+userJID );

            }

            isOnline = getUserStatus(isOnline, presence);
            DataManager.getDataManagerInstance().getContactItems()
                    .add(new ContactItem(userJID, navigationLetter, name
                            , null, isOnline));//avatar temporary set null
            DataManager.getDataManagerInstance().getIsOnline().put(userJID, isOnline);
        }
        //sort the ContactItems
        Collections.sort(DataManager.getDataManagerInstance().getContactItems());
//            Log.e(TAG, "initializeContactsData: 联系人在服务初始化了DataManager.getDataManagerInstance().getContactItems().size()="+DataManager.getDataManagerInstance().getContactItems().size() );
    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        Log.e(TAG, "chatCreated: createdLocally=" + createdLocally);
        chat.addMessageListener(this);
    }

    //    send message logic
    public synchronized void sendMessage(BaseItem baseItem) throws SmackException.NotConnectedException, ConnectException {
        Chat chat;
        if (dataManagerInstance.getChats().containsKey(baseItem.getUserJID())) {
            chat = dataManagerInstance.getChats().get(baseItem.getUserJID());
        } else {
            try {
                chat = getChatManager().createChat(baseItem.getUserJID());
                dataManagerInstance.getChats().put(baseItem.getUserJID(), chat);
            } catch (NullPointerException e) {
                Log.e(TAG, "sendMessage: getXmppTcpConnectionInstance()==null 没连接服务器");
                throw new ConnectException();
            }
        }

        Message message = new Message();
//       this is the name of which send this message;
        message.setSubject(DataManager.getDataManagerInstance().getCurrentMasterUserName());////此处应该获取正在登陆的用户信息：
        message.setBody(baseItem.getMesage());
        message.setFrom(Constants.AppConfigConstants.CLIENT_EMAIL);///此处应该获取正在登陆的用户信息：
        message.setTo(baseItem.getUserJID());
        chat.sendMessage(message);

        dataManagerInstance.collectMessages(dataManagerInstance.getAllUsersMessageRecords(), baseItem);

//        show the message in the ChattingRoomFragment
        createRecentChatRecord(baseItem);
    }


    @Override            // Receive message logic
    public void processMessage(Chat chat, Message message) {
        String JIDFromUserSendMsg = message.getFrom();
        if (JIDFromUserSendMsg.contains("/")) {
            JIDFromUserSendMsg = JIDFromUserSendMsg.split("/")[0];
        }
// Using a hashmap to manage the chats , which is very conenient to reuse;
        dataManagerInstance.getChats().put(JIDFromUserSendMsg, chat);
        String receivedMsg = message.getBody();
        String userNameFrom = message.getSubject();

        if (userNameFrom == null || userNameFrom.trim().equals("")) {
            userNameFrom = JIDFromUserSendMsg.split("@")[0];
        }
        if (receivedMsg != null) {
            Log.e(TAG, "processMessage: receivedMsg=" + receivedMsg);
            TextMessageItem messageItem = saveTheMessageInfo(JIDFromUserSendMsg, receivedMsg, userNameFrom, false);
            createRecentChatRecord(messageItem);
            if (currentActivity != null && JIDFromUserSendMsg
                    .equals(DataManager.getDataManagerInstance().getCurrentChattingUserJID())) {
//                show massage
                showMessageIntheChattingRoomFragment(messageItem);
            } else {
//                notify coming message
                notifyComingMessageInStateBar(JIDFromUserSendMsg, receivedMsg, userNameFrom);
            }
        }
    }

    @NonNull
    private TextMessageItem saveTheMessageInfo(String JIDFromUserSendMsg, String receivedMsg, String userNameFrom, Boolean isOnline) {
        TextMessageItem messageItem = new TextMessageItem(userNameFrom
                , SystemUtil.getCurrentSystemTime()
                , receivedMsg, null, JIDFromUserSendMsg, MessageRvAdapter.TEXT_MESSAGE_VIEW_TYPE, true, isOnline);

        if (!dataManagerInstance.getMessageNotificationIds().containsKey(JIDFromUserSendMsg)) {
            dataManagerInstance.getMessageNotificationIds().put(JIDFromUserSendMsg, ++singleUserJIDMessageId);
        }
        dataManagerInstance.collectMessages(dataManagerInstance.getAllUsersMessageRecords(), messageItem);
        return messageItem;
    }

    private void showMessageIntheChattingRoomFragment(TextMessageItem messageItem) {
        BaseFragment currentFragment = currentActivity.getmCurrentFragment();
        if (currentFragment instanceof ChattingRoomFragment) {
            ((ChattingRoomFragment) currentFragment).refreshMessageContainer(false, messageItem);
            SystemUtil.Vibrate(XmppListenerService.this,280);
        }
    }

    private void notifyComingMessageInStateBar(String JIDFromUserSendMsg
            , String receivedMsg, String userNameFrom) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.KeyConstants.IS_THIS_INTEN_FROM_PENDING_INTENT, true);
        intent.putParcelableArrayListExtra(Constants.KeyConstants.USER_MESSAGES_RECORD
                , dataManagerInstance.getAllUsersMessageRecords().get(JIDFromUserSendMsg));
        PendingIntent pendingIntent = PendingIntent.getActivity(this
                , dataManagerInstance.getMessageNotificationIds().get(JIDFromUserSendMsg)
                , intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentText(receivedMsg)
                .setContentTitle(userNameFrom)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.app_symbol)//UserAvatar
                .setContentIntent(pendingIntent)
                .build();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(dataManagerInstance
                .getMessageNotificationIds()
                .get(JIDFromUserSendMsg), notification);
    }

    // notify the RecentChatFragment create recentChatItem;
    private void createRecentChatRecord(final BaseItem baseItem) {
        RecentChatItem recentChatItem = new RecentChatItem(baseItem.getUserName()
                , baseItem.getCurrentTimeStamp()
                , baseItem.getMesage()
                , baseItem.getUserAvatar()
                , baseItem.getUserJID()
                , baseItem.isOnline());
//        this is to RecentChatFragment
        EventBus.getDefault().postSticky(new OnMessageCreatedEvent(recentChatItem));
//        Log.e(TAG, "createRecentChatRecord: TimeStamp/Message="+baseItem.getCurrentTimeStamp()+"/"+baseItem.getMesage() );
//        store the message record in anotherThread
        ThreadUtil.executeThreadTask(new Runnable() {
            @Override
            public void run() {
//                MessageRecord 第一个参数为数据库的主键值，设置为null的目的是让数据库的主键值自动增长，而不是手动去设置麻烦。
                DataBaseUtil.getDataBaseInstance(XmppListenerService.this.getApplicationContext()).create(new MessageRecord(null, baseItem.getUserName(), dataManagerInstance.getCurrentMasterUserName(),
                        baseItem.getCurrentTimeStamp(), baseItem.getMesage(),
                        baseItem.getCurrentTimeStamp(), baseItem.getMesage(), baseItem.getUserJID(), baseItem.getTypeView()
                        , baseItem.isReceivedMessage(), baseItem.isOnline()));
//                Log.e(TAG, "createRecentChatRecord run: 插入消息记录线程完成执行");
            }
        });
    }


    public void setCurrentActivity(BaseActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    //    this  is invoked  by ChattingRoomFragment
    public void cleanCurrentMessageNotification(String currentMsgJID) {
        if (notificationManager != null && dataManagerInstance.getMessageNotificationIds().containsKey(currentMsgJID)) {
            notificationManager.cancel(dataManagerInstance.getMessageNotificationIds().get(currentMsgJID));
        }
        dataManagerInstance.getMessageNotificationIds().remove(currentMsgJID);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        release resource
        try {
            removeTheXmppListeners();
            logOut();
            cleanXMPPSetup();
        } catch (Exception e) {
            Log.e(TAG, "onDestroy: Exception=" + e.getMessage());
            e.printStackTrace();
        }
//       clear all notifications if there is any;
        clearNotification();

        dataManagerInstance.clearDatas();
        Constants.AppConfigConstants.ifNeedToLoginManually = true;
        Log.e(TAG, "onDestroy: 服务死了");
    }

    private void removeTheXmppListeners() {
        getChatManager().removeChatListener(this);
        getRoster().removeRosterListener(this);
        getXmppTcpConnectionInstance().removeConnectionListener(XmppListenerService.this);
    }

    private void clearNotification() {
        if (notificationManager != null && !dataManagerInstance.getMessageNotificationIds().isEmpty()) {
            Set<Map.Entry<String, Integer>> entries = dataManagerInstance.getMessageNotificationIds().entrySet();
            for (Map.Entry<String, Integer> entry : entries) {
                notificationManager.cancel(entry.getValue());
            }
        }
    }


//    RosterListener needs to implement those methods

    @Override
    public void entriesAdded(Collection<String> addresses) {

    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {

    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {

    }

    @Override
    public void presenceChanged(Presence presence) {

        String JIDFromUser = presence.getFrom();

        if (JIDFromUser.contains("/")) {
            JIDFromUser = JIDFromUser.split("/")[0];
        }
//        Log.e(TAG, "presenceChanged: JIDFromUser=" + JIDFromUser);
        boolean isOnline = false;
        isOnline = getUserStatus(isOnline, presence);
        DataManager.getDataManagerInstance().getIsOnline().put(JIDFromUser, isOnline);
        updateCurrentChattingJIDUserState(presence);
        Log.e(TAG, "presenceChanged updateCurrentChattingJIDUserState: JIDFromUser="+JIDFromUser);

    }

    private void updateCurrentChattingJIDUserState(Presence presence) {
//      this event is posted to  notify the ChattingActivity to change its toolbar status and update DataMangae;
        EventBus.getDefault().postSticky(new OnUserStatusChangeEvent(presence));
    }

    public class MyBinder extends Binder {
        public XmppListenerService getXmppListenerService() {
            return XmppListenerService.this;
        }
    }


    //  - - - -- - - - - - -- - - - - -- - - - Below all are XMPP interface methods - - - Split Line  - - - -- - - - -- - - - - - -- - -
    @Override
    public void connectOpenfireServer(String serverIP) throws IOException, XMPPException, SmackException {
        dataManagerInstance.setServerIP(serverIP);
        XMPPTCPConnectionConfiguration xmppConfigBuilder = XMPPTCPConnectionConfiguration.builder()
//                .setHost(Constants.AppConfigConstants.OPEN_FIRE_SERVER_IP)
                .setHost(serverIP)
                .setConnectTimeout(2500)
                .setDebuggerEnabled(true)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setPort(Constants.AppConfigConstants.OPEN_FIRE_SERVER_LOGIN_PORT)
                .setSendPresence(true)
                .setServiceName(Constants.AppConfigConstants.OPEN_FIRE_SERVER_DOMAIN_NAME)
                .build();

        setXmppTcpConnectionInstance(new XMPPTCPConnection(xmppConfigBuilder));
        getXmppTcpConnectionInstance().connect();

    }

    @Override
    public void login(String userName, String password, String serverIP) throws XMPPException, IOException, SmackException {
        if (getXmppTcpConnectionInstance() == null) {
            connectOpenfireServer(serverIP);
        }

        getXmppTcpConnectionInstance().login(userName, password);
        setStatu(getXmppTcpConnectionInstance(), Presence.Type.available, Constants.KeyConstants.IS_ONLINE);
        setTheXmppListeners();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeContactsData();
//                这里需要优化
            }
        },2000);
    }

    private void setTheXmppListeners() {
        getChatManager().addChatListener(XmppListenerService.this);
        getRoster().addRosterListener(XmppListenerService.this);
        getXmppTcpConnectionInstance().addConnectionListener(XmppListenerService.this);
    }

    @Override
    public void setStatu(XMPPTCPConnection xmppConnectionInstance, Presence.Type type, String statuString) throws SmackException.NotConnectedException {
        Presence presence = new Presence(type);
        presence.setStatus(statuString);
        xmppConnectionInstance.sendStanza(presence);
    }

    @Override
    public void logOut() throws SmackException.NotConnectedException {
        Presence unavailablePresence = new Presence(Presence.Type.unavailable);
        unavailablePresence.setStatus("I m offline");
        getXmppTcpConnectionInstance().disconnect(unavailablePresence);
    }

    @Override
    public Set<RosterEntry> getContacts() {
        return getRoster().getEntries();
    }

    @Override
    public ChatManager getChatManager() {
        return ChatManager.getInstanceFor(getXmppTcpConnectionInstance());
    }

    @Override
    public Roster getRoster() {
        return Roster.getInstanceFor(getXmppTcpConnectionInstance());
    }


    @Override
    public boolean userRegister(String loginName, String password, String email, String studentId, String serverIP)
            throws SmackException
            , XMPPException, IOException {
        if (getXmppTcpConnectionInstance() == null) {
            connectOpenfireServer(serverIP);
        }
        AccountManager accountManager = getAccountManager();
        if (accountManager == null) {
            Log.e(TAG, "userRegister: accountManager == null");
            return false;
        }

        boolean isSupportCreateNewAccount = accountManager.supportsAccountCreation();
        if (!isSupportCreateNewAccount) {
            Log.e(TAG, "userRegister: isSupportCreateNewAccount =" + isSupportCreateNewAccount);
            return false;
        }

        HashMap<String, String> accountAttributes = getaccountAttibutes(email, studentId);

        accountManager.createAccount(loginName, password, accountAttributes);


        return true;
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
     * @param email
     * @param studentId
     */
    @Override
    public HashMap<String, String> getaccountAttibutes(String email, String studentId) {
        if (email == null || studentId == null) {
            Log.e(TAG, "getaccountAttibutes: email/studentId=" + email + "/" + studentId);
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

    @Override
    public AccountManager getAccountManager() {
        return AccountManager.getInstance(getXmppTcpConnectionInstance());
    }


    @Override
    public void cleanXMPPSetup() {
        setXmppTcpConnectionInstance(null);
//        kill all the TimerTask
        scheduleTimer.cancel();
        scheduleTimer.purge();
    }


//  - - - -- - - - - - -- - - - - -- - - - Below all are ConnectionListener interface methods - - - Split Line  - - - -- - - - -- - - - - - -- - -

    @Override
    public void connected(XMPPConnection connection) {
        Log.e(TAG, "connected: ");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.e(TAG, "authenticated: ");//这个登陆就执行
    }

    @Override
    public void connectionClosed() {
        Log.e(TAG, "connectionClosed: ");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        String exceptionMsg = e.getMessage();
        Log.e(TAG, "connectionClosedOnError: Exception=" + exceptionMsg);

//        judge if the currentAccount is logined other place.
        boolean isAccountLoginedOther = exceptionMsg.startsWith(Constants.AppConfigConstants
                .CURRENT_ACCOUNT_IS_LOGINED_IN_OTHER_DEVICE_EXCEPTION_MESSAGE);
        if (!isAccountLoginedOther) {
//            relogin current account
            scheduleTimer.schedule(new ReloginTimerTask(), (reloginInterval * reloginInterval));

        } else {
            Intent intent = new Intent(Constants.KeyConstants
                    .CURRENT_ACCOUNT_IS_LOGINED_BY_OTHERS_EXCEPTION);
            sendBroadcast(intent);
            Log.e(TAG, "connectionClosedOnError: 发广播了" );
        }
    }

    class ReloginTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
//                clean the connection
                setXmppTcpConnectionInstance(null);
                MessageRecord loginInfo = DataBaseUtil.getDataBaseInstance(getApplicationContext()).retrive();
                login(loginInfo.getUserName(), loginInfo.getMasterUserName(), loginInfo.getLatestMessageTimeStamp());
//                clean all the dead Chat ,when it relogins to build a new connection.
                DataManager.getDataManagerInstance().getChats().clear();
                Log.e(TAG, "ReloginTimerTask run: 服务在监听切换网络时登陆成功了");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "ReloginTimerTask run: 服务在监听切换网络时登陆出错了 Exception=" + e.getMessage());
                scheduleTimer.schedule(new ReloginTimerTask(), (reloginInterval * reloginInterval));
            }
        }
    }


    @Override
    public void reconnectionSuccessful() {
        Log.e(TAG, "reconnectionSuccessful: ");
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.e(TAG, "reconnectingIn: ");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.e(TAG, "reconnectionFailed: ");
    }


//      - - - - - -  - - -- - - - - -- - - end  - - - --  - - -- - - - - -  -- - - -

}
