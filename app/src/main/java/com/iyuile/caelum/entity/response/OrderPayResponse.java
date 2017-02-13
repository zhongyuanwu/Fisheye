package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.model.OrderPayModel;

import java.io.Serializable;

/**
 * @Description 响应 {@link OrderPayModel}
 */
public class OrderPayResponse implements Serializable {

    private OrderPayModel data;

    public OrderPayModel getData() {
        return data;
    }

    public void setData(OrderPayModel data) {
        this.data = data;
    }

}
