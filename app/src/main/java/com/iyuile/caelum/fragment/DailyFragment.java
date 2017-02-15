package com.iyuile.caelum.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.VideoDetailActivity;
import com.iyuile.caelum.adapter.DailyAdapter;
import com.iyuile.caelum.model.HomePicEntity;
import com.iyuile.caelum.network.HttpAdress;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Response;
import retrofit2.Call;


/**
 * Created by k21 on 2017/1/16.
 */

public class DailyFragment extends SupportFragment {
    private static final String TAG = "TestActivity";
    @Bind(R.id.lv_home)
    RecyclerView lvHome;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;


    private View mView;
    private String nextUrl;
    private DailyAdapter mDailyAdapter;
    private Call<HomePicEntity> everyDayDatas;
    private boolean mIsRefreshing = false;
    private boolean isRun;
    List<HomePicEntity.IssueListEntity.ItemListEntity> listAll = new ArrayList<>();

    public static DailyFragment newInstance() {
        Bundle args = new Bundle();

        DailyFragment fragment = new DailyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_daily, container, false);
        ButterKnife.bind(this, mView);
        initView();
        setListener();
        return mView;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        showProgressBar(HttpAdress.DAILY);
    }

    private void initView() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);


        mDailyAdapter = new DailyAdapter(this, _mActivity, listAll);
        lvHome.setLayoutManager(new LinearLayoutManager(_mActivity));
        lvHome.setAdapter(mDailyAdapter);
    }

    private void showProgressBar(final String url) {
        mRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mRefreshLayout.setRefreshing(true);
                mIsRefreshing = true;
                downLoad(HttpAdress.DAILY);
//                everyDayDatas = RetrofitHelper.getEveryDayService().getEveryDayDatas();
//                everyDayDatas.enqueue(new Callback<HomePicEntity>() {
//                    @Override
//                    public void onResponse(Call<HomePicEntity> call, Response<HomePicEntity> response) {
//                        handleData(response.body());
//                    }
//
//                    @Override
//                    public void onFailure(Call<HomePicEntity> call, Throwable t) {
//                        isRun = false;
//                        mRefreshLayout.setRefreshing(false);
//                    }
//                });
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                downLoad(HttpAdress.DAILY);

//                //多次请求 call只被调用一次 可以利用Call接口中提供的clone()方法实现多次请求。
//                everyDayDatas.clone().enqueue(new Callback<HomePicEntity>() {
//                    @Override
//                    public void onResponse(Call<HomePicEntity> call, Response<HomePicEntity> response) {
//                        handleData(response.body());
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<HomePicEntity> call, Throwable t) {
//                        isRun = false;
//                        mRefreshLayout.setRefreshing(false);
//                    }
//                });
            }
        });
    }

//    private void downMoreLoad(String url) {
//        RetrofitHelper.getEveryDayMoreService().getEveryDayMoreDatas(url).enqueue(new Callback<HomePicEntity>() {
//            @Override
//            public void onResponse(Call<HomePicEntity> call, Response<HomePicEntity> response) {
//                handleData(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<HomePicEntity> call, Throwable t) {
//
//            }
//        });
//    }

    private void setListener() {
        //单个的点击事件
        mDailyAdapter.setOnItemClickListener(new DailyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getContext(), VideoDetailActivity.class);
                Bundle bundle = new Bundle();
                Log.i("--->", position + "");
                HomePicEntity.IssueListEntity.ItemListEntity.DataEntity data = listAll.get(position).getData();
                if (!"video".equals(listAll.get(position).getType())) {
                    return;
                }
                bundle.putString("title", data.getTitle());
                //获取到时间
                int duration = data.getDuration();
                int mm = duration / 60;//分
                int ss = duration % 60;//秒
                String second = "";//秒
                String minute = "";//分
                if (ss < 10) {
                    second = "0" + String.valueOf(ss);
                } else {
                    second = String.valueOf(ss);
                }
                if (mm < 10) {
                    minute = "0" + String.valueOf(mm);
                } else {
                    minute = String.valueOf(mm);//分钟
                }
                bundle.putString("time", "#" + data.getCategory() + " / " + minute + "'" + second + '"');
                bundle.putString("desc", data.getDescription());//视频描述
                bundle.putString("blurred", data.getCover().getBlurred());//模糊图片地址
                bundle.putString("feed", data.getCover().getFeed());//图片地址
                bundle.putString("video", data.getPlayUrl());//视频播放地址
                bundle.putInt("collect", data.getConsumption().getCollectionCount());//收藏量
                bundle.putInt("share", data.getConsumption().getShareCount());//分享量
                bundle.putInt("reply", data.getConsumption().getReplyCount());//回复数量
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        lvHome.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，
                    if (lastVisibleItem == (totalItemCount - 1)) {
                        //加载更多功能的代码
                        if (!isRun) {
                            isRun = true;
                            if (nextUrl != null) {
                                downLoad(nextUrl);
                            }
                        }
                    }
                }
            }
        });

    }

//    /**
//     * 处理数据
//     *
//     * @param homePicEntity
//     */
//    private void handleData(HomePicEntity homePicEntity) {
//        List<HomePicEntity.IssueListEntity> issueList = homePicEntity.getIssueList();
//        HomePicEntity.IssueListEntity issueListEntity = issueList.get(0);
//        List<HomePicEntity.IssueListEntity.ItemListEntity> itemList = issueListEntity.getItemList();
//        HomePicEntity.IssueListEntity issueListEntity2 = issueList.get(1);
//        List<HomePicEntity.IssueListEntity.ItemListEntity> itemList1 = issueListEntity2.getItemList();
//        Log.e("===>" + TAG, "===完成===>" + homePicEntity.getNextPageUrl());
//
//        isRun = false;
//
//        //刷新需要清除数据
//        if (mIsRefreshing) {
//            listAll.removeAll(listAll);
//            mRefreshLayout.setRefreshing(false);
//            mIsRefreshing = false;
//        }
//
//        listAll.addAll(itemList);
//        listAll.addAll(itemList1);
//
//        nextUrl = homePicEntity.getNextPageUrl();
//        myNotify();
//    }

    private void downLoad(final String url) {

        OkGo
                .get(url)
                .tag(this)
                .cacheKey("cacheky")
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, okhttp3.Call call, Response response) {
                        Gson mGson = new Gson();
                        HomePicEntity homePicEntity = mGson.fromJson(s, HomePicEntity.class);
                        List<HomePicEntity.IssueListEntity> issueList = homePicEntity.getIssueList();
                        HomePicEntity.IssueListEntity issueListEntity = issueList.get(0);
                        List<HomePicEntity.IssueListEntity.ItemListEntity> itemList = issueListEntity.getItemList();
                        HomePicEntity.IssueListEntity issueListEntity2 = issueList.get(1);
                        List<HomePicEntity.IssueListEntity.ItemListEntity> itemList1 = issueListEntity2.getItemList();

                        isRun = false;

                        //刷新需要清除数据
                        if (mIsRefreshing) {
                            listAll.removeAll(listAll);
                            mRefreshLayout.setRefreshing(false);
                            mIsRefreshing = false;
                        }

                        listAll.addAll(itemList);
                        listAll.addAll(itemList1);

                        nextUrl = homePicEntity.getNextPageUrl();
                        Log.e("===>" + TAG, "===完成===>" + nextUrl);
                        myNotify();
                    }

                    @Override
                    public void onError(okhttp3.Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        isRun = false;
                        mRefreshLayout.setRefreshing(false);
                    }
                });

    }

    public void myNotify() {
        if (mDailyAdapter != null)
            mDailyAdapter.setData(listAll);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        OkGo.getInstance().cancelTag(this);
    }
}
