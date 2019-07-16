package com.whty.eschoolbag.filemanager;

import android.app.Application;
import android.os.Environment;

import com.tencent.bugly.crashreport.CrashReport;
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



}