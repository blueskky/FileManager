package com.whty.eschoolbag.filemanager.model;

import java.io.File;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/13 9:45
 * description:
 */
public class ScanCate {


    private String name;
    private File root;
    private int type;
    private boolean isSelect;


    public ScanCate(File root, String name, int type,boolean isSelect) {
        this.root = root;
        this.name=name;
        this.type = type;
        this.isSelect=isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getRoot() {
        return root;
    }

    public void setRoot(File root) {
        this.root = root;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
