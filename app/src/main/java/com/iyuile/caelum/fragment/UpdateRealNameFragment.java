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

import com.google.gson.Gson;
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
 * @Description 修改真实姓名
 */
public class UpdateRealNameFragment extends BaseFragment implements OnClickListener {

    private static final int BUTTON_STATUS__CODE_SUCCESS = 100; // 成功
    private static final int BUTTON_STATUS__CODE_ERROR = -1; // 错误
    private static final int BUTTON_STATUS__CODE_DEFAULT = 0; // 默认
    private static final int BUTTON_STATUS__CODE_LOADING = 50; // 加载中

    ErrorHandlingCallAdapter.MyCall<Void> callUpdateRealNameResponse;

    private TextView tvClose;
    public CircularProgressButton btnUpdateRealName;
    private EditText ceRealName;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_realname, null);
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
        btnUpdateRealName = (CircularProgressButton) getActivity().findViewById(R.id.wb_update_real_name_btn_save);
        btnUpdateRealName.setIndeterminateProgressMode(true);//true=等待模式,false=进度条模式

        ceRealName = (EditText) getActivity().findViewById(R.id.wb_update_real_name);

        tvClose.setOnClickListener(this);
        btnUpdateRealName.setOnClickListener(this);

        tvClose.setTypeface(mApplication.getIconStyleFont());

        try {
            ceRealName.setText(mApplication.mUserObject.getRealname() != null ? mApplication.mUserObject.getRealname() : "");
        } catch (Exception e) {
            SuperToast.makeText(getActivity(), getString(R.string.unknown_code_3013),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            e.printStackTrace();
            UpdateInfoActivity.mInstance.finishActivityAnimU2D();
            return;
        }

    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;

        switch (v.getId()) {
            case R.id.tv_close:
                if (btnUpdateRealName.getProgress() == BUTTON_STATUS__CODE_LOADING)// 进行中让退出键失效
                    return;
                UpdateInfoActivity.mInstance.finishActivityAnimU2D();
                break;
            case R.id.wb_update_real_name_btn_save:
                updateRealName();
                break;
            default:
                break;
        }
    }


    /**
     * 修改昵称
     */
    private void updateRealName() {

        switch (btnUpdateRealName.getProgress()) {// 按钮状态
            case BUTTON_STATUS__CODE_SUCCESS:
            case BUTTON_STATUS__CODE_ERROR:
                btnUpdateRealName.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                return;
            case BUTTON_STATUS__CODE_DEFAULT:
                break;
            default:
                return;
        }

        btnUpdateRealName.setProgress(BUTTON_STATUS__CODE_LOADING);

        final String realName = ceRealName.getText().toString().trim();

        if (TextUtils.isEmpty(realName)) {
            SuperToast.makeText(getActivity(), getString(R.string.update_real_name_description),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnUpdateRealName.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (realName.length() > AppConstants.REAL_NAME_MAX) {
            SuperToast.makeText(getActivity(), getString(R.string.update_real_name_length),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnUpdateRealName.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        // 姓名一样,不修改,
        if (realName.equals(mApplication.mUserObject.getRealname())) {
            btnUpdateRealName.setProgress(BUTTON_STATUS__CODE_SUCCESS);
            handler.sendEmptyMessageDelayed(1, 1000);
            return;
        }

        callUpdateRealNameResponse = ApiServiceImpl.updateUserInfoToRealnameImpl(getActivity(), realName, new ResponseHandlerListener<Void>(getActivity()) {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void success(int statusCode, final Response<Void> response) {
                        super.success(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnUpdateRealName.setProgress(BUTTON_STATUS__CODE_SUCCESS);
                            }
                        });
                        handler.sendEmptyMessageDelayed(1, 1000);
                        try {
                            mApplication.mUserObject.setRealname(realName);
                            try {
                                mApplication.getSpUtil().setCurrentUsersInfo(new Gson().toJson(mApplication.mUserObject));
                            } catch (Exception e) {
                            }
                            MyFragment.mInstance.handler.sendEmptyMessage(1);
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void failure(int statusCode, Object o) {
                        super.failure(statusCode, o);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnUpdateRealName.setProgress(BUTTON_STATUS__CODE_ERROR);
                            }
                        });
                    }

                    @Override
                    public void clientError(final int statusCode, final Response<?> response) {
                        super.clientError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SuperToast.makeText(getContext(), getString(R.string.btn_failure),
                                        SuperToast.Icon.Resource.ERROR,
                                        SuperToast.Background.RED).show();
                            }
                        });

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
                                } else if (statusCode == NetworkConstants.RESPONSE_CODE_UNPROCESSABLE_ENTITY_422) {
                                    SuperToast.makeText(getActivity(), getString(R.string.update_nickname_exist),
                                            SuperToast.Icon.Resource.WARNING,
                                            SuperToast.Background.YELLOW).show();
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
                                btnUpdateRealName.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                            }
                        });
                    }

                }

        );

        if (callUpdateRealNameResponse == null) {//没有网络 或者token
            btnUpdateRealName.setProgress(BUTTON_STATUS__CODE_ERROR);
        }

//        try {
//            callUpdateSexResponse.cancel();
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
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(UpdateRealNameFragment.class.getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(UpdateRealNameFragment.class.getSimpleName());
    }

}
