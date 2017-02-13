package com.iyuile.caelum.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 不能滑动的scrollview
 * <p/>
 * Created by WangYao on 2016/12/5.
 */
public class HorizontalScrollViewNoSliding extends HorizontalScrollView {

    public HorizontalScrollViewNoSliding(Context context) {
        super(context);
        noSliding();
    }

    public HorizontalScrollViewNoSliding(Context context, AttributeSet attrs) {
        super(context, attrs);
        noSliding();
    }

    public HorizontalScrollViewNoSliding(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        noSliding();
    }

    private void noSliding() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
    }
}
