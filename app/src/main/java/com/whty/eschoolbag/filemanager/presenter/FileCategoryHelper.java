package com.whty.eschoolbag.filemanager.presenter;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;

import com.whty.eschoolbag.filemanager.util.Util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Iterator;

public class FileCategoryHelper {

    private static final String LOG_TAG = "FileCategoryHelper";

    private static String APK_EXT = "apk";
    private static String THEME_EXT = "mtz";
    private static String[] ZIP_EXTS = new String[]{"zip", "rar"};

    public static HashMap<FileCategory, FilenameExtFilter> filters = new HashMap<FileCategory,
            FilenameExtFilter>();
    private FileCategory mCategory;
    private Context mContext;

    public FileCategoryHelper(Context context) {
        mContext = context;
    }


    public void setCustomCategory(String[] exts) {
        mCategory = FileCategory.Custom;
        if (filters.containsKey(FileCategory.Custom)) {
            filters.remove(FileCategory.Custom);
        }

        filters.put(FileCategory.Custom, new FilenameExtFilter(exts));
    }

    public FilenameFilter getFilter() {
        return filters.get(mCategory);
    }


    /**
     * 根据类别查询文件size
     *
     * @param category
     * @return
     */
    public CategoryInfo queryCategoryFileSize(FileCategory category) {
        Uri uri = getContentUriByCategory(category);
        CategoryInfo categoryInfo = refreshMediaCategory(category, uri);
        return categoryInfo;
    }

    public Cursor query(FileCategory fc, FileSortHelper.SortMethod sort) {
        Uri uri = getContentUriByCategory(fc);
        String selection = buildSelectionByCategory(fc);
        String sortOrder = buildSortOrder(sort);

        if (uri == null) {
            Log.e(LOG_TAG, "invalid uri, category:" + fc.name());
            return null;
        }

        String[] columns = new String[]{FileColumns._ID, FileColumns.DATA, FileColumns.SIZE,
                FileColumns.DATE_MODIFIED};

        return mContext.getContentResolver().query(uri, columns, selection, null, sortOrder);
    }


    /**
     * 根据文件路径判别文件
     *
     * @param path
     * @return
     */
    public static FileCategory getCategoryFromPath(String path) {
        MediaFile.MediaFileType type = MediaFile.getFileType(path);
        if (type != null) {
            if (MediaFile.isAudioFileType(type.fileType)) return FileCategory.Music;
            if (MediaFile.isVideoFileType(type.fileType)) return FileCategory.Video;
            if (MediaFile.isImageFileType(type.fileType)) return FileCategory.Picture;
            if (Util.sDocMimeTypesSet.contains(type.mimeType)) return FileCategory.Doc;
        }

        int dotPosition = path.lastIndexOf('.');
        if (dotPosition < 0) {
            return FileCategory.Other;
        }

        String ext = path.substring(dotPosition + 1);
        if (ext.equalsIgnoreCase(APK_EXT)) {
            return FileCategory.Apk;
        }
        if (ext.equalsIgnoreCase(THEME_EXT)) {
            return FileCategory.Theme;
        }

        if (matchExts(ext, ZIP_EXTS)) {
            return FileCategory.Zip;
        }

        return FileCategory.Other;
    }


    private String buildDocSelection() {
        StringBuilder selection = new StringBuilder();
        Iterator<String> iter = Util.sDocMimeTypesSet.iterator();
        while (iter.hasNext()) {
            selection.append("(" + FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
        }
        return selection.substring(0, selection.lastIndexOf(")") + 1);
    }

    private String buildSelectionByCategory(FileCategory cat) {
        String selection = null;
        switch (cat) {
            case Theme:
                selection = FileColumns.DATA + " LIKE '%.mtz'";
                break;
            case Doc:
                selection = buildDocSelection();
                break;
            case Zip:
                selection = "(" + FileColumns.MIME_TYPE + " == '" + Util.sZipFileMimeType + "')";
                break;
            case Apk:
                selection = FileColumns.DATA + " LIKE '%.apk'";
                break;
            default:
                selection = null;
        }
        return selection;
    }

    private Uri getContentUriByCategory(FileCategory cat) {
        Uri uri;
        String volumeName = "external";
        switch (cat) {
            case Theme:
            case Doc:
            case Zip:
            case Apk:
                uri = Files.getContentUri(volumeName);
                break;
            case Music:
                uri = Audio.Media.getContentUri(volumeName);
                break;
            case Video:
                uri = Video.Media.getContentUri(volumeName);
                break;
            case Picture:
                uri = Images.Media.getContentUri(volumeName);
                break;
            default:
                uri = null;
        }
        return uri;
    }

    private String buildSortOrder(FileSortHelper.SortMethod sort) {
        String sortOrder = null;
        switch (sort) {
            case name:
                sortOrder = FileColumns.TITLE + " asc";
                break;
            case size:
                sortOrder = FileColumns.SIZE + " asc";
                break;
            case date:
                sortOrder = FileColumns.DATE_MODIFIED + " desc";
                break;
            case type:
                sortOrder = FileColumns.MIME_TYPE + " asc, " + FileColumns.TITLE + " asc";
                break;
        }
        return sortOrder;
    }


    private CategoryInfo refreshMediaCategory(FileCategory fc, Uri uri) {
        CategoryInfo fileInfo = new CategoryInfo();
        String[] columns = new String[]{FileColumns.DATA, FileColumns.SIZE};
        Cursor c = mContext.getContentResolver().query(uri, columns, buildSelectionByCategory(fc)
                , null, null);
        if (c == null) {
            Log.e(LOG_TAG, "fail to query uri:" + uri);
            return fileInfo;
        }

        while (c.moveToNext()) {
            String filePath = c.getString(c.getColumnIndex(FileColumns.DATA));
            long fileSize = c.getLong(c.getColumnIndex(FileColumns.SIZE));
            File file = new File(filePath);
            if (file != null && file.exists()&&file.length()>0) {
                fileInfo.count += 1;
                fileInfo.size += fileSize;
            }
        }

        c.close();
        return fileInfo;
    }


    private static boolean matchExts(String ext, String[] exts) {
        for (String ex : exts) {
            if (ex.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }


    //================================================================


    public class CategoryInfo {
        public long count;

        public long size;
    }

    public enum FileCategory {
        All, Music, Video, Picture, Theme, Doc, Zip, Apk, Custom, Other, Favorite
    }
}
