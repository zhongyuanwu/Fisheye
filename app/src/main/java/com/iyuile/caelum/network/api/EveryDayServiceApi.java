package com.iyuile.caelum.network.api;


import com.iyuile.caelum.network.HomePicEntity;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by k21 on 2017/1/19.
 */

public interface EveryDayServiceApi {
    @GET("v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83")
    Call<HomePicEntity> getEveryDayDatas();
}
