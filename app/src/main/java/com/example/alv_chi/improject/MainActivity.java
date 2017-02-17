package com.example.alv_chi.improject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alv_chi.improject.adapter.VpFragmentAdapter;
import com.example.alv_chi.improject.fragment.ChattingFragment;
import com.example.alv_chi.improject.fragment.FriendChatFragment;
import com.example.alv_chi.improject.fragment.GroupChatFragment;
import com.example.alv_chi.improject.fragment.ShareFragment;
import com.example.alv_chi.improject.ui.CircleImageView;
import com.example.alv_chi.improject.ui.DepthPageTransformer;
import com.example.alv_chi.improject.ui.TabView;
import com.example.alv_chi.improject.util.BitmapUtil;
import com.example.alv_chi.improject.util.ThreadUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.vpContent)
    ViewPager vpContent;
    @BindView(R.id.tlBottom)
    TabLayout tlBottom;
    @BindView(R.id.rlLeftDrawer)
    RelativeLayout rlLeftDrawer;
    @BindView(R.id.dlDrawerRoot)
    DrawerLayout dlDrawerRoot;
    @BindView(R.id.tb)
    Toolbar tb;


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
    private FragmentManager mSupportFragmentManager;

    private String[] mTitles = {"聊天", "好友", "群聊", "分享"};
    private int[] mIconfontIds = {R.string.chatting, R.string.friend_chat, R.string.group_chat, R.string.share};
    private VpFragmentAdapter mVpFragmentAdapter;
    private ArrayList<Fragment> fragments;
    private ChattingFragment mChattingFragment;
    private FriendChatFragment mFriendChatFragment;
    private GroupChatFragment mGroupChatFragment;
    private ShareFragment mShareFragment;
    private TabView mTabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialFragments();
        initialVpAndTabLayout();

        initialToolbar();

        ThreadUtil.executeThreadTask(new Runnable() {
            @Override
            public void run() {
//                connectXMPP();
            }
        });

    }

    private void initialToolbar() {

        setSupportActionBar(tb);//Use the Toolbar as the Actionbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null) {
            return;
        }
        supportActionBar.setHomeButtonEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);// Make the homebutton in the actionbar shows up
//        Make the DrawerLayout work with the homebutton in the actionbar
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, dlDrawerRoot, R.string.menu, R.string.left_arrow);
        actionBarDrawerToggle.syncState();//Synchronize with the homeButton
        dlDrawerRoot.addDrawerListener(actionBarDrawerToggle);

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dlDrawerRoot.isDrawerOpen(GravityCompat.START)) {
                    dlDrawerRoot.closeDrawer(GravityCompat.START);
                } else {
                    dlDrawerRoot.openDrawer(GravityCompat.START);
                }
            }
        });


        tb.setCollapsible(true);
        setUserName("UserName");
        setUserLogo(BitmapFactory.decodeResource(getResources(), R.mipmap.meinv4));
    }

    public void setUserName(String userName) {
        tb.setTitle(userName);
    }

    public void setUserLogo(Bitmap bitmap) {

        Bitmap circleBitmap = BitmapUtil.createCircleBitmap(bitmap, bitmap.getHeight() > bitmap.getWidth() ? bitmap.getWidth() : bitmap.getHeight());
        tb.setLogo(new BitmapDrawable(getResources(), circleBitmap));
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
        mFriendChatFragment = new FriendChatFragment();
        fragments.add(mFriendChatFragment);
        mGroupChatFragment = new GroupChatFragment();
        fragments.add(mGroupChatFragment);
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
