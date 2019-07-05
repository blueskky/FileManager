package com.whty.eschoolbag.filemanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.whty.eschoolbag.filemanager.bean.Sort;
import com.whty.eschoolbag.filemanager.model.SortBean;


public class SPUtil {


    private static final String SP_NAME ="file_manager_sort";
    private static SPUtil sp;
    private final SharedPreferences.Editor editor;
    private final SharedPreferences sharedPreferences;

    public static SPUtil getInstance(Context context) {
        if (sp == null) {
            sp = new SPUtil(context);
        }
        return sp;
    }

    private SPUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void saveSort(String type, SortBean sortBean) {
        editor.putString(type,new Gson().toJson(sortBean)).commit();
    }


    public SortBean getSort(String type) {

        SortBean sortBean;

        String jsonSort = sharedPreferences.getString(type, "");
        if(TextUtils.isEmpty(jsonSort)){
            //默认的
             sortBean = new SortBean(1, Sort.SORT_TIME.getValue());
        }else{
             sortBean = new Gson().fromJson(jsonSort, SortBean.class);
        }

        return sortBean;
    }
}
