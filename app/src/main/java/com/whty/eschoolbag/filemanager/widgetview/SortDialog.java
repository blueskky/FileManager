package com.whty.eschoolbag.filemanager.widgetview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.Sort;
import com.whty.eschoolbag.filemanager.model.SortBean;

import java.util.HashMap;
import java.util.Map;

public class SortDialog extends Dialog implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private final LayoutInflater inflater;
    SortListener mSortListener;
    private HashMap<CheckBox, Sort> map;
    SortBean sort;

    public SortDialog(@NonNull Context context, SortBean sort) {
        super(context, R.style.Dialog);
        this.sort = sort;
        inflater = LayoutInflater.from(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_sort, null);
        setContentView(view);

        CheckBox cb_category = (CheckBox) view.findViewById(R.id.cb_category);
        cb_category.setOnCheckedChangeListener(this);
        CheckBox cb_name = (CheckBox) view.findViewById(R.id.cb_name);
        cb_name.setOnCheckedChangeListener(this);
        CheckBox cb_size = (CheckBox) view.findViewById(R.id.cb_size);
        cb_size.setOnCheckedChangeListener(this);
        CheckBox cb_time = (CheckBox) view.findViewById(R.id.cb_time);
        cb_time.setOnCheckedChangeListener(this);

        view.findViewById(R.id.btn_des).setOnClickListener(this);
        view.findViewById(R.id.btn_asc).setOnClickListener(this);


        map = new HashMap<>();
        map.put(cb_category, Sort.SORT_CATEGORY);
        map.put(cb_name, Sort.SORT_NAME);
        map.put(cb_size, Sort.SORT_SIZE);
        map.put(cb_time, Sort.SORT_TIME);


        if (sort.getSortType() == Sort.SORT_CATEGORY.getValue()) {
            cb_category.setChecked(true);
        } else if (sort.getSortType() == Sort.SORT_NAME.getValue()) {
            cb_name.setChecked(true);
        }else if (sort.getSortType() == Sort.SORT_SIZE.getValue()) {
            cb_size.setChecked(true);
        }else if (sort.getSortType() == Sort.SORT_TIME.getValue()) {
            cb_time.setChecked(true);
        }



    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            for (Map.Entry<CheckBox, Sort> entry : map.entrySet()) {
                entry.getKey().setChecked(false);
            }
            ((CheckBox) buttonView).setChecked(true);
        }
    }


    @Override
    public void onClick(View v) {
        Sort value = Sort.SORT_CATEGORY;
        for (Map.Entry<CheckBox, Sort> entry : map.entrySet()) {
            if (entry.getKey().isChecked()) {
                value = entry.getValue();
                break;
            }
        }

        switch (v.getId()) {
            case R.id.btn_des://降序
                if (mSortListener != null) {
                    mSortListener.sort(new SortBean(-1,value.getValue()));
                }
                break;
            case R.id.btn_asc://升序
                if (mSortListener != null) {
                    mSortListener.sort(new SortBean(1,value.getValue()));
                }
                break;
        }

        dismiss();
    }


    public void setmSortListener(SortListener mSortListener) {
        this.mSortListener = mSortListener;
    }

    public interface SortListener {
        void sort(SortBean sortBean);
    }
}
