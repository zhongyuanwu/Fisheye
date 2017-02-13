package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.AddressEntity;

import java.io.Serializable;

/**
 * @Description 响应 {@link AddressEntity}
 */
public class AddressResponse implements Serializable {

    private AddressEntity data;

    public AddressEntity getData() {
        return data;
    }

    public void setData(AddressEntity data) {
        this.data = data;
    }

}
