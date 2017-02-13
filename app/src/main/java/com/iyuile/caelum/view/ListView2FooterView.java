package com.iyuile.caelum.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author WangYao
 * @version 1
 * @Description ListView底部加载更多布局
 * @ClassName {@link ListView2FooterView}
 * @Date 2016-12-13 19:22:56
 */
public class ListView2FooterView extends RelativeLayout {
    private Context context;

    private RelativeLayout listFooterView;
    private TextView tvPrompt;
    private GifImageView gif;


    public ListView2FooterView(Context context) {
        super(context);
        onCreate(context, null);
    }

    public ListView2FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate(context, attrs);
    }

    private void onCreate(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listFooterView = (RelativeLayout) inflater.inflate(R.layout.listview_footer_view, this);
        tvPrompt = (TextView) findViewById(R.id.tv_prompt);
        GifDrawable gifDrawable = null;
        try {
            gifDrawable = new GifDrawable(context.getAssets(), "pull-to-refresh-footer-loading-bar.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }

        gif = (GifImageView) findViewById(R.id.gif_footer);
        gif.setImageDrawable(gifDrawable);
    }


    public void openEmptyView() {
        openEmptyView(null);
    }

    /**
     * 开打null布局
     */
    public void openEmptyView(String textPrompt) {
        if (tvPrompt != null && gif != null) {
            if (textPrompt != null)
                tvPrompt.setText(textPrompt);
            else
                tvPrompt.setText(context.getString(R.string.list_data_not));
            tvPrompt.setVisibility(View.VISIBLE);
            gif.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 关闭null布局
     */
    public void closeEmptyView() {
        if (tvPrompt != null && gif != null) {
            tvPrompt.setVisibility(View.GONE);
            gif.setVisibility(View.VISIBLE);
        }
    }
}
