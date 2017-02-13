package com.iyuile.caelum.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;


/**
 * @author WangYao
 * @version 1
 * @Description ListView empty布局
 * @ClassName {@link ListView2EmptyView}
 * @Date 2016-12-13 19:22:56
 */
public class ListView2EmptyView extends RelativeLayout {
    private Context context;

    private RelativeLayout listEmptyView;
    private AnimationDrawable animationDrawableEmptyView;
    private ImageView imgEmptyViewLoadingAnim;
    public TextView tvPrompt;


    public ListView2EmptyView(Context context, AbsListView listView) {
        this(context, listView, false, null);
    }

    /**
     * @param context
     */
    public ListView2EmptyView(Context context, AbsListView listView, boolean isLoading, String textPrompt) {
        super(context);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listEmptyView = (RelativeLayout) inflater.inflate(R.layout.listview_empty_view, this);

        imgEmptyViewLoadingAnim = (ImageView) findViewById(R.id.iv_loading_anim);
        tvPrompt = (TextView) findViewById(R.id.tv_prompt);
        animationDrawableEmptyView = (AnimationDrawable) imgEmptyViewLoadingAnim.getDrawable();

        if (textPrompt != null)
            tvPrompt.setText(textPrompt);

        //默认显示加载中的字样
        if (isLoading)
            startEmptyViewAnim(textPrompt);

        listView.setEmptyView(this);
    }

    public void startEmptyViewAnim() {
        startEmptyViewAnim(null);
    }

    /**
     * 开始空数据布局加载动画
     */
    public void startEmptyViewAnim(String textPrompt) {
        if (imgEmptyViewLoadingAnim != null && animationDrawableEmptyView != null) {
            if (textPrompt != null)
                tvPrompt.setText(textPrompt);
            else
                tvPrompt.setText(context.getString(R.string.list_data_loading));

            imgEmptyViewLoadingAnim.setVisibility(View.VISIBLE);
            animationDrawableEmptyView.start();
        }
    }

    public void stopEmptyViewAnim() {
        stopEmptyViewAnim(null);
    }

    /**
     * 关闭空数据布局加载动画
     */
    public void stopEmptyViewAnim(String textPrompt) {
        if (imgEmptyViewLoadingAnim != null && animationDrawableEmptyView != null) {
            if (textPrompt != null)
                tvPrompt.setText(textPrompt);
            else
                tvPrompt.setText(context.getString(R.string.list_data_not));

            imgEmptyViewLoadingAnim.setVisibility(View.GONE);
            animationDrawableEmptyView.stop();
        }
    }
}
