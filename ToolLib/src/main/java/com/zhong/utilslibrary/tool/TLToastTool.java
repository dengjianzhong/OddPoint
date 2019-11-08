package com.zhong.utilslibrary.tool;

import android.content.Context;

import com.zhong.utilslibrary.factory.ToastFactory;

public class TLToastTool {
    private static ToastFactory toastFactory;

    private TLToastTool() {
        throw new UnsupportedOperationException("禁止实例化");
    }

    public static void showToast(Context context, String text) {
        if (toastFactory == null) {
            toastFactory = new ToastFactory(context);
        }
        toastFactory.showView(text);
    }
}
