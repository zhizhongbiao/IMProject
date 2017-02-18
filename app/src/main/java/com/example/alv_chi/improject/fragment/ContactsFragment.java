package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.ui.LetterNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class ContactsFragment extends Fragment implements LetterNavigationView.OnLetterChooseListener {

    @BindView(R.id.lnvLetterNavigationView)
    LetterNavigationView lnvLetterNavigationView;
    @BindView(R.id.tvCenterNavigationLetter)
    TextView tvCenterNavigationLetter;
    @BindView(R.id.cvLetterCardView)
    CardView cvLetterCardView;
    private View mContentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(R.layout.friend_chat_fragment, container, false);
            ButterKnife.bind(this, mContentView);

            lnvLetterNavigationView.setOnLetterChooseListener(this);
        }
        ButterKnife.bind(this, mContentView);
        return mContentView;
    }

    @Override
    public void dealWithChosenLetter(String letter) {
        if (cvLetterCardView.getVisibility() == View.VISIBLE) {
            tvCenterNavigationLetter.setText(letter);
        }
    }

    @Override
    public void isOnTouch(boolean isOnTouch) {

        int visibility = cvLetterCardView.getVisibility();
        if (isOnTouch && (visibility == View.GONE || visibility == View.INVISIBLE)) {
            cvLetterCardView.setVisibility(View.VISIBLE);
        } else {
            cvLetterCardView.setVisibility(View.GONE);
        }


    }
}
