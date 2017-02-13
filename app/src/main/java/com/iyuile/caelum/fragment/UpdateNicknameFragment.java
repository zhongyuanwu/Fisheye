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
import com.iyuile.caelum.utils.ButtonUtils;
import com.iyuile.caelum.utils.RetrofitUtils;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.circularprogressbutton.CircularProgressButton;
import com.umeng.analytics.MobclickAgent;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * @Description 修改昵称
 */
public class UpdateNicknameFragment extends BaseFragment implements OnClickListener {

    private static final int BUTTON_STATUS__CODE_SUCCESS = 100; // 成功
    private static final int BUTTON_STATUS__CODE_ERROR = -1; // 错误
    private static final int BUTTON_STATUS__CODE_DEFAULT = 0; // 默认
    private static final int BUTTON_STATUS__CODE_LOADING = 50; // 加载中

    ErrorHandlingCallAdapter.MyCall<Void> callUpdateNicknameResponse;

    private TextView tvClose;
    public CircularProgressButton btnSave;
    private EditText ceNickname;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_nickname, null);
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
        btnSave = (CircularProgressButton) getActivity().findViewById(R.id.wb_update_nickname_btn_save);
        btnSave.setIndeterminateProgressMode(true);//true=等待模式,false=进度条模式

        ceNickname = (EditText) getActivity().findViewById(R.id.wb_update_nickname);

        tvClose.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        tvClose.setTypeface(mApplication.getIconStyleFont());

        try {
            ceNickname.setText(mApplication.mUserObject.getNickname() != null ? mApplication.mUserObject.getNickname() : "");
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
                if (btnSave.getProgress() == BUTTON_STATUS__CODE_LOADING)// 进行中让退出键失效
                    return;
                UpdateInfoActivity.mInstance.finishActivityAnimU2D();
                break;
            case R.id.wb_update_nickname_btn_save:
                updateNickname();
                break;
            default:
                break;
        }
    }

    /**
     * 修改昵称
     */
    private void updateNickname() {

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

        final String nickname = ceNickname.getText().toString().trim();

        if (TextUtils.isEmpty(nickname)) {
            SuperToast.makeText(getActivity(), getString(R.string.update_nickname_description),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSave.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (!nickname.matches(AppConstants.NICKNAME_REGEX)) {
            SuperToast.makeText(getActivity(), getString(R.string.update_nickname_rule),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSave.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        // 昵称一样,不修改,
        if (nickname.equals(mApplication.mUserObject.getNickname())) {
            btnSave.setProgress(BUTTON_STATUS__CODE_SUCCESS);
            handler.sendEmptyMessageDelayed(1, 1000);
            return;
        }

        callUpdateNicknameResponse = ApiServiceImpl.updateUserInfoToNicknameImpl(getActivity(), nickname, new ResponseHandlerListener<Void>(getActivity()) {

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
                                btnSave.setProgress(BUTTON_STATUS__CODE_SUCCESS);
                            }
                        });
                        handler.sendEmptyMessageDelayed(1, 1000);
                        try {
                            mApplication.mUserObject.setNickname(nickname);
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
                                btnSave.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                            }
                        });
                    }

                }

        );

        if (callUpdateNicknameResponse == null) {//没有网络 或者token
            btnSave.setProgress(BUTTON_STATUS__CODE_ERROR);
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
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(UpdateNicknameFragment.class.getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(UpdateNicknameFragment.class.getSimpleName());
    }

}
