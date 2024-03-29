package com.whty.eschoolbag.filemanager.presenter.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/*
FileTools类封装了文件相关操作以及文件类型和后缀名
 */
public class FileTools {
    //文件类型
    public final static int MUSIC = 0;//音乐
    public final static int VIDEO = 1;//视频
    public final static int DOCUMENT = 2;//文档
    public final static int IMAGE = 3;//图片
    public final static int ALL_FILES = 4;//所有文件
    public final static int APK = 5;//安装包
    public final static int CACHE_DIR = 6;//缓存文件夹
    public final static int TEMP_DIR = 7;//临时文件夹
    public final static int LOG_FILES = 8;//日志文件
    public final static int LARGE_FILES = 9;//所有类型的文件

    public final static int CUSTOM = 10;//所有类型的文件

    //文件后缀名
    public final static String[] musicSuffix = {".mp3", ".wma", ".ogg"};//音频文件后缀名
    public final static String[] videoSuffix = {".mp4", "mkv", "avi", "flv"};//视频文件后缀名
    public final static String[] documentSuffix = {".txt", ".doc", ".docx"};//文本文件后缀名
    public final static String[] imageSuffix = {".jpg", ".gif", ".png"};//图片文件后缀名
    public final static String[] apkSuffix = {".apk"};//应用程序安装包后缀名
    public final static String[] cacheDirName = {"cache", ".cache"};//缓存文件夹常用名
    public final static String[] tempDirName = {"tmp"};//临时文件夹常用名
    public final static String[] logSuffix = {".log", "log.txt", "Log.txt", "log1.txt"};//日志文件名
    public final static String[] largeFilesSuffix = {""};//后缀名无限制


    /*
    获取dir的总大小
    dir是文件时，返回文件大小
    dir是目录时，对dir内的文件递归调用本方法
    */
    public static long getTotalSize(File dir) {
        long totalSize = 0;
        if (dir.isFile()) {
            totalSize = dir.length();
            return dir.length();
        } else if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null) {
                for (File child : children)
                    totalSize += getTotalSize(child);
            }
        }
        return totalSize;
    }

    //获取File[] 内所有文件总大小
    public static long getTotalSize(List<File> files) {
        long totalSize = 0;
        for (File file : files) {
            totalSize += getTotalSize(file);
        }
        return totalSize;
    }

    //获取文件类型方法，通过文件名是否与后缀名字符串相同判断。
    public static int getFileType(File file) {
        for (String suffix : FileTools.musicSuffix) {
            if (file.getName().endsWith(suffix))
                return FileTools.MUSIC;
        }
        for (String suffix : FileTools.videoSuffix) {
            if (file.getName().endsWith(suffix))
                return FileTools.VIDEO;
        }
        for (String suffix : FileTools.documentSuffix) {
            if (file.getName().endsWith(suffix))
                return FileTools.DOCUMENT;
        }
        for (String suffix : FileTools.imageSuffix) {
            if (file.getName().endsWith(suffix))
                return FileTools.IMAGE;
        }
        for (String suffix : FileTools.apkSuffix) {
            if (file.getName().endsWith(suffix))
                return FileTools.APK;
        }
        //未知类型返回-1
        return -1;
    }

    //打开文件，判断文件类型，根据文件类型调用不同的打开方法
    public static void openFile(File file, Context mContext) {
        int fileType = getFileType(file);
        switch (fileType) {
            case MUSIC:
                openMusicFile(file, mContext);
                break;
            case VIDEO:
                openVideoFile(file, mContext);
                break;
            case DOCUMENT:
                openDocumentFile(file, mContext);
                break;
            case IMAGE:
                openImageFile(file, mContext);
                break;
            case APK:
                openApkFile(file, mContext);
                break;
            default:
                Toast.makeText(mContext, "未知的文件类型", Toast.LENGTH_SHORT).show();
                break;

        }
        Log.i("open file", "open:" + file.getName() +
                "##path:" + file.getAbsolutePath());

    }

    //打开图片文件
    public static void openImageFile(File file, Context mContext) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        mContext.startActivity(intent);
    }

    //打开文本文件
    public static void openDocumentFile(File file, Context mContext) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "text/plain");
        mContext.startActivity(intent);
    }

    //打开视频文件
    public static void openVideoFile(File file, Context mContext) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "video/*");
        mContext.startActivity(intent);
    }

    //打开音频文件
    public static void openMusicFile(File file, Context mContext) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "audio/*");
        mContext.startActivity(intent);
    }

    //打开apk文件
    public static void openApkFile(File file, Context mContext) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

}
