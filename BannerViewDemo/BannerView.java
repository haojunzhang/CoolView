package com.example.bannerviewdemo;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

public class BannerView extends FrameLayout {

    public static final int BANNER_LOOP_INTERVAL = 3000;

    private RecyclerView rvBanner, rvIndicator;

    private LinearLayoutManager bannerLayoutManager, indicatorLayoutManager;

    private BannerAdapter bannerAdapter;
    private IndicatorAdapter indicatorAdapter;

    private int currentPosition = -1;

    private Handler bannerLoopHandler;
    private boolean isRunning = false, isDragging = false;

    public BannerView(Context context) {
        super(context);
        init();
        initView(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initView(context);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initView(context);
    }

    private void init() {
        bannerLoopHandler = new Handler();
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.banner_view, null);
        rvBanner = view.findViewById(R.id.rvBanner);
        rvIndicator = view.findViewById(R.id.rvIndicator);

        bannerLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        indicatorLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);

        rvBanner.setLayoutManager(bannerLayoutManager);
        rvIndicator.setLayoutManager(indicatorLayoutManager);

        bannerAdapter = new BannerAdapter();
        indicatorAdapter = new IndicatorAdapter();

        rvBanner.setAdapter(bannerAdapter);
        rvIndicator.setAdapter(indicatorAdapter);

        rvBanner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isDragging = newState == RecyclerView.SCROLL_STATE_DRAGGING;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                applyIndicatorPosition();
            }
        });

        new PagerSnapHelper().attachToRecyclerView(rvBanner);

        addView(view);
    }

    public void setData(List<Banner> list) {
        bannerAdapter.setData(list);
        indicatorAdapter.setData(list);
    }

    private class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

        private List<Banner> list;

        BannerAdapter() {
            list = new ArrayList<>();
        }

        void setData(List<Banner> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Banner item = list.get(position % list.size());

            holder.loadBanner(item);
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView ivBanner;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ivBanner = itemView.findViewById(R.id.ivBanner);
            }

            void loadBanner(Banner item) {
                Glide.with(getContext())
                        .load(item.getUrl())
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivBanner);
            }
        }
    }

    private class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.ViewHolder> {

        private List<Banner> list;

        IndicatorAdapter() {
            list = new ArrayList<>();
        }

        void setData(List<Banner> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.indicator_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            position = position % list.size();
            if (currentPosition == position) {
                holder.indicatorOn();
            } else {
                holder.indicatorOff();
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivIndicator;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ivIndicator = itemView.findViewById(R.id.ivIndicator);
            }

            void indicatorOn() {
                ivIndicator.setImageResource(R.drawable.indicator_on);
            }

            void indicatorOff() {
                ivIndicator.setImageResource(R.drawable.indicator_off);
            }
        }
    }

    private void applyIndicatorPosition() {
        int position = bannerLayoutManager.findFirstCompletelyVisibleItemPosition() % indicatorAdapter.getItemCount();
        if (bannerLayoutManager.findFirstCompletelyVisibleItemPosition() >= 0 && currentPosition != position) {
            currentPosition = position;
            indicatorAdapter.notifyDataSetChanged();
        }
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            changeBanner();
        }
    }

    private void changeBanner() {
        if (!isRunning || bannerAdapter.getItemCount() == 0) {
            return;
        }

        bannerLoopHandler.postDelayed(() -> {
            if (!isDragging) {
                rvBanner.scrollToPosition(++currentPosition);
                applyIndicatorPosition();
            }

            // call self
            bannerLoopHandler.post(this::changeBanner);
        }, BANNER_LOOP_INTERVAL);
    }

    public void stop() {
        isRunning = false;
    }
}
