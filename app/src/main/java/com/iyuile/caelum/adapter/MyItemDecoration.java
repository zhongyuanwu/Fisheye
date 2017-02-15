package com.iyuile.caelum.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * **作者*****$$$$$\ $$\
 * **********\__$$ |\__|
 * ************|$$ |$$\ $$$$$$\$$$$\
 * ************|$$ |$$ |$$  _$$  _$$\
 * ******|$$\**|$$ |$$ |$$ / $$ / $$ |
 * *******\$$|_|$$ |$$ |$$ | $$ | $$ |
 * ********\$$$$$$ |$$ |$$ | $$ | $$ |
 * *********\______/\__|\__| \__| \__|
 * <p>
 * 时间:2017/2/15
 * 描叙:
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * @param outRect 边界
     * @param view    recyclerView ItemView
     * @param parent  recyclerView
     * @param state   recycler 内部数据管理
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //设定底部边距为1px
        outRect.set(0, 0, 0, 1);
    }
}
