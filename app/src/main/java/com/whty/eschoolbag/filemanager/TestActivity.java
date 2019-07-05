package com.whty.eschoolbag.filemanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        DisplayMetrics dm = getResources().getDisplayMetrics();
        //手机宽度dp值 = 手机实际宽度像素px / 手机屏幕密度比
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        int min = Math.min(widthPixels, heightPixels);
        float widthDP = min / dm.density;

        int px2Dp = Px2Dp(this, min);

        Toast.makeText(this, px2Dp + "---"+widthDP, Toast.LENGTH_SHORT).show();
    }


    public int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
