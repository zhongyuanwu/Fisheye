package com.iyuile.caelum.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iyuile.caelum.R;
import com.iyuile.caelum.fragment.DailyFragment;
import com.iyuile.caelum.model.HomePicEntity;
import com.iyuile.caelum.view.CustomTextView;

import java.util.List;


/**
 * Created by k21 on 2017/1/16.
 */

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {
    private List<HomePicEntity.IssueListEntity.ItemListEntity> mItemList;
    private LayoutInflater mInflater;
    public static final int VIDEO = 1;
    public static final int TEXT = 2;
    protected OnItemClickListener mOnItemClickListener;
    protected DailyFragment mFragment;

    public DailyAdapter(DailyFragment fragment, Context context, List<HomePicEntity.IssueListEntity.ItemListEntity> itemList) {
        this.mItemList = itemList;
        this.mFragment = fragment;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public DailyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIDEO:
                final View videoView = mInflater.inflate(R.layout.list_home_vedio_item, parent, false);
                final DailyViewHolder dailyHolder = new DailyViewHolder(videoView);
                videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = dailyHolder.getAdapterPosition();
                            mOnItemClickListener.onItemClick(videoView, dailyHolder, position);
                        }
                    }
                });
                return dailyHolder;
            case TEXT:
                View textView = mInflater.inflate(R.layout.list_home_text_item, parent, false);
                return new DailyViewHolder(textView);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(DailyViewHolder holder, int position) {
        HomePicEntity.IssueListEntity.ItemListEntity itemListEntity = mItemList.get(position);
        int viewType = getItemViewType(position);
        String feed = "1";
        String title = "1";
        String category = "1";
        int duration = 0;
        String text = "1";
        switch (viewType) {
            case VIDEO:
                //得到不同类型所需要的数据
                feed = itemListEntity.getData().getCover().getFeed();
                title = itemListEntity.getData().getTitle();
                category = itemListEntity.getData().getCategory();
                category = "#" + category + "  /  ";

                duration = itemListEntity.getData().getDuration();

                int last = duration % 60;
                String stringLast;
                if (last <= 9) {
                    stringLast = "0" + last;
                } else {
                    stringLast = last + "";
                }

                String durationString;
                int minit = duration / 60;
                if (minit < 10) {
                    durationString = "0" + minit;

                } else {
                    durationString = "" + minit;

                }
                String stringTime = durationString + "' " + stringLast + '"';

                //set data

                Uri uri = Uri.parse(feed);
                SimpleDraweeView draweeView = (SimpleDraweeView) holder.imageView;
                draweeView.setImageURI(uri);


                holder.tvTitle.setText(title);
                holder.tvTime.setText(category + stringTime);
            case TEXT:
                //set data
                text = itemListEntity.getData().getText();
                if(text!=null){
                    holder.textView.setText(text);
                }

                String image = mItemList.get(position).getData().getImage();

                if (!TextUtils.isEmpty(image)) {
                    holder.textView.setTextSize(20);
                    holder.textView.setText("-Weekend  special-");
                }
            default:
                Log.e("avg","-------------------------其他类型");

        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {

        HomePicEntity.IssueListEntity.ItemListEntity itemListEntity = mItemList.get(position);
        if ("video".equals(itemListEntity.getType())) {
            return VIDEO;
        }

        return TEXT;
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle;
        TextView tvTime;
        CustomTextView textView;

        public DailyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iv);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            textView = (CustomTextView) view.findViewById(R.id.tv_home_text);

        }
    }

    public void setData(List<HomePicEntity.IssueListEntity.ItemListEntity> itemList) {
        this.mItemList = itemList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
