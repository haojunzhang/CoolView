package idv.haojun.adsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        

        findViewById(R.id.bt_banner).setOnClickListener(this);
        findViewById(R.id.bt_interstitial).setOnClickListener(this);
        findViewById(R.id.bt_reward_video).setOnClickListener(this);
        findViewById(R.id.bt_native_ad).setOnClickListener(this);
        findViewById(R.id.bt_recyclerview).setOnClickListener(this);

        findViewById(R.id.bt_dc_banner).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_banner:
                startActivity(new Intent(this, BannerActivity.class));
                break;
            case R.id.bt_interstitial:
                startActivity(new Intent(this, InterstitialActivity.class));
                break;
            case R.id.bt_reward_video:
                startActivity(new Intent(this, RewardVideoActivity.class));
                break;
            case R.id.bt_native_ad:
                startActivity(new Intent(this, NativeAdActivity.class));
                break;
            case R.id.bt_recyclerview:
                startActivity(new Intent(this, RecyclerViewAdActivity.class));
                break;
            case R.id.bt_dc_banner:
                startActivity(new Intent(this, DCBannerActivity.class));
                break;
        }
    }
}
