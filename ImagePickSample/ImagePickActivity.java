package ryan.idv.imagepicksample;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImagePickActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_VIEWER = 0;

    private RecyclerView rv;
    private Spinner spinner;

    private List<String> listPath;
    private List<String> listFolders;
    private boolean userIsInteracting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pick);

        spinner = findViewById(R.id.spinner);
        rv = findViewById(R.id.rv);

        rv.setLayoutManager(new GridLayoutManager(this, 3));
        rv.setAdapter(new ImagePickRVAdapter());

        spinner.setAdapter(new FolderArrayAdapter());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (userIsInteracting) {
                    getImages(listFolders.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getImages();

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
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
        if (listFolders == null) {
            listFolders = new ArrayList<>();
        } else {
            listFolders.clear();
        }
        listFolders.add("All");

        List<String> paths = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {

                    paths.add(cursor.getString(0));

                    String folderName = new File(cursor.getString(0)).getParentFile().getName();
                    if (!listFolders.contains(folderName))
                        listFolders.add(folderName);
                }
                cursor.close();
            }
        }
        listPath = paths;

        ((FolderArrayAdapter) spinner.getAdapter()).setFolderNames(listFolders);
        ((ImagePickRVAdapter) rv.getAdapter()).setPaths(paths);
    }

    private void getImages(String folder) {
        List<String> paths = new ArrayList<>();
        for (String path : listPath) {
            if (folder.equals("All") || folder.equals(new File(path).getParentFile().getName())) {
                paths.add(path);
            }
        }
        ((ImagePickRVAdapter) rv.getAdapter()).setPaths(paths);
    }

    class FolderArrayAdapter extends BaseAdapter {
        private List<String> folderNames;

        FolderArrayAdapter() {
            this.folderNames = new ArrayList<>();
        }

        void setFolderNames(List<String> folderNames) {
            this.folderNames = folderNames;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return folderNames.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder holder;
            if (v == null) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, null);
                holder = new ViewHolder(v);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            holder.folderName.setText(folderNames.get(position));
            return v;
        }

        class ViewHolder {
            TextView folderName;

            ViewHolder(View v) {
                folderName = v.findViewById(R.id.tv_item_folder_name);
            }
        }
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
