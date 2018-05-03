package idv.haojun.floatingplayer;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DraggableLayout.DraggableListener {

    //
    private DraggableLayout draggableLayout;
    private VideoViewCustom videoView;
    private ImageView iv_play, iv_fullscreen;
    private boolean isFullscreen;

    //
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        draggableLayout = (DraggableLayout) findViewById(R.id.draggable_layout);
        draggableLayout.setDraggableListener(this);
        draggableLayout.setOnClickListener(this);
        videoView = (VideoViewCustom) findViewById(R.id.video_view);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_fullscreen = (ImageView) findViewById(R.id.iv_fullscreen);
        iv_play.setOnClickListener(this);
        iv_fullscreen.setOnClickListener(this);

        frameLayout = (FrameLayout) findViewById(R.id.fl);

        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            playerVideo();
        }
    }

    private void toggleVideoPlay() {
        if (videoView.isPlaying()) {
            videoView.pause();
            iv_play.setImageResource(R.drawable.ic_play_circle_filled_blue_80dp);
        } else {
            videoView.start();
            iv_play.setImageResource(R.drawable.ic_pause_circle_filled_blue_80dp);
        }
    }

    private void toggleVideoControllerVisibility() {
        if (iv_play.getVisibility() == View.VISIBLE) {
            iv_play.setVisibility(View.GONE);
            iv_fullscreen.setVisibility(View.GONE);
        } else {
            iv_play.setVisibility(View.VISIBLE);
            iv_fullscreen.setVisibility(View.VISIBLE);
        }
    }

    private void playerVideo() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sample.mp4";
        File file = new File(path);
        videoView.setVideoPath(file.getAbsolutePath());
        videoView.start();
    }

    private void toggleScreenMode() {

        // 1. show/hide system ui
        // 2. portrait/landscape

        if (isFullscreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            iv_fullscreen.setImageResource(R.drawable.ic_fullscreen_blue_36dp);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            iv_fullscreen.setImageResource(R.drawable.ic_fullscreen_exit_blue_36dp);
        }
        isFullscreen = !isFullscreen;
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.draggable_layout:
                toggleVideoControllerVisibility();
                break;
            case R.id.iv_play:
                toggleVideoPlay();
                break;
            case R.id.iv_fullscreen:
                toggleScreenMode();
                break;
        }
    }

    @Override
    public void onMaximized() {
        videoView.topMode();

        // animate content
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.topMargin, EnvHelper.dpToPx(this, 200));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.topMargin = (Integer) valueAnimator.getAnimatedValue();
                frameLayout.requestLayout();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        animator.setDuration(300);
        animator.start();
    }

    @Override
    public void onMinimized() {
        videoView.floatingMode();

        // animate content
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.topMargin, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.topMargin = (Integer) valueAnimator.getAnimatedValue();
                frameLayout.requestLayout();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        animator.setDuration(300);
        animator.start();
    }

    @Override
    public void onFullscreen() {
        videoView.fullscreenMode();

    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().getDecorView().invalidate();
//            L.d("config-LANDSCAPE");
            hideSystemUI();
            draggableLayout.fullscreenMode();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
//            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().setAttributes(attrs);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            L.d("config-PORTRAIT");
            showSystemUI();
            draggableLayout.maximize();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        playerVideo();
    }


}
