package com.iyuile.caelum.network;


import com.iyuile.caelum.network.api.EveryDayMoreServiceApi;
import com.iyuile.caelum.network.api.EveryDayServiceApi;
import com.iyuile.caelum.utils.MyApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by k21 on 2017/1/18.
 */

public class RetrofitHelper {
    private static OkHttpClient mOkHttpClient;
    private static final String API_BASE_URL = "http://baobab.wandoujia.com/api/";
    private static final String API_BASE_URL_TWO="http://baobab.kaiyanapp.com/api/";
    static
    {
        initOkHttpClient();
    }
    public static EveryDayServiceApi getEveryDayService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(EveryDayServiceApi.class);
    }

    public static EveryDayMoreServiceApi getEveryDayMoreService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL_TWO)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(EveryDayMoreServiceApi.class);
    }



    /**
     * 初始化OKHttpClient
     * 设置缓存
     * 设置超时时间
     * 设置打印日志
     */
    private static void initOkHttpClient()
    {

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null)
        {
            synchronized (RetrofitHelper.class)
            {
                if (mOkHttpClient == null)
                {
                    //设置Http缓存
                    Cache cache = new Cache(new File(MyApplication.getInstance()
                            .getCacheDir(), "HttpCache"), 1024 * 1024 * 100);

                    mOkHttpClient = new OkHttpClient.Builder()
//                            .cache(cache)
//                            .addInterceptor(interceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }
}
