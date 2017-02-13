package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.ItemCatalogEntity;

import java.io.Serializable;

/**
 * @Description 响应 {@link ItemCatalogEntity}
 */
public class ItemCatalogResponse implements Serializable {

    private ItemCatalogEntity data;

    public ItemCatalogEntity getData() {
        return data;
    }

    public void setData(ItemCatalogEntity data) {
        this.data = data;
    }

}
