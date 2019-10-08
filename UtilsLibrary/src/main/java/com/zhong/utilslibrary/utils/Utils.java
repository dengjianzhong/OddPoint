package com.zhong.utilslibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * The type Utils.
 */
public class Utils {
    private static DisplayMetrics displayMetrics;
    /**
     * 得到屏幕的宽度
     *
     * @param context the context
     * @return the screen width
     */
    public static int getScreenWidth(Context context) {
        if (displayMetrics == null) {
            displayMetrics = new DisplayMetrics();
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 得到屏幕的高度
     *
     * @param context the context
     * @return the screen height
     */
    public static int getScreenHeight(Context context) {
        if (displayMetrics == null) {
            displayMetrics = new DisplayMetrics();
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * Gets activity.
     *
     * @param context the context
     * @return the activity
     */
    public static Activity getActivity(Context context) {
        return (Activity) context;
    }

    /**
     * 设置全屏模式
     *
     * @param context the context
     */
    public Utils setFullScreen(Context context) {
        if (context != null)
            ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //全屏
        return this;
    }

    /**
     * 取消全屏模式
     *
     * @param context the context
     */
    public Utils setCancelScreen(Context context) {
        if (context != null)
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return this;
    }

    /**
     * dip转px
     *
     * @param context  the context
     * @param dipValue the dip value
     * @return the int
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
