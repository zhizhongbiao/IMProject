package com.example.alv_chi.improject.activity;

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
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.exception.LoginNameOrPasswordException;
import com.example.alv_chi.improject.handler.ActivityHandler;
import com.example.alv_chi.improject.handler.HandlerHelper;
import com.example.alv_chi.improject.handler.OnThreadTaskFinishedListener;
import com.example.alv_chi.improject.util.ThreadUtil;
import com.example.alv_chi.improject.xmpp.XmppHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements OnThreadTaskFinishedListener {

    private static final String TAG = "LoginActivity";
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
    private ActivityHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mHandler = getActivityHandler();
        mHandler.addListeners(TAG, this);
    }

//    login logic
    public void onUserLoginClick(View view) {
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
                    showAlertDialogInThread("Tips"
                            ,"您的用户名或者密码错了，请更正再试！"
                            ,"知错了，下次改正"
                            ,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (((AlertDialog) dialog).isShowing()) dialog.dismiss();

                        }
                    }
                    ,"觉得不爽，要投诉！"
                    ,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(LoginActivity.this, "temporaryNotSupport", Toast.LENGTH_SHORT).show();
                                    if (((AlertDialog) dialog).isShowing()) dialog.dismiss();
                                }
                            }
                    ,null,null);

                } catch (ConnectException e) {
                    btnLoginButton.post(new Runnable() {
                        @Override
                        public void run() {
                            btnLoginButton.setClickable(true);
                        }
                    });
                    Log.e(TAG, "happen Exception" );
                    e.printStackTrace();
                    showAlertDialogInThread("Tips"
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

                                    Toast.makeText(LoginActivity.this, "temporaryNotSupport", Toast.LENGTH_SHORT).show();
                                    if (((AlertDialog) dialog).isShowing()) dialog.dismiss();
                                }
                            }
                            , "查看详情"
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(LoginActivity.this, "temporaryNotSupport", Toast.LENGTH_SHORT).show();
                                    if (((AlertDialog) dialog).isShowing()) dialog.dismiss();

                                }
                            });
                }
            }
        });



    }


    @Override
    protected void intializeToolbar() {
        toolbarViewHolder.toolbar.setVisibility(View.GONE);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeListener(TAG);//remove listener when this is destroyed
    }

    @Override
    public void loginSuccess() {
        Log.e(TAG, "loginSuccess: startActivity");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();//kill this LoginActivity
    }
}
