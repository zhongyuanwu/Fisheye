package com.iyuile.caelum.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.MallWareDetailedActivity;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.entity.ItemEntity;
import com.iyuile.caelum.util.ImageLoadOptions;
import com.iyuile.caelum.view.RoundAngleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * 商城适配器
 */
public class MallAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ItemEntity> list;
    private GridView gv;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions optionItem;

    public MallAdapter(Context context, GridView gv, List<ItemEntity> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.gv = gv;
        optionItem = ImageLoadOptions.getOptionItem();
    }

    public void setList(List<ItemEntity> list) {
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
    public ItemEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_mall, null);
            holder.rlContent = (RelativeLayout) convertView.findViewById(R.id.rl_content);
            holder.ivImg = (RoundAngleImageView) convertView.findViewById(R.id.iv_img);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int horizontalSpacing = gv.getHorizontalSpacing();
        int width = ((gv.getWidth() - horizontalSpacing * 2 - gv.getPaddingLeft() - gv.getPaddingRight()) / 3) - holder.rlContent.getPaddingLeft() - holder.rlContent.getPaddingRight();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        holder.ivImg.setLayoutParams(params);

        final ItemEntity itemEntity = getItem(position);

        try {
            String image = itemEntity.getImages().getData().get(0).getUrl();
            if (!image.equals(holder.ivImg.getTag())) {
                imageLoader.displayImage(image + AppConstants.IMAGE_URL_AVATAR_MIDDLE_THUMBNAIL_PARAMETER, holder.ivImg, optionItem);
                holder.ivImg.setTag(image);
            }
        } catch (Exception e) {
        }

        holder.tvTitle.setText(itemEntity.getName());

        try {
            holder.tvPrice.setText(mInflater.getContext().getString(R.string.order_price_param, itemEntity.getModels().getData().get(0).getPrice()));
        } catch (Exception e) {
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mInflater.getContext(), MallWareDetailedActivity.class);
                Bundle bundle = new Bundle();
                try {
                    bundle.putString(MallWareDetailedActivity.INTENT_PARAM_TITLE, itemEntity.getName());
                } catch (Exception e) {
                }
                bundle.putSerializable(MallWareDetailedActivity.INTENT_PARAM_ENTITY, itemEntity);
                bundle.putBoolean(MallWareDetailedActivity.INTENT_PARAM_PROGRESSBAR, false);
                intent.putExtras(bundle);
                mInflater.getContext().startActivity(intent);
            }
        });

        return convertView;
    }


    static class ViewHolder {
        public RelativeLayout rlContent;
        public RoundAngleImageView ivImg;
        public TextView tvTitle, tvPrice;

    }


}
