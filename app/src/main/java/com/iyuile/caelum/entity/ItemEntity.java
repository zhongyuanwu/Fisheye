package com.iyuile.caelum.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iyuile.caelum.entity.response.ItemCatalogResponse;
import com.iyuile.caelum.entity.response.ItemImagesResponse;
import com.iyuile.caelum.entity.response.ItemModelsResponse;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城商品
 * <p>
 * Created by WangYao on 2016/12/12.
 */
public class ItemEntity implements Serializable {

    /**
     * id : 2
     * name : 测试商品2
     * desc : 测试描述2
     * status : 1
     * catalog : {"data":{"id":2,"name":"分类2"}}
     * models : {"data":[{"id":2,"name":"商品2模式1","price":0.02,"count":2221}]}
     */

    private Long id;
    private String name;
    private String desc;
    private int status;
    private ItemCatalogResponse catalog;
    private ItemModelsResponse models;
    private ItemImagesResponse images;

    public static ItemEntity toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, ItemEntity.class);
    }

    public static List<ItemEntity> toJSONArrayItemEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<ItemEntity>>() {
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ItemCatalogResponse getCatalog() {
        return catalog;
    }

    public void setCatalog(ItemCatalogResponse catalog) {
        this.catalog = catalog;
    }

    public ItemModelsResponse getModels() {
        return models;
    }

    public void setModels(ItemModelsResponse models) {
        this.models = models;
    }

    public ItemImagesResponse getImages() {
        return images;
    }

    public void setImages(ItemImagesResponse images) {
        this.images = images;
    }
}
