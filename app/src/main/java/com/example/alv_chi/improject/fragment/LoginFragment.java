package com.example.alv_chi.improject.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.exception.LoginNameOrPasswordException;
import com.example.alv_chi.improject.handler.HandlerHelper;
import com.example.alv_chi.improject.handler.OnThreadTaskFinishedListener;
import com.example.alv_chi.improject.service.InComingMessageListenerService;
import com.example.alv_chi.improject.util.ThreadUtil;
import com.example.alv_chi.improject.xmpp.XmppHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private Intent serviceIntent;

    public static LoginFragment newInstance() {
        return new LoginFragment();
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
        initService();
        Log.e(TAG, "onThreadTaskFinished: startActivity");
        Intent intent = new Intent(mHoldingActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        mHoldingActivity.finish();//kill this LogInAndSignUpActivity
    }


    private void initService() {
        serviceIntent = new Intent(mHoldingActivity, InComingMessageListenerService.class);
        startInComingMessageListenerService(serviceIntent);

    }

    public void startInComingMessageListenerService(Intent serviceIntent) {
        mHoldingActivity.startService(serviceIntent);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginButton:
                onLoginBtnClick(v);
                break;
        }
    }

    //    login logic
    public void onLoginBtnClick(View view) {
        btnLoginButton.setClickable(false);
        ThreadUtil.executeThreadTask(new Runnable() {
            @Override
            public void run() {
                try {

                    XmppHelper.getXmppHelperInStance().login(Constants.AppConfigConstants.CLIENT_USER_NAME, Constants.AppConfigConstants.CLIENT_PASSWORD);
                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.LOGIN_SUCCESS);

                } catch (LoginNameOrPasswordException e) {
                    btnLoginButton.post(new Runnable() {
                        @Override
                        public void run() {
                            btnLoginButton.setClickable(true);
                        }
                    });

                    e.printStackTrace();
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

                } catch (ConnectException e) {
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
            }
        });


    }
}
