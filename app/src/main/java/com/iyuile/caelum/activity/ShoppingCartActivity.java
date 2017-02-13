package com.iyuile.caelum.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.adapter.ShoppingCartAdapter;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.entity.ShoppingCartEntity;
import com.iyuile.caelum.entity.response.ShoppingCartsResponse;
import com.iyuile.caelum.utils.ButtonUtils;
import com.iyuile.caelum.view.ListView2EmptyView;
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
 * 购物车
 * TODO :::not page(不提示没有更多数据)
 * listView.setMode(PullToRefreshBase.Mode.DISABLED);
 */
public class ShoppingCartActivity extends BaseBackSwipeActivity implements View.OnClickListener {

    public static ShoppingCartActivity mInstance;

    private PullToRefreshListView listView;
    private ListView actualListView;
    public List<ShoppingCartEntity> listData = new ArrayList<ShoppingCartEntity>();
    private ShoppingCartAdapter mAdapter;

    private ErrorHandlingCallAdapter.MyCall<ShoppingCartsResponse> callResponse;
    private ShoppingCartsResponse shoppingCartResponse;

    private ListView2EmptyView listEmptyView;

    //--
    private RadioButton btnCheckAll;
    private TextView tvPrice;
    private Button btnImmediatelyOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        mApplication.mActivityManager.addActivity(this);
        setContentView(R.layout.activity_shopping_cart);
        getTintManager().setStatusBarDarkMode(true, this);// 文字颜色,true:黑色,false:白色
//        getTintManager().setStatusBarTintColor(ContextCompat.getColor(this, android.R.color.transparent));

        initTopBarForLeftSecondaryIcon(getString(R.string.shopping_cart_my), 0, 0, mApplication.getWoodBodyStyleFont());
        initActionbar();

        initView();

        initData(getString(R.string.order_price_param, 0.00));
    }

    private void initData(String string) {
        tvPrice.setText(string);
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

    private boolean isCheckAll;

    private void initView() {
        btnCheckAll = (RadioButton) findViewById(R.id.wb_btn_check_all);
        tvPrice = (TextView) findViewById(R.id.wb_tv_price);
        btnImmediatelyOrder = (Button) findViewById(R.id.wb_btn_settle_accounts);

        btnImmediatelyOrder.setOnClickListener(this);
        btnCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheckAll) {
                    isCheckAll = true;
                    totalPrice = 0f;
                } else {
                    btnCheckAll.setChecked(false);
                    isCheckAll = false;
                    totalPrice = 0f;
                    initData(getString(R.string.order_price_param, 0.00));
                }
                checkAllList(isCheckAll);
            }
        });

        // ---
        listView = (PullToRefreshListView) findViewById(R.id.list_view);
        actualListView = listView.getRefreshableView();
        listView.setMode(PullToRefreshBase.Mode.DISABLED);

        listEmptyView = new ListView2EmptyView(this, actualListView);

        mAdapter = new ShoppingCartAdapter(this, listData);
        listView.setAdapter(mAdapter);

        //第一个参数就是我们的图片加载对象ImageLoader, 第二个是控制是否在滑动过程中暂停加载图片，如果需要暂停传true就行了，第三个参数控制猛的滑动界面的时候图片是否加载,第四个参数监听滑动事件
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));// 自动加载更多

        loadingData();
    }

    /**
     * 全选/反选
     *
     * @param isCheck
     */
    public void checkAllList(boolean isCheck) {
        for (ShoppingCartEntity shoppingCartEntity : listData) {
            shoppingCartEntity.setCheck(isCheck);
            if (isCheck) {
                try {
                    Message msg = new Message();
                    msg.what = ShoppingCartActivity.RESPONSE_CODE_INIT;
                    msg.arg1 = shoppingCartEntity.getCount();
                    msg.arg2 = 1;
                    msg.obj = shoppingCartEntity.getModel().getData().getPrice();
                    ShoppingCartActivity.mInstance.handler.sendMessage(msg);
                } catch (Exception e1) {
                }
            }
        }
        mAdapter.setList(listData);
    }

    /**
     * 获取选中数据
     *
     * @return
     */
    public List<Long> findCheckItem() {
        List<Long> checkItemList = new ArrayList<Long>();
        for (ShoppingCartEntity shoppingCartEntity : listData) {
            if (shoppingCartEntity.isCheck())
                checkItemList.add(shoppingCartEntity.getId());
        }
        return checkItemList;
    }

    /**
     * 加载数据和刷新数据
     */
    private void loadingData() {

        callResponse = ApiServiceImpl.findShoppingCartListImpl(this, new ResponseHandlerListener<ShoppingCartsResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
                listEmptyView.startEmptyViewAnim();
            }

            @Override
            public void success(int statusCode, final Response<ShoppingCartsResponse> response) {
                super.success(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            shoppingCartResponse = response.body();

                            if (shoppingCartResponse.getData() != null && shoppingCartResponse.getData().size() != 0) {
                                listData.clear();

//                                List<NewsEntity> listDatas = Arrays.asList(newsResponse.getData());
                                List<ShoppingCartEntity> listDatas = shoppingCartResponse.getData();
                                listData.addAll(listDatas);

                                mAdapter.setList(listData);
                            } else {
                                listData.clear();
                                mAdapter.setList(listData);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // listView.setMode(blogResponse.getMeta().getPagination().getCurrent_page() == blogResponse.getMeta().getPagination().getTotal_pages()?Mode.PULL_FROM_START:Mode.BOTH);

                            // if(usersResponse.getMeta().getPagination().getCurrent_page() == 1)
                            //	ToastUtils.makeText(AtUserActivity.this,getResources().getString(R.string.list_refresh_success_data),ToastUtils.MESSAGE_TYPE_SUCCESS);
                        }
                    }
                });
            }

            @Override
            public void clientError(int statusCode, Response<?> response) {
                super.clientError(statusCode, response);
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
                        listView.onRefreshComplete();
                        listEmptyView.stopEmptyViewAnim();
                    }
                });
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }

        });

        if (callResponse == null) {//没有网络 或者token
            listView.post(new Runnable() {
                @Override
                public void run() {
                    listView.onRefreshComplete();// 在异步里调用才起作用
                    listEmptyView.stopEmptyViewAnim();
                }
            });
        }

//        try {
//            callResponse.cancel();
//        } catch (NullPointerException e) {}
    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.wb_btn_settle_accounts:
                if (findCheckItem().size() == 0) {
                    SuperToast.makeText(ShoppingCartActivity.this, getResources().getString(R.string.shoppingcart_prompt_check_null),
                            SuperToast.Icon.Resource.INFO,
                            SuperToast.Background.BLUE).show();
                    return;
                }

                Intent intent = new Intent(this, OrderConfirmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(OrderConfirmActivity.INTENT_PARAM_STR_LIST, findCheckItem().toString());
                bundle.putFloat(OrderConfirmActivity.INTENT_PARAM_TOTAL_PRICE, totalPrice);
                intent.putExtras(bundle);
                startAnimActivity(intent);
                break;
        }
    }

    public final static int RESPONSE_CODE_UPDATE = 0x00002011;
    public final static int RESPONSE_CODE_INIT = 0x00002012;

    /**
     * 更新页面上的信息
     */
    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESPONSE_CODE_UPDATE:
                    updateInfo((float) msg.obj, msg.arg1, msg.arg2);
                    break;
                case RESPONSE_CODE_INIT:
                    initInfo((float) msg.obj, msg.arg1, msg.arg2);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private float totalPrice;

    /**
     * 更新信息
     */
    private void updateInfo(float price, int currentCount, int type) {
        totalPrice = type == 1 ? totalPrice + (currentCount * price) - ((currentCount - 1) * price) : totalPrice - (currentCount * price) + ((currentCount - 1) * price);
        totalPrice = totalPrice >= 0 ? totalPrice : 0;
        initData(getString(R.string.order_price_param, totalPrice));
    }

    /**
     * 初始化信息
     */
    private void initInfo(float price, int currentCount, int type) {
        totalPrice = type == 1 ? totalPrice + (currentCount * price) : totalPrice - (currentCount * price);
        totalPrice = totalPrice >= 0 ? totalPrice : 0;
        initData(getString(R.string.order_price_param, totalPrice));
        if (type == 2 && isCheckAll && findCheckItem().size() != listData.size()) {
            btnCheckAll.setChecked(false);
            isCheckAll = false;
        } else if (type == 1 && !isCheckAll && findCheckItem().size() == listData.size()) {
            btnCheckAll.setChecked(true);
            isCheckAll = true;
        }
    }

    @Override
    protected void onDestroy() {
        listData = null;
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