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
import com.example.alv_chi.improject.bean.ContactItem;
import com.example.alv_chi.improject.custom.LetterNavigationView;
import com.example.alv_chi.improject.data.DataManager;
import com.example.alv_chi.improject.eventbus.EventBusHelper;
import com.example.alv_chi.improject.eventbus.OnUserChattingToStatusEvent;
import com.example.alv_chi.improject.exception.ConnectException;
import com.example.alv_chi.improject.util.ChineseToPinyinHelper;
import com.example.alv_chi.improject.xmpp.XmppHelper;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class ContactsFragment extends BaseFragment implements LetterNavigationView.OnLetterChooseListener, RosterListener {
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
    private Roster roster;

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
        initializeContactsData();

        List<ContactItem> contactItems = DataManager.getDataManagerInstance().getContactItems();

        linearLayoutManager = new LinearLayoutManager(mHoldingActivity);
        rvContactItem.setLayoutManager(linearLayoutManager);
        reUsableAdapter = new ReUsableAdapter(mHoldingActivity
                , contactItems
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
    }


    private void initializeContactsData() {

        try {
            Set<RosterEntry> contacts = XmppHelper.getXmppHelperInStance().getContacts();

            roster = XmppHelper.getXmppHelperInStance().getRoster();
            roster.addRosterListener(this);

            Iterator<RosterEntry> iterator = contacts.iterator();
            while (iterator.hasNext()) {
                RosterEntry rosterEntry = iterator.next();
                if (rosterEntry == null) continue;
                String name = rosterEntry.getName();
                String userJID = rosterEntry.getUser();
                String navigationLetter = null;
                boolean isOnline = false;


                if (name != null) {
                    String pingYin = ChineseToPinyinHelper.getInstance().getPinyin(name).toUpperCase();
                    navigationLetter = pingYin.charAt(0) + "";
                    if (!navigationLetter.matches("[A-Z]")) {
                        navigationLetter = "#";
                    }
                } else {
                    name = "Unknown";
                    navigationLetter = "#";
                }

                Presence presence = XmppHelper.getXmppHelperInStance().getRoster().getPresence(userJID);
                isOnline = getUserStatus(isOnline, presence);
                DataManager.getDataManagerInstance().getIsOnline().put(userJID, isOnline);

                DataManager.getDataManagerInstance().getContactItems().add(new ContactItem(userJID, navigationLetter, name
                        , null, DataManager.getDataManagerInstance().getIsOnline().get(userJID)));//avatar temporary set null
            }

            Collections.sort(DataManager.getDataManagerInstance().getContactItems());//sort the ContactItems

        } catch (ConnectException e) {
            e.printStackTrace();
            Log.e(TAG, "initializeView: ConnectException=" + e.getMessage());
        }


    }

    private boolean getUserStatus(boolean isOnline, Presence presence) {
        if (presence != null) {
            Presence.Type type = presence.getType();

            switch (type) {
                case available:
                    isOnline = true;
                    break;
                default:
                    isOnline = false;
                    break;
            }


        }
        return isOnline;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        clear the contacts;
        roster.removeRosterListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataManager.getDataManagerInstance().clearContactsData();
    }

    private void updateLetterNavigationView() {
        if (isTouchingOnLetterNavigationView) return;
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

    @Override
    public void entriesAdded(Collection<String> addresses) {

    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {

    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {

    }

    @Override
    public void presenceChanged(Presence presence) {
        String JIDFromUser = presence.getFrom();
        Log.e(TAG, "presenceChanged: JIDFromUser=" + JIDFromUser);
        boolean isOnline = false;
        isOnline = getUserStatus(isOnline, presence);
        DataManager.getDataManagerInstance().getIsOnline().put(JIDFromUser, isOnline);

//      this event is posted to  notify the ChattingActivity to change its toolbar status;
        EventBusHelper.getEventBusHelperInstance().getEventBusInstance().post(new OnUserChattingToStatusEvent(presence));

    }
}
