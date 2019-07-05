package com.whty.eschoolbag.filemanager.bean;

public enum Sort {

    SORT_CATEGORY(0), SORT_NAME(1), SORT_SIZE(2), SORT_TIME(3);

    int value;

    Sort(int value){
        this.value=value;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
