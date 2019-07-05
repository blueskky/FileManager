package com.whty.eschoolbag.filemanager.ui.preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tencent.smtt.sdk.TbsReaderView;
import com.whty.eschoolbag.filemanager.R;

import java.io.File;

public class FilePreviewActivity extends AppCompatActivity implements TbsReaderView.ReaderCallback {

    private TbsReaderView tbsReaderView;

    public static void start(Context context, String filePath) {
        context.startActivity(new Intent(context, FilePreviewActivity.class).putExtra("path", filePath));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_preview);

        ViewGroup container = findViewById(R.id.container);

        tbsReaderView = new TbsReaderView(this, this);
        container.addView(tbsReaderView, new RelativeLayout.LayoutParams(-1, -1));


        Intent intent = getIntent();
        if (intent != null) {
            String path = intent.getStringExtra("path");
            if (!TextUtils.isEmpty(path)) {
                //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
                String bsReaderTemp = "/storage/emulated/0/TbsReaderTemp";
                File bsReaderTempFile = new File(bsReaderTemp);

                if (!bsReaderTempFile.exists()) {
                    bsReaderTempFile.mkdir();

                }

                boolean preOpen = tbsReaderView.preOpen(getFileType(path), false);
                if (preOpen) {
                    Bundle bundle = new Bundle();
                    bundle.putString("filePath", path);
                    bundle.putString("tempPath", Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");
                    tbsReaderView.openFile(bundle);
                }
            }
        }
    }


    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }


    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }

        str = paramString.substring(i + 1);
        return str;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tbsReaderView != null) {
            tbsReaderView.onStop();
        }
    }
}
