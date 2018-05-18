package idv.haojun.imagepickersample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ImageViewerActivity extends AppCompatActivity {

    private TextView tvPosition;
    private ImageView ivToggle;
    private ViewPager vp;

    private int position;
    private List<String> paths;
    private List<String> selectedPaths;

    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        position = getIntent().getIntExtra("position", 0);
        paths = getIntent().getStringArrayListExtra("paths");
        selectedPaths = getIntent().getStringArrayListExtra("selectedPaths");

        tvPosition = findViewById(R.id.tv_image_viewer_position);
        ivToggle = findViewById(R.id.iv_image_viewer_toggle);
        vp = findViewById(R.id.vp_image_viewer);
        vp.setAdapter(new ImageViewerVPAdapter(paths));
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int p) {
                position = p;
                refreshSelectedUI();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ivToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPaths.contains(paths.get(position))) {
                    selectedPaths.remove(paths.get(position));
                } else {
                    selectedPaths.add(paths.get(position));
                }
                refreshSelectedUI();
            }
        });
        vp.setCurrentItem(position, true);
        refreshSelectedUI();
    }

    private void refreshSelectedUI() {
        tvPosition.setText(String.format(Locale.getDefault(), "%d/%d", position + 1, paths.size()));
        ivToggle.setImageResource(selectedPaths.contains(paths.get(position)) ? R.drawable.toggle_selected : R.drawable.toggle_unselected);
    }

    private class ImageViewerVPAdapter extends PagerAdapter {
        List<String> paths;

        ImageViewerVPAdapter(List<String> paths) {
            this.paths = paths;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());

            Picasso.get()
                    .load(new File(paths.get(position)))
                    .resize(screenWidth, screenHeight)
                    .centerInside()
                    .into(imageView);

            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("selectedPaths", new ArrayList<>(selectedPaths));
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
