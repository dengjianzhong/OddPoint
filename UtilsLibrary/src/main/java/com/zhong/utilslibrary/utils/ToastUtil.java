package com.zhong.utilslibrary.utils;

import android.content.Context;

import com.zhong.utilslibrary.factory.ToastFactory;

public class ToastUtil {
    private static ToastFactory toastFactory;

    public static void showToast(Context context, String text) {
        if (toastFactory == null) {
            toastFactory = new ToastFactory(context);
        }
        toastFactory.showView(text);
    }
}
