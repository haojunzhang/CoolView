package idv.haojun.adsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class RewardVideoActivity extends AppCompatActivity implements RewardedVideoAdListener, View.OnClickListener {

    private TextView tvAdState;

    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_video);

        tvAdState = findViewById(R.id.tv_ad_state);
        findViewById(R.id.bt_show).setOnClickListener(this);

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.test_reward_video_id),
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        tvAdState.setText("onRewardedVideoAdLoaded");
    }

    @Override
    public void onRewardedVideoAdOpened() {
        tvAdState.setText("onRewardedVideoAdOpened");

    }

    @Override
    public void onRewardedVideoStarted() {
        tvAdState.setText("onRewardedVideoStarted");

    }

    @Override
    public void onRewardedVideoAdClosed() {
        tvAdState.setText("onRewardedVideoAdClosed");
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem reward) {
        tvAdState.setText("onRewarded! currency: " + reward.getType() + "  amount: " +
                reward.getAmount());

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        tvAdState.setText("onRewardedVideoAdLeftApplication");

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        tvAdState.setText("onRewardedVideoAdLoaded:" + i);

    }

    @Override
    public void onRewardedVideoCompleted() {
        tvAdState.setText("onRewardedVideoCompleted");

    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_show:
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
                break;
        }
    }
}
