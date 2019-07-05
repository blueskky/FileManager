package com.whty.eschoolbag.filemanager.ui.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whty.eschoolbag.filemanager.Config;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.adapter.FileExploreAdapter;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.db.FavoriteDatabaseHelper;
import com.whty.eschoolbag.filemanager.model.SortBean;
import com.whty.eschoolbag.filemanager.presenter.LoadPresenter;
import com.whty.eschoolbag.filemanager.presenter.LoadType;
import com.whty.eschoolbag.filemanager.util.FileListSorter;
import com.whty.eschoolbag.filemanager.util.FileOpenHelper;
import com.whty.eschoolbag.filemanager.util.SPUtil;
import com.whty.eschoolbag.filemanager.widgetview.DetailDialog;
import com.whty.eschoolbag.filemanager.widgetview.OperationDialog;
import com.whty.eschoolbag.filemanager.widgetview.SortDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExploreListActivity extends AppCompatActivity implements LoadPresenter.OnLoadCallBack {


    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private FileExploreAdapter exploreAdapter;
    private Context mContext;
    private LoadType loadType;
    private String sortKey;

    public static void start(Context context, LoadType type) {
        context.startActivity(new Intent(context, ExploreListActivity.class).putExtra("type", type));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_explore_list);
        ButterKnife.bind(this);
        mContext = this;

        loadType = (LoadType) getIntent().getSerializableExtra("type");

        recycler.setLayoutManager(new LinearLayoutManager(this));
        exploreAdapter = new FileExploreAdapter();
        recycler.setAdapter(exploreAdapter);


        exploreAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            showmDialog(position);
            return false;
        });

        exploreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FileInfo fileInfo = exploreAdapter.getItem(position);
                FileOpenHelper.openFile(mContext, new File(fileInfo.getFilePath()));
            }
        });

        LoadPresenter loadPresenter = new LoadPresenter(this);

        switch (loadType) {
            case VIDEO:
                loadPresenter.loadVideo(this, LoadType.VIDEO);
                tvTitle.setText("视频");
                sortKey = Config.TYPE_VIDEO;
                break;
            case AUDIO:
                loadPresenter.loadAudio(this);
                tvTitle.setText("音频");

                sortKey = Config.TYPE_AUDIO;
                break;
            case DOC:
                loadPresenter.loadDocFile(this);
                tvTitle.setText("文档");

                sortKey = Config.TYPE_DOC;
                break;
        }


    }

    @Override
    public void onLoadFinish(ArrayList<FileInfo> list) {

        if (list == null || list.size() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.empty_img, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_img);
            TextView tvTip = (TextView) view.findViewById(R.id.tv_tip);
            exploreAdapter.setEmptyView(view);

            if (loadType == LoadType.VIDEO) {
                imageView.setImageResource(R.drawable.no_video);
                tvTip.setText("暂无视频文件");
            } else if (loadType == LoadType.AUDIO) {
                imageView.setImageResource(R.drawable.no_audio);
                tvTip.setText("暂无音频文件");
            } else if (loadType == LoadType.DOC) {
                imageView.setImageResource(R.drawable.no_document);
                tvTip.setText("暂无文档");
            }
            return;
        }


        SortBean sort = SPUtil.getInstance(this).getSort(sortKey);
        Collections.sort(list, new FileListSorter(0, sort.getSortType(), sort.getAsc()));

        exploreAdapter.setNewData(list);
    }


    public void showmDialog(int position) {
        FileInfo item = exploreAdapter.getItem(position);
        FavoriteDatabaseHelper helper = new FavoriteDatabaseHelper(this, null);
        boolean favorite = helper.isFavorite(item.getFilePath());

        OperationDialog operationDialog = new OperationDialog(this, favorite);
        operationDialog.setClickListener(new OperationDialog.MenuClickListener() {
            @Override
            public void onFavouritClick() {
                if (favorite) {
                    boolean delete = helper.delete(item.getFilePath());
                    if (delete) {
                        Toast.makeText(mContext, "收藏已取消", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    boolean insert = helper.insert(item.getFileName(), item.getFilePath());
                    if (insert) {
                        Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onDetailClick() {
                DetailDialog detailDialog = new DetailDialog(mContext);
                detailDialog.show();
                detailDialog.setDetailInfo(item);

            }
        });
        operationDialog.show();
    }


    @OnClick({R.id.iv_back, R.id.iv_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_menu:
                SortBean sort = SPUtil.getInstance(this).getSort(sortKey);
                SortDialog sortDialog = new SortDialog(this, sort);
                sortDialog.show();
                sortDialog.setmSortListener(new SortDialog.SortListener() {
                    @Override
                    public void sort(SortBean sortBean) {
                        SPUtil.getInstance(mContext).saveSort(sortKey, sortBean);

                        List<FileInfo> data = exploreAdapter.getData();
                        SortBean sort = SPUtil.getInstance(mContext).getSort(sortKey);
                        Collections.sort(data, new FileListSorter(0, sort.getSortType(), sort.getAsc()));
                        exploreAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }


    }
}
