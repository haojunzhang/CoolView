package com.example.haojun.youtubelistdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private StickyGroupExpandableListView mExpandableListView;
    private MyExpandableListAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExpandableListView = findViewById(R.id.elv);

        mAdapter = new MyExpandableListAdapter(this);
        mAdapter.setGroups(getGroups());
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setPinnedHeaderView(getGroupView());
        mExpandableListView.setOnScrollListener(mAdapter);
        mExpandableListView.setDividerHeight(0);
    }

    private View getGroupView() {
        return LayoutInflater.from(this).inflate(R.layout.item_group, (ViewGroup)findViewById(R.id.ll_main), false);
    }

    private List<Group> getGroups() {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            groups.add(new Group("Group" + i, getChildren()));
        }
        return groups;
    }

    private List<Child> getChildren() {
        List<Child> children = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            children.add(new Child("Child" + i));
        }
        return children;
    }

}