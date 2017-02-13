package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.ItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 响应 {@link ItemEntity}
 */
public class ItemsResponse implements Serializable {

    private List<ItemEntity> data;

    private MetaResponse meta;

    public List<ItemEntity> getData() {
        return data;
    }

    public void setData(List<ItemEntity> data) {
        this.data = data;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public void setMeta(MetaResponse meta) {
        this.meta = meta;
    }
}
