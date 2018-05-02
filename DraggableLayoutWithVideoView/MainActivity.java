package idv.haojun.floatingplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements DraggableLayout.OnModeChangeListener, View.OnClickListener {

    private DraggableLayout draggableLayout;
    private VideoViewCustom videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        draggableLayout = (DraggableLayout) findViewById(R.id.draggable_layout);
        draggableLayout.setOnModeChangeListener(this);

        videoView = (VideoViewCustom) findViewById(R.id.video_view);

        findViewById(R.id.bt).setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            playerVideo();
        }
    }

    private void playerVideo() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sample.mp4";
        File file = new File(path);
        videoView.setVideoPath(file.getAbsolutePath());
        videoView.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        playerVideo();
    }

    @Override
    public void onModeChange(int status) {
        switch (status) {
            case DraggableLayout.STATUS_TOP:
                videoView.topMode();
                break;
            case DraggableLayout.STATUS_FLOATING:
                videoView.floatingMode();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (videoView.isPlaying()) {
            videoView.pause();
        } else {
            videoView.start();
        }
    }
}
