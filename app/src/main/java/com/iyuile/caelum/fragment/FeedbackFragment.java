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
import com.iyuile.caelum.activity.FeedbackActivity;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.utils.AppInfo;
import com.iyuile.caelum.utils.ButtonUtils;
import com.iyuile.caelum.utils.ImageLoadOptions;
import com.iyuile.caelum.view.CircularImageView;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.circularprogressbutton.CircularProgressButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import retrofit2.Response;

/**
 * @Description 意见反馈
 */
public class FeedbackFragment extends BaseFragment implements OnClickListener {

    private static final int BUTTON_STATUS__CODE_SUCCESS = 100; // 成功
    private static final int BUTTON_STATUS__CODE_ERROR = -1; // 错误
    private static final int BUTTON_STATUS__CODE_DEFAULT = 0; // 默认
    private static final int BUTTON_STATUS__CODE_LOADING = 50; // 加载中

    ErrorHandlingCallAdapter.MyCall<Void> callFeedbackResponse;

    private TextView tvClose, tvNickname;
    private CircularImageView ivAvatar;
    private EditText etContent;
    public CircularProgressButton btnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback, null);
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
        ivAvatar = (CircularImageView) getActivity().findViewById(R.id.iv_avatar);
        tvNickname = (TextView) getActivity().findViewById(R.id.wb_tv_nickname);
        btnSubmit = (CircularProgressButton) getActivity().findViewById(R.id.wb_btn_submit);
        btnSubmit.setIndeterminateProgressMode(true);//true=等待模式,false=进度条模式

        etContent = (EditText) getActivity().findViewById(R.id.wb_et_content);

        tvClose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        tvClose.setTypeface(mApplication.getIconStyleFont());

        try {
            tvNickname.setText(mApplication.mUserObject.getNickname() != null ? mApplication.mUserObject.getNickname() : "");
        } catch (Exception e) {
            SuperToast.makeText(getActivity(), getString(R.string.unknown_code_3014),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            e.printStackTrace();
            FeedbackActivity.mInstance.finishActivityAnimU2D();
            return;
        }

        try {
            ImageLoader.getInstance().displayImage(mApplication.mUserObject.getAvatar() + AppConstants.IMAGE_URL_AVATAR_MIDDLE_THUMBNAIL_PARAMETER, ivAvatar, ImageLoadOptions.getOptionAvatar());
        } catch (Exception e) {
            SuperToast.makeText(getActivity(), getString(R.string.unknown_code_3015),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            e.printStackTrace();
            FeedbackActivity.mInstance.finishActivityAnimU2D();
            return;
        }

    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;

        switch (v.getId()) {
            case R.id.tv_close:
                FeedbackActivity.mInstance.finishActivityAnimU2D();
                break;
            case R.id.wb_btn_submit:
                feedbackApi();
                break;
            default:
                break;
        }
    }

    /**
     * 提交反馈
     */
    private void feedbackApi() {

        switch (btnSubmit.getProgress()) {// 按钮状态
            case BUTTON_STATUS__CODE_SUCCESS:
            case BUTTON_STATUS__CODE_ERROR:
                btnSubmit.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                return;
            case BUTTON_STATUS__CODE_DEFAULT:
                break;
            default:
                return;
        }

        btnSubmit.setProgress(BUTTON_STATUS__CODE_LOADING);

        final String content = etContent.getText().toString().trim();

        // null内容假提示
        if (TextUtils.isEmpty(content)) {
            SuperToast.makeText(getActivity(), getString(R.string.feedback_btn_text_complete),
                    SuperToast.Icon.Resource.YES,
                    SuperToast.Background.GREEN).show();
            btnSubmit.setProgress(BUTTON_STATUS__CODE_SUCCESS);
            handler.sendEmptyMessageDelayed(1, 1000);
            return;
        }

        if (content.length() > AppConstants.FEEDBACK_CONTENT_LENGTH_MAX) {
            SuperToast.makeText(getActivity(), getString(R.string.feedback_content_length_max),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnSubmit.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        String versionName = AppInfo.getVersionName(getActivity());
        int versionCode = AppInfo.getVersionCode(getActivity());
        String os = android.os.Build.VERSION.RELEASE;
        os = "android " + os;
        String model = android.os.Build.MODEL;

        callFeedbackResponse = ApiServiceImpl.addFeedbackImpl(getActivity(), content, versionName, versionCode + "", os, model, new ResponseHandlerListener<Void>(getActivity()) {

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
                                SuperToast.makeText(getActivity(), getString(R.string.feedback_btn_text_complete),
                                        SuperToast.Icon.Resource.YES,
                                        SuperToast.Background.GREEN).show();
                                btnSubmit.setProgress(BUTTON_STATUS__CODE_SUCCESS);
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
                                btnSubmit.setProgress(BUTTON_STATUS__CODE_ERROR);
                            }
                        });
                    }

                    @Override
                    public void clientError(final int statusCode, final Response<?> response) {
                        super.clientError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SuperToast.makeText(getContext(), getString(R.string.feedback_btn_text_failure),
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
                                SuperToast.makeText(getContext(), getString(R.string.feedback_btn_text_failure),
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnSubmit.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                            }
                        });
                    }

                }

        );

        if (callFeedbackResponse == null) {//没有网络 或者token
            btnSubmit.setProgress(BUTTON_STATUS__CODE_ERROR);
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
                        FeedbackActivity.mInstance.finishActivityAnimU2D();
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
        MobclickAgent.onPageEnd(FeedbackFragment.class.getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(FeedbackFragment.class.getSimpleName());
    }

}
