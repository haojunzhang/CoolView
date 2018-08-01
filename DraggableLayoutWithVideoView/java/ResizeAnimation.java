package idv.haojun.floatingplayer;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public class ResizeAnimation extends Animation {

    private int startHeight;
    private int deltaHeight;

    private int startWidth;
    private int deltaWidth;

    private int startTopMargin;
    private int deltaTopMargin;

    private int startLeftMargin;
    private int deltaLeftMargin;

    private int startRightMargin;
    private int deltaRightMargin;

    private View view;

    public ResizeAnimation(
            View v,
            int startHeight, int endHeight,
            int startWidth, int endWidth,
            int startTopMargin, int endTopMargin,
            int startLeftMargin, int endLeftMargin,
            int startRightMargin, int endRightMargin
    ) {
        this.view = v;

        this.startHeight = startHeight;
        deltaHeight = endHeight - this.startHeight;

        this.startWidth = startWidth;
        deltaWidth = endWidth - this.startWidth;

        this.startLeftMargin = startLeftMargin;
        deltaLeftMargin = endLeftMargin - this.startLeftMargin;

        this.startTopMargin = startTopMargin;
        deltaTopMargin = endTopMargin - this.startTopMargin;

        this.startRightMargin = startRightMargin;
        deltaRightMargin = endRightMargin - this.startRightMargin;
    }

    private float lastInterpolatedTime;

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (interpolatedTime == lastInterpolatedTime) return;
        lastInterpolatedTime = interpolatedTime;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.height = (int) (startHeight + deltaHeight * interpolatedTime);
        params.width = (int) (startWidth + deltaWidth * interpolatedTime);
        if (deltaTopMargin != 0) {
            params.topMargin = (int) (startTopMargin + deltaTopMargin * interpolatedTime);
        }
        if (deltaLeftMargin != 0) {
            params.leftMargin = (int) (startLeftMargin + deltaLeftMargin * interpolatedTime);
        }
        if (deltaRightMargin != 0) {
            params.rightMargin = (int) (startRightMargin + deltaRightMargin * interpolatedTime);
        }
        view.requestLayout();
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}