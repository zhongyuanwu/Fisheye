package com.iyuile.caelum.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyuile.caelum.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by k21 on 2017/2/10.
 * 我的 收藏的视频 评论的信息 我的订单等入口
 */

public class MyFunctionFragment extends SupportFragment {
    @Bind(R.id.tv_my_order)
    TextView tv_my_order;


    public static MyFunctionFragment newInstance() {
        Bundle args = new Bundle();

        MyFunctionFragment fragment = new MyFunctionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_function, container, false);
        ButterKnife.bind(this, inflate);
        setListener();
        return inflate;
    }
    private void setListener(){
        tv_my_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(OrderFragment.newInstance());
            }
        });
    }


}
