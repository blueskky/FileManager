package com.whty.eschoolbag.filemanager.ui.preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.bean.FileInfo;
import com.whty.eschoolbag.filemanager.db.FavoriteDatabaseHelper;
import com.whty.eschoolbag.filemanager.widgetview.DetailDialog;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImagePreviewActivity extends AppCompatActivity {


    private static final int DISMESS = 100;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_detail)
    ImageView ivDetail;
    @BindView(R.id.iv_collection)
    ImageView ivCollection;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.root)
    RelativeLayout root;
    private List<FileInfo> data;
    FavoriteDatabaseHelper helper = new FavoriteDatabaseHelper(this, null);
    private MyPagerAdapter pagerAdapter;

    public static void start(Context context, List<FileInfo> data, int position) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra("data", ((Serializable) data));
        intent.putExtra("index", position);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_image_preview);
        ButterKnife.bind(this);


        data = (List<FileInfo>) getIntent().getSerializableExtra("data");
        int index = getIntent().getIntExtra("index", 0);

        pagerAdapter = new MyPagerAdapter(data);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(index);

        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("lhq","onPageSelected=="+position);
                FileInfo fileInfo = data.get(position);
                notifyFavorite(fileInfo);
            }
        });

        notifyFavorite(data.get(index));

    }

    @OnClick({R.id.iv_detail, R.id.iv_collection})
    public void onViewClicked(View view) {
        int currentItem = viewpager.getCurrentItem();
        FileInfo fileInfo = data.get(currentItem);

        switch (view.getId()) {
            case R.id.iv_detail:
                DetailDialog detailDialog = new DetailDialog(this);
                detailDialog.show();
                detailDialog.setDetailInfo(fileInfo);

                break;
            case R.id.iv_collection:
                if (helper.isFavorite(fileInfo.getFilePath())) {
                    helper.delete(fileInfo.getFilePath());
                } else {
                    helper.insert(fileInfo.getFileName(), fileInfo.getFilePath());
                }
                notifyFavorite(fileInfo);
                break;
        }
    }


    class MyPagerAdapter extends PagerAdapter {
        List<FileInfo> data;

        public MyPagerAdapter(List<FileInfo> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(ImagePreviewActivity.this).inflate(R.layout.pic_preview_item, null);
            PhotoView photoView = view.findViewById(R.id.photoView);

            FileInfo fileInfo = data.get(position);
            Glide.with(ImagePreviewActivity.this).load(fileInfo.getFilePath()).into(photoView);


            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    if (llHeader.getVisibility() == View.GONE) {
                        llHeader.setVisibility(View.VISIBLE);
                        handler.removeMessages(DISMESS);
                        handler.sendEmptyMessageDelayed(DISMESS, 5000);
                    } else {
                        llHeader.setVisibility(View.GONE);
                    }
                }
            });



            container.addView(view);
            return view;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    private void notifyFavorite(FileInfo fileInfo) {
        if (helper.isFavorite(fileInfo.getFilePath())) {
            ivCollection.setImageResource(R.drawable.iv_collection_ed);
        } else {
            ivCollection.setImageResource(R.drawable.iv_collection);
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == DISMESS) {
                llHeader.setVisibility(View.GONE);
            }
        }
    };

}
