package com.whty.eschoolbag.filemanager.presenter;

import android.os.AsyncTask;
import android.os.Environment;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.whty.eschoolbag.filemanager.bean.CacheCate;
import com.whty.eschoolbag.filemanager.bean.FileList;
import com.whty.eschoolbag.filemanager.model.ScanCate;
import com.whty.eschoolbag.filemanager.presenter.core.FileTools;
import com.whty.eschoolbag.filemanager.presenter.core.FilterByType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/13 11:07
 * description:
 */
public class ScanPresenter {


    private static final String dataPath = Environment.getExternalStorageDirectory().toString() + "/Android/data";//data目录地址
    private static final String rootPath = Environment.getExternalStorageDirectory().toString();//外存根目录地址
    private static final String homework = Environment.getExternalStorageDirectory().getPath() + "/eschoolHomework";
    private static final String interactiveroom = Environment.getExternalStorageDirectory() + "/interactiveroom/文件传输";

    private ScanTask scanTask;
    private List<ScanCate> scanList;

    public interface ScanListener {
        void scanProgress(String progress);

        void scanResult(ArrayList<MultiItemEntity> list, long totalSize);
    }

    private ScanListener scanListener;

    public ScanPresenter(ScanListener scanListener) {
        this.scanListener = scanListener;
    }


    public void scan() {

        scanList = new ArrayList<ScanCate>();
        scanList.add(new ScanCate(new File(dataPath), "缓存文件", FileTools.CACHE_DIR, true));
        scanList.add(new ScanCate(new File(rootPath), "临时文件", FileTools.TEMP_DIR, true));
        scanList.add(new ScanCate(new File(rootPath), "日志文件", FileTools.LOG_FILES, true));

        //        scanList.add(new ScanCate(new File(rootPath), "大文件", FileTools.LARGE_FILES, false));
        scanList.add(new ScanCate(new File(rootPath), "安装包", FileTools.APK, false));
        scanList.add(new ScanCate(new File(homework), "作业", FileTools.CUSTOM, false));
        scanList.add(new ScanCate(new File(interactiveroom), "互动课堂", FileTools.CUSTOM, false));


        scanTask = new ScanTask();
        scanTask.execute();
    }

    public void cancelScan() {
        if (scanTask != null) {
            scanTask.cancel(true);
        }
    }


    class ScanTask extends AsyncTask<Void, String, List<FileList>> {

        @Override
        protected List<FileList> doInBackground(Void... voids) {


            List<FileList> result = new ArrayList<FileList>();
            for (ScanCate scanCate : scanList) {
                if (scanCate.getType() == FileTools.CUSTOM) {

                    FileList subResult = new FileList(scanCate.getName(), scanCate.isSelect());
                    File[] files = scanCate.getRoot().listFiles();

                    if (files != null && files.length > 0) {
                        ArrayList<File> arrayList = new ArrayList<>();
                        for (File file : files) {
                            if (file.exists() && file.length() > 0) {
                                publishProgress(file.getPath());
                                arrayList.add(file);
                            }
                        }

                        subResult.setList(arrayList);
                        result.add(subResult);
                    }

                } else {
                    FileList subResult = new FileList(scanCate.getName(), scanCate.isSelect());
                    deepFirstSearch(scanCate.getRoot(), subResult.getList(), scanCate.getType());

                    if (subResult.getList().size() > 0) {
                        result.add(subResult);
                    }
                }
            }

            return result;
        }


        @Override
        protected void onPostExecute(List<FileList> lists) {
            super.onPostExecute(lists);

            ArrayList<MultiItemEntity> list = new ArrayList<>();
            CacheCate cacheCate = new CacheCate("扫描结果", lists.size());
            long totalSize = 0;
            for (FileList files : lists) {
                cacheCate.addSubItem(files);
                totalSize += files.getStorageSize();
            }
            cacheCate.setStorageSize(totalSize);

            list.add(cacheCate);

            if (scanListener != null) {
                scanListener.scanResult(list, totalSize);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values.length > 0) {
                if (scanListener != null) {
                    scanListener.scanProgress(values[0]);
                }
            }
        }


        /**
         * 深度优先搜索，通过filter筛选出目标文件，将目标文件存入array
         * 递归调用本方法，搜索所有目录
         *
         * @param dir
         * @param array
         * @param type
         */
        private void deepFirstSearch(File dir, List array, int type) {
            File[] allFiles = dir.listFiles();
            File[] targeFiles = dir.listFiles(new FilterByType(type));

            if (targeFiles != null) {
                for (File f : targeFiles) {
                    if (f.exists() && f.length() > 0) {
                        array.add(f);

                    }
                }
            }

            if (allFiles != null) {
                for (File file : allFiles) {
                    publishProgress(file.getPath());
                    if (file.isDirectory()) {
                        deepFirstSearch(file, array, type);
                    }
                }
            }
        }
    }


}
