package com.whty.eschoolbag.filemanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/5 16:45
 * description:
 */
public class PreferencesUtil {


    private static final String SP_NAME = "search_history";
    private static final String KEY = "history";
    private static volatile PreferencesUtil singleton;
    private final SharedPreferences preferences;

    private PreferencesUtil(Context context) {
        preferences = context.getSharedPreferences(SP_NAME, 0);

    }

    public static PreferencesUtil getInstance(Context context) {
        if (singleton == null) {
            synchronized (PreferencesUtil.class) {
                if (singleton == null) {
                    singleton = new PreferencesUtil(context);
                }
            }
        }
        return singleton;
    }


    public void addItem(String content) {

        ArrayList<String> historyList = getHistoryList();
        if (historyList != null && historyList.size() > 9) {
            historyList.remove(0);
        }
        if(historyList.contains(content)){
            historyList.remove(content);
        }
        historyList.add(0,content);
        String json = new Gson().toJson(historyList);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(KEY, json).commit();
    }


    public ArrayList<String> getHistoryList() {
        ArrayList<String> list = new ArrayList<>();
        String string = preferences.getString(KEY, "");
        if (!TextUtils.isEmpty(string)) {
            list = new Gson().fromJson(string, new TypeToken<List<String>>() {
            }.getType());
        }
        return list;
    }


    public void removeAll() {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(KEY, "").commit();
    }
}
