package com.iyuile.caelum.activity;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;


/**
 * Activity管理器
 * @author WangYao
 * @version 1
 * @Description 用于处理退出程序时可以退出所有的activity，而编写的通用类
 * @ClassName {@link ActivityManager}
 * @Date 2016-5-5 下午4:24:12
 */
public class ActivityManager {

    private List<Activity> activityList = new LinkedList<Activity>();
    private static ActivityManager instance;

    private ActivityManager() {

    }

    // 单例模式中获取唯一的实例
    public static ActivityManager getInstance() {
        if (null == instance) {
            instance = new ActivityManager();
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
//		 System.exit(0);
    }
}
