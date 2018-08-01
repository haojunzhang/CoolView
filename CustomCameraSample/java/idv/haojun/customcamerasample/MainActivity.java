package idv.haojun.customcamerasample;

import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };
    // ui
    private FrameLayout flPreview;
    private CameraPreview mCameraPreview;
    private ImageView ivFacingToggle;
    private ImageView ivCapture;
    private ImageView ivFlashToggle;
    //
    private OrientationEventListener orientationEventListener;

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flPreview = findViewById(R.id.flPreview);
        ivFacingToggle = findViewById(R.id.ivFacingToggle);
        ivCapture = findViewById(R.id.ivCapture);
        ivFlashToggle = findViewById(R.id.ivFlashToggle);
        ivFacingToggle.setOnClickListener(this);
        ivCapture.setOnClickListener(this);
        ivFlashToggle.setOnClickListener(this);

        mCameraPreview = new CameraPreview(this);
        flPreview.addView(mCameraPreview);

        initOrientationListener();
        hideStatusBar();
    }

    private void initOrientationListener() {
        orientationEventListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {
            int lastOrientation;

            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return;
                }

                if (orientation > 350 || orientation < 10) {
                    orientation = 0;
                } else if (orientation > 80 && orientation < 100) {
                    orientation = 90;
                } else if (orientation > 170 && orientation < 190) {
                    orientation = 180;
                } else if (orientation > 260 && orientation < 280) {
                    orientation = 270;
                } else {
                    return;
                }
                if (orientation == lastOrientation) return;
                lastOrientation = orientation;
                ivFacingToggle.setRotation(270 - lastOrientation);
                ivCapture.setRotation(270 - lastOrientation);
                ivFlashToggle.setRotation(270 - lastOrientation);
            }
        };
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivFacingToggle:
                mCameraPreview.toggleCameraFacing(this);
                if (mCameraPreview.isBackCamera()) {
                    ivFacingToggle.setImageResource(R.drawable.ic_camera_rear);
                } else {
                    ivFacingToggle.setImageResource(R.drawable.ic_camera_front);
                }
                break;
            case R.id.ivCapture:
                mCameraPreview.takePicture();
                break;
            case R.id.ivFlashToggle:
                mCameraPreview.toggleFlashMode();
                if (mCameraPreview.getFlashMode().equals(Camera.Parameters.FLASH_MODE_ON)) {
                    ivFlashToggle.setImageResource(R.drawable.ic_flash_on);
                } else {
                    ivFlashToggle.setImageResource(R.drawable.ic_flash_off);
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orientationEventListener != null) {
            orientationEventListener.enable();
        }
    }
}