package com.iyuile.caelum.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 八字
 * <p/>
 * Created by WangYao on 2016/1/19.
 */
public class BaZiModel {
    /**
     * key : 9jicu4i59d9g8gv9es0s9ak3m4nh48h3
     * userString : 姓名|男|公历|1996|03|12|15|0|否
     * year : 2017
     */
    private String key;
    private String userString;
    private String year;

    public BaZiModel(String key, String name, String sex, String year, String month, String day, String time, String yearNow) {
        this.key = key;
        this.year = yearNow;

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(name + "|");
        stringBuffer.append(sex + "|");
        stringBuffer.append("公历|");
        stringBuffer.append(year + "|");
        stringBuffer.append(month + "|");
        stringBuffer.append(day + "|");
        stringBuffer.append(time + "|");
        stringBuffer.append("0|");
        stringBuffer.append("否");

        this.userString = stringBuffer.toString();
    }

    public static BaZiModel toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, BaZiModel.class);
    }

    public static List<BaZiModel> toJSONArrayBaZiModelFromData(String str) {

        Type listType = new TypeToken<ArrayList<BaZiModel>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserString() {
        return userString;
    }

    public void setUserString(String userString) {
        this.userString = userString;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
