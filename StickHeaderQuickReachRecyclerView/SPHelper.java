package com.example.haojun.nationcodedemo;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {

    public static SharedPreferences getSP(Context context){
        return context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static void setCode(Context context, String code){
        getSP(context).edit().putString("code", code).apply();
    }
    public static String getCode(Context context){
        return getSP(context).getString("code", "");
    }
}
