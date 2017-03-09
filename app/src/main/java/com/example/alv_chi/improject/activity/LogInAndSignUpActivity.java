package com.example.alv_chi.improject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.LoginFragment;
import com.example.alv_chi.improject.handler.HandlerHelper;
import com.example.alv_chi.improject.handler.OnThreadTaskFinishedListener;
import com.example.alv_chi.improject.util.ThreadUtil;
import com.example.alv_chi.improject.xmpp.XmppHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogInAndSignUpActivity extends BaseActivity implements OnThreadTaskFinishedListener {

    private static final String TAG = "LogInAndSignUpActivity";

    @BindView(R.id.flFragmentContainer)
    FrameLayout flFragmentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if the service is alive ,it dose not need to login ;
        if (!Constants.AppConfigConstants.isNeedToLogin) {
            addThisOnThreadTaskFinishedListenerToActivityHandler();
            login();
        }
        setContentView(R.layout.activity_login_and_signup);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!Constants.AppConfigConstants.isNeedToLogin) {
            removeThisOnThreadTaskFinishedListenerFromActivityHandler();
        }
    }

    @Override
    protected void intializeToolbar(ToolbarViewHolder toolbarViewHolder) {
        toolbarViewHolder.toolbar.setVisibility(View.GONE);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login_and_signup;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.flFragmentContainer;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return LoginFragment.newInstance();
    }

    //    login logic
    public void login() {

        ThreadUtil.executeThreadTask(new Runnable() {
            @Override
            public void run() {
                try {
                    XmppHelper.getXmppHelperInStance().login(DataManager.getDataManagerInstance().getCurrentMasterUserName(), DataManager.getDataManagerInstance().getCurrentMasterPassword());
                    HandlerHelper.sendMessageByHandler(mHandler, TAG, Constants.HandlerMessageType.LOGIN_SUCCESS);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "login: Exception=" + e.getMessage());
                }
            }
        });

    }

    @Override
    public void onThreadTaskFinished() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //kill this LogInAndSignUpActivity
        this.finish();

    }

    @Override
    public void addThisOnThreadTaskFinishedListenerToActivityHandler() {
        mHandler.addListeners(TAG, this);
    }

    @Override
    public void removeThisOnThreadTaskFinishedListenerFromActivityHandler() {
        mHandler.removeListener(TAG);
    }
}
