package com.iyuile.caelum.utils;

import android.os.CountDownTimer;

/**
 * 
 * @Description 计时器(用于验证码重新发送计时)
 */
public class TimerUtil extends CountDownTimer {

	public TimerUtil(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}

	@Override
	public void onTick(long millisUntilFinished) {// 计时过程
		if (onTimerUtilListener != null)
			onTimerUtilListener.onTick(millisUntilFinished);
	}

	@Override
	public void onFinish() {// 计时完毕
		if (onTimerUtilListener != null)
			onTimerUtilListener.onFinish();
	}

	private OnTimerUtilListener onTimerUtilListener;

	public void setOnTmerUtilListener(OnTimerUtilListener onTimerUtilListener) {
		this.onTimerUtilListener = onTimerUtilListener;
	}

	public interface OnTimerUtilListener {
		public void onTick(long millisUntilFinished);

		public void onFinish();
	}
	
}
