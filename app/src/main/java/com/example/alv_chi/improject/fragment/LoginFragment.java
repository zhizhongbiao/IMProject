package com.example.alv_chi.improject.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.activity.LogInAndSignUpActivity;
import com.example.alv_chi.improject.activity.MainActivity;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.data.constant.Constants;
import com.example.alv_chi.improject.greendao.DataBaseUtil;
import com.example.alv_chi.improject.greendao.MessageRecord;
import com.example.alv_chi.improject.handler.HandlerHelper;
import com.example.alv_chi.improject.handler.OnThreadTaskFinishedListener;
import com.example.alv_chi.improject.util.ThreadUtil;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class LoginFragment extends BaseFragment implements OnThreadTaskFinishedListener, View.OnClickListener {

    private static final String TAG = "LoginFragment";
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.llUserName)
    LinearLayout llUserName;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.etUserLoginPassword)
    EditText etUserLoginPassword;
    @BindView(R.id.llUserLoginPassword)
    LinearLayout llUserLoginPassword;
    @BindView(R.id.btnLoginButton)
    Button btnLoginButton;

    @BindView(R.id.activity_login)
    LinearLayout activityLogin;
    @BindView(R.id.tvUserRegister)
    TextView tvUserRegister;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.etIPAddressOne)
    EditText etIPAddressOne;
    @BindView(R.id.etIPAddressTwo)
    EditText etIPAddressTwo;
    @BindView(R.id.etIPAddressThree)
    EditText etIPAddressThree;
    @BindView(R.id.etIPAddressFour)
    EditText etIPAddressFour;
    @BindView(R.id.llServerIP)
    LinearLayout llServerIP;


    private LogInAndSignUpActivity mHoldingActivity;
    private String masterLoginName;
    private String masterLoginPassWord;
    private String ipMatcher = "^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$";
    private SharedPreferences sharedPreferences;
    private ProgressDialog contentLoadingProgressBar;
    private String IP;


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public void getLoginInfoFromSp() {
        sharedPreferences = mHoldingActivity.getPreferences(MODE_PRIVATE);
        final String serverIP = sharedPreferences.getString(Constants.KeyConstants.OPENFIRE_SERVER_IP, null);
        final String userName = sharedPreferences.getString(Constants.KeyConstants.MASTER_USER_LOGIN_NAME, null);
        final String loginPsw = sharedPreferences.getString(Constants.KeyConstants.MASTER_USER_LOGIN_PASSWORD, null);

        if (userName == null || loginPsw == null || serverIP == null) return;
//        此处应该把IP设进去
        EditText[] ipEts = {etIPAddressOne, etIPAddressTwo, etIPAddressThree, etIPAddressFour};
        String[] IPs = serverIP.split("\\.");//分割点“.”，要转义该字符，否则识别不出来。
        if (IPs != null && IPs.length == 4) {
            for (int i = 0; i < ipEts.length; i++) {
                ipEts[i].setText(IPs[i]);
            }
        }
        etUserLoginPassword.setText(loginPsw);
        etUserName.setText(userName);

    }

    public void saveLoginInfoToSp(String masterLoginName, String masterLoginPassWord, String serverIP) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor
                .putString(Constants.KeyConstants.OPENFIRE_SERVER_IP, serverIP)
                .putString(Constants.KeyConstants.MASTER_USER_LOGIN_NAME, masterLoginName)
                .putString(Constants.KeyConstants.MASTER_USER_LOGIN_PASSWORD, masterLoginPassWord)
                .commit();
    }

    private void saveTheLoginInfoToDB(final String serverIP, final String userName, final String loginPsw) {
//        Firstly , delete the old LoginInfo.After that it the new LoginIno,
//        because all the LoginInfo have the same ID.
        ThreadUtil.executeThreadTask(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: DB 插入LoginInfo中.....");
                DataBaseUtil
                        .getDataBaseInstance(mHoldingActivity
                                .getApplicationContext()
                                .getApplicationContext())
                        .deleteOldLoginInfo();

                MessageRecord newLoginInfo = new MessageRecord(Constants.DatabaseConstants.LOGIN_INGO_DB_ID
                        , userName, loginPsw, serverIP, null, null,null, null, null, -2, false, false);

                DataBaseUtil.getDataBaseInstance(mHoldingActivity.getApplicationContext()).create(
                        newLoginInfo);
                Log.e(TAG, "run: DB 插入成功");
            }
        });

    }

    private void saveTheCurrentLoginInfoToDataManager(String masterLoginName, String masterLoginPassWord, String serverIP) {
        DataManager.getDataManagerInstance().setServerIP(serverIP);
//            this is for testting
//        DataManager.getDataManagerInstance().setServerIP(Constants.AppConfigConstants.OPEN_FIRE_SERVER_IP);
//        Log.e(TAG, "saveLoginInfoToSp: setIP=" + serverIP);

        DataManager.getDataManagerInstance().setCurrentMasterUserName(masterLoginName);
        DataManager.getDataManagerInstance().setCurrentMasterPassword(masterLoginPassWord);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initializeView(View rootView, Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        addThisOnThreadTaskFinishedListenerToActivityHandler();//add this listener to handler;
//      if the service is alive ,it dose not need to login ;
        if (!Constants.AppConfigConstants.ifNeedToLoginManually) {
            Log.e(TAG, "initializeView: 自动登录isNeedManuallyToLogin=" + Constants.AppConfigConstants.ifNeedToLoginManually);
            login(false);
        }
        initialContentView();
    }

    private void initialContentView() {
        getLoginInfoFromSp();
        btnLoginButton.setOnClickListener(this);
        tvUserRegister.setOnClickListener(this);
    }

    @Override
    protected void castActivity(BaseActivity baseActivity) {
        mHoldingActivity = (LogInAndSignUpActivity) baseActivity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //remove listener when this is destroyed
        removeThisOnThreadTaskFinishedListenerFromActivityHandler();
    }

    @Override
    public void addThisOnThreadTaskFinishedListenerToActivityHandler() {
        mHandler.addListeners(TAG, this);
    }

    @Override
    public void removeThisOnThreadTaskFinishedListenerFromActivityHandler() {
        mHandler.removeListener(TAG);
    }

    @Override
    public void onThreadTaskFinished(int messageType) {

        btnLoginButton.setClickable(true);
        switch (messageType) {
            case Constants.HandlerMessageType.SUCCESS:
//                DataManager.getDataManagerInstance().setCurrentMasterInfo();

//        try {
//
//            VCard userVCard = XmppHelper.getXmppHelperInStance().getUserVCard(Constants.AppConfigConstants.CLIENT_EMAIL);
//            String emailHome = userVCard.getEmailHome();
//            String emailWork = userVCard.getEmailWork();
//            Log.e(TAG, "onThreadTaskFinished: emailHome/emailWork="+emailHome+"/"+emailWork );
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "onThreadTaskFinished: Exception="+e.getMessage() );
//        }


                //kill this LogInAndSignUpActivity
                Log.e(TAG, "onThreadTaskFinished: LoginFragment Login success");
                mHoldingActivity.hideTheProgressbar(contentLoadingProgressBar);
                mHoldingActivity.startService(serviceIntent);
                startMainActivity();
//              kill this LogInAndSignUpActivity
                mHoldingActivity.finish();
                break;
            case Constants.HandlerMessageType.HUMAN_FAILURE:
                mHoldingActivity.hideTheProgressbar(contentLoadingProgressBar);
                DataManager.getDataManagerInstance()
                        .getXmppListenerService()
                        .setXmppTcpConnectionInstance(null);
                loginNameOrPswWrong();
                break;
            case Constants.HandlerMessageType.SERVER_FAILURE:
                mHoldingActivity.hideTheProgressbar(contentLoadingProgressBar);
                DataManager.getDataManagerInstance()
                        .getXmppListenerService()
                        .setXmppTcpConnectionInstance(null);
                connectServerWrong();
                break;
            case Constants.HandlerMessageType.BIND_SERVICE_SUCCESS:
                login(true);
                break;

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginButton:
                setWaiting();
                bindXmppListenerService(mHoldingActivity, TAG, Constants.HandlerMessageType.BIND_SERVICE_SUCCESS);
                break;
            case R.id.tvUserRegister:
                mHoldingActivity.replaceFragmentAndAddToBackStack(new SignUpFragment());
                break;
        }
    }

    //    login logic
    public void login(final boolean isManual) {
        if (isManual) {
            masterLoginName = getStringFromEditText(etUserName);
            masterLoginPassWord = getStringFromEditText(etUserLoginPassword);
            IP = "";
            EditText[] ipEt = {etIPAddressOne, etIPAddressTwo, etIPAddressThree, etIPAddressFour};
            for (int i = 0; i < ipEt.length; i++) {
                String partOfIp = getStringFromEditText(ipEt[i]);
                if (i == 0) {
                    IP = partOfIp;
                } else {
                    IP = IP + "." + partOfIp;
                }
//                Log.e(TAG, "login: IP=" + IP);
            }
            if (!IP.matches(ipMatcher)) {
                Log.e(TAG, "login: IP Address Wrong!!!");
                Toast.makeText(mHoldingActivity, "IP地址填写有误！", Toast.LENGTH_SHORT).show();
                mHoldingActivity.hideTheProgressbar(contentLoadingProgressBar);
                return;
            }


            if (masterLoginName.equals("") || masterLoginPassWord.equals("")) {
                loginNameOrPswWrong();
                mHoldingActivity.hideTheProgressbar(contentLoadingProgressBar);
                return;
            }
            saveTheCurrentLoginInfoToDataManager(masterLoginName, masterLoginPassWord, IP);
        }
//        else {
//
//            ThreadUtil.executeThreadTask(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        XmppHelper.getXmppHelperInStance().login(DataManager.getDataManagerInstance().getCurrentMasterUserName()
//                                , DataManager.getDataManagerInstance().getCurrentMasterPassword());
//                        HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.SUCCESS);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.e(TAG, "login: Exception=" + e.getMessage());
//                    }
//                }
//            });

//        }


        ThreadUtil.executeThreadTask(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isManual) {
                        DataManager.getDataManagerInstance().getXmppListenerService().login(masterLoginName, masterLoginPassWord, IP);
                        saveLoginInfoToSp(masterLoginName, masterLoginPassWord, IP);
                        saveTheLoginInfoToDB(IP, masterLoginName, masterLoginPassWord);
                    }

                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.SUCCESS);
                } catch (XMPPException e) {
                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.HUMAN_FAILURE);
                    Log.e(TAG, "login happen XMPPException=" + e.getMessage());
                    e.printStackTrace();
                } catch (SmackException e) {
                    e.printStackTrace();
                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.SERVER_FAILURE);
                    Log.e(TAG, "login happen SmackException=" + e.getMessage());
                } catch (IOException e) {
                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.SERVER_FAILURE);
                    Log.e(TAG, "login happen IOException=" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });


    }

    private void setWaiting() {
        btnLoginButton.setClickable(false);
        contentLoadingProgressBar = mHoldingActivity.showProgressBar(mHoldingActivity, "please wait...loading ...");
    }

    private void connectServerWrong() {
        mHoldingActivity.showAlertDialog("Tips"
                , "服务器IP地址不对或者服务器连接不上，请稍后再试！"
                , "知道了，下次再试"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (((AlertDialog) dialog).isShowing()) dialog.dismiss();

                    }
                }
                , "觉得不爽，要投诉！"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(mHoldingActivity, "temporaryNotSupport", Toast.LENGTH_SHORT).show();
                        if (((AlertDialog) dialog).isShowing()) dialog.dismiss();
                    }
                }
                , "查看详情"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mHoldingActivity, "temporaryNotSupport", Toast.LENGTH_SHORT).show();
                        if (((AlertDialog) dialog).isShowing()) dialog.dismiss();

                    }
                });
    }

    private void loginNameOrPswWrong() {

        mHoldingActivity.showAlertDialog("Tips"
                , "您的用户名或者密码错了，请更正再试！"
                , "知错了，下次改正"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (((AlertDialog) dialog).isShowing()) dialog.dismiss();

                    }
                }
                , "觉得不爽，要投诉！"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(mHoldingActivity, "temporaryNotSupport", Toast.LENGTH_SHORT).show();
                        if (((AlertDialog) dialog).isShowing()) dialog.dismiss();
                    }
                }
                , null, null);
    }


    public String getStringFromEditText(EditText et) {
        Editable editableMessage = et.getText();
        String strFromEt = editableMessage.toString();
        strFromEt = strFromEt.trim();
        return strFromEt;
    }

    public void startMainActivity() {
        Intent intent = new Intent(mHoldingActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
