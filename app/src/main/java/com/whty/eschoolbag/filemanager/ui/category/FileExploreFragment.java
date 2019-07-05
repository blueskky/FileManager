package com.whty.eschoolbag.filemanager.ui.category;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.adapter.FileExploreAdapter;
import com.whty.eschoolbag.filemanager.adapter.PathAdapter;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.bean.PathItem;
import com.whty.eschoolbag.filemanager.db.FavoriteDatabaseHelper;
import com.whty.eschoolbag.filemanager.model.SortBean;
import com.whty.eschoolbag.filemanager.util.FileListSorter;
import com.whty.eschoolbag.filemanager.util.FileOpenHelper;
import com.whty.eschoolbag.filemanager.util.Util;
import com.whty.eschoolbag.filemanager.widgetview.DetailDialog;
import com.whty.eschoolbag.filemanager.widgetview.OperationDialog;
import com.whty.eschoolbag.filemanager.widgetview.SpacesItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileExploreFragment extends Fragment {

    public static final String TYPE_CLASS = "class";
    public static final String TYPE_FAVORITE = "favorite";
    public static final String TYPE_DOWNLOAD = "download";
    public static final String TYPE_SEARCH = "search";
    private static final String TYPE_CLEAN = "clean";

    @BindView(R.id.recy_path)
    RecyclerView recyPath;
    @BindView(R.id.recy_file)
    RecyclerView recyFile;
    Unbinder unbinder;
    private FileExploreAdapter exploreAdapter;
    private PathAdapter pathAdapter;
    private String rootPath;
    private String type;
    private ArrayList<FileInfo> originalFileList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_explore, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyFile.setLayoutManager(new LinearLayoutManager(getActivity()));
        exploreAdapter = new FileExploreAdapter();
        recyFile.setAdapter(exploreAdapter);


        recyPath.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL, false));
        recyPath.addItemDecoration(new SpacesItemDecoration(40));
        pathAdapter = new PathAdapter(R.layout.path_item);
        recyPath.setAdapter(pathAdapter);

        exploreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FileInfo fileInfo = exploreAdapter.getItem(position);

                File file = new File(fileInfo.getFilePath());
                if (file.isDirectory()) {
                    ArrayList<FileInfo> childFileList = Util.getChildFileList(fileInfo.getFilePath());
                    setFileList(childFileList);
                    setPathList(fileInfo.getFilePath());
                } else {
                    //                    FileUtil.openFile(getActivity(), fileInfo.getFilePath());
                    FileOpenHelper.openFile(getActivity(), new File(fileInfo.getFilePath()));
                }
            }
        });


        exploreAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                showDialog(position);
                return false;
            }
        });


        pathAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PathItem item = pathAdapter.getItem(position);
                String path = item.getPath();

                if (item.isRoot()) {
                    initFileList(originalFileList, type);
                } else {
                    ArrayList<FileInfo> childFileList = Util.getChildFileList(path);
                    setFileList(childFileList);
                    setPathList(path);
                }
            }
        });


        if (getArguments() != null) {
            type = getArguments().getString("type");
            originalFileList = ((ArrayList<FileInfo>) getArguments().getSerializable("data"));
            initFileList(originalFileList, type);
        }
    }

    private void showDialog(int position) {
        FileInfo item = exploreAdapter.getItem(position);
        FavoriteDatabaseHelper helper = new FavoriteDatabaseHelper(getActivity(), null);
        boolean favorite = helper.isFavorite(item.getFilePath());

        OperationDialog operationDialog = new OperationDialog(getActivity(), favorite);
        operationDialog.setClickListener(new OperationDialog.MenuClickListener() {
            @Override
            public void onFavouritClick() {
                if (favorite) {
                    boolean delete = helper.delete(item.getFilePath());
                    if (delete) {
                        Toast.makeText(getActivity(), "收藏已取消", Toast.LENGTH_SHORT).show();
                        exploreAdapter.remove(position);
                        if (exploreAdapter.getItemCount() == 0) {
                            setEmptyView();
                        }
                    }
                } else {
                    boolean insert = helper.insert(item.getFileName(), item.getFilePath());
                    if (insert) {
                        Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onDetailClick() {
                DetailDialog detailDialog = new DetailDialog(getActivity());
                detailDialog.show();
                detailDialog.setDetailInfo(item);

            }
        });
        operationDialog.show();
    }

    private void setPathList(String filePath) {
        if (TextUtils.isEmpty(rootPath)) {
            rootPath = filePath.substring(0, filePath.lastIndexOf(File.separator));
        }
        String showPath = filePath.replaceAll(rootPath, "");

        ArrayList<PathItem> pathItems = addRootPathItem(type);
        if (!TextUtils.isEmpty(showPath)) {
            String[] paths = showPath.split(File.separator);
            StringBuffer sb = new StringBuffer(rootPath);
            for (String name : paths) {
                if (!TextUtils.isEmpty(name)) {
                    PathItem pathItem = new PathItem(name, sb.append(File.separator + name).toString());
                    pathItems.add(pathItem);
                }
            }
        }

        pathAdapter.setNewData(pathItems);

    }

    private void setFileList(ArrayList<FileInfo> childFileList) {
        exploreAdapter.setNewData(childFileList);
    }

    public void initFileList(ArrayList<FileInfo> list, String type) {
        rootPath = null;
        exploreAdapter.setNewData(list);

        ArrayList<PathItem> pathList = addRootPathItem(type);
        pathAdapter.setNewData(pathList);

        setEmptyView();
    }

    private void setEmptyView() {
        List<FileInfo> list = exploreAdapter.getData();
        if (list == null || list.size() == 0) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.empty_img, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_img);
            TextView tvTip = (TextView) view.findViewById(R.id.tv_tip);
            exploreAdapter.setEmptyView(view);

            if (type == TYPE_FAVORITE) {
                imageView.setImageResource(R.drawable.no_favourite);
                tvTip.setText("暂无收藏");
            } else if (type == TYPE_SEARCH) {
                tvTip.setText("未搜索到文件");
            } else {
                imageView.setImageResource(R.drawable.no_document);
            }
        }
    }


    @NonNull
    private ArrayList<PathItem> addRootPathItem(String type) {
        ArrayList<PathItem> pathList = new ArrayList<>();
        switch (type) {
            case TYPE_FAVORITE:
                PathItem rootPathItem = new PathItem("收藏", rootPath, true);
                pathList.add(rootPathItem);

                break;
            case TYPE_CLASS:
                rootPathItem = new PathItem("互动课堂", rootPath, true);
                pathList.add(rootPathItem);

                break;
            case TYPE_DOWNLOAD:
                rootPathItem = new PathItem("下载", rootPath, true);
                pathList.add(rootPathItem);
                break;

            case TYPE_SEARCH:
                rootPathItem = new PathItem("搜索", rootPath, true);
                pathList.add(rootPathItem);
                break;

            case TYPE_CLEAN:
                rootPathItem = new PathItem("文件清理", rootPath, true);
                pathList.add(rootPathItem);
                break;
        }
        return pathList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void onBackPressed() {
        List<PathItem> data = pathAdapter.getData();
        PathItem pathItem = data.get(data.size() - 1);

        if (pathItem.isRoot()) {
            getActivity().finish();
        } else {
            PathItem item = data.get(data.size() - 2);
            if (item.isRoot()) {
                initFileList(originalFileList, type);
            } else {
                ArrayList<FileInfo> childFileList = Util.getChildFileList(item.getPath());
                setFileList(childFileList);
                setPathList(item.getPath());
            }
        }
    }

    public void setSort(SortBean sortBean) {
        List<FileInfo> data = exploreAdapter.getData();
        Collections.sort(data, new FileListSorter(0, sortBean.getSortType(), sortBean.getAsc()));
        exploreAdapter.notifyDataSetChanged();
    }
}
