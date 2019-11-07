package com.zhong.utilslibrary.helper;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity收集助手
 */
public class ActivityCollectorHelper {
    private static ActivityCollectorHelper activityCollectorHelper;
    private static List<Activity> activityList;

    private ActivityCollectorHelper() {
    }

    /**
     * Gets instance.
     *
     * @return the ActivityCollectorHelper instance
     */
    public static ActivityCollectorHelper getInstance() {
        if (activityCollectorHelper == null) {
            synchronized (ActivityCollectorHelper.class) {
                if (activityCollectorHelper == null) {
                    activityCollectorHelper = new ActivityCollectorHelper();
                }
                activityList = new ArrayList<>();
            }
        }

        return activityCollectorHelper;
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
