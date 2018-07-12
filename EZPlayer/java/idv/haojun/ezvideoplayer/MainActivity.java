package idv.haojun.ezvideoplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_VIDEO = 0;

    private RecyclerView rv;

    // extra(from service)
    private VideoItem mVideoItem;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // extra
        mVideoItem = getIntent().getParcelableExtra("videoItem");
        currentPosition = getIntent().getIntExtra("currentPosition", 0);

        rv = findViewById(R.id.rv_main);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new VideoListRVAdapter());
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getAllVideoInPhone();

        // from service > main > video
        if (mVideoItem != null){
            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            intent.putExtra("videoItem", mVideoItem);
            intent.putExtra("currentPosition", currentPosition);
            startActivityForResult(intent, REQUEST_VIDEO);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllVideoInPhone() {
        new AsyncTask<Void, Void, Void>() {

            List<VideoItem> items = new ArrayList<>();

            @Override
            protected Void doInBackground(Void... voids) {
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.SIZE};

                final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

                Cursor cursor = getContentResolver().query(uri, projection, null, null, orderBy + " DESC");


                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        VideoItem item = new VideoItem();
                        item.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
                        item.setDuration(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
                        item.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
                        items.add(item);
                    }
                    cursor.close();
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                findViewById(R.id.progress_main).setVisibility(View.GONE);
                ((VideoListRVAdapter) rv.getAdapter()).setItems(items);
            }
        }.execute();
    }

    private class VideoListRVAdapter extends RecyclerView.Adapter<VideoListRVAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView ivThumbnail;
            TextView tvName;
            TextView tvSize;
            TextView tvDuration;

            ViewHolder(View itemView) {
                super(itemView);

                ivThumbnail = itemView.findViewById(R.id.iv_item_video_thumbnail);
                tvName = itemView.findViewById(R.id.tv_item_video_name);
                tvSize = itemView.findViewById(R.id.tv_item_video_size);
                tvDuration = itemView.findViewById(R.id.tv_item_video_duration);

                itemView.setOnClickListener(this);
            }

            void setThumbnail(String path) {
                Glide.with(MainActivity.this)
                        .load(new File(path))
                        .into(ivThumbnail);
            }

            void setName(String name) {
                tvName.setText(name);
            }

            void setSize(String size) {
                tvSize.setText(size);
            }

            void setDuration(String duration) {
                tvDuration.setText(duration);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();

                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                intent.putExtra("videoItem", items.get(position));
                startActivityForResult(intent, REQUEST_VIDEO);
            }
        }

        private List<VideoItem> items;

        VideoListRVAdapter() {
            items = new ArrayList<>();
        }

        public void setItems(List<VideoItem> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            VideoItem item = items.get(position);
            holder.setThumbnail(item.getPath());
            holder.setName(item.getName());
            holder.setSize(item.getSizeText());
            holder.setDuration(DateTimeHelper.second2mmss(item.getDuration() / 1000));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_VIDEO:
                    finish();
                    break;
            }
        }
    }
}
