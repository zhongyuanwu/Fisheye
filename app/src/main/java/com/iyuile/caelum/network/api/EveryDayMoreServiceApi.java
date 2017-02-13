package com.iyuile.caelum.network.api;


import com.iyuile.caelum.network.HomePicEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by k21 on 2017/1/20.
 */

public interface EveryDayMoreServiceApi {
    @GET("{url}")
    Call<HomePicEntity> getEveryDayMoreDatas(@Path("url") String url);
}

