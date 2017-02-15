package com.iyuile.caelum.fragment;

import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.UpdateInfoActivity;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.response.RequestTokenResponse;
import com.iyuile.caelum.entity.response.VerityUsersResponse;
import com.iyuile.caelum.util.ButtonUtils;
import com.iyuile.caelum.util.TimerUtilImpl;
import com.iyuile.caelum.util.VerifyUtil;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.circularprogressbutton.CircularProgressButton;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @Description 修改新的手机号
 */
public class UpdateNewPhoneFragment extends BaseFragment implements OnClickListener {

    private static final int BUTTON_STATUS__CODE_SUCCESS = 100; // 成功
    private static final int BUTTON_STATUS__CODE_ERROR = -1; // 错误
    private static final int BUTTON_STATUS__CODE_DEFAULT = 0; // 默认
    private static final int BUTTON_STATUS__CODE_LOADING = 50; // 加载中

    private ErrorHandlingCallAdapter.MyCall<ResponseBody> callImageCodeResponse;
    private ErrorHandlingCallAdapter.MyCall<VerityUsersResponse> callUserVerityResponse;
    private ErrorHandlingCallAdapter.MyCall<Void> callSmsCodeResponse;

    private ErrorHandlingCallAdapter.MyCall<RequestTokenResponse> callRequestTokenResponse;
    private String mDeviceRequestToken = "";

    private TimerUtilImpl timerUtil;// 计时器

    private TextView tvReturn;
    public CircularProgressButton btnNextStep;
    private EditText etPhone;
    private EditText etImageCode;

    private ImageView ivImageCode;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_new_phone, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        btnNextStep.setTag(true);
        timerUtil = new TimerUtilImpl(getActivity(), btnNextStep);
        timerUtil.validateTime();

    }

    /**
     * 加载view
     */
    private void initView() {
        tvReturn = (TextView) getActivity().findViewById(R.id.tv_return);
        btnNextStep = (CircularProgressButton) getActivity().findViewById(R.id.wb_btn_next_step);
        btnNextStep.setIndeterminateProgressMode(true);//true=等待模式,false=进度条模式

        etPhone = (EditText) getActivity().findViewById(R.id.wb_et_phone);
        etImageCode = (EditText) getActivity().findViewById(R.id.wb_et_image_code);

        ivImageCode = (ImageView) getActivity().findViewById(R.id.iv_image_code);

        tvReturn.setOnClickListener(this);
        btnNextStep.setOnClickListener(this);
        ivImageCode.setOnClickListener(this);

        tvReturn.setTypeface(mApplication.getIconStyleFont());

    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;

        switch (v.getId()) {
            case R.id.tv_return:
                if (btnNextStep.getProgress() == BUTTON_STATUS__CODE_LOADING)// 进行中让退出键失效
                    return;
                UpdateInfoActivity.mInstance.setTabSelection(4, false);
                break;
            case R.id.wb_btn_next_step:
                validate();
                break;
            case R.id.iv_image_code:
                refreshImageCode();
                break;
            default:
                break;
        }
    }

    /**
     * 刷新图片验证码
     */
    private void refreshImageCode() {
        callRequestTokenResponse = ApiServiceImpl.servicesRequestTokenImpl(getActivity(), mDeviceRequestToken, new ApiServiceImpl.OnServicesRequestTokenListener() {
            @Override
            public void onResult(String[] data) {
                if (!mDeviceRequestToken.equals(data[1]))
                    mDeviceRequestToken = data[1];
//		        ImageLoader.getInstance().displayImage(NetworkConstants._URL_SERVICES_IMAGE_CODE_GET, ivImageCode, ImageLoadOptions.getOptionImageCode());
                callImageCodeResponse = ApiServiceImpl.servicesImageCodeImpl(getActivity(), data[0], data[1], new ResponseHandlerListener<ResponseBody>(getActivity()) {

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
                                            SuperToast.makeText(getActivity(), getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
                                                    SuperToast.Icon.Resource.ERROR,
                                                    SuperToast.Background.RED).show();
                                        }
                                    } catch (IOException e) {
                                        SuperToast.makeText(getActivity(), getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
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
                                SuperToast.makeText(getActivity(), getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
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
                                SuperToast.makeText(getActivity(), getString(R.string.sgin_tv_graph_identifying_code_refresh_failure),
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

//        try {
//            callImageCodeResponse.cancel();
//        } catch (NullPointerException e) {}

            }
        });

        if (callRequestTokenResponse == null) {//没有网络 或者token
        }

//        try {
//            callRequestTokenResponse.cancel();
//        } catch (NullPointerException e) { }

    }

    String phone;
    String verifyImageCode;

    /**
     * 验证
     */
    private void validate() {

        boolean isEnable = (boolean) btnNextStep.getTag();
        if (!isEnable)
            return;

        switch (btnNextStep.getProgress()) {// 按钮状态
            case BUTTON_STATUS__CODE_SUCCESS:
            case BUTTON_STATUS__CODE_ERROR:
                btnNextStep.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                return;
            case BUTTON_STATUS__CODE_DEFAULT:
                break;
            default:
                return;
        }

        btnNextStep.setProgress(BUTTON_STATUS__CODE_LOADING);

        phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            SuperToast.makeText(getActivity(), getString(R.string.update_new_phone_description),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (!VerifyUtil.isMobileNO(phone)) {
            SuperToast.makeText(getActivity(), getString(R.string.sign_in_phone_falseness),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        verifyImageCode = etImageCode.getText().toString();
        if (TextUtils.isEmpty(verifyImageCode)) {
            SuperToast.makeText(getActivity(), getString(R.string.sign_up_et_image_code_hint),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (verifyImageCode.length() != AppConstants.VERIFY_IMAGE_CODE_LENGTH) {
            SuperToast.makeText(getActivity(), getString(R.string.sign_up_btn_send_verification_code_no),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        callUserVerityResponse = ApiServiceImpl.userVerityImpl(getActivity(), phone, new ResponseHandlerListener<VerityUsersResponse>(getActivity()) {

            @Override
            public void onStart() {
                super.onStart();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnNextStep.setProgress(BUTTON_STATUS__CODE_LOADING);
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
                            requestSmsCode();
                            return;
                        }
                        try {
                            Gson gson = new Gson();
                            VerityUsersResponse verityError = response.body();
                            if (verityError.getTelephone() != null) {
                                SuperToast.makeText(getActivity(), getString(R.string.sign_phone_exist),
                                        SuperToast.Icon.Resource.ERROR,
                                        SuperToast.Background.RED).show();
                                etPhone.setText("");
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        }
                        btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
                    }
                });
            }

            @Override
            public void failure(int statusCode, Object o) {
                super.failure(statusCode, o);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnNextStep.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                    }
                });
            }

        });

        if (callUserVerityResponse == null) {//没有网络 或者token
            try {
                btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
            } catch (Exception e) {
            }
        }

//        try {
//            callUserVerityResponse.cancel();
//        } catch (NullPointerException e) {}

    }

    /**
     * 请求验证码
     */
    private void requestSmsCode() {
        callRequestTokenResponse = ApiServiceImpl.servicesRequestTokenImpl(getActivity(), mDeviceRequestToken, new ApiServiceImpl.OnServicesRequestTokenListener() {
            @Override
            public void onResult(String[] data) {
                if (!mDeviceRequestToken.equals(data[1]))
                    mDeviceRequestToken = data[1];
                callSmsCodeResponse = ApiServiceImpl.servicesSmsCodeImpl(getActivity(), phone, verifyImageCode, data[0], data[1], new ResponseHandlerListener<Void>(getActivity()) {

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
                                btnNextStep.setProgress(BUTTON_STATUS__CODE_SUCCESS);
                                timerUtil.createTimer(0);
                                UpdateInfoActivity.mInstance.setPhoneNew(phone);

                                handler.sendEmptyMessageDelayed(1, 1000);

                            }
                        });
                    }

                    @Override
                    public void failure(int statusCode, Object o) {
                        super.failure(statusCode, o);
                        btnNextStep.setTag(true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
                            }
                        });
                    }

                    @Override
                    public void clientError(final int statusCode, final Response<?> response) {
                        super.clientError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (statusCode == NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400) {
                                    SuperToast.makeText(getActivity(), getString(R.string.sign_up_btn_send_verification_code_no),
                                            SuperToast.Icon.Resource.ERROR,
                                            SuperToast.Background.RED).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void serverError(final int statusCode, final Response<?> response) {
                        super.serverError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (statusCode == NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400) {
                                    SuperToast.makeText(getActivity(), getString(R.string.sign_up_btn_send_verification_code_no),
                                            SuperToast.Icon.Resource.ERROR,
                                            SuperToast.Background.RED).show();
                                }
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnNextStep.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                            }
                        });
                    }

                });

                if (callSmsCodeResponse == null) {//没有网络 或者token
                    btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
                } else {
                    btnNextStep.setTag(false);
                }

//        try {
//            callSmsCodeResponse.cancel();
//        } catch (NullPointerException e) {}
            }
        });

        if (callRequestTokenResponse == null) {//没有网络 或者token
            btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
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
                    UpdateInfoActivity.mInstance.setTabSelection(6, true);
                    etImageCode.setText("");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(UpdateNewPhoneFragment.class.getSimpleName());

        try {
            if (timerUtil != null) {//取消计时器
                timerUtil.cancelTimer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(UpdateNewPhoneFragment.class.getSimpleName());

        refreshImageCode();

        try {
            if (timerUtil == null)
                timerUtil = new TimerUtilImpl(getActivity(), btnNextStep);
            timerUtil.validateTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
