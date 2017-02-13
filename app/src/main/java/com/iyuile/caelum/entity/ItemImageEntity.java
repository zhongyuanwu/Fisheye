package com.iyuile.caelum.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城商品分类
 * <p>
 * Created by WangYao on 2016/12/12.
 */
public class ItemImageEntity implements Serializable {


    /**
     * id : 1
     * url : jc.jpg
     * sort : 1
     */

    private Long id;
    private String url;
    private int sort;

    public static ItemImageEntity toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, ItemImageEntity.class);
    }

    public static List<ItemImageEntity> toJSONArrayItemImageEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<ItemImageEntity>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
