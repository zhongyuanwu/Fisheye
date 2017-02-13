package com.iyuile.caelum.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城商品分类
 * <p/>
 * Created by WangYao on 2016/12/12.
 */
public class ItemCatalogEntity implements Serializable {

    /**
     * id : 2
     * name : 分类2
     */

    private Long id;
    private String name;

    public static ItemCatalogEntity toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, ItemCatalogEntity.class);
    }

    public static List<ItemCatalogEntity> toJSONArrayItemCatalogEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<ItemCatalogEntity>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
