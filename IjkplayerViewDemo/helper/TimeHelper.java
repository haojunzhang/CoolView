package idv.haojun.aplayer.helper;

import java.util.Locale;

public class TimeHelper {
    public static String getVideoFormat(int second) {
        int min = second / 60;
        int sec = second % 60;
        return String.format(Locale.getDefault(),"%02d:%02d", min, sec);
    }
}
