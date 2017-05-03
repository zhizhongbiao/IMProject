package com.example.alv_chi.improject.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.example.alv_chi.improject.custom.CircleImageView;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.data.constant.Constants;
import com.example.alv_chi.improject.handler.HandlerHelper;
import com.example.alv_chi.improject.handler.OnThreadTaskFinishedListener;
import com.example.alv_chi.improject.util.ThreadUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class SignUpFragment extends BaseFragment implements View.OnClickListener, OnThreadTaskFinishedListener {

    private static final String TAG = "SignUpFragment";
    @BindView(R.id.civAvatar)
    CircleImageView civAvatar;
    @BindView(R.id.btnUploadNewAvatar)
    Button btnUploadNewAvatar;
    @BindView(R.id.etLoginUserName)
    EditText etLoginUserName;
    @BindView(R.id.tvEditUserNameForSure)
    TextView tvEditUserNameForSure;
    @BindView(R.id.llUserName)
    LinearLayout llUserName;
    @BindView(R.id.etUserID)
    EditText etUserID;
    @BindView(R.id.tvEditUserIDForSure)
    TextView tvEditUserIDForSure;
    @BindView(R.id.llUserID)
    LinearLayout llUserID;
    @BindView(R.id.etUserEmail)
    EditText etUserEmail;
    @BindView(R.id.tvEditUserEmailForSure)
    TextView tvEditUserEmailForSure;
    @BindView(R.id.llUserEmail)
    LinearLayout llUserEmail;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.etUserPassword)
    EditText etUserPassword;
    @BindView(R.id.tvEditUserPasswordForSure)
    TextView tvEditUserPasswordForSure;

    private LogInAndSignUpActivity mHoldingActivity;
    private String userName;
    private String userID;
    private String userEmail;
    private String userPasssword;
    private ProgressDialog progressDialog;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected void initializeView(View rootView, Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        setOnClickListener();
        addThisOnThreadTaskFinishedListenerToActivityHandler();
        setViewsEnable(true, etLoginUserName, tvEditUserNameForSure);
        setViewsEnable(false, etUserPassword, tvEditUserPasswordForSure);
        setViewsEnable(false, etUserID, tvEditUserIDForSure);
        setViewsEnable(false, etUserEmail, tvEditUserEmailForSure);
        btnRegister.setEnabled(false);
    }

    private void setViewsEnable(boolean isEnable, EditText et, TextView tv) {
        et.setEnabled(isEnable);
//        make sure this EditeText get the focus.
        et.setFocusableInTouchMode(true);
        et.requestFocusFromTouch();
        tv.setEnabled(isEnable);
    }

    private void setOnClickListener() {
        btnRegister.setOnClickListener(this);
        tvEditUserNameForSure.setOnClickListener(this);
        tvEditUserIDForSure.setOnClickListener(this);
        tvEditUserEmailForSure.setOnClickListener(this);
        tvEditUserPasswordForSure.setOnClickListener(this);
        etLoginUserName.setOnClickListener(this);
    }

    @Override
    protected void castActivity(BaseActivity baseActivity) {
        mHoldingActivity = (LogInAndSignUpActivity) baseActivity;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.etLoginUserName:
                btnRegister.setEnabled(false);
                break;
            case R.id.tvEditUserNameForSure:
                userName = getTheInfoFromeEditText(etLoginUserName);
                nextItemGetFocus(userName, etLoginUserName, tvEditUserNameForSure, etUserPassword, tvEditUserPasswordForSure);

                break;
            case R.id.tvEditUserPasswordForSure:
                userPasssword = getTheInfoFromeEditText(etUserPassword);
                nextItemGetFocus(userPasssword, etUserPassword, tvEditUserPasswordForSure, etUserID, tvEditUserIDForSure);
                break;
            case R.id.tvEditUserIDForSure:
                userID = getTheInfoFromeEditText(etUserID);
                nextItemGetFocus(userID, etUserID, tvEditUserIDForSure, etUserEmail, tvEditUserEmailForSure);
                break;
            case R.id.tvEditUserEmailForSure:
                userEmail = getTheInfoFromeEditText(etUserID);
                nextItemGetFocus(userEmail, etUserEmail, tvEditUserEmailForSure, null, null);
                setViewsEnable(true, etLoginUserName, tvEditUserNameForSure);
                break;
            case R.id.btnRegister:
                progressDialog = mHoldingActivity.showProgressBar(mHoldingActivity, "Registering ...please wait...");
                bindXmppListenerService(mHoldingActivity,TAG,Constants.HandlerMessageType.BIND_SERVICE_SUCCESS);
                break;
        }
    }

    private void userRegister() {
        ThreadUtil.executeThreadTask(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean isUserRegisterSuccess = DataManager
                            .getDataManagerInstance()
                            .getXmppListenerService()
                            .userRegister(userName, userPasssword, userEmail, userID
                                    ,Constants.AppConfigConstants.OPEN_FIRE_SERVER_IP);//此处暂时设置为固定的，日后有时间改为动态ServerIP.
                    Log.e(TAG, "onClick: isUserRegisterSuccess=" + isUserRegisterSuccess);
                    if (isUserRegisterSuccess) {
                        HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.SUCCESS);
                    } else {
                        HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.FAILURE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onClick: exception=" + e.getMessage());
                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.FAILURE);
                }
            }
        });
    }

    private void nextItemGetFocus(String data, EditText currentEt, TextView currentTv, EditText nextEt, TextView nextTv) {
        if (data != null) {
            setViewsEnable(false, currentEt, currentTv);
            if (nextEt == null || nextTv == null) {
                btnRegister.setEnabled(true);
//                btnRegister.setFocusable(true);
//                btnRegister.requestFocus();
            } else {
                setViewsEnable(true, nextEt, nextTv);
                nextEt.setFocusable(true);
                nextEt.requestFocus();
            }

        }
    }

    public String getTheInfoFromeEditText(EditText et) {
        if (et != null) {
            String data = et.getText().toString();
            if (data != null && !data.trim().equals("")) {
                return data;
            }
            Toast.makeText(mHoldingActivity, "输入不能为空,请重新输入", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onThreadTaskFinished(int messageType) {
        switch (messageType) {
            case Constants.HandlerMessageType.SUCCESS:
                mHoldingActivity.hideTheProgressbar(progressDialog);
                Toast.makeText(mHoldingActivity, "注册成功！", Toast.LENGTH_SHORT).show();
                mHoldingActivity.removeTheTopFragmentFromBackStack();
                break;
            case Constants.HandlerMessageType.FAILURE:
                DataManager.getDataManagerInstance()
                        .getXmppListenerService()
                        .setXmppTcpConnectionInstance(null);
                mHoldingActivity.hideTheProgressbar(progressDialog);
                Toast.makeText(mHoldingActivity, "注册失败，原因未知", Toast.LENGTH_SHORT).show();
                break;
            case Constants.HandlerMessageType.BIND_SERVICE_SUCCESS:
                userRegister();
                break;
        }
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
    public void onDestroy() {
        super.onDestroy();
        removeThisOnThreadTaskFinishedListenerFromActivityHandler();
    }
}
