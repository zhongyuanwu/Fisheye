package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.ItemEntity;
import com.iyuile.caelum.entity.ShoppingCartEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 响应 {@link ItemEntity}
 */
public class ShoppingCartsResponse implements Serializable {

    private List<ShoppingCartEntity> data;

    private MetaResponse meta;

    public List<ShoppingCartEntity> getData() {
        return data;
    }

    public void setData(List<ShoppingCartEntity> data) {
        this.data = data;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public void setMeta(MetaResponse meta) {
        this.meta = meta;
    }
}
