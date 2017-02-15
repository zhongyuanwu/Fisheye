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
 * @author WangYao
 * @version 1
 * @Description 修改密码
 * @ProjectName Apus
 * @ClassName {@link UpdatePwdFragment}
 * @Date 2016-3-30 下午6:24:41
 */
public class UpdatePwdFragment extends BaseFragment implements OnClickListener {

    private static final int BUTTON_STATUS__CODE_SUCCESS = 100; // 成功
    private static final int BUTTON_STATUS__CODE_ERROR = -1; // 错误
    private static final int BUTTON_STATUS__CODE_DEFAULT = 0; // 默认
    private static final int BUTTON_STATUS__CODE_LOADING = 50; // 加载中

    private TextView tvClose;

    public CircularProgressButton btnUpdatePwd;
    private EditText cePwdOld, cePwdNew;

    private ErrorHandlingCallAdapter.MyCall<Void> callUpdatePwdResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_pwd, null);
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
        tvClose = (TextView) getActivity().findViewById(R.id.tv_close);
        btnUpdatePwd = (CircularProgressButton) getActivity().findViewById(R.id.wb_update_btn_update);
        btnUpdatePwd.setIndeterminateProgressMode(true);//true=等待模式,false=进度条模式

        cePwdOld = (EditText) getActivity().findViewById(R.id.wb_update_pwd_old);
        cePwdNew = (EditText) getActivity().findViewById(R.id.wb_update_pwd_new);

        tvClose.setOnClickListener(this);
        btnUpdatePwd.setOnClickListener(this);

        tvClose.setTypeface(mApplication.getIconStyleFont());

    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;

        switch (v.getId()) {
            case R.id.tv_close:
                if (btnUpdatePwd.getProgress() == BUTTON_STATUS__CODE_LOADING)// 进行中让退出键失效
                    return;
                UpdateInfoActivity.mInstance.finishActivityAnimU2D();
                break;
            case R.id.wb_update_btn_update:
                resetPwd();
                break;
            default:
                break;
        }
    }

    /**
     * 重置密码
     */
    private void resetPwd() {

        switch (btnUpdatePwd.getProgress()) {// 按钮状态
            case BUTTON_STATUS__CODE_SUCCESS:
            case BUTTON_STATUS__CODE_ERROR:
                btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                return;
            case BUTTON_STATUS__CODE_DEFAULT:
                break;
            default:
                return;
        }

        btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_LOADING);

        String pwdOld = cePwdOld.getText().toString().trim();

        if (TextUtils.isEmpty(pwdOld)) {
            SuperToast.makeText(getActivity(), getString(R.string.update_pwd_old_description),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (pwdOld.length() < AppConstants.PASSWORD_INTENSITY_LENGTH_MIN) {//
            SuperToast.makeText(getActivity(), getString(R.string.sign_in_password_intensity_error_prompt),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (pwdOld.length() > AppConstants.PASSWORD_INTENSITY_LENGTH_MAX) {//
            SuperToast.makeText(getActivity(), getString(R.string.sign_in_password_intensity_error_prompt),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        String pwdNew = cePwdNew.getText().toString().trim();

        if (TextUtils.isEmpty(pwdNew)) {
            SuperToast.makeText(getActivity(), getString(R.string.update_pwd_new_description),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (pwdNew.length() < AppConstants.PASSWORD_INTENSITY_LENGTH_MIN) {//
            SuperToast.makeText(getActivity(), getString(R.string.sign_in_password_intensity_prompt_min),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (pwdNew.length() > AppConstants.PASSWORD_INTENSITY_LENGTH_MAX) {//
            SuperToast.makeText(getActivity(), getString(R.string.sign_in_password_intensity_prompt_max),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        long userId;

        try {
            userId = mApplication.mUserObject.getId();

            if (userId == 0) {
                SuperToast.makeText(getActivity(), getString(R.string.unknown_code_3012),
                        SuperToast.Icon.Resource.ERROR,
                        SuperToast.Background.RED).show();
                btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_ERROR);
                return;
            }
        } catch (Exception e1) {
            SuperToast.makeText(getActivity(), getString(R.string.unknown_code_3012),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            e1.printStackTrace();
            btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }
        callUpdatePwdResponse = ApiServiceImpl.updatePwdImpl(getActivity(), userId, pwdOld, pwdNew, new ResponseHandlerListener<Void>(getActivity()) {

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
                                btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_SUCCESS);
                            }
                        });
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }

                    @Override
                    public void failure(int statusCode, Object o) {
                        super.failure(statusCode, o);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_ERROR);
                            }
                        });
                    }

                    @Override
                    public void clientError(final int statusCode, final Response<?> response) {
                        super.clientError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (statusCode == NetworkConstants.RESPONSE_CODE_FORBIDDEN_403 || statusCode == NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400) {
                                    try {
                                        Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(getContext()).
                                                responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                        ErrorResponse errorObject = errorConverter.convert(response.errorBody());

                                        if (errorObject.getMessage() != null) {
                                            SuperToast.makeText(getActivity(), errorObject.getMessage(),
                                                    SuperToast.Icon.Resource.ERROR,
                                                    SuperToast.Background.RED).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                            }
                        });
                    }

                }

        );

        if (callUpdatePwdResponse == null) {//没有网络 或者token
            btnUpdatePwd.setProgress(BUTTON_STATUS__CODE_ERROR);
        }

//        try {
//            callFeedbackResponse.cancel();
//        } catch (NullPointerException e) {}

    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        UpdateInfoActivity.mInstance.finishActivityAnimU2D();
                    } catch (Exception e) {
                    }
                    break;
                case 2:
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
        MobclickAgent.onPageEnd(UpdatePwdFragment.class.getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(UpdatePwdFragment.class.getSimpleName());
    }

}
