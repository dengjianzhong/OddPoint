package com.zhong.toollib.helper;

import android.app.ActivityManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/**
 * The type Media helper.
 */
public class MediaHelper {
    /**
     * The constant mediaPlayer.
     */
    public MediaPlayer mediaPlayer;
    private boolean isLocalResources;
    private String resources;
    private boolean looping;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer.OnCompletionListener listener;
    private int raw;
    private static MediaHelper mediaHelper;
    private boolean isStart;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static MediaHelper getInstance() {
        synchronized (MediaHelper.class) {
            if (mediaHelper == null) {
                mediaHelper = new MediaHelper();
            }
        }
        return mediaHelper;
    }

    //--------------------------------设置播放参数------------------------------------------------

    /**
     * 设置本地资源
     *
     * @param raw the raw
     * @return the raw
     */
    public MediaHelper setRaw(int raw) {
        this.raw = raw;
        return this;
    }

    /**
     * 设置本地/网络资源
     *
     * @param resources the resources
     * @return the resource
     */
    public MediaHelper setResource(String resources) {
        this.resources = resources;
        return this;
    }

    /**
     * 设置是否循环播放
     *
     * @param looping the looping
     * @return the looping
     */
    public MediaHelper setLooping(boolean looping) {
        this.looping = looping;
        return this;
    }

    /**
     * 用于绘制视频的SurfaceHolder
     *
     * @param surfaceHolder the surface holder
     * @return the surface holder
     */
    public MediaHelper setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        return this;
    }

    /**
     * 音视频播放完成回调监听
     *
     * @param listener the listener
     * @return the completion listener
     */
    public MediaHelper setCompletionListener(MediaPlayer.OnCompletionListener listener) {
        this.listener = listener;
        return this;
    }

    //---------------------------------------------------------------------------------------

    /**
     * 用于播放本地资源
     *
     * @param context the context
     * @return the media player
     */
    public MediaPlayer prepare(Context context) {
        if (getProcessName(context).equals(context.getPackageName())) {//防止多个进程同时调用出现重音
            mediaPlayer = MediaPlayer.create(context, raw);
            mediaPlayer.setOnCompletionListener(listener);
            mediaPlayer.setLooping(looping);
            mediaPlayer.start();
            isLocalResources = true;
        }
        return mediaPlayer;
    }

    /**
     * 用于播放本地/网络资源
     */
    public MediaHelper prepare() {
        if (isLocalResources && mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = mediaPlayer == null ? new MediaPlayer() : mediaPlayer;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setDataSource(resources);
            mediaPlayer.setOnCompletionListener(listener);
            mediaPlayer.setLooping(looping);
        } catch (IOException e) {
            e.printStackTrace();
        }
        isStart = false;

        return this;
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * 播放
     */
    public void startPlay() {
        try {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                if (!isStart) {
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            isStart = true;
                        }
                    });
                } else {
                    MediaHelper.this.prepare();
                    MediaHelper.this.startPlay();
                }
            }
        } catch (IllegalStateException e) {
//            throw new UnsupportedOperationException("每次在你调用startPlay之前必须调用prepare");
        }
    }

    /**
     * 暂停后继续播放
     */
    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    /**
     * 释放.
     */
    public void release() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 重置
     */
    public void reset() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
        }
    }


    private static String getProcessName(Context cxt) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
}
