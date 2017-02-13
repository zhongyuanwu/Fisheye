package com.iyuile.caelum.api;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.iyuile.caelum.R;
import com.iyuile.caelum.SupportMainActivity;
import com.iyuile.caelum.activity.SignInActivity;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.response.ErrorResponse;
import com.iyuile.caelum.utils.Log;
import com.iyuile.caelum.utils.MyApplication;
import com.iyuile.caelum.utils.RetrofitUtils;
import com.iyuile.caelum.view.toast.SuperToast;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by WangYao on 2016-10-20 14:18:44
 *
 * @version 4
 */
public class ResponseHandlerListener<T> implements ErrorHandlingCallAdapter.MyCallback<T>, Callback<T> {

    private Context context;

    public ResponseHandlerListener(Context context) {
        this.context = context;
    }

    // -------------------retrofit ---------
    @Override
    @Deprecated
    public void onResponse(Call<T> call, Response<T> response) {
        int statusCode = response.code();
        responseHandle(response, statusCode);
    }

    @Override
    @Deprecated
    public void onFailure(Call<T> call, Throwable t) {
        failureHandle(t);
    }

    @Deprecated
    private void responseHandle(Response<T> response, int statusCode) {

        Log.e(":::ResponseHandlerListener", ":::statusCode:" + statusCode + "\n:::msg:" + response.message() + "\n:::response.body():" + response.body() + "\n:::response.errorBody():" + response.errorBody());

        if (statusCode >= 200 && statusCode <= 299) {// 2XX成功
            switch (statusCode) {
                case NetworkConstants.RESPONSE_CODE_OK_200:
                    break;
                case NetworkConstants.RESPONSE_CODE_CREATED_201:
                    break;
                case NetworkConstants.RESPONSE_CODE_ACCEPTED_202:
                    break;
                case NetworkConstants.RESPONSE_CODE_NO_CONTENT_204:
                    break;
                default:
                    break;
            }
        } else if (statusCode >= 400 && statusCode <= 499) {// 4XX客户端错误
            switch (statusCode) {
                case NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400:// 坏请求(短信验证码发送失败;)
                    break;
                case NetworkConstants.RESPONSE_CODE_UNAUTHORIZED_401:// 授权失败
                    skipLogin();
                    break;
                case NetworkConstants.RESPONSE_CODE_FORBIDDEN_403:// 被禁止
                    SuperToast.makeText(context, context.getResources().getString(R.string.response_code_forbidden_403),
                            SuperToast.Icon.Resource.ERROR,
                            SuperToast.Background.RED).show();
                    break;
                case NetworkConstants.RESPONSE_CODE__NOT_FOUND_404:// 未发现(修改密码,该手机号没有注册,图片验证码不正确;使用者处理)
                    break;
                case NetworkConstants.RESPONSE_CODE_METHOD_NOT_ALLOWED_405:// 不允许的方法
                    SuperToast.makeText(context, context.getResources().getString(R.string.response_code_method_not_allowed_405),
                            SuperToast.Icon.Resource.ERROR,
                            SuperToast.Background.RED).show();
                    break;
                case NetworkConstants.RESPONSE_CODE_UNPROCESSABLE_ENTITY_422:// 无法处理的实体(注册参数存在错误;验证码不正确;使用者处理)
                    break;
                default:
                    break;
            }
        } else if (statusCode >= 500 && statusCode <= 599) {// 5XX服务器错误
            switch (statusCode) {
                case NetworkConstants.RESPONSE_CODE_INTERNAL_SERVER_ERROR_500:// 内部服务器错误(访问不到数据库,可能用户名或密码有更新;使用者处理)
                    if (response != null) {
                        try {
                            ErrorResponse errorObject = (ErrorResponse) response.body();
                            if (errorObject.getCode() == NetworkConstants.RESPONSE_CODE_SQL_STATE_1045) {// 内部服务器错误(数据库问题)
                                SuperToast.makeText(context, context.getResources().getString(R.string.service_defeated_check_up_network),
                                        SuperToast.Icon.Resource.ERROR,
                                        SuperToast.Background.RED).show();
                                return;
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    SuperToast.makeText(context, context.getResources().getString(R.string.service_response_error),
                            SuperToast.Icon.Resource.ERROR,
                            SuperToast.Background.RED).show();
                    break;
                case NetworkConstants.RESPONSE_CODE_SERVICE_UNAVAILABLE_503:// 暂停服务(没有网络也报这个)
                case NetworkConstants.RESPONSE_CODE_GATEWAY_TIMEOUT_504:// 网关超时(没有网络也报这个)
                    SuperToast.makeText(context, context.getResources().getString(R.string.response_code_not_networkavailable_0),
                            SuperToast.Icon.Resource.WARNING,
                            SuperToast.Background.YELLOW).show();
                    break;
                default:
                    SuperToast.makeText(context, context.getResources().getString(R.string.response_fail_retry),
                            SuperToast.Icon.Resource.ERROR,
                            SuperToast.Background.RED).show();
                    break;
            }
        }
    }

    @Deprecated
    private void failureHandle(Throwable t) {
        Log.e(":::ResponseHandlerListener", "onFailure:getLocalizedMessage(): " + t.getLocalizedMessage());
        Log.e(":::ResponseHandlerListener", "onFailure:getMessage(): " + t.getMessage());
        Log.e(":::ResponseHandlerListener", "onFailure:getCause(): " + t.getCause());
        t.printStackTrace();
    }

    /**
     * 跳转到登录
     *
     * @Description 未授权的时候
     */
    public void skipLogin() {
        /*&& ApiServiceImpl.mAccountDialog == null*/
        if (SignInActivity.mInstance == null) {//说明不再login页面,可能是请求的时候token过期,所以跳转
            SuperToast.makeText(context, context.getResources().getString(R.string.response_code_unauthorized_401),
                    SuperToast.Icon.Resource.INFO,
                    SuperToast.Background.BLUE).show();
            //跳转+重现获取token

            MyApplication mApplication = MyApplication.getInstance();

            mApplication.clearDaoUserMasterAndSession();// 清除用户本地数据库对象

            Intent intentLogin = new Intent(context, SignInActivity.class);

            /*if (mApplication.mUserObject != null) {
                intentLogin.putExtras()
            }*/

            intentLogin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            SignInActivity.startAnimU2DActivity(context, intentLogin, SupportMainActivity.REQUEST_CODE_LOGIN);
        }
    }


    @Override
    public void success(int statusCode, Response<T> response) {
        Log.e(":::ResponseHandlerListener:::success(code:200-299)",
                ":::statusCode:" + statusCode
                        + "\n:::msg:" + response.message()
                        + "\n:::response.body():" + response.body()
                        + "\n:::response.raw().cacheControl():" + response.raw().cacheControl()
                        + "\n:::response.raw().cacheResponse():" + response.raw().cacheResponse()
                        + "\n:::response.errorBody():" + response.errorBody());
        switch (statusCode) {
            case NetworkConstants.RESPONSE_CODE_OK_200:
                break;
            case NetworkConstants.RESPONSE_CODE_CREATED_201:
                break;
            case NetworkConstants.RESPONSE_CODE_ACCEPTED_202:
                break;
            case NetworkConstants.RESPONSE_CODE_NO_CONTENT_204:
                break;
            default:
                break;
        }
    }

    /**
     * @param statusCode
     * @param o          Response<?> response or IOException e or Throwable t
     * @version 4 所有失败的请求先进入{@link #failure(int, Object)} 然后进入各自的错误处理体(:::用于一些view的状态统一处理)
     */
    @Override
    public void failure(int statusCode, Object o) {
        String msgType = "null";
        if (o instanceof Response<?>)
            msgType = "Response<?> response";
        else if (o instanceof IOException)
            msgType = "IOException e";
        else if (o instanceof Throwable)
            msgType = "Throwable t";
        Log.e(":::ResponseHandlerListener:::failure(code:?)", ":::statusCode:" + statusCode + "\n:::msgType:" + msgType);
    }

    /**
     * @param statusCode
     * @param response
     * @version 1 statusCode=401会进入 {@link #unauthenticated(int, Response)} and {@link #onFinish()}
     * @version 3 修改后statusCode=401会先进入{@link #clientError(int, Response)} and {@link #unauthenticated(int, Response)} and {@link #onFinish()}
     */
    @Override
    public void unauthenticated(int statusCode, Response<?> response) {
        Log.e(":::ResponseHandlerListener:::unauthenticated(code:401)", ":::statusCode:" + statusCode + "\n:::msg:" + response.message() + "\n:::response.body():" + response.body() + "\n:::response.errorBody():" + response.errorBody());
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                skipLogin();
            }
        });
    }

    @Override
    public void clientError(int statusCode, Response<?> response) {
        Log.e(":::ResponseHandlerListener:::clientError(code:400-499)", ":::statusCode:" + statusCode + "\n:::msg:" + response.message() + "\n:::response.body():" + response.body() + "\n:::response.errorBody():" + response.errorBody());
        switch (statusCode) {
            case NetworkConstants.RESPONSE_CODE_BAD_REQUEST_400:// 坏请求(短信验证码发送失败;)
                break;
            case NetworkConstants.RESPONSE_CODE_UNAUTHORIZED_401:// 未授权
                /**
                 * {@link #unauthenticated(int, Response)} )
                 */
                break;
            case NetworkConstants.RESPONSE_CODE_FORBIDDEN_403:// 被禁止
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(context, context.getResources().getString(R.string.response_code_forbidden_403),
                                SuperToast.Icon.Resource.ERROR,
                                SuperToast.Background.RED).show();
                    }
                });
                break;
            case NetworkConstants.RESPONSE_CODE__NOT_FOUND_404:// 未发现(修改密码,该手机号没有注册,图片验证码不正确;使用者处理)
                break;
            case NetworkConstants.RESPONSE_CODE_METHOD_NOT_ALLOWED_405:// 不允许的方法
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(context, context.getResources().getString(R.string.response_code_method_not_allowed_405),
                                SuperToast.Icon.Resource.ERROR,
                                SuperToast.Background.RED).show();
                    }
                });
                break;
            case NetworkConstants.RESPONSE_CODE_UNPROCESSABLE_ENTITY_422:// 无法处理的实体(注册参数存在错误;验证码不正确;使用者处理)
                break;
            default:
                break;
        }
    }

    @Override
    public void serverError(int statusCode, Response<?> response) {
        Log.e(":::ResponseHandlerListener:::serverError(code:500-599)", ":::statusCode:" + statusCode + "\n:::msg:" + response.message() + "\n:::response.body():" + response.body() + "\n:::response.errorBody():" + response.errorBody());
        switch (statusCode) {
            case NetworkConstants.RESPONSE_CODE_INTERNAL_SERVER_ERROR_500:// 内部服务器错误(访问不到数据库,可能用户名或密码有更新;使用者处理)
                if (response.errorBody() != null) {
                    try {
                        Converter<ResponseBody, ErrorResponse> errorConverter = RetrofitUtils.getRetrofit(context).
                                responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                        ErrorResponse errorObject = errorConverter.convert(response.errorBody());

                        Log.e("ResponseHandlerListener:::serverError(code:500-599):errorObject.getMessage():",errorObject.getMessage());

                        if (errorObject.getCode() == NetworkConstants.RESPONSE_CODE_SQL_STATE_1045) {// 内部服务器错误(数据库问题)
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SuperToast.makeText(context, context.getResources().getString(R.string.service_defeated_check_up_network),
                                            SuperToast.Icon.Resource.ERROR,
                                            SuperToast.Background.RED).show();
                                }
                            });
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(context, context.getResources().getString(R.string.service_response_error),
                                SuperToast.Icon.Resource.ERROR,
                                SuperToast.Background.RED).show();
                    }
                });
                break;
            case NetworkConstants.RESPONSE_CODE_SERVICE_UNAVAILABLE_503:// 暂停服务(没有网络也报这个)
            case NetworkConstants.RESPONSE_CODE_GATEWAY_TIMEOUT_504:// 网关超时(没有网络也报这个)(Unsatisfiable Request (only-if-cached)(找不到缓存也报这个))
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(context, context.getResources().getString(R.string.response_code_not_networkavailable_0),
                                SuperToast.Icon.Resource.WARNING,
                                SuperToast.Background.YELLOW).show();
                    }
                });
                break;
            default:
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuperToast.makeText(context, context.getResources().getString(R.string.response_fail_retry),
                                SuperToast.Icon.Resource.ERROR,
                                SuperToast.Background.RED).show();
                    }
                });
                break;
        }
    }

    /**
     * 网络错误
     *
     * @param e
     */
    @Override
    public void networkError(IOException e) {
        Log.e(":::ResponseHandlerListener:::networkError(onFailure)", ":::IOException:e.getMessage():" + e.getMessage());
//        Unable to resolve host "api.andromeda.iyuile.com": No address associated with hostname
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SuperToast.makeText(context, context.getResources().getString(R.string.response_code_not_networkavailable_0),
                        SuperToast.Icon.Resource.WARNING,
                        SuperToast.Background.YELLOW).show();
            }
        });
//        e.printStackTrace();
    }

    @Override
    public void unexpectedError(Throwable t) {
        Log.e(":::ResponseHandlerListener:::unexpectedError(onFailure)", ":::IOException:t.getMessage():" + t.getMessage());
        t.printStackTrace();
    }

    @Override
    public void onFinish() {
        Log.e(":::ResponseHandlerListener:::onFinish", ":::finish");
    }

    /**
     * 取消后会进入{@link #networkError(IOException)} and {@link #onFinish()}
     *
     * @version 2 第二次修改后,取消不会再进入{@link #networkError(IOException)},会直接走{@link #onCancel()} and {@link #onFinish()}
     */
    @Override
    public void onCancel() {
        Log.e(":::ResponseHandlerListener:::onCancel", ":::cancel");
    }

    @Override
    public void onStart() {
        Log.e(":::ResponseHandlerListener:::onStart", ":::start");
    }
}
