package com.iyuile.caelum.utils.update;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.iyuile.caelum.utils.Log;

import com.iyuile.alertdialog.AlertDialog;
import com.iyuile.caelum.R;

import org.lzh.framework.updatepluginlib.creator.InstallCreator;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.util.NetworkUtil;
import org.lzh.framework.updatepluginlib.util.SafeDialogOper;

import java.lang.ref.WeakReference;

/**
 * Created by WangYao on 2016/12/22.
 */
public class UpdateNeedInstallCreator extends InstallCreator {

    private WeakReference<Activity> activityRef;

    @Override
    public Dialog create(final Update update, final String path, final Activity activity) {
        if (activity == null || activity.isFinishing()) {
            Log.e("DownDialogCreator--->", "show install dialog failed:activity was recycled or finished");
            return null;
        }
        activityRef = new WeakReference<>(activity);
        String updateContent = activity.getText(org.lzh.framework.updatepluginlib.R.string.update_version_name)
                + ": " + update.getVersionName() + "\n\n\n"
                + update.getUpdateContent();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.install_title))
                .setMessage(updateContent)
                .setNegativeButton(activity.getString(R.string.install_immediate), false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!update.isForced()) {
                            SafeDialogOper.safeDismissDialog((Dialog) dialog);
                        }
                        sendToInstall(path);
                    }
                });

        if (!update.isForced() && update.isIgnore()) {
            builder.setPositiveButton(activity.getString(R.string.update_ignore), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendCheckIgnore(update);
                    SafeDialogOper.safeDismissDialog((Dialog) dialog);
                }
            });
        } else if (update.isForced() && update.isIgnore() && NetworkUtil.isConnectedByWifi()) {
            //:::↑强制更新并且显示退出只有不是wifi的情况下,其他网络的强制更新显示到当前安装页不用显示退出按钮,因为在上个更新页已经给出了一次用户选择退出的机会
            builder.setPositiveButton(activity.getString(R.string.update_exit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SafeDialogOper.safeDismissDialog((Dialog) dialog);
                    exitApp();
                }
            });
        }

        if (!update.isForced()) {
            builder.setNegativeCenterButton(activity.getString(R.string.update_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendUserCancel();
                    SafeDialogOper.safeDismissDialog((Dialog) dialog);
                }
            });
        }
        AlertDialog installDialog = builder.create();
        installDialog.setCancelable(false);
        installDialog.setCanceledOnTouchOutside(false);
        return installDialog;
    }

}
