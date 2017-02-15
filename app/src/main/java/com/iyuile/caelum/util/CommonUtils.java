package com.iyuile.caelum.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author WangYao
 * @version 1
 * @Description 检查网络
 * @ProjectName PavoCustomer
 * @ClassName {@link CommonUtils}
 * @Date 2015-11-11 下午6:17:34
 * <p>
 * <i><uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /></i>
 */
public class CommonUtils {

    /**
     * 检查是否有网络
     */
    public static boolean isNetworkAvailable() {
        NetworkInfo info = getNetworkInfo(MyApplication.getInstance());
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    /**
     * 检查是否是WIFI
     */
    public static boolean isWifi() {
        NetworkInfo info = getNetworkInfo(MyApplication.getInstance());
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
        }
        return false;
    }

    /**
     * 检查是否是移动网络
     */
    public static boolean isMobile() {
        NetworkInfo info = getNetworkInfo(MyApplication.getInstance());
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * 检查SD卡是否存在
     */
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 打开网络设置界面
     *
     * @param activity
     */
    public static void openSetting(Activity activity) {
        Intent intent;
        // 判断手机系统的版本 即API大于10 就是3.0或以上版本及魅族手机
        if (android.os.Build.VERSION.SDK_INT > 10 && !android.os.Build.MANUFACTURER.equals("Meizu")) {
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else if (android.os.Build.VERSION.SDK_INT > 17 && android.os.Build.MANUFACTURER.equals("Meizu")) {
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        activity.startActivityForResult(intent, 0);
    }

}
