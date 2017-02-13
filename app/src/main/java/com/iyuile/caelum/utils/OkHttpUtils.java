package com.iyuile.caelum.utils;

import android.content.Context;
import android.text.TextUtils;

import com.iyuile.caelum.BuildConfig;
import com.iyuile.caelum.api.download.ProgressHelper;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.tools.SDCardTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by WangYao on 2016-10-20 13:57:29
 */
public class OkHttpUtils {
    public static final int CONNECT_TIMEOUT = 10 * 1000;// 值为0表示没有超时，否则值必须介于1和{@link # max_value }当转换为毫秒。
    public static final int READ_TIMEOUT = 10 * 1000;// 值为0表示没有超时，否则值必须介于1和{@link # max_value }当转换为毫秒。
    public static final int WRITE_TIMEOUT = 10 * 1000;// 值为0表示没有超时，否则值必须介于1和{@link # max_value }当转换为毫秒。

    private static OkHttpClient singleton;

    public static OkHttpClient getInstance(Context context) {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {
                    File cacheDir = new File(SDCardTools.getHttpCachePath(context));
                    OkHttpClient.Builder build = new OkHttpClient.Builder()
//                            .authenticator(mAuthenticator)
                            .addInterceptor(mInterceptor)
                            .addNetworkInterceptor(mTokenInterceptor)
                            .cookieJar(cookieJar)
                            .cache(new Cache(cacheDir, 10 * 1024 * 1024))//10 MB
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
                    build = ProgressHelper.addProgress(build);
                    singleton = build.build();
                }
            }
        }
        return singleton;
    }

    private static Interceptor mInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .header(NetworkConstants._PARAM_ACCEPT, NetworkConstants._VALUE_ACCEPT)
                    .method(originalRequest.method(), originalRequest.body());

            if (CommonUtils.isNetworkAvailable()) {
                //如果我api上设置了缓存方式这里就不用处理,如果没有设置给加上默认的no-cache
                String apiCache = originalRequest.header("Cache-Control");
                if (TextUtils.isEmpty(apiCache)) {
                    requestBuilder
                            .removeHeader("Pragma")
                            .header("Cache-Control", "no-cache");
                }
            } else {
                requestBuilder
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=31536000");//60*60*24*365 = 一年 (max-stale 单位:秒)
            }

            Request request = requestBuilder.build();

            Response response = chain.proceed(request);

            if (BuildConfig.DEBUG) {
                Log.i(":::OkHttp", String.format(":::Sending request %s%n%s", request.url(), request.headers()));
//                float duration = (float) (Long.valueOf(response.headers().get("OkHttp-Received-Millis")) - Long.valueOf(response.headers().get("OkHttp-Sent-Millis"))) / 1000;
//                Log.i(":::OkHttp", String.format(":::Received response for %s -%sm%n%s", response.request().url(), duration, response.headers()));
            }

            Response.Builder responseBuilder = response.newBuilder();

            return responseBuilder.build();
        }
    };

    private static Interceptor mTokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request.Builder requestBuilder = originalRequest.newBuilder();
            List<Cookie> cookies = singleton.cookieJar().loadForRequest(requestBuilder.build().url());
            if (!cookies.isEmpty())
                requestBuilder.header("Cookie", cookieHeader(cookies));
            //加上令牌
            /*if (originalRequest.header(NetworkConstants._PARAM_AUTHORIZATION) == null)
                requestBuilder.header(NetworkConstants._PARAM_AUTHORIZATION, String.format(NetworkConstants._VALUE_AUTHORIZATION, MyApplication.getInstance().mToken()));*/

            Request request = requestBuilder.build();

            Response response = chain.proceed(request);
            Response.Builder responseBuilder = response.newBuilder();

            // 如果服务端设置相应的缓存策略那么遵从服务端的不做修改
            /*String serverCache = response.header("Cache-Control");
            if (TextUtils.isEmpty(serverCache)) {
                String cacheControl = request.cacheControl().toString();
                responseBuilder
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", cacheControl);
            }*/

            //根据我api的设置
            String cacheControl = request.cacheControl().toString();
            if (cacheControl.equals("no-cache")) {
                responseBuilder
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "no-cache");
            } else {
                responseBuilder
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", cacheControl);
            }

            return responseBuilder.build();
        }
    };

    static CookieJar cookieJar = new CookieJar() {
        //这里的key必须是String
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    };

//    public void receiveHeaders(Headers headers) throws IOException {
//        if (singleton.cookieJar() == CookieJar.NO_COOKIES) return;
//
//        List<Cookie> cookies = Cookie.parseAll(userRequest.url(), headers);
//        if (cookies.isEmpty()) return;
//
//        singleton.cookieJar().saveFromResponse(userRequest.url(), cookies);
//    }

    private static String cookieHeader(List<Cookie> cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        return cookieHeader.toString();
    }

    /**
     * OkHttp会自动重试未验证的请求。当响应是401 Not Authorized时，Authenticator会被要求提供证书。Authenticator的实现中需要 m建立一个新的包含证书的请求。如果没有证书可用，返回null来跳过尝试。
     */
    private static Authenticator mAuthenticator = new Authenticator() {
        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            if (MyApplication.getInstance().mToken() != null && !MyApplication.getInstance().mToken().equals("null")) {
                return response.request().newBuilder()
                        .addHeader(NetworkConstants._PARAM_AUTHORIZATION, String.format(NetworkConstants._PARAM_AUTHORIZATION, MyApplication.getInstance().mToken()))// newAccessToken
                        .build();
            }
            return null;
        }
    };

}