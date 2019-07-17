package com.example.bannerviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bannerView = findViewById(R.id.bannerView);

        List<Banner> list = new ArrayList<>();
        list.add(new Banner("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/1810-106-on-the-road-01-1538542445.jpg?crop=0.8894117647058823xw:1xh;center,top&resize=640:*"));
        list.add(new Banner("http://pic.mygonews.com/fetch.php?v=b951f5ef971e1fe73007d99019fd0941.jpg&t=newsPhoto"));
        list.add(new Banner("https://travelimg.yam.com/DATA/ARTICLE/20161115163009673.JPG"));
        list.add(new Banner("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFPTnAVbHeENiq87SJh-9jVMkVbKckACx3A7-fANQfD8WsW7iHlw"));
        list.add(new Banner("https://i2.wp.com/tw.tranews.com/Show/images/News/3298989_1.jpg?resize=640%2C480"));
        bannerView.setData(list);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (bannerView != null) {
//            bannerView.start();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (bannerView != null) {
//            bannerView.stop();
//        }
//    }
}
