package com.zhong.toollib.helper;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

public class SyncThreadHelper {

    private static SyncThreadHelper syncThreadHelper;
    private Handler handler;
    private TextView textView;
    private String data;

    public static SyncThreadHelper getInstance() {
        synchronized (SyncThreadHelper.class) {
            if (syncThreadHelper == null) {
                syncThreadHelper = new SyncThreadHelper();
            }
        }
        return syncThreadHelper;
    }

    private SyncThreadHelper() {
        registerSyncThread();
    }

    public void registerSyncThread() {
        handler = new Handler(Looper.getMainLooper());
    }


    private final Runnable mPostValueRunnable = new Runnable() {
        @Override
        public void run() {
            textView.setText(data);
        }
    };

    public SyncThreadHelper setPostValue(TextView textView, String data) {
        if (handler == null)
            throw new RuntimeException("You must register registerSyncThread before calling setPostValue");

        if (textView == null) throw new NullPointerException("textView is null");
        if (data == null) throw new NullPointerException("data is null");


        this.textView = textView;
        this.data = data;
        handler.post(mPostValueRunnable);

        return this;
    }
}
