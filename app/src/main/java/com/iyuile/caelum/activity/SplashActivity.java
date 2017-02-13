package com.iyuile.caelum.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuile.caelum.BuildConfig;
import com.iyuile.caelum.R;
import com.iyuile.caelum.SupportMainActivity;
import com.iyuile.caelum.utils.MyApplication;
import com.umeng.analytics.MobclickAgent;


/**
 * @author WangYao
 * @version 1
 * @Description 欢迎页
 * @ProjectName
 * @ClassName {@link SplashActivity}
 * @Date 2016-11-16 16:32:31
 */
@SuppressLint("NewApi")
public class SplashActivity extends BaseActivity implements View.OnClickListener {

    public static SplashActivity mInstance;

    private static final int GO_HOME = 0x00000001;// 主页
    private static final int GO_SIGN = 0x00000002;// 登录,注册

    private RelativeLayout rlContent;

    private Button btnSignIn, btnSignUp;
    private TextView tvAppName;
    private TextView tvForgetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        mApplication.mActivityManager.addActivity(this);
        setContentView(R.layout.activity_splash);
        getTintManager().setStatusBarTintColor(Color.TRANSPARENT);

        rlContent = (RelativeLayout) findViewById(R.id.content);
        // 隐藏状态栏
        rlContent.setSystemUiVisibility(View.INVISIBLE);
        // 显示状态栏
        // rlContent.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        openApp();
        mApplication.findTokenAndUsersInfo(this, false);
        initUmeng();

        AlphaAnimation mAlphaAction = new AlphaAnimation(0f, 1f);
        mAlphaAction.setDuration(800);
        mAlphaAction.setStartOffset(300);
        tvAppName = (TextView) findViewById(R.id.tv_app_name);
        tvAppName.setTypeface(mApplication.getYeGenyouStyleFont());
        tvAppName.startAnimation(mAlphaAction);

        if (mApplication.getSpUtil().getCurrentUsersPhone().equals("null")) {
            mHandler.sendEmptyMessageDelayed(GO_SIGN, 1000);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_HOME, 1000);
        }

    }

    /**
     * 友盟统计
     * TODO :::um
     */
    private void initUmeng() {
        // 集成测试
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);

        // 如果enable为true，SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
        // 如果enable为false，SDK将按照非加密的方式来传输日志。
        // 如果您没有设置加密模式，SDK的加密模式为false（不加密）。
        MobclickAgent.enableEncrypt(true);

        // 该接口默认参数是true，即采集mac地址，但如果开发者需要在googleplay发布，考虑到审核风险，可以调用该接口，参数设置为false就不会采集mac地址。
        MobclickAgent.setCheckDevice(true);

        // 在仅有Activity的应用中，SDK 自动帮助开发者调用了 2  中的方法，并把Activity 类名作为页面名称统计。但是在包含fragment的程序中我们希望统计更详细的页面，所以需要自己调用方法做更详细的统计。
        // 首先，需要在程序入口处，调用 MobclickAgent.openActivityDurationTrack(false)  禁止默认的页面统计方式，这样将不会再自动统计Activity。

        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);

        /**
         * etype是官方场景，有如下场景:
         EScenarioType. E_UM_NORMAL  普通统计场景类型
         EScenarioType. E_UM_GAME     游戏场景类型
         EScenarioType. E_UM_ANALYTICS_OEM  统计盒子场景类型
         EScenarioType. E_UM_GAME_OEM       游戏盒子场景类型
         */
        // MobclickAgent.startWithConfigure(new UMAnalyticsConfig(mContext, "4f83c5d852701564c0000011", "Umeng",EScenarioType.E_UM_NORMAL));
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        // MobclickAgent.setAutoLocation(true);

        // 当应用在后台运行超过30秒（默认）再回到前端，将被认为是两个独立的session(启动)，例如用户回到home，或进入其他程序，经过一段时间后再返回之前的应用。可通过接口：MobclickAgent.setSessionContinueMillis(long
        // interval) 来自定义这个间隔（参数单位为毫秒）。
        MobclickAgent.setSessionContinueMillis(30 * 1000);
    }

    /**
     * 外部一些app或者html来启动程序
     * {@links caelum://caelum.iyuile.com/openApp?sid=1}
     */
    private void openApp() {
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            if (uri != null) {
                String sid = uri.getQueryParameter("sid");
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isBackPressed = true;
            switch (msg.what) {
                case GO_HOME:
                    finishActivityAnim();
                    break;
                case GO_SIGN:
                    AlphaAnimation mAlphaAction = new AlphaAnimation(0f, 1f);
                    mAlphaAction.setDuration(1000);
                    TranslateAnimation mTranslateAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    mTranslateAction.setDuration(1000);
                    AnimationSet animationSet = new AnimationSet(true);
                    animationSet.addAnimation(mAlphaAction);
                    animationSet.addAnimation(mTranslateAction);
                    animationSet.setFillAfter(true);

                    LinearLayout llSign = (LinearLayout) findViewById(R.id.ll_sign);
                    llSign.startAnimation(animationSet);
                    llSign.setVisibility(View.VISIBLE);

                    btnSignIn = (Button) findViewById(R.id.btn_sign_in);
                    btnSignUp = (Button) findViewById(R.id.btn_sign_up);
                    tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);

                    btnSignIn.setOnClickListener(SplashActivity.this);
                    btnSignUp.setOnClickListener(SplashActivity.this);
                    tvForgetPwd.setOnClickListener(SplashActivity.this);
                    break;
                default:

                    break;
            }
        }
    };

    /**
     * 带动画的退出
     */
    private void finishActivityAnim() {
//        setResult(RESULT_OK);
        startActivity(new Intent(this, SupportMainActivity.class));
        finish();
        // 两个参数分别表示进入的动画,退出的动画
        overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
    }

    private static long firstTime;
    private boolean isBackPressed;

    @Override
    public void onBackPressed() {
        //取消返回键
        if (!isBackPressed)
            return;
        if (firstTime + 2000 > System.currentTimeMillis()) {
            //如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据。
//                        MobclickAgent.onKillProcess(this);

            // 关闭程序
//                super.onBackPressed();
            //如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据。
//            MobclickAgent.onKillProcess(this);
            MyApplication.getInstance().mActivityManager.exit();
            return;
            // 返回桌面
            // Intent home = new Intent(Intent.ACTION_MAIN);
            // home.addCategory(Intent.CATEGORY_HOME);
            // startActivity(home);
        } else {
            Toast.makeText(this, getString(R.string.prompt_application__exit), Toast.LENGTH_SHORT).show();
        }
        firstTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPageEnd(SplashActivity.class.getSimpleName());
        // 确保在所有的Activity中都调用 MobclickAgent.onResume() 和MobclickAgent.onPause()方法，这两个调用将不会阻塞应用程序的主线程，也不会影响应用程序的性能。
        // 注意 如果您的Activity之间有继承或者控制关系请不要同时在父和子Activity中重复添加onPause和onResume方法，否则会造成重复统计，导致启动次数异常增高。(eg.使用TabHost、TabActivity、ActivityGroup时)。
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。)
        MobclickAgent.onPageStart(SplashActivity.class.getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        mInstance = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                startAnimActivity(SignInActivity.class);
                break;
            case R.id.btn_sign_up:
                startAnimActivity(SignUpActivity.class);
                break;
            case R.id.tv_forget_pwd:
                startAnimActivity(SignResetPwdActivity.class);
                break;
        }
    }
}
