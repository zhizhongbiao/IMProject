package com.example.alv_chi.improject.fragment;

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
    @BindView(R.id.view)
    View view;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;

    private LogInAndSignUpActivity mHoldingActivity;
    private String masterLoginName;
    private String masterLoginPassWord;
    private SharedPreferences sharedPreferences;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public void getLoginInfoFrom() {
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
        getLoginInfoFrom();
        btnLoginButton.setOnClickListener(this);
    }

    @Override
    protected void castActivity(BaseActivity baseActivity) {
        mHoldingActivity = (LogInAndSignUpActivity) baseActivity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    public void onThreadTaskFinished() {
        mHoldingActivity.startInComingMessageListenerService();
//        DataManager.getDataManagerInstance().setCurrentMasterInfo();
        saveLoginInfoToSp(masterLoginName, masterLoginPassWord);
        Log.e(TAG, "onThreadTaskFinished: LoginFragment");
        Intent intent = new Intent(mHoldingActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //kill this LogInAndSignUpActivity
        mHoldingActivity.finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginButton:
                login();
                break;
        }
    }

    //    login logic
    public void login() {
        masterLoginName = getStringFromEditText(etUserName);
        masterLoginPassWord = getStringFromEditText(etUserLoginPassword);
        if (masterLoginName.equals("") || masterLoginPassWord.equals("")) {
            loginNameOrPswWrong();
            return;
        }
        btnLoginButton.setClickable(false);
        ThreadUtil.executeThreadTask(new Runnable() {
            @Override
            public void run() {
                try {

//                    XmppHelper.getXmppHelperInStance().login(Constants.AppConfigConstants.CLIENT_USER_NAME, Constants.AppConfigConstants.CLIENT_PASSWORD);
                    XmppHelper.getXmppHelperInStance().login(masterLoginName, masterLoginPassWord);
                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.LOGIN_SUCCESS);

                } catch (LoginNameOrPasswordException e) {
                    e.printStackTrace();
                    loginNameOrPswWrong();

                } catch (ConnectException e) {
                    e.printStackTrace();
                    connectServerWrong(e);
                }
            }
        });

    }

    private void connectServerWrong(ConnectException e) {
        btnLoginButton.post(new Runnable() {
            @Override
            public void run() {
                btnLoginButton.setClickable(true);
            }
        });
        Log.e(TAG, "happen Exception");
        e.printStackTrace();
        mHoldingActivity.showAlertDialogInThread("Tips"
                , "服务器连接不上，请稍后再试！"
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
        btnLoginButton.post(new Runnable() {
            @Override
            public void run() {
                btnLoginButton.setClickable(true);
            }
        });


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
