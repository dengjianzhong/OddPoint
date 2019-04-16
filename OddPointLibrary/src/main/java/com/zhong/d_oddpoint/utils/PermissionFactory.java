package com.zhong.d_oddpoint.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionFactory {
    private Context context;
    public PermissionFactory(Context context) {
        this.context=context;
    }
    public void PermissionRequest(String[] permission){
        if (ContextCompat.checkSelfPermission(context,permission[0])!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,permission,200);
        }
    }
}
