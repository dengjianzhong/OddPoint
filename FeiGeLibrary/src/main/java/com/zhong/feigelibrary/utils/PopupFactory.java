package com.zhong.feigelibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


/**
 * PopupWindow工厂类
 */
public class PopupFactory {

    private static Window window;

    public static PopupWindow ShowPopup(Context context, View view) {
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, getScreenData(context) / 2);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        //打开弹窗，背景变暗淡
        window = ((Activity) context).getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = 0.7f;
        window.setAttributes(attributes);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //背景恢复正常
                WindowManager.LayoutParams attributes1 = window.getAttributes();
                attributes1.alpha = 1f;
                window.setAttributes(attributes1);
            }
        });
        view.startAnimation(setAnimation());
        return popupWindow;
    }

    public static PopupWindow LoadPopup(Context context, View view) {
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        window = ((Activity) context).getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = 0.7f;
        window.setAttributes(attributes);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams attributes1 = window.getAttributes();
                attributes1.alpha = 1f;
                window.setAttributes(attributes1);
            }
        });
        view.startAnimation(setAnimation());
        return popupWindow;
    }

    private static AnimationSet setAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(200);
        //平移
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 100, 0);
        animationSet.addAnimation(translateAnimation);
        //透明度
        AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.0, (float) 0.8);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    @SuppressLint("WrongConstant")
    public static int getScreenData(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private View getView() {

        return null;
    }
}
