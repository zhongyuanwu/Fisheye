package com.iyuile.caelum.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.adapter.OrderConfirmAdapter;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.AddressEntity;
import com.iyuile.caelum.entity.ShoppingCartEntity;
import com.iyuile.caelum.entity.response.AddressResponse;
import com.iyuile.caelum.utils.ButtonUtils;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.pulltorefreshanimlibrary.PullToRefreshBase;
import com.iyuile.pulltorefreshanimlibrary.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * 订单确认
 */
public class OrderConfirmActivity extends BaseBackSwipeActivity implements View.OnClickListener {

    public static OrderConfirmActivity mInstance;

    public static final int REQUEST_CODE = 0x00000001;

    public static final String INTENT_PARAM_STR_LIST = "intent_param_str_list";
    public static final String INTENT_PARAM_TOTAL_PRICE = "intent_param_total_price";

    private PullToRefreshListView listView;
    private ListView actualListView;
    private List<ShoppingCartEntity> listData = new ArrayList<ShoppingCartEntity>();
    private Long[] listDataCheck;
    private OrderConfirmAdapter mAdapter;

    //--
    private LinearLayout llReceiptAddressSelect, llReceiptAddressInfo;
    private TextView tvName, tvTelephone, tvReceiptAddressInfo;

    private TextView tvPrice;
    private Button btnImmediatelyOrder;

    private float totalPrice;

    private ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        try {
            String strList = bundle.getString(INTENT_PARAM_STR_LIST);
            if (strList == null || strList.equals("")) {
                finish();
                return;
            }
            strList = strList.substring(1, strList.length() - 1);
            String[] checkList = strList.split(", ");

            listDataCheck = new Long[checkList.length];
            for (int i = 0; i < checkList.length; i++)
                listDataCheck[i] = Long.parseLong(checkList[i]);

            List<ShoppingCartEntity> _listData = new ArrayList<ShoppingCartEntity>(ShoppingCartActivity.mInstance.listData);
            for (int i = 0, size = listDataCheck.length; i < size; i++) {
                for (int j = 0, sizeJ = _listData.size(); j < sizeJ; j++) {
                    if (listDataCheck[i].longValue() == _listData.get(j).getId().longValue()) {
                        listData.add(_listData.get(j));
                        _listData.remove(j);
                        break;
                    }
                }
            }
            _listData = null;
            totalPrice = bundle.getFloat(INTENT_PARAM_TOTAL_PRICE);
        } catch (Exception e) {
            finish();
            return;
        }
        mInstance = this;
        mApplication.mActivityManager.addActivity(this);
        setContentView(R.layout.activity_order_confirm);
        getTintManager().setStatusBarDarkMode(true, this);// 文字颜色,true:黑色false:白色
//        getTintManager().setStatusBarTintColor(ContextCompat.getColor(this, android.R.color.transparent));

        setAllIconTextViewsFont(this);
        initTopBarForLeftSecondaryIcon(getString(R.string.order_confirm), 0, 0, mApplication.getWoodBodyStyleFont());
        setStatusBar(this);
        initActionbar();

        initView();
        tvPrice.setText(getString(R.string.order_price_param, totalPrice));
        showAddressDefault();
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

    private void initView() {
        llReceiptAddressSelect = (LinearLayout) findViewById(R.id.ll_receipt_address_select);
        llReceiptAddressInfo = (LinearLayout) findViewById(R.id.ll_receipt_address_info);

        llReceiptAddressSelect.setOnClickListener(this);
        llReceiptAddressInfo.setOnClickListener(this);

        tvName = (TextView) findViewById(R.id.wb_tv_name);
        tvTelephone = (TextView) findViewById(R.id.wb_tv_telephone);
        tvReceiptAddressInfo = (TextView) findViewById(R.id.wb_tv_receipt_address_info);

        tvPrice = (TextView) findViewById(R.id.wb_tv_price);
        btnImmediatelyOrder = (Button) findViewById(R.id.wb_btn_immediately_order);
        btnImmediatelyOrder.setOnClickListener(this);
        // ---
        listView = (PullToRefreshListView) findViewById(R.id.list_view);
        actualListView = listView.getRefreshableView();
        listView.setMode(PullToRefreshBase.Mode.DISABLED);

        mAdapter = new OrderConfirmAdapter(this, listData);
        listView.setAdapter(mAdapter);

        //第一个参数就是我们的图片加载对象ImageLoader, 第二个是控制是否在滑动过程中暂停加载图片，如果需要暂停传true就行了，第三个参数控制猛的滑动界面的时候图片是否加载,第四个参数监听滑动事件
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));// 自动加载更多
    }

    private void setAddressData() {
        tvName.setText(getString(R.string.order_confirm_name, addressEntity.getName()));
        tvTelephone.setText(getString(R.string.order_confirm_telephone, addressEntity.getTelephone()));
        tvReceiptAddressInfo.setText(getString(R.string.order_confirm_receipt_address_info, addressEntity.getAddress()));
        llReceiptAddressSelect.setVisibility(View.GONE);
        llReceiptAddressInfo.setVisibility(View.VISIBLE);
    }

    private ErrorHandlingCallAdapter.MyCall<AddressResponse> callResponse;
    private AddressEntity addressEntity;

    private void showAddressDefault() {

        callResponse = ApiServiceImpl.showAddressDefaultImpl(this, new ResponseHandlerListener<AddressResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void success(final int statusCode, final Response<AddressResponse> response) {
                super.success(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            addressEntity = response.body().getData();
                            if (addressEntity != null)
                                setAddressData();
                        } catch (Exception e) {
                        }
                    }
                });
            }

            @Override
            public void clientError(final int statusCode, Response<?> response) {
                super.clientError(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //没有默认地址404
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

        if (callResponse == null) {//没有网络 或者token

        }

//        try {
//            callResponse.cancel();
//        } catch (NullPointerException e) { }
    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.wb_btn_immediately_order:
                createOrderApi();
                break;
            case R.id.ll_receipt_address_select:
            case R.id.ll_receipt_address_info:
                Intent intent = new Intent(this, ReceiptAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(ReceiptAddressActivity.INTENT_PARAM_RETURN_STATUS, true);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    private ErrorHandlingCallAdapter.MyCall<Void> callCreateOrderResponse;

    private boolean isStatus;

    private void createOrderApi() {
        if (isStatus) return;
//        isStatus = true;

        try {
            if (addressEntity == null) {
                SuperToast.makeText(OrderConfirmActivity.this, getResources().getString(R.string.shoppingcart_prompt_address_null),
                        SuperToast.Icon.Resource.INFO,
                        SuperToast.Background.BLUE).show();
                return;
            }
        } catch (Resources.NotFoundException e) {
        }

        if (listDataCheck.length == 0) {
            SuperToast.makeText(OrderConfirmActivity.this, getResources().getString(R.string.shoppingcart_prompt_check_null),
                    SuperToast.Icon.Resource.INFO,
                    SuperToast.Background.BLUE).show();
            finish();
            return;
        }

        callCreateOrderResponse = ApiServiceImpl.createOrderImpl(this, addressEntity.getId(), listDataCheck, new ResponseHandlerListener<Void>(this) {

            @Override
            public void onStart() {
                super.onStart();
                progress = ProgressDialog.show(OrderConfirmActivity.this, null, getString(R.string.shoppingcart_prompt_create_order_loading), false, true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                progress.setCanceledOnTouchOutside(false);
            }

            @Override
            public void success(final int statusCode, final Response<Void> response) {
                super.success(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(OrderConfirmActivity.this, getResources().getString(R.string.shoppingcart_prompt_create_order_complete),
                                SuperToast.Icon.Resource.YES,
                                SuperToast.Background.GREEN).show();

                        okhttp3.Headers headers = response.headers();
                        String uid = null;
                        if (headers.get("Location") != null)
                            uid = headers.get("Location");
                        if (uid != null) {
                            Intent intent = new Intent(OrderConfirmActivity.this, OrderBuyActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(OrderBuyActivity.INTENT_PARAM_UID, uid);
                            intent.putExtras(bundle);
                            startAnimActivity(intent);
                        }
                        try {
                            ShoppingCartActivity.mInstance.finish();
                        } catch (Exception e) {
                        }
                        finish();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (statusCode == NetworkConstants.RESPONSE_CODE_INTERNAL_SERVER_ERROR_500)
                            SuperToast.makeText(OrderConfirmActivity.this, getString(R.string.shoppingcart_prompt_create_order_failure),
                                    SuperToast.Icon.Resource.ERROR,
                                    SuperToast.Background.RED).show();
                    }
                });
            }

            @Override
            public void onFinish() {
                super.onFinish();
                isStatus = false;
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

        });

        if (callCreateOrderResponse == null) {//没有网络 或者token
            isStatus = false;
        }

//        try {
//            callCreateOrderResponse.cancel();
//        } catch (NullPointerException e) { }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                try {
                    addressEntity = (AddressEntity) data.getSerializableExtra(ReceiptAddressActivity.INTENT_PARAM_RETURN_ENTITY);
                    setAddressData();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        listData = null;
        listDataCheck = null;
        mInstance = null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(ShoppingCartActivity.class.getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(ShoppingCartActivity.class.getSimpleName());
        MobclickAgent.onResume(this);
    }

}