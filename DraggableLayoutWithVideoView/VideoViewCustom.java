package idv.haojun.floatingplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class VideoViewCustom extends VideoView {

    private float floatingScale = 0.75f;
    private int layoutWidth;
    private int layoutHeight;
    private int floatingLayoutWidth;
    private int floatingLayoutHeight;

    private int mForceHeight = 0;
    private int mForceWidth = 0;

    public VideoViewCustom(Context context) {
        super(context);
        initView(context);
    }

    public VideoViewCustom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context);
    }

    public VideoViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        layoutWidth = getResources().getDisplayMetrics().widthPixels;
        layoutHeight = (int) (getResources().getDisplayMetrics().density * 200);
        floatingLayoutWidth = (int) (layoutWidth * floatingScale);
        floatingLayoutHeight = (int) (layoutHeight * floatingScale);
        setDimensions(layoutWidth, layoutHeight);
    }

    public void setDimensions(int w, int h) {
        this.mForceHeight = h;
        this.mForceWidth = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mForceWidth, mForceHeight);
    }

    public void topMode() {
        setDimensions(layoutWidth, layoutHeight);
        getHolder().setFixedSize(layoutWidth, layoutHeight);
    }

    public void floatingMode() {
        setDimensions(floatingLayoutWidth, floatingLayoutHeight);
        getHolder().setFixedSize(floatingLayoutWidth, floatingLayoutHeight);
    }
}