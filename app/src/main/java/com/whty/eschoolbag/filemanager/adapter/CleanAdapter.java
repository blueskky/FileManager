package com.whty.eschoolbag.filemanager.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.CacheCate;
import com.whty.eschoolbag.filemanager.bean.FileList;
import com.whty.eschoolbag.filemanager.ui.category.FileExploreActivity;
import com.whty.eschoolbag.filemanager.util.FileUtil;

import java.util.List;

public class CleanAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    private List<MultiItemEntity> data;


    public CleanAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);
    }


    @Override
    public void setNewData(@Nullable List<MultiItemEntity> data) {
        this.data = data;
        super.setNewData(data);
        expandAll();
    }


    @NonNull
    @Override
    public List<MultiItemEntity> getData() {
        return data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final CacheCate cacheCate = (CacheCate) item;

                helper.setText(R.id.tv_size, FileUtil.formatFileSize(cacheCate.getStorageSize()));
                helper.setText(R.id.tv_size2, FileUtil.formatFileSize(cacheCate.getStorageSize()));
                helper.setText(R.id.tv_count, cacheCate.getCount() + "项");

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        Log.d("lhq", "Level 1 item pos: " + pos);
                        if (cacheCate.isExpanded()) {
                            collapse(pos, false);
                            helper.setImageResource(R.id.iv_arror,R.drawable.bottom_arror_sel);
                        } else {
                            expand(pos, false);
                            helper.setImageResource(R.id.iv_arror,R.drawable.top_arror_sel);
                        }
                    }
                });

                CheckBox checkBox = (CheckBox) helper.getView(R.id.checkbox);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        CacheCate cacheCate1 = (CacheCate) getData().get(0);
                        cacheCate1.setSelect(isChecked);
                        List<FileList> subItems = cacheCate1.getSubItems();

                        if(subItems!=null){
                            for (FileList subItem : subItems) {
                                subItem.setSelect(isChecked);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });


                break;
            case TYPE_LEVEL_1:

                FileList fileList = (FileList) item;
                helper.setText(R.id.tv_subSize, FileUtil.formatFileSize(fileList.getStorageSize()));
                helper.setText(R.id.tv_subTitle, fileList.getCateName());

                CheckBox subCheckBox = (CheckBox) helper.getView(R.id.checkbox);
                subCheckBox.setChecked(fileList.isSelect());

                subCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        fileList.setSelect(isChecked);
                        //                        notifyDataSetChanged();
                    }
                });


                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileExploreActivity.start(mContext, fileList.getList());
                    }
                });


                break;

        }
    }
}
