package com.example.alv_chi.improject.util;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

/**
 * Created by Alv_chi on 2017/1/15.
 */

public class GetSystemParameterUtil {

    /**
     * 获取actionbar的像素高度，默认使用android官方兼容包做actionbar兼容
     *
     * @return
     */
    public static int getActionBarHeight(AppCompatActivity activity) {

        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar == null) {
            return 0;
        }

        int actionBarHeight = supportActionBar.getHeight();
        if (actionBarHeight != 0) {
            return actionBarHeight;
        }

        final TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (activity.getTheme()
                    .resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, activity.getResources().getDisplayMetrics());
            }
        } else {
            // 使用android.support.v7.appcompat包做actionbar兼容的情况
            if (activity.getTheme()
                    .resolveAttribute(
                            android.support.v7.appcompat.R.attr.actionBarSize,
                            tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, activity.getResources().getDisplayMetrics());
            }

        }
        // else if
        // (getTheme().resolveAttribute(com.actionbarsherlock.R.attr.actionBarSize,
        // tv, true))
        // {
        // //使用actionbarsherlock的actionbar做兼容的情况
        // actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
        // getResources().getDisplayMetrics());
        // }

        return actionBarHeight;
    }

    //    getScreenWidth
    public static int getScreenWidth(Context context) {
        if (context == null) return -1;
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    //    getScreenHeight
    public static int getScreenHeight(Context context) {
        if (context == null) return -1;
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
