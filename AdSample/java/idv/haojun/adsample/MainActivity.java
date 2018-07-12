package idv.haojun.adsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        

        findViewById(R.id.bt_banner).setOnClickListener(this);
        findViewById(R.id.bt_interstitial).setOnClickListener(this);
        findViewById(R.id.bt_reward_video).setOnClickListener(this);


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
        }
    }
}
