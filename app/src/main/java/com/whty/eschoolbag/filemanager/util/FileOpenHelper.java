package com.whty.eschoolbag.filemanager.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.ui.preview.ImagePreviewActivity;
import com.whty.eschoolbag.filemanager.ui.preview.SimplePlayActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/18 15:28
 * description:
 */
public class FileOpenHelper {

    public static void openFile(Context context, File file) {
        //todo
        String path = file.getPath();
        //先插入数据库  可能是用listFiles得到的文件，不一定存在媒体数据库中
        FileUtil.insertMedia(context, path);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Uri uri = MediaStore.Files.getContentUri("external");
                final String[] columns = {FileColumns.DATA, FileColumns.DISPLAY_NAME, FileColumns.MEDIA_TYPE, FileColumns.MIME_TYPE,
                        FileColumns.SIZE, FileColumns.DATE_MODIFIED};

                String selection = FileColumns.DATA + "=?";
                String[] args = {path};

                Cursor cursor = context.getContentResolver().query(uri, columns, selection, args, null);

                FileInfo fileInfo = null;

                if(cursor!=null){
                    while (cursor.moveToNext()) {
                        fileInfo = new FileInfo();
                        fileInfo.setFilePath(cursor.getString(cursor.getColumnIndex(FileColumns.DATA)));
                        fileInfo.setMediaType(cursor.getString(cursor.getColumnIndex(FileColumns.MEDIA_TYPE)));
                        fileInfo.setMimeType(cursor.getString(cursor.getColumnIndex(FileColumns.MIME_TYPE)));
                        fileInfo.setFileSize(cursor.getLong(cursor.getColumnIndex(FileColumns.SIZE)));
                        fileInfo.setFileName(cursor.getString(cursor.getColumnIndex(FileColumns.DISPLAY_NAME)));
                        fileInfo.setModifiedDate(cursor.getLong(cursor.getColumnIndex(FileColumns.DATE_MODIFIED)) * 1000);
                    }
                    cursor.close();
                }


                //数据库查到该文件
                if (fileInfo != null) {
                    if (fileInfo.getMediaType().equals(String.valueOf(MEDIA_TYPE_IMAGE))) {
                        ArrayList<FileInfo> list = new ArrayList<>();
                        list.add(fileInfo);
                        openImgFileSlideMore(context, list, 0);
                    } else if (fileInfo.getMediaType().equals(String.valueOf(MEDIA_TYPE_VIDEO))) {

                        SimplePlayActivity.start(context, fileInfo.getFilePath());
                        //                        if (Utils.checkAppInstalled(context, "com.whty.flvplayer")) {
                        //                            FileUtil.openFileByTYPlayer(context, fileInfo.getFilePath(), "");
                        //                        } else {
                        //                            SimplePlayActivity.start(context, fileInfo.getFilePath());
                        //
                        //                        }

                    } else if (fileInfo.getMediaType().equals(String.valueOf(MEDIA_TYPE_AUDIO))) {

                        SimplePlayActivity.start(context, fileInfo.getFilePath());

                        //                        if (Utils.checkAppInstalled(context, "com.whty.flvplayer")) {
                        //                            FileUtil.openFileByTYPlayer(context, fileInfo.getFilePath(), "");
                        //                        } else {
                        //
                        //                        }


                    } else {
                        FileUtil.openFileWithExtName(context, fileInfo.getFilePath());
                    }
                } else {
                    FileUtil.openFileWithExtName(context, path);
                }

            }
        }, 500);


    }


    public static void openImgFileSlideMore(Context context, List<FileInfo> data, int position) {

        //intent 传递数据量太大 异常 改为静态赋值
        ImagePreviewActivity.setData(data);
        ImagePreviewActivity.start(context, position);
    }


    public static void showFileIcon(Context context, FileInfo item, ImageView imageView) {
        File file = new File(item.getFilePath());

        String ext = Util.getExtFromFilename(file.getPath());

        if (file.isDirectory()) {
            imageView.setImageResource(R.drawable.icon_file);
        } else if (item.isVideo() || FileUtil.VIDEO_FORFATS.contains(ext)) {
            RequestOptions options = new RequestOptions().placeholder(R.drawable.icon_video)
                    //图片加载出来前，显示的图片
                    .fallback(R.drawable.icon_video) //url为空的时候,显示的图片
                    .error(R.drawable.icon_video);//图片加载失败后，显示的图片

            Glide.with(context).load(item.getFilePath()).apply(options).into(imageView);
        } else if (item.isAudio() || FileUtil.AUDIO_FORFATS.contains(ext)) {
            Glide.with(context).load(R.drawable.file_icon_music).into(imageView);
        } else {

            switch (Util.getExtFromFilename(item.getFilePath())) {
                case "doc":
                case "docx":
                    Glide.with(context).load(R.drawable.file_icon_doc).into(imageView);
                    break;

                case "ppt":
                case "pptx":
                    Glide.with(context).load(R.drawable.file_icon_ppt).into(imageView);
                    break;

                case "xls":
                case "xlsx":
                    Glide.with(context).load(R.drawable.file_icon_xls).into(imageView);
                    break;

                case "txt":
                case "log":
                    Glide.with(context).load(R.drawable.file_icon_txt).into(imageView);
                    break;

                case "pdf":
                    Glide.with(context).load(R.drawable.file_icon_pdf).into(imageView);
                    break;

                case "png":
                case "jpg":
                    RequestOptions options = new RequestOptions().placeholder(R.drawable.icon_pic)//图片加载出来前，显示的图片
                            .fallback(R.drawable.icon_pic) //url为空的时候,显示的图片
                            .error(R.drawable.icon_pic);//图片加载失败后，显示的图片
                    Glide.with(context).load(item.getFilePath()).into(imageView);
                    break;
                case "apk":
                    Glide.with(context).load(R.drawable.apk).into(imageView);
                    break;

                case "zip":
                case "rar":
                    Glide.with(context).load(R.drawable.rar).into(imageView);
                    break;
                default:
                    Glide.with(context).load(R.drawable.file_icon_def).into(imageView);
                    break;
            }
        }
    }
}
