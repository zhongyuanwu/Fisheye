package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.ItemEntity;

import java.io.Serializable;

/**
 * @Description 响应 {@link ItemEntity}
 */
public class ItemResponse implements Serializable {

    private ItemEntity data;

    public ItemEntity getData() {
        return data;
    }

    public void setData(ItemEntity data) {
        this.data = data;
    }

}
