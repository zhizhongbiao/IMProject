package com.example.alv_chi.improject.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.handler.ActivityHandler;
import com.example.alv_chi.improject.ui.CircleImageView;
import com.example.alv_chi.improject.ui.IconfontTextView;

public class BasicActivity extends AppCompatActivity {

    private static final String TAG = "BasicActivity";
    private LinearLayout rootLayout;
    protected ToolbarViewHolder toolbarViewHolder;
    private View lastContentView;
    private static ActivityHandler activityHandler;


    public ActivityHandler getActivityHandler() {
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

    }

    //        initial View
    private void initial() {
        RelativeLayout toolbar = (RelativeLayout) findViewById(R.id.rlCusotmToolbarRoot);
        toolbarViewHolder = new ToolbarViewHolder(toolbar);
        intializeToolbar();
    }


    protected void showAlertDialog(String title, String message
            , String positiveButtonMsg, final DialogInterface.OnClickListener positiveButtonOnClickListener
            , String negativeButtonMsg, final DialogInterface.OnClickListener negativeButtonOnClickListener
            , String neutralButtonMsg, final DialogInterface.OnClickListener neutralButtonOnClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(BasicActivity.this);
        builder
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


    //    subclass can override this method for customing the toolbar
    protected void intializeToolbar() {
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
}
