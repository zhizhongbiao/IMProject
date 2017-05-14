package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.activity.BaseActivity;
import com.example.alv_chi.improject.activity.MainActivity;
import com.example.alv_chi.improject.adapter.OnRecyclerViewScrollListener;
import com.example.alv_chi.improject.adapter.ReUsableAdapter;
import com.example.alv_chi.improject.bean.BaseItem;
import com.example.alv_chi.improject.bean.ContactItem;
import com.example.alv_chi.improject.custom.LetterNavigationView;
import com.example.alv_chi.improject.data.DataManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class ContactsFragment extends BaseFragment implements LetterNavigationView.OnLetterChooseListener {
    private static final String TAG = "ContactsFragment";
    @BindView(R.id.lnvLetterNavigationView)
    LetterNavigationView lnvLetterNavigationView;
    @BindView(R.id.tvCenterNavigationLetter)
    TextView tvCenterNavigationLetter;
    @BindView(R.id.cvLetterCardView)
    CardView cvLetterCardView;
    @BindView(R.id.rvContactItem)
    RecyclerView rvContactItem;


    private MainActivity mHoldingActivity;
    private LinearLayoutManager linearLayoutManager;
    private ReUsableAdapter reUsableAdapter;
    private boolean isTouchingOnLetterNavigationView = false;
    private int autoRefreshCount = 0;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void initializeView(View rootView, Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        lnvLetterNavigationView.setOnLetterChooseListener(this);
        initializeRvContacts();

    }

    private void initializeRvContacts() {
        ArrayList<BaseItem> contactItems = getBaseItems();

        linearLayoutManager = new LinearLayoutManager(mHoldingActivity);
        rvContactItem.setLayoutManager(linearLayoutManager);
        reUsableAdapter = new ReUsableAdapter(mHoldingActivity
                , contactItems
                , ReUsableAdapter.CONTACTS_ITEM_VIEW_TYPE);
        rvContactItem.setAdapter(reUsableAdapter);
//        Log.e(TAG, "intializeRvContacts: 联系人展示完毕DataManager.getDataManagerInstance().getDatas().size()=" + DataManager.getDataManagerInstance().getDatas().size());
//        Log.e(TAG, "intializeRvContacts: 联系人展示完毕reUsableAdapter.getDatas().size()=" + reUsableAdapter.getDatas().size());

        RvSetScrollListener();

    }

    @NonNull
    private ArrayList<BaseItem> getBaseItems() {
        ArrayList<BaseItem> contactItems = new ArrayList<>();
        for (ContactItem contactItem : DataManager.getDataManagerInstance().getContactItems()) {
            contactItems.add(contactItem);
        }
        return contactItems;
    }

    private void RvSetScrollListener() {
        rvContactItem.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void scrollUp(RecyclerView recyclerView, int scrollYdistance) {
                updateLetterNavigationView();
            }

            @Override
            public void scrollDown(RecyclerView recyclerView, int scrollYdistance) {
                updateLetterNavigationView();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        invoke this method in case it can not getRosters from server
        autoRefreshContactItems();
    }

    private void autoRefreshContactItems() {

        if (DataManager.getDataManagerInstance().getContactItems().size() != 0 || autoRefreshCount > 5)
            return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

               DataManager.getDataManagerInstance().getXmppListenerService().initializeContactsData();

                reUsableAdapter.setDatas(getBaseItems());
                reUsableAdapter.notifyDataSetChanged();
                autoRefreshCount++;
                autoRefreshContactItems();

            }
        }, 1000);
    }



    private void updateLetterNavigationView() {
        if (isTouchingOnLetterNavigationView) return;
        String navigationLetter = (((ContactItem) reUsableAdapter.getDatas()
                .get(linearLayoutManager.findFirstCompletelyVisibleItemPosition()))
                .getNavigationLetter());
        lnvLetterNavigationView.setChosenLetter(navigationLetter);
    }

    @Override
    protected void castActivity(BaseActivity baseActivity) {
        mHoldingActivity = (MainActivity) baseActivity;
    }


//    this from NavigationLetterView 's listener;
    @Override
    public void getChosenLetter(String letter) {

        tvCenterNavigationLetter.setText(letter);
        Integer position = reUsableAdapter.getNavigationPositions().get(letter.toUpperCase());
        if (position == null) {
            Log.e(TAG, "getChosenLetter: position==null");
            return;
        }
        linearLayoutManager.scrollToPositionWithOffset(position, 0);
    }

    @Override
    public void isTouching(boolean isOnTouch) {
        isTouchingOnLetterNavigationView = isOnTouch;
        if (isOnTouch) {
            cvLetterCardView.setVisibility(View.VISIBLE);
        } else {
            cvLetterCardView.setVisibility(View.GONE);
        }
    }

//   this two methods are invoked by MainActivity
    public  void showTheNavigationLetterView()
    {
        lnvLetterNavigationView.setVisibility(View.VISIBLE);
    }


    public void hideTheNavigationLetterViewAndCenterTextView()
    {
        lnvLetterNavigationView.setVisibility(View.GONE);
        cvLetterCardView.setVisibility(View.GONE);

    }


}
