package idv.haojun.imagepickersample;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagePickActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_VIEWER = 0;

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pick);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        rv.setAdapter(new ImagePickRVAdapter());

        getImages();
    }

    private void getImages() {
        ContentResolver mContentResolver = getContentResolver();

        Cursor cursor = mContentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA},
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png", "image/bmp", "image/gif"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc"
        );

        List<String> paths = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    paths.add(cursor.getString(0));
                }
                cursor.close();
            }
        }

        ((ImagePickRVAdapter) rv.getAdapter()).setPaths(paths);
    }

    class ImagePickRVAdapter extends RecyclerView.Adapter<ImagePickRVAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView image;
            ImageView outline;
            ImageView toggle;

            ViewHolder(View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.iv_item_image_pick_image);
                outline = itemView.findViewById(R.id.iv_item_image_pick_outline);
                toggle = itemView.findViewById(R.id.iv_item_image_pick_toggle);

                image.setOnClickListener(this);
                toggle.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                switch (v.getId()) {
                    case R.id.iv_item_image_pick_image:
                        Intent intent = new Intent(getApplicationContext(), ImageViewerActivity.class);
                        intent.putExtra("position", position);
                        intent.putStringArrayListExtra("paths", new ArrayList<>(paths));
                        intent.putStringArrayListExtra("selectedPaths", new ArrayList<>(selectedPaths));
                        startActivityForResult(intent, REQUEST_IMAGE_VIEWER);
                        break;
                    case R.id.iv_item_image_pick_toggle:
                        if (selectedPaths.contains(paths.get(position))) {
                            selectedPaths.remove(paths.get(position));
                        } else {
                            selectedPaths.add(paths.get(position));
                        }
                        notifyDataSetChanged();
                        break;
                }
            }

            void setImage(String path) {
                Picasso.get()
                        .load(new File(path))
                        .resize(imageWidth, imageWidth)
                        .centerCrop()
                        .placeholder(R.drawable.loading)
                        .into(image);

                image.setLayoutParams(new RelativeLayout.LayoutParams(imageWidth, imageWidth));
            }

            void setAlpha(float alpha) {
                image.setAlpha(alpha);
            }

            void setToggle(int resId) {
                toggle.setImageResource(resId);
            }

            void setOutline(int resId) {
                outline.setImageResource(resId);
                outline.setLayoutParams(new RelativeLayout.LayoutParams(imageWidth, imageWidth));
            }
        }

        private List<String> paths;
        private List<String> selectedPaths;
        private int imageWidth;

        ImagePickRVAdapter() {
            paths = new ArrayList<>();
            selectedPaths = new ArrayList<>();
            imageWidth = getResources().getDisplayMetrics().widthPixels / 3;
        }

        void setPaths(List<String> paths) {
            this.paths = paths;
            notifyDataSetChanged();
        }

        void setSelectedPaths(List<String> selectedPaths) {
            this.selectedPaths = selectedPaths;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_pick, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setImage(paths.get(position));
            boolean selected = selectedPaths.contains(paths.get(position));
            holder.setAlpha(selected ? 0.5f : 1.0f);
            holder.setToggle(selected ? R.drawable.toggle_selected : R.drawable.toggle_unselected);
            holder.setOutline(selected ? R.drawable.outline : 0);
        }

        @Override
        public int getItemCount() {
            return paths.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_VIEWER) {
            if (resultCode == RESULT_OK) {
                List<String> selectedPaths = data.getStringArrayListExtra("selectedPaths");
                ((ImagePickRVAdapter) rv.getAdapter()).setSelectedPaths(selectedPaths);
            }
        }
    }
}
