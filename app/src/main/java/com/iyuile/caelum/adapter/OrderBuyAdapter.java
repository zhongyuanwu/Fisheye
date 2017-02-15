package com.iyuile.caelum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.entity.OrderItemEntity;
import com.iyuile.caelum.util.ImageLoadOptions;
import com.iyuile.caelum.view.RoundAngleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * 订单支付适配器
 */
public class OrderBuyAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<OrderItemEntity> list;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions optionItem;

    public OrderBuyAdapter(Context context, List<OrderItemEntity> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        optionItem = ImageLoadOptions.getOptionItem();
    }

    public void setList(List<OrderItemEntity> list) {
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
    public OrderItemEntity getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_order_buy, null);
            holder.ivImg = (RoundAngleImageView) convertView.findViewById(R.id.iv_img);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
            holder.tvCountPrompt = (TextView) convertView.findViewById(R.id.tv_count_prompt);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OrderItemEntity orderItemEntity = getItem(position);

        try {
            String image = orderItemEntity.getItem().getData().getImages().getData().get(0).getUrl();
            if (!image.equals(holder.ivImg.getTag())) {
                imageLoader.displayImage(image + AppConstants.IMAGE_URL_AVATAR_MIDDLE_THUMBNAIL_PARAMETER, holder.ivImg, optionItem);
                holder.ivImg.setTag(image);
            }
        } catch (Exception e) {
        }

        try {
            holder.tvTitle.setText(orderItemEntity.getItem().getData().getName());
        } catch (Exception e) {
        }

        try {
            holder.tvDesc.setText(orderItemEntity.getModel().getData().getName());
        } catch (Exception e) {
        }

        try {
            holder.tvCount.setText(String.valueOf(orderItemEntity.getCount()));
        } catch (Exception e) {
        }

        try {
            holder.tvPrice.setText(mInflater.getContext().getString(R.string.order_price_param, orderItemEntity.getPay()));
        } catch (Exception e) {
        }

        return convertView;
    }


    static class ViewHolder {
        public RoundAngleImageView ivImg;
        public TextView tvTitle, tvDesc, tvCountPrompt, tvCount, tvPrice;
    }


}
