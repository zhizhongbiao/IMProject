package com.example.alv_chi.improject.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by Alv_chi on 2017/2/3.
 */

public class LetterNavigationView extends View {

    private static final String TAG = "LetterNavigationView";

    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private int mWidthCwnter;
    private int mHeightCenter;
    private double perLetterAreaHeight;
    private String chosenLetter;
    private final int defautColor = Color.BLACK;
    private final float defautTextSize = 40;
    private Rect rect;

    public LetterNavigationView(Context context) {
        this(context, null);
    }

    public LetterNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LetterNavigationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialPaint();
        rect = new Rect();
    }

    private void initialPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        settingPaint(defautColor, defautTextSize);
    }

    private void settingPaint(int color, float textSize) {
        mPaint.setColor(color);
        mPaint.setTextSize(textSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getHeightAndWidthParameters();
    }

    private void getHeightAndWidthParameters() {
        mHeight = getHeight();
        mWidth = getWidth();
        mWidthCwnter = mWidth / 2;
        mHeightCenter = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mHeightCenter == 0 || mWidthCwnter == 0) {
            getHeightAndWidthParameters();
            invalidate();
            return;
        }

        perLetterAreaHeight = mHeight / 27.0;
        String letter = "Empty";

        for (int i = 0; i < 27; i++) {

            if (i == 26) {
                letter = "#";
            } else {
                letter = ((char) (65 + i)) + "";
            }
            mPaint.getTextBounds(letter, 0, letter.length(), rect);
            int letterHeight = rect.height();
            int letterWidth = rect.width();
            if (letter.equals(chosenLetter)) {
                settingPaint(Color.RED, 45);
            } else {
                settingPaint(defautColor, defautTextSize);
            }
            canvas.drawText(letter, mWidthCwnter - (letterWidth / 2), ((float) (i * perLetterAreaHeight + ((perLetterAreaHeight + letterHeight) / 2))), mPaint);
            rect.setEmpty();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        float y = event.getY();
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:

                setBackgroundColor(getResources().getColor(com.example.alv_chi.improject.R.color.blackTransparentII));

                chosenLetter = getChosenLetter(y);
                invalidate();
                if (onLetterChooseListenerCallback != null) {
                    onLetterChooseListenerCallback.isTouching(true);
                    onLetterChooseListenerCallback.getChosenLetter(chosenLetter);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                chosenLetter = getChosenLetter(y);
                invalidate();
                if (onLetterChooseListenerCallback != null) {
                    onLetterChooseListenerCallback.getChosenLetter(chosenLetter);
                }
                break;
            case MotionEvent.ACTION_UP:

                setBackgroundColor(Color.TRANSPARENT);
                if (onLetterChooseListenerCallback != null) {
                    onLetterChooseListenerCallback.isTouching(false);
                }
                break;
        }
        return true;
    }

    private String getChosenLetter(float y) {
        double num = y / perLetterAreaHeight;//
        long temp = Math.round(num);
        String letter = "null";

        if (temp < 0) {
            temp = 0;
        }
        if (temp > 26) {
            temp = 26;
        }
        if (temp == 26) {
            letter = "#";
        } else {
            letter = ((char) (65 + temp)) + "";
        }

        return letter;
    }

    public interface OnLetterChooseListener {
        void getChosenLetter(String letter);

        void isTouching(boolean isOnTouch);
    }

    private OnLetterChooseListener onLetterChooseListenerCallback = null;

    public void setOnLetterChooseListener(OnLetterChooseListener callback) {
        this.onLetterChooseListenerCallback = callback;
    }
}
