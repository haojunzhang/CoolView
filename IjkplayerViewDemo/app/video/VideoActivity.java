package idv.haojun.aplayer.app.video;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import idv.haojun.aplayer.R;
import idv.haojun.aplayer.base.BaseActivity;
import idv.haojun.aplayer.ijkplayer.AMediaController;
import idv.haojun.aplayer.ijkplayer.IjkVideoView;
import idv.haojun.aplayer.model.AVideo;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoActivity extends BaseActivity implements VideoContract.View, View.OnClickListener {

    private static final String TAG = "VideoActivity";

    private List<AVideo> videos;
    private int playingVideoPosition;

    private IjkVideoView mVideoView;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toggleImmersiveMode();
        setContentView(R.layout.activity_video);

        videos = getIntent().getParcelableArrayListExtra("videos");
        playingVideoPosition = getIntent().getIntExtra("position", 0);

        ((TextView) findViewById(R.id.tv_video_title)).setText(getPlayingVideo().getTitle());

        Toolbar toolbar = findViewById(R.id.toolbar_video);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        AMediaController mMediaController = new AMediaController(this);
        mMediaController.bindVideoContractView(this);
        mMediaController.setOnPrevClickListener(this);
        mMediaController.setOnNextClickListener(this);


        mVideoView = findViewById(R.id.ijk_video_view_video);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoPath(getPlayingVideo().getData());
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                mVideoView.start();
            }
        });
    }

    private AVideo getPlayingVideo() {
        return videos.get(playingVideoPosition);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVideoView.stopPlayback();
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    public void show() {
        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override
    public void hide() {
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void toggleImmersiveMode() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i(TAG, "Turning immersive mode mode off. ");
        } else {
            Log.i(TAG, "Turning immersive mode mode on.");
        }
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    private void playCurrentPositionVideo() {
        mVideoView.stopPlayback();
        AMediaController mMediaController = new AMediaController(this);
        mMediaController.bindVideoContractView(this);
        mMediaController.setOnPrevClickListener(this);
        mMediaController.setOnNextClickListener(this);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoPath(getPlayingVideo().getData());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_media_controller_prev:
                playingVideoPosition--;
                if (playingVideoPosition < 0) {
                    playingVideoPosition++;
                    return;
                }
                playCurrentPositionVideo();
                break;
            case R.id.rl_media_controller_next:
                playingVideoPosition++;
                if (playingVideoPosition >= videos.size()) {
                    playingVideoPosition--;
                    return;
                }
                playCurrentPositionVideo();
                break;
        }
    }
}
