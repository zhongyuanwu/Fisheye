package com.iyuile.caelum.activity;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.iyuile.caelum.R;
import com.iyuile.caelum.fragment.FeedbackFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * @Description 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

    public static FeedbackActivity mInstance;
    private int activityCloseEnterAnimation, activityCloseExitAnimation;

    private FeedbackFragment mFeedbackFragment;// 0

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);
        mInstance = this;
        initActivityStyle();
        mApplication.mActivityManager.addActivity(this);
        getTintManager().setStatusBarDarkMode(true, this);// 文字颜色,true:黑色,false:白色
        getTintManager().setStatusBarTintColor(Color.TRANSPARENT);

        findFragmentManager(savedInstanceState);

        setTabSelection();
        findViewById(R.id.rl_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivityAnimU2D();
            }
        });
    }

    /**
     * 获取FragmentManager对象,解决fragment重叠问题
     *
     * @param savedInstanceState
     */
    private void findFragmentManager(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            mFeedbackFragment = (FeedbackFragment) fragmentManager.findFragmentByTag("0");
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    private void setTabSelection() {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setCustomAnimations(R.anim.feedback_fragment_in, R.anim.feedback_fragment_out);
//        transaction.addToBackStack(null);

        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        if (mFeedbackFragment == null) {
            // 如果MessageFragment为空，则创建一个并添加到界面上
            mFeedbackFragment = new FeedbackFragment();
            transaction.add(R.id.fl_content, mFeedbackFragment, "0");
        } else {
            // 如果MessageFragment不为空，则直接将它显示出来
            transaction.show(mFeedbackFragment);
        }

        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mFeedbackFragment != null) {
            transaction.hide(mFeedbackFragment);
        }
    }

    /**
     * 获取当前那个fragment显示这
     *
     * @return
     */
    public Fragment getVisibleFragment() {
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        finishActivityAnimU2D();
    }

    @Override
    protected void onDestroy() {
        mInstance = null;
        super.onDestroy();
    }

    // - -------------------------------

    /**
     * 解决android 5.0以上style里设置退出动画无效问题.
     */
    @SuppressWarnings("ResourceType")
    private void initActivityStyle() {
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();
    }

    public void finishActivityAnimU2D() {
        finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

}
