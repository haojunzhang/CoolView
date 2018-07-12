package idv.haojun.adsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class BannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        AdView adViewBanner1 = findViewById(R.id.adViewBanner1);
        adViewBanner1.loadAd(new AdRequest.Builder().build());

        AdView adViewBanner2 = findViewById(R.id.adViewBanner2);
        adViewBanner2.loadAd(new AdRequest.Builder().build());

        AdView adViewBanner3 = findViewById(R.id.adViewBanner3);
        adViewBanner3.loadAd(new AdRequest.Builder().build());

        AdView adViewBanner4 = findViewById(R.id.adViewBanner4);
        adViewBanner4.loadAd(new AdRequest.Builder().build());
    }
}
