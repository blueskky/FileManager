package com.whty.eschoolbag.filemanager.model;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/20 16:52
 * description:
 */
public class SortBean {

    //SORT_CATEGORY(0), SORT_NAME(1), SORT_SIZE(2), SORT_TIME(3);

    //  asc 1    des -1

    private int asc;
    private int sortType;


    public SortBean(int asc, int sortType) {
        this.asc = asc;
        this.sortType = sortType;
    }

    public int getAsc() {
        return asc;
    }

    public void setAsc(int asc) {
        this.asc = asc;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }
}
