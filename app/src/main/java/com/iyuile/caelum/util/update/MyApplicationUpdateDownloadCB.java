package com.iyuile.caelum.util.update;

import android.content.Context;

import com.iyuile.caelum.tools.SDCardTools;

import org.lzh.framework.updatepluginlib.callback.UpdateDownloadCB;

import java.io.File;

/**
 * 自定义apk下载的回调
 * (用于MyApplication使用)
 * Created by WangYao on 2016/12/20.
 */
public class MyApplicationUpdateDownloadCB implements UpdateDownloadCB {

    private Context context;
    private static volatile boolean isDownload;

    public static boolean isDownload() {
        return isDownload;
    }

    public MyApplicationUpdateDownloadCB(Context context) {
        this.context = context;
    }

    /**
     * 下载开始
     */
    @Override
    public void onUpdateStart() {
        isDownload = true;
    }

    /**
     * 下载完成
     *
     * @param file
     */
    @Override
    public void onUpdateComplete(File file) {
        isDownload = false;
    }

    @Override
    public void onUpdateProgress(long current, long total) {
    }

    /**
     * 下载失败
     *
     * @param code
     * @param errorMsg
     */
    @Override
    public void onUpdateError(int code, String errorMsg) {
//        Toast.makeText(context, "下载失败：code:" + code + ",errorMsg:" + errorMsg, Toast.LENGTH_SHORT).show();
        isDownload = false;
        SDCardTools.cleanUpdate();
    }

}
