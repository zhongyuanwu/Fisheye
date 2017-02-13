package com.iyuile.caelum.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.adapter.MallAdapter;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.entity.ItemEntity;
import com.iyuile.caelum.entity.response.ItemsResponse;
import com.iyuile.caelum.view.ListView2EmptyView;
import com.iyuile.caelum.view.ListView2FooterView;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.pulltorefreshanimlibrary.PullToRefreshBase;
import com.iyuile.pulltorefreshanimlibrary.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * 商城->分类->商品
 */
public class MallWareListActivity extends BaseBackSwipeActivity implements AbsListView.OnScrollListener {

    public static MallWareListActivity mInstance;

    public static final String INTENT_PARAM_ID = "intent_param_id";

    private PullToRefreshGridView listView;
    private GridView actualListView;
    private List<ItemEntity> listData = new ArrayList<ItemEntity>();
    private MallAdapter mAdapter;

    private ListView2EmptyView listEmptyView;
    private ListView2FooterView listFooterView;

    private ErrorHandlingCallAdapter.MyCall<ItemsResponse> callResponse;
    private ItemsResponse itemResponse;

    private long cid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        try {
            cid = bundle.getLong(INTENT_PARAM_ID);
            if (cid == 0) {
                finish();
                return;
            }
        } catch (Exception e) {
            finish();
            return;
        }
        mInstance = this;
        mApplication.mActivityManager.addActivity(this);
        setContentView(R.layout.activity_mall_ware_list);
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
        listView = (PullToRefreshGridView) findViewById(R.id.list_view);
        actualListView = listView.getRefreshableView();

        listEmptyView = new ListView2EmptyView(this, actualListView);
        listFooterView = (ListView2FooterView) findViewById(R.id.fv_footer);

        mAdapter = new MallAdapter(this, actualListView, listData);
        listView.setAdapter(mAdapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> pullToRefreshBase) {
                loadingData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> pullToRefreshBase) {
                int page = 1;
                if (itemResponse != null)
                    page = itemResponse.getMeta().getPagination().getCurrent_page() + 1;
                loadingData(page);
            }
        });

        //第一个参数就是我们的图片加载对象ImageLoader, 第二个是控制是否在滑动过程中暂停加载图片，如果需要暂停传true就行了，第三个参数控制猛的滑动界面的时候图片是否加载,第四个参数监听滑动事件
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true, this));// 自动加载更多

//        listView.setRefreshing(true);// 自动刷新数据自动执行{@link #onPullDownToRefresh(PullToRefreshBase)}

        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                // 到达最后提示
                if (itemResponse != null) {
                    if (itemResponse.getMeta().getPagination().getTotal() == mAdapter.getCount())

                        SuperToast.makeText(MallWareListActivity.this, getString(R.string.list_not_more_data),
                                SuperToast.Icon.Resource.INFO,
                                SuperToast.Background.BLUE).show();
                }
            }
        });

        loadingData(1);
    }

    /**
     * 滚动监听,设置自动加载更多
     *
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (itemResponse != null) {
            if (itemResponse.getMeta().getPagination().getTotal() == mAdapter.getCount())
                return;
        }

        int lastItem = firstVisibleItem + visibleItemCount;

        if (lastItem == mAdapter.getCount()) {
            if (itemResponse != null && !isListViewRefresh) {
                int page = itemResponse.getMeta().getPagination().getCurrent_page() + 1;
                loadingData(page);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private boolean isListViewRefresh;

    /**
     * 加载数据和刷新数据
     */
    private void loadingData(final int page) {
        isListViewRefresh = true;

        callResponse = ApiServiceImpl.findItemListImpl(this, page, cid, new ResponseHandlerListener<ItemsResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
                listEmptyView.startEmptyViewAnim();
                if (page != 1)
                    listFooterView.setVisibility(View.VISIBLE);
            }

            @Override
            public void success(int statusCode, final Response<ItemsResponse> response) {
                super.success(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            itemResponse = response.body();

                            if (itemResponse.getData() != null && itemResponse.getData().size() != 0) {

                                if (itemResponse.getMeta().getPagination().getCurrent_page() == 1)
                                    listData.clear();

//                                List<NewsEntity> listDatas = Arrays.asList(itemResponse.getData());
                                List<ItemEntity> listDatas = itemResponse.getData();
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
            public void serverError(final int statusCode, final Response<?> response) {
                super.serverError(statusCode, response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isListViewRefresh = false;
                        listView.onRefreshComplete();
                        listEmptyView.stopEmptyViewAnim();
                        listFooterView.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }

        });

//        try {
//            callResponse.cancel();
//        } catch (NullPointerException e) {}
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