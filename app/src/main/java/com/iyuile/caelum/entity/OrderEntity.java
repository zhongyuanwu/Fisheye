package com.iyuile.caelum.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.iyuile.caelum.entity.response.OrderItemsResponse;
import com.iyuile.caelum.enums.PayType;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城商品
 * <p>
 * Created by WangYao on 2016/12/12.
 */
public class OrderEntity implements Serializable {


    /**
     * uid : 20161215145147158638
     * total : 0.01
     * pay_at : null
     * address : 北京市朝阳区姚家园路
     * name : 董凯坡
     * phone : 88888888
     * telephone : 15888888888
     * zip_code : 888888
     * send_at : null
     * created_at : 2016-12-15 14:51:47
     * finish_at : null
     * status : 1
     * express_no : null
     */

    private String uid;
    private float total;
    @SerializedName("pay_at")
    private PayType payAt;
    private String address;
    private String name;
    private String phone;
    private String telephone;
    @SerializedName("zip_code")
    private String zipCode;
    @SerializedName("send_at")
    private String sendAt;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("finish_at")
    private String finishAt;
    private int status;
    @SerializedName("express_no")
    private String expressNo;
    private OrderItemsResponse items;

    public static OrderEntity toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, OrderEntity.class);
    }

    public static List<OrderEntity> toJSONArrayOrderEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<OrderEntity>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public PayType getPayAt() {
        return payAt;
    }

    public void setPayAt(PayType payAt) {
        this.payAt = payAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSendAt() {
        return sendAt;
    }

    public void setSendAt(String sendAt) {
        this.sendAt = sendAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(String finishAt) {
        this.finishAt = finishAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public OrderItemsResponse getItems() {
        return items;
    }

    public void setItems(OrderItemsResponse items) {
        this.items = items;
    }
}
