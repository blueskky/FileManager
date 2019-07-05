package com.whty.eschoolbag.filemanager.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.whty.eschoolbag.filemanager.adapter.CleanAdapter;
import com.whty.eschoolbag.filemanager.presenter.core.FileTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoxw on 2016/8/10.
 */

public class FileList implements MultiItemEntity {

    private String cateName;
    private long storageSize;
    private boolean isSelect;
    private List<File> list=new ArrayList<>();


    public FileList(String cateName,boolean isSelect){
        this.cateName=cateName;
        this.isSelect=isSelect;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public long getStorageSize() {
        long totalSize = FileTools.getTotalSize(getList());
        return totalSize;
    }

    public void setStorageSize(long storageSize) {
        this.storageSize = storageSize;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public List<File> getList() {
        return list;
    }

    public void setList(List<File> list) {
        this.list = list;
    }

    public FileList() {
    }


    @Override
    public int getItemType() {
        return CleanAdapter.TYPE_LEVEL_1;
    }

}