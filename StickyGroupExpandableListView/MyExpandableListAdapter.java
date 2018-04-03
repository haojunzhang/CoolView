package com.example.haojun.youtubelistdemo;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyExpandableListAdapter extends BaseExpandableListAdapter implements StickyGroupExpandableListView.PinnedHeaderAdapter, AbsListView.OnScrollListener {

    private Context context;
    private List<Group> groups;

    // color
    private int groupBackgroundColor;
    private int groupTextColor;

    public MyExpandableListAdapter(Context context) {
        this.context = context;
        this.groups = new ArrayList<>();
        this.groupBackgroundColor = ContextCompat.getColor(context, R.color.groupBackgroundColor);
        this.groupTextColor = ContextCompat.getColor(context, R.color.groupTextColor);
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getChildren().size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View v, ViewGroup parent) {
        GroupViewHolder holder;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_group, null);
            holder = new GroupViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (GroupViewHolder) v.getTag();
        }
        Group group = groups.get(groupPosition);
        holder.title.setText(group.getTitle());
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View v, ViewGroup parent) {
        ChildViewHolder holder;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_child, null);
            holder = new ChildViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ChildViewHolder) v.getTag();
        }
        Child child = groups.get(groupPosition).getChildren().get(childPosition);
        holder.title.setText(child.getTitle());
        return v;
    }

    @Override
    public void configurePinnedHeader(View v, int position, int alpha) {
        TextView header = v.findViewById(R.id.tv_item_group_title);

        header.setText(groups.get(position).getTitle());

        header.setBackgroundColor(groupBackgroundColor);
        header.setTextColor(groupTextColor);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof StickyGroupExpandableListView) {
            ((StickyGroupExpandableListView) view).configureHeaderView(firstVisibleItem);
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub

    }

    class GroupViewHolder {
        TextView title;

        GroupViewHolder(View v) {
            title = v.findViewById(R.id.tv_item_group_title);
        }
    }

    class ChildViewHolder {
        TextView title;

        ChildViewHolder(View v) {
            title = v.findViewById(R.id.tv_item_child_title);
        }
    }
}