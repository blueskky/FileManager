package com.whty.eschoolbag.filemanager.ui.preview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.baidu.cloud.media.player.IMediaPlayer;
import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.widget.BDCloudVideoView;
import com.whty.eschoolbag.filemanager.widgetview.SimpleMediaController;

import java.util.Timer;
import java.util.TimerTask;

public class SimplePlayActivity extends Activity implements IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnBufferingUpdateListener, BDCloudVideoView.OnPlayerStateListener , View.OnClickListener {
    private static final String TAG = "SimplePlayActivity";

    /**
     * 您的AK 请到http://console.bce.baidu.com/iam/#/iam/accesslist获取
     */
    private String ak = ""; // 请录入您的AK !!!

    private BDCloudVideoView cloudVideoView = null;
    private SimpleMediaController mediaController = null;
    private RelativeLayout mViewHolder = null;

    private Timer barTimer;

    /**
     * 记录播放位置
     */
    private int mLastPos = 0;
    private String url;
    private View rlTop;

    public static void start(Context context, String filePath) {
        context.startActivity(new Intent(context, SimplePlayActivity.class).putExtra("url", filePath));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);


        setContentView(R.layout.activity_simple_video_playing);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        url = getIntent().getStringExtra("url");

        initUI();
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
        mediaController = (SimpleMediaController) findViewById(R.id.media_controller_bar);

        rlTop = findViewById(R.id.rl_top);
        findViewById(R.id.iv_back).setOnClickListener(this);
        /**
         * 设置ak
         */
        BDCloudVideoView.setAK(ak);

        cloudVideoView = new BDCloudVideoView(this);
        cloudVideoView.setVideoPath(url);

        //cloudVideoView.setVideoScalingMode(BDCloudVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        cloudVideoView.setVideoScalingMode(BDCloudVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);


        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(-1, -1);
        rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mViewHolder.addView(cloudVideoView, rllp);

        /**
         * 注册listener
         */
        cloudVideoView.setOnPreparedListener(this);
        cloudVideoView.setOnCompletionListener(this);
        cloudVideoView.setOnErrorListener(this);
        cloudVideoView.setOnInfoListener(this);
        cloudVideoView.setOnBufferingUpdateListener(this);
        cloudVideoView.setOnPlayerStateListener(this);

        mediaController.setMediaPlayerControl(cloudVideoView);

        cloudVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");
        if (cloudVideoView != null) {
            cloudVideoView.enterForeground();
        }
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        if (cloudVideoView != null) {
            cloudVideoView.enterBackground();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cloudVideoView != null) {
            cloudVideoView.stopPlayback();
        }
        Log.v(TAG, "onDestroy");
    }

    /**
     * 检测'点击'空白区的事件，若播放控制控件未显示，设置为显示，否则隐藏。
     *
     * @param v
     */
    public void onClickEmptyArea(View v) {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        if (this.mediaController != null) {
            if (mediaController.getVisibility() == View.VISIBLE) {
                mediaController.hide();
                rlTop.setVisibility(View.GONE);

            } else {
                mediaController.show();
                rlTop.setVisibility(View.VISIBLE);
                hideOuterAfterFiveSeconds();
            }
        }
    }

    private void hideOuterAfterFiveSeconds() {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        barTimer = new Timer();
        barTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (mediaController != null) {
                    mediaController.getMainThreadHandler().post(new Runnable() {

                        @Override
                        public void run() {
                            mediaController.hide();
                            rlTop.setVisibility(View.GONE);
                        }

                    });
                }
            }

        }, 5 * 1000);

    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        // restart player?

        return false;
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        if (mediaController != null && cloudVideoView != null) {
            mediaController.onTotalCacheUpdate(percent * cloudVideoView.getDuration() / 100);
        }
    }

    @Override
    public void onPlayerStateChanged(BDCloudVideoView.PlayerState nowState) {
        if (mediaController != null) {
            mediaController.changeState();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
