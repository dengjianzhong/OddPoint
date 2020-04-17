package com.zhong.toollib.tool;

import android.content.Context;

import com.zhong.toollib.factory.ToastFactory;

public class ToastTool {
    private static ToastFactory toastFactory;

    private ToastTool() {
        throw new UnsupportedOperationException("禁止实例化");
    }

    public static void showToast(Context context, String text) {
        if (toastFactory == null) {
            toastFactory = new ToastFactory(context);
        }
        toastFactory.showView(text);
    }
}
