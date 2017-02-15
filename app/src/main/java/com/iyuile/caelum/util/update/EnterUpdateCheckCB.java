package com.iyuile.caelum.util.update;

import android.Manifest;
import android.content.Context;

import com.fastaccess.permission.base.PermissionHelper;
import com.iyuile.caelum.R;
import com.iyuile.caelum.receiver.ConnectionNetworkReceiver;
import com.iyuile.caelum.tools.SDCardTools;
import com.iyuile.caelum.view.toast.SuperToast;

import org.lzh.framework.updatepluginlib.callback.UpdateCheckCB;
import org.lzh.framework.updatepluginlib.model.Update;

/**
 * 自定义检查更新时的回调(用于进入时应用使用)
 * Created by WangYao on 2016/12/20.
 */
public class EnterUpdateCheckCB implements UpdateCheckCB {

    private Context context;
    private ConnectionNetworkReceiver connReceiver;
    private PermissionHelper permissionHelper;

    public EnterUpdateCheckCB(Context context, ConnectionNetworkReceiver connReceiver, PermissionHelper permissionHelper) {
        this.context = context;
        this.connReceiver = connReceiver;
        this.permissionHelper = permissionHelper;
    }

    /**
     * 更新失败
     *
     * @param code
     * @param errorMsg
     */
    @Override
    public void onCheckError(int code, String errorMsg) {
//                                        Toast.makeText(context, "更新失败：code:" + code + ",errorMsg:" + errorMsg, Toast.LENGTH_SHORT).show();
        if (errorMsg.equals("Already have a update task running")) {
            SuperToast.makeText(context, context.getString(R.string.update_loading_prompt),
                    SuperToast.Icon.Resource.INFO,
                    SuperToast.Background.BLUE).show();
        } else {
            SuperToast.makeText(context, context.getString(R.string.update_check_error),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            SDCardTools.cleanUpdate();

            if (!permissionHelper.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionHelper
                        .setForceAccepting(true)//强迫用户允许
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    /**
     * 用户取消更新
     */
    @Override
    public void onUserCancel() {
        connReceiver.isCheckUpdate = true;
    }

    /**
     * 用户忽略此版本更新
     *
     * @param update
     */
    @Override
    public void onCheckIgnore(Update update) {
        connReceiver.isCheckUpdate = true;
    }

    /**
     * 检查到有更新
     *
     * @param update
     */
    @Override
    public void hasUpdate(Update update) {

    }

    /**
     * 无更新
     */
    @Override
    public void noUpdate() {

    }

}
