package com.whty.eschoolbag.filemanager.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.whty.eschoolbag.filemanager.adapter.CleanAdapter;

public class CacheCate extends AbstractExpandableItem<FileList> implements MultiItemEntity {


    private String title;
    private long storageSize;
    private int count;
    private boolean isSelect;


    public CacheCate(String title, int count) {
        this.title = title;
        this.count = count;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(long storageSize) {
        this.storageSize = storageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public int getItemType() {
        return CleanAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}