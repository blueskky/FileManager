package com.whty.eschoolbag.filemanager.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.whty.eschoolbag.filemanager.BuildConfig;
import com.whty.eschoolbag.filemanager.ui.preview.SimplePlayActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FileUtil {

    public static final String BASEPATH = Environment.getExternalStorageDirectory() + File.separator +
            "eschoolHomework";
    public static boolean flag;

    public static List<String> WPS_FORFATS = new ArrayList<>();

    static {
        WPS_FORFATS.add("doc");
        WPS_FORFATS.add("docx");
        WPS_FORFATS.add("pdf");
        WPS_FORFATS.add("txt");
        WPS_FORFATS.add("log");
        WPS_FORFATS.add("dot");
        WPS_FORFATS.add("ppt");
        WPS_FORFATS.add("pptx");
        WPS_FORFATS.add("pps");
        WPS_FORFATS.add("ppsx");
        WPS_FORFATS.add("pot");
        WPS_FORFATS.add("potx");
        WPS_FORFATS.add("xls");
        WPS_FORFATS.add("xlsx");
        WPS_FORFATS.add("csv");
        WPS_FORFATS.add("xlt");
        WPS_FORFATS.add("xltx");
    }

    public static List<String> VIDEO_FORFATS = new ArrayList<>();

    static {
        VIDEO_FORFATS.add("mp4");
        VIDEO_FORFATS.add("3gp");
        VIDEO_FORFATS.add("flv");
        VIDEO_FORFATS.add("rm");
        VIDEO_FORFATS.add("rmvb");
        VIDEO_FORFATS.add("avi");
        VIDEO_FORFATS.add("mpeg");
        VIDEO_FORFATS.add("mpg");
        VIDEO_FORFATS.add("mkv");
        VIDEO_FORFATS.add("asf");
        VIDEO_FORFATS.add("wmv");
        VIDEO_FORFATS.add("mov");
        VIDEO_FORFATS.add("ogg");
        VIDEO_FORFATS.add("ogm");
        VIDEO_FORFATS.add("m4u");
        VIDEO_FORFATS.add("m4v");
        VIDEO_FORFATS.add("mpe");
        VIDEO_FORFATS.add("mpeg");
        VIDEO_FORFATS.add("mpg4");

    }

    public static List<String> AUDIO_FORFATS = new ArrayList<>();

    static {
        AUDIO_FORFATS.add("aac");
        AUDIO_FORFATS.add("mp3");
        AUDIO_FORFATS.add("amr");
        AUDIO_FORFATS.add("pcm");
        AUDIO_FORFATS.add("m3u");
        AUDIO_FORFATS.add("m4a");
        AUDIO_FORFATS.add("m4b");
        AUDIO_FORFATS.add("m4p");
        AUDIO_FORFATS.add("mp2");
        AUDIO_FORFATS.add("mpga");
        AUDIO_FORFATS.add("ogg");
        AUDIO_FORFATS.add("wav");
        AUDIO_FORFATS.add("wma");
        AUDIO_FORFATS.add("wmv");
        AUDIO_FORFATS.add("rmvb");

    }

    // 保存数据到SDCard
    public static void saveToSDCard(String filename, String content) throws Exception {
        // 得到文件对象,找到SDCard的路径
        String path = BASEPATH + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File mfile = new File(path, filename);
        mfile.createNewFile();
        // 得到文件输出流对象
        FileOutputStream outStream = new FileOutputStream(mfile);
        outStream.write(content.getBytes());
        outStream.close();
    }

    // 读取文本文件中的内容
    public static String readFromFile(String fileName) {

        String ret = "";

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            try {
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ret = stringBuilder.toString();
            stringBuilder = null;
        }

        return ret;
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        flag = false;
        File file = new File(sPath);

        if (file == null || !file.exists()) {
            return true;
        }

        final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
        file.renameTo(to);

        // 路径为文件且不为空则进行删除
        if (to.isFile() && to.exists()) {
            // file.delete();
            to.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        // 删除当前目录
        final File to = new File(dirFile.getAbsolutePath() + System.currentTimeMillis());
        dirFile.renameTo(to);

        if (to.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Java文件操作 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename.toLowerCase();
    }

    public static boolean checkFileExist(String path) {
        File mf = new File(path);
        return mf.exists();
    }

    public static void copyfile(File fromFile, File toFile, Boolean rewrite) {
        if (!fromFile.exists()) {
            return;
        }
        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }
        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); //将内容写到新文件当中
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void openFileWithExtName(Context mContext, String filePath) {

        String extensionName = FileUtil.getExtensionName(filePath);

        switch (extensionName) {
            case "apk":
                openApk(mContext, new File(filePath));

                break;
            default:
                if (FileUtil.WPS_FORFATS.contains(extensionName)) {
                    if (Utils.checkAppInstalled(mContext, "cn.wps.moffice_eng")) {
                        FileUtil.openFileByWPS(mContext, filePath);
                    } else {
                        FileUtil.open(mContext, filePath);
                    }
                } else if (FileUtil.VIDEO_FORFATS.contains(extensionName)) {

                    SimplePlayActivity.start(mContext, filePath);
                } else if (FileUtil.AUDIO_FORFATS.contains(extensionName)) {
                    SimplePlayActivity.start(mContext, filePath);
                } else {
                    FileUtil.open(mContext, filePath);
                }

                break;
        }


    }

    public static void open(Context mContext, String filePath) {

        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(mContext, "资源文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Intent mIntent = new Intent(Intent.ACTION_VIEW);
            mIntent.addCategory(Intent.CATEGORY_DEFAULT);

            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider",
                        new File(filePath));
                // 给目标应用一个临时授权
                mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(new File(filePath));
            }
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //            MimeTypeMap map = MimeTypeMap.getSingleton();
            //            String mimeType = map.getMimeTypeFromExtension(FileUtil.getExtensionName(filePath
            //            .toLowerCase()));

            String mimeType = getType(getExtensionName(filePath));
            if (mimeType != null) {
                mIntent.setDataAndType(uri, mimeType);
                mContext.startActivity(mIntent);
            } else {
                Toast.makeText(mContext, "没有打开此文件的应用", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "文件打开失败", Toast.LENGTH_SHORT).show();
        }

    }


    public static String getType(String ext) {
        String type = "*/*";
        for (int i = 0; i < FileType.MATCH_ARRAY.length; i++) {
            if (ext.equals(FileType.MATCH_ARRAY[i][0])) {
                type = FileType.MATCH_ARRAY[i][1];
                break;
            }
        }
        return type;
    }

    public static boolean isImage(String path) {
        String ext = "";
        String[] strs = path.split("[.]");
        if (strs.length > 1) {
            ext = strs[strs.length - 1];
        }
        if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png")) {
            return true;
        }
        return false;
    }


    public static void insertMedia(Context context, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return;
            }
            //        其次把文件插入到系统图库
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", new File(path));
                // 给目标应用一个临时授权
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(new File(path));
            }

            intent.setData(uri);
            context.sendBroadcast(intent);

            Log.d("FileUtils", "insertMedia: over  " + path);
        } catch (Exception e) {

        }

    }


    public static void scanDir(Context context, String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            return;
        }
        String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
        //其次把文件插入到系统图库
        Intent intent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        //        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);

        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", new File(dir));
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(dir));
        }
        intent.setData(uri);
        context.sendBroadcast(intent);

        Log.d("FileUtils", "insertMedia: over  " + dir);
    }


    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {

        if (fileS == 0) {
            return "0B";
        }

        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }


    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static Pair<String, String> formatFile(long fileS) {
        String fileSizeString = "";
        String format = "";
        if (fileS == 0) {
            return new Pair<>("0", "B");
        }

        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS);

            format = "B";

        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024);

            format = "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576);

            format = "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824);

            format = "G";
        }
        return new Pair<>(fileSizeString, format);
    }

    public static void openFileByWPS(Context context, String filePath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setAction(Intent.ACTION_DEFAULT);
        intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager" + ".PreStartActivity2");

        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", new File(filePath));
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static void openFileByTYPlayer(Context context, String filePath, String name) {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(name)) {
            intent.putExtra("title", name);
        }
        intent.setAction(Intent.ACTION_VIEW);
        intent.setAction(Intent.ACTION_DEFAULT);
        intent.setClassName("com.whty.flvplayer", "com.whty.flvplayer.PlayerActivity");
        String type = "video/*";
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", new File(filePath));
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }


    /**
     * @param si whether using SI unit refer to International System of Units.
     */
    public static String humanReadableBytes(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.ENGLISH, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }


    private static void openApk(Context mContext, File apkfile) {
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider",
                    apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            //兼容8.0
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                boolean hasInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions((Activity) mContext,
                            new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 6666);
                }
            }
        } else {
            // 通过Intent安装APK文件
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        }
        if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            mContext.startActivity(intent);
        }

    }


}
