package com.iyuile.caelum.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.FindDetailActivity;
import com.iyuile.caelum.adapter.CommonAdapter;
import com.iyuile.caelum.adapter.ViewHolder;
import com.iyuile.caelum.model.FindMoreEntity;
import com.iyuile.caelum.network.HttpAdress;
import com.iyuile.caelum.utils.JsonParseUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 发现更多
 */
public class FindFragment extends SupportBaseFragment {


    @Bind(R.id.find_grid)
    GridView findGrid;
    private List<FindMoreEntity> dataEntities = new ArrayList<>();
    private View view;

    public static FindFragment newInstance() {
        Bundle args = new Bundle();

        FindFragment fragment = new FindFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, container, false);

        ButterKnife.bind(this, view);
        setListener();
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            initData();
        }
    }

    //设置事件监听
    private void setListener() {
        findGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FindMoreEntity entity = dataEntities.get(position);
                Intent intent = new Intent(getContext(), FindDetailActivity.class);
                intent.putExtra("name", entity.getName());
                startActivity(intent);
            }
        });
    }

    //初始化数据
    private void initData() {
        OkGo.get(HttpAdress.FIND_MORE).tag(this).cacheKey("findfragment").cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        parseJson(s);
                    }
                });
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //下载json数据
//        StringRequest request = new StringRequest(HttpAdress.FIND_MORE, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                parseJson(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        requestQueue.add(request);
//        requestQueue.start();
    }

    //设置适配器
    private void setAdapter(List<FindMoreEntity> dataEntities) {
        findGrid.setAdapter(new CommonAdapter<FindMoreEntity>(getContext(), dataEntities, R.layout.grid_item) {
            @Override
            public void convert(ViewHolder viewHolder, FindMoreEntity dataEntity) {
                viewHolder.setText(R.id.grid_tv, dataEntity.getName());
                viewHolder.setImageResourcewithFresco(R.id.grid_iv, Uri.parse(dataEntity.getBgPicture()));
            }
        });
    }

    //解析json数据
    private void parseJson(String jsonData) {
        List<FindMoreEntity> entities = JsonParseUtils.parseFromJson(jsonData);
        dataEntities.addAll(entities);
        //给适配器设置数据
        setAdapter(dataEntities);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        OkGo.getInstance().cancelTag(this);
    }
}
