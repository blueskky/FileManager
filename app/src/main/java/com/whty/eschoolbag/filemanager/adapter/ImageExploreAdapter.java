package com.whty.eschoolbag.filemanager.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.util.DensityUtil;

public class ImageExploreAdapter extends BaseQuickAdapter<FileInfo, BaseViewHolder> {


    public ImageExploreAdapter() {
        super(R.layout.image_item);
    }


    @Nullable
    @Override
    public FileInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileInfo item) {

        ImageView imageView = (ImageView) helper.getView(R.id.iv_img);
        Glide.with(mContext).load(item.getFilePath()).into(imageView);

        int screenWidth = DensityUtil.getScreenWidth(mContext);

        int imgWidth = (screenWidth - 210) / 9;

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = imgWidth;
        params.width = imgWidth;
        imageView.setLayoutParams(params);

    }
}