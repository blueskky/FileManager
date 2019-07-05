package com.whty.eschoolbag.filemanager.ui.category;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whty.eschoolbag.filemanager.Config;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.adapter.ImageExploreAdapter;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.model.Album;
import com.whty.eschoolbag.filemanager.model.SortBean;
import com.whty.eschoolbag.filemanager.presenter.AlbumMediaCollection;
import com.whty.eschoolbag.filemanager.presenter.LoadPresenter;
import com.whty.eschoolbag.filemanager.util.FileListSorter;
import com.whty.eschoolbag.filemanager.util.FileOpenHelper;
import com.whty.eschoolbag.filemanager.util.SPUtil;
import com.whty.eschoolbag.filemanager.widgetview.GridSpacingItemDecoration;
import com.whty.eschoolbag.filemanager.widgetview.SortDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImgExploreListActivity extends AppCompatActivity implements
        AlbumMediaCollection.AlbumMediaCallbacks, LoadPresenter.OnLoadCallBack {


    private static final String ITEM = "item";
    private static final String NAME = "name";


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

    private ImageExploreAdapter mAdapter;
    private SortBean defaultSort;
    private String bucketName;
    private Album album;

    public static void start(Context context, String bucketName) {
        Intent intent = new Intent(context, ImgExploreListActivity.class);
        intent.putExtra(NAME, bucketName);
        context.startActivity(intent);
    }


    public static void start(Context context, Album item) {
        Intent intent = new Intent(context, ImgExploreListActivity.class);
        intent.putExtra(ITEM, item);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explore2);
        ButterKnife.bind(this);

        recycler.setLayoutManager(new GridLayoutManager(this, 9, OrientationHelper.VERTICAL, false));

        int spanCount = 9; // 3 columns
        int spacing = 21; // 50px
        boolean includeEdge = true;
        recycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        mAdapter = new ImageExploreAdapter();
        recycler.setAdapter(mAdapter);

        bucketName = getIntent().getStringExtra(NAME);

        if(TextUtils.isEmpty(bucketName)){
            album = (Album) getIntent().getSerializableExtra(ITEM);
            bucketName=album.getDisplayName();

            //Camera
            //Screenshots
            defaultSort = SPUtil.getInstance(this).getSort(bucketName);
            LoadPresenter loadPresenter = new LoadPresenter(this);
            loadPresenter.loadBucketImageWithId(this, album.getId(), defaultSort);
        }else{
            //Camera
            //Screenshots
            defaultSort = SPUtil.getInstance(this).getSort(bucketName);
            LoadPresenter loadPresenter = new LoadPresenter(this);
            loadPresenter.loadBucketImage(this, bucketName, defaultSort);
        }



        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ImagePreviewActivity.start(ImgExploreListActivity.this, mAdapter.getData(), position);
                FileOpenHelper.openImgFileSlideMore(ImgExploreListActivity.this, mAdapter.getData(), position);
            }
        });

        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_img, null);
        mAdapter.setEmptyView(emptyView);

        if (bucketName.equals(Config.BUCKET_SCREENSHOT)) {
            tvTitle.setText("截屏");
        } else if (bucketName.equals(Config.BUCKET_CAMERA)) {
            tvTitle.setText("相机");
        }else{
            tvTitle.setText(bucketName);
        }

    }


    @Override
    public void onAlbumMediaLoad(Cursor cursor) {
//        ArrayList<Item> list = new ArrayList<>();
//        while (cursor.moveToNext()) {
//            Item item = Item.valueOf(cursor);
//            list.add(item);
//        }
//        adapter.setNewData(list);

    }

    @Override
    public void onAlbumMediaReset() {

    }

    @Override
    public void onLoadFinish(ArrayList<FileInfo> list) {
        if (list == null || list.size() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.empty_img, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_img);
            TextView tvTip = (TextView) view.findViewById(R.id.tv_tip);
            mAdapter.setEmptyView(view);
            imageView.setImageResource(R.drawable.no_video);
            tvTip.setText("暂无图片");

            return;
        }


        mAdapter.setNewData(list);
    }

    @OnClick({R.id.iv_back, R.id.iv_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_menu:

                SortBean sort = SPUtil.getInstance(this).getSort(bucketName);
                SortDialog sortDialog = new SortDialog(this, sort);
                sortDialog.show();
                sortDialog.setmSortListener(new SortDialog.SortListener() {
                    @Override
                    public void sort(SortBean sortBean) {
                        SPUtil.getInstance(ImgExploreListActivity.this).saveSort(bucketName, sortBean);
                        List<FileInfo> data = mAdapter.getData();

                        Collections.sort(data, new FileListSorter(sortBean.getSortType(), sortBean.getAsc()));
                        mAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }
    }

}
