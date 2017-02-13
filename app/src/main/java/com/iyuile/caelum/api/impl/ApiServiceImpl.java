package com.iyuile.caelum.api.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.Gson;
import com.iyuile.caelum.R;
import com.iyuile.caelum.api.ApiService;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.response.AddressResponse;
import com.iyuile.caelum.entity.response.AddressesResponse;
import com.iyuile.caelum.entity.response.CloudStorageTokenResponse;
import com.iyuile.caelum.entity.response.ErrorResponse;
import com.iyuile.caelum.entity.response.ItemCatalogsResponse;
import com.iyuile.caelum.entity.response.ItemsResponse;
import com.iyuile.caelum.entity.response.OrderPayResponse;
import com.iyuile.caelum.entity.response.OrderResponse;
import com.iyuile.caelum.entity.response.OrdersResponse;
import com.iyuile.caelum.entity.response.RequestTokenResponse;
import com.iyuile.caelum.entity.response.ShoppingCartsResponse;
import com.iyuile.caelum.entity.response.TokenResponse;
import com.iyuile.caelum.entity.response.UpdateResponse;
import com.iyuile.caelum.entity.response.UserResponse;
import com.iyuile.caelum.entity.response.VerityUsersResponse;
import com.iyuile.caelum.enums.OrderStatusValue;
import com.iyuile.caelum.enums.PayType;
import com.iyuile.caelum.fragment.MyFragment;
import com.iyuile.caelum.utils.CommonUtils;
import com.iyuile.caelum.utils.MyApplication;
import com.iyuile.caelum.utils.RetrofitUtils;
import com.iyuile.caelum.view.toast.SuperToast;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by WangYao on 2016/6/2.
 */
public class ApiServiceImpl {

    private static ProgressDialog progress;

    /*public static AlertDialog mAccountDialog;*/

    private static volatile boolean isUserInfo = false;

    /**
     * 获取token
     *
     * @return
     */
    public static String findToken() {
        String token = MyApplication.getInstance().mToken();
        if (token.equals("null"))
            return null;
        // String header="Basic " + Base64.encodeToString("username:password")
        return String.format(NetworkConstants._VALUE_AUTHORIZATION, token);
    }

    /**
     * 没有网络处理
     */
    private static void noNetwork() {
        SuperToast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getResources().getString(R.string.response_code_not_networkavailable_0),
                SuperToast.Icon.Resource.WARNING,
                SuperToast.Background.YELLOW).show();
    }

    /**
     * 登录 实现
     *
     * @param context
     * @param phone
     * @param pwd
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<TokenResponse> loginImpl(Context context, String phone, String pwd, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("grant_type", "password");
            paramMap.put("client_id", "1");
            paramMap.put("client_secret", "czUS3xA85zfAtDlajXdPgfJHTT4TabLo4tPiPj8u");
            paramMap.put("scope", "*");
            paramMap.put("username", phone);
            paramMap.put("password", pwd);
            ApiService apiService = RetrofitUtils.getApiService(context);
            ErrorHandlingCallAdapter.MyCall<TokenResponse> callResponse = apiService.login(paramMap);
            callResponse.enqueue(responseHandlerListener);
            return callResponse;
        } else
            noNetwork();
        return null;
    }

    /**
     * 注册
     *
     * @param context
     * @param phone
     * @param password
     * @param smsCode
     * @param device
     * @param token
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> signUpImpl(Context context, String phone, String password, String smsCode, String device, String token, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("telephone", phone + "");
            paramMap.put("password", password + "");
            paramMap.put("sms_code", smsCode + "");
            paramMap.put("device", device + "");
            paramMap.put("token", token + "");
            ApiService apiService = RetrofitUtils.getApiService(context);
            ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.signUp(paramMap);
            callResponse.enqueue(responseHandlerListener);
            return callResponse;
        } else
            noNetwork();
        return null;
    }

    /**
     * 验证用户
     *
     * @param context
     * @param phone
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<VerityUsersResponse> userVerityImpl(Context context, String phone, ResponseHandlerListener responseHandlerListener) {
        return userVerityImpl(context, phone, null, null, null, null, responseHandlerListener);
    }

    /**
     * 验证用户
     *
     * @param context
     * @param phone
     * @param nickname
     * @param password
     * @param username
     * @param email
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<VerityUsersResponse> userVerityImpl(Context context, String phone, String nickname, String password, String username, String
            email, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            Map<String, String> paramMap = new HashMap<String, String>();
            if (phone != null)
                paramMap.put("telephone", phone + "");
            if (nickname != null)
                paramMap.put("nickname", nickname + "");
            if (username != null)
                paramMap.put("username", username + "");
            if (email != null)
                paramMap.put("email", email + "");
            ApiService apiService = RetrofitUtils.getApiService(context);
            ErrorHandlingCallAdapter.MyCall<VerityUsersResponse> callResponse = apiService.userVerity(paramMap);
            callResponse.enqueue(responseHandlerListener);
            return callResponse;
        } else
            noNetwork();
        return null;
    }

    /**
     * 修改用户昵称
     *
     * @param context
     * @param nickname
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoToNicknameImpl(Context context, String nickname, ResponseHandlerListener responseHandlerListener) {
        return updateUserInfoImpl(context, nickname, null, null, 0, null, null, responseHandlerListener);
    }

    /**
     * 修改用户头像
     *
     * @param context
     * @param avatar
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoToAvatarImpl(Context context, String avatar, ResponseHandlerListener responseHandlerListener) {
        return updateUserInfoImpl(context, null, null, avatar, 0, null, null, responseHandlerListener);
    }

    /**
     * 修改用户性别
     *
     * @param context
     * @param sex
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoToSexImpl(Context context, int sex, ResponseHandlerListener responseHandlerListener) {
        return updateUserInfoImpl(context, null, null, null, sex, null, null, responseHandlerListener);
    }

    /**
     * 修改用户生日
     *
     * @param context
     * @param birthday
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoToBirthdayImpl(Context context, String birthday, ResponseHandlerListener responseHandlerListener) {
        return updateUserInfoImpl(context, null, null, null, 0, birthday, null, responseHandlerListener);
    }

    /**
     * 修改用户真实姓名
     *
     * @param context
     * @param realname
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoToRealnameImpl(Context context, String realname, ResponseHandlerListener responseHandlerListener) {
        return updateUserInfoImpl(context, null, null, null, 0, null, realname, responseHandlerListener);
    }

    /**
     * 修改用户信息
     *
     * @param context
     * @param nickname
     * @param username
     * @param avatar
     * @param responseHandlerListener
     * @return
     */
    private static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoImpl(Context context, String nickname, String username, String avatar, int sex, String birthday, String realname, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                Map<String, String> paramMap = new HashMap<String, String>();
                if (nickname != null)
                    paramMap.put("nickname", nickname);
                if (username != null)
                    paramMap.put("username", username);
                if (avatar != null)
                    paramMap.put("avatar", avatar);
                if (sex != 0)
                    paramMap.put("sex", sex + "");
                if (birthday != null)
                    paramMap.put("birthday", birthday);
                if (realname != null)
                    paramMap.put("realname", realname);
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.updateUserInfo(token, paramMap);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 修改用户详细信息
     *
     * @param context
     * @param userId
     * @param realName
     * @param position
     * @param officeNumber
     * @param officeEmail
     * @param companyName
     * @param companyAddress
     * @param provinceId
     * @param cityId
     * @param districtId
     * @param level
     * @param experience
     * @param responseHandlerListener
     * @return
     */
    @Deprecated
    private static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoProfileImpl(Context context, long userId,
                                                                                   String realName, String position, String officeNumber, String officeEmail, String companyName, String companyAddress,
                                                                                   int provinceId, int cityId, int districtId, Integer level, Integer experience,
                                                                                   ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null && MyApplication.getInstance().mUserObject != null) {
                if (userId == 0)
                    userId = MyApplication.getInstance().mUserObject.getId();
                Map<String, String> paramMap = new HashMap<String, String>();
                if (realName != null)
                    paramMap.put("real_name", realName);
                if (position != null)
                    paramMap.put("position", position);
                if (officeNumber != null)
                    paramMap.put("office_number", officeNumber);
                if (officeEmail != null)
                    paramMap.put("office_email", officeEmail);
                if (companyName != null)
                    paramMap.put("company_name", companyName);
                if (companyAddress != null)
                    paramMap.put("company_address", companyAddress);
                if (provinceId != 0)
                    paramMap.put("province_id", provinceId + "");
                if (cityId != 0)
                    paramMap.put("city_id", cityId + "");
                if (districtId != 0)
                    paramMap.put("district_id", districtId + "");
                if (level != null)
                    paramMap.put("level", level.toString());
                if (experience != null)
                    paramMap.put("experience", experience.toString());
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.updateUserProfileInfo(token, userId, paramMap);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 修改用户详细信息-真实姓名
     *
     * @param context
     * @param realName
     * @param responseHandlerListener
     * @return
     */
    @Deprecated
    public static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoProfile2RealNameImpl(Context context, String realName, ResponseHandlerListener responseHandlerListener) {
        return updateUserInfoProfileImpl(context, 0, realName, null, null, null, null, null, 0, 0, 0, null, null, responseHandlerListener);
    }

    /**
     * 修改用户详细信息-公司名称
     *
     * @param context
     * @param companyName
     * @param responseHandlerListener
     * @return
     */
    @Deprecated
    public static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoProfile2CompanyNameImpl(Context context, String companyName, ResponseHandlerListener responseHandlerListener) {
        return updateUserInfoProfileImpl(context, 0, null, null, null, null, companyName, null, 0, 0, 0, null, null, responseHandlerListener);
    }

    /**
     * 修改用户详细信息-职位
     *
     * @param context
     * @param job
     * @param responseHandlerListener
     * @return
     */
    @Deprecated
    public static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoProfile2JobImpl(Context context, String job, ResponseHandlerListener responseHandlerListener) {
        return updateUserInfoProfileImpl(context, 0, null, job, null, null, null, null, 0, 0, 0, null, null, responseHandlerListener);
    }

    /**
     * 修改用户详细信息-公司地址
     *
     * @param context
     * @param companyAddress
     * @param responseHandlerListener
     * @return
     */
    @Deprecated
    public static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoProfile2CompanyAddressImpl(Context context, String companyAddress, ResponseHandlerListener responseHandlerListener) {
        return updateUserInfoProfileImpl(context, 0, null, null, null, null, null, companyAddress, 0, 0, 0, null, null, responseHandlerListener);
    }

    /**
     * 修改用户详细信息-
     *
     * @param context
     * @param officeEmail
     * @param responseHandlerListener
     * @return
     */
    @Deprecated
    public static ErrorHandlingCallAdapter.MyCall<Void> updateUserInfoProfile2OfficeEmailImpl(Context context, String officeEmail, ResponseHandlerListener responseHandlerListener) {
        return updateUserInfoProfileImpl(context, 0, null, null, null, officeEmail, null, null, 0, 0, 0, null, null, responseHandlerListener);
    }

    /**
     * 重置密码
     *
     * @param context
     * @param phone
     * @param password
     * @param verityCode
     * @param device
     * @param token
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> restPwdImpl(Context context, String phone, String password, String verityCode, String device, String token, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("telephone", phone + "");
            paramMap.put("password", password + "");
            paramMap.put("sms_code", verityCode + "");
            paramMap.put("device", device + "");
            paramMap.put("token", token + "");
            ApiService apiService = RetrofitUtils.getApiService(context);
            ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.restPwd(paramMap);
            callResponse.enqueue(responseHandlerListener);
            return callResponse;
        } else
            noNetwork();
        return null;
    }

    /**
     * 修改密码
     *
     * @param context
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> updatePwdImpl(Context context, long userId, String oldPwd, String newPwd, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                Map<String, String> paramMap = new HashMap<String, String>();
                if (oldPwd != null)
                    paramMap.put("password", oldPwd);
                if (newPwd != null)
                    paramMap.put("new_password", newPwd);
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.updatePwd(token, userId, paramMap);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 修改手机号
     *
     * @param context
     * @param userId
     * @param oldPhone
     * @param newPhone
     * @param smsCode
     * @param device
     * @param requestToken
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> updateTelephoneImpl(Context context, long userId, String oldPhone, String newPhone, String smsCode, String device, String requestToken, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                Map<String, String> paramMap = new HashMap<String, String>();
                if (oldPhone != null)
                    paramMap.put("old_telephone", oldPhone);
                if (newPhone != null)
                    paramMap.put("telephone", newPhone);
                if (smsCode != null)
                    paramMap.put("verity_code", smsCode);
                paramMap.put("device", device);
                paramMap.put("token", requestToken);
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.updateTelephone(token, userId, paramMap);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 图片验证码
     *
     * @param context
     * @param device
     * @param token
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<ResponseBody> servicesImageCodeImpl(final Context context, String device, String token, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            ApiService apiService = RetrofitUtils.getApiService(context);
            ErrorHandlingCallAdapter.MyCall<ResponseBody> callResponse = apiService.servicesImageCode(device, token);
            callResponse.enqueue(responseHandlerListener);
            return callResponse;
        } else
            noNetwork();
        return null;
    }

    /**
     * 短信验证码
     *
     * @param context
     * @param phone
     * @param imageCode
     * @param device
     * @param token
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> servicesSmsCodeImpl(Context context, String phone, String imageCode, String device, String token, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("telephone", phone + "");
            paramMap.put("image_code", imageCode + "");
            paramMap.put("device", device + "");
            paramMap.put("token", token + "");
            ApiService apiService = RetrofitUtils.getApiService(context);
            ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.servicesSmsCode(paramMap);
            callResponse.enqueue(responseHandlerListener);
            return callResponse;
        } else
            noNetwork();
        return null;
    }

    /**
     * 获取云存储 实现
     *
     * @param context
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<CloudStorageTokenResponse> servicesCloudStorageTokenImpl(Context context, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<CloudStorageTokenResponse> callResponse = apiService.servicesCloudStorageToken(token);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    public interface OnServicesRequestTokenListener {
        void onResult(String[] data);
    }

    /**
     * 获取请求token 实现
     *
     * @param context
     * @param mDeviceRequestToken
     * @param onServicesRequestTokenListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<RequestTokenResponse> servicesRequestTokenImpl(final Context context, final String mDeviceRequestToken, final OnServicesRequestTokenListener onServicesRequestTokenListener) {
        final String deviceInfo = MyApplication.getInstance().mDeviceInfo();
        if (CommonUtils.isNetworkAvailable()) {
            if (!mDeviceRequestToken.equals("")) {
                //有token直接执行
                if (onServicesRequestTokenListener != null)
                    onServicesRequestTokenListener.onResult(new String[]{deviceInfo, mDeviceRequestToken});
            } else {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<RequestTokenResponse> callResponse = apiService.servicesRequestToken(deviceInfo);
                callResponse.enqueue(new ResponseHandlerListener<RequestTokenResponse>(context) {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void success(final int statusCode, final Response<RequestTokenResponse> response) {
                        super.success(statusCode, response);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RequestTokenResponse requestTokenResponse = response.body();
                                { //执行
                                    if (onServicesRequestTokenListener != null)
                                        onServicesRequestTokenListener.onResult(new String[]{deviceInfo, requestTokenResponse.getData().getToken()});
                                }
                            }
                        });
                    }

                    @Override
                    public void clientError(int statusCode, Response<?> response) {
                        super.clientError(statusCode, response);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SuperToast.makeText(context, context.getString(R.string.response_fail_retry),
                                        SuperToast.Icon.Resource.ERROR,
                                        SuperToast.Background.RED).show();
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
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                    }

                });

//                try {
//                    callResponse.cancel();
//                } catch (NullPointerException e) { }
                return callResponse;
            }
        } else
            noNetwork();
        return null;
    }


    /**
     * 获取自己的用户信息
     *
     * @param context
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<UserResponse> findMyUsersInfoImpl(final Context context) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<UserResponse> callResponse = apiService.findMyUsersInfo(token);

                callResponse.enqueue(new ResponseHandlerListener<UserResponse>(context) {

                    @Override
                    public void onStart() {
                        super.onStart();
                        isUserInfo = true;
//                        progress = ProgressDialog.show(context, null, context.getResources().getString(R.string.find_user_info_loading_prompt));
                    }

                    @Override
                    public void success(int statusCode, final Response<UserResponse> response) {
                        super.success(statusCode, response);

                        UserResponse userResponse = response.body();
                        MyApplication.getInstance().mUserObject = userResponse.getData();
                        MyApplication.getInstance().mUserObject.setTelephone(MyApplication.getInstance().getSpUtil().getCurrentUsersPhone());
                        //存到本地
                        String userJSON = new Gson().toJson(MyApplication.getInstance().mUserObject);
                        MyApplication.getInstance().getSpUtil().setCurrentUsersInfo(userJSON);

                        /*if (context instanceof UserInfoActivity) {
                        }*/

                        try {
                            MyFragment.mInstance.handler.sendEmptyMessage(1);
                        } catch (Exception e) {
                        }

//                        ((Activity) context).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    if (MyApplication.getInstance().mUserObject.getStatus() != StatusValue.NORMAL.getValue())
//                                        createAlertDialog(context);
//                                } catch (Exception e) {
//                                }
//                            }
//                        });
                    }

                    @Override
                    public void failure(int statusCode, Object o) {
                        super.failure(statusCode, o);
                        isUserInfo = false;
                    }

                    @Override
                    public void clientError(int statusCode, Response<?> response) {
                        super.clientError(statusCode, response);
                    }

                    @Override
                    public void serverError(int statusCode, final Response<?> response) {
                        super.serverError(statusCode, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (progress != null)
                            progress.dismiss();
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                    }


                    /**
                     * 创建alertDialog
                     *
                     * @param context
                     */
                    //TODO :::debug
                    /*private void createAlertDialog(final Context context) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(context.getString(R.string.alertDialog_userinfo_prompt_title))
                                .setMessage(context.getString(R.string.alertDialog_userinfo_prompt_content))
                                .setPositiveButton(
                                        context.getString(R.string.alertDialog_userinfo_prompt_out),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                exitApplication();
                                            }
                                        });
                        mAccountDialog = builder.create();
                        mAccountDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
                                    //物理返回键监听
                                    exitApplication();
                                return false;
                            }
                        });
//				        mAccountDialog.setCanceledOnTouchOutside(false);// 按对话框以外的地方不起作用。按返回键还起作用
                        mAccountDialog.setCancelable(false);// 按对话框以外的地方不起作用。按返回键也不起作用
                        mAccountDialog.show();
                    }

                    *//**
                     * 退出应用操作
                     *//*
                    private void exitApplication() {
                        mAccountDialog.cancel();
                        mAccountDialog = null;
                        //如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据。
//                        MobclickAgent.onKillProcess(context);
                        MyApplication.getInstance().mActivityManager.exit();
                    }*/

                });
//                try {
//                    callResponse.cancel();
//                } catch (NullPointerException e) {}

                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    // ---------------------------------------------------------------------------------------------


    /**
     * 商城商品列表
     *
     * @param context
     * @param page
     * @param cid
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<ItemsResponse> findItemListImpl(Context context, int page, long cid, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                Map<String, String> paramMap = new HashMap<String, String>();
                if (page != 0)
                    paramMap.put("page", page + "");
                if (cid != 0)
                    paramMap.put("cid", cid + "");
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<ItemsResponse> callResponse = apiService.findItemList(token, paramMap);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 商城商品列表(设置了cache,所以不需要自己判断网络)
     *
     * @param context
     * @param page
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<ItemsResponse> findItemListImpl(Context context, int page, ResponseHandlerListener responseHandlerListener) {
        return findItemListImpl(context, page, 0, responseHandlerListener);
    }

    /**
     * 商城商品分类列表(设置了cache,所以不需要自己判断网络)
     *
     * @param context
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<ItemCatalogsResponse> findItemCatalogListImpl(Context context, ResponseHandlerListener responseHandlerListener) {
        String token = findToken();
        if (token != null) {
            ApiService apiService = RetrofitUtils.getApiService(context);
            ErrorHandlingCallAdapter.MyCall<ItemCatalogsResponse> callResponse = apiService.findItemCatalogList(token);
            callResponse.enqueue(responseHandlerListener);
            return callResponse;
        } else {
            //弹出
            new ResponseHandlerListener(context).skipLogin();
        }
        return null;
    }

    /**
     * 购物车添加
     *
     * @param context
     * @param itemId
     * @param modelId
     * @param count
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> addShoppingCartImpl(Context context, long itemId, long modelId, int count, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                Map<String, String> paramMap = new HashMap<String, String>();
                if (itemId != 0)
                    paramMap.put("item_id", itemId + "");
                if (modelId != 0)
                    paramMap.put("model_id", modelId + "");
                if (count != 0)
                    paramMap.put("count", count + "");
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.addShoppingCart(token, paramMap);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 购物车列表
     *
     * @param context
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<ShoppingCartsResponse> findShoppingCartListImpl(Context context, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<ShoppingCartsResponse> callResponse = apiService.findShoppingcartList(token);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 购物车删除item
     *
     * @param context
     * @param shoppingCartItemId
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> deleteShoppingCartItemImpl(Context context, long shoppingCartItemId, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.deleteShoppingCartItem(token, shoppingCartItemId);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 创建订单
     *
     * @param context
     * @param addressId
     * @param itemList
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> createOrderImpl(Context context, long addressId, Long[] itemList, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.createOrder(token, addressId, itemList);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 订单列表获取
     *
     * @param context
     * @param page
     * @param orderStatusValue
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<OrdersResponse> findOrderListImpl(Context context, int page, OrderStatusValue orderStatusValue, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                Map<String, String> paramMap = new HashMap<String, String>();
                if (orderStatusValue.getValue() != 0)
                    paramMap.put("status", orderStatusValue.getValue() + "");
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<OrdersResponse> callResponse = apiService.findOrderList(token, page, paramMap);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 订单详细
     *
     * @param context
     * @param uid
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<OrderResponse> showOrderImpl(Context context, String uid, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<OrderResponse> callResponse = apiService.showOrder(token, uid);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 购物车item修改数量
     *
     * @param context
     * @param shoppingCartItemId
     * @param count
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> changeShoppingCartItemCountImpl(Context context, long shoppingCartItemId, int count, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.changeShoppingCartItemCount(token, shoppingCartItemId, count);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 地址列表
     *
     * @param context
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<AddressesResponse> findAddressListImpl(Context context, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<AddressesResponse> callResponse = apiService.findAddressList(token);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 默认地址信息
     *
     * @param context
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<AddressResponse> showAddressDefaultImpl(Context context, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<AddressResponse> callResponse = apiService.showAddressDefault(token);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 地址删除
     *
     * @param context
     * @param addressId
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> deleteAddressImpl(Context context, long addressId, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.deleteAddress(token, addressId);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 地址修改默认
     *
     * @param context
     * @param addressId
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> updateAddressDefaultImpl(Context context, long addressId, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.updateAddressDefault(token, addressId, 0);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 地址添加
     *
     * @param context
     * @param address
     * @param name
     * @param telephone
     * @param phone
     * @param zipCode
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> addAddressImpl(Context context, String address, String name, String telephone, String phone, String zipCode, boolean isCheck, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                Map<String, String> paramMap = new HashMap<String, String>();
                if (address != null)
                    paramMap.put("address", address);
                if (name != null)
                    paramMap.put("name", name);
                if (telephone != null)
                    paramMap.put("telephone", telephone);
                if (phone != null)
                    paramMap.put("phone", phone);
                if (zipCode != null)
                    paramMap.put("zip_code", zipCode);
                if (isCheck)
                    paramMap.put("default", "1");
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.addAddress(token, paramMap);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 地址修改
     *
     * @param context
     * @param address
     * @param name
     * @param telephone
     * @param phone
     * @param zipCode
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> updateAddressImpl(Context context, long addressId, String address, String name, String telephone, String phone, String zipCode, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                Map<String, String> paramMap = new HashMap<String, String>();
                if (address != null)
                    paramMap.put("address", address);
                if (name != null)
                    paramMap.put("name", name);
                if (telephone != null)
                    paramMap.put("telephone", telephone);
                if (phone != null)
                    paramMap.put("phone", phone);
                if (zipCode != null)
                    paramMap.put("zip_code", zipCode);
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.updateAddress(token, addressId, paramMap);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    public interface OnPayStatusListener {
        /**
         * start
         */
        void onStart();

        /**
         * Called for [200, 300) responses.
         */
        void success(int statusCode, Response<OrderPayResponse> response);

        /**
         * Called for [200, 600) responses.
         *
         * @param statusCode
         * @param o          Response<?> response or IOException e or Throwable t
         */
        void failure(int statusCode, Object o);

        /**
         * Called for [400, 500) responses, except 401.
         */
        void clientError(int statusCode, Response<?> response);

        /**
         * Called for [500, 600) response.
         */
        void serverError(int statusCode, Response<?> response);

        /**
         * Called for network errors while making the call.
         */
        void networkError(IOException e);

        /**
         * Called for unexpected errors while making the call.
         */
        void unexpectedError(Throwable t);

        /**
         * Called for 401 responses.
         */
        void unauthenticated(int statusCode, Response<?> response);

        /**
         * finish
         */
        void onFinish();

        /**
         * cancel
         */
        void onCancel();

        /**
         * failure
         */
        void onFailure();
    }

    /**
     * 订单支付
     *
     * @param context
     * @param uid
     * @param isVerify
     * @param payType
     * @param onPayStatusListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<OrderPayResponse> orderPayImpl(final Context context, String uid, boolean isVerify, final PayType payType, final OnPayStatusListener onPayStatusListener) {
        if (CommonUtils.isNetworkAvailable()) {
            if (isVerify) {
                switch (payType) {
                    case WeChat:
                        final IWXAPI wxApi = WXAPIFactory.createWXAPI(context, MyApplication.getInstance().wxAppID);
                        if (!wxApi.isWXAppInstalled()) {
                            SuperToast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getResources().getString(R.string.pay_wx_no_installed_prompt),
                                    SuperToast.Icon.Resource.WARNING,
                                    SuperToast.Background.YELLOW).show();
                            return null;
                        }
                        boolean isPaySupported = wxApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                        if (!isPaySupported) {
                            SuperToast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getResources().getString(R.string.pay_wx_no_supported_prompt),
                                    SuperToast.Icon.Resource.WARNING,
                                    SuperToast.Background.YELLOW).show();
                            return null;
                        }
                        break;
                    case Alipay:
                        break;
                    case CreditCard:
                        break;
                }
            }
            String token = findToken();
            if (token != null) {
                int type = payType.getValue();
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<OrderPayResponse> callResponse = apiService.orderPay(token, uid, type);
                callResponse.enqueue(new ResponseHandlerListener<OrderPayResponse>(context) {

                                         @Override
                                         public void onStart() {
                                             super.onStart();
                                             if (onPayStatusListener != null)
                                                 onPayStatusListener.onStart();
//                                             progress = ProgressDialog.show(context, null, context.getString(R.string.pay_go_prompt));
                                         }

                                         @Override
                                         public void success(final int statusCode, final Response<OrderPayResponse> response) {
                                             super.success(statusCode, response);
                                             ((Activity) context).runOnUiThread(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        if (statusCode == NetworkConstants.RESPONSE_CODE_OK_200) {//调用支付
                                                                                            try {
                                                                                                final OrderPayResponse orderBuyResponse = response.body();
                                                                                                switch (payType) {
                                                                                                    case WeChat:
                                                                                                        SuperToast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getResources().getString(R.string.skip_wx_pay_prompt),
                                                                                                                SuperToast.Icon.Resource.INFO,
                                                                                                                SuperToast.Background.BLUE).show();
                                                                                                        String wxAppID = MyApplication.getInstance().wxAppID;
                                                                                                        final IWXAPI wxApi = WXAPIFactory.createWXAPI(context, wxAppID);
                                                                                                        if (!wxApi.isWXAppInstalled()) {
                                                                                                            SuperToast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getResources().getString(R.string.pay_wx_no_installed_prompt),
                                                                                                                    SuperToast.Icon.Resource.WARNING,
                                                                                                                    SuperToast.Background.YELLOW).show();
                                                                                                            if (onPayStatusListener != null)
                                                                                                                onPayStatusListener.onFailure();
                                                                                                            return;
                                                                                                        }
                                                                                                        boolean isPaySupported = wxApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                                                                                                        if (isPaySupported) {
                                                                                                            PayReq req = new PayReq();
                                                                                                            req.appId = wxAppID;
                                                                                                            req.partnerId = orderBuyResponse.getData().getPartnerid();
                                                                                                            req.prepayId = orderBuyResponse.getData().getPrepayid();
                                                                                                            req.nonceStr = orderBuyResponse.getData().getNoncestr();
                                                                                                            req.timeStamp = orderBuyResponse.getData().getTimestamp();
                                                                                                            req.packageValue = orderBuyResponse.getData().getPackageValue();
                                                                                                            req.sign = orderBuyResponse.getData().getSign();
                                                                                                            // req.extData = "app data"; // optional
                                                                                                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                                                                                            wxApi.sendReq(req);
                                                                                                        } else {
                                                                                                            SuperToast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getResources().getString(R.string.pay_wx_no_supported_prompt),
                                                                                                                    SuperToast.Icon.Resource.WARNING,
                                                                                                                    SuperToast.Background.YELLOW).show();
                                                                                                            if (onPayStatusListener != null)
                                                                                                                onPayStatusListener.onFailure();
                                                                                                        }
                                                                                                        break;
                                                                                                    case Alipay:
                                                                                                        SuperToast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getResources().getString(R.string.skip_alipay_pay_prompt),
                                                                                                                SuperToast.Icon.Resource.INFO,
                                                                                                                SuperToast.Background.BLUE).show();
                                                                                                        break;
                                                                                                }
                                                                                            } catch (Exception e) {
                                                                                                e.printStackTrace();
                                                                                                SuperToast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getResources().getString(R.string.pay_failure_prompt),
                                                                                                        SuperToast.Icon.Resource.ERROR,
                                                                                                        SuperToast.Background.RED).show();
                                                                                                if (onPayStatusListener != null)
                                                                                                    onPayStatusListener.onFailure();
                                                                                            }
                                                                                        } else if (statusCode == NetworkConstants.RESPONSE_CODE_CREATED_201) {//免费
                                                                                            SuperToast.makeText(context, context.getString(R.string.dialog_order_buy_btn_complete),
                                                                                                    SuperToast.Icon.Resource.YES,
                                                                                                    SuperToast.Background.GREEN).show();
                                                                                            if (onPayStatusListener != null)
                                                                                                onPayStatusListener.success(statusCode, response);
                                                                                        }
                                                                                    }

                                                                                }

                                             );
                                         }

                                         @Override
                                         public void failure(final int statusCode, final Object o) {
                                             super.failure(statusCode, o);
                                             ((Activity) context).runOnUiThread(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     if (onPayStatusListener != null) {
                                                         onPayStatusListener.failure(statusCode, o);
                                                     }
                                                 }
                                             });
                                         }

                                         @Override
                                         public void clientError(final int statusCode, final Response<?> response) {
                                             super.clientError(statusCode, response);
                                             ((Activity) context).runOnUiThread(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     if (onPayStatusListener != null)
                                                         onPayStatusListener.clientError(statusCode, response);
                                                     if (response.errorBody() != null && statusCode == NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400) {
                                                         try {
                                                             Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(context).
                                                                     responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                                                             ErrorResponse errorObject = errorConverter.convert(response.errorBody());

                                                             if (errorObject.getMessage() != null) {
                                                                 SuperToast.makeText(context, errorObject.getMessage(),
                                                                         SuperToast.Icon.Resource.ERROR,
                                                                         SuperToast.Background.RED).show();
                                                             }
                                                         } catch (Exception e) {
                                                             e.printStackTrace();
                                                         }
                                                     }
                                                 }
                                             });
                                         }

                                         @Override
                                         public void serverError(final int statusCode, final Response<?> response) {
                                             super.serverError(statusCode, response);
                                             ((Activity) context).runOnUiThread(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     if (onPayStatusListener != null)
                                                         onPayStatusListener.serverError(statusCode, response);
                                                 }
                                             });
                                         }

                                         @Override
                                         public void unauthenticated(final int statusCode, final Response<?> response) {
                                             super.unauthenticated(statusCode, response);
                                             ((Activity) context).runOnUiThread(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     if (onPayStatusListener != null)
                                                         onPayStatusListener.unauthenticated(statusCode, response);
                                                 }
                                             });
                                         }

                                         @Override
                                         public void networkError(final IOException e) {
                                             super.networkError(e);
                                             ((Activity) context).runOnUiThread(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     if (onPayStatusListener != null)
                                                         onPayStatusListener.networkError(e);
                                                 }
                                             });
                                         }

                                         @Override
                                         public void unexpectedError(final Throwable t) {
                                             super.unexpectedError(t);
                                             ((Activity) context).runOnUiThread(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     if (onPayStatusListener != null)
                                                         onPayStatusListener.unexpectedError(t);
                                                 }
                                             });
                                         }

                                         @Override
                                         public void onFinish() {
                                             super.onFinish();
                                             ((Activity) context).runOnUiThread(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     if (onPayStatusListener != null)
                                                         onPayStatusListener.onFinish();
                                                 }
                                             });
//                         if (progress != null)
//                             progress.dismiss();
                                         }

                                         @Override
                                         public void onCancel() {
                                             super.onCancel();
                                             if (onPayStatusListener != null)
                                                 onPayStatusListener.onCancel();
                                         }
                                     }

                );

//                try {
//                    callResponse.cancel();
//                } catch (NullPointerException e) {}
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }



    /**
     * 意见反馈
     *
     * @param context
     * @param content
     * @param version
     * @param build
     * @param system
     * @param device
     * @param responseHandlerListener
     * @return
     */
    public static ErrorHandlingCallAdapter.MyCall<Void> addFeedbackImpl(Context context, String content, String version, String build, String system, String device, ResponseHandlerListener responseHandlerListener) {
        if (CommonUtils.isNetworkAvailable()) {
            String token = findToken();
            if (token != null) {
                Map<String, String> paramMap = new HashMap<String, String>();
                if (content != null)
                    paramMap.put("content", content);
                if (version != null)
                    paramMap.put("version", version);
                if (build != null)
                    paramMap.put("build", build);
                if (system != null)
                    paramMap.put("system", system);
                if (device != null)
                    paramMap.put("device", device);
                ApiService apiService = RetrofitUtils.getApiService(context);
                ErrorHandlingCallAdapter.MyCall<Void> callResponse = apiService.addFeedback(token, paramMap);
                callResponse.enqueue(responseHandlerListener);
                return callResponse;
            } else {
                //弹出
                new ResponseHandlerListener(context).skipLogin();
            }
        } else
            noNetwork();
        return null;
    }

    /**
     * 更新版本获取(同步)
     *
     * @param context
     * @return
     * @throws IOException
     */
    public static UpdateResponse findUpdateVersionSynchroImpl(Context context) throws IOException {
        if (CommonUtils.isNetworkAvailable()) {
            ApiService apiService = RetrofitUtils.getApiService(context);
            Call<UpdateResponse> call = apiService.findUpdateVersionSynchro();
//            int statusCode = call.clone().execute().code();
//            ResponseBody responseError = call.clone().execute().errorBody();
            return call.clone().execute().body();
        } else
            noNetwork();
        return null;
    }

    /**
     * 下载服务器返回的文件地址(同步)
     *
     * @param context
     * @param downloadUrl
     * @return
     * @throws IOException
     */
    public static ResponseBody downloadUpdateAppSynchroImpl(Context context, String downloadUrl) throws IOException {
        if (CommonUtils.isNetworkAvailable()) {
            ApiService apiService = RetrofitUtils.getApiService(context);
            Call<ResponseBody> call = apiService.downloadUpdateAppSynchro(downloadUrl);
//            int statusCode = call.clone().execute().code();
//            ResponseBody responseError = call.clone().execute().errorBody();
            return call.execute().body();
        } else
            noNetwork();
        return null;
    }


}
