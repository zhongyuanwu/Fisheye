package com.iyuile.caelum.fragment;

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
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.UpdateInfoActivity;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.response.ErrorResponse;
import com.iyuile.caelum.entity.response.RequestTokenResponse;
import com.iyuile.caelum.util.ButtonUtils;
import com.iyuile.caelum.util.RetrofitUtils;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.circularprogressbutton.CircularProgressButton;
import com.umeng.analytics.MobclickAgent;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * @Description 修改新的手机号验证码
 */
public class UpdateNewPhoneCodeFragment extends BaseFragment implements OnClickListener {

    private static final int BUTTON_STATUS__CODE_SUCCESS = 100; // 成功
    private static final int BUTTON_STATUS__CODE_ERROR = -1; // 错误
    private static final int BUTTON_STATUS__CODE_DEFAULT = 0; // 默认
    private static final int BUTTON_STATUS__CODE_LOADING = 50; // 加载中

    private ErrorHandlingCallAdapter.MyCall<Void> callUpdatePhoneResponse;

    private ErrorHandlingCallAdapter.MyCall<RequestTokenResponse> callRequestTokenResponse;
    private String mDeviceRequestToken = "";

    private TextView tvReturn;
    public CircularProgressButton btnSave;
    private EditText etCode;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_new_phone_code, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    /**
     * 加载view
     */
    private void initView() {
        tvReturn = (TextView) getActivity().findViewById(R.id.tv_return_code);
        btnSave = (CircularProgressButton) getActivity().findViewById(R.id.wb_update_btn_save);
        btnSave.setIndeterminateProgressMode(true);//true=等待模式,false=进度条模式

        etCode = (EditText) getActivity().findViewById(R.id.wb_update_code);

        tvReturn.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        tvReturn.setTypeface(mApplication.getIconStyleFont());
    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;

        switch (v.getId()) {
            case R.id.tv_return_code:
                if (btnSave.getProgress() == BUTTON_STATUS__CODE_LOADING)// 进行中让退出键失效
                    return;
                UpdateInfoActivity.mInstance.setTabSelection(5, false);
                break;
            case R.id.wb_update_btn_save:
                changePhone();
                break;

            default:
                break;
        }
    }

    /**
     * 更换手机号
     */
    private void changePhone() {

        switch (btnSave.getProgress()) {// 按钮状态
            case BUTTON_STATUS__CODE_SUCCESS:
            case BUTTON_STATUS__CODE_ERROR:
                btnSave.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                return;
            case BUTTON_STATUS__CODE_DEFAULT:
                break;
            default:
                return;
        }

        btnSave.setProgress(BUTTON_STATUS__CODE_LOADING);

        final String verifyCode = etCode.getText().toString().trim();

        if (TextUtils.isEmpty(verifyCode)) {
            SuperToast.makeText(getActivity(), getString(R.string.sign_up_et_sms_code_hint),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSave.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (verifyCode.length() != AppConstants.VERIFY_CODE_LENGTH) {
            SuperToast.makeText(getActivity(), getString(R.string.sign_up_btn_send_sms_code_no),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            btnSave.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        final long userId;

        try {
            userId = mApplication.mUserObject.getId();

            if (userId == 0) {
                SuperToast.makeText(getActivity(), getString(R.string.unknown_code_3012),
                        SuperToast.Icon.Resource.ERROR,
                        SuperToast.Background.RED).show();
                btnSave.setProgress(BUTTON_STATUS__CODE_ERROR);
                return;
            }
        } catch (Exception e1) {
            SuperToast.makeText(getActivity(), getString(R.string.unknown_code_3012),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            e1.printStackTrace();
            btnSave.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (UpdateInfoActivity.mInstance.getPhoneOld() == null || UpdateInfoActivity.mInstance.getPhoneNew() == null) {
            SuperToast.makeText(getActivity(), getString(R.string.unknown_code_2012),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            handler.sendEmptyMessageDelayed(2, 500);
            btnSave.setProgress(BUTTON_STATUS__CODE_DEFAULT);
            return;
        }

        final String phoneOld = UpdateInfoActivity.mInstance.getPhoneOld();
        final String phoneNew = UpdateInfoActivity.mInstance.getPhoneNew();

        callRequestTokenResponse = ApiServiceImpl.servicesRequestTokenImpl(getActivity(), mDeviceRequestToken, new ApiServiceImpl.OnServicesRequestTokenListener() {
            @Override
            public void onResult(String[] data) {
                if (!mDeviceRequestToken.equals(data[1]))
                    mDeviceRequestToken = data[1];
                callUpdatePhoneResponse = ApiServiceImpl.updateTelephoneImpl(getActivity(), userId, phoneOld, phoneNew, verifyCode, data[0], data[1], new ResponseHandlerListener<Void>(getActivity()) {

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
                                btnSave.setProgress(BUTTON_STATUS__CODE_SUCCESS);
                                handler.sendEmptyMessageDelayed(1, 1000);
                                try {
                                    mApplication.mUserObject.setTelephone(phoneNew);
                                    MyFragment.mInstance.handler.sendEmptyMessage(1);
                                } catch (Exception e) {
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
                                btnSave.setProgress(BUTTON_STATUS__CODE_ERROR);
                            }
                        });
                    }

                    @Override
                    public void clientError(final int statusCode, final Response<?> response) {
                        super.clientError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                switch (statusCode) {
                                    case NetworkConstants.RESPONSE_CODE_UNPROCESSABLE_ENTITY_422:// 重置密码失败

                                        try {
                                            Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(getContext()).
                                                    responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                            ErrorResponse errorObject = errorConverter.convert(response.errorBody());

                                            if (errorObject.getErrors() != null) {
                                                if (errorObject.getErrors().getVerity_code() != null) {
                                                    SuperToast.makeText(getActivity(), getString(R.string.sign_up_btn_send_sms_code_no),
                                                            SuperToast.Icon.Resource.ERROR,
                                                            SuperToast.Background.RED).show();
                                                    etCode.setText("");
                                                    return;
                                                }

                                                handler.sendEmptyMessageDelayed(3, 500);//回到上一个页面,新手机号

                                                if (errorObject.getErrors().getTelephone() != null) {
                                                    SuperToast.makeText(getActivity(), getString(R.string.sign_phone_exist),
                                                            SuperToast.Icon.Resource.ERROR,
                                                            SuperToast.Background.RED).show();
                                                    return;
                                                }
                                            }
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                        break;
                                    case NetworkConstants.RESPONSE_CODE_FORBIDDEN_403:

                                        try {
                                            Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(getContext()).
                                                    responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                            ErrorResponse errorObject = errorConverter.convert(response.errorBody());
                                            if (errorObject.getMessage() != null) {
                                                SuperToast.makeText(getActivity(), errorObject.getMessage(),
                                                        SuperToast.Icon.Resource.ERROR,
                                                        SuperToast.Background.RED).show();
                                            }
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }

                                        break;
                                    default:
                                        break;
                                }


                            }
                        });


                    }

                    @Override
                    public void serverError(final int statusCode, final Response<?> response) {
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

                if (callUpdatePhoneResponse == null) {//没有网络 或者token
                    try {
                        btnSave.setProgress(BUTTON_STATUS__CODE_ERROR);
                    } catch (Exception e) {
                    }
                }

//        try {
//            callUpdatePhoneResponse.cancel();
//        } catch (NullPointerException e) {}
            }
        });

        if (callRequestTokenResponse == null) {//没有网络 或者token
            try {
                btnSave.setProgress(BUTTON_STATUS__CODE_ERROR);
            } catch (Exception e) {
            }
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
                        UpdateInfoActivity.mInstance.loginSuccessFinish();
                    } catch (Exception e) {
                    }
                    break;
                case 2:
                    UpdateInfoActivity.mInstance.setTabSelection(4, false);
                case 3:
                    UpdateInfoActivity.mInstance.setTabSelection(5, false);
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
        MobclickAgent.onPageEnd(UpdateNewPhoneCodeFragment.class.getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(UpdateNewPhoneCodeFragment.class.getSimpleName());
    }

}
