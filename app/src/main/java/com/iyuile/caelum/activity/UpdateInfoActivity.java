package com.iyuile.caelum.activity;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.iyuile.caelum.R;
import com.iyuile.caelum.fragment.UpdateNewPhoneCodeFragment;
import com.iyuile.caelum.fragment.UpdateNewPhoneFragment;
import com.iyuile.caelum.fragment.UpdateNicknameFragment;
import com.iyuile.caelum.fragment.UpdatePhoneFragment;
import com.iyuile.caelum.fragment.UpdatePwdFragment;
import com.iyuile.caelum.fragment.UpdateRealNameFragment;
import com.iyuile.caelum.fragment.UpdateSexFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * @Description 修改昵称, 手机号等.
 */
public class UpdateInfoActivity extends BaseActivity {

    public static UpdateInfoActivity mInstance;
    private int activityCloseEnterAnimation, activityCloseExitAnimation;

    public static final String INTENT_PARAM_TAB_SELECT_INDEX = "intent_param_tab_select_index";

    private UpdateNicknameFragment mUpdateNickname;// 0
    private UpdateRealNameFragment mUpdateRelaName;// 1
    private UpdateSexFragment mUpdateSex;// 2
    private UpdatePwdFragment mUpdatePwd;// 3
    private UpdatePhoneFragment mUpdatePhone;// 4
    private UpdateNewPhoneFragment mUpdateNewPhone;// 5
    private UpdateNewPhoneCodeFragment mUpdateNewPhoneCode;// 6

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    private int tagIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Bundle bundle = getIntent().getExtras();
            tagIndex = bundle.getInt(INTENT_PARAM_TAB_SELECT_INDEX, 0);
        } catch (Exception e) {
        }

        setContentView(R.layout.activity_sign_update);
        mInstance = this;
        initActivityStyle();
        mApplication.mActivityManager.addActivity(this);
        getTintManager().setStatusBarDarkMode(true, this);// 文字颜色,true:黑色,false:白色
        getTintManager().setStatusBarTintColor(Color.TRANSPARENT);

        findFragmentManager(savedInstanceState);

        setTabSelection(tagIndex, false, true);
    }

    public void setTabSelection(int index, boolean isEnter) {
        setTabSelection(index, false, isEnter);
    }

    /**
     * 获取FragmentManager对象,解决fragment重叠问题
     *
     * @param savedInstanceState
     */
    private void findFragmentManager(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            mUpdateNickname = (UpdateNicknameFragment) fragmentManager.findFragmentByTag("0");
            mUpdateRelaName = (UpdateRealNameFragment) fragmentManager.findFragmentByTag("1");
            mUpdateSex = (UpdateSexFragment) fragmentManager.findFragmentByTag("2");
            mUpdatePwd = (UpdatePwdFragment) fragmentManager.findFragmentByTag("3");
            mUpdatePhone = (UpdatePhoneFragment) fragmentManager.findFragmentByTag("4");
            mUpdateNewPhone = (UpdateNewPhoneFragment) fragmentManager.findFragmentByTag("5");
            mUpdateNewPhoneCode = (UpdateNewPhoneCodeFragment) fragmentManager.findFragmentByTag("6");
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index      下标
     * @param initialise 初始化的时候关闭动画 false为不关闭
     * @param isEnter    true:进入动画,false:退出动画
     */
    private void setTabSelection(int index, boolean initialise, boolean isEnter) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (!initialise) {
            if (isEnter) {
                transaction.setCustomAnimations(R.anim.slide_fragment_horizontal_left_in, R.anim.slide_fragment_horizontal_left_out);
            } else {
                transaction.setCustomAnimations(R.anim.slide_fragment_horizontal_right_in, R.anim.slide_fragment_horizontal_right_out);
            }
//			transaction.addToBackStack(null);

        }

        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        switch (index) {
            case 0:
                if (mUpdateNickname == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mUpdateNickname = new UpdateNicknameFragment();
                    transaction.add(R.id.sign_update_fl_content, mUpdateNickname, "0");
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mUpdateNickname);
                }
                break;
            case 1:
                if (mUpdateRelaName == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mUpdateRelaName = new UpdateRealNameFragment();
                    transaction.add(R.id.sign_update_fl_content, mUpdateRelaName, "1");
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mUpdateRelaName);
                }
                break;
            case 2:
                if (mUpdateSex == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mUpdateSex = new UpdateSexFragment();
                    transaction.add(R.id.sign_update_fl_content, mUpdateSex, "2");
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mUpdateSex);
                }
                break;
            case 3:
                if (mUpdatePwd == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mUpdatePwd = new UpdatePwdFragment();
                    transaction.add(R.id.sign_update_fl_content, mUpdatePwd, "3");
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mUpdatePwd);
                }
                break;
            case 4:
                if (mUpdatePhone == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mUpdatePhone = new UpdatePhoneFragment();
                    transaction.add(R.id.sign_update_fl_content, mUpdatePhone, "4");
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mUpdatePhone);
                }
                break;
            case 5:
                if (mUpdateNewPhone == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mUpdateNewPhone = new UpdateNewPhoneFragment();
                    transaction.add(R.id.sign_update_fl_content, mUpdateNewPhone, "5");
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mUpdateNewPhone);
                }
                break;
            case 6:
                if (mUpdateNewPhoneCode == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mUpdateNewPhoneCode = new UpdateNewPhoneCodeFragment();
                    transaction.add(R.id.sign_update_fl_content, mUpdateNewPhoneCode, "6");
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mUpdateNewPhoneCode);
                }
                break;

        }

        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mUpdateNickname != null) {
            transaction.hide(mUpdateNickname);
        }
        if (mUpdateRelaName != null) {
            transaction.hide(mUpdateRelaName);
        }
        if (mUpdateSex != null) {
            transaction.hide(mUpdateSex);
        }
        if (mUpdatePwd != null) {
            transaction.hide(mUpdatePwd);
        }
        if (mUpdatePhone != null) {
            transaction.hide(mUpdatePhone);
        }
        if (mUpdateNewPhone != null) {
            transaction.hide(mUpdateNewPhone);
        }
        if (mUpdateNewPhoneCode != null) {
            transaction.hide(mUpdateNewPhoneCode);
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
        //请求中不能退后
//		try {
//			HttpUtils.getClient().cancelRequests(this, true);// 取消当前请求
//		} catch (Exception e) {}
        try {
            if (mUpdateNickname.btnSave.getProgress() == 50)
                return;
        } catch (Exception e) {
        }
        try {
            if (mUpdateRelaName.btnUpdateRealName.getProgress() == 50)
                return;
        } catch (Exception e) {
        }
        try {
            if (mUpdatePwd.btnUpdatePwd.getProgress() == 50)
                return;
        } catch (Exception e) {
        }
        try {
            if (mUpdatePhone.btnNextStep.getProgress() == 50)
                return;
        } catch (Exception e) {
        }
        try {
            if (mUpdateNewPhone.btnNextStep.getProgress() == 50)
                return;
        } catch (Exception e) {
        }
        try {
            if (mUpdateNewPhoneCode.btnSave.getProgress() == 50)
                return;
        } catch (Exception e) {
        }

        String fragmentTag = getVisibleFragment().getTag();
        if (fragmentTag.equals("5")) {
            setTabSelection(4, false);
        } else if (fragmentTag.equals("6")) {
            setTabSelection(5, false);
        } else {
//			super.onBackPressed();
            finishActivityAnimU2D();
        }
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

    /**
     * 修改成功关闭
     */
    public void loginSuccessFinish() {
        setResult(Activity.RESULT_OK);
        finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    public void finishActivityAnimU2D() {
        finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    private String phoneOld, phoneNew;

    /**
     * @return the phoneOld
     */
    public String getPhoneOld() {
        return phoneOld;
    }

    /**
     * @param phoneOld the phoneOld to set
     */
    public void setPhoneOld(String phoneOld) {
        this.phoneOld = phoneOld;
    }

    /**
     * @return the phoneNew
     */
    public String getPhoneNew() {
        return phoneNew;
    }

    /**
     * @param phoneNew the phoneNew to set
     */
    public void setPhoneNew(String phoneNew) {
        this.phoneNew = phoneNew;
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
