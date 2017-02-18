package com.example.alv_chi.improject.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.adapter.VpFragmentAdapter;
import com.example.alv_chi.improject.fragment.ChattingFragment;
import com.example.alv_chi.improject.fragment.ContactsFragment;
import com.example.alv_chi.improject.fragment.GroupsFragment;
import com.example.alv_chi.improject.fragment.ShareFragment;
import com.example.alv_chi.improject.ui.CircleImageView;
import com.example.alv_chi.improject.ui.DepthPageTransformer;
import com.example.alv_chi.improject.ui.IconfontTextView;
import com.example.alv_chi.improject.ui.TabView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BasicActivity {

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
    @BindView(R.id.rlLeftDrawer)
    RelativeLayout rlLeftDrawer;
    @BindView(R.id.dlDrawerRoot)
    DrawerLayout dlDrawerRoot;

    private FragmentManager mSupportFragmentManager;

    private String[] mTitles = {"聊天", "好友", "群聊", "分享"};
    private int[] mIconfontIds = {R.string.chatting, R.string.friend_chat, R.string.group_chat, R.string.share};
    private VpFragmentAdapter mVpFragmentAdapter;
    private ArrayList<Fragment> fragments;
    private ChattingFragment mChattingFragment;
    private ContactsFragment mContactsFragment;
    private GroupsFragment mGroupsFragment;
    private ShareFragment mShareFragment;
    private TabView mTabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialFragments();
        initialVpAndTabLayout();

        initializeDrawerLayout();


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

    @Override
    protected void intializeToolbar() {
//        toolbarViewHolder.civToolbarCenter.setImageBitmap();
        toolbarViewHolder.tvToolbarCenter.setText("yourLoginName");
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
    }


    private void initialVpAndTabLayout() {
        tlBottom.setupWithViewPager(vpContent);
        mVpFragmentAdapter = new VpFragmentAdapter(mSupportFragmentManager, fragments);
        vpContent.setAdapter(mVpFragmentAdapter);
        vpContent.setPageTransformer(false, new DepthPageTransformer());

        int tabCount = tlBottom.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tab = tlBottom.getTabAt(i);
            if (tab != null) {
                mTabView = new TabView(this);
                if (i == 0) {
                    mTabView.setViewColor(Color.CYAN);
                } else {
                    mTabView.setViewColor(Color.BLACK);
                }

                mTabView.setViewText(getString(mIconfontIds[i]), mTitles[i]);
                tab.setCustomView(mTabView);
            }
        }


        tlBottom.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabView tabView = (TabView) tab.getCustomView();
                if (tabView != null) {
                    tabView.setViewColor(Color.CYAN);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabView tabView = (TabView) tab.getCustomView();
                if (tabView != null) {
                    tabView.setViewColor(Color.BLACK);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initialFragments() {
        mSupportFragmentManager = getSupportFragmentManager();
        fragments = new ArrayList<>();
        mChattingFragment = new ChattingFragment();
        fragments.add(mChattingFragment);
        mContactsFragment = new ContactsFragment();
        fragments.add(mContactsFragment);
        mGroupsFragment = new GroupsFragment();
        fragments.add(mGroupsFragment);
        mShareFragment = new ShareFragment();
        fragments.add(mShareFragment);
    }

    @OnClick({R.id.civAvatar, R.id.llLogOut, R.id.llSetting
            , R.id.llSocialShare, R.id.llDonate, R.id.llFeedback
            , R.id.llHelp})
    public void onViewClick(View view) {
        int viewId = view.getId();
        switch (viewId) {

//            the Menu Item :
            case R.id.llLogOut:
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


}
