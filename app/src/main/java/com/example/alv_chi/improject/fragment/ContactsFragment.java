package com.example.alv_chi.improject.fragment;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.example.alv_chi.improject.R;
import com.example.alv_chi.improject.ui.LetterNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class ContactsFragment extends BaseFragment implements LetterNavigationView.OnLetterChooseListener {
    @BindView(R.id.lnvLetterNavigationView)
    LetterNavigationView lnvLetterNavigationView;
    @BindView(R.id.tvCenterNavigationLetter)
    TextView tvCenterNavigationLetter;
    @BindView(R.id.cvLetterCardView)
    CardView cvLetterCardView;

    @Override
    public int getLayoutId() {
        return R.layout.friend_chat_fragment;
    }

    @Override
    protected void initializeView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        lnvLetterNavigationView.setOnLetterChooseListener(this);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (mContentView == null) {
//            mContentView = inflater.inflate(, container, false);
////            ButterKnife.bind(this, mContentView);
//
//
//        }
//
//        return mContentView;
//    }

    @Override
    public void getChosenLetter(String letter) {
        if (cvLetterCardView.getVisibility() == View.VISIBLE) {
            tvCenterNavigationLetter.setText(letter);
        }
    }

    @Override
    public void isTouching(boolean isOnTouch) {

        int visibility = cvLetterCardView.getVisibility();
        if (isOnTouch && (visibility == View.GONE || visibility == View.INVISIBLE)) {
            cvLetterCardView.setVisibility(View.VISIBLE);
        } else {
            cvLetterCardView.setVisibility(View.GONE);
        }


    }
}
