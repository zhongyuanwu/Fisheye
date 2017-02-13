package com.iyuile.caelum.utils;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * 震动
 * 权限
 * <uses-permission android:name="android.permission.VIBRATE"/>
 * Created by WangYao on 2016/12/5.
 */
public class VibratorHelper {

    /**
     * @param activity
     * @param milliseconds 振动的时长，单位是毫秒。(300m)
     */
    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vibrator = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }

    /**
     * @param activity
     * @param pattern  自定义振动模式。数组中数字的含义依次是[静止时长, 振动时长, 静止时长, 振动时长, ......]。振动时长的单位是毫秒。
     * @param isRepeat 是否重复振动，1为重复，-1为只振动一次。
     */
    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vibrator = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, isRepeat ? 1 : -1);
    }
}
