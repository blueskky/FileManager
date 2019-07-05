package com.whty.eschoolbag.filemanager.ui.preview;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.whty.eschoolbag.filemanager.R;
import com.whty.eschoolbag.filemanager.util.FileUtil;
import com.whty.eschoolbag.filemanager.widgetview.Anticlockwise;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 音频播放类
 *
 * @author chen
 */
public class AudioPlayActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "AudioPlayActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.id_timer)
    Anticlockwise idTimer;
    @BindView(R.id.btn_play)
    Button btnPlay;
    @BindView(R.id.layout_confirm)
    LinearLayout layoutConfim;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_again)
    Button btnAgain;

    private String filePath;
    private MediaPlayer mediaPlayer;

    private boolean isPlay = false;
    private boolean isFirstPlay = true;
    private boolean fromLocal = false;
    private boolean fromRecord = false;

    private int Duration = 0;
    private int currentPosition = 0;


    public static void launchForLocal(Context context, String filePath) {
        Intent intent = new Intent(context, AudioPlayActivity.class);
        intent.putExtra("filePath", filePath);
        intent.putExtra("fromLocal", true);
        context.startActivity(intent);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            idTimer.stop();
            initTimer();
            onMeadiaFinished();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.media_activity_audioplayer);
        ButterKnife.bind(this);

        initParams();
        initView();
    }

    protected void initParams() {
        Intent intent = getIntent();
        if (intent != null) {
            filePath = intent.getStringExtra("filePath");
            fromLocal = intent.getBooleanExtra("fromLocal", false);
            fromRecord = intent.getBooleanExtra("fromRecord", false);
        }

        if(TextUtils.isEmpty(filePath)){
            showToast("地址为空");
            finish();
            return;
        }

        initMediaPlayer(filePath);


        if (fromRecord) {
            layoutConfim.setVisibility(View.VISIBLE);
            btnConfirm.setEnabled(true);
            btnAgain.setEnabled(true);
        }else {
            layoutConfim.setVisibility(View.GONE);
            btnConfirm.setEnabled(false);
            btnAgain.setEnabled(false);
        }
    }

    protected void initView() {
        ivBack.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnAgain.setOnClickListener(this);

        idTimer.setOnTimeCompleteListener(new Anticlockwise.OnTimeCompleteListener() {
            @Override
            public void onTimeComplete() {
                handler.sendEmptyMessage(0);
            }
        });
    }



    private String getResourceId(String data) {
        String id = "";
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        try {
            JSONObject jsonObject = new JSONObject(data);
            id = jsonObject.getString("resourceId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return id;
    }


    void initMediaPlayer(String fileName) {
        Log.d(TAG, "filePath = " + fileName);
        if (!FileUtil.checkFileExist(fileName)) {
            showToast("获取音频失败");
            finish();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(AudioPlayActivity.this, Uri.parse(fileName));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {

            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "mediaPlayer.getDuration()" + mediaPlayer.getDuration());
                Duration = (int) Math.ceil((mediaPlayer.getDuration() / 1000));
                initTimer();
            }
        });
        // 通过异步的方式装载媒体资源
        mediaPlayer.prepareAsync();
    }

    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void onMeadiaFinished() {
        if (btnPlay != null) {
            btnPlay.setEnabled(true);
            btnPlay.setBackgroundResource(R.drawable.audio_play_selector);
        }
        currentPosition = 0;
        isPlay = false;
    }

    private void playAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            isPlay = true;
            idTimer.reStart();
        }
    }

    private void pauseAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            currentPosition = mediaPlayer.getCurrentPosition();
            btnPlay.setBackgroundResource(R.drawable.audio_play_selector);
        }
        isPlay = false;
        idTimer.onPause();
    }

    private void goOnAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(currentPosition);
            mediaPlayer.start();
        }
        btnPlay.setBackgroundResource(R.drawable.audio_pause_selector);
        isPlay = true;
        idTimer.onResume();
    }

    void initTimer() {
        //网络音频预览、本地选择上传前的预览、本地音频上传后的预览
        idTimer.initTime(Duration);
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        pauseAudio();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        super.onDestroy();
    }

    private void doBack() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                doBack();
                break;
            case R.id.btn_play:
                if (isFirstPlay) {
                    playAudio();
                    btnPlay.setBackgroundResource(R.drawable.audio_pause_selector);
                    isFirstPlay = false;
                } else {
                    if (isPlay) {
                        pauseAudio();
                    } else {
                        goOnAudio();
                    }
                }
                break;
            case R.id.btn_confirm:

                break;
            case R.id.btn_again:
                doBack();
                break;
        }
    }
}