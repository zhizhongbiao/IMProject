package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.activity.LogInAndSignUpActivity;
import com.example.alv_chi.improject.custom.CircleImageView;
import com.example.alv_chi.improject.xmpp.XmppHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "SignUpFragment";
    @BindView(R.id.civAvatar)
    CircleImageView civAvatar;
    @BindView(R.id.btnUploadNewAvatar)
    Button btnUploadNewAvatar;
    @BindView(R.id.etUserName)
    EditText etUserName;
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

    private LogInAndSignUpActivity holdingActivity;
    private String userName;
    private String userID;
    private String userEmail;
    private String userPasssword;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected void initializeView(View rootView, Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        setOnClickListener();

        setIfViewsEnable(true, etUserName, tvEditUserNameForSure);
        setIfViewsEnable(false, etUserPassword, tvEditUserPasswordForSure);
        setIfViewsEnable(false, etUserID, tvEditUserIDForSure);
        setIfViewsEnable(false, etUserEmail, tvEditUserEmailForSure);
        btnRegister.setEnabled(false);
    }

    private void setIfViewsEnable(boolean isEnable, EditText et, TextView tv) {
        et.setEnabled(isEnable);
        tv.setEnabled(isEnable);
    }

    private void setOnClickListener() {
        btnRegister.setOnClickListener(this);
        tvEditUserNameForSure.setOnClickListener(this);
        tvEditUserIDForSure.setOnClickListener(this);
        tvEditUserEmailForSure.setOnClickListener(this);
        tvEditUserPasswordForSure.setOnClickListener(this);
    }

    @Override
    protected void castActivity(BaseActivity baseActivity) {
        holdingActivity = (LogInAndSignUpActivity) baseActivity;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvEditUserNameForSure:
                userName = getTheInfoFromeEditText(etUserName);
                nextItemGetFocus(userName, etUserName, tvEditUserNameForSure, etUserPassword, tvEditUserPasswordForSure);

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
                setIfViewsEnable(true, etUserName, tvEditUserNameForSure);
                break;
            case R.id.btnRegister:
                try {
                    boolean isUserRegisterSuccess = XmppHelper.getXmppHelperInStance().userRegister(userName, userPasssword, userEmail, userID);
                    Log.e(TAG, "onClick: isUserRegisterSuccess="+isUserRegisterSuccess );
                    if (isUserRegisterSuccess) {
                        holdingActivity.removeTheTopFragmentFromBackStack();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onClick: exception="+e.getMessage() );
                }

                break;
        }
    }

    private void nextItemGetFocus(String data, EditText currentEt, TextView currentTv, EditText nextEt, TextView nextTv) {
        if (data != null) {
            setIfViewsEnable(false, currentEt, currentTv);
            if (nextEt == null || nextTv == null) {
                btnRegister.setEnabled(true);
                btnRegister.setFocusable(true);
                btnRegister.requestFocus();
            } else {
                setIfViewsEnable(true, nextEt, nextTv);
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
            Snackbar.make(et, "输入不能为空,请重新输入", Snackbar.LENGTH_SHORT);
        }

        return null;
    }

}
