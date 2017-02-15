package com.iyuile.caelum.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.adapter.MallClassifyAdapter;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.entity.ItemCatalogEntity;
import com.iyuile.caelum.entity.response.ItemCatalogsResponse;
import com.iyuile.caelum.util.ButtonUtils;
import com.iyuile.caelum.view.ListView2EmptyView;
import com.iyuile.pulltorefreshanimlibrary.PullToRefreshBase;
import com.iyuile.pulltorefreshanimlibrary.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * 商城->分类
 * TODO :::not page(不提示没有更多数据)
 * listView.setMode(PullToRefreshBase.Mode.DISABLED);
 */
public class MallClassifyActivity extends BaseBackSwipeActivity implements View.OnClickListener {

    public static MallClassifyActivity mInstance;

    private PullToRefreshListView listView;
    private ListView actualListView;
    private List<ItemCatalogEntity> listData = new ArrayList<ItemCatalogEntity>();
    private MallClassifyAdapter mAdapter;

    private ErrorHandlingCallAdapter.MyCall<ItemCatalogsResponse> callResponse;
    private ItemCatalogsResponse itemCatalogResponse;

    private ListView2EmptyView listEmptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        mApplication.mActivityManager.addActivity(this);
        setContentView(R.layout.activity_mall_classify);
        getTintManager().setStatusBarDarkMode(true, this);// 文字颜色,true:黑色,false:白色
//        getTintManager().setStatusBarTintColor(ContextCompat.getColor(this, android.R.color.transparent));

        initTopBarForLeftSecondaryIcon(getString(R.string.mall_classify), 0, 0, mApplication.getWoodBodyStyleFont());
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
        listView = (PullToRefreshListView) findViewById(R.id.list_view);
        actualListView = listView.getRefreshableView();
        listView.setMode(PullToRefreshBase.Mode.DISABLED);

        listEmptyView = new ListView2EmptyView(this, actualListView);

        mAdapter = new MallClassifyAdapter(this, listData);
        listView.setAdapter(mAdapter);

        //第一个参数就是我们的图片加载对象ImageLoader, 第二个是控制是否在滑动过程中暂停加载图片，如果需要暂停传true就行了，第三个参数控制猛的滑动界面的时候图片是否加载,第四个参数监听滑动事件
//        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));// 自动加载更多

        loadingData();
    }

    /**
     * 加载数据和刷新数据
     */
    private void loadingData() {

        callResponse = ApiServiceImpl.findItemCatalogListImpl(this, new ResponseHandlerListener<ItemCatalogsResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
                listEmptyView.startEmptyViewAnim();
            }

            @Override
            public void success(int statusCode, final Response<ItemCatalogsResponse> response) {
                super.success(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            itemCatalogResponse = response.body();

                            if (itemCatalogResponse.getData() != null && itemCatalogResponse.getData().size() != 0) {
                                listData.clear();

//                                List<NewsEntity> listDatas = Arrays.asList(newsResponse.getData());
                                List<ItemCatalogEntity> listDatas = itemCatalogResponse.getData();
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
    }

    @Override
    protected void onDestroy() {
        mInstance = null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(MallClassifyActivity.class.getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(MallClassifyActivity.class.getSimpleName());
        MobclickAgent.onResume(this);
    }

}