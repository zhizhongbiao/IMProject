package com.example.alv_chi.improject.activity;


import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.handler.ActivityHandler;
import com.example.alv_chi.improject.ui.CircleImageView;
import com.example.alv_chi.improject.ui.IconfontTextView;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    protected static final int PUPOP_WINDOW_WRAP_CONTEN = -1;
    protected static final int NO_FRAGMENT = -1;
    protected float currentAngle = 0.0f;
    protected boolean isPopupWindowShowing = false;
    private LinearLayout rootLayout;
    protected ToolbarViewHolder toolbarViewHolder;
    private View lastContentView;
    private static ActivityHandler activityHandler;
    private BaseFragment mCurrentFragment;


    //       subclasses can override this method for customing the toolbar
    protected abstract void intializeToolbar(ToolbarViewHolder toolbarViewHolder);

    //    subClasses need return their LayoutResId
    protected abstract int getContentViewId();

    //    subClasses can override this method to their intent if have
    protected void handleIntent(Intent intent) {
    }

    ;

    //    subClasses need return their LayoutResId if have
    protected int getFragmentContentId() {
        if (mCurrentFragment == null) return NO_FRAGMENT;
        return mCurrentFragment.getLayoutId();
    }

    ;

    protected ActivityHandler getActivityHandler() {
        if (activityHandler == null) {
            activityHandler = new ActivityHandler(this);
        }
        return activityHandler;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_basic);
        initial();

        if (getIntent() != null) {
            handleIntent(getIntent());
        } else {
            Log.e(TAG, "onCreate: intent=null");
        }

    }

    //        initial View
    private void initial() {
        RelativeLayout toolbar = (RelativeLayout) findViewById(R.id.rlCusotmToolbarRoot);
        toolbarViewHolder = new ToolbarViewHolder(toolbar);
        intializeToolbar(toolbarViewHolder);
    }

    protected void startRotationAnimation(View view, float angle, int duration) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", currentAngle, angle);
        rotation
                .setDuration(duration)
                .setInterpolator(new AccelerateInterpolator());
        rotation.setAutoCancel(true);
        rotation.start();
        currentAngle = angle;
    }

    protected PopupWindow createThePopupWindow(View customCotentView
            , int width, int height, PopupWindow.OnDismissListener onDismissListener) {

        PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setElevation(100);
        if (height == PUPOP_WINDOW_WRAP_CONTEN) {
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            popupWindow.setHeight(height);

        }
        if (width == PUPOP_WINDOW_WRAP_CONTEN) {
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            popupWindow.setWidth(width);
        }

        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(customCotentView);
        if (onDismissListener != null) {
            popupWindow.setOnDismissListener(onDismissListener);
        }

//        popupWindow.setAnimationStyle(R.anim.scale_and_alpha);
        popupWindow.setTouchable(true);

        return popupWindow;
    }


    protected void showAlertDialog(String title, String message
            , String positiveButtonMsg, final DialogInterface.OnClickListener positiveButtonOnClickListener
            , String negativeButtonMsg, final DialogInterface.OnClickListener negativeButtonOnClickListener
            , String neutralButtonMsg, final DialogInterface.OnClickListener neutralButtonOnClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder
                .setIcon(R.mipmap.meinv4)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true);

        if (negativeButtonOnClickListener != null) {
            builder.setNegativeButton(negativeButtonMsg, negativeButtonOnClickListener);
        }
        if (neutralButtonOnClickListener != null) {
            builder.setNeutralButton(neutralButtonMsg, neutralButtonOnClickListener);
        }
        if (positiveButtonOnClickListener != null) {
            builder.setPositiveButton(positiveButtonMsg, positiveButtonOnClickListener);
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    protected void showAlertDialogInThread(final String title, final String message
            , final String positiveButtonMsg, final DialogInterface.OnClickListener positiveButtonOnClickListener
            , final String negativeButtonMsg, final DialogInterface.OnClickListener negativeButtonOnClickListener
            , final String neutralButtonMsg, final DialogInterface.OnClickListener neutralButtonOnClickListener) {

        getActivityHandler().post(new Runnable() {
            @Override
            public void run() {
                showAlertDialog(title, message
                        , positiveButtonMsg, positiveButtonOnClickListener
                        , negativeButtonMsg, negativeButtonOnClickListener
                        , neutralButtonMsg, neutralButtonOnClickListener);
            }
        });

    }


    protected void replaceFragmentAndAddToBackStack(BaseFragment fragment) {
        if (fragment == null || mCurrentFragment == fragment) return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(getContentViewId(), fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
        mCurrentFragment = fragment;

    }

    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    public void setmCurrentFragment(BaseFragment mCurrentFragment) {
        this.mCurrentFragment = mCurrentFragment;
    }


    protected class ToolbarViewHolder {

        protected CircleImageView civToolbarCenter;
        protected IconfontTextView itvToolbarLeft;
        protected IconfontTextView itvToolbarRight;
        protected TextView tvToolbarCenter;
        protected RelativeLayout toolbar;

        private ToolbarViewHolder(RelativeLayout toolbar) {
            if (toolbar == null) return;
            this.toolbar = toolbar;
            civToolbarCenter = (CircleImageView) this.toolbar.findViewById(R.id.civToolabrCenter);
            itvToolbarLeft = (IconfontTextView) this.toolbar.findViewById(R.id.itvToolbarLeft);
            itvToolbarRight = (IconfontTextView) this.toolbar.findViewById(R.id.itvToolbarRight);
            tvToolbarCenter = (TextView) this.toolbar.findViewById(R.id.tvToolbarCenter);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, null, false));

    }

    @Override
    public void setContentView(View view) {
        if (rootLayout == null) {
            rootLayout = ((LinearLayout) findViewById(R.id.llBaseRoot));
        }
        rootLayout.removeView(lastContentView);
        rootLayout.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lastContentView = view;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityHandler != null) {
//            kill all tasks in the queue when BacicActivity is destroy:
            activityHandler.removeCallbacksAndMessages(null);
        }
    }

    //    this method is for making sure Activity be dead when the last fragment is dead;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
