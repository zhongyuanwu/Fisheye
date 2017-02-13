package com.iyuile.caelum.api;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by WangYao on 2016/6/30.
 */
public final class ErrorHandlingCallAdapter {
    /**
     * A callback which offers granular callbacks for various conditions.
     */
    interface MyCallback<T> {
        /**
         * start
         */
        void onStart();

        /**
         * Called for [200, 300) responses.
         */
        void success(int statusCode, Response<T> response);

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
    }

    public static final int CODE_CANCEL = -77581;
    public static final int CODE_NETWORKERROR = -77582;
    public static final int CODE_UNEXPECTEDERROR = -77583;

    public interface MyCall<T> {
        void cancel() throws NullPointerException;

        void enqueue(MyCallback<T> callback);

        MyCall<T> clone();

        // Left as an exercise for the reader...
        // TODO MyResponse<T> execute() throws MyHttpException;
    }

    public static class ErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
        @Override
        public CallAdapter<MyCall<?>> get(Type returnType, Annotation[] annotations,
                                          Retrofit retrofit) {
            if (getRawType(returnType) != MyCall.class) {
                return null;
            }
            if (!(returnType instanceof ParameterizedType)) {
                throw new IllegalStateException(
                        "MyCall must have generic type (e.g., MyCall<ResponseBody>)");
            }
            final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
            final Executor callbackExecutor = retrofit.callbackExecutor();
            return new CallAdapter<MyCall<?>>() {
                @Override
                public Type responseType() {
                    return responseType;
                }

                @Override
                public <R> MyCall<R> adapt(Call<R> call) {
                    return new MyCallAdapter<>(call, callbackExecutor);
                }
            };
        }
    }

    /**
     * Adapts a {@link Call} to {@link MyCall}.
     */
    static class MyCallAdapter<T> implements MyCall<T> {
        private final Call<T> call;
        private final Executor callbackExecutor;

        MyCallAdapter(Call<T> call, Executor callbackExecutor) {
            this.call = call;
            this.callbackExecutor = callbackExecutor;
        }

        @Override
        public void cancel() {
            call.cancel();
        }

        @Override
        public void enqueue(final MyCallback<T> callback) {
            callback.onStart();
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    // TODO if 'callbackExecutor' is not null, the 'callback' methods should be executed
                    // on that executor by submitting a Runnable. This is left as an exercise for the reader.
                    int code = response.code();
                    if (code >= 200 && code < 300) {
                        callback.success(code, response);
                    } else if (code >= 400 && code < 500) {
                        callback.failure(code, response);
                        callback.clientError(code, response);
                        if (code == 401)
                            callback.unauthenticated(code, response);
                    } else if (code >= 500 && code < 600) {
                        callback.failure(code, response);
                        callback.serverError(code, response);
                    } else {
                        callback.failure(code, response);
                        callback.unexpectedError(new RuntimeException("Unexpected response " + response));
                    }
                    callback.onFinish();
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    // TODO if 'callbackExecutor' is not null, the 'callback' methods should be executed
                    // on that executor by submitting a Runnable. This is left as an exercise for the reader.
                    if (t instanceof IOException) {
                        if (((IOException) t).getMessage().equals("Socket closed")) {
                            callback.failure(CODE_CANCEL, (IOException) t);
                            callback.onCancel();
                        } else {
                            callback.failure(CODE_NETWORKERROR, (IOException) t);
                            callback.networkError((IOException) t);
                        }
                    } else {
                        callback.failure(CODE_UNEXPECTEDERROR, t);
                        callback.unexpectedError(t);
                    }
                    callback.onFinish();
                }
            });
        }

        @Override
        public MyCall<T> clone() {
            return new MyCallAdapter<>(call.clone(), callbackExecutor);
        }
    }
}