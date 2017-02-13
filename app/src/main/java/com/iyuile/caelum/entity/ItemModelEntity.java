package com.iyuile.caelum.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城商品模型
 * <p/>
 * Created by WangYao on 2016/12/12.
 */
public class ItemModelEntity implements Serializable {

    /**
     * id : 2
     * name : 商品2模式1
     * price : 0.02
     * count : 2221
     */

    private Long id;
    private String name;
    private float price;
    private int count;

    public static ItemModelEntity toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, ItemModelEntity.class);
    }

    public static List<ItemModelEntity> toJSONArrayItemModelEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<ItemModelEntity>>() {
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
