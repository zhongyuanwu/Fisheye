package com.iyuile.caelum.tools;

import android.content.Context;
import android.os.Environment;

import com.iyuile.caelum.utils.OkHttpUtils;
import com.iyuile.caelum.utils.SDCardUtils;
import com.iyuile.caelum.utils.SDFileOperate;
import com.iyuile.caelum.utils.update.MyApplicationUpdateDownloadCB;

import java.io.File;

/**
 * Created by WangYao on 2016/11/28.
 */
public class SDCardTools {

    // SD卡路径
    public static String SD_PATH;

    public static final String PROJECT_FOLDER = "caelum";// 项目文件夹(.项目名的时候,开头不能是大写字符,否则创建不了文件夹下的子文件)
    public static final String _PROJECT_FOLDER = "." + PROJECT_FOLDER;
    public static final String _NO_MEDIA_FILE = ".nomedia";
    private static final String PROJECT_DB = "database";// 数据库文件夹
    private static final String PROJECT_HTTP_CACHE = "okHttp";// 请求缓存文件夹
    private static final String PROJECT_IMAGE = "image";// 图片文件夹
    private static final String PROJECT_IMAGE_AVATAR = "avatar";// 图片->头像文件夹
    private static final String PROJECT_IMAGE_IMAGE_GOD_SHEFUSHENCAI_ITEM = "guess";// 图片->射覆神猜item文件夹
    private static final String PROJECT_UPDATE = "update";// 更新文件夹(.apk文件存放)

    /**
     * 检查是否有SD卡
     */
    public static boolean isSDCardEnable(Context context) {
        if (SDCardUtils.isSDCardEnable()) {
            SD_PATH = SDCardUtils.getSDCardPath();
            createFolder();
            SDFileOperate.createFolder(context.getExternalCacheDir() + File.separator + PROJECT_DB);
            SDFileOperate.createFolder(context.getExternalCacheDir() + File.separator + PROJECT_HTTP_CACHE);
            return true;
        } else {//sd卡不可用
            SD_PATH = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            createFolder();
            SDFileOperate.createFolder(context.getCacheDir() + File.separator + PROJECT_DB);
            SDFileOperate.createFolder(context.getCacheDir() + File.separator + PROJECT_HTTP_CACHE);
        }
        return false;
    }

    /**
     * 创建文件夹
     */
    private static void createFolder() {
        SDFileOperate.createFolder(getProjectPath());
        SDFileOperate.createFolder(get_ProjectPath());
        SDFileOperate.createFolder(getImagePath());
        SDFileOperate.createFolder(getImageAvatarPath());
        SDFileOperate.createFolder(getImageGodShefushencaiItemPath());
        SDFileOperate.createFolder(getUpdatePath());

        SDFileOperate.createFile(getImageAvatarPath(), _NO_MEDIA_FILE);
    }

    /**
     * 获取本项目隐藏文件在SD中位置
     */
    public static String get_ProjectPath() {
        if (SD_PATH != null)
            return SD_PATH + _PROJECT_FOLDER + File.separator;
        return null;
    }

    /**
     * 获取本项目文件在SD中位置
     */
    public static String getProjectPath() {
        if (SD_PATH != null)
            return SD_PATH + PROJECT_FOLDER + File.separator;
        return null;
    }

    /**
     * 获取图片文件在SD中位置
     */
    public static String getImagePath() {
        if (SD_PATH != null)
            return SD_PATH + _PROJECT_FOLDER + File.separator + PROJECT_IMAGE + File.separator;
        return null;
    }

    /**
     * 获取图片->头像文件在SD中位置
     * 裁切的头像等(清楚缓存的时候会清除)
     */
    public static String getImageAvatarPath() {
        if (SD_PATH != null)
            return SD_PATH + _PROJECT_FOLDER + File.separator + PROJECT_IMAGE + File.separator + PROJECT_IMAGE_AVATAR + File.separator;
        return null;
    }

    /**
     * 获取图片->射覆神猜item文件在SD中位置
     * 裁切的射覆神猜item等(清楚缓存的时候会清除)
     */
    public static String getImageGodShefushencaiItemPath() {
        if (SD_PATH != null)
            return SD_PATH + _PROJECT_FOLDER + File.separator + PROJECT_IMAGE + File.separator + PROJECT_IMAGE_IMAGE_GOD_SHEFUSHENCAI_ITEM + File.separator;
        return null;
    }

    /**
     * 更新文件夹(.apk文件存放)
     *
     * @return
     */
    public static String getUpdatePath() {
        if (SD_PATH != null)
            return SD_PATH + _PROJECT_FOLDER + File.separator + PROJECT_UPDATE + File.separator;
        return null;
    }

    /**
     * 获取数据库文件在SD中位置
     */
    public static String getDBPath(Context context) {
        if (SD_PATH != null)
            return getDiskCacheDir(context) + PROJECT_DB + File.separator;
        return null;
    }

    /**
     * 获取请求缓存文件在SD中位置
     */
    public static String getHttpCachePath(Context context) {
        if (SD_PATH != null)
            return getDiskCacheDir(context) + PROJECT_HTTP_CACHE + File.separator;
        return null;
    }

    public static String getDiskCacheDir(Context context) {
        String cachePath;
        if (SDCardUtils.isSDCardEnable()) {
            cachePath = context.getExternalCacheDir().getPath() + File.separator;
        } else {
            cachePath = context.getCacheDir().getPath() + File.separator;
        }
        return cachePath;
    }

    public static void cleanOkHttp(Context context) {
        try {
            File okhttpCacheFile = OkHttpUtils.getInstance(context).cache().directory();
            SDFileOperate.deleteFilePathSubfile(okhttpCacheFile);
            okhttpCacheFile = null;
        } catch (Exception e) {
        }
    }

    /**
     * 清除上传裁切的图片-头像
     */
    public static void cleanImageAvatar() {
        try {
            File imgFile = new File(getImageAvatarPath());
            SDFileOperate.deleteFilePathSubfile(imgFile);
            SDFileOperate.createFile(imgFile.getAbsolutePath(), _NO_MEDIA_FILE);
            imgFile = null;
        } catch (Exception e) {
        }
    }

    /**
     * 清除上传裁切的图片-射覆神猜item
     */
    public static void cleanImageGodShefushencaiItem() {
        try {
            File imgFile = new File(getImageGodShefushencaiItemPath());
            SDFileOperate.deleteFilePathSubfile(imgFile);
            imgFile = null;
        } catch (Exception e) {
        }
    }

    /**
     * 清除更新文件夹(.apk文件存放)
     */
    public static void cleanUpdate() {
        try {
            if (MyApplicationUpdateDownloadCB.isDownload()) return;
            File imgFile = new File(getUpdatePath());
            SDFileOperate.deleteFilePathSubfile(imgFile);
            imgFile = null;
        } catch (Exception e) {
        }
    }

    /**
     * 清除WebView缓存
     */
    public static void clearWebViewCache(Context context) {

        // 清理Webview缓存数据库
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // WebView 缓存文件
            File webviewCacheDir = new File(getDBPath(context));
            // 删除webview 缓存目录
            SDFileOperate.deleteFilePathSubfile(webviewCacheDir);
            webviewCacheDir = null;
        } catch (Exception e) {
        }
    }

}
