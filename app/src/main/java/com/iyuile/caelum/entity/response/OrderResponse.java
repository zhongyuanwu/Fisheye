package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.OrderEntity;

import java.io.Serializable;

/**
 * @Description 响应 {@link OrderEntity}
 */
public class OrderResponse implements Serializable {

    private OrderEntity data;

    public OrderEntity getData() {
        return data;
    }

    public void setData(OrderEntity data) {
        this.data = data;
    }

}
