package com.example.dragdropview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayView extends FrameLayout {

    LockableScrollView mLockableScrollView;
    LinearLayout mLinearLayout;

    // 容器
    FrameLayout mFrameLayout;

    // 左側時間, 分隔線
    RecyclerView mRecyclerView;

    // 左側指示器
    TextView mDraggingIndicatorTextView;

    // DayView監聽器
    OnDayViewListener mOnDayViewListener;

    TimeAdapter adapter;

    List<DayViewEvent> eventList;
    List<View> eventViewList;

    public DayView(@NonNull Context context) {
        super(context);
        init();
    }

    public DayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.day_view, this, true);
        mLockableScrollView = findViewById(R.id.lockableScrollView);
        mLinearLayout = findViewById(R.id.linearLayout);
        mFrameLayout = findViewById(R.id.frameLayout);
        mRecyclerView = findViewById(R.id.recyclerView);

        eventList = new ArrayList<>();
        eventViewList = new ArrayList<>();

        // 容器高度
        mFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DayViewUtils.TOTAL_LAYOUT_HEIGHT));

        // 左邊刻度, 分隔線
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter = new TimeAdapter(getContext(), R.layout.time_item));
        adapter.setData(getTimeList());

        // 預設可滑動
        mLockableScrollView.setScrollingEnabled(true);
    }

    private DayViewEvent getDayViewEventByTime(String time) {
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);

        for (DayViewEvent event : eventList) {
            if (hour == event.getFromCalendar().get(Calendar.HOUR_OF_DAY) &&
                    minute == event.getFromCalendar().get(Calendar.MINUTE)) {
                return event;
            }
        }
        return null;
    }

    public void setEventList(List<DayViewEvent> eventList) {
        this.eventList = eventList;
        clearAllEvent();
        refreshEvent();
    }

    private void clearAllEvent() {
        for (View view : eventViewList) {
            mFrameLayout.removeView(view);
        }
        eventViewList.clear();
    }

    private void refreshEvent() {
        for (DayViewEvent event : eventList) {
            addEvent(event);
        }
    }

    @SuppressLint("DefaultLocale")
    private List<String> getTimeList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list.add(String.format("%02d:00", i));
        }
        return list;
    }

    private class TimeAdapter extends BaseRecyclerViewAdapter<String> {

        public TimeAdapter(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        protected void convert(Context context, final BaseViewHolder holder, final String time) {
            holder.setText(R.id.tvTime, time);

            RelativeLayout rl = holder.getView(R.id.itemLayout);
            rl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 4 * DayViewUtils.FIFTEEN_MINUTE_MARGIN));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDayViewListener != null) {
                        mOnDayViewListener.onEmptySpaceClick(v, time);
                    }
                }
            });
        }
    }

    @SuppressLint("DefaultLocale")
    public void addEvent(DayViewEvent event) {
        Button button = new Button(getContext());
        MyTouchListener listener = new MyTouchListener();
        listener.setEventListener(new MyTouchListener.OnEventListener() {

            // 按下去那瞬間的marginTop
            int actionDownMarginTop;

            @Override
            public void onEventActionUp(View v) {
                mLockableScrollView.setScrollingEnabled(true);

                // 移除左側指示器
                mFrameLayout.removeView(getDraggingIndicatorTextView());

                // 如果移動後有重疊則恢復原狀
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
                boolean isOverlapping = DayViewUtils.isOverlapping(eventViewList);
                if (isOverlapping) {
                    // 重置
                    params.topMargin = actionDownMarginTop;
                    v.setLayoutParams(params);

                    if (mOnDayViewListener != null) {
                        mOnDayViewListener.onOverlapping(v);
                    }
                } else {
                    // 設DayViewEvent新值
                    DayViewEvent oldEvent = getDayViewEventByTime(DayViewUtils.getTimeByMargin(actionDownMarginTop));
                    if (oldEvent != null) {
                        String newFromTime = DayViewUtils.getTimeByMargin(params.topMargin);
                        oldEvent.setFromTimeWithOriginalOffset(newFromTime);
                    }
                }

                for (DayViewEvent e : eventList) {
                    L.d(e.toString());
                }
            }

            @Override
            public void onEventActionDown(View v) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
                actionDownMarginTop = params.topMargin;
            }

            @Override
            public void onDragging(View v, int marginTop) {
                // 如果事件正在滑動, 則停止ScrollView滑動
                if (mLockableScrollView.isScrollable()) {
                    mLockableScrollView.setScrollingEnabled(false);
                }
                TextView tv = getDraggingIndicatorTextView();

                // 先移除左側指示器
                mFrameLayout.removeView(getDraggingIndicatorTextView());

                // 再新增左側指示器
                tv.setText(DayViewUtils.getTimeByMargin(marginTop));
                tv.setTextColor(Color.BLUE);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = marginTop;
                params.leftMargin = 0;
                tv.setLayoutParams(params);
                mFrameLayout.addView(tv);
            }
        });
        button.setOnTouchListener(listener);

        // 計算marginTop, EventView高度
        String fromTime = String.format("%02d:%02d", event.getFromCalendar().get(Calendar.HOUR_OF_DAY), event.getFromCalendar().get(Calendar.MINUTE));
        String toTime = String.format("%02d:%02d", event.getToCalendar().get(Calendar.HOUR_OF_DAY), event.getToCalendar().get(Calendar.MINUTE));
        int fromMargin = DayViewUtils.getMarginByTime(fromTime);
        int toMargin = DayViewUtils.getMarginByTime(toTime);
        int eventViewHeight = toMargin - fromMargin - 10; // -10為了留空
        FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, eventViewHeight);
        tvParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, getResources().getDisplayMetrics());
        tvParams.topMargin = fromMargin;
        button.setLayoutParams(tvParams);

        // 顏色, 文字
        button.setBackgroundColor(Color.RED);
        button.setText(event.getContent());
        button.setTextColor(Color.WHITE);
        eventViewList.add(button);
        mFrameLayout.addView(button);
    }

    private TextView getDraggingIndicatorTextView() {
        if (mDraggingIndicatorTextView == null) {
            mDraggingIndicatorTextView = new TextView(getContext());
        }
        return mDraggingIndicatorTextView;
    }

    public void setOnDayViewListener(OnDayViewListener listener) {
        this.mOnDayViewListener = listener;
    }

    public interface OnDayViewListener {
        void onEmptySpaceClick(View v, String time);

        void onOverlapping(View v);
    }
}
