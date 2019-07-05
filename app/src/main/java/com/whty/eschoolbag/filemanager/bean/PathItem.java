package com.whty.eschoolbag.filemanager.bean;

public class PathItem {

    private String pathName;

    private String path;

    private boolean root;

    public PathItem(String pathName, String path) {
        this.pathName = pathName;
        this.path = path;
    }


    public PathItem(String pathName, String path,boolean isRoot) {
        this.pathName = pathName;
        this.path = path;
        this.root=isRoot;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
