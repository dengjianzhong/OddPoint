package com.zhong.toollib.tool;

import android.util.Log;

public class CLLogTool {
    private final static String TAG = "========>";

    private CLLogTool(){
        throw new UnsupportedOperationException("Can not be instantiated.");
    }

    public static final void logI(String string){
        Log.i(TAG, string);
    }

    public static final void logE(String string){
        Log.e(TAG, string);
    }
}
