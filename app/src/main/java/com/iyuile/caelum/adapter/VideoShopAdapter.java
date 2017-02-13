package com.iyuile.caelum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.entity.ItemEntity;
import com.iyuile.caelum.utils.ImageLoadOptions;
import com.iyuile.caelum.view.RoundAngleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by k21 on 2017/2/9.
 */

public class VideoShopAdapter extends RecyclerView.Adapter<VideoShopAdapter.VideoShopViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ItemEntity> listData;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions optionItem;
    private RecyclerView recyclerView;
    protected VideoShopAdapter.OnItemClickListener mOnItemClickListener;

    public VideoShopAdapter(Context context, List<ItemEntity> listData, RecyclerView recyclerView) {
        this.mContext = context;
        this.listData = listData;
        this.recyclerView = recyclerView;
        optionItem = ImageLoadOptions.getOptionItem();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public VideoShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.item_mall, parent, false);
        final VideoShopViewHolder videoShopViewHolder = new VideoShopViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = videoShopViewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(view, videoShopViewHolder, position);
                }
            }
        });
        return videoShopViewHolder;
    }

    @Override
    public void onBindViewHolder(VideoShopViewHolder holder, int position) {
        ItemEntity itemEntity = listData.get(position);

        int width = (recyclerView.getWidth() / 3) - holder.rl_content.getPaddingLeft()*2 - holder.rl_content.getPaddingRight();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        holder.iv_img.setLayoutParams(params);

        try {
            String image = itemEntity.getImages().getData().get(0).getUrl();
            if (!image.equals(holder.iv_img.getTag())) {
                imageLoader.displayImage(image + AppConstants.IMAGE_URL_AVATAR_MIDDLE_THUMBNAIL_PARAMETER, holder.iv_img, optionItem);
                holder.iv_img.setTag(image);
            }
        } catch (Exception e) {
        }
        holder.tv_title.setText(itemEntity.getName());

        try {
            holder.tv_price.setText(mInflater.getContext().getString(R.string.order_price_param, itemEntity.getModels().getData().get(0).getPrice()));
        } catch (Exception e) {
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class VideoShopViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_content;
        RoundAngleImageView iv_img;
        TextView tv_title, tv_price;

        public VideoShopViewHolder(View view) {
            super(view);
            rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
            iv_img = (RoundAngleImageView) view.findViewById(R.id.iv_img);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
        }
    }

    public void setData(List<ItemEntity> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(VideoShopAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
