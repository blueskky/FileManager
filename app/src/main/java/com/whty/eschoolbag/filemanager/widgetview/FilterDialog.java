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
import com.whty.eschoolbag.filemanager.bean.Filter;

import java.util.HashMap;
import java.util.Map;

public class FilterDialog extends Dialog implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private final LayoutInflater inflater;
    FilterListener mFilterListener;
    private HashMap<CheckBox, Filter> map;

    public FilterDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
        inflater = LayoutInflater.from(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_filter, null);
        setContentView(view);

        CheckBox cb_doc = (CheckBox) view.findViewById(R.id.cb_doc);
        cb_doc.setOnCheckedChangeListener(this);
        CheckBox cb_ppt = (CheckBox) view.findViewById(R.id.cb_ppt);
        cb_ppt.setOnCheckedChangeListener(this);
        CheckBox cb_xls = (CheckBox) view.findViewById(R.id.cb_xls);
        cb_xls.setOnCheckedChangeListener(this);
        CheckBox cb_pdf = (CheckBox) view.findViewById(R.id.cb_pdf);
        cb_pdf.setOnCheckedChangeListener(this);
        CheckBox cb_txt = (CheckBox) view.findViewById(R.id.cb_txt);
        cb_txt.setOnCheckedChangeListener(this);
        CheckBox cb_other = (CheckBox) view.findViewById(R.id.cb_other);
        cb_other.setOnCheckedChangeListener(this);

        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_confirm).setOnClickListener(this);


        map = new HashMap<>();
        map.put(cb_doc, Filter.DOC);
        map.put(cb_ppt, Filter.PPT);
        map.put(cb_xls, Filter.XLS);
        map.put(cb_pdf, Filter.PDF);
        map.put(cb_txt, Filter.TXT);
        map.put(cb_other, Filter.OTHER);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            for (Map.Entry<CheckBox, Filter> entry : map.entrySet()) {
                entry.getKey().setChecked(false);
            }
            ((CheckBox) buttonView).setChecked(true);
        }
    }


    @Override
    public void onClick(View v) {
        Filter value = Filter.DOC;
        for (Map.Entry<CheckBox, Filter> entry : map.entrySet()) {
            if (entry.getKey().isChecked()) {
                value = entry.getValue();
                break;
            }
        }

        switch (v.getId()) {
            case R.id.btn_confirm:
                if (mFilterListener != null) {
                    mFilterListener.filter(value);
                }
                break;
        }

        dismiss();
    }


    public void setFilterListener(FilterListener mFilterListener) {
        this.mFilterListener = mFilterListener;
    }

    public interface FilterListener {
        void filter(Filter filter);
    }
}
