package idv.haojun.floatingplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class DraggableLayout extends RelativeLayout {

    // status
    public static final int STATUS_TOP = 0;
    public static final int STATUS_FLOATING = 1;
    private int status = STATUS_TOP;

    // bound
    public static int TOP_MARGIN_LIMIT_FLOATING_MODE;
    public static int LEFT_MARGIN_LIMIT_FLOATING_MODE;
    public static int RIGHT_MARGIN_LIMIT_FLOATING_MODE;
    public static int BOTTOM_MARGIN_LIMIT_FLOATING_MODE;

    // scale
    private float floatingScale = 0.75f;

    // width & height
    private int screenHeight;
    private int statusBarHeight;
    private int navigationBarHeight;
    private int layoutWidth;
    private int layoutHeight;
    private int floatingLayoutWidth;
    private int floatingLayoutHeight;

    // params
    private RelativeLayout.LayoutParams layoutParams;

    // resize animation
    private int currentHeight, currentWidth;
    private boolean isResizing = false;

    // listener
    private OnModeChangeListener onModeChangeListener;

    public DraggableLayout(Context context) {
        super(context);
        initView(context);
    }

    public DraggableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DraggableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        TOP_MARGIN_LIMIT_FLOATING_MODE = (int) (context.getResources().getDisplayMetrics().density * 20);
        LEFT_MARGIN_LIMIT_FLOATING_MODE = (int) (context.getResources().getDisplayMetrics().density * 0);
        RIGHT_MARGIN_LIMIT_FLOATING_MODE = (int) (context.getResources().getDisplayMetrics().density * 0);
        BOTTOM_MARGIN_LIMIT_FLOATING_MODE = (int) (context.getResources().getDisplayMetrics().density * 0);

        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        statusBarHeight = EnvironmentHelper.getStatusBarHeight(context);
        navigationBarHeight = EnvironmentHelper.getNavigationBarHeight(context);
        layoutWidth = context.getResources().getDisplayMetrics().widthPixels;
        layoutHeight = (int) (context.getResources().getDisplayMetrics().density * 200);
        floatingLayoutWidth = (int) (layoutWidth * floatingScale);
        floatingLayoutHeight = (int) (layoutHeight * floatingScale);

        currentHeight = layoutHeight;
        currentWidth = layoutWidth;

        setOnTouchListener(new OnTouchListener() {

            float downX, downY;
            int downTopMargin, downLeftMargin, downBottomMargin, downRightMargin;

            float moveX, moveY;
            int moveTopMargin, moveLeftMargin, moveBottomMargin, moveRightMargin;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        layoutParams = (LayoutParams) getLayoutParams();
                        downX = event.getRawX();
                        downY = event.getRawY();

                        downTopMargin = layoutParams.topMargin;
                        downLeftMargin = layoutParams.leftMargin;
                        downBottomMargin = layoutParams.bottomMargin;
                        downRightMargin = layoutParams.rightMargin;
//                        L.d("b:" + downBottomMargin);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveX = event.getRawX();
                        moveY = event.getRawY();

                        float deltaX = event.getRawX() - downX;
                        float deltaY = event.getRawY() - downY;


                        // resize width, height
                        if (isTopOutBound(view.getTop())) {
                            topMode();
                        } else {
                            floatingMode();
                        }

                        // moving
                        if (!isResizing) {
                            moveTopMargin = (int) (downTopMargin + deltaY);
                            moveLeftMargin = (int) (downLeftMargin + deltaX);
                            moveBottomMargin = (int) (downBottomMargin - deltaY);
                            moveRightMargin = (int) (downRightMargin - deltaX);
                            setParams();
                            setLayoutParams(layoutParams);
                            invalidate();
                        }
                        break;
                }
                return true;
            }

            private boolean isTopOutBound(int topMargin) {
                return topMargin <= TOP_MARGIN_LIMIT_FLOATING_MODE;
            }

            private boolean isLeftOutBound(int leftMargin) {
                return leftMargin <= LEFT_MARGIN_LIMIT_FLOATING_MODE;
            }

            private boolean isRightOutBound(int rightMargin) {
                return rightMargin <= RIGHT_MARGIN_LIMIT_FLOATING_MODE;
            }

            private boolean isBottomOutBound(int bottomMargin) {
                return bottomMargin <= -(screenHeight - floatingLayoutHeight - statusBarHeight - BOTTOM_MARGIN_LIMIT_FLOATING_MODE);
            }

            private void setParams() {

                if (isTopOutBound(moveTopMargin)) {
                    layoutParams.topMargin = 0;
                    layoutParams.leftMargin = 0;
                    layoutParams.rightMargin = 0;
                    layoutParams.bottomMargin = 0;
                } else if ((isLeftOutBound(moveLeftMargin) || isRightOutBound(moveRightMargin)) && isBottomOutBound(moveBottomMargin)) {
//                    layoutParams.topMargin = topMargin;
//                    layoutParams.leftMargin = leftMargin;
//                    layoutParams.rightMargin = rightMargin;
//                    layoutParams.bottomMargin = bottomMargin;
                } else if (isLeftOutBound(moveLeftMargin) || isRightOutBound(moveRightMargin)) {
                    layoutParams.topMargin = moveTopMargin;
//                    layoutParams.leftMargin = leftMargin;
//                    layoutParams.rightMargin = rightMargin;
                    layoutParams.bottomMargin = moveBottomMargin;
                } else if (isBottomOutBound(moveBottomMargin)) {
//                    layoutParams.topMargin = topMargin;
                    layoutParams.leftMargin = moveLeftMargin;
                    layoutParams.rightMargin = moveRightMargin;
//                    layoutParams.bottomMargin = bottomMargin;
                } else {
                    layoutParams.topMargin = moveTopMargin;
                    layoutParams.leftMargin = moveLeftMargin;
                    layoutParams.rightMargin = moveRightMargin;
                    layoutParams.bottomMargin = moveBottomMargin;
                }

            }

            private void topMode() {
                resizeView(
                        floatingLayoutHeight, layoutHeight,
                        floatingLayoutWidth, layoutWidth,
                        layoutParams.topMargin, 0,
                        layoutParams.leftMargin, 0,
                        layoutParams.rightMargin, 0
                );
                status = STATUS_TOP;
                if (onModeChangeListener != null) onModeChangeListener.onModeChange(status);
            }

            private void floatingMode() {
                resizeView(
                        layoutHeight, floatingLayoutHeight,
                        layoutWidth, floatingLayoutWidth,
                        0, (int) (moveY - floatingLayoutHeight / 2),
                        0, (int) (moveX - floatingLayoutWidth / 2),
                        0, (int) (layoutWidth - moveX - floatingLayoutWidth / 2)
                );

                status = STATUS_FLOATING;
                if (onModeChangeListener != null) onModeChangeListener.onModeChange(status);
            }

            private void resizeView(
                    int startHeight, int endHeight,
                    int startWidth, int endWidth,
                    int startTopMargin, int endTopMargin,
                    int startLeftMargin, int endLeftMargin,
                    int startRightMargin, int endRightMargin
            ) {
                if (currentHeight == endHeight && currentWidth == endWidth) return;
                currentHeight = endHeight;
                currentWidth = endWidth;

                // floating
                if (startHeight > endHeight) {
                    if (isLeftOutBound(endLeftMargin)) {
                        endLeftMargin = 0;
                        endRightMargin = layoutWidth - floatingLayoutWidth;
                    } else if (isRightOutBound(endRightMargin)) {
                        endLeftMargin = layoutWidth - floatingLayoutWidth;
                        endRightMargin = 0;
                    }
//                    downTopMargin = endTopMargin;
                    downLeftMargin = endLeftMargin;
                    downRightMargin = endRightMargin;
                }

                Animation anim = new ResizeAnimation(
                        DraggableLayout.this,
                        startHeight, endHeight,
                        startWidth, endWidth,
                        startTopMargin, endTopMargin,
                        startLeftMargin, endLeftMargin,
                        startRightMargin, endRightMargin
                );

//                anim.setDuration(200);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isResizing = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                isResizing = true;
                startAnimation(anim);
            }
        });
    }

    public void setOnModeChangeListener(OnModeChangeListener onModeChangeListener) {
        this.onModeChangeListener = onModeChangeListener;
    }


//    private float currentScale = 1.0f;
//    private float currentScale2 = 1.0f;
//
//    public void scaleView(View v, float startScale, float endScale) {
//        if (currentScale == endScale) return;
//        currentScale = endScale;
//        Log.d("@@@", "layout scale");
//        Animation anim = new ScaleAnimation(
//                startScale, endScale, // Start and end values for the X axis scaling
//                startScale, endScale, // Start and end values for the Y axis scaling
//                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
//                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
//        anim.setFillAfter(true); // Needed to keep the result of the animation
//        anim.setDuration(100);
//        v.startAnimation(anim);
//    }
//
//    public void scaleView2(View v, float startScale, float endScale) {
//        if (currentScale2 == endScale) return;
//        currentScale2 = endScale;
//        Log.d("@@@", "videoview scale");
//        Animation anim = new ScaleAnimation(
//                startScale, endScale, // Start and end values for the X axis scaling
//                startScale, endScale, // Start and end values for the Y axis scaling
//                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
//                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
//        anim.setFillAfter(true); // Needed to keep the result of the animation
//        anim.setDuration(100);
//        v.startAnimation(anim);
//    }

    public interface OnModeChangeListener {
        void onModeChange(int status);
    }
}
