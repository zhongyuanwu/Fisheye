package com.iyuile.caelum.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;

/**
 * 黄历
 *
 * @author WangYao
 * @version 1
 * @ClassName DialogUtil
 * @date 2016-12-06 15:48:06
 */
public class DialogAlmanacUtil {

    private static DialogAlmanacUtil instance;

    /**
     * @return the instance
     */
    public static DialogAlmanacUtil getInstance() {
        if (instance == null)
            instance = new DialogAlmanacUtil();
        return instance;
    }

    private Context context;

    private Dialog dialog;
    private View dialogView;

    public Dialog showDiaLog(Context context, String title, String content) {
        this.context = context;
        dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_almanac, null);
        dialog = new Dialog(context, R.style.transparentFrameWindowStyle_DimTotEnabled);
        dialog.setContentView(dialogView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.shaking_answer_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
//        wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        wl.y = 0;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.CENTER;

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);

        initView(dialogView);
        setData(title,content);

        dialog.show();
        return dialog;
    }

    private void setData(String title, String content) {
        tvTitle.setText(title);
        tvContent.setText(content);
    }

    private TextView tvTitle, tvContent;

    private void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
    }

}
