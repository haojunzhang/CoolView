package com.example.threedpager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Integer> imageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);

        imageList = new ArrayList<>();
        imageList.add(R.drawable.ic_launcher_background);
        imageList.add(R.drawable.ic_launcher_foreground);
        imageList.add(R.mipmap.ic_launcher);
        imageList.add(R.mipmap.ic_launcher_round);
        viewPager.setAdapter(new MyViewPagerAdapter());
        viewPager.setPageTransformer(true, new CubeTransformer());
    }

    private class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.pager_item, container, false);
            ImageView iv = view.findViewById(R.id.ivImage);

            iv.setImageResource(imageList.get(position));

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
