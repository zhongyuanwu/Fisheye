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
import com.iyuile.caelum.util.ButtonUtils;
import com.iyuile.caelum.util.VerifyUtil;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.circularprogressbutton.CircularProgressButton;
import com.umeng.analytics.MobclickAgent;

/**
 * @author WangYao
 * @version 1
 * @Description 修改手机号
 * @ProjectName Apus
 * @ClassName {@link UpdatePhoneFragment}
 * @Date 2016-3-30 下午6:24:41
 */
public class UpdatePhoneFragment extends BaseFragment implements OnClickListener {

    private static final int BUTTON_STATUS__CODE_SUCCESS = 100; // 成功
    private static final int BUTTON_STATUS__CODE_ERROR = -1; // 错误
    private static final int BUTTON_STATUS__CODE_DEFAULT = 0; // 默认
    private static final int BUTTON_STATUS__CODE_LOADING = 50; // 加载中

    private TextView tvClose;
    public CircularProgressButton btnNextStep;
    private EditText cePhoneOld;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_phone, null);
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
        btnNextStep = (CircularProgressButton) getActivity().findViewById(R.id.wb_update_btn_next_step);
        btnNextStep.setIndeterminateProgressMode(true);//true=等待模式,false=进度条模式

        cePhoneOld = (EditText) getActivity().findViewById(R.id.wb_update_phone);

        tvClose.setOnClickListener(this);
        btnNextStep.setOnClickListener(this);

        tvClose.setTypeface(mApplication.getIconStyleFont());


        if (UpdateInfoActivity.mInstance.getPhoneOld() != null)
            cePhoneOld.setText(UpdateInfoActivity.mInstance.getPhoneOld());
        // TODO :::test
//		cePhoneOld.setText("13366664009");
    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;

        switch (v.getId()) {
            case R.id.tv_close:
                if (btnNextStep.getProgress() == BUTTON_STATUS__CODE_LOADING)// 进行中让退出键失效
                    return;
                UpdateInfoActivity.mInstance.finishActivityAnimU2D();
                break;
            case R.id.wb_update_btn_next_step:
                validatePhoneOld();
                break;
            default:
                break;
        }
    }

    /**
     * 验证旧的手机号
     */
    private void validatePhoneOld() {

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

        String phoneOld = cePhoneOld.getText().toString().trim();

        if (TextUtils.isEmpty(phoneOld)) {
            SuperToast.makeText(getActivity(), getString(R.string.business_card_holder_2_create_telephone_prompt),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        if (!VerifyUtil.isMobileNO(phoneOld)) {
            SuperToast.makeText(getActivity(), getString(R.string.sign_in_phone_falseness),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        try {
            String phone = mApplication.mUserObject.getTelephone();

            if (!phoneOld.equals(phone)) {
                SuperToast.makeText(getActivity(), getString(R.string.fragment_sgin_ce_phoneold_falseness),
                        SuperToast.Icon.Resource.ERROR,
                        SuperToast.Background.RED).show();
                btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
                return;
            }
        } catch (Exception e1) {
            SuperToast.makeText(getActivity(), getString(R.string.unknown_code_3011),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            e1.printStackTrace();
            btnNextStep.setProgress(BUTTON_STATUS__CODE_ERROR);
            return;
        }

        btnNextStep.setProgress(BUTTON_STATUS__CODE_SUCCESS);

        UpdateInfoActivity.mInstance.setPhoneOld(phoneOld);

        handler.sendEmptyMessageDelayed(1, 1000);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    UpdateInfoActivity.mInstance.setTabSelection(5, true);
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
        MobclickAgent.onPageEnd(UpdatePhoneFragment.class.getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(UpdatePhoneFragment.class.getSimpleName());
    }

}
