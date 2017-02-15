package com.iyuile.caelum.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.ShoppingCartActivity;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.ShoppingCartEntity;
import com.iyuile.caelum.entity.response.ErrorResponse;
import com.iyuile.caelum.util.ImageLoadOptions;
import com.iyuile.caelum.util.MyApplication;
import com.iyuile.caelum.util.RetrofitUtils;
import com.iyuile.caelum.view.RoundAngleImageView;
import com.iyuile.caelum.view.toast.SuperToast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;


/**
 * 购物车适配器
 */
public class ShoppingCartAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ShoppingCartEntity> list;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions optionItem;

    public ShoppingCartAdapter(Context context, List<ShoppingCartEntity> list) {
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
            convertView = mInflater.inflate(R.layout.item_shopping_cart, null);
            holder.rbCheck = (RadioButton) convertView.findViewById(R.id.rb_check);
            holder.ivImg = (RoundAngleImageView) convertView.findViewById(R.id.iv_img);
            holder.etCount = (EditText) convertView.findViewById(R.id.et_count);
            holder.btnSubtract = (Button) convertView.findViewById(R.id.btn_subtract);
            holder.btnAdd = (Button) convertView.findViewById(R.id.btn_add);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
            holder.iconDelete = (TextView) convertView.findViewById(R.id.icon_delete);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

            holder.iconDelete.setTypeface(MyApplication.getInstance().getIconStyleFont());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ViewHolder finalHolder = holder;
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
            holder.etCount.setText(String.valueOf(shoppingCartEntity.getCount()));
        } catch (Exception e) {
        }

        try {
            holder.tvPrice.setText(mInflater.getContext().getString(R.string.order_price_param, shoppingCartEntity.getModel().getData().getPrice()));
        } catch (Exception e) {
        }

        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteShoppingCartItemApi(shoppingCartEntity, position, shoppingCartEntity.getId());
            }
        });

        holder.btnSubtract.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeShoppingCartItemCountApi(finalHolder.etCount, shoppingCartEntity, -1);
            }
        });

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeShoppingCartItemCountApi(finalHolder.etCount, shoppingCartEntity, +1);
            }
        });

        holder.rbCheck.setChecked(shoppingCartEntity.isCheck());

        holder.rbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!shoppingCartEntity.isCheck()) {
                    shoppingCartEntity.setCheck(true);
                    try {
                        Message msg = new Message();
                        msg.what = ShoppingCartActivity.RESPONSE_CODE_INIT;
                        msg.arg1 = shoppingCartEntity.getCount();
                        msg.arg2 = 1;
                        msg.obj = shoppingCartEntity.getModel().getData().getPrice();
                        ShoppingCartActivity.mInstance.handler.sendMessage(msg);
                    } catch (Exception e1) {
                    }
                } else {
                    finalHolder.rbCheck.setChecked(false);
                    shoppingCartEntity.setCheck(false);
                    try {
                        Message msg = new Message();
                        msg.what = ShoppingCartActivity.RESPONSE_CODE_INIT;
                        msg.arg1 = shoppingCartEntity.getCount();
                        msg.arg2 = 2;
                        msg.obj = shoppingCartEntity.getModel().getData().getPrice();
                        ShoppingCartActivity.mInstance.handler.sendMessage(msg);
                    } catch (Exception e1) {
                    }
                }
            }
        });

        return convertView;
    }

    private ErrorHandlingCallAdapter.MyCall<Void> callResponse;

    private boolean isStatus;

    private void deleteShoppingCartItemApi(final ShoppingCartEntity shoppingCartEntity, final int position, long shoppingCartItemId) {
        if (isStatus) return;
        isStatus = true;

        callResponse = ApiServiceImpl.deleteShoppingCartItemImpl(mInflater.getContext(), shoppingCartItemId, new ResponseHandlerListener<Void>(mInflater.getContext()) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void success(final int statusCode, final Response<Void> response) {
                super.success(statusCode, response);
                ((Activity) mInflater.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(mInflater.getContext(), mInflater.getContext().getResources().getString(R.string.shoppingcart_prompt_delete_complete),
                                SuperToast.Icon.Resource.YES,
                                SuperToast.Background.GREEN).show();
                        try {
                            if (shoppingCartEntity.isCheck()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = ShoppingCartActivity.RESPONSE_CODE_INIT;
                                    msg.arg1 = shoppingCartEntity.getCount();
                                    msg.arg2 = 2;
                                    msg.obj = shoppingCartEntity.getModel().getData().getPrice();
                                    ShoppingCartActivity.mInstance.handler.sendMessage(msg);
                                } catch (Exception e1) {
                                }
                            }
                            list.remove(position);
                            notifyDataSetChanged();
                        } catch (Exception e) {
                        }

                    }
                });
            }

            @Override
            public void clientError(final int statusCode, Response<?> response) {
                super.clientError(statusCode, response);
                ((Activity) mInflater.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(mInflater.getContext(), mInflater.getContext().getResources().getString(R.string.shoppingcart_prompt_delete_failure),
                                SuperToast.Icon.Resource.ERROR,
                                SuperToast.Background.RED).show();
                    }
                });
            }

            @Override
            public void serverError(int statusCode, Response<?> response) {
                super.serverError(statusCode, response);
                ((Activity) mInflater.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(mInflater.getContext(), mInflater.getContext().getResources().getString(R.string.shoppingcart_prompt_delete_failure),
                                SuperToast.Icon.Resource.ERROR,
                                SuperToast.Background.RED).show();
                    }
                });
            }

            @Override
            public void onFinish() {
                super.onFinish();
                isStatus = false;
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }

        });

        if (callResponse == null) {//没有网络 或者token
            isStatus = false;
        }

//        try {
//            callResponse.cancel();
//        } catch (NullPointerException e) { }
    }

    private ErrorHandlingCallAdapter.MyCall<Void> callChangeCountResponse;

    private void changeShoppingCartItemCountApi(final EditText etCount, final ShoppingCartEntity shoppingCartEntity, final int count) {
        if (isStatus) return;
        isStatus = true;

        if (count < 0 && shoppingCartEntity.getCount() == 1) {
            isStatus = false;
            return;
        }

        long shoppingCartItemId = shoppingCartEntity.getId();

        callChangeCountResponse = ApiServiceImpl.changeShoppingCartItemCountImpl(mInflater.getContext(), shoppingCartItemId, count, new ResponseHandlerListener<Void>(mInflater.getContext()) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void success(final int statusCode, final Response<Void> response) {
                super.success(statusCode, response);
                ((Activity) mInflater.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int _count = shoppingCartEntity.getCount() + count;
                            shoppingCartEntity.setCount(_count);
                            etCount.setText(_count + "");
                            if (shoppingCartEntity.isCheck()) {
                                Message msg = new Message();
                                msg.arg1 = _count;
                                msg.obj = shoppingCartEntity.getModel().getData().getPrice();
                                if (count > 0) {
                                    msg.what = ShoppingCartActivity.RESPONSE_CODE_UPDATE;
                                    msg.arg2 = 1;
                                } else {
                                    msg.what = ShoppingCartActivity.RESPONSE_CODE_UPDATE;
                                    msg.arg2 = 0;
                                }
                                ShoppingCartActivity.mInstance.handler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }

            @Override
            public void clientError(final int statusCode, final Response<?> response) {
                super.clientError(statusCode, response);
                ((Activity) mInflater.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (statusCode == NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400 || statusCode == NetworkConstants.RESPONSE_CODE_UNPROCESSABLE_ENTITY_422)
                            if (response.errorBody() != null) {
                                try {
                                    Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(mInflater.getContext()).
                                            responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                    ErrorResponse errorObject = errorConverter.convert(response.errorBody());

                                    if (errorObject.getMessage() != null)
                                        SuperToast.makeText(mInflater.getContext(), errorObject.getMessage(),
                                                SuperToast.Icon.Resource.ERROR,
                                                SuperToast.Background.RED).show();
                                } catch (Exception e) {
                                }
                            }
                    }
                });
            }

            @Override
            public void serverError(int statusCode, Response<?> response) {
                super.serverError(statusCode, response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                isStatus = false;
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }

        });

        if (callChangeCountResponse == null) {//没有网络 或者token
            isStatus = false;
        }

//        try {
//            callChangeCountResponse.cancel();
//        } catch (NullPointerException e) { }
    }

    static class ViewHolder {
        public RadioButton rbCheck;
        public RoundAngleImageView ivImg;
        public EditText etCount;
        private Button btnSubtract, btnAdd;
        public TextView tvTitle, tvDesc, iconDelete, tvPrice;
    }


}
