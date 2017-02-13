package com.iyuile.caelum.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.adapter.ReceiptAddressAdapter;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.entity.AddressEntity;
import com.iyuile.caelum.entity.response.AddressesResponse;
import com.iyuile.caelum.utils.ButtonUtils;
import com.iyuile.caelum.view.ListView2EmptyView;
import com.iyuile.pulltorefreshanimlibrary.PullToRefreshBase;
import com.iyuile.pulltorefreshanimlibrary.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * 收货地址
 */
public class ReceiptAddressActivity extends BaseBackSwipeActivity implements View.OnClickListener {

    public static ReceiptAddressActivity mInstance;

    public static final int REQUEST_CODE = 0x00000001;

    public static final String INTENT_PARAM_RETURN_STATUS = "intent_param_return_status";
    public static final String INTENT_PARAM_RETURN_ENTITY = "intent_param_return_entity";

    private PullToRefreshListView listView;
    private ListView actualListView;
    private List<AddressEntity> listData = new ArrayList<AddressEntity>();
    private ReceiptAddressAdapter mAdapter;

    private ErrorHandlingCallAdapter.MyCall<AddressesResponse> callResponse;
    private AddressesResponse addressResponse;

    private ListView2EmptyView listEmptyView;

    //--
    private LinearLayout llReceiptAddressAdd;

    private boolean isClickReturn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        try {
            isClickReturn = bundle.getBoolean(INTENT_PARAM_RETURN_STATUS, false);
        } catch (Exception e) {
        }
        mInstance = this;
        mApplication.mActivityManager.addActivity(this);
        setContentView(R.layout.activity_receipt_address);
        getTintManager().setStatusBarDarkMode(true, this);// 文字颜色,true:黑色,false:白色
//        getTintManager().setStatusBarTintColor(ContextCompat.getColor(this, android.R.color.transparent));

        setAllIconTextViewsFont(this);
        initTopBarForLeftSecondaryIcon(getString(R.string.receipt_address), 0, 0, mApplication.getWoodBodyStyleFont());
        setStatusBar(this);
        initActionbar();

        initView();
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
        llReceiptAddressAdd = (LinearLayout) findViewById(R.id.ll_receipt_address_add);

        llReceiptAddressAdd.setOnClickListener(this);
        // -----
        listView = (PullToRefreshListView) findViewById(R.id.list_view);
        actualListView = listView.getRefreshableView();

        listEmptyView = new ListView2EmptyView(this, actualListView);

        mAdapter = new ReceiptAddressAdapter(this, listData);
        listView.setAdapter(mAdapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                loadingData();
            }
        });

        //第一个参数就是我们的图片加载对象ImageLoader, 第二个是控制是否在滑动过程中暂停加载图片，如果需要暂停传true就行了，第三个参数控制猛的滑动界面的时候图片是否加载,第四个参数监听滑动事件
//        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));// 自动加载更多

        loadingData();

        if (isClickReturn)
            actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AddressEntity addressEntity = listData.get(position - 1);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(INTENT_PARAM_RETURN_ENTITY, addressEntity);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
    }


    /**
     * 加载数据和刷新数据
     */
    private void loadingData() {

        callResponse = ApiServiceImpl.findAddressListImpl(this, new ResponseHandlerListener<AddressesResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
                listEmptyView.startEmptyViewAnim();
            }

            @Override
            public void success(int statusCode, final Response<AddressesResponse> response) {
                super.success(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            addressResponse = response.body();

                            if (addressResponse.getData() != null && addressResponse.getData().size() != 0) {
                                listData.clear();

//                                List<NewsEntity> listDatas = Arrays.asList(newsResponse.getData());
                                List<AddressEntity> listDatas = addressResponse.getData();
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
            case R.id.ll_receipt_address_add:
                startActivityForResult(new Intent(this, ReceiptAddressNewActivity.class), REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == this.REQUEST_CODE) {
                loadingData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mInstance = null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(ReceiptAddressActivity.class.getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(ReceiptAddressActivity.class.getSimpleName());
        MobclickAgent.onResume(this);
    }

}