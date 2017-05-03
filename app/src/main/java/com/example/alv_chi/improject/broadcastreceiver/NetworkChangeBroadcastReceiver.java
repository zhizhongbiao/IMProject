package com.example.alv_chi.improject.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.data.constant.Constants;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Alv_chi on 2017/5/2.
 * <p>
 * Listen the Network info to know if it needs to connect again
 */

public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "NetChangeBroadReceiver";
    private BaseActivity baseActivity;

    public NetworkChangeBroadcastReceiver(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String intentAction = intent.getAction();
//            Log.e(TAG, "onReceive: 这是广播接收器接收到的action="+intentAction );
        switch (intentAction) {
//                this is Wifi network situation
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                whenTheWifiNetworkChange(intent);
                break;

//                this is situation about if wifi is enabled
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
//                Log.e("TAG", "这是接收wifi关闭开启的广播wifiState:" + wifiState);
                break;


//            - - - - - - - - - - - - - split line - - - - - - - - -  - - -
//            - - - - - - - - - - - - - split line - - - - - - - - -  - - -


//                this is all Networks situation
            case ConnectivityManager.CONNECTIVITY_ACTION:
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable()) {
                    baseActivity.hideTheWarningText();
                } else {
                    baseActivity.showTheWarningText("网络连接有问题,请检查您设备连接网络情况...");
                }

                break;

            case Constants.KeyConstants
                    .CURRENT_ACCOUNT_IS_LOGINED_BY_OTHERS_EXCEPTION:
                baseActivity.showTheWarningText("你的账号已在别出登陆，请重新登陆");
                break;


        }

    }

    private void whenTheWifiNetworkChange(Intent intent) {
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (networkInfo != null) {
            NetworkInfo.State state = networkInfo.getState();
//                    Log.e(TAG, "onReceive: 这是接收wifi网络切换的广播 NetworkInfo.State=" + state);
            if (state == NetworkInfo.State.CONNECTED) {

            } else if (state == NetworkInfo.State.DISCONNECTED) {

            }
        }
    }
}
