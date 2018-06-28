package idv.haojun.floatingvideoviewsample;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.FloatMath;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class VideoService extends Service {

    private static final int MODE_NONE = 0;
    private static final int MODE_TOUCH = 1;

    private int mTouchMode = MODE_NONE;

    private boolean isSingleTap;

    private float START_X;
    private float START_Y;

    private int PREV_X;
    private int PREV_Y;

    private int mWidth;
    private int mHeight;

    private int videoWidth;
    private int videoHeight;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams windowParams;

    private Button btnClose;

    private RelativeLayout mOverlay;
    private RelativeLayout layoutVideo;

    private VideoView videoViewPlayer;

    private RemoteCallbackList<IMessageServiceCallback> callbackList =
            new RemoteCallbackList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return serviceStub;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(startId, new Notification());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finishVideo();
        stopForeground(true);
    }

    private void finishVideo() {
        mWindowManager.removeView(mOverlay);
    }

    private void initWindowManager() {
        windowParams = new WindowManager.LayoutParams(
                mWidth * 3 / 4,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST, 
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        windowParams.gravity = Gravity.CENTER;
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
    }

    private void initLayout() {

        LayoutInflater mInflater = LayoutInflater.from(this);

        mOverlay = (RelativeLayout) mInflater.inflate(R.layout.activity_video_window, null);
        layoutVideo = mOverlay.findViewById(R.id.layoutVideo);
        btnClose = mOverlay.findViewById(R.id.btnClose);
        videoViewPlayer = mOverlay.findViewById(R.id.videoViewPlayer);

        mWindowManager.addView(mOverlay, windowParams);

        btnClose.setOnClickListener(mOnClickListener);
        videoViewPlayer.setOnTouchListener(mOnTouchListener);
        videoViewPlayer.setOnPreparedListener(mNativePreparedListener);

    }

    private void initData() {
        videoDataInit();
    }

    private void videoDataInit() {
        String tempVideoURL = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
        Uri uri = Uri.parse(tempVideoURL);
        videoViewPlayer.setVideoURI(uri);
    }
    
    public IMessageService.Stub serviceStub = new IMessageService.Stub() {

        @Override
        public void sendMessage(String message, int width, int height) throws RemoteException {

            mWidth = width;
            mHeight = height;

            initWindowManager();
            initLayout();
            initData();
        }

        @Override
        public void registerCallback(IMessageServiceCallback callback) throws RemoteException {
            callbackList.register(callback);
        }

        @Override
        public void unregisterCallback(IMessageServiceCallback callback) throws RemoteException {
            callbackList.unregister(callback);
        }

    };
    
    private OnPreparedListener mNativePreparedListener = new OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {

            layoutVideo.setVisibility(View.VISIBLE);

            videoViewPlayer.start();

            videoWidth = videoViewPlayer.getWidth();
            videoHeight = videoViewPlayer.getHeight();

            int cnt = callbackList.beginBroadcast();
            for (int i = 0; i < cnt; i++) {
                try {
                    callbackList.getBroadcastItem(i).finishAppCallback();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            callbackList.finishBroadcast();
        }
    };
    
    private Button.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnClose:

                    int cnt = callbackList.beginBroadcast();
                    for (int i = 0; i < cnt; i++) {
                        try {
                            callbackList.getBroadcastItem(i).finishCallback();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    callbackList.finishBroadcast();

                    break;
            }
        }
    };

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    isSingleTap = true;
                    mTouchMode = MODE_TOUCH;
                    btnClose.setVisibility(View.INVISIBLE);

                    START_X = event.getRawX();
                    START_Y = event.getRawY();
                    PREV_X = windowParams.x;
                    PREV_Y = windowParams.y;

                    break;

                case MotionEvent.ACTION_MOVE:    

                    if (mTouchMode == MODE_TOUCH) {
                        int x = (int) (event.getRawX() - START_X);
                        int y = (int) (event.getRawY() - START_Y);

                        windowParams.x = PREV_X + x;
                        windowParams.y = PREV_Y + y;

                        mWindowManager.updateViewLayout(mOverlay, windowParams);
                    }
                    break;

                case MotionEvent.ACTION_UP:

                    btnClose.setVisibility(View.VISIBLE);

                    //싱글탭
                    if ((PREV_X - 0.1 < windowParams.x && windowParams.x < PREV_X + 0.1) && isSingleTap) {

                        int cnt = callbackList.beginBroadcast();
                        for (int i = 0; i < cnt; i++) {
                            try {
                                callbackList.getBroadcastItem(i).startAppCallback();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        callbackList.finishBroadcast();
                    }

                    mWindowManager.updateViewLayout(mOverlay, windowParams);
                    btnClose.setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        }
    };

}