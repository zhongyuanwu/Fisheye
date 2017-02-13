package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.ItemModelEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 响应 {@link ItemModelEntity}
 */
public class ItemModelsResponse implements Serializable {

    private List<ItemModelEntity> data;

    private MetaResponse meta;

    public List<ItemModelEntity> getData() {
        return data;
    }

    public void setData(List<ItemModelEntity> data) {
        this.data = data;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public void setMeta(MetaResponse meta) {
        this.meta = meta;
    }
}
