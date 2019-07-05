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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.adapter.ImageExploreListAdapter;
import com.whty.eschoolbag.filemanager.model.Album;
import com.whty.eschoolbag.filemanager.presenter.ImagePresenter;
import com.whty.eschoolbag.filemanager.viewinterface.ILoadView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImgCategoryActivity extends AppCompatActivity implements ILoadView {


    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private ImageExploreListAdapter mAdapter;
    private Context mContext;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ImgCategoryActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explore);
        ButterKnife.bind(this);
        mContext = this;
        ivMenu.setVisibility(View.GONE);


        ImagePresenter presenter = new ImagePresenter(this);
        presenter.loadAlbum(this);


        recycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ImageExploreListAdapter();
        recycler.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Album item = mAdapter.getItem(position);

                ImgExploreListActivity.start(mContext, item);

            }
        });
    }


    @OnClick({R.id.iv_search, R.id.iv_menu, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                break;
            case R.id.iv_menu:
                //                showSortDialog();
                //                showFilterDialog();
                break;
        }
    }


    @Override
    public void onAlbumLoad(ArrayList<Album> list) {

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
}
