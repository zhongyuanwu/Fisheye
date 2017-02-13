package com.iyuile.caelum.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *
 * @Description 跑马灯效果的TextView
 * @ProjectName Apus
 * @ClassName {@link AlwaysMarqueeTextView}
 * @author WangYao
 * @version 1
 * @Date 2016-5-5 下午7:22:48
 * android:gravity="center" 可以换成其他属性
 *
 		android:ellipsize="marquee"
        android:focusable="true"
        android:focusableInTouchMode="true"
        android:gravity="center"
        android:marqueeRepeatLimit="marquee_forever"
        android:scrollHorizontally="true"
        android:singleLine="true"

		setEllipsize(TextUtils.TruncateAt.MARQUEE);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setGravity(Gravity.CENTER);
		setMarqueeRepeatLimit(-1);
		setHorizontallyScrolling(true);
		setSingleLine(true);
 *
 *
 */
public class AlwaysMarqueeTextView extends TextView {
	public AlwaysMarqueeTextView(Context context) {
		super(context);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true;
	}
}
