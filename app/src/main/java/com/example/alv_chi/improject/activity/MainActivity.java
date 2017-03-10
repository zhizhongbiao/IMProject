package com.example.alv_chi.improject.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.adapter.VpFragmentAdapter;
import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.constant.Constants;
import com.example.alv_chi.improject.custom.CircleImageView;
import com.example.alv_chi.improject.custom.IconfontTextView;
import com.example.alv_chi.improject.custom.TabButton;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.fragment.BaseFragment;
import com.example.alv_chi.improject.fragment.ContactsFragment;
import com.example.alv_chi.improject.fragment.GroupsFragment;
import com.example.alv_chi.improject.fragment.RecentChatFragment;
import com.example.alv_chi.improject.fragment.ShareFragment;
import com.example.alv_chi.improject.service.XmppListenerService;
import com.example.alv_chi.improject.util.SystemUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.vpContent)
    ViewPager vpContent;
    @BindView(R.id.tlBottom)
    TabLayout tlBottom;
    @BindView(R.id.civAvatar)
    CircleImageView civAvatar;
    @BindView(R.id.textViewUserName)
    TextView textViewUserName;
    @BindView(R.id.llLogOut)
    LinearLayout llLogOut;
    @BindView(R.id.llSetting)
    LinearLayout llSetting;
    @BindView(R.id.llSocialShare)
    LinearLayout llSocialShare;
    @BindView(R.id.llDonate)
    LinearLayout llDonate;
    @BindView(R.id.llFeedback)
    LinearLayout llFeedback;
    @BindView(R.id.llHelp)
    LinearLayout llHelp;

    @BindView(R.id.dlDrawerRoot)
    DrawerLayout dlDrawerRoot;
    @BindView(R.id.flLeftDrawer)
    FrameLayout flLeftDrawer;


    private FragmentManager mSupportFragmentManager;

    private String[] mTitles = {"聊天", "好友", "群聊", "分享"};
    private int[] mIconfontIds = {R.string.chatting, R.string.friend_chat, R.string.group_chat, R.string.share};
    private VpFragmentAdapter mVpFragmentAdapter;
    private ArrayList<Fragment> fragments;
    private RecentChatFragment mRecentChatFragment;
    private ContactsFragment mContactsFragment;
    private GroupsFragment mGroupsFragment;
    private ShareFragment mShareFragment;
    private TabButton mTabButton;
    private View pupopWindowContentView;
    private PopupWindow mWindow;
    private LinearLayout mAddNewFirend;
    private LinearLayout mScanQCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialFragment();
        initialVpAndTabLayout();
        initializeDrawerLayout();

    }

    @Override
    protected void handleIntent(Intent intentFromLastContext) {
        boolean isFromPendingIntent = intentFromLastContext.getBooleanExtra(Constants.KeyConstants.IS_THIS_INTEN_FROM_PENDING_INTENT, false);
        if (isFromPendingIntent) {

            ArrayList<BaseItem> messages = intentFromLastContext.getParcelableArrayListExtra(Constants.KeyConstants.USER_MESSAGES_RECORD);
            Intent intent = new Intent(this, ChatRoomActivity.class);

            intent.putParcelableArrayListExtra(Constants.KeyConstants.USER_MESSAGES_RECORD, messages);
            intent.putExtra(Constants.KeyConstants.IS_THIS_INTEN_FROM_PENDING_INTENT, true);
            startActivity(intent);
        }
    }

    private void initializeDrawerLayout() {
        dlDrawerRoot.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                if (slideOffset < 0.5) {
                    toolbarViewHolder.itvToolbarLeft.setText(R.string.menu);
                    toolbarViewHolder.itvToolbarLeft.setAlpha(1 - slideOffset * 2);
                } else {
                    toolbarViewHolder.itvToolbarLeft.setText(R.string.left_arrow);
                    toolbarViewHolder.itvToolbarLeft.setAlpha(1 * slideOffset);
                }


            }
        });
    }

    private void initialVpAndTabLayout() {
        tlBottom.setupWithViewPager(vpContent);
        mVpFragmentAdapter = new VpFragmentAdapter(mSupportFragmentManager, fragments);
        vpContent.setAdapter(mVpFragmentAdapter);
//        vpContent.setPageTransformer(false, new DepthPageTransformer());

        int tabCount = tlBottom.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tab = tlBottom.getTabAt(i);
            if (tab != null) {
                mTabButton = new TabButton(this);
                if (i == 0) {
                    mTabButton.setViewColor(Color.CYAN);
                } else {
                    mTabButton.setViewColor(Color.BLACK);
                }

                mTabButton.setViewText(getString(mIconfontIds[i]), mTitles[i]);
                tab.setCustomView(mTabButton);
            }
        }


        tlBottom.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabButton tabButton = (TabButton) tab.getCustomView();
                if (tabButton != null) {
                    tabButton.setViewColor(Color.CYAN);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabButton tabButton = (TabButton) tab.getCustomView();
                if (tabButton != null) {
                    tabButton.setViewColor(Color.BLACK);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initialFragment() {
        mSupportFragmentManager = getSupportFragmentManager();
        fragments = new ArrayList<>();
        mRecentChatFragment = new RecentChatFragment();
        fragments.add(mRecentChatFragment);
        mContactsFragment = new ContactsFragment();
        fragments.add(mContactsFragment);
        mGroupsFragment = new GroupsFragment();
        fragments.add(mGroupsFragment);
        mShareFragment = new ShareFragment();
        fragments.add(mShareFragment);
    }

    @Override
    protected void intializeToolbar(ToolbarViewHolder toolbarViewHolder) {
        initializePopupWindow();

        toolbarViewHolder.tvToolbarCenter.setText(DataManager.getDataManagerInstance().getCurrentMasterUserName());
        toolbarViewHolder.itvToolbarLeft.setText(R.string.menu);
        toolbarViewHolder.itvToolbarRight.setText(R.string.plus);

        toolbarViewHolder.itvToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dlDrawerRoot.isDrawerOpen(GravityCompat.START)) {
                    dlDrawerRoot.closeDrawer(GravityCompat.START);
                    changeText(v, R.string.menu);
                } else {
                    dlDrawerRoot.openDrawer(GravityCompat.START);
                    changeText(v, R.string.left_arrow);
                }
            }

            private void changeText(final View v, final int text) {

                ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 1, 0, 1);
                alpha.setStartDelay(2000);
                alpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (animation.getAnimatedFraction() == 0.5)
                            ((IconfontTextView) v).setText(text);
                    }
                });

            }
        });
        toolbarViewHolder.itvToolbarRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final IconfontTextView iconfontTextView = (IconfontTextView) v;

                mWindow = createThePopupWindow(pupopWindowContentView
                        , SystemUtil.getScreenWidth(MainActivity.this) / 2, PUPOP_WINDOW_WRAP_CONTEN
                        , new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                startRotationAnimation(iconfontTextView, 0, 300);
                                iconfontTextView.setTextColor(Color.BLACK);
                            }
                        }
                );

                iconfontTextView.setTextColor(Color.RED);
                startRotationAnimation(iconfontTextView, 45, 300);
                mWindow.showAsDropDown(v, -450, 10, Gravity.CENTER);


            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContainerId() {
        return NO_FRAGMENT;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

    private void initializePopupWindow() {
        pupopWindowContentView = LayoutInflater.from(this).inflate(R.layout.view_popupwindow_content_view_toolbar_right_activity_main, null, false);
        mAddNewFirend = (LinearLayout) pupopWindowContentView.findViewById(R.id.llAddNewFirend);
        mScanQCode = (LinearLayout) pupopWindowContentView.findViewById(R.id.llScanQCode);
        mAddNewFirend.setOnClickListener(this);
        mScanQCode.setOnClickListener(this);
    }


    //    this onClick dependences ButterKnife
    @OnClick({R.id.civAvatar, R.id.llLogOut, R.id.llSetting
            , R.id.llSocialShare, R.id.llDonate, R.id.llFeedback
            , R.id.llHelp})
    public void onViewClick(View view) {
        int viewId = view.getId();
        switch (viewId) {

//            the Menu Item :
            case R.id.llLogOut:
                stopService(new Intent(this, XmppListenerService.class));
                Intent intent = new Intent(this,LogInAndSignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startAnotherActivity(this, intent);
                this.finish();
                break;
            case R.id.llSetting:
                break;
            case R.id.llSocialShare:
                break;
            case R.id.llDonate:
                break;
            case R.id.llFeedback:
                break;
            case R.id.llHelp:
                Snackbar.make(view, "help", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {

//            the popupWindow Item :
            case R.id.llAddNewFirend:
                Toast.makeText(this, "llAddNewFirend", Toast.LENGTH_SHORT).show();
                mWindow.dismiss();
                break;
            case R.id.llScanQCode:
                Toast.makeText(this, "llScanQCode", Toast.LENGTH_SHORT).show();
                mWindow.dismiss();
                break;
        }
    }


}
