package com.whty.eschoolbag.filemanager.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.util.FileOpenHelper;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/18 10:29
 * description:
 */
class RecentFileAdapter extends BaseQuickAdapter<FileInfo, BaseViewHolder> {


    public RecentFileAdapter() {
        super(R.layout.recent_item_file);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileInfo item) {

        ImageView imageView = (ImageView) helper.getView(R.id.iv_img);

        File file = new File(item.getFilePath());
        helper.setText(R.id.tv_name, file.getName());
        helper.setText(R.id.tv_time, new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(item.getModifiedDate()));

        FileOpenHelper.showFileIcon(mContext,item,imageView);
    }
}
