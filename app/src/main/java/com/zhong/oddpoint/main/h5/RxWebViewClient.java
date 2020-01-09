package com.zhong.oddpoint.main.h5;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;

public class RxWebViewClient extends WebViewClient {

    private HashMap<String, Long> timer = new HashMap<>();

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, String url) {

        //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放，
        // 禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ，
        // 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
        if (url.startsWith("intent://") && url.contains("com.youku.phone")) {
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        timer.put(url, System.currentTimeMillis());
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (timer.get(url) != null) {
            long overTime = System.currentTimeMillis();
            Long startTime = timer.get(url);
        }

    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
        super.onReceivedSslError(view, handler, error);
    }
}
