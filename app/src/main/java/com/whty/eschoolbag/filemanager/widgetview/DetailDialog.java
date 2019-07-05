package com.whty.eschoolbag.filemanager.widgetview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lu
 * @emil lhqwust@163.com
 * create at 2019/6/3 15:21
 * description:
 */
public class DetailDialog extends Dialog {


    private final LayoutInflater inflater;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_duriation)
    TextView tvDuriation;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_path)
    TextView tvPath;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    public DetailDialog(Context context) {
        super(context, R.style.Dialog);

        inflater = LayoutInflater.from(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_detail, null);
        setContentView(view);
        ButterKnife.bind(this);

        setCanceledOnTouchOutside(true);
    }


    public void setDetailInfo(FileInfo fileInfo) {
        tvName.setText(fileInfo.getFileName());

        if (new File(fileInfo.getFilePath()).isDirectory()) {
            tvType.setText("文件夹");
        } else {
            tvType.setText(fileInfo.getMimeType());
        }

        tvSize.setText(Util.convertStorage(fileInfo.getFileSize()));
        tvDuriation.setText(fileInfo.getDuration() + "");


        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
        String timeStr = format.format(fileInfo.getModifiedDate());

        tvTime.setText(timeStr);
        tvPath.setText(fileInfo.getFilePath());
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        dismiss();
    }
}
