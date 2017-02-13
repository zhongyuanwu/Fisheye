package com.iyuile.caelum.fragment;

import android.widget.Toast;

import com.iyuile.caelum.R;

import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by k21 on 2017/1/19.
 */

public class SupportBaseFragment extends SupportFragment {

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
