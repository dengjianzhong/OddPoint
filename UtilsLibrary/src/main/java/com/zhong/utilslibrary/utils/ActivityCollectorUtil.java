package com.zhong.utilslibrary.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity收集工具
 */
public class ActivityCollectorUtil {
    public List<Activity> activityList = new ArrayList<>();
    private static ActivityCollectorUtil activityCollectorUtil;

    /**
     * Gets instance.
     *
     * @return the ActivityCollectorUtil instance
     */
    public static ActivityCollectorUtil getInstance() {
        if (activityCollectorUtil == null) {
            synchronized (ActivityCollectorUtil.class) {
                activityCollectorUtil = new ActivityCollectorUtil();
            }
        }

        return activityCollectorUtil;
    }

    /**
     * 添加Activity
     *
     * @param activity the activity
     */
    public void addActivity(Activity activity) {
        if (activity != null) {
            activityList.add(activity);
        }
    }

    /**
     * 得到容器里面的所有Activity
     *
     * @return the activity list
     */
    public List<Activity> getActivityList() {

        return activityList;
    }

    /**
     * Finish all Activity.
     */
    public void finishAll() {
        for (Activity activity : activityList) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        activityList.clear();
    }

    /**
     * Finish a activity.
     *
     * @param activity the activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
        }
    }
}
