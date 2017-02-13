package com.iyuile.caelum.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.iyuile.caelum.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


/**
 * @author WangYao
 * @version 1
 * @Description ListView底部加载更多布局
 * @ClassName {@link PullToRefreshFooterView}
 * @Date 2015-12-10 下午3:45:36
 */
public class PullToRefreshFooterView {

    private View listFooterView;

    private Context context;

    public PullToRefreshFooterView(Context context) {
        this.context = context;
    }

    /**
     *
     */
    public View onCreate() {
        listFooterView = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_footer, null);
        GifDrawable gifDrawable = null;
        try {
            gifDrawable = new GifDrawable(context.getAssets(), "pull-to-refresh-footer-loading-bar.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }

        GifImageView gif = (GifImageView) listFooterView.findViewById(R.id.gif_footer);
        gif.setImageDrawable(gifDrawable);

        return listFooterView;
    }

}
