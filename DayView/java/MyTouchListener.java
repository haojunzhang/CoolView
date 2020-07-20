package com.example.dragdropview;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class MyTouchListener implements View.OnTouchListener {

    int initY;
    int initTopMargin;
    boolean isDragging = false;
    OnEventListener mOnEventListener;

    Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            isDragging = true;
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (!isDragging) {
                    return true;
                }
                handler.removeCallbacks(mLongPressed);

                // 判斷marginTop量
                int dY = (int) event.getRawY() - initY;
                int count = dY / DayViewUtils.FIFTEEN_MINUTE_MARGIN;
                int targetTopMargin = initTopMargin + (count * DayViewUtils.FIFTEEN_MINUTE_MARGIN);
                if (targetTopMargin < DayViewUtils.START_MARGIN_TOP_LIMIT || targetTopMargin > DayViewUtils.END_MARGIN_TOP_LIMIT) {
                    // 到頂/底了
                    return true;
                }
                params.topMargin = targetTopMargin;
                v.setLayoutParams(params);

                if (mOnEventListener != null) {
                    mOnEventListener.onDragging(v, targetTopMargin);
                }
                return true;
            }
            case MotionEvent.ACTION_UP: {
                handler.removeCallbacks(mLongPressed);
                isDragging = false;
                if (mOnEventListener != null) {
                    mOnEventListener.onEventActionUp(v);
                }
                return true;
            }
            case MotionEvent.ACTION_DOWN: {
                handler.postDelayed(mLongPressed, ViewConfiguration.getLongPressTimeout());

                initY = (int) event.getRawY();
                initTopMargin = params.topMargin;

                if (mOnEventListener != null) {
                    mOnEventListener.onEventActionDown(v);
                }
                return true;
            }
        }
        return true;
    }

    public void setEventListener(OnEventListener listener) {
        this.mOnEventListener = listener;
    }

    public interface OnEventListener {
        void onEventActionUp(View v);

        void onEventActionDown(View v);

        void onDragging(View v, int marginTop);
    }
}
