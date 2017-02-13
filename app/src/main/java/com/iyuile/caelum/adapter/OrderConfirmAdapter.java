package com.iyuile.caelum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.entity.ShoppingCartEntity;
import com.iyuile.caelum.utils.ImageLoadOptions;
import com.iyuile.caelum.utils.MyApplication;
import com.iyuile.caelum.view.RoundAngleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * 订单确认适配器
 */
public class OrderConfirmAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ShoppingCartEntity> list;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions optionItem;

    public OrderConfirmAdapter(Context context, List<ShoppingCartEntity> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        optionItem = ImageLoadOptions.getOptionItem();
    }

    public void setList(List<ShoppingCartEntity> list) {
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
    public ShoppingCartEntity getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_order_confirm, null);
            holder.ivImg = (RoundAngleImageView) convertView.findViewById(R.id.iv_img);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ShoppingCartEntity shoppingCartEntity = getItem(position);

        try {
            String image = shoppingCartEntity.getItem().getData().getImages().getData().get(0).getUrl();
            if (!image.equals(holder.ivImg.getTag())) {
                imageLoader.displayImage(image + AppConstants.IMAGE_URL_AVATAR_MIDDLE_THUMBNAIL_PARAMETER, holder.ivImg, optionItem);
                holder.ivImg.setTag(image);
            }
        } catch (Exception e) {
        }

        try {
            holder.tvTitle.setText(shoppingCartEntity.getItem().getData().getName());
        } catch (Exception e) {
        }

        try {
            holder.tvDesc.setText(shoppingCartEntity.getModel().getData().getName());
        } catch (Exception e) {
        }

        try {
            holder.tvCount.setText(String.valueOf(shoppingCartEntity.getCount()));
        } catch (Exception e) {
        }

        try {
            holder.tvPrice.setText(mInflater.getContext().getString(R.string.order_price_param, shoppingCartEntity.getModel().getData().getPrice()));
        } catch (Exception e) {
        }

        return convertView;
    }

    static class ViewHolder {
        public RoundAngleImageView ivImg;
        public TextView tvTitle, tvDesc, tvCount, tvPrice;
    }

}
