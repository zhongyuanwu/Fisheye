package com.iyuile.caelum.api.download;

import android.os.Looper;

import com.iyuile.caelum.utils.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by WangYao on 2016/12/20.
 */
public class ProgressHelper {

    private static ProgressBean progressBean = new ProgressBean();
    private static ProgressHandler mProgressHandler;

    public static OkHttpClient.Builder addProgress(OkHttpClient.Builder builder) {

        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }

        final ProgressListener progressListener = new ProgressListener() {
            //该方法在子线程中运行
            @Override
            public void onProgress(long progress, long total, boolean done) {

                if (mProgressHandler == null) {
                    return;
                }

                //TODO :::progress
                Log.e("----------------------:::是否在主线程中运行", String.valueOf(Looper.getMainLooper() == Looper.myLooper()));
                Log.e(":::onProgress", String.format("%d%% done\n", (progress * 100) / total));
                int max = (int) (total / 1024);
                int bytesRead = (int) (progress / 1024);
                Log.e(":::onProgress", String.format("%1d KB/%2d KB", bytesRead, max));
                Log.e(":::onProgress:::done", "--->" + String.valueOf(done));
                //完成后调用清除方法
//                if (done) {
//                    ProgressHelper.clearProgressHandler();
//                }

                progressBean.setBytesRead(progress);
                progressBean.setContentLength(total);
                progressBean.setDone(done);
                mProgressHandler.sendMessage(progressBean);
            }
        };

        //添加拦截器，自定义ResponseBody，添加下载进度
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();

            }
        });

        return builder;
    }

    public static void setProgressHandler(ProgressHandler progressHandler) {
        mProgressHandler = progressHandler;
    }

    public static void clearProgressHandler() {
        mProgressHandler = null;
    }
}
