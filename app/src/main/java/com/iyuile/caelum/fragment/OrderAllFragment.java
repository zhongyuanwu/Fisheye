package com.iyuile.caelum.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.iyuile.caelum.R;
import com.iyuile.caelum.adapter.MyItemDecoration;
import com.iyuile.caelum.adapter.OrderAllAdapter;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.entity.OrderEntity;
import com.iyuile.caelum.entity.response.OrdersResponse;
import com.iyuile.caelum.enums.OrderStatusValue;
import com.iyuile.caelum.util.Log;
import com.iyuile.caelum.view.EmptyRecyclerView;
import com.iyuile.caelum.view.ListView2EmptyLayout;
import com.iyuile.caelum.view.ListView2FooterView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * 订单->全部
 *
 * @author WangYao
 */
public class OrderAllFragment extends BaseLazyFragment implements AbsListView.OnScrollListener {
    @Bind(R.id.order_refresh)
    SwipeRefreshLayout orderRefresh;
    @Bind(R.id.order_recycler)
    EmptyRecyclerView orderRecycler;
    @Bind(R.id.empty_view)
    LinearLayout mEmptyView;

    //    private PullToRefreshListView listView;
//    private ListView actualListView;
    private List<OrderEntity> listData = new ArrayList<OrderEntity>();
    private OrderAllAdapter mAdapter;

    private ErrorHandlingCallAdapter.MyCall<OrdersResponse> callResponse;
    private OrdersResponse ordersResponse;

    private ListView2EmptyLayout listEmptyView;
    private ListView2FooterView listFooterView;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared, isRequestData;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_order_all, null);
            ButterKnife.bind(this, rootView);
            initView();
            Log.e(":::全部", "初始化完毕");
            isPrepared = true;// 初始化完成改为true
            lazyLoad();
        }
        return rootView;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isRequestData)
            return;
        Log.e(":::全部", "加载数据");
        orderRefresh.post(new Runnable() {
            @Override
            public void run() {
                orderRefresh.setRefreshing(true);
                loadingData(1);
            }
        });

        orderRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingData(1);
            }
        });
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        Log.e(":::全部", "隐藏,不可见");
    }

    private void initView() {
        orderRefresh.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new OrderAllAdapter(_mActivity, listData);
        orderRecycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        orderRecycler.addItemDecoration(new MyItemDecoration());
        orderRecycler.setEmptyView(mEmptyView);
        orderRecycler.setAdapter(mAdapter);
//        listView = (PullToRefreshListView) rootView.findViewById(R.id.list_view_order_all);
//        actualListView = listView.getRefreshableView();
//
//        listEmptyView = new ListView2EmptyLayout(getActivity(), actualListView);
//        listFooterView = new ListView2FooterView(getActivity());
//        listEmptyView.setShowErrorButton(true);
//        listEmptyView.setErrorButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadingData(1);
//            }
//        });
//        listEmptyView.setShowEmptyButton(true);
//        listEmptyView.setEmptyButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadingData(1);
//            }
//        });
//
//        mAdapter = new OrderAdapter(getActivity(), listData);
//        listView.setAdapter(mAdapter);
//
//        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
//                loadingData(1);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
//                int page = 1;
//                if (ordersResponse != null)
//                    page = ordersResponse.getMeta().getPagination().getCurrent_page() + 1;
//                loadingData(page);
//            }
//        });

//        第一个参数就是我们的图片加载对象ImageLoader, 第二个是控制是否在滑动过程中暂停加载图片，如果需要暂停传true就行了，第三个参数控制猛的滑动界面的时候图片是否加载,第四个参数监听滑动事件
//        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true, this));// 自动加载更多

//        懒加载关闭自动加载
//        listView.setRefreshing(true);// 自动刷新数据自动执行{@link #onPullDownToRefresh(PullToRefreshBase)}
//
//        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
//
//            @Override
//            public void onLastItemVisible() {
//                // 到达最后提示
//                if (ordersResponse != null) {
//                    if (ordersResponse.getMeta().getPagination().getTotal() == mAdapter.getCount())
//
//                        SuperToast.makeText(getActivity(), getString(R.string.list_not_more_data),
//                                SuperToast.Icon.Resource.INFO,
//                                SuperToast.Background.BLUE).show();
//                }
//            }
//        });
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
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {

        if (ordersResponse != null) {
            if (ordersResponse.getMeta().getPagination().getTotal() == mAdapter.getItemCount())
                return;
        }

        int lastItem = firstVisibleItem + visibleItemCount;

        if (lastItem == mAdapter.getItemCount()) {
            if (ordersResponse != null && !isListViewRefresh) {
                int page = ordersResponse.getMeta().getPagination().getCurrent_page() + 1;
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

        callResponse = ApiServiceImpl.findOrderListImpl(getActivity(), page, OrderStatusValue.ALL, new ResponseHandlerListener<OrdersResponse>(getActivity()) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void success(int statusCode, final Response<OrdersResponse> response) {
                super.success(statusCode, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ordersResponse = response.body();

                            if (ordersResponse.getData() != null && ordersResponse.getData().size() != 0) {

                                if (ordersResponse.getMeta().getPagination().getCurrent_page() == 1)
                                    listData.clear();

//                                List<NewsEntity> listDatas = Arrays.asList(newsResponse.getData());
                                List<OrderEntity> listDatas = ordersResponse.getData();
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

                            isRequestData = true;// 数据请求成功后改为true
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
                        orderRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }

        });

        if (callResponse == null) {//没有网络 或者token
            orderRefresh.setRefreshing(false);
        }

//        try {
//            callResponse.cancel();
//        } catch (NullPointerException e) {}
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Main_Order_All");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("Main_Order_All");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
