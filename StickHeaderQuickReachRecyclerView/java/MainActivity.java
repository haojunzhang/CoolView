package com.example.haojun.nationcodedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final List<String> SIDE_LIST = Arrays.asList(
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    );

    private TextView tv_list;
    private RecyclerView mRecyclerView;
    private MyAdapter adapter;

    private List<Country> countries;
    private Map<String, Integer> firstAlphabetPosition;
    private LinearLayoutManager layoutManager;

    private String selectedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedCode = SPHelper.getCode(this);
        getCounties();
        getFirstAlphabetPositionToMap();

        tv_list = findViewById(R.id.tv_main_list);
        mRecyclerView = findViewById(R.id.recyclerview);

        tv_list.setText(getSideListText());
        tv_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent m) {
                Integer position = firstAlphabetPosition.get(SIDE_LIST.get(getSideListPosition(view.getHeight(), m.getY())));
                if (position != null) {
                    layoutManager.scrollToPositionWithOffset(position, 0);
                }
                return true;
            }
        });
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(this);
        mRecyclerView.setAdapter(adapter);

        adapter.setCountries(countries);
        adapter.setSelectedCode(selectedCode);

        mRecyclerView.addItemDecoration(
                new RecyclerSectionItemDecoration(
                        getResources().getDimensionPixelSize(R.dimen.header),
                        true,
                        getSectionCallback(countries)
                )
        );     
		scrollToDefaultPosition();
    }
	
	private void scrollToDefaultPosition() {
        int defaultPosition = getCountryPosition(selectedCode);
        layoutManager.scrollToPositionWithOffset(defaultPosition, isFirstAlphabetPosition(defaultPosition) ? 0 : getResources().getDimensionPixelSize(R.dimen.header));
    }
	
	private void scrollToDefaultPositionV23() {
        int defaultPosition = getCountryPosition(selectedCode);
        layoutManager.scrollToPositionWithOffset(defaultPosition, 0);
        if (!isFirstAlphabetPosition(defaultPosition)){
            mRecyclerView.smoothScrollBy(0, -getResources().getDimensionPixelSize(R.dimen.header));
        }
    }

    private String getSideListText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIDE_LIST.size(); i++) {
            if (i != 0)
                sb.append("\n");
            sb.append(SIDE_LIST.get(i));
        }
        return sb.toString();
    }

    private void getCounties() {
        String text = FileHelper.readRawText(this, R.raw.countrycodes_us);
        try {
            countries = new ArrayList<>();
            JSONArray array = new JSONArray(text);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                countries.add(new Country(
                        obj.getString("code"),
                        obj.getString("name"),
                        obj.getString("dial_code")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // for efficient
    private void getFirstAlphabetPositionToMap() {
        firstAlphabetPosition = new HashMap<>();
        for (int i = 0; i < countries.size(); i++) {
            String first = String.valueOf(countries.get(i).getName().charAt(0));
            if (!firstAlphabetPosition.containsKey(first))
                firstAlphabetPosition.put(first, i);
        }
    }

    private int getCountryPosition(String code) {
        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            if (country.getCode().equals(code)) return i;
        }
        return 0;
    }


    private int getSideListPosition(int viewHeight, float y) {
        if (y < 0) {
            return 0;
        } else if (y >= viewHeight) {
            return SIDE_LIST.size() - 1;
        } else {
            int itemHeight = viewHeight / SIDE_LIST.size();
            int p = (int) (y / itemHeight);
            return p >= SIDE_LIST.size() ? SIDE_LIST.size() - 1 : p;
        }
    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<Country> countries) {
        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int position) {
                return isFirstAlphabetPosition(position);
            }

            @Override
            public CharSequence getSectionHeader(int position) {
                return countries.get(position).getName()
                        .subSequence(0, 1);
            }
        };
    }

    private boolean isFirstAlphabetPosition(int position) {
        return position == 0
                || countries.get(position).getName()
                .charAt(0) != countries.get(position - 1).getName()
                .charAt(0);
    }
}