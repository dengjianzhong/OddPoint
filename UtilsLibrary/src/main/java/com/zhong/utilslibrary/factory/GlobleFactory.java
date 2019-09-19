package com.zhong.utilslibrary.factory;

import android.app.Application;
import android.content.Context;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;

public class GlobleFactory extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        initEmoji();//初始化Emoji  越早越好
    }

    private void initEmoji() {
        BundledEmojiCompatConfig config = new BundledEmojiCompatConfig(context);
        EmojiCompat.init(config);
    }

    public static Context getGlobleContext() {
        return context;
    }
}