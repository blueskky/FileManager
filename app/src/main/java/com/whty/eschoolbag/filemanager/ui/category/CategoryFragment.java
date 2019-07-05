package com.whty.eschoolbag.filemanager.ui.category;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whty.eschoolbag.filemanager.Config;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.RefreshEvent;
import com.whty.eschoolbag.filemanager.presenter.FileCategoryHelper;
import com.whty.eschoolbag.filemanager.presenter.LoadType;
import com.whty.eschoolbag.filemanager.util.Util;
import com.whty.eschoolbag.filemanager.widgetview.PopMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {


    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    Unbinder unbinder;
    @BindView(R.id.pb_internal)
    ProgressBar pbInternal;
    @BindView(R.id.tv_internal)
    TextView tvInternal;
    @BindView(R.id.pb_external)
    ProgressBar pbExternal;
    @BindView(R.id.tv_external)
    TextView tvExternal;
    @BindView(R.id.tv_image)
    TextView tvImage;
    @BindView(R.id.tv_video)
    TextView tvVideo;
    @BindView(R.id.tv_audio)
    TextView tvAudio;
    @BindView(R.id.tv_doc)
    TextView tvDoc;


    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!EasyPermissions.hasPermissions(getActivity(), Config.requestPermissions)) {
            return;
        }

        setStorageInfo();
        setCategoryCount();
        EventBus.getDefault().register(this);

    }

    private void setCategoryCount() {
        FileCategoryHelper helper = new FileCategoryHelper(getActivity());

        long picCount = helper.queryCategoryFileSize(FileCategoryHelper.FileCategory.Picture).count;
        tvImage.setText(String.format("图片(%s)", picCount));

        long videoCount = helper.queryCategoryFileSize(FileCategoryHelper.FileCategory.Video).count;
        tvVideo.setText(String.format("视频(%s)", videoCount));

        long musicCount = helper.queryCategoryFileSize(FileCategoryHelper.FileCategory.Music).count;
        tvAudio.setText(String.format("音频(%s)", musicCount));

        long docCount = helper.queryCategoryFileSize(FileCategoryHelper.FileCategory.Doc).count;
        tvDoc.setText(String.format("文档(%s)", docCount));
    }

    private void setStorageInfo() {
        Util.StorageInfo storageInfo = Util.getInternalStorage();
        if (storageInfo != null) {
            tvInternal.setText(String.format("已使用%s/%s", Util.convertStorage(storageInfo.used), Util.convertStorage(storageInfo.total)));
            pbInternal.setProgress(storageInfo.getUsedRate());
        }

        Util.StorageInfo externalStorage = Util.getExternalStorage();
        if (externalStorage != null) {
            tvExternal.setText(String.format("已使用%s/%s", Util.convertStorage(externalStorage.used), Util.convertStorage(externalStorage.total)));
            pbExternal.setProgress(storageInfo.getUsedRate());
        }
    }


    @Subscribe
    public void onRefresh(RefreshEvent event){
        setStorageInfo();
        setCategoryCount();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.iv_menu, R.id.iv_search, R.id.rl_pic, R.id.rl_video, R.id.rl_audio, R.id.rl_doc, R.id.iv_screenshot, R.id.iv_camera, R.id.iv_class, R.id.iv_favourite, R.id.iv_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                showMenuPop();
                break;
            case R.id.iv_search:
                SearchActivity.start(getActivity());
                break;


                //region 快捷访问 //截屏
            case R.id.iv_screenshot:
                ImgExploreListActivity.start(getActivity(), Config.BUCKET_SCREENSHOT);
                break;
            case R.id.iv_camera: //相机
                ImgExploreListActivity.start(getActivity(), Config.BUCKET_CAMERA);
                break;
            case R.id.iv_class: //互动课堂
                FileExploreActivity.start(getActivity(),FileExploreFragment.TYPE_CLASS);
                break;
            case R.id.iv_favourite://收藏
                FileExploreActivity.start(getActivity(),FileExploreFragment.TYPE_FAVORITE);
                break;
            case R.id.iv_download://下载
                FileExploreActivity.start(getActivity(),FileExploreFragment.TYPE_DOWNLOAD);
                break;
                //endregion



            //=====================================
            case R.id.rl_pic://图片
                ImgCategoryActivity.start(getActivity());
                break;
            case R.id.rl_video://视频
                ExploreListActivity.start(getActivity(), LoadType.VIDEO);
                break;
            case R.id.rl_audio://音频
                ExploreListActivity.start(getActivity(),LoadType.AUDIO);
                break;
            case R.id.rl_doc://文档
                ExploreListActivity.start(getActivity(),LoadType.DOC);
                break;

            //========================================

        }


    }

    private void showMenuPop() {
        PopMenu popupMenu = new PopMenu(getActivity());
        popupMenu.showAsDropDown(ivMenu, 200, 0);
        popupMenu.setOnMenuClickListener(new PopMenu.MenuClickListener() {
            @Override
            public void onCleanClick() {
                CleanUpActivity.start(getActivity());
            }

            @Override
            public void onSettingClick() {

            }
        });
    }

}
