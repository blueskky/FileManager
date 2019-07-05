package com.whty.eschoolbag.filemanager.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/17 17:05
 * description:
 */
public class HeadItem {


    private String mediaType;
    private List<FileInfo> list=new ArrayList<>();
    private long timeStamp;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public List<FileInfo> getList() {
        return list;
    }

    public void addFileInfo(FileInfo fileInfo) {
        this.list .add(fileInfo);
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
