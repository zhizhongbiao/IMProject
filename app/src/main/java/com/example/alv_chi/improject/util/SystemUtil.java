package com.example.alv_chi.improject.util;

import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alv_chi on 2017/1/15.
 */

public class SystemUtil {

    /**
     * 获取actionbar的像素高度，默认使用android官方兼容包做actionbar兼容
     *
     * @return height
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

    public static String getCurrentSystemTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
    }


    public static void showSoftInput(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean isSoftInputOpened(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager.isActive();
    }

//
    public static void Vibrate(Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(Context context,  long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }


}
