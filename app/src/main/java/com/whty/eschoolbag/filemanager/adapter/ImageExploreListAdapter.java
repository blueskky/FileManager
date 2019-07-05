package com.whty.eschoolbag.filemanager.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.model.Album;

public class ImageExploreListAdapter extends BaseQuickAdapter<Album,BaseViewHolder> {


    public ImageExploreListAdapter() {
        super(R.layout.file_explore_item);
    }


    @Nullable
    @Override
    public Album getItem(int position) {
        return super.getItem(position);
    }

    @Override
    protected void convert(BaseViewHolder helper, Album item) {
        ImageView imageView = (ImageView) helper.getView(R.id.iv_avater);
        Glide.with(mContext).load(item.getCoverPath()).into(imageView);

        if(item.getDisplayName().equals("All")){
            helper.setText(R.id.tv_name,"全部");
        }else{
            helper.setText(R.id.tv_name,item.getDisplayName());
        }

        helper.setText(R.id.tv_count,String.format("共%d张",item.getCount()));
    }
}
