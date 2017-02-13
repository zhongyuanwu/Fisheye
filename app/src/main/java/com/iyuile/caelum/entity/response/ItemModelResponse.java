package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.ItemModelEntity;

import java.io.Serializable;

/**
 * @Description 响应 {@link ItemModelEntity}
 */
public class ItemModelResponse implements Serializable {

    private ItemModelEntity data;

    public ItemModelEntity getData() {
        return data;
    }

    public void setData(ItemModelEntity data) {
        this.data = data;
    }
}
