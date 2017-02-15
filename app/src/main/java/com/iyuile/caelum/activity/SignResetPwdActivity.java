package com.iyuile.caelum.activity;

import android.annotation.TargetApi;
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
import com.iyuile.caelum.entity.response.VerityUsersResponse;
import com.iyuile.caelum.util.ButtonUtils;
import com.iyuile.caelum.util.MIUIUtils;
import com.iyuile.caelum.util.RetrofitUtils;
import com.iyuile.caelum.util.TimerUtilImpl;
import com.iyuile.caelum.util.VerifyUtil;
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
 * 重置密码
 */
public class SignResetPwdActivity extends BaseActivity implements View.OnClickListener {

    public static SignResetPwdActivity mInstance;

    private static final int BUTTON_STATUS__CODE_SUCCESS = 100; // 成功
    private static final int BUTTON_STATUS__CODE_ERROR = -1; // 错误
    private static final int BUTTON_STATUS__CODE_DEFAULT = 0; // 默认
    private static final int BUTTON_STATUS__CODE_LOADING = 50; // 加载中

    private ErrorHandlingCallAdapter.MyCall<ResponseBody> callImageCodeResponse;
    private ErrorHandlingCallAdapter.MyCall<Void> callSmsCodeResponse;
    private ErrorHandlingCallAdapter.MyCall<VerityUsersResponse> callUserVerityResponse;
    private ErrorHandlingCallAdapter.MyCall<Void> callRestPwdResponse;

    private ErrorHandlingCallAdapter.MyCall<RequestTokenResponse> callRequestTokenResponse;
    private String mDeviceRequestToken = "";

    private View contentLayout;
    private CustomEditText cePhone, cePwd;
    private TextView iconImgcode, iconSMSCode;
    private EditText etImgCode, etSMSCode;
    private CircularProgressButton btnResetPwd;

    private ImageView ivImageCode;
    private Button btnSendSMS;

    private TimerUtilImpl timerUtil;// 计时器

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;

        mApplication.mActivityManager.addActivity(this);
        setContentView(R.layout.activity_sign_reset_pwd);
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
        tvSubhead.setText(getString(R.string.sign_reset_pwd));
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

        btnResetPwd = (CircularProgressButton) findViewById(R.id.btn_sign_reset_pwd);
        btnResetPwd.setIndeterminateProgressMode(true);//true=等待模式,false=进度条模式
        btnResetPwd.setOnClickListener(this);

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
        MobclickAgent.onPageStart(SignInActivity.class.getSimpleName());
        MobclickAgent.onResume(this);

        refreshImageCode();

        try {
            if (timerUtil == null)
                timerUtil = new TimerUtilImpl(this, btnResetPwd);
            timerUtil.validateTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //请求中不能退后
        try {
            if (btnResetPwd.getProgress() == BUTTON_STATUS__CODE_LOADING)
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
            case R.id.btn_sign_reset_pwd:
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

        switch (btnResetPwd.getProgress()) {// 按钮状态
            case BUTTON_STATUS__CODE_SUCCESS:
            case BUTTON_STATUS__CODE_ERROR:
                btnResetPwd.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                return;
            case BUTTON_STATUS__CODE_DEFAULT:
                break;
            default:
                return;
        }

        btnResetPwd.setProgress(BUTTON_STATUS__CODE_LOADING);

        phone = cePhone.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            SuperToast.makeText(this, getString(R.string.business_card_holder_2_create_telephone_prompt),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (!VerifyUtil.isMobileNO(phone)) {
            SuperToast.makeText(this, getString(R.string.sign_in_phone_falseness),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        verifySmsCode = etSMSCode.getText().toString();
        if (TextUtils.isEmpty(verifySmsCode)) {
            SuperToast.makeText(this, getString(R.string.sign_up_et_sms_code_hint),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (verifySmsCode.length() != AppConstants.VERIFY_CODE_LENGTH) {
            SuperToast.makeText(this, getString(R.string.sign_up_btn_send_sms_code_no),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        password = cePwd.getText().toString();

        if (TextUtils.isEmpty(password)) {
            SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.pwd_hint),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (password.length() < AppConstants.PASSWORD_INTENSITY_LENGTH_MIN) {//
            SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sign_in_password_intensity_prompt_min),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (password.length() > AppConstants.PASSWORD_INTENSITY_LENGTH_MAX) {//
            SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sign_in_password_intensity_prompt_max),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }
        callUserVerityResponse = ApiServiceImpl.userVerityImpl(this, phone, new ResponseHandlerListener<VerityUsersResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnResetPwd.setProgress(BUTTON_STATUS__CODE_LOADING);
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
                            // 手机号不存在,所以不能发送验证码,也就是不能使用改密码功能
                            SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sign_phone_exist_no),
                                    SuperToast.Icon.Resource.ERROR,
                                    SuperToast.Background.RED).show();
                            cePhone.setText("");
                            btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
                        } else {
                            restPwd();
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
                        btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
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
                btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            } catch (Exception e) {
            }
        }

//        try {
//            callUserVerityResponse.cancel();
//        } catch (NullPointerException e) {}

    }

    /**
     * 重设密码
     */
    private void restPwd() {
        callRequestTokenResponse = ApiServiceImpl.servicesRequestTokenImpl(this, mDeviceRequestToken, new ApiServiceImpl.OnServicesRequestTokenListener() {
            @Override
            public void onResult(String[] data) {
                if (!mDeviceRequestToken.equals(data[1]))
                    mDeviceRequestToken = data[1];

                callRestPwdResponse = ApiServiceImpl.restPwdImpl(SignResetPwdActivity.this, phone, password, verifySmsCode, data[0], data[1], new ResponseHandlerListener<Void>(SignResetPwdActivity.this) {

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
                                btnResetPwd.setProgress(BUTTON_STATUS__CODE_SUCCESS);

                                handler.sendEmptyMessageDelayed(1, 500);
                                mApplication.getSpUtil().setIdentifyingCodeDate(0l);//重置密码成功清除验证码计时器之前的值
                            }
                        });
                    }

                    @Override
                    public void failure(int statusCode, Object o) {
                        super.failure(statusCode, o);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
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
                                btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
                                switch (statusCode) {
                                    case NetworkConstants.RESPONSE_CODE_UNPROCESSABLE_ENTITY_422:// 注册失败

                                        try {
                                            Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(SignResetPwdActivity.this).
                                                    responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                            ErrorResponse errorObject = errorConverter.convert(response.errorBody());
                                            if (errorObject.getErrors() != null) {
                                                if (errorObject.getErrors().getVerity_code() != null) {
                                                    SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sign_up_btn_send_sms_code_no),
                                                            SuperToast.Icon.Resource.ERROR,
                                                            SuperToast.Background.RED).show();
                                                    etSMSCode.setText("");
                                                    return;
                                                }

                                                if (errorObject.getErrors().getTelephone() != null) {
                                                    SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sign_phone_exist),
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
                                        SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sign_up_btn_send_sms_code_no),
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

                if (callRestPwdResponse == null) {//没有网络 或者token
                    try {
                        btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
                    } catch (Exception e) {
                    }
                }

//                try {
//                    callRestPwdResponse.cancel();
//                } catch (NullPointerException e) { }
            }
        });

        if (callRequestTokenResponse == null) {//没有网络 或者token
            btnResetPwd.setProgress(BUTTON_STATUS__CODE_ERROR);
        }

//        try {
//            callRequestTokenResponse.cancel();
//        } catch (NullPointerException e) { }
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
                callSmsCodeResponse = ApiServiceImpl.servicesSmsCodeImpl(SignResetPwdActivity.this, phone, verifyImageCode, data[0], data[1], new ResponseHandlerListener<Void>(SignResetPwdActivity.this) {

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
                                            Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(SignResetPwdActivity.this).
                                                    responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                            ErrorResponse errorObject = errorConverter.convert(response.errorBody());
                                            SuperToast.makeText(SignResetPwdActivity.this, errorObject.getMessage(),
                                                    SuperToast.Icon.Resource.ERROR,
                                                    SuperToast.Background.RED).show();
                                            break;
                                        default:
                                            SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sign_up_btn_send_verification_code_no),
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
                                SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sign_up_btn_send_verification_code_no),
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
                callImageCodeResponse = ApiServiceImpl.servicesImageCodeImpl(SignResetPwdActivity.this, data[0], data[1], new ResponseHandlerListener<ResponseBody>(SignResetPwdActivity.this) {

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
                                            SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
                                                    SuperToast.Icon.Resource.ERROR,
                                                    SuperToast.Background.RED).show();
                                        }
                                    } catch (IOException e) {
                                        SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
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
                                SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
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
                                SuperToast.makeText(SignResetPwdActivity.this, getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
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