package com.zhong.toollib.weight;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.zhong.toollib.helper.MediaHelper;

public class SmVideo extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder surfaceHolder;
    private OnCompletionListener completionListener;
    private String path;
    private MediaHelper mediaHelper;

    public SmVideo(Context context) {
        super(context);
        init();
    }

    public SmVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SmVideo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setResourceData(String path, OnCompletionListener completionListener) {
        this.path = path;
        this.completionListener = completionListener;
    }

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        MediaHelper.getInstance()
                .setLooping(false)
                .setResource("https://vodpub2.v.news.cn/original/20190917/93bcc38f5a274b988392254cde5fc436.mp4")
                .setSurfaceHolder(holder)
                .setCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        completionListener.onCompletion();
                    }
                })
                .prepare();

        mediaHelper = MediaHelper.getInstance();
        mediaHelper.mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                changeVideoSize(mp);
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {

    }

    public void changeVideoSize(MediaPlayer mediaPlayer) {
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        int surfaceWidth = this.getWidth();
        int surfaceHeight = this.getHeight();

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
        } else {
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoWidth / (float) surfaceHeight), (float) videoHeight / (float) surfaceWidth);
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.setLayoutParams(layoutParams);
    }


    /**
     * 暂停播放
     */
    public void pause() {
        mediaHelper.pause();
    }

    /**
     * 播放
     */
    public void startPlay() {
        mediaHelper.startPlay();
    }

    /**
     * 播放
     */
    public void resume() {
        mediaHelper.resume();
    }

    /**
     * 释放.
     */
    public void release() {
        mediaHelper.release();
    }

    /**
     * 重置
     */
    public void reset() {
        mediaHelper.reset();
    }

    public interface OnCompletionListener {
        void onCompletion();
    }
}
