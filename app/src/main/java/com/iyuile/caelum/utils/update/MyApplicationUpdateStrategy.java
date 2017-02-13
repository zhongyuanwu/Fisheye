package com.iyuile.caelum.utils.update;

import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;
import org.lzh.framework.updatepluginlib.util.NetworkUtil;

/**
 * 自定义更新策略，默认WIFI下自动下载更新
 * (用于MyApplication使用)
 * Created by WangYao on 2016/12/20.
 */
public class MyApplicationUpdateStrategy implements UpdateStrategy {

    boolean isWifi;

    /**
     * 指定是否在判断出有需要更新的版本时。弹出更新提醒弹窗
     *
     * @param update 需要更新的版本信息
     * @return true 显示弹窗
     */
    @Override
    public boolean isShowUpdateDialog(Update update) {
        isWifi = NetworkUtil.isConnectedByWifi();
        return !isWifi;
    }

    /**
     * 指定是否下载完成后自动进行,安装页不显示弹窗
     *
     * @param update 需要更新的版本信息
     * @return true 直接安装，不显示弹窗
     */
    @Override
    public boolean isAutoInstall(Update update) {
        //强制更新时候,不再wifi的情况下显示安装页,为了是能有一个阻挡的窗口
        if (update.isForced())
            return false;
        else
            return !isWifi;
    }

    /**
     * 指定是否在下载的时候显示下载进度弹窗
     *
     * @return true 显示弹窗
     */
    @Override
    public boolean isShowDownloadDialog() {
        return !isWifi;
    }

}
