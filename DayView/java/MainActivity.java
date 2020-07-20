package com.example.dragdropview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// 拖到的地點重疊則禁止

public class MainActivity extends AppCompatActivity {
    DayView dayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dayView = findViewById(R.id.dayView);

        dayView.setOnDayViewListener(new DayView.OnDayViewListener() {
            @Override
            public void onEmptySpaceClick(View v, String time) {

            }

            @Override
            public void onOverlapping(View v) {
                Toast.makeText(MainActivity.this, "重複", Toast.LENGTH_SHORT).show();
            }
        });

        List<DayViewEvent> eventList = new ArrayList<>();
        eventList.add(addEvent("aaa", 5, 0, 6, 0));
        eventList.add(addEvent("bbb", 6, 0, 7, 0));

        dayView.setEventList(eventList);

    }

    private DayViewEvent addEvent(String content,
                                  int fromHour,
                                  int fromMinute,
                                  int toHour,
                                  int toMinute) {
        DayViewEvent event = new DayViewEvent();
        event.setContent(content);
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(Calendar.HOUR_OF_DAY, fromHour);
        fromCalendar.set(Calendar.MINUTE, fromMinute);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(Calendar.HOUR_OF_DAY, toHour);
        toCalendar.set(Calendar.MINUTE, toMinute);
        event.setFromCalendar(fromCalendar);
        event.setToCalendar(toCalendar);
        return event;
    }
}


