package com.whty.eschoolbag.filemanager.ui.category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whty.eschoolbag.filemanager.Config;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.model.SortBean;
import com.whty.eschoolbag.filemanager.presenter.LoadPresenter;
import com.whty.eschoolbag.filemanager.util.FileListSorter;
import com.whty.eschoolbag.filemanager.util.SPUtil;
import com.whty.eschoolbag.filemanager.widgetview.SortDialog;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileExploreActivity extends AppCompatActivity implements LoadPresenter.OnLoadCallBack {


    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    private FileExploreFragment exploreFragment;
    private String type;


    @BindView(R.id.tv_title)
    TextView tvTitle;


    public static void start(Activity context, String typeClass) {
        context.startActivity(new Intent(context, FileExploreActivity.class).putExtra("type", typeClass));
    }

    public static void start(Context mContext, List<File> list) {
        Intent intent = new Intent(mContext, FileExploreActivity.class);
        intent.putExtra("FILE_LIST", ((Serializable) list));
        intent.putExtra("type", Config.TYPE_CLEAN);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        ButterKnife.bind(this);
        ivMenu.setVisibility(View.VISIBLE);

        type = getIntent().getStringExtra("type");

        LoadPresenter loadPresenter = new LoadPresenter(this);


        switch (type) {
            case Config.TYPE_FAVORITE:
                tvTitle.setText("收藏");
                loadPresenter.loadFavoriteFile(this, null);
                break;
            case Config.TYPE_CLASS:
                tvTitle.setText("互动课堂");
                loadPresenter.loadClassFile();
                break;
            case Config.TYPE_DOWNLOAD:
                tvTitle.setText("下载");
                loadPresenter.loadDownloadFile();
                break;
            case Config.TYPE_CLEAN:
                tvTitle.setText("文件浏览");

                List<File> fileList = (List<File>) getIntent().getSerializableExtra("FILE_LIST");
                ArrayList<FileInfo> infoArrayList = new ArrayList<>();
                for (File file : fileList) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFilePath(file.getPath());
                    infoArrayList.add(fileInfo);
                }

                onLoadFinish(infoArrayList);
                break;
        }

    }


    @Override
    public void onLoadFinish(ArrayList<FileInfo> list) {
        SortBean sort = SPUtil.getInstance(this).getSort(type);
        Collections.sort(list, new FileListSorter(0, sort.getSortType(), sort.getAsc()));

        exploreFragment = new FileExploreFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", list);
        bundle.putString("type", type);
        exploreFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, exploreFragment).commitAllowingStateLoss ();
    }


    private void onBack() {
        if (exploreFragment != null) {
            exploreFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }


    @OnClick({R.id.iv_back, R.id.iv_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBack();
                break;
            case R.id.iv_menu:

                SortBean sort = SPUtil.getInstance(this).getSort(type);
                SortDialog sortDialog = new SortDialog(this, sort);
                sortDialog.show();
                sortDialog.setmSortListener(new SortDialog.SortListener() {
                    @Override
                    public void sort(SortBean sortBean) {
                        SPUtil.getInstance(FileExploreActivity.this).saveSort(type,sortBean);
                        exploreFragment.setSort(sortBean);
                    }
                });
                break;
        }


    }

    @Override
    public void onBackPressed() {
        onBack();
    }


}
