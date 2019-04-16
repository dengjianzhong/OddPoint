package com.zhong.d_oddpoint.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class ToastFactory extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public ToastFactory(Context context) {
        super(context);
    }
    public ToastFactory setLayoutView(View view){
        if (view!=null){
            this.setDuration(Toast.LENGTH_SHORT);
            this.setGravity(Gravity.CENTER,0,0);
            this.setView(view);
        }
        return this;
    }
}
