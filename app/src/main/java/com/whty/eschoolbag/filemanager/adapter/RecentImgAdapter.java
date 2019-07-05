package com.whty.eschoolbag.filemanager.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.FileInfo;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/18 10:25
 * description:
 */
class RecentImgAdapter extends BaseQuickAdapter<FileInfo, BaseViewHolder> {


    public RecentImgAdapter() {
        super(R.layout.recent_item_img);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileInfo item) {

        ImageView imageView = (ImageView) helper.getView(R.id.iv_img);
        Glide.with(mContext).load(item.getFilePath()).into(imageView);
    }


    @Nullable
    @Override
    public FileInfo getItem(int position) {
        return super.getItem(position);
    }
}
