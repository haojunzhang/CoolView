package idv.haojun.snaphelpersample;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvStart;
    private RecyclerView rvCenter;
    private RecyclerView rvPager;

    private List<Integer> mColorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorList = new ArrayList<>();
        mColorList.add(ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_bright));
        mColorList.add(ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_dark));
        mColorList.add(ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_light));
        mColorList.add(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_dark));
        mColorList.add(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light));
        mColorList.add(ContextCompat.getColor(MainActivity.this, android.R.color.holo_orange_dark));
        mColorList.add(ContextCompat.getColor(MainActivity.this, android.R.color.holo_orange_light));
        mColorList.add(ContextCompat.getColor(MainActivity.this, android.R.color.holo_purple));
        mColorList.add(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_dark));
        mColorList.add(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_light));

        rvStart = findViewById(R.id.rvStart);
        rvCenter = findViewById(R.id.rvCenter);
        rvPager = findViewById(R.id.rvPager);

        rvStart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvStart.setAdapter(new MyAdapter());
        new StartSnapHelper().attachToRecyclerView(rvStart);

        rvCenter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCenter.setAdapter(new MyAdapter());
        new LinearSnapHelper().attachToRecyclerView(rvCenter);

        rvPager.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPager.setAdapter(new MyAdapter());
        new PagerSnapHelper().attachToRecyclerView(rvPager);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {
            CardView card;

            ViewHolder(View v) {
                super(v);
                card = v.findViewById(R.id.card);
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.card.setCardBackgroundColor(mColorList.get(position));
        }

        @Override
        public int getItemCount() {
            return mColorList.size();
        }
    }
}
