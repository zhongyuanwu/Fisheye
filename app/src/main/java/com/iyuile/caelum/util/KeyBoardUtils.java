package com.iyuile.caelum.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 键盘操作
 * <p/>
 * Created by WangYao on 2016/11/10.
 */
public class KeyBoardUtils {


    /**
     * 打开软键盘
     *
     * @param mEditText
     * @param mContext
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        try {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } catch (Exception e) {
        }
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText
     * @param mContext
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        try {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive())
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);/*强制隐藏键盘*/
        } catch (Exception e) {
        }
    }
}
