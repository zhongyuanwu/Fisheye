package com.iyuile.caelum.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.OrderBuyActivity;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.entity.OrderEntity;
import com.iyuile.caelum.enums.OrderStatusValue;
import com.iyuile.caelum.fragment.OrderFragment;
import com.iyuile.caelum.utils.ImageLoadOptions;
import com.iyuile.caelum.view.RoundAngleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * 订单适配器
 */
public class OrderAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<OrderEntity> list;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions optionItem;

    public OrderAdapter(Context context, List<OrderEntity> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        optionItem = ImageLoadOptions.getOptionItem();
    }

    public void setList(List<OrderEntity> list) {
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
    public OrderEntity getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_order, null);
            holder.ivImg = (RoundAngleImageView) convertView.findViewById(R.id.iv_img);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OrderEntity orderEntity = getItem(position);

        try {
            String image = orderEntity.getItems().getData().get(0).getItem().getData().getImages().getData().get(0).getUrl();
            if (!image.equals(holder.ivImg.getTag())) {
                imageLoader.displayImage(image + AppConstants.IMAGE_URL_AVATAR_MIDDLE_THUMBNAIL_PARAMETER, holder.ivImg, optionItem);
                holder.ivImg.setTag(image);
            }
        } catch (Exception e) {
        }

        try {
            holder.tvTitle.setText(orderEntity.getItems().getData().get(0).getItem().getData().getName());
        } catch (Exception e) {
        }

        try {
            holder.tvDesc.setText(orderEntity.getItems().getData().get(0).getModel().getData().getName());
        } catch (Exception e) {
        }

        try {
            OrderStatusValue orderStatusValue = OrderStatusValue.getActionType(orderEntity.getStatus());
            if (orderStatusValue != null) {
                switch (orderStatusValue) {
                    case UNPAID:
                        holder.tvStatus.setText(mInflater.getContext().getString(R.string.order_status_not_pay));
                        break;
                    case PAID:
                        holder.tvStatus.setText(mInflater.getContext().getString(R.string.order_status_not_shipped));
                        break;
                    case SEND:
                        holder.tvStatus.setText(mInflater.getContext().getString(R.string.order_status_already_shipped));
                        break;
                    case FINISH:
                        holder.tvStatus.setText(mInflater.getContext().getString(R.string.order_status_already_completed));
                        break;
                    default:
                        holder.tvStatus.setText(mInflater.getContext().getString(R.string.order_status_unknown));
                        break;
                }
            } else {
                holder.tvStatus.setText(mInflater.getContext().getString(R.string.order_status_unknown));
            }
        } catch (Exception e) {
            holder.tvStatus.setText(mInflater.getContext().getString(R.string.order_status_unknown));
        }

        try {
            holder.tvCount.setText(orderEntity.getItems().getData().size() + "");
        } catch (Exception e) {
        }

        try {
            holder.tvPrice.setText(mInflater.getContext().getString(R.string.order_price_param, orderEntity.getTotal()));
        } catch (Exception e) {
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mInflater.getContext(), OrderBuyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(OrderBuyActivity.INTENT_PARAM_ENTITY, orderEntity);
                intent.putExtras(bundle);
                try {
                    ((Activity) mInflater.getContext()).startActivityForResult(intent, OrderFragment.mInstance.REQUEST_CODE_RESULT_REFRESHING);
                } catch (Exception e) {
                    mInflater.getContext().startActivity(intent);
                }
            }
        });

        return convertView;
    }


    static class ViewHolder {
        public RoundAngleImageView ivImg;
        public TextView tvTitle, tvDesc, tvStatus, tvCount, tvPrice;

    }


}
