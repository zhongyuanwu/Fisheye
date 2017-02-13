package com.iyuile.caelum.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城广告
 * <p/>
 * Created by WangYao on 2016/12/12.
 */
public class BannerEntity implements Serializable {


    private Long id;
    private String name;
    private String desc;
    private String image;
    private String url;
    private String sort;
    private String status;

    public static BannerEntity toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, BannerEntity.class);
    }

    public static List<BannerEntity> toJSONArrayBannerEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<BannerEntity>>() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
