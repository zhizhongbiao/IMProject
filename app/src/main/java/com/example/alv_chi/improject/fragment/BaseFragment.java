package com.example.alv_chi.improject.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.handler.HandlerHelper;
import com.example.alv_chi.improject.handler.MyHandler;
import com.example.alv_chi.improject.xmpp.service.XmppListenerService;

import static android.content.Context.BIND_AUTO_CREATE;


/**
 * Created by Alv_chi on 2017/2/20.
 */

public abstract class BaseFragment extends Fragment {

    private View mContentView;
    private BaseActivity mHoldingActivity;
    private static final String TAG = "BaseFragment";
    protected MyHandler mHandler;

    private ServiceConnection serviceConnection;
    protected Intent serviceIntent;


    //   this method is for getting subClass LayoutResId to  be loaded in this basic Fragment:
    public abstract int getLayoutId();

    //    this method is for the subClasses to handle their contentView:
    protected abstract void initializeView(View rootView, Bundle savedInstanceState);


    //    this method is for the subClasses to handle their contentView:
    protected abstract void castActivity(BaseActivity baseActivity);

    protected BaseActivity getHoldingActivity() {
        return mHoldingActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHoldingActivity = (BaseActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(getLayoutId(), container, false);
            castActivity(getHoldingActivity());
            mHandler = getHoldingActivity().getMyHandler();
            Bundle bundle = getArguments();
            if (bundle != null) {
                handleBundleFromOutside(bundle);
            }
            serviceIntent = new Intent(mHoldingActivity, XmppListenerService.class);
            initializeView(mContentView, savedInstanceState);
        }
        return mContentView;
    }

//    subclass can override this method to handle bundleData from outside if needed;
    protected void handleBundleFromOutside(Bundle bundle) {
    }



    @Override
    public void onResume() {
        super.onResume();
        mHoldingActivity.setCurrentFragment(this);
    }



    public void bindXmppListenerService(BaseActivity baseActivity,final String listenerKeyByObj, final int messageTypeByParamWhat) {

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                DataManager.getDataManagerInstance()
                        .setXmppListenerService(((XmppListenerService.MyBinder) iBinder).getXmppListenerService());
                HandlerHelper.sendMessageByHandler(mHandler,listenerKeyByObj,messageTypeByParamWhat);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, "onServiceDisconnected: ComponentName=" + name.toString());
            }
        };

        Log.e(TAG, "bindXmppListenerService: isBindServiceSuccess=" +
                baseActivity.bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (serviceConnection!=null)
        {
            mHoldingActivity.unbindService(serviceConnection);
//            Log.e(TAG, "onStop: unbindService" );
        }
    }
}
