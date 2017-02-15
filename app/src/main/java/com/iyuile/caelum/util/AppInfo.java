package com.iyuile.caelum.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 应用信息
 * 
 * @ClassName
 * @author WangYao
 * @version 1
 * @date 2015-05-21 17:24:44
 * 
 */
public class AppInfo {
	private static Context context;
	private static PackageManager pm;

	public AppInfo(Context context) {
		this.context = context;
		pm = context.getPackageManager();
	}

	/**
	 * 获取程序包名
	 */
	public static String getPackageName() {
		String packageNames;
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			// // 当前应用的版本名称
			// String versionName = info.versionName;
			// // 当前版本的版本号
			// int versionCode = info.versionCode;
			// 当前版本的包名
			packageNames = info.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 获取程序 图标
	 */
	public static Drawable getAppIcon(String packname) {
		try {
			ApplicationInfo info = pm.getApplicationInfo(packname, 0);
			return info.loadIcon(pm);
		} catch (NameNotFoundException e) {
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取程序的版本号
	 *
	 * @param context
	 * @return 当前应用的版本名称
     */
	public static int getVersionCode(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/*
	 * 获取程序的权限
	 */
	public static String[] getAppPremission(String packname) {
		try {
			PackageInfo packinfo = pm.getPackageInfo(packname,
					PackageManager.GET_PERMISSIONS);
			// 获取到所有的权限
			return packinfo.requestedPermissions;

		} catch (NameNotFoundException e) {
			e.printStackTrace();

		}
		return null;
	}

	/*
	 * 获取程序的签名
	 */
	public static String getAppSignature(String packname) {
		try {
			PackageInfo packinfo = pm.getPackageInfo(packname,
					PackageManager.GET_SIGNATURES);
			// 获取到所有的权限
			return packinfo.signatures[0].toCharsString();

		} catch (NameNotFoundException e) {
			e.printStackTrace();

		}
		return null;
	}
	
	/**
	 * 获取IEMI
	 * 
	 * <uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission> {@link #findIEMI(Context)}
	 * 
	 * 1: The IMEI: 仅仅只对Android手机有效:
	 * 采用此种方法，需要在AndroidManifest.xml中加入一个许可：android.permission.READ_PHONE_STATE，
	 * 并且用户应当允许安装此应用。作为手机来讲，IMEI是唯一的，它应该类似于
	 * 359881030314356（除非你有一个没有量产的手机（水货）它可能有无效的IMEI，如：0000000000000）。
	 */
	public static String findIEMI(Context context) {
		TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		String szImei = TelephonyMgr.getDeviceId();
		return szImei;
	}
	
	/**
	 * 获取wifi mac
	 * 
	 * <uses-permissionandroid:name="android.permission.ACCESS_WIFI_STATE" ></uses-permission> {@link #wlanMAC(Context)}
	 * 
	 * 4. The WLAN MAC Address string
	 * 是另一个唯一ID。但是你需要为你的工程加入android.permission.ACCESS_WIFI_STATE
	 * 权限，否则这个地址会为null。
	 */
	public static String wlanMAC(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

		return m_szWLANMAC;
	}
	
	/**
	 * 获取设备唯一标识
	 * 
	 * @return
	 */
	public static String getDeviceInfo(Context context) {
	    try{
	      org.json.JSONObject json = new org.json.JSONObject();
	      TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

	      String device_id = tm.getDeviceId();

	      WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

	      String mac = wifi.getConnectionInfo().getMacAddress();
	      json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

	      if( TextUtils.isEmpty(device_id) ){
	        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
	      }

	      json.put("device_id", device_id);

	      return json.toString();
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	  return null;
	}
}
