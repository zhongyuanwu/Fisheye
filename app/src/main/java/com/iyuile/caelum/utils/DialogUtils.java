package com.iyuile.caelum.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.iyuile.caelum.R;


/**
 * 对话框
 *
 * @author WangYao
 * @version 1
 * @ClassName DialogUtil
 * @date 2015-08-24 11:54:05
 */
public class DialogUtils {

    private static DialogUtils instance;

    /**
     * @return the instance
     */
    public static DialogUtils getInstance() {
        if (instance == null)
            instance = new DialogUtils();
        return instance;
    }

    public static final int DIALOG_TYPE_PHOTO = 0x001;//照片
    public static final int DIALOG_TYPE_REPORT = 0x002;//举报

    /**
     * 初始化对话框
     *
     * @version 1
     */
    public Dialog showDiaLog(Context c, int layoutID, int type) {
        View view = ((Activity) c).getLayoutInflater().inflate(layoutID, null);
        Dialog dialog = new Dialog(c, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) c).getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);

        switch (type) {
            case DIALOG_TYPE_PHOTO:
                showPhoto(view, dialog);
                break;
            case DIALOG_TYPE_REPORT:
//                showReport(view, dialog);
                break;

            default:
                break;
        }

        dialog.show();
        return dialog;
    }

    /**
     * 照片
     *
     * @param view
     * @param dialog
     */
    private void showPhoto(View view, final Dialog dialog) {
        Button top = (Button) view.findViewById(R.id.photo_top);
        Button bottom = (Button) view.findViewById(R.id.photo_bottom);
        Button cancel = (Button) view.findViewById(R.id.photo_cancel);
        top.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBackEventForPhoto.local();
            }
        });
        bottom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBackEventForPhoto.take();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    CallBackEventForPhoto callBackEventForPhoto;

    public void setCallBackEventForPhoto(CallBackEventForPhoto callBackEventForPhoto) {
        this.callBackEventForPhoto = callBackEventForPhoto;
    }

    // 调用照相机用的接口
    public interface CallBackEventForPhoto {
        public void local();

        public void take();
    }
}
