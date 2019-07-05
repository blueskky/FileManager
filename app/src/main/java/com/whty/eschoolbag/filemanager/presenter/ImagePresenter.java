package com.whty.eschoolbag.filemanager.presenter;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.whty.eschoolbag.filemanager.model.Album;
import com.whty.eschoolbag.filemanager.viewinterface.ILoadView;

import java.io.File;
import java.util.ArrayList;

public class ImagePresenter {

    ILoadView loadView;

    public ImagePresenter(ILoadView loadView) {
        this.loadView = loadView;
    }

    public void loadAlbum(Context context) {

        String COLUMN_COUNT = "count";
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] columns = {MediaStore.Files.FileColumns._ID, "bucket_id", "bucket_display_name",
                MediaStore.MediaColumns.DATA, "COUNT(*) AS count"};


        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " AND " + MediaStore.MediaColumns.SIZE +
                ">0" + ") GROUP BY (bucket_id";

        String[] args = {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)};

        String sortOrder = "datetaken DESC";


        final String[] COLUMNS = {MediaStore.Files.FileColumns._ID, "bucket_id", "bucket_display_name",
                MediaStore.MediaColumns.DATA, COLUMN_COUNT};


        new AsyncTask<Void, Integer, ArrayList<Album>>() {
            @Override
            protected ArrayList<Album> doInBackground(Void... voids) {

                Cursor albums = context.getContentResolver().query(uri, columns, selection, args, sortOrder);

                if (true) {
                    ArrayList<Album> list = new ArrayList<>();
                    while (albums.moveToNext()) {
                        String bucket_id = albums.getString(albums.getColumnIndex("bucket_id"));
                        String bucket_display_name = albums.getString(albums.getColumnIndex("bucket_display_name"));

                        Uri uri = MediaStore.Files.getContentUri("external");
                        String[] columns = {MediaStore.Files.FileColumns._ID, MediaStore.MediaColumns.DATA};
                        String selection =
                                MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " AND bucket_id=?";

                        String[] args = {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE), bucket_id};
                        Cursor cursor = context.getContentResolver().query(uri, columns, selection, args, null);


                        String bucketCover = null;
                        int count = 0;

                        while (cursor.moveToNext()) {
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                            File file = new File(path);
                            if (file.exists()&&file.length()>0) {
                                if (bucketCover == null) {
                                    bucketCover = path;
                                }
                                count++;
                            }
                        }

                        Album album = new Album(bucket_id, bucketCover, bucket_display_name, count);
                        list.add(album);
                    }

                    //和并 添加全部项
                    if (list.size() > 0) {
                        Album all = new Album("-1", list.get(0).getCoverPath(), "All", 0);
                        int allCount = 0;
                        for (Album album1 : list) {
                            allCount += album1.getCount();
                        }
                        all.setCount(allCount);

                        list.add(0,all);
                    }
                    return list;

                } else {

                    //虚构cursor
                    MatrixCursor allAlbum = new MatrixCursor(COLUMNS);
                    int totalCount = 0;
                    String allAlbumCoverPath = "";
                    if (albums != null) {
                        while (albums.moveToNext()) {
                            totalCount += albums.getInt(albums.getColumnIndex(COLUMN_COUNT));
                        }
                        if (albums.moveToFirst()) {
                            allAlbumCoverPath = albums.getString(albums.getColumnIndex(MediaStore.MediaColumns.DATA));
                        }
                    }


                    allAlbum.addRow(new String[]{Album.ALBUM_ID_ALL, Album.ALBUM_ID_ALL, Album.ALBUM_NAME_ALL,
                            allAlbumCoverPath, String.valueOf(totalCount)});

                    //合并结果集
                    MergeCursor mergeCursor = new MergeCursor(new Cursor[]{allAlbum, albums});

                    ArrayList<Album> list = new ArrayList<>();
                    while (mergeCursor.moveToNext()) {
                        Album album = Album.valueOf(mergeCursor);
                        list.add(album);
                    }

                    return list;
                }

            }

            @Override
            protected void onPostExecute(ArrayList<Album> fileInfos) {
                super.onPostExecute(fileInfos);
                if (loadView != null) {
                    loadView.onAlbumLoad(fileInfos);
                }
            }
        }.execute();
    }
}
