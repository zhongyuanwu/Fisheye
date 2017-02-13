package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.ItemCatalogEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 响应 {@link ItemCatalogEntity}
 */
public class ItemCatalogsResponse implements Serializable {

    private List<ItemCatalogEntity> data;

    private MetaResponse meta;

    public List<ItemCatalogEntity> getData() {
        return data;
    }

    public void setData(List<ItemCatalogEntity> data) {
        this.data = data;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public void setMeta(MetaResponse meta) {
        this.meta = meta;
    }
}
