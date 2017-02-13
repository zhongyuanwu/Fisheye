package com.iyuile.caelum.utils.update;

import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

/**
 * 自定义更新策略，默认WIFI下自动下载更新
 * (用户主动调用)
 * Created by WangYao on 2016/12/20.
 */
public class UserCallsUpdateStrategy implements UpdateStrategy {

    /**
     * 指定是否在判断出有需要更新的版本时。弹出更新提醒弹窗
     *
     * @param update 需要更新的版本信息
     * @return true 显示弹窗
     */
    @Override
    public boolean isShowUpdateDialog(Update update) {
        return true;
    }

    /**
     * 指定是否下载完成后自动进行,安装页不显示弹窗
     *
     * @param update 需要更新的版本信息
     * @return true 直接安装，不显示弹窗
     */
    @Override
    public boolean isAutoInstall(Update update) {
        return true;
    }

    /**
     * 指定是否在下载的时候显示下载进度弹窗
     *
     * @return true 显示弹窗
     */
    @Override
    public boolean isShowDownloadDialog() {
        return true;
    }

}
