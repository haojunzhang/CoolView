package idv.haojun.floatingplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public class DraggableLayout extends RelativeLayout {

    // mode
    public static final int MODE_MAXIMIZE = 0;
    public static final int MODE_MINIMIZE = 1;
    public static final int MODE_FULLSCREEN = 2;
    private int mode = MODE_MAXIMIZE;

    // bound
    public static int TOP_MARGIN_LIMIT_FLOATING_MODE;
    public static int LEFT_MARGIN_LIMIT_FLOATING_MODE;
    public static int RIGHT_MARGIN_LIMIT_FLOATING_MODE;
    public static int BOTTOM_MARGIN_LIMIT_FLOATING_MODE;

    // scale
    private float floatingScale = 0.75f;

    // width & height
    private int screenWidth;
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
//    private int currentHeight, currentWidth;
    private boolean isResizing = false;

    // listener
    private DraggableListener draggableListener;

    // touch listener
    private float downX, downY;
    private int downTopMargin, downLeftMargin, downBottomMargin, downRightMargin;
    private float moveX, moveY;
    private int moveTopMargin, moveLeftMargin, moveBottomMargin, moveRightMargin;
    private boolean touchable = true;

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

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        statusBarHeight = EnvHelper.getStatusBarHeight(context);
        navigationBarHeight = EnvHelper.getNavigationBarHeight(context);
        layoutWidth = context.getResources().getDisplayMetrics().widthPixels;
        layoutHeight = (int) (context.getResources().getDisplayMetrics().density * 200);
        floatingLayoutWidth = (int) (layoutWidth * floatingScale);
        floatingLayoutHeight = (int) (layoutHeight * floatingScale);

//        currentHeight = layoutHeight;
//        currentWidth = layoutWidth;


        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (!touchable) return false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        layoutParams = (LayoutParams) getLayoutParams();
                        downX = event.getRawX();
                        downY = event.getRawY();

                        downTopMargin = layoutParams.topMargin;
                        downLeftMargin = layoutParams.leftMargin;
                        downBottomMargin = layoutParams.bottomMargin;
                        downRightMargin = layoutParams.rightMargin;

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
                            maximize();
                        } else {
                            minimize();
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
                return false;
            }
        });
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

    public void maximize() {
        if (mode == MODE_MAXIMIZE) return;

        // 1.variable change
        mode = MODE_MAXIMIZE;
        touchable = true;

        // 2.animation
        if (layoutParams == null) layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = layoutHeight;
        resizeView(
                floatingLayoutHeight, layoutHeight,
                floatingLayoutWidth, layoutWidth,
                layoutParams.topMargin, 0,
                layoutParams.leftMargin, 0,
                layoutParams.rightMargin, 0
        );

        // 3.callback listener
        if (draggableListener != null) draggableListener.onMaximized();
    }

    private void minimize() {
        if (mode == MODE_MINIMIZE) return;

        // 1.variable change
        mode = MODE_MINIMIZE;
        touchable = true;

        // 2.animation
        if (layoutParams == null) layoutParams = (LayoutParams) getLayoutParams();
        resizeView(
                layoutHeight, floatingLayoutHeight,
                layoutWidth, floatingLayoutWidth,
                0, (int) (moveY - floatingLayoutHeight / 2),
                0, (int) (moveX - floatingLayoutWidth / 2),
                0, (int) (layoutWidth - moveX - floatingLayoutWidth / 2)
        );


        // 3.callback listener
        if (draggableListener != null) draggableListener.onMinimized();
    }

    public void fullscreenMode() {
        if (mode == MODE_FULLSCREEN) return;
        // 1.variable change
        mode = MODE_FULLSCREEN;
        touchable = false;

        // 2.animation
//        resizeView(
//                layoutHeight, floatingLayoutHeight,
//                layoutWidth, floatingLayoutWidth,
//                0, (int) (moveY - floatingLayoutHeight / 2),
//                0, (int) (moveX - floatingLayoutWidth / 2),
//                0, (int) (layoutWidth - moveX - floatingLayoutWidth / 2)
//        );
        if (layoutParams == null)
            layoutParams = new LayoutParams(screenHeight + navigationBarHeight, screenWidth);
        layoutParams.width = screenHeight + navigationBarHeight;
        layoutParams.height = screenWidth;
        layoutParams.topMargin = 0;
        layoutParams.leftMargin = 0;
        layoutParams.bottomMargin = 0;
        layoutParams.rightMargin = 0;
        setLayoutParams(layoutParams);

        // 3.callback listener
        if (draggableListener != null) draggableListener.onFullscreen();
    }

    private void resizeView(
            int startHeight, int endHeight,
            int startWidth, int endWidth,
            int startTopMargin, int endTopMargin,
            int startLeftMargin, int endLeftMargin,
            int startRightMargin, int endRightMargin
    ) {
//        if (currentHeight == endHeight && currentWidth == endWidth) return;
//        currentHeight = endHeight;
//        currentWidth = endWidth;

        // floating
        if (startHeight > endHeight) {
            if (isLeftOutBound(endLeftMargin)) {
                endLeftMargin = 0;
                endRightMargin = layoutWidth - floatingLayoutWidth;
            } else if (isRightOutBound(endRightMargin)) {
                endLeftMargin = layoutWidth - floatingLayoutWidth;
                endRightMargin = 0;
            }
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

    public void setDraggableListener(DraggableListener draggableListener) {
        this.draggableListener = draggableListener;
    }

    public interface DraggableListener {
        void onMaximized();

        void onMinimized();

        void onFullscreen();

        void onClosed();
    }
}
