package idv.haojun.ezvideoplayer;

import java.text.DecimalFormat;

public class FileHelper {
    private static final DecimalFormat format = new DecimalFormat("#");
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;

    public static String getFileSize(long file) {


        final double length = Double.parseDouble(String.valueOf(file));

        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }
}
