package idv.haojun.adsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class InterstitialActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvAdState;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        tvAdState = findViewById(R.id.tv_ad_state);
        findViewById(R.id.bt_show).setOnClickListener(this);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.test_interstitial_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                tvAdState.setText("onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                tvAdState.setText("onAdFailedToLoad:" + errorCode);
            }

            @Override
            public void onAdOpened() {
                tvAdState.setText("onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                tvAdState.setText("onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                tvAdState.setText("onAdClosed");
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_show:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                break;
        }
    }
}
