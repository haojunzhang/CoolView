package idv.haojun.sidebarindicator;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    View rootMain;
    List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        items.add(new Item(R.drawable.item1, "item1", Color.parseColor("#FF9800")));
        items.add(new Item(R.drawable.item2, "item2", Color.parseColor("#F44336")));
        items.add(new Item(R.drawable.item3, "item3", Color.parseColor("#4CAF50")));
        items.add(new Item(R.drawable.item4, "item4", Color.parseColor("#00BCD4")));
        items.add(new Item(R.drawable.item5, "item5", Color.parseColor("#9C27B0")));

        rootMain = findViewById(R.id.rl_main_root);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RVAdapter());
    }

    class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

        int selectedPosition = 0;

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            View root;
            ImageView icon;
            TextView title;
            ImageView indicator;

            ViewHolder(View itemView) {
                super(itemView);
                root = itemView.findViewById(R.id.ll_item_rv_root);
                icon = (ImageView) itemView.findViewById(R.id.iv_item_rv_icon);
                title = (TextView) itemView.findViewById(R.id.tv_item_rv_title);
                indicator = (ImageView) itemView.findViewById(R.id.iv_item_rv_indicator);

                itemView.setOnClickListener(this);
            }

            void setIcon(int id) {
                icon.setImageResource(id);
            }

            void setTitle(String t) {
                title.setText(t);
            }

            void setIndicatorVisibility(int i) {
                indicator.setVisibility(i);
            }

            @Override
            public void onClick(View view) {
                int i=getAdapterPosition();
                setSelectedPosition(i);
                rootMain.setBackgroundColor(items.get(i).color);
            }
        }

        void setSelectedPosition(int p) {
            selectedPosition = p;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setIcon(items.get(position).icon);
            holder.setTitle(items.get(position).title);
            holder.setIndicatorVisibility(selectedPosition == position ? View.VISIBLE : View.GONE);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private class Item {
        int icon;
        String title;
        int color;

        Item(int icon, String title,int color) {
            this.icon = icon;
            this.title = title;
            this.color = color;
        }
    }
}
