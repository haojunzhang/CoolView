package idv.haojun.recyclerviewdividersample;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable drawable;
    private int marginLeft;
    private int marginRight;

    public MItemDecoration(Context context) {
        this(context, 0, 0, R.drawable.divider_default);
    }

    public MItemDecoration(Context context, int marginLeftDp, int marginRightDp) {
        this(context, marginLeftDp, marginRightDp, R.drawable.divider_default);
    }

    public MItemDecoration(Context context, int marginLeftDp, int marginRightDp, int dividerId) {
        this.drawable = ContextCompat.getDrawable(context, dividerId);
        this.marginLeft = (int) (context.getResources().getDisplayMetrics().density * marginLeftDp);
        this.marginRight = (int) (context.getResources().getDisplayMetrics().density * marginRightDp);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + marginLeft;
        int right = parent.getWidth() - parent.getPaddingRight() - marginRight;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + drawable.getIntrinsicHeight();

            drawable.setBounds(left, top, right, bottom);
            drawable.draw(c);
        }
    }
}
