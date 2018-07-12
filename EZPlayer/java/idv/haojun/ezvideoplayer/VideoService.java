package idv.haojun.ezvideoplayer;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class VideoService extends Service {

    // window manager
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams windowParams;

    // video view
    private View mView;
    private VideoView mVideoView;

    // extra
    private VideoItem mVideoItem;
    private int currentPosition;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.d("VideoService-onCreate");

        initWindowManager();
        initLayout();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.d("VideoService-onStartCommand");

        mVideoItem = intent.getParcelableExtra("videoItem");
        currentPosition = intent.getIntExtra("currentPosition", 0);

        startForeground(startId, new Notification());

        initVideoData();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d("VideoService-onDestroy");

        stopForeground(true);

        if (mView != null) {
            mWindowManager.removeView(mView);
        }
    }

    private void initWindowManager() {
        int mWidth = getResources().getDisplayMetrics().heightPixels;

        windowParams = new WindowManager.LayoutParams(
                mWidth * 3 / 4,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        windowParams.gravity = Gravity.CENTER;
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }

    private void initLayout() {
        LayoutInflater mInflater = LayoutInflater.from(this);

        mView = mInflater.inflate(R.layout.service_video_window, null);
        mVideoView = mView.findViewById(R.id.window_video_view);
        mView.findViewById(R.id.root_video_window).setOnTouchListener(mOnTouchListener);
        mView.findViewById(R.id.window_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
        mWindowManager.addView(mView, windowParams);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
                if (currentPosition != 0) {
                    mVideoView.seekTo(currentPosition);
                }
            }
        });
    }

    private void initVideoData() {
        if (mVideoItem != null) {
            mVideoView.setVideoPath(mVideoItem.getPath());
        }
    }

    private void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("videoItem", mVideoItem);
        intent.putExtra("currentPosition", mVideoView.getCurrentPosition());
        startActivity(intent);
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        public static final long CLICK_INTERVAL = 200;
        private long actionDownTime;

        private float START_X;
        private float START_Y;

        private int PREV_X;
        private int PREV_Y;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    actionDownTime = System.currentTimeMillis();

                    START_X = event.getRawX();
                    START_Y = event.getRawY();
                    PREV_X = windowParams.x;
                    PREV_Y = windowParams.y;

                    break;

                case MotionEvent.ACTION_MOVE:

                    int x = (int) (event.getRawX() - START_X);
                    int y = (int) (event.getRawY() - START_Y);

                    windowParams.x = PREV_X + x;
                    windowParams.y = PREV_Y + y;

                    mWindowManager.updateViewLayout(mView, windowParams);
                    break;

                case MotionEvent.ACTION_UP:
                    if (System.currentTimeMillis() - actionDownTime < CLICK_INTERVAL) {
                        // click
                        openMain();
                        stopSelf();
                    }
                    break;
            }
            return true;
        }
    };
}