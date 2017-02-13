package com.iyuile.caelum.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 地址
 * <p>
 * Created by WangYao on 2016/12/12.
 */
public class AddressEntity implements Serializable {


    /**
     * id : 1
     * name : 董凯坡
     * telephone : 15888888888
     * phone : 88888888
     * address : 北京市朝阳区姚家园路
     * zip_code : 888888
     * status : 1
     * default : 1
     */

    private Long id;
    private String name;
    private String telephone;
    private String phone;
    private String address;
    private String zip_code;
    private int status;
    @SerializedName("default")
    private boolean defaultX;

    public static AddressEntity toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, AddressEntity.class);
    }

    public static List<AddressEntity> toJSONArrayAddressEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<AddressEntity>>() {
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isDefaultX() {
        return defaultX;
    }

    public void setDefaultX(boolean defaultX) {
        this.defaultX = defaultX;
    }
}
