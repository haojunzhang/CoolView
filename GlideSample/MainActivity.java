package idv.haojun.glidesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/d/df/Doge_homemade_meme.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_normal).setOnClickListener(this);
        findViewById(R.id.bt_center_crop).setOnClickListener(this);
        findViewById(R.id.bt_circle_crop).setOnClickListener(this);
        findViewById(R.id.bt_fade).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_normal:
                displayNormalImage();
                break;
            case R.id.bt_center_crop:
                displayCenterCropImage();
                break;
            case R.id.bt_circle_crop:
                displayCircleCropImage();
                break;
            case R.id.bt_fade:
                displayFadeImage();
                break;
        }
    }

    private void displayNormalImage() {
        ImageView ivNormal = findViewById(R.id.iv_normal);

        Glide.with(this)
                .load(IMAGE_URL)
                .into(ivNormal);
    }

    private void displayCenterCropImage() {
        ImageView ivCenterCrop = findViewById(R.id.iv_center_crop);

        Glide.with(this)
                .load(IMAGE_URL)
                .apply(new RequestOptions().centerCrop())
                .into(ivCenterCrop);
    }

    private void displayCircleCropImage() {
        ImageView ivCircleCrop = findViewById(R.id.iv_circle_crop);

        Glide.with(this)
                .load(IMAGE_URL)
                .apply(new RequestOptions().circleCrop())
                .into(ivCircleCrop);
    }

    private void displayFadeImage() {
        ImageView ivFade = findViewById(R.id.iv_fade);

        Glide.with(this)
                .load(IMAGE_URL)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivFade);
    }
}
