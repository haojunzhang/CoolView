package idv.haojun.adsample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdActivity extends AppCompatActivity {

    private RecyclerView rv;

    private List<Object> items = new ArrayList<>();
    private List<NativeAd> ads = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_ad);


        rv = findViewById(R.id.recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new MyAdapter());

        loadItems();
        loadAds();
    }

    private void loadItems() {
        for (int i = 0; i < 20; i++) {
            items.add("text" + i);
        }
        rv.getAdapter().notifyDataSetChanged();
    }

    private void loadAds() {
        AdLoader.Builder builder = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/8135179316");
        AdLoader adLoader = builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                ads.add(ad);
                insertAdToItems();
            }
        }).forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd ad) {
                ads.add(ad);
                insertAdToItems();
            }
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(RecyclerViewAdActivity.this, "errorCode:" + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void insertAdToItems() {
        if (ads.isEmpty()) return;
        items.add(10, ads.get(0));
        rv.getAdapter().notifyDataSetChanged();
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        static final int ITEM_VIEW_TYPE = 0;
        static final int APP_INSTALL_AD_VIEW_TYPE = 1;
        static final int CONTENT_AD_VIEW_TYPE = 2;

        class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            ItemViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.text);
            }

            void setText(String text) {
                tv.setText(text);
            }
        }

        class AppInstallAdViewHolder extends RecyclerView.ViewHolder {

            AppInstallAdViewHolder(View itemView) {
                super(itemView);
                NativeAppInstallAdView adView = (NativeAppInstallAdView) itemView;

                // The MediaView will display a video asset if one is present in the ad, and the
                // first image asset otherwise.
                MediaView mediaView = (MediaView) adView.findViewById(R.id.appinstall_media);
                adView.setMediaView(mediaView);

                // Register the view used for each individual asset.
                adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
                adView.setBodyView(adView.findViewById(R.id.appinstall_body));
                adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
                adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
                adView.setPriceView(adView.findViewById(R.id.appinstall_price));
                adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
                adView.setStoreView(adView.findViewById(R.id.appinstall_store));
            }

        }

        class ContentAdViewHolder extends RecyclerView.ViewHolder {

            ContentAdViewHolder(View itemView) {
                super(itemView);
                NativeContentAdView adView = (NativeContentAdView) itemView;

                // Register the view used for each individual asset.
                adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
                adView.setImageView(adView.findViewById(R.id.contentad_image));
                adView.setBodyView(adView.findViewById(R.id.contentad_body));
                adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
                adView.setLogoView(adView.findViewById(R.id.contentad_logo));
                adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));
            }

        }

        @Override
        public int getItemViewType(int position) {

            Object recyclerViewItem = items.get(position);
            if (recyclerViewItem instanceof NativeAppInstallAd) {
                return APP_INSTALL_AD_VIEW_TYPE;
            } else if (recyclerViewItem instanceof NativeContentAd) {
                return CONTENT_AD_VIEW_TYPE;
            }
            return ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case APP_INSTALL_AD_VIEW_TYPE:
                    View nativeAppInstallLayoutView = LayoutInflater.from(
                            parent.getContext()).inflate(R.layout.item_rv_app_install_ad,
                            parent, false);
                    return new AppInstallAdViewHolder(nativeAppInstallLayoutView);
                case CONTENT_AD_VIEW_TYPE:
                    View nativeContentLayoutView = LayoutInflater.from(
                            parent.getContext()).inflate(R.layout.item_rv_content_ad,
                            parent, false);
                    return new ContentAdViewHolder(nativeContentLayoutView);
                default:
                    View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_rv_text, parent, false);
                    return new ItemViewHolder(itemLayoutView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            switch (viewType) {
                case APP_INSTALL_AD_VIEW_TYPE:
                    NativeAppInstallAd appInstallAd = (NativeAppInstallAd) items.get(position);
                    populateAppInstallAdView(appInstallAd, (NativeAppInstallAdView) holder.itemView);
                    break;
                case CONTENT_AD_VIEW_TYPE:
                    NativeContentAd contentAd = (NativeContentAd) items.get(position);
                    populateContentAdView(contentAd, (NativeContentAdView) holder.itemView);
                    break;
                default:
                    ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                    String text = (String) items.get(position);
                    itemViewHolder.setText(text);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd,
                                              NativeAppInstallAdView adView) {

            ((ImageView) adView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon()
                    .getDrawable());
            ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
            ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
            ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());

            if (nativeAppInstallAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.INVISIBLE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
            }

            if (nativeAppInstallAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
            }

            if (nativeAppInstallAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAppInstallAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }

            adView.setNativeAd(nativeAppInstallAd);
        }

        private void populateContentAdView(NativeContentAd nativeContentAd,
                                           NativeContentAdView adView) {

            ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
            ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
            ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
            ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

            List<NativeAd.Image> images = nativeContentAd.getImages();

            if (images.size() > 0) {
                ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
            }

            NativeAd.Image logoImage = nativeContentAd.getLogo();

            if (logoImage == null) {
                adView.getLogoView().setVisibility(View.INVISIBLE);
            } else {
                ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
                adView.getLogoView().setVisibility(View.VISIBLE);
            }

            adView.setNativeAd(nativeContentAd);
        }
    }
}
