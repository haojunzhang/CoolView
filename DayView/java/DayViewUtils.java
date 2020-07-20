package com.example.dragdropview;

import android.view.View;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Locale;

public class DayViewUtils {
    public static int FIFTEEN_MINUTE_MARGIN = 50;
    public static final int TOTAL_LAYOUT_HEIGHT = FIFTEEN_MINUTE_MARGIN * 4 * 24;
    public static final int START_MARGIN_TOP_LIMIT = 0;
    public static final int END_MARGIN_TOP_LIMIT = FIFTEEN_MINUTE_MARGIN * 4 * 24;


    public static String getTimeByMargin(int margin) {
        // 0 > "00:00"
        // 50 > "00:15"
        // 4800 > "24:00"
        int hourMargin = FIFTEEN_MINUTE_MARGIN * 4;
        int hour = margin / hourMargin;
        int minute = ((margin % hourMargin) / FIFTEEN_MINUTE_MARGIN) * 15;
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    public static int getMarginByTime(String time) {
        // "00:00" > 0
        // "00:15" > 50
        // "24:00" > 4800
        String[] arr = time.split(":");
        int hour = Integer.parseInt(arr[0]);
        int minute = Integer.parseInt(arr[1]);
        return (hour * 4 + (minute / 15)) * FIFTEEN_MINUTE_MARGIN;
    }

    public static boolean isOverlapping(List<View> viewList) {
        // 根據View的marginTop和height判斷是否有任何一組View重疊
        // [[1000, 1100], [1200, 1300]]
        int[][] arr = new int[viewList.size()][2];
        for (int i = 0; i < viewList.size(); i++) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewList.get(i).getLayoutParams();
            arr[i] = new int[]{params.topMargin, params.topMargin + params.height};
        }

        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                // 以上面範例,
                // 如完全一樣或1200在1000,1100之間或如1300在1000,1100之間
                // 則重疊
                if ((arr[i][0] == arr[j][0] && arr[i][1] == arr[j][1]) ||
                        isBetween(arr[j][0], arr[i][0], arr[i][1]) ||
                        isBetween(arr[j][1], arr[i][0], arr[i][1])) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isBetween(int value, int from, int to) {
        return from < value && value < to;
    }
}
