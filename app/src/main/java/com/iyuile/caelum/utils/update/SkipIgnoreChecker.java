package com.iyuile.caelum.utils.update;

import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateChecker;
import org.lzh.framework.updatepluginlib.util.InstallUtil;

/**
 * 自定义更新检查器。(用户主动调用时,跳过忽略的版本,全部检查)
 * Created by WangYao on 2016/12/22.
 */
public class SkipIgnoreChecker implements UpdateChecker {

    public boolean check(Update update) {
        try {
            int e = InstallUtil.getApkVersion(UpdateConfig.getConfig().getContext());
            return update.getVersionCode() > e;
        } catch (Exception var3) {
            return false;
        }
    }
}
