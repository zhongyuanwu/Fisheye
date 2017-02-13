package com.iyuile.caelum.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.response.ErrorResponse;
import com.iyuile.caelum.entity.response.RequestTokenResponse;
import com.iyuile.caelum.entity.response.TokenResponse;
import com.iyuile.caelum.entity.response.VerityUsersResponse;
import com.iyuile.caelum.utils.ButtonUtils;
import com.iyuile.caelum.utils.MIUIUtils;
import com.iyuile.caelum.utils.RetrofitUtils;
import com.iyuile.caelum.utils.TimerUtilImpl;
import com.iyuile.caelum.utils.VerifyUtil;
import com.iyuile.caelum.view.CustomEditText;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.circularprogressbutton.CircularProgressButton;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * 注册
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    public static SignUpActivity mInstance;

    private static final int BUTTON_STATUS__CODE_SUCCESS = 100; // 成功
    private static final int BUTTON_STATUS__CODE_ERROR = -1; // 错误
    private static final int BUTTON_STATUS__CODE_DEFAULT = 0; // 默认
    private static final int BUTTON_STATUS__CODE_LOADING = 50; // 加载中

    private ErrorHandlingCallAdapter.MyCall<ResponseBody> callImageCodeResponse;
    private ErrorHandlingCallAdapter.MyCall<Void> callSmsCodeResponse;
    private ErrorHandlingCallAdapter.MyCall<VerityUsersResponse> callUserVerityResponse;
    private ErrorHandlingCallAdapter.MyCall<Void> callSignUpResponse;

    private ErrorHandlingCallAdapter.MyCall<RequestTokenResponse> callRequestTokenResponse;
    private String mDeviceRequestToken = "";

    private View contentLayout;
    private CustomEditText cePhone, cePwd;
    private TextView iconImgcode, iconSMSCode;
    private EditText etImgCode, etSMSCode;
    private CircularProgressButton btnSignUp;

    private ImageView ivImageCode;
    private Button btnSendSMS;

    private TimerUtilImpl timerUtil;// 计时器

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        mApplication.mActivityManager.addActivity(this);
        setContentView(R.layout.activity_sign_up);
        getTintManager().setStatusBarDarkMode(true, this);// 文字颜色,true:黑色,false:白色
//        getTintManager().setStatusBarTintColor(ContextCompat.getColor(this, android.R.color.transparent));

        initTopBarForLeftSecondaryIcon("", 0, 0, mApplication.getWoodBodyStyleFont());
        initActionbar();

        initView();
        btnSendSMS.setTag(true);
        timerUtil = new TimerUtilImpl(this, btnSendSMS);
        timerUtil.validateTime();

        //TODO :::test
//        cePhone.setText("13366664009");
//        cePwd.setText("password");
    }


    private void initActionbar() {
        final LinearLayout llSubhead = (LinearLayout) findViewById(R.id.ll_subhead);
        final TextView tvSubhead = (TextView) llSubhead.findViewById(R.id.tv_subhead);
        final View tvLabel = llSubhead.findViewById(R.id.tv_label);
        tvSubhead.setTypeface(mApplication.getWoodBodyStyleFont());
        tvSubhead.setText(getString(R.string.sign_up));
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
        etImgCode = (EditText) findViewById(R.id.image_code_edit_txt);
        //将输入法弹出的右下角的按钮改为完成，不改的话会是下一步。
        etImgCode.setImeOptions(EditorInfo.IME_ACTION_DONE);

        etSMSCode = (EditText) findViewById(R.id.sms_code_edit_txt);
        ivImageCode = (ImageView) findViewById(R.id.image_code_iv_code);
        ivImageCode.setOnClickListener(this);
        btnSendSMS = (Button) findViewById(R.id.btn_sms_code);
        btnSendSMS.setOnClickListener(this);
        iconImgcode = (TextView) findViewById(R.id.image_code_txt_icon);
        iconImgcode.setTypeface(mApplication.getIconStyleFont());
        iconSMSCode = (TextView) findViewById(R.id.sms_code_txt_icon);
        iconSMSCode.setTypeface(mApplication.getIconStyleFont());

        btnSignUp = (CircularProgressButton) findViewById(R.id.btn_sign_up);
        btnSignUp.setIndeterminateProgressMode(true);//true=等待模式,false=进度条模式
        btnSignUp.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        mInstance = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(SignUpActivity.class.getSimpleName());
        MobclickAgent.onPause(this);
        try {
            if (timerUtil != null) {//取消计时器
                timerUtil.cancelTimer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MIUIUtils.isMIUISuspensionWindowPermission(this, contentLayout);
        MobclickAgent.onPageStart(SignUpActivity.class.getSimpleName());
        MobclickAgent.onResume(this);

        refreshImageCode();

        try {
            if (timerUtil == null)
                timerUtil = new TimerUtilImpl(this, btnSignUp);
            timerUtil.validateTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startAnimU2DActivity(Context context, Class<?> cla) {
        context.startActivity(new Intent(context, cla));
        // 两个参数分别表示进入的动画,退出的动画
        ((Activity) context).overridePendingTransition(R.anim.qr_code_anim_in, R.anim.qr_code_anim_out_below);
    }

    public static void startAnimU2DActivity(Context context, Intent intent) {
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.qr_code_anim_in, R.anim.qr_code_anim_out_below);
    }

    public static void startAnimU2DActivity(Context context, Intent intent, int requestCode) {
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.qr_code_anim_in, R.anim.qr_code_anim_out_below);
    }

    /**
     * 带动画的退出
     */
    private void finishActivityAnimU2D() {
        mInstance = null;
        this.finish();
        this.overridePendingTransition(R.anim.qr_code_anim_out_below, R.anim.qr_code_anim_out);
    }

    @Override
    public void onBackPressed() {
        //请求中不能退后
        try {
            if (btnSignUp.getProgress() == BUTTON_STATUS__CODE_LOADING)
                return;
        } catch (Exception e) {
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.btn_sign_up:
                registerValidate();
                break;
            case R.id.btn_sms_code:
                requestSmsCode();
                break;
            case R.id.image_code_iv_code:
                refreshImageCode();
                break;
        }
    }

    String phone;
    String verifyImageCode;
    String verifySmsCode;
    String password;

    /**
     * 注册验证
     */

    private void registerValidate() {

        switch (btnSignUp.getProgress()) {// 按钮状态
            case BUTTON_STATUS__CODE_SUCCESS:
            case BUTTON_STATUS__CODE_ERROR:
                btnSignUp.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                return;
            case BUTTON_STATUS__CODE_DEFAULT:
                break;
            default:
                return;
        }

        btnSignUp.setProgress(BUTTON_STATUS__CODE_LOADING);

        phone = cePhone.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            SuperToast.makeText(this, getString(R.string.business_card_holder_2_create_telephone_prompt),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (!VerifyUtil.isMobileNO(phone)) {
            SuperToast.makeText(this, getString(R.string.sign_in_phone_falseness),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        verifySmsCode = etSMSCode.getText().toString();
        if (TextUtils.isEmpty(verifySmsCode)) {
            SuperToast.makeText(this, getString(R.string.sign_up_et_sms_code_hint),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (verifySmsCode.length() != AppConstants.VERIFY_CODE_LENGTH) {
            SuperToast.makeText(this, getString(R.string.sign_up_btn_send_sms_code_no),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        password = cePwd.getText().toString();

        if (TextUtils.isEmpty(password)) {
            SuperToast.makeText(SignUpActivity.this, getString(R.string.pwd_hint),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (password.length() < AppConstants.PASSWORD_INTENSITY_LENGTH_MIN) {//
            SuperToast.makeText(SignUpActivity.this, getString(R.string.sign_in_password_intensity_prompt_min),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (password.length() > AppConstants.PASSWORD_INTENSITY_LENGTH_MAX) {//
            SuperToast.makeText(SignUpActivity.this, getString(R.string.sign_in_password_intensity_prompt_max),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }
        callUserVerityResponse = ApiServiceImpl.userVerityImpl(this, phone, new ResponseHandlerListener<VerityUsersResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnSignUp.setProgress(BUTTON_STATUS__CODE_LOADING);
                    }
                });
            }

            @Override
            public void success(final int statusCode, final Response<VerityUsersResponse> response) {
                super.success(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (statusCode == NetworkConstants.RESPONSE_CODE_NO_CONTENT_204) {
                            signUp();
                        } else {
                            VerityUsersResponse verityError = response.body();
                            if (verityError.getTelephone() != null) {
                                SuperToast.makeText(SignUpActivity.this, getString(R.string.sign_phone_exist),
                                        SuperToast.Icon.Resource.ERROR,
                                        SuperToast.Background.RED).show();
                                cePhone.setText("");
                            }
                            btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
                        }
                    }
                });
            }

            @Override
            public void failure(int statusCode, Object o) {
                super.failure(statusCode, o);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
                    }
                });
            }

            @Override
            public void clientError(int statusCode, Response<?> response) {
                super.clientError(statusCode, response);
            }

            @Override
            public void serverError(int statusCode, Response<?> response) {
                super.serverError(statusCode, response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }

        });

        if (callUserVerityResponse == null) {//没有网络 或者token
            try {
                btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
            } catch (Exception e) {
            }
        }

//        try {
//            callUserVerityResponse.cancel();
//        } catch (NullPointerException e) {}

    }

    /**
     * 注册
     */
    private void signUp() {
        callRequestTokenResponse = ApiServiceImpl.servicesRequestTokenImpl(this, mDeviceRequestToken, new ApiServiceImpl.OnServicesRequestTokenListener() {
            @Override
            public void onResult(String[] data) {
                if (!mDeviceRequestToken.equals(data[1]))
                    mDeviceRequestToken = data[1];

                callSignUpResponse = ApiServiceImpl.signUpImpl(SignUpActivity.this, phone, password, verifySmsCode, data[0], data[1], new ResponseHandlerListener<Void>(SignUpActivity.this) {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void success(final int statusCode, final Response<Void> response) {
                        super.success(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnSignUp.setProgress(BUTTON_STATUS__CODE_SUCCESS);
                                mApplication.getSpUtil().setIdentifyingCodeDate(0l);//注册成功清除验证码计时器之前的值
                                login();
//                                handler.sendEmptyMessageDelayed(1, 500);
                            }
                        });
                    }

                    @Override
                    public void failure(int statusCode, Object o) {
                        super.failure(statusCode, o);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
                            }
                        });
                    }

                    @Override
                    public void clientError(final int statusCode, final Response<?> response) {
                        super.clientError(statusCode, response);
                        refreshImageCode();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (statusCode) {
                                    case NetworkConstants.RESPONSE_CODE_UNPROCESSABLE_ENTITY_422:// 注册失败

                                        try {
                                            Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(SignUpActivity.this).
                                                    responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                            ErrorResponse errorObject = errorConverter.convert(response.errorBody());
                                            if (errorObject.getErrors() != null) {
                                                if (errorObject.getErrors().getVerity_code() != null) {
                                                    SuperToast.makeText(SignUpActivity.this, getString(R.string.sign_up_btn_send_sms_code_no),
                                                            SuperToast.Icon.Resource.ERROR,
                                                            SuperToast.Background.RED).show();
                                                    etSMSCode.setText("");
                                                    return;
                                                }

                                                if (errorObject.getErrors().getTelephone() != null) {
                                                    SuperToast.makeText(SignUpActivity.this, getString(R.string.sign_phone_exist),
                                                            SuperToast.Icon.Resource.ERROR,
                                                            SuperToast.Background.RED).show();
                                                    return;
                                                }
                                            }
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                        break;
                                    case NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400:// 坏请求;默认提示验证码不正确
                                        SuperToast.makeText(SignUpActivity.this, getString(R.string.sign_up_btn_send_sms_code_no),
                                                SuperToast.Icon.Resource.ERROR,
                                                SuperToast.Background.RED).show();
                                        etSMSCode.setText("");
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                    }

                    @Override
                    public void serverError(int statusCode, Response<?> response) {
                        super.serverError(statusCode, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                    }

                });

                if (callSignUpResponse == null) {//没有网络 或者token
                    try {
                        btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
                    } catch (Exception e) {
                    }
                }

//                try {
//                    callSignUpResponse.cancel();
//                } catch (NullPointerException e) {}
            }
        });

        if (callRequestTokenResponse == null) {//没有网络 或者token
            btnSignUp.setProgress(BUTTON_STATUS__CODE_ERROR);
        }

//        try {
//            callRequestTokenResponse.cancel();
//        } catch (NullPointerException e) { }
    }

    /**
     * 登录
     */
    private void login() {
        ErrorHandlingCallAdapter.MyCall<TokenResponse> callResponse = ApiServiceImpl.loginImpl(this, phone, password, new ResponseHandlerListener<TokenResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(SignUpActivity.this, getString(R.string.register_success_skip_login_loading),
                                SuperToast.Icon.Resource.INFO,
                                SuperToast.Background.BLUE).show();
                    }
                });
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
                            handler.sendEmptyMessageDelayed(2, 500);
                            return;
                        }
                        handler.sendEmptyMessageDelayed(1, 500);

                        mApplication.findTokenAndUsersInfo(SplashActivity.mInstance);

                        mApplication.getSpUtil().setCurrentUsersPhone(phone);
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
                                Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(SignUpActivity.this).
                                        responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                ErrorResponse errorObject = errorConverter.convert(response.errorBody());

                                if (errorObject.getCode() == NetworkConstants.RESPONSE_CODE_UNAUTHORIZED_401)
                                    SuperToast.makeText(SignUpActivity.this, errorObject.getMessage(),
                                            SuperToast.Icon.Resource.ERROR,
                                            SuperToast.Background.RED).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessageDelayed(2, 500);
                    }
                });
            }

            @Override
            public void clientError(int statusCode, Response<?> response) {
                super.clientError(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessageDelayed(2, 500);
                    }
                });
            }

            @Override
            public void serverError(int statusCode, final Response<?> response) {
                super.serverError(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.errorBody() != null) {
                            try {
                                Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(SignUpActivity.this).
                                        responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                ErrorResponse errorObject = errorConverter.convert(response.errorBody());

                                if (errorObject.getCode() == NetworkConstants.RESPONSE_CODE_UNAUTHORIZED_401)
                                    SuperToast.makeText(SignUpActivity.this, errorObject.getMessage(),
                                            SuperToast.Icon.Resource.ERROR,
                                            SuperToast.Background.RED).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessageDelayed(2, 500);
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

                    }
                });
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }
        });

        if (callResponse == null) {//没有网络 或者token
            SuperToast.makeText(SignUpActivity.this, getString(R.string.register_success_skip_login),
                    SuperToast.Icon.Resource.INFO,
                    SuperToast.Background.BLUE).show();
            handler.sendEmptyMessageDelayed(2, 500);
        }

//        try {
//            callResponse.cancel();
//        } catch (NullPointerException e) {}
    }

    /**
     * 请求验证码
     */
    private void requestSmsCode() {
        boolean isEnable = (boolean) btnSendSMS.getTag();
        if (!isEnable)
            return;

        phone = cePhone.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            SuperToast.makeText(this, getString(R.string.phone_hint),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            return;
        }

        if (!VerifyUtil.isMobileNO(phone)) {
            SuperToast.makeText(this, getString(R.string.sign_in_phone_falseness),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            return;
        }

        verifyImageCode = etImgCode.getText().toString();
        if (TextUtils.isEmpty(verifyImageCode)) {
            SuperToast.makeText(this, getString(R.string.sign_up_et_image_code_hint),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            return;
        }

        if (verifyImageCode.length() != AppConstants.VERIFY_IMAGE_CODE_LENGTH) {
            SuperToast.makeText(this, getString(R.string.sign_up_btn_send_verification_code_no),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            return;
        }

        callRequestTokenResponse = ApiServiceImpl.servicesRequestTokenImpl(this, mDeviceRequestToken, new ApiServiceImpl.OnServicesRequestTokenListener() {
            @Override
            public void onResult(String[] data) {
                if (!mDeviceRequestToken.equals(data[1]))
                    mDeviceRequestToken = data[1];
                callSmsCodeResponse = ApiServiceImpl.servicesSmsCodeImpl(SignUpActivity.this, phone, verifyImageCode, data[0], data[1], new ResponseHandlerListener<Void>(SignUpActivity.this) {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void success(final int statusCode, final Response<Void> response) {
                        super.success(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timerUtil.createTimer(0);
                            }
                        });
//                DebugEntity debugEntity = response.body();
//                Log.e("//--------debug---------//", "success: " + debugEntity.toString());
                    }

                    @Override
                    public void failure(int statusCode, Object o) {
                        super.failure(statusCode, o);
                        btnSendSMS.setTag(true);
                    }

                    @Override
                    public void clientError(final int statusCode, final Response<?> response) {
                        super.clientError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    switch (statusCode) {
                                        case NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400:
                                            Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(SignUpActivity.this).
                                                    responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                            ErrorResponse errorObject = errorConverter.convert(response.errorBody());
                                            SuperToast.makeText(SignUpActivity.this, errorObject.getMessage(),
                                                    SuperToast.Icon.Resource.ERROR,
                                                    SuperToast.Background.RED).show();
                                            break;
                                        default:
                                            SuperToast.makeText(SignUpActivity.this, getString(R.string.sign_up_btn_send_verification_code_no),
                                                    SuperToast.Icon.Resource.ERROR,
                                                    SuperToast.Background.RED).show();
                                            break;
                                    }
                                    etImgCode.setText("");
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void serverError(int statusCode, final Response<?> response) {
                        super.serverError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SuperToast.makeText(SignUpActivity.this, getString(R.string.sign_up_btn_send_verification_code_no),
                                        SuperToast.Icon.Resource.ERROR,
                                        SuperToast.Background.RED).show();
                            }
                        });
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                    }

                });

                if (callSmsCodeResponse == null) {//没有网络 或者token
                } else {
                    btnSendSMS.setTag(false);
                }

//                try {
//                    callSmsCodeResponse.cancel();
//                } catch (NullPointerException e) {}

            }
        });

        if (callRequestTokenResponse == null) {//没有网络 或者token

        }

//        try {
//            callRequestTokenResponse.cancel();
//        } catch (NullPointerException e) { }
    }

    /**
     * 刷新图片验证码
     */
    private void refreshImageCode() {
        callRequestTokenResponse = ApiServiceImpl.servicesRequestTokenImpl(this, mDeviceRequestToken, new ApiServiceImpl.OnServicesRequestTokenListener() {
            @Override
            public void onResult(String[] data) {
                if (!mDeviceRequestToken.equals(data[1]))
                    mDeviceRequestToken = data[1];
                //		ImageLoader.getInstance().displayImage(NetworkConstants._URL_SERVICES_IMAGE_CODE_GET, ivImageCode, ImageLoadOptions.getOptionImageCode());
                callImageCodeResponse = ApiServiceImpl.servicesImageCodeImpl(SignUpActivity.this, data[0], data[1], new ResponseHandlerListener<ResponseBody>(SignUpActivity.this) {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void success(final int statusCode, final Response<ResponseBody> response) {
                        super.success(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (statusCode == NetworkConstants.RESPONSE_CODE_OK_200) {
                                    Bitmap bitmap;
                                    try {
                                        byte[] responseByte = response.body().bytes();
                                        if (responseByte.length != 0) {
                                            bitmap = BitmapFactory.decodeByteArray(responseByte, 0, responseByte.length);
                                            ivImageCode.setImageBitmap(bitmap);
                                        } else {
                                            SuperToast.makeText(SignUpActivity.this, getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
                                                    SuperToast.Icon.Resource.ERROR,
                                                    SuperToast.Background.RED).show();
                                        }
                                    } catch (IOException e) {
                                        SuperToast.makeText(SignUpActivity.this, getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
                                                SuperToast.Icon.Resource.ERROR,
                                                SuperToast.Background.RED).show();
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void clientError(int statusCode, Response<?> response) {
                        super.clientError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SuperToast.makeText(SignUpActivity.this, getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
                                        SuperToast.Icon.Resource.ERROR,
                                        SuperToast.Background.RED).show();
                            }
                        });
                    }

                    @Override
                    public void serverError(int statusCode, Response<?> response) {
                        super.serverError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SuperToast.makeText(SignUpActivity.this, getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
                                        SuperToast.Icon.Resource.ERROR,
                                        SuperToast.Background.RED).show();
                            }
                        });
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                    }

                });

                if (callImageCodeResponse == null) {//没有网络 或者token

                }

//                try {
//                    callImageCodeResponse.cancel();
//                } catch (NullPointerException e) {}
            }
        });

        if (callRequestTokenResponse == null) {//没有网络 或者token

        }

//        try {
//            callRequestTokenResponse.cancel();
//        } catch (NullPointerException e) { }
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
                case 2:
                    try {
                        finish();
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