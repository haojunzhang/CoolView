package idv.haojun.ezvideoplayer;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.Locale;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    // const
    public static final long PANEL_HIDE_TIME = 5000;
    public static final long VIDEO_UPDATE_DELAY = 500;
    public static final long ONCE_VIDEO_FAST_SECOND = 5000;

    // extra
    private VideoItem mVideoItem;
    private int currentPosition;

    // ui
    private TextView tvTitle;
    private VideoView mVideoView;
    private ImageView ivPlay;
    private TextView tvCurrentDuration;
    private TextView tvTotalDuration;
    private SeekBar sbProgress;

    private View panelTop;
    private View panelMid;
    private View panelBot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        hideAllSystemUI();

        // extra
        mVideoItem = getIntent().getParcelableExtra("videoItem");
        currentPosition = getIntent().getIntExtra("currentPosition", 0);

        // ui
        tvTitle = findViewById(R.id.tv_video_title);
        mVideoView = findViewById(R.id.vv_video);
        ivPlay = findViewById(R.id.iv_video_play);
        panelTop = findViewById(R.id.video_panel_top);
        panelMid = findViewById(R.id.video_panel_mid);
        panelBot = findViewById(R.id.video_panel_bottom);
        tvCurrentDuration = findViewById(R.id.tv_video_current_duration);
        tvTotalDuration = findViewById(R.id.tv_video_total_duration);
        sbProgress = findViewById(R.id.sb_video_progress);
        findViewById(R.id.root_video).setOnTouchListener(panelOnTouchListener);
        findViewById(R.id.iv_video_play).setOnClickListener(this);
        findViewById(R.id.vv_video).setOnClickListener(this);
        findViewById(R.id.iv_video_floating).setOnClickListener(this);

        initPanel();
        initVideoView();
    }

    private void initPanel() {
        tvTitle.setText(mVideoItem.getName());

        tvCurrentDuration.setText("00:00");

        tvTotalDuration.setText(DateTimeHelper.second2mmss(mVideoItem.getDuration() / 1000));

        sbProgress.setProgress(0);
        sbProgress.setMax((int) (mVideoItem.getDuration() / 1000));
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mVideoView.seekTo(progress * 1000);
                    updateCurrentDurationText();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initVideoView() {
        mVideoView.setVideoPath(mVideoItem.getPath());
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
                if (currentPosition != 0) {
                    mVideoView.seekTo(currentPosition);
                }
                videoUiUpdater.sendEmptyMessage(0);
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.seekTo(0);
                updateProgressPanel();
                ivPlay.setImageResource(R.drawable.ic_video_play);
            }
        });
    }

    private void updateCurrentDurationText() {
        int second = mVideoView.getCurrentPosition() / 1000;
        tvCurrentDuration.setText(DateTimeHelper.second2mmss(second));
    }

    private void updateProgressPanel() {
        int second = mVideoView.getCurrentPosition() / 1000;
        tvCurrentDuration.setText(DateTimeHelper.second2mmss(second));
        sbProgress.setProgress(second);
    }

    private void playVideo() {

        if (mVideoView.isPlaying()) {
            // do pause
            mVideoView.pause();

            ivPlay.setImageResource(R.drawable.ic_video_play);
        } else {
            // do play
            mVideoView.start();
            videoUiUpdater.sendEmptyMessage(0);

            ivPlay.setImageResource(R.drawable.ic_video_pause);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler videoUiUpdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mVideoView == null || !mVideoView.isPlaying()) return;

            updateProgressPanel();

            videoUiUpdater.sendEmptyMessageDelayed(0, VIDEO_UPDATE_DELAY);
        }
    };

    private void hideAllSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void showAllSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private AsyncTask hidePanelTask;

    @SuppressLint("StaticFieldLeak")
    private void executeHidePanelTask() {
        hidePanelTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(PANEL_HIDE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isCancelled()) return;
                setPanelVisibility(View.GONE);
                hideAllSystemUI();
            }
        }.execute();
    }

    private void cancelHidePanelTask() {
        if (hidePanelTask != null) {
            hidePanelTask.cancel(true);
            hidePanelTask = null;
        }
    }

    private void setPanelVisibility(int visibility) {
        panelTop.setVisibility(visibility);
        panelMid.setVisibility(visibility);
        panelBot.setVisibility(visibility);
    }

    private boolean isPanelVisible() {
        return panelTop.getVisibility() == View.VISIBLE;
    }

    private void togglePanelVisibility() {
        if (isPanelVisible()) {
            setPanelVisibility(View.GONE);
            cancelHidePanelTask();
        } else {
            setPanelVisibility(View.VISIBLE);
            executeHidePanelTask();
        }
    }

    private void videoFastRight() {
        mVideoView.seekTo((int) (mVideoView.getCurrentPosition() + ONCE_VIDEO_FAST_SECOND));
    }

    private void videoFastLeft() {
        mVideoView.seekTo((int) (mVideoView.getCurrentPosition() - ONCE_VIDEO_FAST_SECOND));
    }

    private View.OnTouchListener panelOnTouchListener = new View.OnTouchListener() {

        private static final int TOUCH_AREA_NONE = -1;
        private static final int TOUCH_AREA_LEFT = 0;
        private static final int TOUCH_AREA_RIGHT = 1;

        private static final long CLICK_INTERVAL = 200;
        private static final long SPEED_CLICK_INTERVAL = 200;

        private long actionDownTime;
        private long lastClickTime;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float rawX = event.getRawX();
            float rawY = event.getRawY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    actionDownTime = System.currentTimeMillis();

                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:
                    long actionUpTime = System.currentTimeMillis();
//                    L.d("actionUpTime:" + actionUpTime);
                    if (actionUpTime - actionDownTime < CLICK_INTERVAL) {
                        // click
                        if (actionUpTime - lastClickTime < SPEED_CLICK_INTERVAL) {
                            // speed click
                            speedClick(v, rawX, rawY);
                        } else {
                            // normal click
                            normalClick();
                        }

                        lastClickTime = actionUpTime;
                    }
                    break;
            }


            return true;
        }

        private void normalClick() {
            togglePanelVisibility();
        }

        private void speedClick(View v, float x, float y) {
            int touchArea = getTouchArea(v, x, y);
            switch (touchArea) {
                case TOUCH_AREA_LEFT:
                    videoFastLeft();
                    break;
                case TOUCH_AREA_RIGHT:
                    videoFastRight();
                    break;
            }
        }

        private int getTouchArea(View v, float x, float y) {
            int width = v.getWidth();
            int height = v.getHeight();

            // right area
            int rightXFrom = width - width / 4;
            int rightXTo = width;
            int rightYFrom = height / 4;
            int rightYTo = height - height / 4;
            if (rightXFrom <= x && x <= rightXTo && rightYFrom <= y && y <= rightYTo) {
                return TOUCH_AREA_RIGHT;
            }

            // left area
            int leftXFrom = 0;
            int leftXTo = width / 4;
            int leftYFrom = height / 4;
            int leftYTo = height - height / 4;
            if (leftXFrom <= x && x <= leftXTo && leftYFrom <= y && y <= leftYTo) {
                return TOUCH_AREA_LEFT;
            }

            return TOUCH_AREA_NONE;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_video_play:
                playVideo();
                break;
            case R.id.iv_video_floating:
                // start service
                Intent intent = new Intent(this, VideoService.class);
                intent.putExtra("videoItem", mVideoItem);
                intent.putExtra("currentPosition", currentPosition);
                startService(intent);

                // finish activity
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
