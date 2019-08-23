package com.zhong.utilslibrary.factory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 授权工厂类
 */
public class PermissionFactory {
    private Context context;
    private static int requestCode = 200;
    private static RelativeLayout.LayoutParams layoutParams;

    public PermissionFactory(Context context) {
        this.context = context;
    }

    /**
     * Permission request.
     *
     * @param permission the permission
     */
    public void PermissionRequest(String[] permission) {
        if (ContextCompat.checkSelfPermission(context, permission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, permission, 200);
        }
    }


    /**
     * 检测通知是否启用
     *
     * @param context the context
     */
    public static void checkNotificationIsEnabel(Context context) {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            gotoNotificationSetting(context);

            PopupFactory.showPopupWindow(context,initView(context), Gravity.BOTTOM);
        }
    }

    private static View initView(Context context){
        int padding = dip2px(context, 20);
        layoutParams = new RelativeLayout.LayoutParams(dip2px(context,500),dip2px(context,260));
        RelativeLayout relativeLayout=new RelativeLayout(context);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setPaddingRelative(padding,padding,padding,padding);

        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setCornerRadius(13);
        gradientDrawable.setColor(Color.WHITE);

        relativeLayout.setBackground(gradientDrawable);

        layoutParams = new RelativeLayout.LayoutParams(-2,-2);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP|RelativeLayout.CENTER_HORIZONTAL);
        TextView tipTextView=new TextView(context);
        tipTextView.setLayoutParams(layoutParams);
        tipTextView.setTextColor(Color.BLACK);
        tipTextView.setEms(13);
        tipTextView.setMaxLines(10);
        tipTextView.setLineSpacing(1.5F,0);
        relativeLayout.addView(tipTextView);

        return relativeLayout;
    }

    /**
     * 引导用户去打开消息通知,由内部调用
     *
     * @param context the context
     */
    private static void gotoNotificationSetting(Context context) {
        Activity activity = ((Activity) context);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                activity.startActivityForResult(intent, requestCode);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + pkg));
                activity.startActivityForResult(intent, requestCode);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivityForResult(intent, requestCode);
        }
    }
    private static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
