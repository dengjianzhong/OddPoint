package com.zhong.utilslibrary.anim;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * JSON动画工厂类
 */
public class LottieAnimation {
    private int match = LinearLayout.LayoutParams.MATCH_PARENT;
    private int wrap = LinearLayout.LayoutParams.WRAP_CONTENT;
    private LinearLayout.LayoutParams layoutParams;
    private BufferedInputStream inputStream;
    private LottieAnimationView animationView;
    private static LottieAnimation lottieAnimation;
    private Context context;
    private TextView tipView;
    private LinearLayout rootView;

    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    public static LottieAnimation getInstance(Context context) {
        return lottieAnimation = new LottieAnimation(context);
    }

    /**
     * Instantiates a new Lottie animation.
     *
     * @param context the context
     */
    public LottieAnimation(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        int margin = dip2px(context, 16);
        layoutParams = new LinearLayout.LayoutParams(match, match);
        layoutParams.setMargins(margin, margin, margin, margin);
        rootView = new LinearLayout(context);
        rootView.setLayoutParams(layoutParams);
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setGravity(Gravity.CENTER);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(23);
        gradientDrawable.setColor(Color.WHITE);
        rootView.setBackground(gradientDrawable);

        layoutParams = new LinearLayout.LayoutParams(dip2px(context, 92), dip2px(context, 92));
        animationView = new LottieAnimationView(context);
        animationView.setLayoutParams(layoutParams);
        animationView.loop(true);

        rootView.addView(animationView);

        layoutParams = new LinearLayout.LayoutParams(match, wrap);
        tipView = new TextView(context);
        tipView.setLayoutParams(layoutParams);
        tipView.setGravity(Gravity.CENTER_HORIZONTAL);
        tipView.setTextColor(Color.parseColor("#00aaff"));
        rootView.addView(tipView);
    }

    /**
     * 存储在assets下的JSON动画文件名
     *
     * @param fileName 文件名
     * @param showText 提示文本
     * @return the view
     */
    public View loaddingAnimationFile(String fileName, String showText) {

        LottieComposition lottieComposition = null;
        try {
            lottieComposition = LottieComposition.Factory.fromFileSync(context, fileName);
            animationView.setComposition(lottieComposition);
            animationView.playAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String tipText = TextUtils.isEmpty(showText) ? "加载中..." : showText;
        tipView.setText(tipText);

        return rootView;
    }

    /**
     * 加载存储在内存中的JSON动画文件
     *
     * @param filePath 文件路径
     * @param showText 提示文本
     * @return the view
     */
    public View loaddingAnimationStream(String filePath, String showText) {
        try {
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            LottieComposition.Factory.fromInputStream(context, inputStream, new OnCompositionLoadedListener() {
                @Override
                public void onCompositionLoaded(@Nullable LottieComposition composition) {
                    animationView.setComposition(composition);
                    animationView.playAnimation();
                }
            });
        }
        String tipText = TextUtils.isEmpty(showText) ? "加载中..." : showText;
        tipView.setText(tipText);

        return rootView;
    }

    /**
     * 远程加载JSON动画
     *
     * @param URL      SON动画文件的URL
     * @param showText 提示文本
     * @return the view
     */
    public View loaddingAnimationURL(String URL, String showText) {
        loaddingURL(context, animationView, URL);
        String tipText = TextUtils.isEmpty(showText) ? "加载中..." : showText;
        tipView.setText(tipText);

        return rootView;
    }

    private void loaddingURL(final Context context, final LottieAnimationView animationView, String jsonAnimURL) {
        if (jsonAnimURL.startsWith("http") || jsonAnimURL.startsWith("https")) {
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .get()
                    .url(jsonAnimURL)
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

    private static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
