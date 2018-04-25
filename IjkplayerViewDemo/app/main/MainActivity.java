package idv.haojun.aplayer.app.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import idv.haojun.aplayer.R;
import idv.haojun.aplayer.adapter.BaseRVAdapter;
import idv.haojun.aplayer.adapter.VideoRVAdapter;
import idv.haojun.aplayer.app.video.VideoActivity;
import idv.haojun.aplayer.base.App;
import idv.haojun.aplayer.base.BaseActivity;
import idv.haojun.aplayer.model.AVideo;

public class MainActivity extends BaseActivity implements MainContract.View, BaseRVAdapter.OnItemClickListener {

    private MainContract.Presenter mPresenter;

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTitle(R.string.app_name);

        rv = findViewById(R.id.rv_main);

        rv.setLayoutManager(new LinearLayoutManager(this));
        VideoRVAdapter adapter = new VideoRVAdapter(this, R.layout.item_rv_video);
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);

        mPresenter = new MainPresenter(this);
        mPresenter.getLocalVideosWithPermission();
    }

    @Override
    public void displayVideos(List<AVideo> videos) {

        ((VideoRVAdapter) rv.getAdapter()).setData(videos);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onItemClick(View view, int position) {
        List<AVideo> videos = ((VideoRVAdapter) rv.getAdapter()).getData();
        startActivity(
                new Intent(this, VideoActivity.class)
                        .putParcelableArrayListExtra("videos", new ArrayList<Parcelable>(videos))
                        .putExtra("position", position)
        );
    }

    @Override
    public void exit() {
        App.getInstance().exitApp();
    }
}
