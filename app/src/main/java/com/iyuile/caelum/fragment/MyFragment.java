package com.iyuile.caelum.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.ReceiptAddressActivity;
import com.iyuile.caelum.activity.UpdateInfoActivity;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.UserEntity;
import com.iyuile.caelum.entity.response.CloudStorageTokenResponse;
import com.iyuile.caelum.entity.response.ErrorResponse;
import com.iyuile.caelum.enums.SexValue;
import com.iyuile.caelum.tools.SDCardTools;
import com.iyuile.caelum.utils.BitmapUtil;
import com.iyuile.caelum.utils.DialogUtils;
import com.iyuile.caelum.utils.ImageLoadOptions;
import com.iyuile.caelum.utils.MIUIUtils;
import com.iyuile.caelum.utils.PhotoUtils;
import com.iyuile.caelum.utils.RetrofitUtils;
import com.iyuile.caelum.utils.SDCardUtils;
import com.iyuile.caelum.utils.SDFileOperate;
import com.iyuile.caelum.utils.TimeUtil;
import com.iyuile.caelum.utils.VerifyUtil;
import com.iyuile.caelum.view.CircularImageView;
import com.iyuile.caelum.view.HeaderLayout;
import com.iyuile.caelum.view.toast.SuperToast;
import com.iyuile.pickerlibrary.TimePopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;


/**
 * 我的
 *
 * @author WangYao
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    public static MyFragment mInstance;

    private CircularImageView ivAvatar;
    private LinearLayout llNicknameUpdate;
    private TextView tvNickname;

    private LinearLayout llRealName, llSex, llBirthday, llUpdatePwd, llUpdatePhone, llHarvestAddress;
    private EditText etRealName, etSex, etBirthday;
    private TextView tvPhone;

    private TimePopupWindow pwTime;
    private String dateStr;

    private File imgFile, mCurrentPhotoFile;
    private String imgUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mInstance = this;
        initHeader();
        initView();
        initCalendarPicker();
        setUserInfo();
    }

    private void initHeader() {
        LinearLayout mHeaderLinearLayout = (LinearLayout) getActivity().findViewById(R.id.common_actionbar_fragment_my);
        View statusBar = mHeaderLinearLayout.findViewById(R.id.v_status_bar);
        setStatusBar(getActivity(), statusBar);
        HeaderLayout mHeaderLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(getString(R.string.main_toolbar_tv_my));
        mHeaderLayout.setTitleTypeface(mApplication.getWoodBodyStyleFont());

        final View tvLabel = mHeaderLinearLayout.findViewById(R.id.tv_label);
        final TextView tvTitle = mHeaderLayout.getTitleCenter();
        ViewTreeObserver vto = tvTitle.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                tvTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvLabel.getLayoutParams();
                layoutParams.width = tvTitle.getWidth();
                tvLabel.setLayoutParams(layoutParams);
            }
        });
    }

    /**
     * 设置数据
     */
    private void setUserInfo() {
        if (mApplication.getSpUtil().getCurrentUsersPhone().equals("null")) {
            mApplication.findTokenAndUsersInfo(getActivity());
        } else {
            try {
                String avatar = mApplication.mUserObject.getAvatar();
                if (!avatar.equals(ivAvatar.getTag())) {
                    ImageLoader.getInstance().displayImage(avatar + AppConstants.IMAGE_URL_AVATAR_MIDDLE_THUMBNAIL_PARAMETER, ivAvatar, ImageLoadOptions.getOptionAvatar());
                    ivAvatar.setTag(avatar);
                }
            } catch (Exception e) {
            }

            try {
                String nickname = mApplication.mUserObject.getNickname();
                if (!nickname.equals(tvNickname.getTag())) {
                    tvNickname.setText(nickname);
                    tvNickname.setTag(nickname);
                }
            } catch (Exception e) {
            }

            try {
                String realName = mApplication.mUserObject.getRealname();
                if (!realName.equals(etRealName.getTag())) {
                    etRealName.setText(realName);
                    etRealName.setTag(realName);
                }
            } catch (Exception e) {
            }

            try {
                SexValue sex = SexValue.getActionType(mApplication.mUserObject.getSex());
                String sexStr = getString(R.string.sex_3);
                if (sex != null) {
                    switch (sex) {
                        case MAN:
                            sexStr = getString(R.string.sex_1);
                            break;
                        case GIRL:
                            sexStr = getString(R.string.sex_2);
                            break;
                        case SECRECY:
                            break;
                    }
                }
                if (!sexStr.equals(etSex.getTag())) {
                    etSex.setText(sexStr);
                    etSex.setTag(sexStr);
                }
            } catch (Exception e) {
            }

            try {
                String birthday = mApplication.mUserObject.getBirthday();
                birthday = TimeUtil.stringToString(birthday, TimeUtil.FORMAT_DATE, TimeUtil.FORMAT_DATE);
                if (!birthday.equals(etBirthday.getTag())) {
                    etBirthday.setText(birthday);
                    etBirthday.setTag(birthday);
                }
            } catch (Exception e) {
            }

            try {
                String telephone = mApplication.mUserObject.getTelephone();
                telephone = VerifyUtil.processTelephone(telephone);
                if (!telephone.equals(tvPhone.getTag())) {
                    tvPhone.setText(telephone);
                    tvPhone.setTag(telephone);
                }
            } catch (Exception e) {
                tvPhone.setText(getString(R.string.my_tv_update_phone));
                tvPhone.setTag("");
            }

        }

    }

    ErrorHandlingCallAdapter.MyCall<Void> callUpdateBirthdayResponse;

    /**
     * 初始化日历
     */
    private void initCalendarPicker() {
        pwTime = new TimePopupWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setCyclic(true);
        int year = Integer.parseInt(TimeUtil.dateToString(new Date(), "yyyy"));
        pwTime.setRange(1901, year);
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                dateStr = TimeUtil.dateToString(date, TimeUtil.FORMAT_DATE);
                etBirthday.setText(dateStr);
                updateBirthday();
            }
        });

        try {
            Date date;
            if (mApplication.mUserObject.getBirthday() != null)
                date = TimeUtil.stringToDate(mApplication.mUserObject.getBirthday(), TimeUtil.FORMAT_DATE);
            else
                date = new Date();
            dateStr = TimeUtil.dateToString(date, TimeUtil.FORMAT_DATE);
        } catch (Exception e) {
            dateStr = TimeUtil.dateToString(new Date(), TimeUtil.FORMAT_DATE);
        }
    }

    /**
     * 修改生日
     */
    private void updateBirthday() {
        callUpdateBirthdayResponse = ApiServiceImpl.updateUserInfoToBirthdayImpl(getActivity(), dateStr, new ResponseHandlerListener<Void>(getActivity()) {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void success(int statusCode, final Response<Void> response) {
                        super.success(statusCode, response);
                        try {
                            mApplication.mUserObject.setBirthday(dateStr);
                            try {
                                mApplication.getSpUtil().setCurrentUsersInfo(new Gson().toJson(mApplication.mUserObject));
                            } catch (Exception e) {
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void clientError(final int statusCode, final Response<?> response) {
                        super.clientError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SuperToast.makeText(getContext(), getString(R.string.btn_failure),
                                        SuperToast.Icon.Resource.ERROR,
                                        SuperToast.Background.RED).show();
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (statusCode == NetworkConstants.RESPONSE_CODE_FORBIDDEN_403 || statusCode == NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400) {
                                    try {
                                        Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(getContext()).
                                                responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                        ErrorResponse errorObject = errorConverter.convert(response.errorBody());

                                        if (errorObject.getMessage() != null) {
                                            SuperToast.makeText(getActivity(), errorObject.getMessage(),
                                                    SuperToast.Icon.Resource.ERROR,
                                                    SuperToast.Background.RED).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (statusCode == NetworkConstants.RESPONSE_CODE_UNPROCESSABLE_ENTITY_422) {
                                    SuperToast.makeText(getActivity(), getString(R.string.update_nickname_exist),
                                            SuperToast.Icon.Resource.WARNING,
                                            SuperToast.Background.YELLOW).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void serverError(int statusCode, Response<?> response) {
                        super.serverError(statusCode, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                }

        );

        if (callUpdateBirthdayResponse == null) {//没有网络 或者token
        }

//        try {
//            callUpdateBirthdayResponse.cancel();
//        } catch (NullPointerException e) {}

    }

    private void initView() {
        setAllIconTextViewsFont(getActivity());

        ivAvatar = (CircularImageView) getActivity().findViewById(R.id.iv_avatar);
        tvNickname = (TextView) getActivity().findViewById(R.id.wb_tv_nickname);
        llNicknameUpdate = (LinearLayout) getActivity().findViewById(R.id.ll_nickname_update);

        ivAvatar.setOnClickListener(this);
        llNicknameUpdate.setOnClickListener(this);

        llRealName = (LinearLayout) getActivity().findViewById(R.id.ll_real_name);
        llSex = (LinearLayout) getActivity().findViewById(R.id.ll_sex);
        llBirthday = (LinearLayout) getActivity().findViewById(R.id.ll_birthday);
        llUpdatePwd = (LinearLayout) getActivity().findViewById(R.id.ll_update_pwd);
        llUpdatePhone = (LinearLayout) getActivity().findViewById(R.id.ll_update_phone);
        llHarvestAddress = (LinearLayout) getActivity().findViewById(R.id.ll_harvest_address);

        llRealName.setOnClickListener(this);
        llSex.setOnClickListener(this);
        llBirthday.setOnClickListener(this);
        llUpdatePwd.setOnClickListener(this);
        llUpdatePhone.setOnClickListener(this);
        llHarvestAddress.setOnClickListener(this);

        etRealName = (EditText) getActivity().findViewById(R.id.wb_et_real_name);
        etSex = (EditText) getActivity().findViewById(R.id.wb_et_sex);
        etBirthday = (EditText) getActivity().findViewById(R.id.wb_et_birthday);
        tvPhone = (TextView) getActivity().findViewById(R.id.wb_tv_update_phone);

        etRealName.setOnClickListener(this);
        etSex.setOnClickListener(this);
        etBirthday.setOnClickListener(this);
        tvPhone.setOnClickListener(this);

    }

    private static final int CAMERA_WITH_DATA = 0x00003102;// 照相机
    private static final int PHOTO_PICKED_WITH_DATA = 0x00003101;// 选择图片窗口
    private static final int MIUI_PHOTO_BUG = 0x00003103;// 小米系统_bug

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionFragmentHelper.onActivityForResult(requestCode);

        if (resultCode != getActivity().RESULT_OK) {
            if (imgFile == null)
                return;
            try {
                SDFileOperate.deleteFilePath(imgFile);
                imgFile = null;
            } catch (Exception e) {
            }
            return;
        }

        if (repairMIUI8BUG(requestCode, data)) return;

        switch (requestCode) {

            case CAMERA_WITH_DATA:
                Intent intent = PhotoUtils.doCropPhoto(mCurrentPhotoFile, imgFile, 1, 1, AppConstants.USERINFO_AVATAR_WIDTH, AppConstants.USERINFO_AVATAR_HEIGHT);
                startActivityForResult(intent, MIUI_PHOTO_BUG);
                break;

            case MIUI_PHOTO_BUG:
            case PHOTO_PICKED_WITH_DATA:
                try {
                    imgUrl = imgFile.getAbsolutePath();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                try {//用户选择图片时没有通过图库(bug:跳过了图片自带的裁切)而是通过其他软件(如:文件管理等)
                    if (SDFileOperate.getFileSizes(imgFile) == 0l) {
                        mCurrentPhotoFile = new File(data.getData().getPath());
                        Intent intent1 = PhotoUtils.doCropPhoto(mCurrentPhotoFile, imgFile, 1, 1, AppConstants.USERINFO_AVATAR_WIDTH, AppConstants.USERINFO_AVATAR_HEIGHT);
                        startActivityForResult(intent1, MIUI_PHOTO_BUG);
                        return;
                    }
                } catch (Exception e) {
                }

                if (imgUrl != null) {
                    String avatarUrl = "file://" + imgUrl;
                    ImageLoader.getInstance().displayImage(avatarUrl, ivAvatar, ImageLoadOptions.getOptionAvatar());
                    ivAvatar.setTag(avatarUrl);
                }
                imgFile = null;

                //上传
                uploadImageService();
                break;
            default:
                break;
        }
    }

    private boolean repairMIUI8BUG(int requestCode, Intent data) {
        if (MIUIUtils.isMIUI() && requestCode != CAMERA_WITH_DATA && requestCode != MIUI_PHOTO_BUG) {
            if (data == null) {
                return true;
            }
            Uri uri = data.getData();
            Cursor cursor = getCursor(data, uri);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);// 图片在的路径
                mCurrentPhotoFile = new File(path);
                Intent intent = PhotoUtils.doCropPhoto(mCurrentPhotoFile, imgFile, 1, 1, AppConstants.USERINFO_AVATAR_WIDTH, AppConstants.USERINFO_AVATAR_HEIGHT);
                startActivityForResult(intent, MIUI_PHOTO_BUG);
                return true;
            }
        }
        return false;
    }

    @Nullable
    private Cursor getCursor(Intent data, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            cursor = getActivity().managedQuery(uri, proj, null, null, null);
        } else {
            CursorLoader cursorLoader = new CursorLoader(getActivity(), uri, proj, null, null, null);
            cursor = cursorLoader.loadInBackground();
        }

        if (cursor == null) {
            try {
                uri = MIUIUtils.getUri(data, getActivity());//解决方案
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    cursor = getActivity().managedQuery(uri, proj, null, null, null);
                } else {
                    CursorLoader cursorLoader = new CursorLoader(getActivity(), uri, proj, null, null, null);
                    cursor = cursorLoader.loadInBackground();
                }
            } catch (Exception e) {
                return null;
            }
        }
        return cursor;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionFragmentHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                if (permissionFragmentHelper.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    selectPhoto();
                else {
                    permissionFragmentHelper
//                        .setForceAccepting(true)//强迫用户允许
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    //fragment需要自己手动去判断不再提示
                    if (permissionFragmentHelper.isPermissionPermanentlyDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        onPermissionReallyDeclined(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            case R.id.ll_nickname_update:
                startAlert(0);
                break;
            case R.id.ll_real_name:
            case R.id.wb_et_real_name:
                startAlert(1);
                break;
            case R.id.ll_sex:
            case R.id.wb_et_sex:
                startAlert(2);
                break;
            case R.id.ll_birthday:
            case R.id.wb_et_birthday:
                Date date;
                if (dateStr != null)
                    date = TimeUtil.stringToDate(dateStr, TimeUtil.FORMAT_DATE);
                else
                    date = new Date();
                pwTime.showAtLocation(v, Gravity.BOTTOM, 0, 0, date);
                break;
            case R.id.ll_update_pwd:
                startAlert(3);
                break;
            case R.id.ll_update_phone:
            case R.id.wb_tv_update_phone:
                startAlert(4);
                break;
            case R.id.ll_harvest_address:
                startAnimActivity(ReceiptAddressActivity.class);
                break;
        }
    }

    /**
     * 选择照片
     */
    private void selectPhoto() {
        if (mApplication.getSpUtil().getCurrentUsersPhone().equals("null")) {
            mApplication.findTokenAndUsersInfo(getActivity());
            return;
        }
        final Dialog dialog1 = DialogUtils.getInstance().showDiaLog(getActivity(), R.layout.dialogutils_choose_dialog, DialogUtils.DIALOG_TYPE_PHOTO);
        DialogUtils.getInstance().setCallBackEventForPhoto(new DialogUtils.CallBackEventForPhoto() {
            @Override
            public void local() {
                imgFile = SDFileOperate.createFile(SDCardTools.getImageAvatarPath(), PhotoUtils.getPhotoFileName());
                Intent intent = PhotoUtils.doPickPhotoFromGallery(imgFile, 1, 1, AppConstants.USERINFO_AVATAR_WIDTH, AppConstants.USERINFO_AVATAR_HEIGHT);
                startActivityForResult(Intent.createChooser(intent, "选择一个头像"), PHOTO_PICKED_WITH_DATA);

                dialog1.dismiss();
            }

            @Override
            public void take() {
                imgFile = SDFileOperate.createFile(SDCardTools.getImageAvatarPath(), PhotoUtils.getPhotoFileName());
                mCurrentPhotoFile = new File(SDCardUtils.getDCIMCameraPath(), PhotoUtils.getPhotoFileName());
                Intent intent = PhotoUtils.doTakePhoto(mCurrentPhotoFile);
                startActivityForResult(intent, CAMERA_WITH_DATA);

                dialog1.dismiss();
            }
        });
    }

    /**
     * 进入alert
     *
     * @param tabIndex
     */
    private void startAlert(int tabIndex) {
        if (mApplication.getSpUtil().getCurrentUsersPhone().equals("null")) {
            mApplication.findTokenAndUsersInfo(getActivity());
            return;
        }

        Intent intent = new Intent(getActivity(), UpdateInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(UpdateInfoActivity.INTENT_PARAM_TAB_SELECT_INDEX, tabIndex);
        intent.putExtras(bundle);
        startAnimActivity(intent);
    }

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 99:
                    updateUserInfoToAvatar();
                    break;
                case 1:
                    setUserInfo();
                    break;
                case 2:
                    updateAvatar(AppConstants.RESOURCE_URL + msg.obj.toString());
                    break;
                case 3://error
                    SuperToast.makeText(getActivity(), getString(R.string.cloud_storage_upload_error),
                            SuperToast.Icon.Resource.ERROR,
                            SuperToast.Background.RED).show();
                    break;
                case 4://更新生日,星座调用
                    try {
                        if (mApplication.mUserObject.getBirthday() != null) {
                            dateStr = mApplication.mUserObject.getBirthday();
                            etBirthday.setText(dateStr);
                        }
                    } catch (Exception e) {
                    }
                    break;
                case 5:
                    try {
                        ImageLoader.getInstance().displayImage("null", ivAvatar, ImageLoadOptions.getOptionAvatar());
                        ivAvatar.setTag("");

                        tvNickname.setText("");
                        tvNickname.setTag("");

                        etRealName.setText("");
                        etRealName.setTag("");

                        etSex.setText("");
                        etSex.setTag("");

                        etBirthday.setText("");
                        etBirthday.setTag("");

                        tvPhone.setText(getString(R.string.my_tv_update_phone));
                        tvPhone.setTag("");
                    } catch (Exception e) {
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    /**
     * 更新头像
     */
    private void updateUserInfoToAvatar() {
        if (mApplication.getSpUtil().getCurrentUsersPhone().equals("null")) {
            mApplication.findTokenAndUsersInfo(getActivity());
        } else {
            try {
                String avatar = mApplication.mUserObject.getAvatar();
                if (!avatar.equals(ivAvatar.getTag())) {
                    ImageLoader.getInstance().displayImage(avatar + AppConstants.IMAGE_URL_AVATAR_MIDDLE_THUMBNAIL_PARAMETER, ivAvatar, ImageLoadOptions.getOptionAvatar());
                    ivAvatar.setTag(avatar);
                }
            } catch (Exception e) {
            }
        }
    }

    // ------上传头像
    ErrorHandlingCallAdapter.MyCall<CloudStorageTokenResponse> callCloudStorageResponse;

    /**
     * 上传服务
     */
    private void uploadImageService() {

        callCloudStorageResponse = ApiServiceImpl.servicesCloudStorageTokenImpl(getActivity(), new ResponseHandlerListener<CloudStorageTokenResponse>(getActivity()) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void success(int statusCode, final Response<CloudStorageTokenResponse> response) {
                super.success(statusCode, response);
                try {
                    CloudStorageTokenResponse responseObject = response.body();
                    String cloudStorageToken = responseObject.getData().getSecretToken();
                    if (cloudStorageToken != null && !cloudStorageToken.equals("")) {
                        //上传
                        UploadManager uploadManager = new UploadManager();

                        byte[] data = BitmapUtil.bitmapToByteArray(imgUrl, 95);//<File对象、或 文件路径、或 字节数组>

                        String key = String.format(AppConstants._IMAGE_AVATAR, TimeUtil.longToString(System.currentTimeMillis(), "yyyyMMddHHmmssSSS") + (int) ((Math.random() * 9 + 1) * 100000) + mApplication.mUserObject.getId());//<指定七牛服务上的文件名，或 null>

                        uploadManager.put(data, key, cloudStorageToken, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, com.qiniu.android.http.ResponseInfo info, JSONObject res) {
                                //  res 包含hash、key等信息，具体字段取决于上传策略的设置。 Log.i("qiniu:", key + ",\r\n " + info + ",\r\n " + res.toString());
                                Message msg = new Message();
                                if (info.statusCode == 200 && info.error == null && res != null) {
                                    msg.what = 2;
                                    msg.obj = key;//文件名.
                                    handler.sendMessage(msg);
                                } else {
                                    msg.what = 3;
                                    msg.obj = key;
                                    handler.sendMessage(msg);
                                }
                            }
                        }, null);


                    } else {
                        uploadFailure();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SuperToast.makeText(getActivity(), getString(R.string.cloud_storage_upload_error),
                                        SuperToast.Icon.Resource.ERROR,
                                        SuperToast.Background.RED).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    uploadFailure();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SuperToast.makeText(getActivity(), getString(R.string.cloud_storage_upload_error),
                                    SuperToast.Icon.Resource.ERROR,
                                    SuperToast.Background.RED).show();
                        }
                    });
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(int statusCode, Object o) {
                super.failure(statusCode, o);
                uploadFailure();
            }

            @Override
            public void clientError(int statusCode, Response<?> response) {
                super.clientError(statusCode, response);
            }

            @Override
            public void serverError(int statusCode, Response<?> response) {
                super.serverError(statusCode, response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }

        });

        if (callCloudStorageResponse == null) {//没有网络 或者token
        }

//        try {
//            callCloudStorageResponse.cancel();
//        } catch (NullPointerException e) {}

    }

    ErrorHandlingCallAdapter.MyCall<Void> callUpdateAvatarResponse;

    /**
     * 修改头像
     *
     * @param avatarPath
     */
    private void updateAvatar(final String avatarPath) {
        callUpdateAvatarResponse = ApiServiceImpl.updateUserInfoToAvatarImpl(getActivity(), avatarPath, new ResponseHandlerListener<Void>(getActivity()) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void success(int statusCode, final Response<Void> response) {
                super.success(statusCode, response);
                try {
                    mApplication.getSpUtil().setCurrentUsersInfo(new Gson().toJson(mApplication.mUserObject));
                } catch (Exception e) {
                }
            }

            @Override
            public void failure(int statusCode, Object o) {
                super.failure(statusCode, o);
                uploadFailure();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(getActivity(), getString(R.string.avatar_upload_failure),
                                SuperToast.Icon.Resource.ERROR,
                                SuperToast.Background.RED).show();
                    }
                });
            }

            @Override
            public void clientError(int statusCode, Response<?> response) {
                super.clientError(statusCode, response);
            }

            @Override
            public void serverError(int statusCode, Response<?> response) {
                super.serverError(statusCode, response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }

        });

        if (callUpdateAvatarResponse == null) {//没有网络 或者token
            SuperToast.makeText(getActivity(), getString(R.string.avatar_upload_failure),
                    SuperToast.Icon.Resource.ERROR,
                    SuperToast.Background.RED).show();
            uploadFailure();
        } else {
            mApplication.mUserObject.setAvatar(avatarPath);
        }

//        try {
//            callUpdateAvatarResponse.cancel();
//        } catch (NullPointerException e) {}

    }

    /**
     * 上传失败
     */
    private void uploadFailure() {
        try {
            UserEntity usersEntity = UserEntity.toJSONObjectFromData(mApplication.getSpUtil().getCurrentUserInfo());
            mApplication.mUserObject.setAvatar(usersEntity.getAvatar());
        } catch (JsonSyntaxException e) {
        }
        try {
            MyFragment.mInstance.handler.sendEmptyMessage(99);
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        mInstance = null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Main_My");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("Main_My");
    }
}