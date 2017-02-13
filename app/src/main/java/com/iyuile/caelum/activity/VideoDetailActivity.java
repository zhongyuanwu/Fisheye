package com.iyuile.caelum.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iyuile.caelum.R;
import com.iyuile.caelum.adapter.VideoShopAdapter;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.entity.ItemEntity;
import com.iyuile.caelum.entity.response.ItemsResponse;
import com.iyuile.caelum.utils.NetConnectedUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;


/**
 * 视频详情activity
 */
public class VideoDetailActivity extends AppCompatActivity {

    @Bind(R.id.video_toolbar_iv_right)
    ImageButton videoToolbarIvRight;
    @Bind(R.id.video_toolbar)
    Toolbar videoToolbar;
    @Bind(R.id.video_detail_iv)
    SimpleDraweeView videoDetailIv;
    @Bind(R.id.video_paly)
    ImageView videoPaly;
    @Bind(R.id.video_detail_ivmo)
    SimpleDraweeView videoDetailIvmo;
    @Bind(R.id.video_detail_title)
    TextView videoDetailTitle;
    @Bind(R.id.video_detail_time)
    TextView videoDetailTime;
    @Bind(R.id.video_detail_desc)
    TextView videoDetailDesc;
    @Bind(R.id.video_detail_iv_fav)
    ImageView videoDetailIvFav;
    @Bind(R.id.video_detail_tv_fav)
    TextView videoDetailTvFav;
    @Bind(R.id.video_detail_iv_share)
    ImageView videoDetailIvShare;
    @Bind(R.id.video_detail_tv_share)
    TextView videoDetailTvShare;
    @Bind(R.id.video_detail_iv_reply)
    ImageView videoDetailIvReply;
    @Bind(R.id.video_detail_tv_reply)
    TextView videoDetailTvReply;
    @Bind(R.id.video_detail_iv_down)
    ImageView videoDetailIvDown;
    @Bind(R.id.video_detail_tv_down)
    TextView videoDetailTvDown;
    @Bind(R.id.video_shop_recycler)
    RecyclerView videoShopRecycler;

    private ErrorHandlingCallAdapter.MyCall<ItemsResponse> callResponse;
    private ItemsResponse itemResponse;
    private VideoShopAdapter mAdapter;
    private List<ItemEntity> listData = new ArrayList<ItemEntity>();
    private String video;
    private String title;
    //    @Bind(R.id.video_detail_viewpager)
//    ViewPager videoDetailViewpager;
    //保存Fragemnt集合
//    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        ButterKnife.bind(this);
        initData();
        loadingData(1);
    }

    //初始化数据
    private void initData() {
        String feed = getIntent().getStringExtra("feed");//背景图片
        title = getIntent().getStringExtra("title");
        String time = getIntent().getStringExtra("time");//时间
        String desc = getIntent().getStringExtra("desc");//视频详情
        String blurred = getIntent().getStringExtra("blurred");//模糊图片
        video = getIntent().getStringExtra("video");//视频播放地址
        int collect = getIntent().getIntExtra("collect", 0);//收藏量
        int share = getIntent().getIntExtra("share", 0);//分享量
        int reply = getIntent().getIntExtra("reply", 0);//回复量
        //给控件设置数据
        videoDetailIv.setImageURI(Uri.parse(feed));
        videoDetailTitle.setText(title);
        videoDetailTime.setText(time);
        videoDetailDesc.setText(desc);
        videoDetailIvmo.setImageURI(Uri.parse(blurred));
        videoDetailTvFav.setText(String.valueOf(collect));
        videoDetailTvShare.setText(String.valueOf(share));
        videoDetailTvReply.setText(String.valueOf(reply));

        mAdapter = new VideoShopAdapter(this, listData,videoShopRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videoShopRecycler.setLayoutManager(layoutManager);
        videoShopRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new VideoShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ItemEntity itemEntity = listData.get(position);
                Intent intent = new Intent(VideoDetailActivity.this, MallWareDetailedActivity.class);
                Bundle bundle = new Bundle();
                try {
                    bundle.putString(MallWareDetailedActivity.INTENT_PARAM_TITLE, itemEntity.getName());
                } catch (Exception e) {
                }
                bundle.putSerializable(MallWareDetailedActivity.INTENT_PARAM_ENTITY, itemEntity);
                bundle.putBoolean(MallWareDetailedActivity.INTENT_PARAM_PROGRESSBAR, false);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    //点击事件
    @OnClick(R.id.video_toolbar_iv_right)
    public void onClick() {
        finish();
    }

    @OnClick({R.id.video_paly, R.id.video_detail_iv_fav, R.id.video_detail_iv_share, R.id.video_detail_iv_reply, R.id.video_detail_iv_down})

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_paly://播放
                if (NetConnectedUtils.isNetConnected(this)) {
                    Intent intent = new Intent(this, ShowVideoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("video", video);
                    bundle.putString("title", title);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "网络异常，请稍后再试", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.video_detail_iv_fav://收藏
                break;
            case R.id.video_detail_iv_share://分享
                break;
            case R.id.video_detail_iv_reply://评论
                break;
            case R.id.video_detail_iv_down://下载
                break;
        }
    }


    /**
     * 加载数据和刷新数据
     */
    private void loadingData(final int page) {

        callResponse = ApiServiceImpl.findItemListImpl(this, page, new ResponseHandlerListener<ItemsResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
//                listEmptyView.startEmptyViewAnim();
//                if (page != 1)
//                    listFooterView.setVisibility(View.VISIBLE);
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
                                mAdapter.setData(listData);

                            } else {
                                listData.clear();
                                mAdapter.setData(listData);
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
//                        listEmptyView.stopEmptyViewAnim();
//                        listFooterView.setVisibility(View.GONE);
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


}
