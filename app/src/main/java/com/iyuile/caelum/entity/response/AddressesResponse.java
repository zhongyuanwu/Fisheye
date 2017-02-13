package com.iyuile.caelum.entity.response;


import com.google.gson.Gson;
import com.iyuile.caelum.entity.AddressEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 响应 {@link AddressEntity}
 */
public class AddressesResponse implements Serializable {

    private List<AddressEntity> data;

    public List<AddressEntity> getData() {
        return data;
    }

    public void setData(List<AddressEntity> data) {
        this.data = data;
    }

    public static AddressesResponse toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, AddressesResponse.class);
    }
}
