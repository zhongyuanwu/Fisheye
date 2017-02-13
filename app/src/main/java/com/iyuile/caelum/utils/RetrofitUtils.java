package com.iyuile.caelum.utils;

import android.content.Context;

import com.iyuile.caelum.api.ApiService;
import com.iyuile.caelum.api.ErrorHandlingCallAdapter;
import com.iyuile.caelum.contants.NetworkConstants;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by WangYao on 2016-10-20 13:57:21
 */
public class RetrofitUtils {

    private static ApiService apiService;
    private static Retrofit retrofit;

    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkConstants.API_URL)
                    .addCallAdapterFactory(new ErrorHandlingCallAdapter.ErrorHandlingCallAdapterFactory())
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpUtils.getInstance(context))
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService(Context context) {
        if (apiService == null) {
            getRetrofit(context);
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    /**
     * 解决responseBody多种类型问题
     * (其实修改Call<ResponseBody> 调用的地方自己解析json,response.body().string())
     */
    private static class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);
                }
            };
        }
    }
}