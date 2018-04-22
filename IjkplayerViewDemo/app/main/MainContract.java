package idv.haojun.aplayer.app.main;

import android.net.Uri;

import java.util.List;

import idv.haojun.aplayer.model.AVideo;

public interface MainContract {
    interface View{
        void exit();

        void displayVideos(List<AVideo> videos);
    }
    interface Presenter{
        void getLocalVideosWithPermission();

        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    }
}
