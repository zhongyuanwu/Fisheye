package com.iyuile.caelum.entity.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 返回云存储token
 */
public class CloudStorageTokenResponse {
    private DataEntity data;

    public static CloudStorageTokenResponse toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, CloudStorageTokenResponse.class);
    }

    public static List<CloudStorageTokenResponse> toJSONArrayCloudStorageTokenResponseFromData(String str) {

        Type listType = new TypeToken<ArrayList<CloudStorageTokenResponse>>() {
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
        @SerializedName("token")
        private String secretToken;

        public static DataEntity toJSONObjectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public static List<DataEntity> toJSONArrayDataEntityFromData(String str) {

            Type listType = new TypeToken<ArrayList<DataEntity>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getSecretToken() {
            return secretToken;
        }

        public void setSecretToken(String secretToken) {
            this.secretToken = secretToken;
        }
    }
}
