package com.whty.eschoolbag.filemanager;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.umeng.commonsdk.UMConfigure;
import com.whty.eschoolbag.filemanager.util.FileUtil;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/18 18:47
 * description:
 */
public class APPAplication extends Application {

    private String appkey="5d11753d0cafb2077f0003a5";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        initTBS();

        initUmeng();

        CrashReport.initCrashReport(getApplicationContext(), "52e2c99968", BuildConfig.DEBUG);

//        ScanDir();
    }

    private void ScanDir() {
        String path = Environment.getExternalStorageDirectory().getPath();
        FileUtil.scanDir(this,path);
    }


    private void initUmeng() {

        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        UMConfigure.init(this, appkey, "TianYu",UMConfigure.DEVICE_TYPE_PHONE,"");
    }

    private void initTBS() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("lhq", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                Log.d("lhq", " onCoreInitFinished is " );
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        QbSdk.setNeedInitX5FirstTime(true);
        QbSdk.setDownloadWithoutWifi(false);

        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("lhq", " onDownloadFinish is " + i);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("lhq", " onInstallFinish is " + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("lhq", " onDownloadProgress is " + i);
            }
        });
    }

}