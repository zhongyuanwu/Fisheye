package com.iyuile.caelum.utils.update;

import android.content.Context;

import com.iyuile.caelum.R;
import com.iyuile.caelum.tools.SDCardTools;
import com.iyuile.caelum.utils.CommonUtils;
import com.iyuile.caelum.view.toast.SuperToast;

import org.lzh.framework.updatepluginlib.callback.UpdateCheckCB;
import org.lzh.framework.updatepluginlib.model.Update;

/**
 * 自定义检查更新时的回调
 * (用于MyApplication使用)
 * Created by WangYao on 2016/12/20.
 */
public class MyApplicationUpdateCheckCB implements UpdateCheckCB {

    private Context context;

    public MyApplicationUpdateCheckCB(Context context) {
        this.context = context;
    }

    /**
     * 更新失败
     *
     * @param code
     * @param errorMsg
     */
    @Override
    public void onCheckError(int code, String errorMsg) {
//        Toast.makeText(context, "更新失败：code:" + code + ",errorMsg:" + errorMsg, Toast.LENGTH_SHORT).show();
        if (CommonUtils.isNetworkAvailable()) {
            if (errorMsg.equals("Already have a update task running")) {
                SuperToast.makeText(context, context.getString(R.string.update_loading_prompt),
                        SuperToast.Icon.Resource.INFO,
                        SuperToast.Background.BLUE).show();
            } else {
                SuperToast.makeText(context, context.getString(R.string.update_check_error),
                        SuperToast.Icon.Resource.ERROR,
                        SuperToast.Background.RED).show();
                SDCardTools.cleanUpdate();
            }
        } else {
            SuperToast.makeText(context, context.getResources().getString(R.string.response_code_not_networkavailable_0),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
        }
    }

    /**
     * 用户取消更新
     */
    @Override
    public void onUserCancel() {
    }

    /**
     * 用户忽略此版本更新
     *
     * @param update
     */
    @Override
    public void onCheckIgnore(Update update) {
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
        SuperToast.makeText(context, context.getString(R.string.update_check_no),
                SuperToast.Icon.Resource.INFO,
                SuperToast.Background.BLUE).show();
    }

}
