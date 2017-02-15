package com.iyuile.caelum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.entity.OrderEntity;
import com.iyuile.caelum.enums.OrderStatusValue;
import com.iyuile.caelum.util.ImageLoadOptions;
import com.iyuile.caelum.view.RoundAngleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

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
 * 时间:2017/2/14
 * 描叙:
 */

public class OrderAllAdapter extends RecyclerView.Adapter<OrderAllAdapter.OrderAllViewHolder> {
    private LayoutInflater mInflater;
    private List<OrderEntity> list;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions optionItem;
    protected DailyAdapter.OnItemClickListener mOnItemClickListener;

    public OrderAllAdapter(Context context, List<OrderEntity> list) {
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
    public OrderAllViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.item_order, null);
        final OrderAllViewHolder viewHolder = new OrderAllViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(view, viewHolder, position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderAllViewHolder holder, int position) {
        final OrderEntity orderEntity = list.get(position);
        String image = orderEntity.getItems().getData().get(0).getItem().getData().getImages().getData().get(0).getUrl();
        if (!image.equals(holder.ivImg.getTag())) {
            imageLoader.displayImage(image + AppConstants.IMAGE_URL_AVATAR_MIDDLE_THUMBNAIL_PARAMETER, holder.ivImg, optionItem);
            holder.ivImg.setTag(image);
        }

        holder.tvTitle.setText(orderEntity.getItems().getData().get(0).getItem().getData().getName());

        holder.tvDesc.setText(orderEntity.getItems().getData().get(0).getModel().getData().getName());

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

            holder.tvCount.setText(orderEntity.getItems().getData().size() + "");

            holder.tvPrice.setText(mInflater.getContext().getString(R.string.order_price_param, orderEntity.getTotal()));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class OrderAllViewHolder extends RecyclerView.ViewHolder {
        RoundAngleImageView ivImg;
        TextView tvTitle;
        TextView tvDesc;
        TextView tvStatus;
        TextView tvCount;
        TextView tvPrice;

        OrderAllViewHolder(View view) {
            super(view);
            ivImg = (RoundAngleImageView) view.findViewById(R.id.iv_img);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            tvStatus = (TextView) view.findViewById(R.id.tv_status);
            tvCount = (TextView) view.findViewById(R.id.tv_count);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(DailyAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
