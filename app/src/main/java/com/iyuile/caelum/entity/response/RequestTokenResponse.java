package com.iyuile.caelum.entity.response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 响应的request_token等信息
 */
public class RequestTokenResponse {


    /**
     * token : 5138ab95-51dc-4e57-875e-4edc6d23eece
     * expired_in : 604800
     */

    private DataEntity data;

    public static RequestTokenResponse toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, RequestTokenResponse.class);
    }

    public static List<RequestTokenResponse> toJSONArrayRequestTokenResponseFromData(String str) {

        Type listType = new TypeToken<ArrayList<RequestTokenResponse>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String token;
        private String created_at;
        private String expired_in;

        public static DataEntity toJSONObjectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public static List<DataEntity> toJSONArrayDataEntityFromData(String str) {

            Type listType = new TypeToken<ArrayList<DataEntity>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getExpired_in() {
            return expired_in;
        }

        public void setExpired_in(String expired_in) {
            this.expired_in = expired_in;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
