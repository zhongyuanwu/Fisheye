package com.iyuile.caelum.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 省市
 * <p/>
 * Created by WangYao on 2016/12/23.
 */
public class ChineseCitiesModel {

    /**
     * name : 北京
     * city : [{"name":"北京","area":["东城区","西城区","崇文区","宣武区","朝阳区","丰台区","石景山区","海淀区","门头沟区","房山区","通州区","顺义区","昌平区","大兴区","平谷区","怀柔区","密云县","延庆县"]}]
     */

    private String name;
    /**
     * name : 北京
     * area : ["东城区","西城区","崇文区","宣武区","朝阳区","丰台区","石景山区","海淀区","门头沟区","房山区","通州区","顺义区","昌平区","大兴区","平谷区","怀柔区","密云县","延庆县"]
     */

    private ArrayList<CityModel> city;

    public static ChineseCitiesModel toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, ChineseCitiesModel.class);
    }

    public static ArrayList<ChineseCitiesModel> toJSONArrayChineseCitiesModelFromData(String str) {

        Type listType = new TypeToken<ArrayList<ChineseCitiesModel>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CityModel> getCity() {
        return city;
    }

    public void setCity(ArrayList<CityModel> city) {
        this.city = city;
    }

    public static class CityModel {
        private String name;
        private ArrayList<String> area;

        public static CityModel toJSONObjectFromData(String str) {

            return new Gson().fromJson(str, CityModel.class);
        }

        public static ArrayList<CityModel> toJSONArrayCityModelFromData(String str) {

            Type listType = new TypeToken<ArrayList<CityModel>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<String> getArea() {
            return area;
        }

        public void setArea(ArrayList<String> area) {
            this.area = area;
        }
    }
}
