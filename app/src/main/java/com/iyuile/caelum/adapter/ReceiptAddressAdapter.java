package com.iyuile.caelum.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.iyuile.alertdialog.AlertDialog;
import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.ReceiptAddressActivity;
import com.iyuile.caelum.activity.ReceiptAddressNewActivity;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.AddressEntity;
import com.iyuile.caelum.entity.response.ErrorResponse;
import com.iyuile.caelum.util.MyApplication;
import com.iyuile.caelum.util.RetrofitUtils;
import com.iyuile.caelum.view.toast.SuperToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;


/**
 * 收货地址适配器
 */
public class ReceiptAddressAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<AddressEntity> list;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public ReceiptAddressAdapter(Context context, List<AddressEntity> list) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void setList(List<AddressEntity> list) {
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
    public AddressEntity getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_receipt_address, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.telephone = (TextView) convertView.findViewById(R.id.tv_telephone);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_receipt_address);
            holder.btnDefaultAddress = (RadioButton) convertView.findViewById(R.id.btn_default_address);
            holder.llEdit = (LinearLayout) convertView.findViewById(R.id.ll_edit);
            holder.llDelete = (LinearLayout) convertView.findViewById(R.id.ll_delete);
            holder.iconEdit = (TextView) convertView.findViewById(R.id.icon_edit);
            holder.iconDelete = (TextView) convertView.findViewById(R.id.icon_delete);
            holder.tvEdit = (TextView) convertView.findViewById(R.id.tv_edit);
            holder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);

            holder.iconEdit.setTypeface(MyApplication.getInstance().getIconStyleFont());
            holder.iconDelete.setTypeface(MyApplication.getInstance().getIconStyleFont());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ViewHolder finalHolder = holder;
        final AddressEntity addressEntity = getItem(position);

        holder.tvName.setText(addressEntity.getName());

        holder.telephone.setText(addressEntity.getTelephone());

        holder.tvAddress.setText(addressEntity.getAddress());

        holder.btnDefaultAddress.setChecked(addressEntity.isDefaultX());
        holder.btnDefaultAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDefaultApi(addressEntity);
            }
        });

        holder.llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mInflater.getContext(), ReceiptAddressNewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ReceiptAddressNewActivity.INTENT_PARAM_ENTITY, addressEntity);
                intent.putExtras(bundle);
                ((Activity) mInflater.getContext()).startActivityForResult(intent, ReceiptAddressActivity.REQUEST_CODE);
            }
        });
        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mInflater.getContext());
                builder.setTitle(mInflater.getContext().getString(R.string.alert_dialog_delete_prompt_title))
                        .setMessage(mInflater.getContext().getString(R.string.alert_dialog_delete_prompt_content))
                        .setPositiveButton(
                                mInflater.getContext().getString(R.string.alert_dialog_delete_prompt_cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                        .setNegativeButton(mInflater.getContext().getString(R.string.alert_dialog_delete_prompt_delete), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteApi(position, addressEntity.getId());
                            }

                        });

                AlertDialog mDialogDelete = builder.create();
                mDialogDelete.setCanceledOnTouchOutside(false);// 按对话框以外的地方不起作用。按返回键还起作用
                mDialogDelete.setCancelable(true);// 按对话框以外的地方不起作用。按返回键也不起作用=false
                mDialogDelete.show();
            }
        });

        return convertView;
    }

    private ErrorHandlingCallAdapter.MyCall<Void> callDeleteResponse;
    private boolean isStatus;

    private void deleteApi(final int position, long addressId) {
        if (isStatus) return;
        isStatus = true;

        callDeleteResponse = ApiServiceImpl.deleteAddressImpl(mInflater.getContext(), addressId, new ResponseHandlerListener<Void>(mInflater.getContext()) {

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
                            SuperToast.makeText(mInflater.getContext(), mInflater.getContext().getString(R.string.btn_complete),
                                    SuperToast.Icon.Resource.YES,
                                    SuperToast.Background.GREEN).show();
                            list.remove(position);
                            notifyDataSetChanged();
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
                        if (statusCode == NetworkConstants.RESPONSE_CODE__NOT_FOUND_404) {
                            SuperToast.makeText(mInflater.getContext(), mInflater.getContext().getString(R.string.response_code_not_found_404),
                                    SuperToast.Icon.Resource.ERROR,
                                    SuperToast.Background.RED).show();
                        } else if (statusCode == NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400) {
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
                                    e.printStackTrace();
                                }
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

        if (callDeleteResponse == null) {//没有网络 或者token
            isStatus = false;
        }

//        try {
//            callDeleteResponse.cancel();
//      } catch (NullPointerException e) { }
    }

    private ErrorHandlingCallAdapter.MyCall<Void> callUpdateDefaultResponse;

    private void updateDefaultApi(final AddressEntity addressEntity) {
        if (isStatus) return;
        isStatus = true;

        callUpdateDefaultResponse = ApiServiceImpl.updateAddressDefaultImpl(mInflater.getContext(), addressEntity.getId(), new ResponseHandlerListener<Void>(mInflater.getContext()) {

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
                            SuperToast.makeText(mInflater.getContext(), mInflater.getContext().getString(R.string.btn_complete),
                                    SuperToast.Icon.Resource.YES,
                                    SuperToast.Background.GREEN).show();
                            for (AddressEntity entity : list)
                                entity.setDefaultX(false);
                            addressEntity.setDefaultX(true);
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
                        if (statusCode == NetworkConstants.RESPONSE_CODE__NOT_FOUND_404) {
                            SuperToast.makeText(mInflater.getContext(), mInflater.getContext().getString(R.string.response_code_not_found_404),
                                    SuperToast.Icon.Resource.ERROR,
                                    SuperToast.Background.RED).show();
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

        if (callUpdateDefaultResponse == null) {//没有网络 或者token
            isStatus = false;
        }

//        try {
//            callUpdateDefaultResponse.cancel();
//      } catch (NullPointerException e) { }
    }

    static class ViewHolder {
        public TextView tvName, telephone, tvAddress;
        public RadioButton btnDefaultAddress;
        public LinearLayout llEdit, llDelete;
        public TextView iconEdit, iconDelete;
        public TextView tvEdit, tvDelete;
    }

}
