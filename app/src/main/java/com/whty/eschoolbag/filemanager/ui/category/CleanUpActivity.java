package com.whty.eschoolbag.filemanager.ui.category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.adapter.CleanAdapter;
import com.whty.eschoolbag.filemanager.bean.CacheCate;
import com.whty.eschoolbag.filemanager.bean.FileList;
import com.whty.eschoolbag.filemanager.bean.RefreshEvent;
import com.whty.eschoolbag.filemanager.presenter.ScanPresenter;
import com.whty.eschoolbag.filemanager.presenter.core.FileDeleter;
import com.whty.eschoolbag.filemanager.util.FileUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CleanUpActivity extends AppCompatActivity implements ScanPresenter.ScanListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    private TextView tvTotal;
    private TextView tvScan;
    private Button btnClean;

    ArrayList<MultiItemEntity> list = new ArrayList<>();
    private CleanAdapter adapter;
    private TextView tv_format;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, CleanUpActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_up);
        ButterKnife.bind(this);

        ivMenu.setVisibility(View.GONE);

        adapter = new CleanAdapter(list);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter.expandAll();

        findView();

        ScanPresenter scanPresenter = new ScanPresenter(this);
        scanPresenter.scan();

    }

    private void findView() {
        tvTotal = (TextView) findViewById(R.id.tv_total);
        tv_format = (TextView) findViewById(R.id.tv_format);
        tvScan = (TextView) findViewById(R.id.tv_scan);
        btnClean = (Button) findViewById(R.id.btn_clean);


        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MultiItemEntity> data = adapter.getData();
                CacheCate cacheCate = (CacheCate) data.get(0);
                List<FileList> items = cacheCate.getSubItems();

                if (items.size() == 0) {
                    return;
                }

                Iterator<FileList> listIterator = items.iterator();
                while (listIterator.hasNext()) {
                    FileList fileList = listIterator.next();
                    if (fileList.isSelect()) {
                        boolean deleteAll = FileDeleter.deleteAll(fileList.getList());
                        if (deleteAll) {
                            listIterator.remove();
                        }
                    }
                }


                ArrayList<MultiItemEntity> list = new ArrayList<>();
                CacheCate currCacheCate = new CacheCate("扫描结果", items.size());
                long totalSize = 0;
                for (FileList item : items) {
                    currCacheCate.addSubItem(item);
                    totalSize += item.getStorageSize();
                }
                currCacheCate.setStorageSize(totalSize);
                list.add(currCacheCate);

                adapter.setNewData(list);

                Pair<String, String> pair = FileUtil.formatFile(totalSize);
                tvTotal.setText(pair.first);
                tv_format.setText(pair.second);


                if (items.size() == 0) {
                    btnClean.setEnabled(false);
                }


                EventBus.getDefault().post(new RefreshEvent());
            }
        });
    }

    @Override
    public void scanProgress(String progress) {
        tvScan.setText("正在扫描" + progress);
    }

    @Override
    public void scanResult(ArrayList<MultiItemEntity> list, long totalSize) {
        adapter.setNewData(list);
        tvScan.setText("扫描完成！");

        CacheCate cacheCate = (CacheCate) list.get(0);
        List<FileList> items = cacheCate.getSubItems();

        if (items != null && items.size() > 0) {
            btnClean.setEnabled(true);
        }

        Pair<String, String> pair = FileUtil.formatFile(totalSize);
        tvTotal.setText(pair.first);
        tv_format.setText(pair.second);

    }


    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }
}
