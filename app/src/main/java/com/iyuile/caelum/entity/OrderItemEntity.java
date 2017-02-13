package com.iyuile.caelum.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iyuile.caelum.entity.response.ItemModelResponse;
import com.iyuile.caelum.entity.response.ItemResponse;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城商品
 * <p>
 * Created by WangYao on 2016/12/12.
 */
public class OrderItemEntity implements Serializable {


    /**
     * id : 3
     * pay : 0.01
     * count : 9
     * item : {"data":{"id":1,"name":"测试商品1","desc":"测试描述","status":1,"images":{"data":[{"id":1,"url":"jc.jpg","sort":1}]}}}
     * model : {"data":{"id":1,"name":"商品1模式1","price":0.01,"count":2197}}
     */

    private Long id;
    private float pay;
    private int count;
    /**
     * data : {"id":1,"name":"测试商品1","desc":"测试描述","status":1,"images":{"data":[{"id":1,"url":"jc.jpg","sort":1}]}}
     */

    private ItemResponse item;
    /**
     * data : {"id":1,"name":"商品1模式1","price":0.01,"count":2197}
     */

    private ItemModelResponse model;

    public static OrderItemEntity toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, OrderItemEntity.class);
    }

    public static List<OrderItemEntity> toJSONArrayOrderItemEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<OrderItemEntity>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getPay() {
        return pay;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ItemResponse getItem() {
        return item;
    }

    public void setItem(ItemResponse item) {
        this.item = item;
    }

    public ItemModelResponse getModel() {
        return model;
    }

    public void setModel(ItemModelResponse model) {
        this.model = model;
    }
}
