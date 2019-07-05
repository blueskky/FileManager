package com.whty.eschoolbag.filemanager.util;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/18 15:08
 * description:
 */
public class TimeUtils {


    /**
     *      * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *      
     */

    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / 1000;
        long time = curTime - timeStamp;
        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "";
        }


    }
}
