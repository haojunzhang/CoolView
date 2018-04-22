package idv.haojun.aplayer.app.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import idv.haojun.aplayer.R;
import idv.haojun.aplayer.model.AVideo;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private Context context;

    MainPresenter(MainContract.View mView) {
        this.mView = mView;
        this.context = (Context) mView;
    }

    @Override
    public void getLocalVideosWithPermission() {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE

        };

        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission((Activity) mView, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mView, PERMISSIONS, 0);
                return;
            }
        }

        getLocalVideos();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                mView.exit();
                Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        getLocalVideos();
    }

    private void getLocalVideos() {
        List<AVideo> videos = new ArrayList<>();
        String[] projection = {
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.TITLE,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.DATA
        };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                videos.add(new AVideo(
                        cursor.getLong(0),
                        cursor.getString(1),
                        (int) (cursor.getLong(2) / 1000),
                        cursor.getString(3)
                ));
            }
            cursor.close();
            mView.displayVideos(videos);
        }
    }
}
