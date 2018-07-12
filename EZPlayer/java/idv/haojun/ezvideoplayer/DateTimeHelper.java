package idv.haojun.ezvideoplayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeHelper {

    public static int mmss2Second(String mmss) {
        String[] arr = mmss.split(":");
        return Integer.parseInt(arr[0]) * 60 + Integer.parseInt(arr[1]);
    }

    public static String second2mmss(long second) {
        return String.format(Locale.getDefault(), "%02d:%02d", ((second) / 60), (second) % 60);
    }
}
