package com.whty.eschoolbag.filemanager.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.bean.HeadItem;
import com.whty.eschoolbag.filemanager.util.FileOpenHelper;
import com.whty.eschoolbag.filemanager.widgetview.GridSpacingItemDecoration;
import com.whty.eschoolbag.filemanager.widgetview.RecycleViewDivider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class RecentAdapter extends BaseQuickAdapter<HeadItem, BaseViewHolder> {


    private OnItemFoldListener onItemFoldListener;
    private int imgFoldCount = 16;
    private int fileFoldCount = 3;


    public RecentAdapter() {
        super(R.layout.header_item);
    }


    public void setOnItemFoldListener(OnItemFoldListener onItemFoldListener) {
        this.onItemFoldListener = onItemFoldListener;

    }


    @Override
    protected void convert(BaseViewHolder helper, HeadItem item) {


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String tiem = format.format(item.getTimeStamp());
        helper.setText(R.id.tv_time, tiem);
        RecyclerView subRecyclerView = (RecyclerView) helper.getView(R.id.recyclerView);


        if (item.getMediaType().equals(String.valueOf(MEDIA_TYPE_IMAGE))) {
            String formatStr = String.format("图片%d项", item.getList().size());
            helper.setText(R.id.tv_name, formatStr);
            subRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 8));

            if (subRecyclerView.getItemDecorationCount() == 0) {
                subRecyclerView.addItemDecoration(new GridSpacingItemDecoration(8, 30, true));
            }

            RecentImgAdapter recentImgAdapter = new RecentImgAdapter();
            subRecyclerView.setAdapter(recentImgAdapter);

            if (item.getList().size() > imgFoldCount) {
                helper.setGone(R.id.ll_more, true);
                helper.setText(R.id.tv_more, "更多");
                List<FileInfo> subList = item.getList().subList(0, imgFoldCount);
                recentImgAdapter.setNewData(subList);
                helper.getView(R.id.ll_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (recentImgAdapter.getItemCount() > imgFoldCount) {
                            recentImgAdapter.setNewData(subList);
                            helper.setText(R.id.tv_more, "更多");
                            helper.setImageResource(R.id.iv_more,R.drawable.bottom_arror_sel);

                            //折叠了
                            if (onItemFoldListener != null) {
                                onItemFoldListener.onItemFold(helper.getLayoutPosition());
                            }

                        } else {
                            recentImgAdapter.setNewData(item.getList());
                            helper.setText(R.id.tv_more, "收起");
                            helper.setImageResource(R.id.iv_more,R.drawable.top_arror_sel);
                        }
                    }
                });
            } else {
                helper.setGone(R.id.ll_more, false);
                recentImgAdapter.setNewData(item.getList());
            }

            recentImgAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    FileOpenHelper.openImgFileSlideMore(mContext, item.getList(), position);
                }
            });


        } else {

            if (item.getMediaType().equals(String.valueOf(MEDIA_TYPE_VIDEO))) {
                String formatStr = String.format("视频%d项", item.getList().size());
                helper.setText(R.id.tv_name, formatStr);
            } else if (item.getMediaType().equals(String.valueOf(MEDIA_TYPE_AUDIO))) {

                String formatStr = String.format("音频%d项", item.getList().size());
                helper.setText(R.id.tv_name, formatStr);
            } else {
                String formatStr = String.format("文件%d项", item.getList().size());
                helper.setText(R.id.tv_name, formatStr);
            }

            subRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            //添加自定义分割线：可自定义分割线高度和颜色
            if (subRecyclerView.getItemDecorationCount() == 0) {
                subRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, 10,
                        mContext.getResources().getColor(R.color.white)));
            }


            RecentFileAdapter recentFileAdapter = new RecentFileAdapter();
            subRecyclerView.setAdapter(recentFileAdapter);

            if (item.getList().size() > fileFoldCount) {
                helper.setGone(R.id.ll_more, true);
                helper.setText(R.id.tv_more, "更多");
                List<FileInfo> subList = item.getList().subList(0, fileFoldCount);
                recentFileAdapter.setNewData(subList);
                helper.getView(R.id.ll_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recentFileAdapter.getItemCount() > fileFoldCount) {
                            recentFileAdapter.setNewData(subList);
                            helper.setText(R.id.tv_more, "更多");
                            helper.setImageResource(R.id.iv_more,R.drawable.bottom_arror_sel);

                            if (onItemFoldListener != null) {
                                onItemFoldListener.onItemFold(helper.getLayoutPosition());
                            }

                        } else {
                            recentFileAdapter.setNewData(item.getList());
                            helper.setText(R.id.tv_more, "收起");
                            helper.setImageResource(R.id.iv_more,R.drawable.top_arror_sel);

                        }
                    }
                });
            } else {
                helper.setGone(R.id.ll_more, false);
                recentFileAdapter.setNewData(item.getList());
            }


            recentFileAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    FileOpenHelper.openFile(mContext, new File(recentFileAdapter.getItem(position).getFilePath()));
                }
            });
        }


    }


    public interface OnItemFoldListener {
        void onItemFold(int position);
    }
}
