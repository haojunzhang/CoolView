package ryan.net.timelineviewdemo;

import java.util.Date;

public class Todo {
    private Date date;
    private String title;
    private String content;

    public Todo(){

    }

    public Todo(Date date, String title, String content) {
        this.date = date;
        this.title = title;
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
