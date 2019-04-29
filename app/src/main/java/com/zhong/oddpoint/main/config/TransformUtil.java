package com.zhong.oddpoint.main.config;

import android.content.Context;

public class TransformUtil {
    public static int dip2px(Context context, float dipValue)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
