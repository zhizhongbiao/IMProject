package com.example.alv_chi.improject.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.exception.LoginNameOrPasswordException;
import com.example.alv_chi.improject.handler.HandlerHelper;
import com.example.alv_chi.improject.handler.OnThreadTaskFinishedListener;
import com.example.alv_chi.improject.util.ThreadUtil;
import com.example.alv_chi.improject.xmpp.XmppHelper;

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
//    @BindView(R.id.textView4)
//    TextView textView4;
//    @BindView(R.id.etIPAddressOne)
//    EditText etIPAddressOne;
//    @BindView(R.id.etIPAddressTwo)
//    EditText etIPAddressTwo;
//    @BindView(R.id.etIPAddressThree)
//    EditText etIPAddressThree;
//    @BindView(R.id.etIPAddressFour)
//    EditText etIPAddressFour;
//    @BindView(R.id.llServerIP)
//    LinearLayout llServerIP;

    private LogInAndSignUpActivity mHoldingActivity;
    private String masterLoginName;
    private String masterLoginPassWord;
//    private String ipMatcher="^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$";
    private SharedPreferences sharedPreferences;
    private ProgressDialog contentLoadingProgressBar;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public void getLoginInfoFromSp() {
        sharedPreferences = mHoldingActivity.getPreferences(MODE_PRIVATE);
        String userName = sharedPreferences.getString(Constants.KeyConstants.MASTER_USER_LOGIN_NAME, null);
        String loginPsw = sharedPreferences.getString(Constants.KeyConstants.MASTER_USER_LOGIN_PASSWORD, null);

        if (userName == null || loginPsw == null) return;
        etUserLoginPassword.setText(loginPsw);
        etUserName.setText(userName);

    }

    public void saveLoginInfoToSp(String masterLoginName, String masterLoginPassWord) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor
                .putString(Constants.KeyConstants.MASTER_USER_LOGIN_NAME, masterLoginName.trim())
                .putString(Constants.KeyConstants.MASTER_USER_LOGIN_PASSWORD, masterLoginPassWord.trim())
                .commit();

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
        initialContentView();
        addThisOnThreadTaskFinishedListenerToActivityHandler();//add this listener to handler;
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
        removeThisOnThreadTaskFinishedListenerFromActivityHandler();//remove listener when this is destroyed
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
        if (contentLoadingProgressBar!=null)
        {
            contentLoadingProgressBar.dismiss();
        }
        switch (messageType) {
            case Constants.HandlerMessageType.SUCCESS:
                Log.e(TAG, "onThreadTaskFinished: LoginFragment");
                mHoldingActivity.startInComingMessageListenerService();
                mHoldingActivity.startMainActivity();
                saveLoginInfoToSp(masterLoginName, masterLoginPassWord);

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
                mHoldingActivity.finish();
                break;
            case Constants.HandlerMessageType.FAILURE:
                btnLoginButton.setClickable(true);
                break;
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginButton:
                login();
                break;
            case R.id.tvUserRegister:
                mHoldingActivity.replaceFragmentAndAddToBackStack(new SignUpFragment());
                break;
        }
    }

    //    login logic
    public void login() {
        masterLoginName = getStringFromEditText(etUserName);
        masterLoginPassWord = getStringFromEditText(etUserLoginPassword);
//        String IP = "";
//        EditText[] ipEt = {etIPAddressOne, etIPAddressTwo, etIPAddressThree, etIPAddressFour};

//        for (int i = 0; i < ipEt.length; i++) {
//            String partOfIp = getStringFromEditText(ipEt[i]);
//            if (i == 0) {
//                IP = partOfIp;
//            } else {
//                IP = IP + "." + partOfIp;
//            }
//            Log.e(TAG, "login: IP=" + IP);
//
//        }
//        if (!IP.matches(ipMatcher)) {
//            Log.e(TAG, "login: IP Address Wrong!!!" );
//            connectServerWrong(null);
//            return;
//        }
//        DataManager.getDataManagerInstance().setServerIP(IP);
//        DataManager.getDataManagerInstance().setServerIP(Constants.AppConfigConstants.OPEN_FIRE_SERVER_IP);

        if (masterLoginName.equals("") || masterLoginPassWord.equals("")) {
            loginNameOrPswWrong();
            return;
        }
        btnLoginButton.setClickable(false);
        contentLoadingProgressBar = mHoldingActivity.showProgressBar(mHoldingActivity);

        ThreadUtil.executeThreadTask(new Runnable() {
            @Override
            public void run() {
                try {

//                    XmppHelper.getXmppHelperInStance().login(Constants.AppConfigConstants.CLIENT_USER_NAME, Constants.AppConfigConstants.CLIENT_PASSWORD);
                    XmppHelper.getXmppHelperInStance().login(masterLoginName, masterLoginPassWord);
                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.SUCCESS);

                } catch (LoginNameOrPasswordException e) {
                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.FAILURE);
                    Log.e(TAG, "login happen Exception=" + e.getMessage());
                    e.printStackTrace();
                    loginNameOrPswWrong();

                } catch (ConnectException e) {
                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.FAILURE);
                    Log.e(TAG, "login happen Exception=" + e.getMessage());
                    e.printStackTrace();
                    connectServerWrong(e);
                }
            }
        });

    }

    private void connectServerWrong(ConnectException e) {
        mHoldingActivity.showAlertDialogInThread("Tips"
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

        mHoldingActivity.showAlertDialogInThread("Tips"
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


}
