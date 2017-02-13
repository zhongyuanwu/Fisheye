package com.iyuile.caelum.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Button;

import com.iyuile.caelum.R;
import com.iyuile.caelum.contants.AppConstants;

/**
 * @Description 验证码计时器实现
 */
public class TimerUtilImpl {

    private Context context;
    private Button btnSend;
    public TimerUtil timerUtilObject;// 计时器

    /**
     * @param context
     * @param btn_send
     */
    public TimerUtilImpl(Context context, Button btn_send) {
        super();
        this.context = context;
        this.btnSend = btn_send;
    }

    /**
     * 验证时间(创建页面时调用,检查时间)
     */
    public void validateTime() {
        long timeValue = MyApplication.getInstance().getSpUtil().getIdentifyingCodeDate();

        if (timeValue != 0l) {
            long result = (System.currentTimeMillis() - timeValue) / 1000;
            if (result != 0l && result < AppConstants.AGAIN_SEND_VERIFY_CODE_TIME) {
                createTimer(result);
            }
        }
    }

    /**
     * 创建并且开启一个计时器
     *
     * @param result
     */
    @SuppressLint("NewApi")
    public void createTimer(long result) {
        MyApplication.getInstance().getSpUtil().setIdentifyingCodeDate(System.currentTimeMillis());

//		btnSend.setEnabled(false);
        btnSend.setTag(false);
        timerUtilObject = new TimerUtil((AppConstants.AGAIN_SEND_VERIFY_CODE_TIME - result) * 1000, 1000);
        timerUtilObject.setOnTmerUtilListener(new TimerUtil.OnTimerUtilListener() {

            @Override
            public void onTick(long millisUntilFinished) {
                btnSend.setText(context.getResources().getString(R.string.sign_btn_send_sms_param, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
//				btnSend.setEnabled(true);
                btnSend.setTag(true);
                btnSend.setText(context.getResources().getString(R.string.sign_btn_send_sms));
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//				    btnSend.setBackgroundColor(context.getResources().getColor(R.color.theme_green, context.getTheme()));
//				} else {
//					btnSend.setBackgroundColor(context.getResources().getColor(R.color.theme_green));
//				}

                MyApplication.getInstance().getSpUtil().setIdentifyingCodeDate(0l);
                timerUtilObject = null;
            }
        });
        timerUtilObject.start();
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//		    btnSend.setBackgroundColor(context.getResources().getColor(R.color.theme_gray_light, context.getTheme()));
//		} else {
//			btnSend.setBackgroundColor(context.getResources().getColor(R.color.theme_gray_light));
//		}
    }

    /**
     * 取消计时器(退出页面的时销毁)
     */
    public void cancelTimer() {
        if (timerUtilObject != null) {// 取消计时器
            timerUtilObject.cancel();
            timerUtilObject = null;
        }
    }


}
