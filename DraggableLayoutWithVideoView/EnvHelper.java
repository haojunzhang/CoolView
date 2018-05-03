package idv.haojun.floatingplayer;

import android.content.Context;
import android.content.res.Resources;

public class EnvHelper {

    public static float getDensity(Context context){
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPx(Context context, int dp){
        return (int) (dp * getDensity(context));
    }

    public static int getWidthPixels(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeightPixels(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
