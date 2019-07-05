package com.whty.eschoolbag.filemanager.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.PathItem;

public class PathAdapter extends BaseQuickAdapter<PathItem, BaseViewHolder> {


    public PathAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Nullable
    @Override
    public PathItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    protected void convert(BaseViewHolder helper, PathItem item) {

        helper.setText(R.id.tv_path, item.getPathName());
    }
}