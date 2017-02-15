package com.iyuile.caelum.util.update;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.iyuile.caelum.R;
import com.iyuile.caelum.view.progressbar.IconRoundCornerProgressBar;

/**
 * 更新版本下载dialog
 *
 * @author WangYao
 * @version 1
 * @ClassName DialogUpdateDownloadUtil
 * @date 2016-12-28 18:08:35
 */
public class DialogUpdateDownloadUtil {

    private static DialogUpdateDownloadUtil instance;

    /**
     * @return the instance
     */
    public static DialogUpdateDownloadUtil getInstance() {
        if (instance == null)
            instance = new DialogUpdateDownloadUtil();
        return instance;
    }

    private Context context;

    private Dialog dialog;
    private View dialogView;

    private IconRoundCornerProgressBar pbIrcView;

    public Dialog showDiaLog(Context context) {
        this.context = context;

        dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_update_download, null);
        dialog = new Dialog(context, R.style.transparentFrameWindowStyle_DimTotEnabled);
        dialog.setContentView(dialogView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
//        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        pbIrcView = (IconRoundCornerProgressBar) dialog.findViewById(R.id.pb_irc_view);
        return dialog;
    }

    private int mProgress;

    /**
     * 设置进度
     *
     * @param progress
     * @return
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        pbIrcView.setProgress(progress);
    }

    public int getProgress() {
        return mProgress;
    }

}
