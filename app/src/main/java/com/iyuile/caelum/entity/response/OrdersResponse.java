package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.ItemEntity;
import com.iyuile.caelum.entity.OrderEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 响应 {@link OrderEntity}
 */
public class OrdersResponse implements Serializable {

    private List<OrderEntity> data;

    private MetaResponse meta;

    public List<OrderEntity> getData() {
        return data;
    }

    public void setData(List<OrderEntity> data) {
        this.data = data;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public void setMeta(MetaResponse meta) {
        this.meta = meta;
    }
}
