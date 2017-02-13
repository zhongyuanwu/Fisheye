package com.iyuile.caelum.api;

import android.content.Context;

import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.contants.NetworkConstants;
import com.iyuile.caelum.entity.response.AddressResponse;
import com.iyuile.caelum.entity.response.AddressesResponse;
import com.iyuile.caelum.entity.response.CloudStorageTokenResponse;
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

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by WangYao on 2016/6/27.
 * <p>
 * {@link com.iyuile.caelum.api.impl.ApiServiceImpl}
 */
public interface ApiService {

    /**
     * 登录
     * {@link com.iyuile.caelum.api.impl.ApiServiceImpl#loginImpl(Context, String, String, ResponseHandlerListener)}
     *
     * @param paramMap
     * @return
     */
    @FormUrlEncoded
    @POST("oauth/token")
    ErrorHandlingCallAdapter.MyCall<TokenResponse> login(@FieldMap Map<String, String> paramMap);

    /**
     * 注册
     * {@link com.iyuile.caelum.api.impl.ApiServiceImpl#signUpImpl(Context, String, String, String, String, String, ResponseHandlerListener)}
     *
     * @param paramMap
     * @return
     */
    @FormUrlEncoded
    @POST("users")
    ErrorHandlingCallAdapter.MyCall<Void> signUp(@FieldMap Map<String, String> paramMap);

    /**
     * 获取自己的用户信息
     * {@link com.iyuile.caelum.api.impl.ApiServiceImpl#findMyUsersInfoImpl(Context)}
     *
     * @param authorization
     * @return
     */
    @GET("users/my")
    ErrorHandlingCallAdapter.MyCall<UserResponse> findMyUsersInfo(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization);

    /**
     * 验证用户
     * {@link com.iyuile.caelum.api.impl.ApiServiceImpl#userVerityImpl(Context, String, String, String, String, String, ResponseHandlerListener)}
     *
     * @param paramMap
     * @return
     */
    @GET("users/verity")
    ErrorHandlingCallAdapter.MyCall<VerityUsersResponse> userVerity(@QueryMap Map<String, String> paramMap);

    /**
     * 修改用户信息
     *
     * @param authorization
     * @param paramMap
     * @return
     */
    @FormUrlEncoded
    @PUT("users")
    ErrorHandlingCallAdapter.MyCall<Void> updateUserInfo(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @FieldMap Map<String, String> paramMap);


    /**
     * 修改用户详细信息
     *
     * @param authorization
     * @param paramMap
     * @return
     */
    @Deprecated
    @FormUrlEncoded
    @PUT("users/{userId}/profile")
    ErrorHandlingCallAdapter.MyCall<Void> updateUserProfileInfo(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Path("userId") Long userId, @FieldMap Map<String, String> paramMap);

    /**
     * 重置密码
     * {@link com.iyuile.caelum.api.impl.ApiServiceImpl#restPwdImpl(Context, String, String, String, String, String, ResponseHandlerListener)}
     *
     * @param paramMap
     * @return
     */
    @FormUrlEncoded
    @POST("users/forgot")
    ErrorHandlingCallAdapter.MyCall<Void> restPwd(@FieldMap Map<String, String> paramMap);

    /**
     * 修改密码
     * {@link com.iyuile.caelum.api.impl.ApiServiceImpl#updatePwdImpl(Context, long, String, String, ResponseHandlerListener)}
     *
     * @param authorization
     * @param paramMap
     * @return
     */
    @FormUrlEncoded
    @PUT("users/{userId}/password")
    ErrorHandlingCallAdapter.MyCall<Void> updatePwd(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Path("userId") Long userId, @FieldMap Map<String, String> paramMap);

    /**
     * 修改手机号
     *
     * @param authorization
     * @param userId
     * @param paramMap
     * @return
     */
    @FormUrlEncoded
    @PUT("users/{userId}/telephone")
    ErrorHandlingCallAdapter.MyCall<Void> updateTelephone(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Path("userId") Long userId, @FieldMap Map<String, String> paramMap);

    /**
     * 图片验证码
     * {@link com.iyuile.caelum.api.impl.ApiServiceImpl#servicesImageCodeImpl(Context, String, String, ResponseHandlerListener)}
     *
     * @return
     */
    @GET("services/image_code?width=202&height=58")
    ErrorHandlingCallAdapter.MyCall<ResponseBody> servicesImageCode(@Query("device") String device, @Query("token") String token);

    /**
     * 短信验证码
     * {@link com.iyuile.caelum.api.impl.ApiServiceImpl#servicesSmsCodeImpl(Context, String, String, String, String, ResponseHandlerListener)}
     *
     * @param paramMap
     * @return
     */
    @GET("services/send_sms_code")
    ErrorHandlingCallAdapter.MyCall<Void> servicesSmsCode(@QueryMap Map<String, String> paramMap);


    /**
     * 获取云存储token
     * {@link com.iyuile.caelum.api.impl.ApiServiceImpl#servicesCloudStorageTokenImpl(Context, ResponseHandlerListener)}
     *
     * @param authorization
     * @return
     */
    @GET("services/upload_token")
    ErrorHandlingCallAdapter.MyCall<CloudStorageTokenResponse> servicesCloudStorageToken(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization);

    /**
     * 获取请求token,用户图片验证码和短信验证码
     * {@link com.iyuile.caelum.api.impl.ApiServiceImpl#servicesRequestTokenImpl(Context, String, ApiServiceImpl.OnServicesRequestTokenListener)}
     *
     * @param device
     * @return
     */
    @GET("services/request_token")
    ErrorHandlingCallAdapter.MyCall<RequestTokenResponse> servicesRequestToken(@Query("device") String device);


    /**
     * 商城商品列表
     *
     * @param authorization
     * @param paramMap
     * @return
     */
    @GET("items?include=catalog,models,images")
    ErrorHandlingCallAdapter.MyCall<ItemsResponse> findItemList(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @QueryMap Map<String, String> paramMap);

    /**
     * 商城商品分类列表
     *
     * @param authorization
     * @return
     */
    @Headers("Cache-Control: public, max-age=3600")//max-age 单位:秒 (缓存1小时)
    @GET("item_catalogs")
    ErrorHandlingCallAdapter.MyCall<ItemCatalogsResponse> findItemCatalogList(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization);

    /**
     * 购物车添加
     *
     * @param authorization
     * @param paramMap
     * @return
     */
    @FormUrlEncoded
    @POST("shoppingcart")
    ErrorHandlingCallAdapter.MyCall<Void> addShoppingCart(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @FieldMap Map<String, String> paramMap);

    /**
     * 购物车列表
     *
     * @param authorization
     * @return
     */
    @GET("shoppingcart?include=item.images,model")
    ErrorHandlingCallAdapter.MyCall<ShoppingCartsResponse> findShoppingcartList(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization);

    /**
     * 购物车删除
     *
     * @param authorization
     * @param shoppingCartItemId
     * @return
     */
    @DELETE("shoppingcart/{shoppingCartItemId}")
    ErrorHandlingCallAdapter.MyCall<Void> deleteShoppingCartItem(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Path("shoppingCartItemId") Long shoppingCartItemId);

    /**
     * 创建订单
     *
     * @param authorization
     * @param addressId
     * @param itemList
     * @return
     */
    @FormUrlEncoded
    @POST("orders")
    ErrorHandlingCallAdapter.MyCall<Void> createOrder(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Field("address_id") Long addressId, @Field("list[]") Long... itemList);

    /**
     * 订单列表获取
     *
     * @param authorization
     * @param paramMap
     * @return
     */
    @GET("orders?include=items.item,items.item.images,items.model")
    ErrorHandlingCallAdapter.MyCall<OrdersResponse> findOrderList(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Query("page") int page, @QueryMap Map<String, String> paramMap);

    /**
     * 订单详细
     *
     * @param authorization
     * @return
     */
    @GET("orders/{uid}?include=items.item,items.item.images,items.model")
    ErrorHandlingCallAdapter.MyCall<OrderResponse> showOrder(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Path("uid") String uid);

    /**
     * 购物车item修改数量
     *
     * @param authorization
     * @param shoppingCartItemId
     * @param count
     * @return
     */
    @FormUrlEncoded
    @PUT("shoppingcart/{shoppingCartItemId}/count")
    ErrorHandlingCallAdapter.MyCall<Void> changeShoppingCartItemCount(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Path("shoppingCartItemId") Long shoppingCartItemId, @Field("count") int count);

    /**
     * 地址列表
     *
     * @param authorization
     * @return
     */
    @GET("addresses")
    ErrorHandlingCallAdapter.MyCall<AddressesResponse> findAddressList(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization);

    /**
     * 默认地址信息
     *
     * @param authorization
     * @return
     */
    @GET("addresses/default")
    ErrorHandlingCallAdapter.MyCall<AddressResponse> showAddressDefault(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization);


    /**
     * 地址删除
     *
     * @param authorization
     * @return
     */
    @DELETE("addresses/{addressId}")
    ErrorHandlingCallAdapter.MyCall<Void> deleteAddress(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Path("addressId") Long addressId);

    /**
     * 地址修改默认
     *
     * @param authorization
     * @param addressId
     * @return
     */
    @FormUrlEncoded
    @PUT("addresses/{addressId}/default")
    ErrorHandlingCallAdapter.MyCall<Void> updateAddressDefault(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Path("addressId") Long addressId, @Field("defaultSeat") int defaultSeat);

    /**
     * 地址添加
     *
     * @param authorization
     * @param paramMap
     * @return
     */
    @FormUrlEncoded
    @POST("addresses")
    ErrorHandlingCallAdapter.MyCall<Void> addAddress(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @FieldMap Map<String, String> paramMap);

    /**
     * 地址修改
     *
     * @param authorization
     * @param addressId
     * @param paramMap
     * @return
     */
    @FormUrlEncoded
    @PUT("addresses/{addressId}")
    ErrorHandlingCallAdapter.MyCall<Void> updateAddress(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Path("addressId") Long addressId, @FieldMap Map<String, String> paramMap);

    /**
     * 订单支付
     */
    @GET("orders/{uid}/pay")
    ErrorHandlingCallAdapter.MyCall<OrderPayResponse> orderPay(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @Path("uid") String uid, @Query("type") int type);

    /**
     * 意见反馈
     *
     * @param authorization
     * @param paramMap
     * @return
     */
    @FormUrlEncoded
    @POST("services/feedback")
    ErrorHandlingCallAdapter.MyCall<Void> addFeedback(@Header(NetworkConstants._PARAM_AUTHORIZATION) String authorization, @FieldMap Map<String, String> paramMap);

    /**
     * 更新版本获取
     *
     * @return
     */
    @GET("android")
    Call<UpdateResponse> findUpdateVersionSynchro();

    /**
     * 下载服务器返回的文件地址
     *
     * @param downloadUrl
     * @return
     */
    @GET
    Call<ResponseBody> downloadUpdateAppSynchro(@Url String downloadUrl);

}