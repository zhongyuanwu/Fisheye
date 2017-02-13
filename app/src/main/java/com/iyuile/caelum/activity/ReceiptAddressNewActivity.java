package com.iyuile.caelum.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.AddressEntity;
import com.iyuile.caelum.model.ChineseCitiesModel;
import com.iyuile.caelum.utils.ChineseCitiesUtil;
import com.iyuile.caelum.utils.KeyBoardUtils;
import com.iyuile.caelum.utils.VerifyUtil;
import com.iyuile.caelum.view.HeaderLayout;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.pickerlibrary.OptionsPopupWindow;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * 收货地址->新建
 */
public class ReceiptAddressNewActivity extends BaseBackSwipeActivity {

    public static ReceiptAddressNewActivity mInstance;

    public static final String INTENT_PARAM_ENTITY = "intent_param_entity";

    private LinearLayout llCity;
    private EditText etName, etTelephone, etCity, etAddress;
    private RadioButton btnDefaultAddress;

    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();

    private OptionsPopupWindow pwOptions;

    private ProgressDialog progress;

    private AddressEntity addressEntity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        try {
            addressEntity = (AddressEntity) bundle.getSerializable(INTENT_PARAM_ENTITY);
        } catch (Exception e) {
        }
        mInstance = this;
        mApplication.mActivityManager.addActivity(this);
        setContentView(R.layout.activity_receipt_address_new);
        getTintManager().setStatusBarDarkMode(true, this);// 文字颜色,true:黑色,false:白色
//        getTintManager().setStatusBarTintColor(ContextCompat.getColor(this, android.R.color.transparent));

        setAllIconTextViewsFont(this);
        initTopBarForBothSecondaryTxt(getString(R.string.receipt_address_new),
                R.string.head_layout_left_return_icon, R.string.head_layout_left_return_secondary, mApplication.getIconStyleFont(), mApplication.getWoodBodyStyleFont(),
                new OnLeftButtonClickListener(),
                R.string.receipt_address_new_actionbar_right_txt, 0, mApplication.getWoodBodyStyleFont(), null,
                new HeaderLayout.onRightImageButtonClickListener() {
                    @Override
                    public void onClick() {
                        saveVerify();
                    }
                }
        );
        setStatusBar(this);
        initActionbar();

        initView();
        initData();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initActionbar() {
        mHeaderLayout.setTitleTypeface(mApplication.getWoodBodyStyleFont());
        final View tvLabel = findViewById(R.id.tv_label);
        tvLabel.setBackground(ContextCompat.getDrawable(this, R.drawable.actionbar_label_blue_bg));
        final TextView tvTitle = mHeaderLayout.getTitleCenter();
        ViewTreeObserver vto = tvTitle.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                tvTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvLabel.getLayoutParams();
                layoutParams.width = tvTitle.getWidth();
                tvLabel.setLayoutParams(layoutParams);
            }
        });
    }

    private boolean isCheck;

    private void initView() {
        etName = (EditText) findViewById(R.id.wb_et_name);
        etTelephone = (EditText) findViewById(R.id.wb_et_telephone);
        llCity = (LinearLayout) findViewById(R.id.ll_city);
        etCity = (EditText) findViewById(R.id.wb_et_city);
        etAddress = (EditText) findViewById(R.id.wb_et_address);
        btnDefaultAddress = (RadioButton) findViewById(R.id.wb_btn_default_address);
        btnDefaultAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheck) {
                    isCheck = true;
                } else {
                    btnDefaultAddress.setChecked(false);
                    isCheck = false;
                }
            }
        });

        etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(((EditText) v), ReceiptAddressNewActivity.this);
                try {
                    if (pwOptions != null)
                        pwOptions.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        initOptionsData();
        initOptions();
    }

    private void initData() {
        if (addressEntity == null)
            return;
        btnDefaultAddress.setVisibility(View.GONE);
        llCity.setVisibility(View.GONE);
        etName.setText(addressEntity.getName());
        etTelephone.setText(addressEntity.getTelephone());
        etAddress.setText(addressEntity.getAddress());
    }

    /**
     * 屏幕半黑色
     *
     * @param alpha 开打0.5f 和关闭1.0
     */
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = alpha;
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.getWindow().setAttributes(lp);
    }

    private void initOptions() {
        try {
            //选项选择器
            pwOptions = new OptionsPopupWindow(this);

            //三级联动效果
            pwOptions.setPicker(options1Items, options2Items, options3Items, true);
            //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
            //设置默认选中的三级项目
            pwOptions.setSelectOptions(0, 0, 0);
            //监听确定选择按钮
            pwOptions.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {

                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    //返回的分别是三个级别的选中位置
                    String tx = options1Items.get(options1) + "," + options2Items.get(options1).get(option2) + "," + options3Items.get(options1).get(option2).get(options3);
                    etCity.setText(tx.replace(" ", ""));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initOptionsData() {
        try {
            ArrayList<ChineseCitiesModel> chineseCitiesModels = ChineseCitiesModel.toJSONArrayChineseCitiesModelFromData(ChineseCitiesUtil.getChineseCitiesJSON(this));
            for (ChineseCitiesModel chineseCitiesModel : chineseCitiesModels) {
                ArrayList<ChineseCitiesModel.CityModel> cityModels = chineseCitiesModel.getCity();
                ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
                ArrayList<String> options2Items_01 = new ArrayList<String>();
                for (ChineseCitiesModel.CityModel cityModel : cityModels) {
                    //选项3
                    options3Items_01.add(cityModel.getArea());
                    //选项2
                    String item2 = cityModel.getName();
                    options2Items_01.add(item2);
                }
                options3Items.add(options3Items_01);
                options2Items.add(options2Items_01);
                //选项1
                String item1 = chineseCitiesModel.getName();
                options1Items.add(item1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String paramName, paramTelephone, paramCity, paramAddress;

    /**
     * 验证
     */
    private void saveVerify() {
        paramName = etName.getText().toString().trim();

        if (TextUtils.isEmpty(paramName)) {
            SuperToast.makeText(this, getString(R.string.receipt_address_new_name_prompt_empty),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            return;
        }

        if (paramName.length() > AppConstants.RECEIPT_ADDRESS_NEW_NAME_LENGTH_MAX) {
            SuperToast.makeText(this, getString(R.string.receipt_address_new_name_prompt_length_max, AppConstants.RECEIPT_ADDRESS_NEW_NAME_LENGTH_MAX),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            return;
        }

        paramTelephone = etTelephone.getText().toString().trim();

        if (TextUtils.isEmpty(paramTelephone)) {
            SuperToast.makeText(this, getString(R.string.receipt_address_new_telephone_prompt_empty),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            return;
        }

        if (!VerifyUtil.isMobileNO(paramTelephone)) {
            SuperToast.makeText(this, getString(R.string.sign_in_phone_falseness),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            return;
        }

        if (addressEntity == null) {
            paramCity = etCity.getText().toString().trim();

            if (TextUtils.isEmpty(paramCity)) {
                SuperToast.makeText(this, getString(R.string.receipt_address_new_city_prompt_empty),
                        SuperToast.Icon.Resource.WARNING,
                        SuperToast.Background.YELLOW).show();
                return;
            }
        }

        paramAddress = etAddress.getText().toString().trim();

        if (paramAddress.length() < AppConstants.RECEIPT_ADDRESS_NEW_ADDRESS_LENGTH_MIN) {
            SuperToast.makeText(this, getString(R.string.receipt_address_new_address_prompt_empty),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            return;
        }

        if (paramAddress.length() > AppConstants.RECEIPT_ADDRESS_NEW_ADDRESS_LENGTH_MAX) {
            SuperToast.makeText(this, getString(R.string.receipt_address_new_address_prompt_length_max, AppConstants.RECEIPT_ADDRESS_NEW_ADDRESS_LENGTH_MAX),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            return;
        }

        if (addressEntity == null)
            addApi();
        else
            updateApi();
    }

    ErrorHandlingCallAdapter.MyCall<Void> callResponse;

    /**
     * 添加
     */
    private void addApi() {

        String address = paramCity + " " + paramAddress;

        callResponse = ApiServiceImpl.addAddressImpl(this, address, paramName, paramTelephone, null, null, isCheck, responseHandlerListener);

        if (callResponse == null) {//没有网络 或者token

        }

//        try {
//            callResponse.cancel();
//        } catch (NullPointerException e) {}

    }

    /**
     * 修改
     */
    private void updateApi() {

        if (addressEntity.getName().equals(paramName) && addressEntity.getTelephone().equals(paramTelephone) && addressEntity.getAddress().equals(paramAddress)) {
            SuperToast.makeText(ReceiptAddressNewActivity.this, getString(R.string.btn_complete),
                    SuperToast.Icon.Resource.YES,
                    SuperToast.Background.GREEN).show();
            finish();
            return;
        }

        callResponse = ApiServiceImpl.updateAddressImpl(this, addressEntity.getId(), paramAddress, paramName, paramTelephone, null, null, responseHandlerListener);

        if (callResponse == null) {//没有网络 或者token

        }

//        try {
//            callResponse.cancel();
//        } catch (NullPointerException e) {}

    }

    ResponseHandlerListener responseHandlerListener = new ResponseHandlerListener<Void>(this) {

        @Override
        public void onStart() {
            super.onStart();
            progress = ProgressDialog.show(ReceiptAddressNewActivity.this, null, getString(addressEntity != null ? R.string.receipt_address_new_update_loading : R.string.receipt_address_new_send_loading), false, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    setResult(RESULT_OK);
                    finish();
                }
            });
            progress.setCanceledOnTouchOutside(false);
        }

        @Override
        public void success(int statusCode, final Response<Void> response) {
            super.success(statusCode, response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SuperToast.makeText(ReceiptAddressNewActivity.this, getString(R.string.btn_complete),
                            SuperToast.Icon.Resource.YES,
                            SuperToast.Background.GREEN).show();
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }

        @Override
        public void failure(final int statusCode, Object o) {
            super.failure(statusCode, o);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SuperToast.makeText(ReceiptAddressNewActivity.this, getString(R.string.btn_failure),
                            SuperToast.Icon.Resource.ERROR,
                            SuperToast.Background.RED).show();
                }
            });
        }

        @Override
        public void clientError(final int statusCode, Response<?> response) {
            super.clientError(statusCode, response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (statusCode == NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400)
                        SuperToast.makeText(ReceiptAddressNewActivity.this, getString(addressEntity != null ? R.string.receipt_address_new_update_prompt_failure : R.string.receipt_address_new_prompt_failure),
                                SuperToast.Icon.Resource.ERROR,
                                SuperToast.Background.RED).show();
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progress != null)
                        progress.dismiss();
                }
            });
        }

        @Override
        public void onCancel() {
            super.onCancel();
        }

    };

    @Override
    protected void onDestroy() {
        pwOptions = null;
        options1Items = null;
        options2Items = null;
        options3Items = null;
        mInstance = null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(ReceiptAddressNewActivity.class.getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(ReceiptAddressNewActivity.class.getSimpleName());
        MobclickAgent.onResume(this);
    }

}