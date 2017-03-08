package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
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
import com.example.alv_chi.improject.custom.LetterNavigationView;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.xmpp.XmppHelper;

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
    private boolean isTouchingOnLetterNavigationView=false;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void initializeView(View rootView, Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        lnvLetterNavigationView.setOnLetterChooseListener(this);

        intializeRvContacts();

    }

    private void intializeRvContacts() {
        try {
            linearLayoutManager = new LinearLayoutManager(mHoldingActivity);
            rvContactItem.setLayoutManager(linearLayoutManager);
            reUsableAdapter = new ReUsableAdapter(mHoldingActivity
                    , XmppHelper.getXmppHelperInStance().getContacts()
                    , ReUsableAdapter.CONTACTS_ITEM_VIEW_TYPE);
            rvContactItem.setAdapter(reUsableAdapter);

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


        } catch (ConnectException e) {
            e.printStackTrace();
            Log.e(TAG, "initializeView: ConnectException=" + e.getMessage());
        }
    }

    private void updateLetterNavigationView() {
        if (isTouchingOnLetterNavigationView)return;
        String navigationLetter = reUsableAdapter.getContactItems().get(linearLayoutManager.findFirstCompletelyVisibleItemPosition()).getNavigationLetter();
        lnvLetterNavigationView.setChosenLetter(navigationLetter);
    }

    @Override
    protected void castActivity(BaseActivity baseActivity) {
        mHoldingActivity = (MainActivity) baseActivity;
    }

    @Override
    public void getChosenLetter(String letter) {

        tvCenterNavigationLetter.setText(letter);
        Integer position = reUsableAdapter.getNavigationPositions().get(letter.toUpperCase());
        linearLayoutManager.scrollToPositionWithOffset(position,0);

    }

    @Override
    public void isTouching(boolean isOnTouch) {
        isTouchingOnLetterNavigationView=isOnTouch;
        if (isOnTouch) {
            cvLetterCardView.setVisibility(View.VISIBLE);
        } else {
            cvLetterCardView.setVisibility(View.GONE);
        }


    }

}
