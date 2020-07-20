package com.example.dragdropview;

import java.util.Calendar;

public class DayViewEvent {
    private Calendar fromCalendar;
    private Calendar toCalendar;
    private String content;
    private Object tag;

    public Calendar getFromCalendar() {
        return fromCalendar;
    }

    public void setFromCalendar(Calendar fromCalendar) {
        this.fromCalendar = fromCalendar;
    }

    public Calendar getToCalendar() {
        return toCalendar;
    }

    public void setToCalendar(Calendar toCalendar) {
        this.toCalendar = toCalendar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setFromTimeWithOriginalOffset(String time) {
        // 如原本是01:00~02:00
        // time是06:00的話
        // 會變06:00~07:00
        int newFromHour = Integer.parseInt(time.split(":")[0]);
        int newFromMinute = Integer.parseInt(time.split(":")[1]);

        // 如果from一樣則跳過
        if (newFromHour == fromCalendar.get(Calendar.HOUR_OF_DAY) &&
                newFromMinute == fromCalendar.get(Calendar.MINUTE)) {
            return;
        }

        int deltaHour = toCalendar.get(Calendar.HOUR_OF_DAY) - fromCalendar.get(Calendar.HOUR_OF_DAY);
        int deltaMinute = toCalendar.get(Calendar.MINUTE) - fromCalendar.get(Calendar.MINUTE);
        fromCalendar.set(Calendar.HOUR_OF_DAY, newFromHour);
        fromCalendar.set(Calendar.MINUTE, newFromMinute);

        // 解決進位, ex: 03:45 + 01:30 > 05:15
        int newToHour = newFromHour + deltaHour + (newFromMinute + deltaMinute) / 60;
        int newToMinute = (newFromMinute + deltaMinute) % 60;
        toCalendar.set(Calendar.HOUR_OF_DAY, newToHour);
        toCalendar.set(Calendar.MINUTE, newToMinute);
    }

    @Override
    public String toString() {
        return String.format("%s %02d:%02d ~ %02d:%02d", content,
                fromCalendar.get(Calendar.HOUR_OF_DAY),
                fromCalendar.get(Calendar.MINUTE),
                toCalendar.get(Calendar.HOUR_OF_DAY),
                toCalendar.get(Calendar.MINUTE));
    }
}
