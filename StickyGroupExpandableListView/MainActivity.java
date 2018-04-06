package com.example.haojun.youtubelistdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity implements View.OnClickListener, ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnGroupExpandListener {

    TextView mTextTitle;
    ImageView mImageToggle;

    ExpandableListView mExpandableListView;
    MyAdapter mAdapter;
    List<Group> mGroups;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGroups = getGroups();

        mTextTitle = findViewById(R.id.tv_item_group_title);
        mImageToggle = findViewById(R.id.iv_item_group_toggle);
        mExpandableListView = findViewById(R.id.elv);
        mTextTitle.setOnClickListener(this);
        mExpandableListView.setOnGroupCollapseListener(this);
        mExpandableListView.setOnGroupExpandListener(this);

        mAdapter=new MyAdapter();
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int groupPosition = getFirstVisibleGroup();

                mTextTitle.setText(mGroups.get(groupPosition).getTitle());

                if (mAdapter.getExpandGroupPositions().contains(groupPosition)) {
                    mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                } else {
                    mImageToggle.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
                }
            }
        });
    }

    public int getFirstVisibleGroup() {
        int firstVis = mExpandableListView.getFirstVisiblePosition();
        long packedPosition = mExpandableListView.getExpandableListPosition(firstVis);
        return ExpandableListView.getPackedPositionGroup(packedPosition);
    }


    private List<Group> getGroups() {
        List<Group> mGroups = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mGroups.add(new Group("Group" + i, getChildren()));
        }
        return mGroups;
    }

    private List<Child> getChildren() {
        List<Child> children = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            children.add(new Child("Child" + i));
        }
        return children;
    }

    @Override
    public void onClick(View v) {
        int groupPosition = getFirstVisibleGroup();
        if (mExpandableListView.isGroupExpanded(groupPosition)) {
            mExpandableListView.collapseGroup(groupPosition);
        } else {
            mExpandableListView.expandGroup(groupPosition);
        }

    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        mAdapter.toggleGroupIcon(groupPosition);
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        mAdapter.toggleGroupIcon(groupPosition);
    }

    class MyAdapter extends BaseExpandableListAdapter {
        private Set<Integer> expandGroupPositions;

        MyAdapter() {
            expandGroupPositions = new HashSet<>();
        }

        Set<Integer> getExpandGroupPositions(){
            return expandGroupPositions;
        }

        @Override
        public int getGroupCount() {
            return mGroups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mGroups.get(groupPosition).getChildren().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View v, ViewGroup parent) {
            GroupViewHolder h;
            if (v == null) {
                v = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_group, null);
                h = new GroupViewHolder(v);
                v.setTag(h);
            } else {
                h = (GroupViewHolder) v.getTag();
            }
            h.title.setText(mGroups.get(groupPosition).getTitle());

            if (expandGroupPositions.contains(groupPosition)) {
                h.toggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
            } else {
                h.toggle.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
            }

            return v;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View v, ViewGroup parent) {
            ChildViewHolder h;
            if (v == null) {
                v = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_child, null);
                h = new ChildViewHolder(v);
                v.setTag(h);
            } else {
                h = (ChildViewHolder) v.getTag();
            }
            h.title.setText(mGroups.get(groupPosition).getChildren().get(childPosition).getTitle());
            return v;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        void toggleGroupIcon(int groupPosition) {
            if (expandGroupPositions.contains(groupPosition)) {
                expandGroupPositions.remove(groupPosition);
            } else {
                expandGroupPositions.add(groupPosition);
            }
            notifyDataSetChanged();
        }

        class GroupViewHolder {
            TextView title;
            ImageView toggle;

            GroupViewHolder(View v) {
                title = v.findViewById(R.id.tv_item_group_title);
                toggle = v.findViewById(R.id.iv_item_group_toggle);
            }
        }

        class ChildViewHolder {
            TextView title;

            ChildViewHolder(View v) {
                title = v.findViewById(R.id.tv_item_child_title);
            }
        }
    }
}