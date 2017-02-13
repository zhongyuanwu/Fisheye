package com.iyuile.caelum.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.MallWareListActivity;
import com.iyuile.caelum.entity.ItemCatalogEntity;
import com.iyuile.caelum.utils.MyApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * 商城->分类适配器
 */
public class MallClassifyAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ItemCatalogEntity> list;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public MallClassifyAdapter(Context context, List<ItemCatalogEntity> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void setList(List<ItemCatalogEntity> list) {
        if (list != null) {
            this.list = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public ItemCatalogEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_mall_classify, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.iconIndicate = (TextView) convertView.findViewById(R.id.icon_indicate);

            holder.iconIndicate.setTypeface(MyApplication.getInstance().getIconStyleFont());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ItemCatalogEntity itemCatalogEntity = getItem(position);

        holder.tvTitle.setText(itemCatalogEntity.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mInflater.getContext(), MallWareListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(MallWareListActivity.INTENT_PARAM_ID, itemCatalogEntity.getId());
                intent.putExtras(bundle);
                mInflater.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView iconIndicate;
    }

}
