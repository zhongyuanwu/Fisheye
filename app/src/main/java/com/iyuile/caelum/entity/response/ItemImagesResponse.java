package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.ItemImageEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 响应 {@link ItemImageEntity}
 */
public class ItemImagesResponse implements Serializable {

    private List<ItemImageEntity> data;

    private MetaResponse meta;

    public List<ItemImageEntity> getData() {
        return data;
    }

    public void setData(List<ItemImageEntity> data) {
        this.data = data;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public void setMeta(MetaResponse meta) {
        this.meta = meta;
    }
}
