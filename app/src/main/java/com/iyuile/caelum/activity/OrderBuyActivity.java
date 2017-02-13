package com.iyuile.caelum.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.adapter.OrderBuyAdapter;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.entity.OrderEntity;
import com.iyuile.caelum.entity.OrderItemEntity;
import com.iyuile.caelum.entity.response.OrderPayResponse;
import com.iyuile.caelum.entity.response.OrderResponse;
import com.iyuile.caelum.enums.OrderStatusValue;
import com.iyuile.caelum.enums.PayType;
import com.iyuile.caelum.utils.ButtonUtils;
import com.iyuile.caelum.view.WarpHeightListView;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.circularprogressbutton.CircularProgressButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * 订单支付
 */
public class OrderBuyActivity extends BaseBackSwipeActivity implements View.OnClickListener {

    public static OrderBuyActivity mInstance;

    public static final String INTENT_PARAM_UID = "intent_param_uid";
    public static final String INTENT_PARAM_ENTITY = "intent_param_entity";

    private static final int BUTTON_STATUS__CODE_SUCCESS = 100; // 成功
    private static final int BUTTON_STATUS__CODE_ERROR = -1; // 错误
    private static final int BUTTON_STATUS__CODE_DEFAULT = 0; // 默认
    private static final int BUTTON_STATUS__CODE_LOADING = 50; // 加载中

    private WarpHeightListView listView;
    private List<OrderItemEntity> listData = new ArrayList<OrderItemEntity>();
    private OrderBuyAdapter mAdapter;

    private String uid;

    public ErrorHandlingCallAdapter.MyCall<OrderPayResponse> callOrderPayResponse;
    public boolean isRequestStatus, isCreateOrder;

    private boolean isPaySuccess;//支付成功

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        try {
            orderEntity = (OrderEntity) bundle.getSerializable(INTENT_PARAM_ENTITY);
            if (orderEntity == null) {
                uid = bundle.getString(INTENT_PARAM_UID);
                if (uid == null || uid.equals("")) {
                    finish();
                    return;
                }
            } else
                uid = orderEntity.getUid();
        } catch (Exception e) {
            finish();
            return;
        }
        mInstance = this;
        mApplication.mActivityManager.addActivity(this);
        setContentView(R.layout.activity_order_buy);
        getTintManager().setStatusBarDarkMode(true, this);// 文字颜色,true:黑色,false:白色
//        getTintManager().setStatusBarTintColor(ContextCompat.getColor(this, android.R.color.transparent));

        initTopBarForLeftSecondaryIcon(getString(R.string.order_buy), 0, 0, mApplication.getWoodBodyStyleFont());
        setStatusBar(this);
        initActionbar();

        initView();
        setData();
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

    //--
    private LinearLayout llPay;
    private TextView tvOrderPrice, tvOrderNumber, tvOrderTime, tvOrderAddressInfo;
    private TextView tvOrderPrompt;
    private TextView tvCount;
    private TextView tvPrice, tvCarriage, tvPayablePrice;
    private RadioGroup rgPayMode;
    private RadioButton rbWeChat;
    private CircularProgressButton btnBuy;


    private void initView() {
        llPay = (LinearLayout) findViewById(R.id.ll_pay);
        tvOrderPrice = (TextView) findViewById(R.id.wb_tv_order_price);
        tvOrderNumber = (TextView) findViewById(R.id.wb_tv_order_number);
        tvOrderTime = (TextView) findViewById(R.id.wb_tv_order_time);
        tvOrderAddressInfo = (TextView) findViewById(R.id.wb_tv_order_address_info);
        tvOrderPrompt = (TextView) findViewById(R.id.wb_tv_order_prompt);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvOrderPrompt.setText(Html.fromHtml(getText(R.string.order_buy_order_prompt_html).toString(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvOrderPrompt.setText(Html.fromHtml(getText(R.string.order_buy_order_prompt_html).toString()));
        }

        tvCount = (TextView) findViewById(R.id.wb_tv_inventory_title_subhead_2);

        tvPrice = (TextView) findViewById(R.id.wb_tv_inventory_title_2_subhead);
        tvCarriage = (TextView) findViewById(R.id.wb_tv_carriage_title_subhead);
        tvPayablePrice = (TextView) findViewById(R.id.wb_tv_payable_price_title_subhead);

        rgPayMode = (RadioGroup) findViewById(R.id.rg_pay_mode);
        rbWeChat = (RadioButton) findViewById(R.id.wb_btn_pay_wechat);

        btnBuy = (CircularProgressButton) findViewById(R.id.wb_btn_buy);
        btnBuy.setIndeterminateProgressMode(true);//true=等待模式,false=进度条模式
        btnBuy.setOnClickListener(this);

        // ---
        listView = (WarpHeightListView) findViewById(R.id.list_view);

        mAdapter = new OrderBuyAdapter(this, listData);
        listView.setAdapter(mAdapter);

        //第一个参数就是我们的图片加载对象ImageLoader, 第二个是控制是否在滑动过程中暂停加载图片，如果需要暂停传true就行了，第三个参数控制猛的滑动界面的时候图片是否加载,第四个参数监听滑动事件
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));// 自动加载更多

        loadingData();
    }

    private void setData() {
        try {
            if (orderEntity != null) {
                OrderStatusValue orderStatusValue = OrderStatusValue.getActionType(orderEntity.getStatus());
                if (orderStatusValue != null) {
                    switch (orderStatusValue) {
                        case UNPAID:
                            llPay.setVisibility(View.VISIBLE);
                            break;
                        default:
                            llPay.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    llPay.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
        }

        try {
            float total = orderEntity.getTotal();
            if (total != Float.parseFloat(String.valueOf(tvOrderPrice.getTag()))) {
                tvOrderPrice.setText(getString(R.string.order_pay_price_param, total));
                tvOrderPrice.setTag(total);
            }
        } catch (Exception e) {
            tvOrderPrice.setText(getString(R.string.order_pay_price_param, orderEntity != null ? orderEntity.getTotal() : 0f));
            tvOrderPrice.setTag(0f);
        }
        try {
            String uuid = orderEntity.getUid();
            if (!uuid.equals(tvOrderNumber.getTag())) {
                tvOrderNumber.setText(getString(R.string.order_pay_number_param, uuid));
                tvOrderNumber.setTag(uuid);
            }
        } catch (Exception e) {
            tvOrderNumber.setText(getString(R.string.order_pay_number_param, orderEntity != null ? orderEntity.getUid() : ""));
            tvOrderNumber.setTag("");
        }
        try {
            String time = orderEntity.getCreatedAt();
            if (!time.equals(tvOrderNumber.getTag())) {
                tvOrderTime.setText(getString(R.string.order_pay_time_param, time));
                tvOrderTime.setTag(time);
            }
        } catch (Exception e) {
            tvOrderTime.setText(getString(R.string.order_pay_time_param, orderEntity != null ? orderEntity.getCreatedAt() : ""));
            tvOrderTime.setTag("");
        }
        try {
            String address = orderEntity.getAddress();
            if (!address.equals(tvOrderNumber.getTag())) {
                tvOrderAddressInfo.setText(getString(R.string.order_pay_address_info_param, address));
                tvOrderAddressInfo.setTag(address);
            }
        } catch (Exception e) {
            tvOrderAddressInfo.setText(getString(R.string.order_pay_address_info_param, orderEntity != null ? orderEntity.getAddress() : ""));
            tvOrderAddressInfo.setTag("");
        }
        try {
            int count = orderEntity.getItems().getData().size();
            if (count != Integer.parseInt(String.valueOf(tvCount.getTag()))) {
                tvCount.setText(count + "");
                tvCount.setTag(count);
            }
        } catch (Exception e) {
            try {
                tvCount.setText(orderEntity != null ? orderEntity.getItems().getData().size() + "" : "0");
                tvCount.setTag(0);
            } catch (Exception e1) {
                tvCount.setTag(0);
            }
        }
        try {
            float total = orderEntity.getTotal();
            if (total != Float.parseFloat(String.valueOf(tvPrice.getTag()))) {
                tvPrice.setText(getString(R.string.order_pay_price_param, total));
                tvPrice.setTag(total);
            }
        } catch (Exception e) {
            tvPrice.setText(getString(R.string.order_pay_price_param, orderEntity != null ? orderEntity.getTotal() : 0f));
            tvPrice.setTag(0f);
        }
        try {
            float total = orderEntity.getTotal();
            if (total != Float.parseFloat(String.valueOf(tvPayablePrice.getTag()))) {
                tvPayablePrice.setText(getString(R.string.order_pay_price_param, total));
                tvPayablePrice.setTag(total);
            }
        } catch (Exception e) {
            tvPayablePrice.setText(getString(R.string.order_pay_price_param, orderEntity != null ? orderEntity.getTotal() : 0f));
            tvPayablePrice.setTag(0f);
        }
        try {
            listData = orderEntity.getItems().getData();
            if (listData.size() != mAdapter.getCount() && listData.size() != 0) {
                mAdapter.setList(listData);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.wb_btn_buy:
                payApi();
                break;
        }
    }

    private void payApi() {

        if (isRequestStatus) return;
        isRequestStatus = true;

        switch (btnBuy.getProgress()) {// 按钮状态
            case BUTTON_STATUS__CODE_SUCCESS:
            case BUTTON_STATUS__CODE_ERROR:
                btnBuy.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                setViewEnabled(true);
                isRequestStatus = false;

                return;
            case BUTTON_STATUS__CODE_DEFAULT:
                break;
            default:
                return;
        }

        btnBuy.setProgress(BUTTON_STATUS__CODE_LOADING);

        boolean isVerify = orderEntity.getTotal() != 0f ? true : false;

        callOrderPayResponse = ApiServiceImpl.orderPayImpl(this, orderEntity.getUid(), isVerify, PayType.WeChat, new ApiServiceImpl.OnPayStatusListener() {
            @Override
            public void onStart() {
                SuperToast.makeText(OrderBuyActivity.this, getString(R.string.create_order_prompt),
                        SuperToast.Icon.Resource.INFO,
                        SuperToast.Background.BLUE).show();
                setViewEnabled(false);
                isCreateOrder = true;
            }

            @Override
            public void success(int statusCode, Response<OrderPayResponse> response) {
                setViewEnabled(true);
                try {
                    btnBuy.setProgress(BUTTON_STATUS__CODE_SUCCESS);
                } catch (Exception e) {
                }
                handler.sendEmptyMessageDelayed(1, 1000);
            }

            @Override
            public void failure(int statusCode, Object o) {
                try {
                    btnBuy.setProgress(BUTTON_STATUS__CODE_ERROR);
                } catch (Exception e1) {
                }
                setViewEnabled(true);
                isCreateOrder = false;
            }

            @Override
            public void clientError(int statusCode, Response<?> response) {
            }

            @Override
            public void serverError(int statusCode, Response<?> response) {
            }

            @Override
            public void networkError(IOException e) {
            }

            @Override
            public void unexpectedError(Throwable t) {
            }

            @Override
            public void unauthenticated(int statusCode, Response<?> response) {
            }

            @Override
            public void onFinish() {
                isRequestStatus = false;
            }

            @Override
            public void onCancel() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(OrderBuyActivity.this, getString(R.string.create_order_prompt_cancel),
                                SuperToast.Icon.Resource.WARNING,
                                SuperToast.Background.YELLOW).show();
                        try {
                            btnBuy.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                        } catch (Exception e) {
                        }
                    }
                });
            }

            @Override
            public void onFailure() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            btnBuy.setProgress(BUTTON_STATUS__CODE_ERROR);
                        } catch (Exception e1) {
                        }
                        setViewEnabled(true);
                        isCreateOrder = false;
                    }
                });
            }
        });

        if (callOrderPayResponse == null) {//没有网络 或者token
            btnBuy.setProgress(BUTTON_STATUS__CODE_ERROR);
            isRequestStatus = false;
        }
//        try {
//            callOrderPayResponse.cancel();
//        } catch (NullPointerException e) { }

        setOnPayResultListener(new OnPayResultListener() {
            @Override
            public void onFinish(int resultCode) {
                isUserReturn = true;
                setViewEnabled(true);
                switch (resultCode) {
                    case 0://成功
                        SuperToast.makeText(OrderBuyActivity.this, getString(R.string.dialog_order_pay_btn_complete),
                                SuperToast.Icon.Resource.YES,
                                SuperToast.Background.GREEN).show();
                        try {
                            btnBuy.setProgress(BUTTON_STATUS__CODE_SUCCESS);
                        } catch (Exception e) {
                        }
                        setViewEnabled(true);
                        handler.sendEmptyMessageDelayed(1, 1000);
                        break;
                    case -1://错误
                        SuperToast.makeText(OrderBuyActivity.this, getString(R.string.dialog_order_pay_btn_error),
                                SuperToast.Icon.Resource.ERROR,
                                SuperToast.Background.RED).show();
                        try {
                            btnBuy.setProgress(BUTTON_STATUS__CODE_ERROR);
                        } catch (Exception e) {
                        }
                        setViewEnabled(true);
                        isCreateOrder = false;
                        break;
                    case -2://用户取消
                        SuperToast.makeText(OrderBuyActivity.this, getString(R.string.dialog_order_pay_btn_cancel),
                                SuperToast.Icon.Resource.WARNING,
                                SuperToast.Background.YELLOW).show();
                        try {
                            btnBuy.setProgress(BUTTON_STATUS__CODE_DEFAULT);
                        } catch (Exception e) {
                        }
                        setViewEnabled(true);
                        isCreateOrder = false;
                        break;
                }
            }
        });
    }

    private void setViewEnabled(boolean enabled) {
        rbWeChat.setEnabled(enabled);
    }

    private boolean isUserReturn = true;

    public interface OnPayResultListener {
        void onFinish(int resultCode);
    }

    public OnPayResultListener onPayResultListener;

    public void setOnPayResultListener(OnPayResultListener onPayResultListener) {
        this.onPayResultListener = onPayResultListener;
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {//支付完成,(关闭或者更新页面)
                        Animation.AnimationListener al = new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                llPay.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        };
                        collapse(llPay, al);
                    } catch (Exception e) {
                    }
                    isPaySuccess = true;
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    private void collapse(final View view, Animation.AnimationListener al) {
        final int originHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1.0f) {
                    view.getLayoutParams().height = 0;
                    view.requestLayout();
                } else {
                    view.getLayoutParams().height = originHeight - (int) (originHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        if (al != null) {
            animation.setAnimationListener(al);
        }
        animation.setDuration(500);
        view.startAnimation(animation);
    }

    private ErrorHandlingCallAdapter.MyCall<OrderResponse> callResponse;
    private OrderEntity orderEntity;

    private void loadingData() {
        callResponse = ApiServiceImpl.showOrderImpl(this, uid, new ResponseHandlerListener<OrderResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void success(final int statusCode, final Response<OrderResponse> response) {
                super.success(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            orderEntity = response.body().getData();
                            if (orderEntity != null) {
                                setData();
                            } else {
                                SuperToast.makeText(OrderBuyActivity.this, getString(R.string.order_confirm_get_order_failure),
                                        SuperToast.Icon.Resource.ERROR,
                                        SuperToast.Background.RED).show();
                                finish();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void clientError(final int statusCode, Response<?> response) {
                super.clientError(statusCode, response);
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

        if (callResponse == null) {//没有网络 或者token

        }

//        try {
//            callResponse.cancel();
//        } catch (NullPointerException e) { }
    }

    @Override
    protected void onDestroy() {
        mInstance = null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        isUserReturn = false;
        super.onPause();
        MobclickAgent.onPageEnd(ShoppingCartActivity.class.getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isUserReturn && isCreateOrder) {
            SuperToast.makeText(this, getString(R.string.dialog_order_pay_btn_cancel),
                    SuperToast.Icon.Resource.WARNING,
                    SuperToast.Background.YELLOW).show();
            try {
                btnBuy.setProgress(BUTTON_STATUS__CODE_DEFAULT);
            } catch (Exception e) {
            }
            try {
                callOrderPayResponse.cancel();
            } catch (NullPointerException e) {
            }
            isCreateOrder = false;
        }
        MobclickAgent.onPageStart(ShoppingCartActivity.class.getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    public void onBackPressed() {
        if (isPaySuccess)
            setResult(RESULT_OK);
        finish();
    }
}