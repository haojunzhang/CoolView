package com.example.haojun.youtubelistdemo;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String title;
    private List<Child> children;

    public Group(String title, List<Child> children) {
        this.title = title;
        this.children = children;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Child> getChildren() {
        return children;
    }
}
