package com.iyuile.caelum.util.update;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.iyuile.caelum.util.Log;

import com.iyuile.alertdialog.AlertDialog;
import com.iyuile.caelum.R;

import org.lzh.framework.updatepluginlib.creator.DialogCreator;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.util.SafeDialogOper;

/**
 * Created by WangYao on 2016/12/22.
 */
public class UpdateNeedUpdateCreator extends DialogCreator {

    //用于用户主动调用时,显示忽略的版本提示信息,但是不显示忽略的按钮
    private boolean isForcedBlank;

    public UpdateNeedUpdateCreator() {
    }

    public UpdateNeedUpdateCreator(boolean isForcedBlank) {
        this.isForcedBlank = isForcedBlank;
    }

    @Override
    public Dialog create(final Update update, final Activity activity) {

        if (activity == null || activity.isFinishing()) {
            Log.e("DialogCreator--->", "Activity was recycled or finished,dialog shown failed!");
            return null;
        }

        String updateContent = activity.getText(org.lzh.framework.updatepluginlib.R.string.update_version_name)
                + ": " + update.getVersionName() + "\n\n\n"
                + update.getUpdateContent();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setMessage(updateContent)
                .setTitle(activity.getString(R.string.update_title))
                .setNegativeButton(activity.getString(R.string.update_immediate), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendDownloadRequest(update, activity);
                        SafeDialogOper.safeDismissDialog((Dialog) dialog);
                    }
                });
        if (update.isIgnore() && !update.isForced() && !isForcedBlank) {
            builder.setPositiveButton(activity.getString(R.string.update_ignore), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendUserIgnore(update);
                    SafeDialogOper.safeDismissDialog((Dialog) dialog);
                }
            });
        } else if (update.isIgnore() && update.isForced() && !isForcedBlank) {
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
