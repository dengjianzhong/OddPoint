package com.zhong.utilslibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * JSON动画工厂类
 */
public class LottieAnimationFactory {
    private static int match = LinearLayout.LayoutParams.MATCH_PARENT;
    private static int wrap = LinearLayout.LayoutParams.WRAP_CONTENT;
    private static LinearLayout.LayoutParams layoutParams;


    private static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static View getLoaddingView(Context context, String Url) {
        layoutParams = new LinearLayout.LayoutParams(match, match);
        LinearLayout rootView = new LinearLayout(context);
        rootView.setLayoutParams(layoutParams);
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setGravity(Gravity.CENTER);

        layoutParams = new LinearLayout.LayoutParams(dip2px(context, 172), dip2px(context, 172));
        LottieAnimationView animationView = new LottieAnimationView(context);
        animationView.setLayoutParams(layoutParams);
        animationView.loop(true);
        animationView.playAnimation();
        startAnimation(context, animationView, Url);
        rootView.addView(animationView);

        layoutParams = new LinearLayout.LayoutParams(match, wrap);
        TextView tipView = new TextView(context);
        tipView.setLayoutParams(layoutParams);
        tipView.setGravity(Gravity.CENTER_HORIZONTAL);
        tipView.setText("数据准备中");
        tipView.setTextColor(Color.parseColor("#00aaff"));
        rootView.addView(tipView);

        return rootView;
    }

    //"http://www.196mk.com/JsonAnimation/loadding.json"
    private static void startAnimation(final Context context, final LottieAnimationView animationView, String jsonAnimUrl) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .get()
                .url(jsonAnimUrl)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("=========>", "Json动画数据获取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                try {
                    JSONObject json = new JSONObject(data);
                    LottieComposition.Factory.fromJson(context.getResources(), json, new OnCompositionLoadedListener() {
                        @Override
                        public void onCompositionLoaded(LottieComposition composition) {
                            animationView.setComposition(composition);
                            animationView.playAnimation();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("=========>", "Json动画数据解析出错");
                }
            }
        });
    }
}
