package com.iyuile.caelum.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 订单支付
 */
public class OrderPayModel implements Serializable {

    private String appid;
    private String partnerid;
    private String noncestr;
    private String prepayid;
    @SerializedName("package")
    private String packageValue;
    private String timestamp;
    private String sign;

    public static OrderPayModel toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, OrderPayModel.class);
    }

    public static List<OrderPayModel> toJSONArrayDataEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<OrderPayModel>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
