package com.whty.eschoolbag.filemanager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.whty.eschoolbag.filemanager.ui.category.CategoryFragment;
import com.whty.eschoolbag.filemanager.ui.recently.RecentlyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.rb_recently)
    RadioButton rbRecently;
    @BindView(R.id.rb_category)
    RadioButton rbCategory;
    @BindView(R.id.rb_cloud)
    RadioButton rbCloud;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private ArrayList<Fragment> list;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

        //        requestPermission();

        initRadioGroup();

        checkPermission();

        if (BuildConfig.DEBUG) {
            getDpi();
        }


    }

    private void initRadioGroup() {
        //定义底部标签图片大小
        Drawable drawable1 = getResources().getDrawable(R.drawable.selector_radio_recent);
        drawable1.setBounds(0, 0, 48, 48);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        rbRecently.setCompoundDrawables(drawable1, null, null, null);

        //定义底部标签图片大小
        Drawable drawable2 = getResources().getDrawable(R.drawable.selector_radio_category);
        drawable2.setBounds(0, 0, 48, 48);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        rbCategory.setCompoundDrawables(drawable2, null, null, null);


        //定义底部标签图片大小
        Drawable drawable3 = getResources().getDrawable(R.drawable.selector_radio);
        drawable3.setBounds(0, 0, 48, 48);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        rbCloud.setCompoundDrawables(drawable3, null, null, null);
    }


    private void checkPermission() {
        if (EasyPermissions.hasPermissions(this, Config.requestPermissions)) {
            setViewPager();
            // 已经申请过权限，做想做的事
        } else {
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(this, getString(R.string.request_permission), 0, Config.requestPermissions);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //    private void requestPermission() {
    //        final RxPermissions rxPermissions = new RxPermissions(this);
    //
    //        rxPermissions
    //                .requestEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE,
    //                        Manifest.permission.READ_EXTERNAL_STORAGE)
    //                .subscribe(permission -> { // will emit 1 Permission object
    //                    if (permission.granted) {
    //                        // All permissions are granted !
    //                        setViewPager();
    //                        Log.d("lhq", "granted ");
    //                    } else if (permission.shouldShowRequestPermissionRationale) {
    //                        // At least one denied permission without ask never again
    //                        Log.d("lhq", "denied ");
    //                        finish();
    //                    } else {
    //                        // At least one denied permission with ask never again
    //                        // Need to go to the settings
    //                        Log.d("lhq", "denied  no ask=" + permission.name);
    //
    //                        showDialog();
    //                    }
    //                });
    //    }

    private void showDialog() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("提示");
        localBuilder.setIcon(R.mipmap.ic_launcher);
        localBuilder.setMessage("该应用需要获取存储权限,请到设置中打开存储权限");
        localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
                startActivity(intent);
                finish();
            }
        });

        localBuilder.setCancelable(false).create();
        localBuilder.show();
    }

    private void setViewPager() {
        list = new ArrayList<>();
        list.add(new RecentlyFragment());
        list.add(new CategoryFragment());
        //        list.add(new CloudFragment());

        viewpager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_recently:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.rb_category:
                        viewpager.setCurrentItem(1);
                        break;
                    case R.id.rb_cloud:
                        viewpager.setCurrentItem(2);
                        break;
                }
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        rbRecently.setChecked(true);
                        break;
                    case 1:
                        rbCategory.setChecked(true);
                        break;
                    case 2:
                        rbCloud.setChecked(true);
                        break;
                }
            }
        });


        viewpager.setCurrentItem(1);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        setViewPager();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d("lhq", "onPermissionsDenied=" + perms.toString());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //永久拒绝
            new AppSettingsDialog.Builder(this).setRationale(getString(R.string.request_permission)).setTitle(getString(R.string.request_title)).build().show();
            //弹出个对话框
        } else {
            String[] permsww = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(this, getString(R.string.request_permission), 0, permsww);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            checkPermission();
        }
    }


    private void getDpi() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // 屏幕的逻辑密度，是密度无关像素（dip）的缩放因子，160dpi是系统屏幕显示的基线，1dip = 1px， 所以，在160dpi的屏幕上，density = 1， 而在一个120dpi屏幕上 density = 0.75。
        float density = metrics.density;

        //  屏幕的绝对宽度（像素）
        int screenWidth = metrics.widthPixels;

        // 屏幕的绝对高度（像素）
        int screenHeight = metrics.heightPixels;


        Log.d("lhq", "width=" + screenWidth + "  height-" + screenHeight);

        //  屏幕上字体显示的缩放因子，一般与density值相同，除非在程序运行中，用户根据喜好调整了显示字体的大小时，会有微小的增加。
        float scaledDensity = metrics.scaledDensity;

        // X轴方向上屏幕每英寸的物理像素数。
        float xdpi = metrics.xdpi;

        // Y轴方向上屏幕每英寸的物理像素数。
        float ydpi = metrics.ydpi;

        // 每英寸的像素点数，屏幕密度的另一种表示。densityDpi = density * 160.
        float desityDpi = metrics.densityDpi;

        int min = Math.min(screenWidth, screenHeight);

        double dpi = min / (metrics.densityDpi / 160.0);

        double physicsScreenSize = getPhysicsScreenSize(this);

        Log.d("lhq", "density=" + density + "\nwidth=" + screenWidth + "\n heigiht=" + screenHeight + "\nxdpi=" + xdpi + "\nydpi=" + ydpi + "\n " +
                "desityDpi=" + desityDpi + "\n dpi=" + dpi + "\n physices =" + physicsScreenSize);
    }


    public static double getPhysicsScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;//得到屏幕的密度值，但是该密度值只能作为参考，因为他是固定的几个密度值。
        double x = Math.pow(point.x / dm.xdpi, 2);//dm.xdpi是屏幕x方向的真实密度值，比上面的densityDpi真实。
        double y = Math.pow(point.y / dm.ydpi, 2);//dm.xdpi是屏幕y方向的真实密度值，比上面的densityDpi真实。
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }


    class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
