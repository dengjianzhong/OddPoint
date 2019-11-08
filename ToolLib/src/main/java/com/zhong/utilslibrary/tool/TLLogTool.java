package com.zhong.utilslibrary.tool;

import android.util.Log;

public class TLLogTool {
    private final static String TAG = "========>";

    private TLLogTool(){
        throw new UnsupportedOperationException("Can not be instantiated.");
    }

    public static final void logI(String string){
        Log.i(TAG, string);
    }

    public static final void logE(String string){
        Log.e(TAG, string);
    }
}
