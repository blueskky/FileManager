package com.whty.eschoolbag.filemanager.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;

import com.whty.eschoolbag.filemanager.Config;
import com.whty.eschoolbag.filemanager.bean.FavoriteItem;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.db.FavoriteDatabaseHelper;
import com.whty.eschoolbag.filemanager.model.SortBean;
import com.whty.eschoolbag.filemanager.util.FileUtil;
import com.whty.eschoolbag.filemanager.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class LoadPresenter {


    String[] defaultColumns = new String[]{Files.FileColumns._ID, Files.FileColumns.DATA, Files.FileColumns.SIZE,
            Files.FileColumns.DATE_MODIFIED, FileColumns.MIME_TYPE

    };
    private OnLoadCallBack callBack;

    public LoadPresenter(OnLoadCallBack callBack) {
        this.callBack = callBack;
    }


    /**
     * 查询 视频文件  过滤不存在文件
     *
     * @param context
     * @param type
     */
    public void loadVideo(Context context, LoadType type) {

        Uri queryUri = MediaStore.Files.getContentUri("external");
        String[] queryColumns = new String[]{FileColumns._ID, FileColumns.DATA, FileColumns.DISPLAY_NAME,
                FileColumns.MIME_TYPE, FileColumns.SIZE, FileColumns.DATE_MODIFIED, "duration"

        };
        //        String selection =
        //                FileColumns.MEDIA_TYPE + "=?" + " AND " + MediaStore.MediaColumns.SIZE + ">0";

        String selection =FileColumns.MEDIA_TYPE + "=?" + " AND " + MediaStore.MediaColumns.SIZE + ">0";

        String[] args = {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};


        new AsyncTask<Void, Integer, ArrayList<FileInfo>>() {
            @Override
            protected ArrayList<FileInfo> doInBackground(Void... voids) {

                //                Uri queryUri = getQueryUri(type);
                //                String[] queryColumns = getQueryColumns(type);
                //                String selection = MediaStore.MediaColumns.SIZE + ">0";
                String order = "datetaken DESC";

                Cursor cursor = context.getContentResolver().query(queryUri, queryColumns, selection, args, order);

                ArrayList<FileInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    FileInfo fileInfo = new FileInfo(cursor.getLong(cursor.getColumnIndex(FileColumns._ID)),
                            cursor.getString(cursor.getColumnIndex(FileColumns.MIME_TYPE)),
                            cursor.getLong(cursor.getColumnIndex(FileColumns.SIZE)),
                            cursor.getLong(cursor.getColumnIndex("duration")),
                            cursor.getString(cursor.getColumnIndex(FileColumns.DISPLAY_NAME)));
                    fileInfo.setFilePath(cursor.getString(cursor.getColumnIndex(FileColumns.DATA)));

                    //                    fileInfo.setModifiedDate(cursor.getLong(cursor
                    //                    .getColumnIndex(FileColumns.DATE_MODIFIED)));

                    list.add(fileInfo);
                }
                cursor.close();

                filterNotExitsFile(list);
                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<FileInfo> fileInfos) {
                super.onPostExecute(fileInfos);
                callBack.onLoadFinish(fileInfos);
            }
        }.execute();
    }


    /**
     * 查询 音频文件  过滤不存在文件
     *
     * @param context
     */
    public void loadAudio(Context context) {

        Uri uri = MediaStore.Audio.Media.getContentUri("external");
        String[] columns = new String[]{FileColumns._ID, FileColumns.DATA, FileColumns.DISPLAY_NAME,
                FileColumns.MIME_TYPE, FileColumns.SIZE, FileColumns.DATE_MODIFIED, "duration"

        };
        //        String selection =
        //                MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
        //                        + " AND " + MediaStore.MediaColumns.SIZE + ">0";

                String selection = MediaStore.MediaColumns.SIZE + ">0";
        //        String[] args = {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};
        String order = "datetaken DESC";


        new AsyncTask<Void, Integer, ArrayList<FileInfo>>() {
            @Override
            protected ArrayList<FileInfo> doInBackground(Void... voids) {

                Cursor cursor = context.getContentResolver().query(uri, columns, selection, null, null);

                ArrayList<FileInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    FileInfo fileInfo =
                            new FileInfo(cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                                    cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                                    cursor.getLong(cursor.getColumnIndex("duration")),
                                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)));

                    fileInfo.setFilePath(cursor.getString(cursor.getColumnIndex(FileColumns.DATA)));
                    //                    fileInfo.setModifiedDate(cursor.getLong(cursor
                    //                    .getColumnIndex(FileColumns.DATE_MODIFIED)));
                    list.add(fileInfo);
                }

                cursor.close();
                filterNotExitsFile(list);
                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<FileInfo> fileInfos) {
                super.onPostExecute(fileInfos);
                callBack.onLoadFinish(fileInfos);
            }
        }.execute();
    }


    /**
     * 查询 文档文件  过滤不存在文件
     *
     * @param context
     */
    public void loadDocFile(Context context) {
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] columns = new String[]{FileColumns._ID, FileColumns.DATA, FileColumns.DISPLAY_NAME,
                FileColumns.MIME_TYPE, FileColumns.SIZE, FileColumns.DATE_MODIFIED,

        };

        String selection = MediaStore.MediaColumns.SIZE + ">0" + " AND " + buildDocSelection();
//        String selection = buildDocSelection();
        //        String[] args = {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};
        String order = "datetaken DESC";


        new AsyncTask<Void, Integer, ArrayList<FileInfo>>() {
            @Override
            protected ArrayList<FileInfo> doInBackground(Void... voids) {

                Cursor cursor = context.getContentResolver().query(uri, columns, selection, null, null);

                ArrayList<FileInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setDbId(cursor.getLong(cursor.getColumnIndex(FileColumns._ID)));
                    fileInfo.setMimeType(cursor.getString(cursor.getColumnIndex(FileColumns.MIME_TYPE)));
                    fileInfo.setFileSize(cursor.getLong(cursor.getColumnIndex(FileColumns.SIZE)));
                    String name = cursor.getString(cursor.getColumnIndex(FileColumns.DISPLAY_NAME));
                    fileInfo.setFilePath(cursor.getString(cursor.getColumnIndex(FileColumns.DATA)));
                    fileInfo.setFileName(Util.getNameFromFilepath(fileInfo.getFilePath()));
                    //                    fileInfo.setModifiedDate(cursor.getLong(cursor
                    //                    .getColumnIndex(FileColumns.DATE_MODIFIED)));
                    list.add(fileInfo);
                }
                cursor.close();
                filterNotExitsFile(list);
                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<FileInfo> fileInfos) {
                super.onPostExecute(fileInfos);
                callBack.onLoadFinish(fileInfos);
            }
        }.execute();
    }


    /**
     * 查询 最近7天文件  过滤不存在文件
     *
     * @param context
     */
    public void loadRecentFiles(Context context) {

        Uri uri = MediaStore.Files.getContentUri("external");
        final String[] columns = {FileColumns.DATA, FileColumns.DISPLAY_NAME, FileColumns.MEDIA_TYPE,
                FileColumns.MIME_TYPE, FileColumns.SIZE, FileColumns.DATE_MODIFIED};
        String order = "date_modified DESC";

        String selection = MediaStore.MediaColumns.SIZE + ">0";
        //        String[] args = {String.valueOf(FileColumns.MEDIA_TYPE_IMAGE)};

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - 7);
        Date d = c.getTime();

        AsyncTask<Void, Integer, ArrayList<FileInfo>> asyncTask = new AsyncTask<Void, Integer, ArrayList<FileInfo>>() {
            @Override
            protected ArrayList<FileInfo> doInBackground(Void... voids) {

                ArrayList<FileInfo> recentFiles = new ArrayList<>();

                Cursor cursor = context.getContentResolver().query(uri, columns, selection, null, order);
                if (cursor == null) return recentFiles;
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    do {
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                        File f = new File(path);
                        if (d.compareTo(new Date(f.lastModified())) != 1 && !f.isDirectory()) {

                            FileInfo fileInfo = new FileInfo();
                            fileInfo.setFilePath(path);
                            fileInfo.setMediaType(cursor.getString(cursor.getColumnIndex(FileColumns.MEDIA_TYPE)));
                            fileInfo.setMimeType(cursor.getString(cursor.getColumnIndex(FileColumns.MIME_TYPE)));
                            fileInfo.setFileSize(cursor.getLong(cursor.getColumnIndex(FileColumns.SIZE)));
                            fileInfo.setFileName(cursor.getString(cursor.getColumnIndex(FileColumns.DISPLAY_NAME)));
                            fileInfo.setModifiedDate(cursor.getLong(cursor.getColumnIndex(FileColumns.DATE_MODIFIED)) * 1000);

                            recentFiles.add(fileInfo);
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
                Collections.sort(recentFiles,
                        (lhs, rhs) -> -1 * Long.valueOf(lhs.getModifiedDate()).compareTo(rhs.getModifiedDate()));
                if (recentFiles.size() > 20) {
                    //                    for (int i = recentFiles.size() - 1; i > 20; i--) {
                    //                        recentFiles.remove(i);
                    //                    }
                }

                filterNotExitsFile(recentFiles);
                return filterFile(recentFiles);
            }

            @Override
            protected void onPostExecute(ArrayList<FileInfo> fileInfos) {
                super.onPostExecute(fileInfos);
                callBack.onLoadFinish(fileInfos);
            }
        };
        asyncTask.execute();
    }


    private ArrayList<FileInfo> filterFile(ArrayList<FileInfo> recentFiles) {
        Iterator<FileInfo> iterator = recentFiles.iterator();
        while (iterator.hasNext()) {
            FileInfo fileInfo = iterator.next();
            String mediaType = fileInfo.getMediaType();
            String extensionName = FileUtil.getExtensionName(fileInfo.getFilePath());

            if (mediaType.equals(String.valueOf(FileColumns.MEDIA_TYPE_IMAGE)) || mediaType.equals(String.valueOf(FileColumns.MEDIA_TYPE_VIDEO)) || mediaType.equals(String.valueOf(FileColumns.MEDIA_TYPE_AUDIO)) || FileUtil.WPS_FORFATS.contains(extensionName) || FileUtil.AUDIO_FORFATS.contains(extensionName) || FileUtil.VIDEO_FORFATS.contains(extensionName)) {

            } else {
                iterator.remove();
            }
        }

        return recentFiles;
    }


    private String buildDocSelection() {
        StringBuilder selection = new StringBuilder();
        Iterator<String> iter = Util.sDocMimeTypesSet.iterator();
        while (iter.hasNext()) {
            selection.append("(" + FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
        }
        return selection.substring(0, selection.lastIndexOf(")") + 1);
    }

    private String[] getQueryColumns(LoadType type) {

        String[] columns = defaultColumns;
        switch (type) {
            case IMAGE:
                break;
            case VIDEO:
                columns = new String[]{FileColumns._ID, FileColumns.DATA, FileColumns.DISPLAY_NAME,
                        FileColumns.MIME_TYPE, FileColumns.SIZE, FileColumns.DATE_MODIFIED, "duration"};
                break;
            case AUDIO:
                columns = new String[]{FileColumns._ID, FileColumns.DATA, FileColumns.DISPLAY_NAME,
                        FileColumns.MIME_TYPE, FileColumns.SIZE, FileColumns.DATE_MODIFIED, "duration"};
                break;
            case DOC:
                columns = new String[]{FileColumns._ID, FileColumns.DATA, FileColumns.DISPLAY_NAME,
                        FileColumns.MIME_TYPE, FileColumns.DATE_MODIFIED, FileColumns.SIZE,};
                break;

        }

        return columns;
    }

    private Uri getQueryUri(LoadType type) {
        Uri uri;
        switch (type) {
            case IMAGE:
                uri = MediaStore.Images.Media.getContentUri("external");
                break;
            case VIDEO:
                uri = MediaStore.Video.Media.getContentUri("external");
                break;
            case AUDIO:
                uri = MediaStore.Audio.Media.getContentUri("external");
                break;
            case DOC:
                uri = MediaStore.Files.getContentUri("external");
                break;
            default:
                uri = MediaStore.Files.getContentUri("external");
                break;

        }

        return uri;
    }

    private String buildSortOrder(SortBean sort) {
        String sortOrder = null;
        int sortType = sort.getSortType();
        switch (sortType) {
            case 1:
                if (sort.getAsc() == 1) {
                    sortOrder = FileColumns.TITLE + " asc";

                } else {
                    sortOrder = FileColumns.TITLE + " desc";
                }
                break;
            case 2:

                if (sort.getAsc() == 1) {
                    sortOrder = FileColumns.SIZE + " asc";

                } else {
                    sortOrder = FileColumns.SIZE + " desc";
                }

                break;
            case 3:

                if (sort.getAsc() == 1) {
                    sortOrder = FileColumns.DATE_MODIFIED + " asc";

                } else {
                    sortOrder = FileColumns.DATE_MODIFIED + " desc";
                }

                break;
            case 0:

                if (sort.getAsc() == 1) {
                    sortOrder = FileColumns.MIME_TYPE + " asc, " + FileColumns.TITLE + " asc";

                } else {
                    sortOrder = FileColumns.MIME_TYPE + " desc, " + FileColumns.TITLE + " desc";
                }


                break;
        }
        return sortOrder;
    }

    /**
     * 查询 图片文件  过滤不存在文件
     *
     * @param context
     * @param
     * @param sort
     */
    public void loadBucketImage(Context context, String bucketName, SortBean sort) {
        Uri uri = MediaStore.Images.Media.getContentUri("external");
        String[] columns = new String[]{FileColumns._ID, FileColumns.DATA, FileColumns.DISPLAY_NAME,
                FileColumns.MIME_TYPE, FileColumns.SIZE, FileColumns.DATE_MODIFIED,

        };

        //        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
        //                + " AND "
        //                + " bucket_id=?"
        //                + " AND " + MediaStore.MediaColumns.SIZE + ">0";

        //        String selection = "bucket_display_name=?" + " AND " + MediaStore.MediaColumns.SIZE + ">0";

        //        String selection = "bucket_display_name=?";

        String selection = "bucket_display_name=?" + " AND " + MediaStore.MediaColumns.SIZE + ">0";
        String[] args = new String[]{bucketName};

        String sortOrder = buildSortOrder(sort);

        if (bucketName.equals("All")) {
            //            selection = MediaStore.MediaColumns.SIZE + ">0";
            selection = null;
            args = null;
        }

        String finalSelection = selection;
        String[] finalArgs = args;
        new AsyncTask<Void, Integer, ArrayList<FileInfo>>() {
            @Override
            protected ArrayList<FileInfo> doInBackground(Void... voids) {

                Cursor cursor = context.getContentResolver().query(uri, columns, finalSelection, finalArgs, sortOrder);

                ArrayList<FileInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setDbId(cursor.getLong(cursor.getColumnIndex(FileColumns._ID)));
                    fileInfo.setMimeType(cursor.getString(cursor.getColumnIndex(FileColumns.MIME_TYPE)));
                    fileInfo.setFileSize(cursor.getLong(cursor.getColumnIndex(FileColumns.SIZE)));
                    fileInfo.setFilePath(cursor.getString(cursor.getColumnIndex(FileColumns.DATA)));
                    fileInfo.setFileName(cursor.getString(cursor.getColumnIndex(FileColumns.DISPLAY_NAME)));
                    fileInfo.setModifiedDate(cursor.getLong(cursor.getColumnIndex(FileColumns.DATE_MODIFIED)));
                    list.add(fileInfo);
                }

                cursor.close();
                filterNotExitsFile(list);
                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<FileInfo> fileInfos) {
                super.onPostExecute(fileInfos);
                callBack.onLoadFinish(fileInfos);
            }
        }.execute();
    }


    /**
     * 查询 图片文件  过滤不存在文件
     *
     * @param context
     * @param
     * @param sort
     */
    public void loadBucketImageWithId(Context context, String bucketId, SortBean sort) {
        Uri uri = MediaStore.Images.Media.getContentUri("external");
        String[] columns = new String[]{FileColumns._ID, FileColumns.DATA, FileColumns.DISPLAY_NAME,
                FileColumns.MIME_TYPE, FileColumns.SIZE, FileColumns.DATE_MODIFIED,

        };

        //        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
        //                + " AND "
        //                + " bucket_id=?"
        //                + " AND " + MediaStore.MediaColumns.SIZE + ">0";

        //        String selection = "bucket_display_name=?" + " AND " + MediaStore.MediaColumns.SIZE + ">0";

        //        String selection = "bucket_display_name=?";

        String selection = "bucket_id=? AND "+MediaStore.MediaColumns.SIZE+">0";
        String[] args = new String[]{bucketId};

        String sortOrder = buildSortOrder(sort);

        //全部的图册
        if (bucketId.equals("-1")) {
            //            selection = MediaStore.MediaColumns.SIZE + ">0";
            selection = null;
            args = null;
        }

        String finalSelection = selection;
        String[] finalArgs = args;
        new AsyncTask<Void, Integer, ArrayList<FileInfo>>() {
            @Override
            protected ArrayList<FileInfo> doInBackground(Void... voids) {

                Cursor cursor = context.getContentResolver().query(uri, columns, finalSelection, finalArgs, sortOrder);

                ArrayList<FileInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setDbId(cursor.getLong(cursor.getColumnIndex(FileColumns._ID)));
                    fileInfo.setMimeType(cursor.getString(cursor.getColumnIndex(FileColumns.MIME_TYPE)));
                    fileInfo.setFileSize(cursor.getLong(cursor.getColumnIndex(FileColumns.SIZE)));
                    fileInfo.setFilePath(cursor.getString(cursor.getColumnIndex(FileColumns.DATA)));
                    fileInfo.setFileName(cursor.getString(cursor.getColumnIndex(FileColumns.DISPLAY_NAME)));
                    fileInfo.setModifiedDate(cursor.getLong(cursor.getColumnIndex(FileColumns.DATE_MODIFIED)));
                    list.add(fileInfo);
                }

                cursor.close();
                filterNotExitsFile(list);
                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<FileInfo> fileInfos) {
                super.onPostExecute(fileInfos);
                callBack.onLoadFinish(fileInfos);
            }
        }.execute();
    }


    /**
     * 收藏文件
     *
     * @param context
     * @param listener
     */
    public void loadFavoriteFile(Context context, FavoriteDatabaseHelper.FavoriteDatabaseListener listener) {

        new AsyncTask<Void, Integer, ArrayList<FileInfo>>() {
            @Override
            protected ArrayList<FileInfo> doInBackground(Void... voids) {

                FavoriteDatabaseHelper databaseHelper = new FavoriteDatabaseHelper(context, listener);

                ArrayList<FileInfo> list = new ArrayList<>();
                Cursor c = databaseHelper.query();
                if (c != null) {
                    while (c.moveToNext()) {
                        FavoriteItem item = new FavoriteItem(c.getLong(0), c.getString(1), c.getString(2));
                        FileInfo fileInfo = Util.GetFileInfo(item.location);
                        if (fileInfo != null) {
                            fileInfo.setDbId(item.id);
                            list.add(fileInfo);
                        }

                    }
                    c.close();
                }

                // remove not existing items
                filterNotExitsFile(list);

                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<FileInfo> fileInfos) {
                super.onPostExecute(fileInfos);
                if (callBack != null) {
                    callBack.onLoadFinish(fileInfos);
                }
            }
        }.execute();


    }

    /**
     * 互动课堂文件
     */
    public void loadClassFile() {
        new AsyncTask<Void, Integer, ArrayList<FileInfo>>() {
            @Override
            protected ArrayList<FileInfo> doInBackground(Void... voids) {

                String basePath = Config.ClASS_FILE;

                ArrayList<FileInfo> childFileList = listFiles(basePath);

                filterNotExitsFile(childFileList);
                return childFileList;
            }

            @Override
            protected void onPostExecute(ArrayList<FileInfo> fileInfos) {
                super.onPostExecute(fileInfos);
                if (callBack != null) {
                    callBack.onLoadFinish(fileInfos);
                }
            }
        }.execute();


    }

    /**
     * 下载的文件
     */
    public void loadDownloadFile() {


        ArrayList<FileInfo> list = new ArrayList<>();
        list.addAll(listFiles(Config.downloadpath1));
        list.addAll(listFiles(Config.downloadpath2));

        ArrayList<FileInfo> folderList = new ArrayList<>();
        ArrayList<FileInfo> filelist = new ArrayList<>();
        for (FileInfo fileInfo : list) {
            if (fileInfo.isDir()) {
                folderList.add(fileInfo);
            } else {
                filelist.add(fileInfo);
            }
        }
        list.clear();
        list.addAll(folderList);
        list.addAll(filelist);


        filterNotExitsFile(list);
        if (callBack != null) {
            callBack.onLoadFinish(list);
        }

    }


    /**
     * 搜索文件
     *
     * @param content
     */
    public void searchFile(Context context, String content) {
        Uri contentUri = Files.getContentUri("external");

        String[] columns = new String[]{FileColumns._ID, FileColumns.DATA, FileColumns.DISPLAY_NAME,
                FileColumns.MIME_TYPE, FileColumns.SIZE, FileColumns.DATE_MODIFIED,

        };

        //        String selection = String.format(FileColumns.DISPLAY_NAME + " LIKE '%s'",
        //        content);
        String selection = FileColumns.TITLE + " LIKE '%" + content + "%'";

        new AsyncTask<Void, Integer, ArrayList<FileInfo>>() {
            @Override
            protected ArrayList<FileInfo> doInBackground(Void... voids) {

                Cursor cursor = context.getContentResolver().query(contentUri, columns, selection, null, null);

                ArrayList<FileInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setDbId(cursor.getLong(cursor.getColumnIndex(FileColumns._ID)));
                    fileInfo.setMimeType(cursor.getString(cursor.getColumnIndex(FileColumns.MIME_TYPE)));
                    fileInfo.setFileSize(cursor.getLong(cursor.getColumnIndex(FileColumns.SIZE)));
                    String name = cursor.getString(cursor.getColumnIndex(FileColumns.DISPLAY_NAME));
                    fileInfo.setFilePath(cursor.getString(cursor.getColumnIndex(FileColumns.DATA)));
                    fileInfo.setFileName(Util.getNameFromFilepath(fileInfo.getFilePath()));
                    //                    fileInfo.setModifiedDate(cursor.getLong(cursor
                    //                    .getColumnIndex(FileColumns.DATE_MODIFIED)));
                    list.add(fileInfo);
                }

                cursor.close();
                filterNotExitsFile(list);
                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<FileInfo> fileInfos) {
                super.onPostExecute(fileInfos);
                callBack.onLoadFinish(fileInfos);
            }
        }.execute();

    }


    /**
     * 根据路径获取路径下所有文件
     *
     * @param path
     */
    private ArrayList<FileInfo> listFiles(String path) {
        File rootFile = new File(path);
        ArrayList<FileInfo> list = new ArrayList<>();
        if (rootFile.exists() && rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            for (File file : files) {
                FileInfo info = new FileInfo();
                info.setFilePath(file.getAbsolutePath());
                info.setFileName(file.getName());
                info.setFileSize(file.length());
                info.setModifiedDate(file.lastModified());
                info.setDir(file.isDirectory());
                list.add(info);
            }
        }

        return list;
    }


    /**
     * // remove not existing items
     *
     * @param list
     */
    private void filterNotExitsFile(ArrayList<FileInfo> list) {
        if (list != null && list.size() > 0) {
            Iterator<FileInfo> iterator = list.iterator();
            while (iterator.hasNext()) {
                FileInfo next = iterator.next();
                File file = new File(next.getFilePath());
                if (file == null || !file.exists()||file.length()==0) {
                    iterator.remove();
                }
            }
        }
    }


    public interface OnLoadCallBack {
        void onLoadFinish(ArrayList<FileInfo> list);
    }

}
