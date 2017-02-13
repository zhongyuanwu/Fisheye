package com.iyuile.caelum.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author WangYao
 * @version 1
 * @Description 应用偏好
 */
@SuppressLint("CommitPrefEdits")
public class SharedPreferenceUtil {

    private SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    public SharedPreferenceUtil(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    // -------------------------首次使用---------------------------------------------
    private final String SHARED_KEY_FIRST_USE = "SHARED_KEY_FIRST_USE";

    // 获取是否是首次使用
    public boolean getFirstUse() {
        return mSharedPreferences.getBoolean(SHARED_KEY_FIRST_USE, false);
    }

    // 设置
    public void setFirstUse(boolean firstUse) {
        editor.putBoolean(SHARED_KEY_FIRST_USE, firstUse);
        editor.commit();
    }

    // -------------------------验证码发送的时间---------------------------------------------
    private final String SHARED_KEY_IDENTIFYING_CODE = "SHARED_KEY_IDENTIFYING_CODE";

    // 获取
    public long getIdentifyingCodeDate() {
        return mSharedPreferences.getLong(SHARED_KEY_IDENTIFYING_CODE, 0);
    }

    // 存入
    public void setIdentifyingCodeDate(long date) {
        editor.putLong(SHARED_KEY_IDENTIFYING_CODE, date);
        editor.commit();
    }

    // -------------------------{@link UsersEntity} 当前用户的 phone(:::用于标注是否是登录状态)---------------------------------------------
    private final String SHARED_KEY_CURRENT_USER_PHONE = "SHARED_KEY_CURRENT_USER_PHONE";

    // 获取
    public String getCurrentUsersPhone() {
        return mSharedPreferences.getString(SHARED_KEY_CURRENT_USER_PHONE, "null");
    }

    // 存入
    public void setCurrentUsersPhone(String userPhone) {
        editor.putString(SHARED_KEY_CURRENT_USER_PHONE, userPhone);
        editor.commit();
    }

    // -------------------------{@link TokenResponse}数据---------------------------------------------
    private final String SHARED_KEY_ACCESS_TOKEN_DATA = "SHARED_KEY_ACCESS_TOKEN_DATA";

    // 获取
    public String getAccessTokenData() {
        return mSharedPreferences.getString(SHARED_KEY_ACCESS_TOKEN_DATA, "null");
    }

    // 存入
    public void setAccessTokenData(String jsonStr) {
        editor.putString(SHARED_KEY_ACCESS_TOKEN_DATA, jsonStr);
        editor.commit();
    }

    // -------------------------{@link isPush}是否接收推送---------------------------------------------
    private final String SHARED_KEY_IS_PUSH = "SHARED_KEY_IS_PUSH";

    // 获取
    public boolean isPush() {
        return mSharedPreferences.getBoolean(SHARED_KEY_IS_PUSH, true);
    }

    // 存入
    public void setPush(boolean push) {
        editor.putBoolean(SHARED_KEY_IS_PUSH, push);
        editor.commit();
    }

    // -------------------------{@link isPlayAuto}是否自动播放---------------------------------------------
    private final String SHARED_KEY_IS_AUTO_PLAY = "SHARED_KEY_IS_AUTO_PLAY";

    // 获取
    public boolean isPlayAuto() {
        return mSharedPreferences.getBoolean(SHARED_KEY_IS_AUTO_PLAY, true);
    }

    // 存入
    public void setPlayAuto(boolean auto) {
        editor.putBoolean(SHARED_KEY_IS_AUTO_PLAY, auto);
        editor.commit();
    }

    // -------------------------{@link isSoundAuto}是否自动声音---------------------------------------------
    private final String SHARED_KEY_IS_AUTO_SOUND = "SHARED_KEY_IS_AUTO_SOUND";

    // 获取
    public boolean isSoundAuto() {
        return mSharedPreferences.getBoolean(SHARED_KEY_IS_AUTO_SOUND, true);
    }

    // 存入
    public void setSoundAuto(boolean auto) {
        editor.putBoolean(SHARED_KEY_IS_AUTO_SOUND, auto);
        editor.commit();
    }

    // -------------------------{@link UsersEntity} 当前用户的userJSON---------------------------------------------
    private final String SHARED_KEY_CURRENT_USER_INFO = "SHARED_KEY_CURRENT_USER_INFO";

    // 获取
    public String getCurrentUserInfo() {
        return mSharedPreferences.getString(SHARED_KEY_CURRENT_USER_INFO, "null");
    }

    // 存入
    public void setCurrentUsersInfo(String userJSON) {
        editor.putString(SHARED_KEY_CURRENT_USER_INFO, userJSON);
        editor.commit();
    }

    // ------------------------- 当前设备信息---------------------------------------------
    private final String SHARED_KEY_CURRENT_DEVICE_INFO = "SHARED_KEY_CURRENT_DEVICE_INFO";

    // 获取
    public String getCurrentDeviceInfo() {
        return mSharedPreferences.getString(SHARED_KEY_CURRENT_DEVICE_INFO, "null");
    }

    // 存入
    public void setCurrentDeviceInfo(String deviceJSON) {
        editor.putString(SHARED_KEY_CURRENT_DEVICE_INFO, deviceJSON);
        editor.commit();
    }


    // ------------------------- 定位信息---------------------------------------------

    //区域 码
    private final String SHARED_KEY_MAP_AD_CODE = "SHARED_KEY_MAP_AD_CODE";

    // 获取
    public int getMapAdCode() {
        return mSharedPreferences.getInt(SHARED_KEY_MAP_AD_CODE, 0);
    }

    // 存入
    public void setMapAdCode(int mapAdCode) {
        editor.putInt(SHARED_KEY_MAP_AD_CODE, mapAdCode);
        editor.commit();
    }

    //城市名称
    private final String SHARED_KEY_MAP_CITY = "SHARED_KEY_MAP_CITY";

    // 获取
    public String getMapCity() {
        return mSharedPreferences.getString(SHARED_KEY_MAP_CITY, "null");
    }

    // 存入
    public void setMapCity(String mapCity) {
        editor.putString(SHARED_KEY_MAP_CITY, mapCity);
        editor.commit();
    }

    //区域名称
    private final String SHARED_KEY_MAP_DISTRICT = "SHARED_KEY_MAP_DISTRICT";

    // 获取
    public String getMapDistrict() {
        return mSharedPreferences.getString(SHARED_KEY_MAP_DISTRICT, "null");
    }

    // 存入
    public void setMapDistrict(String mapDistrict) {
        editor.putString(SHARED_KEY_MAP_DISTRICT, mapDistrict);
        editor.commit();
    }

    //经度
    private final String SHARED_KEY_MAP_LONGITUDE = "SHARED_KEY_MAP_LONGITUDE";

    // 获取
    public String getMapLongitude() {
        return mSharedPreferences.getString(SHARED_KEY_MAP_LONGITUDE, "0");
    }

    // 存入
    public void setMapLongitude(String mapLongitude) {
        editor.putString(SHARED_KEY_MAP_LONGITUDE, mapLongitude);
        editor.commit();
    }

    //纬度
    private final String SHARED_KEY_MAP_LATITUDE = "SHARED_KEY_MAP_LATITUDE";

    // 获取
    public String getMapLatitude() {
        return mSharedPreferences.getString(SHARED_KEY_MAP_LATITUDE, "0");
    }

    // 存入
    public void setMapLatitude(String mapLatitude) {
        editor.putString(SHARED_KEY_MAP_LATITUDE, mapLatitude);
        editor.commit();
    }

}
