package com.whty.eschoolbag.filemanager.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.util.FileOpenHelper;
import com.whty.eschoolbag.filemanager.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;

public class FileExploreAdapter extends BaseQuickAdapter<FileInfo, BaseViewHolder> {


    public FileExploreAdapter() {
        super(R.layout.file_base_explore_item);
    }


    @Nullable
    @Override
    public FileInfo getItem(int position) {
        return super.getItem(position);
    }


    @Override
    protected void convert(BaseViewHolder helper, FileInfo item) {
        ImageView imageView = (ImageView) helper.getView(R.id.iv_avater);
        FileOpenHelper.showFileIcon(mContext,item,imageView);

        helper.setText(R.id.tv_name, Util.getNameFromFilepath(item.getFilePath()));

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = format.format(item.getModifiedDate());
        helper.setText(R.id.tv_date, date);

        File file = new File(item.getFilePath());
        if(file.isFile()){
            helper.setText(R.id.tv_size, Util.convertStorage(file.length()));
            helper.setGone(R.id.tv_size,true);
        }else{
            helper.setGone(R.id.tv_size,false);
        }

//        helper.setText(R.id.tv_count,item.getCount()+"");

    }

}