package com.zhong.d_oddpoint.utils;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhong.d_oddpoint.R;

public class PopupFactory {
    public static PopupWindow ShowPopup(View view, int height, final Window window){
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, height);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.showAtLocation(view,Gravity.BOTTOM,0,0);
        //打开弹窗，背景变暗淡
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha=0.7f;
        window.setAttributes(attributes);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //背景恢复正常
                WindowManager.LayoutParams attributes1 = window.getAttributes();
                attributes1.alpha=1f;
                window.setAttributes(attributes1);
            }
        });
        return popupWindow;
    }
    public static PopupWindow LoadPopup(View view, final Window window){
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);
//        popupWindow.setFocusable(true);
//        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.showAtLocation(view,Gravity.CENTER,0,0);
        //打开弹窗，背景变暗淡
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha=0.7f;
        window.setAttributes(attributes);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //背景恢复正常
                WindowManager.LayoutParams attributes1 = window.getAttributes();
                attributes1.alpha=1f;
                window.setAttributes(attributes1);
            }
        });
        return popupWindow;
    }
}
