package com.whty.eschoolbag.filemanager.ui.recently;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.adapter.RecentAdapter;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.bean.HeadItem;
import com.whty.eschoolbag.filemanager.bean.SectionMultipleItem;
import com.whty.eschoolbag.filemanager.presenter.LoadPresenter;
import com.whty.eschoolbag.filemanager.widgetview.RecycleViewDivider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentlyFragment extends Fragment implements LoadPresenter.OnLoadCallBack {


    @BindView(R.id.recycler)
    RecyclerView recycler;
    Unbinder unbinder;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    private RecentAdapter recentAdapter;


    public RecentlyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recently, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivBack.setVisibility(View.GONE);
        tvTitle.setText("最近访问文件");
        ivMenu.setVisibility(View.GONE);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<SectionMultipleItem> data = new ArrayList<>();
        recentAdapter = new RecentAdapter();
        recycler.setAdapter(recentAdapter);
        //添加自定义分割线：可自定义分割线高度和颜色
        recycler.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.color_eeeee)));

        recentAdapter.setOnItemFoldListener(new RecentAdapter.OnItemFoldListener() {
            @Override
            public void onItemFold(int position) {
                recycler.scrollToPosition(position);
            }
        });


//
        LoadPresenter loadPresenter = new LoadPresenter(this);
        loadPresenter.loadRecentFiles(getContext());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onLoadFinish(ArrayList<FileInfo> list) {

        ArrayList<HeadItem> headItems = categoryData(list);
        recentAdapter.setNewData(headItems);

    }

    private ArrayList<HeadItem> categoryData(ArrayList<FileInfo> list) {

        ArrayList<HeadItem> headItems = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                FileInfo fileInfo = list.get(i);
                HeadItem headItem = new HeadItem();
                headItem.setMediaType(fileInfo.getMediaType());
                headItem.setTimeStamp(fileInfo.getModifiedDate());
                headItem.addFileInfo(fileInfo);

                headItems.add(headItem);
            } else {
                FileInfo preFileInfo = list.get(i - 1);
                FileInfo currentFileInfo = list.get(i);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String preDay = format.format(preFileInfo.getModifiedDate());
                String currDay = format.format(currentFileInfo.getModifiedDate());
                if (currentFileInfo.getMediaType().equals(preFileInfo.getMediaType()) && preDay.equals(currDay)) {

                    HeadItem headItem = headItems.get(headItems.size() - 1);
                    headItem.addFileInfo(currentFileInfo);

                } else {
                    HeadItem headItem = new HeadItem();
                    headItem.setMediaType(currentFileInfo.getMediaType());
                    headItem.setTimeStamp(currentFileInfo.getModifiedDate());
                    headItem.addFileInfo(currentFileInfo);
                    headItems.add(headItem);
                }
            }
        }

        return headItems;
    }


}
