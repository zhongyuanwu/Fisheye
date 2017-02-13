package com.iyuile.caelum.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuile.caelum.R;
import com.iyuile.caelum.SupportMainActivity;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.response.ErrorResponse;
import com.iyuile.caelum.entity.response.TokenResponse;
import com.iyuile.caelum.utils.ButtonUtils;
import com.iyuile.caelum.utils.MIUIUtils;
import com.iyuile.caelum.utils.MyApplication;
import com.iyuile.caelum.utils.RetrofitUtils;
import com.iyuile.caelum.utils.VerifyUtil;
import com.iyuile.caelum.view.CustomEditText;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.circularprogressbutton.CircularProgressButton;
import com.umeng.analytics.MobclickAgent;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * 登录
 */
public class SignInActivity extends BaseActivity implements View.OnClickListener {

    public static SignInActivity mInstance;

    private ErrorHandlingCallAdapter.MyCall<TokenResponse> callResponse;

    public static final String INTENT_PARAM_IS_OPEN_STATUS = "intent_param_is_open_status";

    private static final int BUTTON_STATUS__CODE_SUCCESS = 100; // 成功
    private static final int BUTTON_STATUS__CODE_ERROR = -1; // 错误
    private static final int BUTTON_STATUS__CODE_DEFAULT = 0; // 默认
    private static final int BUTTON_STATUS__CODE_LOADING = 50; // 加载中

    private View contentLayout;
    private CustomEditText cePhone, cePwd;
    private CircularProgressButton btnSignIn;
    private TextView tvForgetPwd;

    private boolean isOpenStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;

        try {
            Bundle bundle = getIntent().getExtras();
            isOpenStatus = bundle.getBoolean(INTENT_PARAM_IS_OPEN_STATUS, false);
        } catch (Exception e) {
        }

        mApplication.mActivityManager.addActivity(this);
        setContentView(R.layout.activity_sign_in);
        getTintManager().setStatusBarDarkMode(true, this);// 文字颜色,true:黑色,false:白色
//        getTintManager().setStatusBarTintColor(ContextCompat.getColor(this, android.R.color.transparent));

        if (isOpenStatus) {
            initTopBarForOnlyTitle("");
            initActionbar();
        } else {
            initTopBarForLeftSecondaryIcon("", 0, 0, mApplication.getWoodBodyStyleFont());
            initActionbar();
        }

        initView();

        try {
            if (mApplication.mUserObject != null)
                cePhone.setText(mApplication.mUserObject.getTelephone());
        } catch (Exception e) {
        }

        //TODO :::test
//        cePhone.setText("13366664009");
//        cePwd.setText("password");
    }

    private void initActionbar() {
        final LinearLayout llSubhead = (LinearLayout) findViewById(R.id.ll_subhead);
        final TextView tvSubhead = (TextView) llSubhead.findViewById(R.id.tv_subhead);
        final View tvLabel = llSubhead.findViewById(R.id.tv_label);
        tvSubhead.setTypeface(mApplication.getWoodBodyStyleFont());
        tvSubhead.setText(getString(R.string.sign_in));
        ViewTreeObserver vto = tvSubhead.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                tvSubhead.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                tvLabel.getLayoutParams().width = tvSubhead.getWidth();
            }
        });
        llSubhead.setVisibility(View.VISIBLE);
    }

    private void initView() {
        contentLayout = findViewById(R.id.content);
        cePhone = (CustomEditText) findViewById(R.id.ce_phone);
        cePwd = (CustomEditText) findViewById(R.id.ce_pwd);
        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        tvForgetPwd.setTypeface(mApplication.getWoodBodyStyleFont());
        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimActivity(SignResetPwdActivity.class);
            }
        });
        btnSignIn = (CircularProgressButton) findViewById(R.id.btn_sign_in);
        btnSignIn.setIndeterminateProgressMode(true);//true=等待模式,false=进度条模式
        btnSignIn.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        mInstance = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(SignInActivity.class.getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MIUIUtils.isMIUISuspensionWindowPermission(this, contentLayout);
        MobclickAgent.onPageStart(SignInActivity.class.getSimpleName());
        MobclickAgent.onResume(this);
    }

    public static void startAnimU2DActivity(Context context, Class<?> cla) {
        Intent intent = new Intent(context, cla);
        Bundle bundle = new Bundle();
        bundle.putBoolean(INTENT_PARAM_IS_OPEN_STATUS, true);
        intent.putExtras(bundle);
        context.startActivity(intent);
        // 两个参数分别表示进入的动画,退出的动画
        ((Activity) context).overridePendingTransition(R.anim.qr_code_anim_in, R.anim.qr_code_anim_out_below);
    }

    public static void startAnimU2DActivity(Context context, Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(INTENT_PARAM_IS_OPEN_STATUS, true);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.qr_code_anim_in, R.anim.qr_code_anim_out_below);
    }

    public static void startAnimU2DActivity(Context context, Intent intent, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(INTENT_PARAM_IS_OPEN_STATUS, true);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.qr_code_anim_in, R.anim.qr_code_anim_out_below);
    }

    /**
     * 带动画的退出
     */
    private void finishActivityAnimU2D() {
        mInstance = null;
//        setResult(RESULT_OK);
        startActivity(new Intent(this, SupportMainActivity.class));
        this.finish();
        this.overridePendingTransition(R.anim.qr_code_anim_out_below, R.anim.qr_code_anim_out);
    }

    private static long firstTime;

    @Override
    public void onBackPressed() {
        //请求中不能退后
        /*try {
            if (btnSignIn.getProgress() == BUTTON_STATUS__CODE_LOADING)
                return;
        } catch (Exception e) {
        }*/
        if (isOpenStatus) {
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
        } else
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.btn_sign_in:
                login();
                break;
        }
    }

    private String phone;

    /**
     * 登录
     */
    private void login() {
        switch (btnSignIn.getProgress()) {// 按钮状态
            case BUTTON_STATUS__CODE_SUCCESS:
            case BUTTON_STATUS__CODE_ERROR:
                btnSignIn.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                tvForgetPwd.setEnabled(true);

                return;
            case BUTTON_STATUS__CODE_DEFAULT:
                break;
            default:
                return;
        }

        btnSignIn.setProgress(BUTTON_STATUS__CODE_LOADING);

        phone = cePhone.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            SuperToast.makeText(this, getString(R.string.phone_hint),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSignIn.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (!VerifyUtil.isMobileNO(phone)) {
            SuperToast.makeText(this, getString(R.string.sign_in_phone_falseness),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            btnSignIn.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        String pwd = cePwd.getText().toString();

        if (TextUtils.isEmpty(pwd)) {
            SuperToast.makeText(this, getString(R.string.pwd_hint),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSignIn.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (pwd.length() < AppConstants.PASSWORD_INTENSITY_LENGTH_MIN) {//
            SuperToast.makeText(this, getString(R.string.sign_in_password_intensity_error_prompt),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            btnSignIn.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (pwd.length() > AppConstants.PASSWORD_INTENSITY_LENGTH_MAX) {//
            SuperToast.makeText(this, getString(R.string.sign_in_password_intensity_error_prompt),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            btnSignIn.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        callResponse = ApiServiceImpl.loginImpl(this, phone, pwd, new ResponseHandlerListener<TokenResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
                btnSignIn.setProgress(BUTTON_STATUS__CODE_LOADING);
                tvForgetPwd.setEnabled(false);
            }

            @Override
            public void success(int statusCode, final Response<TokenResponse> response) {
                super.success(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TokenResponse tokenResponse = response.body();
                        String access_token = tokenResponse.getAccess_token();

                        if (access_token != null) {
                            mApplication.getSpUtil().setAccessTokenData(access_token);
                            //TODO :::um
                            try {
                                //当用户使用第三方账号（如新浪微博）登录时，可以这样统计：
                                //MobclickAgent.onProfileSignIn("WB"，"userID");
                                // 当用户使用自有账号登录时，可以这样统计
                                MobclickAgent.onProfileSignIn(phone);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            btnSignIn.setProgress(BUTTON_STATUS__CODE_ERROR);
                            SuperToast.makeText(SignInActivity.this, getString(R.string.service_response_error),
                                    SuperToast.Icon.Resource.ERROR,
                                    SuperToast.Background.RED).show();

                            return;
                        }

                        btnSignIn.setProgress(BUTTON_STATUS__CODE_SUCCESS);

                        handler.sendEmptyMessageDelayed(1, 500);

                        mApplication.findTokenAndUsersInfo(SplashActivity.mInstance);

                        mApplication.getSpUtil().setCurrentUsersPhone(phone);
                    }
                });
            }

            @Override
            public void failure(int statusCode, Object o) {
                super.failure(statusCode, o);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnSignIn.setProgress(BUTTON_STATUS__CODE_ERROR);
                    }
                });
            }

            @Override
            public void unauthenticated(int statusCode, final Response<?> response) {
                super.unauthenticated(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.errorBody() != null) {
                            try {
                                Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(SignInActivity.this).
                                        responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                ErrorResponse errorObject = errorConverter.convert(response.errorBody());

                                if (errorObject.getCode() == NetworkConstants.RESPONSE_CODE_UNAUTHORIZED_401)
                                    SuperToast.makeText(SignInActivity.this, errorObject.getMessage(),
                                            SuperToast.Icon.Resource.ERROR,
                                            SuperToast.Background.RED).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            @Override
            public void clientError(int statusCode, Response<?> response) {
                super.clientError(statusCode, response);
            }

            @Override
            public void serverError(int statusCode, final Response<?> response) {
                super.serverError(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnSignIn.setProgress(BUTTON_STATUS__CODE_ERROR);
                        if (response.errorBody() != null) {
                            try {
                                Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(SignInActivity.this).
                                        responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                ErrorResponse errorObject = errorConverter.convert(response.errorBody());

                                if (errorObject.getCode() == NetworkConstants.RESPONSE_CODE_UNAUTHORIZED_401)
                                    SuperToast.makeText(SignInActivity.this, errorObject.getMessage(),
                                            SuperToast.Icon.Resource.ERROR,
                                            SuperToast.Background.RED).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFinish() {
                super.onFinish();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvForgetPwd.setEnabled(true);
                    }
                });
            }

            @Override
            public void onCancel() {
                super.onCancel();
                try {
                    btnSignIn.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                } catch (Exception e) {
                }
            }
        });

        if (callResponse == null) {//没有网络 或者token
            btnSignIn.setProgress(BUTTON_STATUS__CODE_ERROR);
        }

//        try {
//            callResponse.cancel();
//        } catch (NullPointerException e) {}
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        finishActivityAnimU2D();
                        SplashActivity.mInstance.finish();
                    } catch (Exception e) {
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };
}