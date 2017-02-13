package com.iyuile.caelum.utils;

/**
 * 
 * @Description 按钮工具类
 */
public class ButtonUtils {

	private static long lastClickTime;

	/**
	 * 防止重复点击
	 * 
	 * @return
	 */
	public synchronized static boolean isSeriesDoubleClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
