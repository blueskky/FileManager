package com.whty.eschoolbag.filemanager.ui.preview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.util.DensityUtil;
import com.whty.eschoolbag.filemanager.util.LightnessController;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 视频播放类
 *
 * @author chen
 */
public class VideoViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG =VideoViewActivity.class.getClass().getSimpleName();
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.top_layout)
    RelativeLayout topLayout;

    // 头部View
    private View mTopView;

    // 底部View
    private View mBottomView;
    // 视频播放拖动条
    private SeekBar mSeekBar;
    private ImageView mPlay;
    private TextView mPlayTime;
    private TextView mDurationTime;


    // 屏幕宽高
    private float width;
    private float height;

    // 视频播放时间
    private int playTime;

    private String videoUrl;
    private String videoName;
    // 自动隐藏顶部和底部View的时间
    private static final int HIDE_TIME = 5000;


    SurfaceView surfaceView;
    MediaPlayer mVideo;
    ContentLoadingProgressBar mCircularProgressBar;
    TextView text_progres;
    Timer timer;

    private boolean fromRecord = false;


    public static void launchForPlay(Context context, String url, String name) {
        Intent intent = new Intent(context, VideoViewActivity.class);
        intent.putExtra("videoUrl", url);
        intent.putExtra("videoName", name);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.media_activity_video_player);
        ButterKnife.bind(this);

        initParams();
        initView();
    }

    protected void initParams() {
        videoUrl = this.getIntent().getStringExtra("videoUrl");
        if (TextUtils.isEmpty(videoUrl)) {
            Toast.makeText(this,"找不到预览地址",Toast.LENGTH_SHORT).show();
        }
        videoName = this.getIntent().getStringExtra("videoName");
        if (TextUtils.isEmpty(videoName)) {
            if (videoUrl.startsWith("http")) {
                videoName = videoUrl;
            } else {
                File file = new File(videoUrl);
                if (file != null && file.exists()) {
                    videoName = file.getName();
                } else {
                    videoName = videoUrl;
                }
            }
        }
        fromRecord = getIntent().getBooleanExtra("fromRecord", false);


    }


    protected void initView() {
        text_progres = (TextView) findViewById(R.id.text_progres);
        mCircularProgressBar = (ContentLoadingProgressBar) findViewById(R.id.mCircularProgressBar);
        mPlayTime = (TextView) findViewById(R.id.play_time);
        mDurationTime = (TextView) findViewById(R.id.total_time);
        mPlay = (ImageView) findViewById(R.id.play_btn);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mTopView = findViewById(R.id.top_layout);
        mBottomView = findViewById(R.id.bottom_layout);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);

        tvConfirm = (TextView) findViewById(R.id.tv_confirm);


        tvConfirm.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        findViewById(R.id.iv_left).setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

        if (!TextUtils.isEmpty(videoName)) {
            tvTitle.setText(videoName);
        }


        if (fromRecord) {
            tvConfirm.setVisibility(View.VISIBLE);
        } else {
            tvConfirm.setVisibility(View.GONE);
        }

        surfaceView.setOnTouchListener(mTouchListener);

        initMediaPlayer();
    }






    void setProgres(int progres) {
        if (text_progres.getVisibility() == View.GONE) {
            text_progres.setVisibility(View.VISIBLE);
            mCircularProgressBar.setVisibility(View.VISIBLE);
        }

        text_progres.setText(progres + "%");
    }

    void setProgresInvisible() {
        text_progres.setVisibility(View.GONE);
        mCircularProgressBar.setVisibility(View.GONE);
    }

    private void initMediaPlayer() {
        // TODO Auto-generated method stub
        mVideo = new MediaPlayer();
//    	mVideo = MediaPlayer.create(this, Uri.parse(videoUrl));
//    	mVideo.reset();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // 开始播放
                play();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });
    }

    private void play() {
        try {
            mVideo.setDataSource(videoUrl);
            // 把视频画面输出到SurfaceView
            mVideo.setDisplay(surfaceView.getHolder());
            mVideo.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

                @Override
                public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
                       /* 打印缓冲的百分比, 如果缓冲 */
                    System.out.println("缓冲了的百分比 : " + arg1 + " %");
                    if (arg0.isPlaying()) {
                        setProgresInvisible();
                    } else {
                        if (arg1 < 100) {
                            setProgres(arg1);
                        } else {
                            setProgresInvisible();
                        }
                    }
                }
            });
            mVideo.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        setProgresInvisible();
                    }
                    return false;
                }
            });
            mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer arg0) {
//                	ToastUtil.showToast(VideoPlayerNewActivity.this, "开始播放");
                    // TODO Auto-generated method stub9
                    Log.d(TAG, "视频准备好了");
                    surfaceView.setBackgroundColor(Color.TRANSPARENT);
                    setSurfaceViewHeightAndWidth();
                    setProgresInvisible();
                    mHandler.removeCallbacks(hideRunnable);
                    mDurationTime.setText(formatTime(mVideo.getDuration()));
                    Log.d(TAG, "mVideo.getDuration() = " + mVideo.getDuration());
                    mSeekBar.setMax(mVideo.getDuration());
                    timer = new Timer();
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(1);
                        }
                    }, 0, 10);
                    mVideo.start();

                    mHandler.sendEmptyMessageDelayed(3, 1000);


                }
            });
            mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer arg0) {
                    // TODO Auto-generated method stub
//                	ToastUtil.showToast(VideoPlayerNewActivity.this, "结束播放");
                    if (mPlay != null) {
                        mPlay.setImageResource(R.drawable.video_btn_down);
                    }

                    mPlayTime.setText("00:00");
                    mSeekBar.setProgress(0);
                }
            });

            mVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    System.out.println("MediaPlayer 出现错误 what : " + what + " , extra : " + extra);
                    showToast("播放失败");
                    onBackPressed();
                    return false;
                }
            });

            mVideo.prepareAsync();

        } catch (Exception e) {
        }

    }

    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    void setSurfaceViewHeightAndWidth() {
        Display mDisplay = getWindowManager().getDefaultDisplay();
        if(mVideo == null){
            return;
        }
        int videoWidth = 0;
        int videoHeight = 0;
        try {
            videoWidth = mVideo.getVideoWidth();
            videoHeight = mVideo.getVideoHeight();
        }catch (Exception e){

        }


        Log.d(TAG, "setSurfaceViewHeightAndWidth: videoWidth=" + videoWidth + " videoHeight=" + videoHeight);
        if (videoHeight == 0 || videoWidth == 0) {
            return;
        }

        // 设置SurfaceView的宽和高
        int w = DensityUtil.getScreenWidth(this);
        int h = DensityUtil.getScreenHeight(this);

        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) surfaceView.getLayoutParams();
        if (videoHeight > videoWidth) {    // 高度大于宽度，竖屏视频
            lp.height = h;
            lp.width = lp.height * videoWidth / videoHeight;
        } else {// 横屏视频
            lp.width = w;
            lp.height = lp.width * videoHeight / videoWidth;
        }

        surfaceView.setLayoutParams(lp);
//        surfaceView.setLayoutParams(new LinearLayout.LayoutParams(w, w / videoWidth * (videoHeight)));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            height = ViewUtil.getDisplayMetricsWidth(this);
//            width = ViewUtil.getDisplayMetricsHeight(this);
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            width = ViewUtil.getDisplayMetricsWidth(this);
//            height = ViewUtil.getDisplayMetricsHeight(this);
//        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(hideRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
//                int time = progress * mVideo.getDuration() / 100;
                mVideo.seekTo(progress);
            }
        }
    };

    private void backward(float delataX) {
        int current = mVideo.getCurrentPosition();
        int backwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current - backwardTime;
        mVideo.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        mPlayTime.setText(formatTime(currentTime));
    }

    private void forward(float delataX) {
        int current = mVideo.getCurrentPosition();
        int forwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current + forwardTime;
        mVideo.seekTo(currentTime);
        int duration = mVideo.getDuration();
        if (duration != 0) {
            mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        } else {
            mSeekBar.setProgress(0);
        }
        mPlayTime.setText(formatTime(currentTime));
    }

    private void lightDown(float delatY) {
        int down = (int) (delatY / height * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) - down;
        LightnessController.setLightness(this, transformatLight);
    }

    private void lightUp(float delatY) {
        int up = (int) (delatY / height * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) + up;
        LightnessController.setLightness(this, transformatLight);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        mHandler.removeMessages(0);
        mHandler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mVideo != null && mVideo.isPlaying() && mVideo.getCurrentPosition() > 0) {
                        mPlayTime.setText(formatTime(mVideo.getCurrentPosition()));
                        Log.d(TAG, "mVideo.getCurrentPosition() = " + mVideo.getCurrentPosition());
                        mSeekBar.setProgress(mVideo.getCurrentPosition());
//                    if (mVideo.getCurrentPosition() > mVideo.getDuration() - 100) {
//                        mPlayTime.setText("00:00");
//                        mSeekBar.setProgress(0);
//                    }
                    } /*else {
                    mPlayTime.setText("00:00");
                    mSeekBar.setProgress(0);
                }*/
                    break;
                case 2:
                    showOrHide();
                    break;
                case 3:
                    setSurfaceViewHeightAndWidth();
                    break;

                default:
                    break;
            }
        }
    };

    private Runnable hideRunnable = new Runnable() {

        @Override
        public void run() {
            showOrHide();
        }
    };

    @SuppressLint("SimpleDateFormat")
    private String formatTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    private float mLastMotionX;
    private float mLastMotionY;
    private int startX;
    private int startY;
    private int threshold;
    private boolean isClick = true;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final float x = event.getX();
            final float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    startX = (int) x;
                    startY = (int) y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mLastMotionX;
                    float deltaY = y - mLastMotionY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    // 声音调节标识
                    boolean isAdjustAudio = false;
                    if (absDeltaX > threshold && absDeltaY > threshold) {
                        if (absDeltaX < absDeltaY) {
                            isAdjustAudio = true;
                        } else {
                            isAdjustAudio = false;
                        }
                    } else if (absDeltaX < threshold && absDeltaY > threshold) {
                        isAdjustAudio = true;
                    } else if (absDeltaX > threshold && absDeltaY < threshold) {
                        isAdjustAudio = false;
                    } else {
                        return true;
                    }
                    if (isAdjustAudio) {
                        if (x < width / 2) {
                            if (deltaY > 0) {
                                lightDown(absDeltaY);
                            } else if (deltaY < 0) {
                                lightUp(absDeltaY);
                            }
                        } else {
                            if (deltaY > 0) {
                                // volumeDown(absDeltaY);
                            } else if (deltaY < 0) {
                                // volumeUp(absDeltaY);
                            }
                        }

                    } else {
                        if (deltaX > 0) {
                            forward(absDeltaX);
                        } else if (deltaX < 0) {
                            backward(absDeltaX);
                        }
                    }
                    mLastMotionX = x;
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(x - startX) > threshold || Math.abs(y - startY) > threshold) {
                        isClick = false;
                    }
                    mLastMotionX = 0;
                    mLastMotionY = 0;
                    startX = (int) 0;
                    if (isClick) {
                        showOrHide();
                    }
                    isClick = true;
                    break;

                default:
                    break;
            }
            return true;
        }

    };

    @Override
    public void onBackPressed() {
        if (mVideo != null) {
            mVideo.stop();
            mVideo.release();
            mVideo = null;
        }
        finish();
    }

    ;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_btn:
                if (mVideo.isPlaying()) {
                    mVideo.pause();
                    mPlay.setImageResource(R.drawable.video_btn_down);
                } else {
                    mVideo.start();
                    mPlay.setImageResource(R.drawable.video_btn_on);
                }
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_confirm:
                setBackResult();
                break;
            default:
                break;
        }
    }

    private void setBackResult() {
        if (mVideo.isPlaying()) {
            mVideo.stop();
        }
    }

    private void showOrHide() {
        if (mTopView.getVisibility() == View.VISIBLE) {
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.option_leave_from_top);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mTopView.setVisibility(View.GONE);
                }
            });
            mTopView.startAnimation(animation);
            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.option_leave_from_bottom);
            animation1.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mBottomView.setVisibility(View.GONE);
                }
            });
            mBottomView.startAnimation(animation1);
        } else {
            mTopView.setVisibility(View.VISIBLE);
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.option_entry_from_top);
            mTopView.startAnimation(animation);

            mBottomView.setVisibility(View.VISIBLE);
            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.option_entry_from_bottom);
            mBottomView.startAnimation(animation1);
            mHandler.removeCallbacks(hideRunnable);
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }
    }

    private class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }


}
