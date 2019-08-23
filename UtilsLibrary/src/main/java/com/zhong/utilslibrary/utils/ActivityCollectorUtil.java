package com.zhong.utilslibrary.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollectorUtil {
    public static List<Activity> activityList=new ArrayList<>();
    public static void finishAll(){
        for(Activity activity:activityList){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        activityList.clear();
    }
    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }
}
