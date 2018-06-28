package idv.haojun.floatingvideoviewsample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.DisplayMetrics;

import android.view.View;

import android.view.View.OnClickListener;

import android.widget.Button;


public class MainActivity extends Activity {

    private boolean isBind;

    private int deviceWidth;
    private int deviceHeight;

    private Intent intent;
    private Button btnOnAir;
    private Button btnOffAir;

    private IMessageService messageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        initData();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        isBind = false;
    }

    private void initLayout() {

        btnOnAir = findViewById(R.id.btnOnAir);
        btnOffAir = findViewById(R.id.btnOffAir);

        btnOnAir.setOnClickListener(mOnClickListener);
        btnOffAir.setOnClickListener(mOnClickListener);
    }

    private void initData() {


        getResolation();
    }

    private void finishApp() {
        finish();
    }

    private void getResolation() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;
        deviceHeight = displayMetrics.heightPixels;
    }

    private void startService() {
        intent = new Intent(MainActivity.this, VideoService.class);
        startService(intent);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private void finishService() {
        if (intent != null) {
            if (isBind) {
                unbindService(conn);
            }
            stopService(intent);
            intent = null;
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messageService = IMessageService.Stub.asInterface(service);

            try {
                messageService.registerCallback(callback);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            try {
                messageService.sendMessage("12345", deviceWidth, deviceHeight);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private IMessageServiceCallback callback = new IMessageServiceCallback.Stub() {
        @Override
        public void finishCallback() throws RemoteException {

            finishService();
        }

        @Override
        public void startAppCallback() throws RemoteException {

            finishService();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        public void finishAppCallback() throws RemoteException {
            finishApp();
        }
    };

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnOnAir:
                    startService();
                    break;
                case R.id.btnOffAir:
                    finishService();
                    break;
            }
        }
    };
}