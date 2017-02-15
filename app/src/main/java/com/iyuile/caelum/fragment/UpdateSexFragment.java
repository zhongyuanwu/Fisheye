package com.iyuile.caelum.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.UpdateInfoActivity;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.response.ErrorResponse;
import com.iyuile.caelum.enums.SexValue;
import com.iyuile.caelum.util.ButtonUtils;
import com.iyuile.caelum.util.RetrofitUtils;
import com.iyuile.caelum.view.toast.SuperToast;
import com.umeng.analytics.MobclickAgent;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * @Description 修改性别
 */
public class UpdateSexFragment extends BaseFragment implements OnClickListener {

    private TextView tvClose;
    private RadioGroup rgSex;
    private RadioButton rbSex1, rbSex2, rbSex3;


    ErrorHandlingCallAdapter.MyCall<Void> callUpdateSexResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_sex, null);
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
        rgSex = (RadioGroup) getActivity().findViewById(R.id.rg_sex);
        rbSex1 = (RadioButton) getActivity().findViewById(R.id.wb_rb_sex_1);
        rbSex2 = (RadioButton) getActivity().findViewById(R.id.wb_rb_sex_2);
        rbSex3 = (RadioButton) getActivity().findViewById(R.id.wb_rb_sex_3);

        rbSex1.setOnClickListener(this);
        rbSex2.setOnClickListener(this);
        rbSex3.setOnClickListener(this);

        tvClose.setOnClickListener(this);

        tvClose.setTypeface(mApplication.getIconStyleFont());

        try {
            SexValue sex = SexValue.getActionType(mApplication.mUserObject.getSex());
            if (sex != null) {
                switch (sex) {
                    case MAN:
                        rbSex1.setChecked(true);
                        break;
                    case GIRL:
                        rbSex2.setChecked(true);
                        break;
                    case SECRECY:
                        rbSex3.setChecked(true);
                        break;
                }
            }
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
                UpdateInfoActivity.mInstance.finishActivityAnimU2D();
                break;
            case R.id.wb_rb_sex_1:
            case R.id.wb_rb_sex_2:
            case R.id.wb_rb_sex_3:
                int sex = Integer.valueOf(v.getTag() + "");
                updateSex(sex);
                break;
            default:
                break;
        }
    }

    /**
     * 修改性别
     */
    private void updateSex(final int sex) {

        rbSex1.setClickable(false);
        rbSex2.setClickable(false);
        rbSex3.setClickable(false);

        // 姓名一样,不修改,
        if (sex == mApplication.mUserObject.getSex()) {
            handler.sendEmptyMessageDelayed(1, 200);
            return;
        }

        callUpdateSexResponse = ApiServiceImpl.updateUserInfoToSexImpl(getActivity(), sex, new ResponseHandlerListener<Void>(getActivity()) {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void success(int statusCode, final Response<Void> response) {
                        super.success(statusCode, response);
                        handler.sendEmptyMessageDelayed(1, 200);
                        try {
                            mApplication.mUserObject.setSex(sex);
                            try {
                                mApplication.getSpUtil().setCurrentUsersInfo(new Gson().toJson(mApplication.mUserObject));
                            } catch (Exception e) {
                            }
                            MyFragment.mInstance.handler.sendEmptyMessage(1);
                        } catch (Exception e) {
                        }
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
                            }
                        });
                    }

                }

        );

        if (callUpdateSexResponse == null) {//没有网络 或者token

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
        MobclickAgent.onPageEnd(UpdateSexFragment.class.getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(UpdateSexFragment.class.getSimpleName());
    }

}
