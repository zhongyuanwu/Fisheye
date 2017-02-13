package com.iyuile.caelum.entity.response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.lzh.framework.updatepluginlib.model.Update;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYao on 2016/12/19.
 */
public class UpdateResponse {
    private Update data;

    public Update getData() {
        return data;
    }

    public void setData(Update data) {
        this.data = data;
    }

    public static UpdateResponse toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, UpdateResponse.class);
    }

    public static List<UpdateResponse> toJSONArrayUpdateResponseFromData(String str) {

        Type listType = new TypeToken<ArrayList<UpdateResponse>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }
}
