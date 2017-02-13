package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.OrderItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 响应 {@link OrderItemEntity}
 */
public class OrderItemsResponse implements Serializable {

    private List<OrderItemEntity> data;

    private MetaResponse meta;

    public List<OrderItemEntity> getData() {
        return data;
    }

    public void setData(List<OrderItemEntity> data) {
        this.data = data;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public void setMeta(MetaResponse meta) {
        this.meta = meta;
    }
}
