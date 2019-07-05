package com.whty.eschoolbag.filemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.widgetview.flowlayout.FlowLayout;
import com.whty.eschoolbag.filemanager.widgetview.flowlayout.TagAdapter;

import java.util.List;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/6 10:26
 * description:
 */
public class HistoryTagAdapter extends TagAdapter<String> {

    private Context context;

    public HistoryTagAdapter(Context context,List<String> datas) {
        super(datas);
        this.context=context;
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        View view = LayoutInflater.from(context).inflate(R.layout.tv, parent, false);
        TextView tvTag = (TextView) view.findViewById(R.id.tv_tag);
        tvTag.setText(s);
        return view;
    }
}
