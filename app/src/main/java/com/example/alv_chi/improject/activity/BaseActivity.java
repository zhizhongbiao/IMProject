package com.example.alv_chi.improject.activity;


import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.custom.CircleImageView;
import com.example.alv_chi.improject.custom.IconfontTextView;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.handler.ActivityHandler;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    protected static final int PUPOP_WINDOW_WRAP_CONTEN = -1;
    protected static final int NO_FRAGMENT = -1;
    protected float currentAngle = 0.0f;
    protected boolean isPopupWindowShowing = false;
    private LinearLayout rootLayout;
    protected ToolbarViewHolder toolbarViewHolder;
    private View lastContentView;
    private ActivityHandler activityHandler;
    private BaseFragment mCurrentFragment;
    protected ActivityHandler mHandler;


    //       subclasses can override this method for customing the toolbar
    protected abstract void intializeToolbar(ToolbarViewHolder toolbarViewHolder);

    //    subClasses need return their LayoutResId
    protected abstract int getActivityLayoutId();

    //    subClasses can override this method to their intentFromLastContext if have
    protected void handleIntent(Intent intentFromLastContext) {
    }

    ;

    //    subClasses need return their FragmentContainerResID
    protected abstract int getFragmentContainerId();

    ;

    public ActivityHandler getActivityHandler() {
        if (activityHandler == null) {
            activityHandler = new ActivityHandler();
        }
        return activityHandler;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);//to tell window that activity need transition animation;
        super.setContentView(R.layout.activity_base);

        mHandler = getActivityHandler();

        if (getIntent() != null) {
            handleIntent(getIntent());
        } else {
            Log.e(TAG, "onCreate: intent=null");
        }
        initial();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityHandler != null) {
//            kill all tasks in the queue when BacicActivity is destroy:
            activityHandler.removeCallbacksAndMessages(null);
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

    protected PopupWindow createThePopupWindow(final View customCotentView
            , int width, int height, PopupWindow.OnDismissListener onDismissListener) {

        final PopupWindow popupWindow = new PopupWindow(this);
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

    public void showAlertDialogInThread(final String title, final String message
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

    public void startAnotherActivity(BaseActivity activity, Intent intent) {
        Transition explode = TransitionInflater.from(activity).inflateTransition(R.transition.explode);
        getWindow().setEnterTransition(explode);
        getWindow().setReenterTransition(explode);
        getWindow().setExitTransition(explode);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());

    }


    protected void replaceFragmentAndAddToBackStack(BaseFragment fragment) {
        showFragment(fragment, true);
    }

    protected void replaceFragmentWithoutAddingToBackStack(BaseFragment fragment) {
        showFragment(fragment, false);
    }

    private void showFragment(BaseFragment fragment, boolean isAddToBackStack) {
        if (fragment == null || mCurrentFragment == fragment) return;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction
                .replace(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName())
                .commit();

        mCurrentFragment = fragment;
    }


    protected void removeTheTopFragmentFromBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    public void setCurrentFragment(BaseFragment mCurrentFragment) {
        this.mCurrentFragment = mCurrentFragment;
    }

    public BaseFragment getmCurrentFragment() {
        return this.mCurrentFragment;
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
//        initialize firstFragment for this activity if have one;
        BaseFragment firstFragment = getFirstFragment();
        replaceFragmentAndAddToBackStack(firstFragment);


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

    //    this method is for activity initialize its firstFragment if it has one,
//    if it has not firstFragment ,it must return null;
    protected abstract BaseFragment getFirstFragment();


}
